package com.example.geekbrainsandroidweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CitiesActivity extends AppCompatActivity {
    private EditText cityInput;
    private Button applyCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        init();
    }

    private void init() {
        cityInput = findViewById(R.id.cityInput);
        setOnEditTextActionBehaviour();
        applyCity = findViewById(R.id.applyButton);
        setOnApplyButtonActionBehaviour();
        TextView city1 = findViewById(R.id.city1);
        setOnTextViewClickBehaviour(city1);
        TextView city2 = findViewById(R.id.city2);
        setOnTextViewClickBehaviour(city2);
        TextView city3 = findViewById(R.id.city3);
        setOnTextViewClickBehaviour(city3);
    }

    private void setOnEditTextActionBehaviour() {
        cityInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    changeToMainActivity();
                }
                return false;
            }
        });
    }

    private void setOnApplyButtonActionBehaviour() {
        applyCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToMainActivity();
            }
        });
    }

    private void setOnTextViewClickBehaviour(final TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityInput.setText(textView.getText());
            }
        });
    }

    private void changeToMainActivity() {
        if (!cityInput.getText().toString().isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), R.string.enterTheCity, Toast.LENGTH_SHORT).show();
        }
    }

}
