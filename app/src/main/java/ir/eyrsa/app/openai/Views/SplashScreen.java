package ir.eyrsa.app.openai.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ir.eyrsa.app.openai.Config.Application;
import ir.eyrsa.app.openai.Config.SharedPreferencesManager;
import ir.eyrsa.app.openai.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = SharedPreferencesManager.getSharedPreferences().getString(SharedPreferencesManager.TOKEN,"");
                //This method will be executed once the timer is over
                if (!token.equals(""))
                    startActivity(new Intent(Application.getContext(), MainActivity.class));
                else
                    startActivity(new Intent(Application.getContext(), SettingsActivity.class));
                finish();
            }
        }, 2000);

    }
}