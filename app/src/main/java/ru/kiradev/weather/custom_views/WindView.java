package ru.kiradev.weather.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import ru.kiradev.weather.R;

public class WindView extends ConstraintLayout {
    private TextView windSpTextView;
    private TextView windDegreesTextView;
    private ImageView windImageView;

    public WindView(Context context) {
        super(context);
        initViews(context);
    }

    public WindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public WindView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.wind_view, this);
        initUI();
        setupImageViewAnimation();
    }

    private void initUI() {
        windSpTextView = this.findViewById(R.id.windSpTextView);
        windDegreesTextView = this.findViewById(R.id.windDegreesTextView);
        windImageView = this.findViewById(R.id.windImageView);
    }

    public void setupTexts(String humidity, String degrees) {
        windSpTextView.setText(humidity);
        windDegreesTextView.setText(degrees);
        invalidate();
    }

    private void setupImageViewAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        windImageView.startAnimation(rotateAnimation);
    }

}