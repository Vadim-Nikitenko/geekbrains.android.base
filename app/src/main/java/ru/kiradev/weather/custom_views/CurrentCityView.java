package ru.kiradev.weather.custom_views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import ru.kiradev.weather.R;
import ru.kiradev.weather.model.CityDetailsData;

public class CurrentCityView extends ConstraintLayout {

    private ConstraintLayout currentCityLayout;
    private TextView currentCity;
    private ImageView currentCityIcon;
    private TextView currentCityTemp;

    public CurrentCityView(Context context) {
        super(context);
        initViews(context);
    }

    public CurrentCityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CurrentCityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.current_city_view, this);
        initUI();
    }

    private void initUI() {
        currentCityLayout = this.findViewById(R.id.currentCityLayout);
        currentCity = this.findViewById(R.id.currentCity);
        currentCityIcon = this.findViewById(R.id.currentCityIcon);
        currentCityTemp = this.findViewById(R.id.currentCityTemp);
    }

    public void setupViews(CityDetailsData cityDetailsData, ConstraintLayout drawerMenuHeader) {
        currentCity.setText(cityDetailsData.getCityName());
        Picasso.get().load(cityDetailsData.getIcon()).into(currentCityIcon);
        currentCityTemp.setText(cityDetailsData.getTemperature());
        setBackground(drawerMenuHeader, cityDetailsData);
        invalidate();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setBackground(ConstraintLayout drawerMenuHeader, CityDetailsData cityDetailsData) {
        switch (cityDetailsData.getDefaultIcon()) {
            case "01n": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.n01min));
                break;
            }
            case "01d": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.d01min));
                break;
            }
            case "02n":
            case "04n": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.n02min));
                break;
            }
            case "02d": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.d02min));
                break;
            }
            case "03n":
            case "50n": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.n03min));
                break;
            }
            case "03d": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.d03min));
                break;
            }
            case "04d": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.d04min));
                break;
            }
            case "09d":
            case "10d": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.d09min));
                break;
            }
            case "09n":
            case "10n": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.n09min));
                break;
            }
            case "11d": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.d11min));
                break;
            }
            case "11n": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.n11min));
                break;
            }
            case "50d": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.d50min));
                break;
            }
            case "13d": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.d13min));
                break;
            }
            case "13n": {
                drawerMenuHeader.setBackground(getResources().getDrawable(R.drawable.n13min));
                break;
            }
        }
    }

}
