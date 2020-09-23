package ru.kiradev.weather.custom_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ru.kiradev.weather.R;
import ru.kiradev.weather.model.CityDetailsData;

public class FavoriteCityView extends ConstraintLayout {

    private ConstraintLayout favoriteCityLayout;
    private TextView favoriteTime;
    private TextView favoriteCity;
    private ImageView favoriteCityIcon;
    private TextView favoriteCityTemp;
    private ImageButton favoriteMenuItem;

    public FavoriteCityView(Context context) {
        super(context);
        initViews(context);
    }

    public FavoriteCityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public FavoriteCityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.favorite_city_view, this);
        initUI();
    }

    private void initUI() {
        favoriteCityLayout = this.findViewById(R.id.favoriteCityLayout);
        favoriteTime = this.findViewById(R.id.favoriteTime);
        favoriteCity = this.findViewById(R.id.favoriteCity);
        favoriteCityIcon = this.findViewById(R.id.favoriteCityIcon);
        favoriteCityTemp = this.findViewById(R.id.favoriteCityTemp);
        favoriteMenuItem = this.findViewById(R.id.favoriteMenuItem);
    }

    public void setupViews(CityDetailsData cityDetailsData) {
        favoriteTime.setText(parseTime(cityDetailsData.getShift()));
        favoriteCity.setText(cityDetailsData.getCityName());
        Picasso.get().load(cityDetailsData.getIcon()).into(favoriteCityIcon);
        favoriteCityTemp.setText(cityDetailsData.getTemperature());
        setBackground(cityDetailsData);
        invalidate();

    }

    private String parseTime(long shift) {
        Calendar currentTime = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getDefault();
        currentTime.add(Calendar.SECOND, (int) shift);
        currentTime.add(Calendar.MILLISECOND, -timeZone.getRawOffset());
        Date date = currentTime.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setBackground(CityDetailsData cityDetailsData) {
        switch (cityDetailsData.getDefaultIcon()) {
            case "01n": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.n01min));
                break;
            }
            case "01d": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.d01min));
                break;
            }
            case "02n":
            case "04n": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.n02min));
                break;
            }
            case "02d": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.d02min));
                break;
            }
            case "03n":
            case "50n": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.n03min));
                break;
            }
            case "03d": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.d03min));
                break;
            }
            case "04d": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.d04min));
                break;
            }
            case "09d":
            case "10d": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.d09min));
                break;
            }
            case "09n":
            case "10n": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.n09min));
                break;
            }
            case "11d": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.d11min));
                break;
            }
            case "11n": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.n11min));
                break;
            }
            case "50d": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.d50min));
                break;
            }
            case "13d": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.d13min));
                break;
            }
            case "13n": {
                favoriteCityLayout.setBackground(getResources().getDrawable(R.drawable.n13min));
                break;
            }
        }
    }
}
