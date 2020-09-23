package ru.kiradev.weather.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import ru.kiradev.weather.R;

public class PressureView extends ConstraintLayout {
    private TextView pressureTextView;
    private TextView cloudyTextView;
    private ImageView pressureImageView;

    public PressureView(Context context) {
        super(context);
        initViews(context);
    }

    public PressureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public PressureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pressure_view, this);
        initUI();
        setupImageViewAnimation();
    }

    private void initUI() {
        pressureTextView = this.findViewById(R.id.pressureTextView);
        cloudyTextView = this.findViewById(R.id.cloudyTextView);
        pressureImageView = this.findViewById(R.id.pressureImageView);
    }

    public void setupTexts(String pressure, String cloudy) {
        pressureTextView.setText(pressure);
        cloudyTextView.setText(cloudy);
        invalidate();
    }

    private void setupImageViewAnimation() {
        pressureImageView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pulse));
    }

}