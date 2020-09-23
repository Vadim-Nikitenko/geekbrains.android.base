package ru.kiradev.weather.rest;

import java.io.File;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.kiradev.weather.BuildConfig;
import ru.kiradev.weather.room.App;

public class OpenWeatherRepo {
    private static OpenWeatherRepo instance = null;
    private IOpenWeather API;

    private OpenWeatherRepo() {
        API = createAdapter();
    }

    public static OpenWeatherRepo getInstance() {
        if (instance == null) {
            instance = new OpenWeatherRepo();
            System.out.println(1);
        }
        return instance;
    }

    public IOpenWeather getAPI() {
        return API;
    }

    private IOpenWeather createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient().build())
                .build();

        return adapter.create(IOpenWeather.class);
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        int maxAge = 60; // read from cache for 1 minute
        return originalResponse.newBuilder()
                .header("Cache-Control", "public, max-age=" + maxAge)
                .build();
    };


    // для API 21, чтобы работали запросы
    public OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);

            File httpCacheDirectory = new File(App.getContext().getCacheDir(), "responses");
            int cacheSize = 2 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);

            //add cache to the client
            builder.cache(cache);

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
