package com.example.geekbrainsandroidweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class CitiesActivity extends AppCompatActivity {
    private EditText cityInput;
    private Button applyCity;
    public final static String cityDataKey = "cityDataKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        init();
        showBackBtn();
    }

    // инициализация views и установка слушателей событий
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

    // слшатель клавиатурной кнопки OK
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

    // сохранение данных для передачи в другую активити
    private void setOnApplyButtonActionBehaviour() {
        applyCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strData = cityInput.getText().toString();
                if (!strData.equals("")) {
                    Intent dataIntent = new Intent();
                    dataIntent.putExtra(cityDataKey, strData);
                    setResult(RESULT_OK, dataIntent);
                    changeToMainActivity();
                }  else {
                    Toast.makeText(getApplicationContext(), R.string.enterTheCity, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // установка города по клику на TextView
    private void setOnTextViewClickBehaviour(final TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityInput.setText(textView.getText());
            }
        });
    }

    // finish текущей активити если город не пустой
    private void changeToMainActivity() {
        if (!cityInput.getText().toString().isEmpty()) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.enterTheCity, Toast.LENGTH_SHORT).show();
        }
    }

    // показ onBackPressed в меню
    private void showBackBtn() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    // finish текущей активити по клику на onBackPressed в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
