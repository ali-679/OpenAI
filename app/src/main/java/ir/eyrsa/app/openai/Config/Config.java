package ir.eyrsa.app.openai.Config;

import com.google.mlkit.nl.translate.TranslateLanguage;

public class Config {

    public static final String BASE_URL="https://api.openai.com/v1/";
    public static final String TAG="OpenAI";

    public static final String model="text-davinci-003";
    public static final int maxTokens=700;
    public static final double temperature=0.5;

    public static final String persian= TranslateLanguage.PERSIAN;
    public static final String english=TranslateLanguage.ENGLISH;

    public static final String AUTH=SharedPreferencesManager.getSharedPreferences().getString(SharedPreferencesManager.TOKEN,"");

    public static final int REQUEST_CODE_SPEECH = 67;

}
