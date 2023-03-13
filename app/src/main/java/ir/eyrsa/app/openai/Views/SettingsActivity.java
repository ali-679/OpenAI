package ir.eyrsa.app.openai.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import ir.eyrsa.app.openai.Config.SharedPreferencesManager;
import ir.eyrsa.app.openai.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_token;
    MaterialButton button_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

    }

    private void init()
    {
        editText_token=findViewById(R.id.editText_token);
        button_save=findViewById(R.id.button_save);

        click();

    }

    private void click() {
        button_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==button_save)
        {
            // save token to sharedpred
            String token=editText_token.getText().toString().trim();
            SharedPreferencesManager.getSharedPreferences().edit().putString(SharedPreferencesManager.TOKEN,token).apply();
        }
    }
}