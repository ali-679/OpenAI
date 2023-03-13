package ir.eyrsa.app.openai.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.HashMap;

import ir.eyrsa.app.openai.Config.Application;
import ir.eyrsa.app.openai.Config.Config;
import ir.eyrsa.app.openai.Model.BodyRecieveModel;
import ir.eyrsa.app.openai.Model.BodySendModel;

import ir.eyrsa.app.openai.Model.Choices;
import ir.eyrsa.app.openai.R;
import ir.eyrsa.app.openai.Retrofit.IRetrofit;
import ir.eyrsa.app.openai.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_input;

    MaterialButton button_record, button_translate, button_chat, button_paly,
            button_load, button_save, button_setting;

    TextView textView_input, textView_answerEng, textView_answerPers;


    IRetrofit iRetrofit;

    BodySendModel bodySendModel;

    String prompt;
    HashMap<String, String> header = new HashMap<>();

    TranslatorOptions translatorOptions;
    Translator translator;

    ProgressDialog progressDialog;

    private static final String TAG = "MAIN_ACTIVITY";

    boolean source = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        click();

    }

    private void init() {
        editText_input = findViewById(R.id.editText_input);

        button_record = findViewById(R.id.button_record);
        button_translate = findViewById(R.id.button_translate);
        button_chat = findViewById(R.id.button_chat);
        button_paly = findViewById(R.id.button_paly);
        button_load = findViewById(R.id.button_load);
        button_save = findViewById(R.id.button_save);
        button_setting = findViewById(R.id.button_setting);

        textView_input = findViewById(R.id.textView_input);
        textView_answerEng = findViewById(R.id.textView_answerEng);
        textView_answerPers = findViewById(R.id.textView_answerPers);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("لطفا صبر کنید...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

//        editText_input.setText("list me the 10 famouse person in world");
//        editText_input.setText("10 شخص معروف دنیا را برای من فهرست کنید");


        iRetrofit = RetrofitClient.getRetrofit(Config.BASE_URL).create(IRetrofit.class);

        header.put("Authorization", Config.AUTH);

//        myModel=new MyModel("text-davinci-003","list me the 10 famouse person in world",
//                700,0.5);


    }

    private void click() {
        button_translate.setOnClickListener(this);
        button_record.setOnClickListener(this);
        button_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == button_translate) {
            if (!editText_input.getText().toString().equals(""))
                initTranslate();
        } else if (view == button_record) {
            speechToText();
        } else if (view == button_setting) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
    }

    private void initTranslate() {
        progressDialog.setTitle("دریافت اطلاعات از سرور...");
        progressDialog.show();

        source = true;

        prompt = editText_input.getText().toString().trim();

        translate(Config.persian, Config.english, prompt, textView_input);

//        bodySendModel = new BodySendModel(Config.model, prompt, Config.maxTokens, Config.temperature);
//
//        getFromAi(header, bodySendModel);
    }

    private void getFromAi(HashMap<String, String> header, BodySendModel bodySendModel) {
        iRetrofit.ai(header, bodySendModel).enqueue(new Callback<BodyRecieveModel>() {
            @Override
            public void onResponse(Call<BodyRecieveModel> call, Response<BodyRecieveModel> response) {
                Choices[] choices = response.body().getChoices();
                textView_answerEng.setText(choices[0].getText());
                translate(Config.english, Config.persian, choices[0].getText(), textView_answerPers);
            }

            @Override
            public void onFailure(Call<BodyRecieveModel> call, Throwable t) {
//                textView_answerEng.setText("error " + t.getMessage());
                getFromAi(header, bodySendModel);
            }
        });
    }

    private void translate(String sourceLanguage, String destinationLanguage, String sourceText, TextView textView) {
        progressDialog.setTitle("Progress language model...");
//        progressDialog.show();
        translatorOptions = new TranslatorOptions.Builder().
                setSourceLanguage(sourceLanguage)
                .setTargetLanguage(destinationLanguage)
                .build();

        translator = Translation.getClient(translatorOptions);

        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(downloadConditions).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: model ready, starting translate...");
                        progressDialog.setTitle("Translating...");

                        translator.translate(sourceText)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {
                                        Log.d(TAG, "onSuccess: TranslatedText: " + translatedText);

                                        textView.setText(translatedText);
                                        if (source) {
                                            bodySendModel = new BodySendModel(Config.model, translatedText, Config.maxTokens, Config.temperature);

                                            getFromAi(header, bodySendModel);
                                            source = false;
                                        } else
                                            progressDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        progressDialog.dismiss();
                                        Log.e(TAG, "onFailure: ", e);
                                        Toast.makeText(MainActivity.this, "Failed to translate due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.e(TAG, "onFailure: ", e);
                        Toast.makeText(MainActivity.this, "Failed to ready model due to " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void speechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Config.persian);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "صحبت کنید ...");

        try {
            startActivityForResult(intent, Config.REQUEST_CODE_SPEECH);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Config.REQUEST_CODE_SPEECH) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText_input.setText(result.get(0));
                    initTranslate();
                }
            }
        }
    }

}
