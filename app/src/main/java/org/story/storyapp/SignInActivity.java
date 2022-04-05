package org.story.storyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Locale;

public class SignInActivity extends AppCompatActivity {

    private final String readPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int PERMISSION_CODE = 100;
    private RadioGroup radioGroup;
    private EditText etId;
    private RadioButton ko, en;
    private Button btnSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        radioGroup = findViewById(R.id.radioGroup);
        etId = findViewById(R.id.et_id);
        ko = findViewById(R.id.rb_ko);
        en = findViewById(R.id.rb_en);
        btnSign = findViewById(R.id.btn_start);
        etId = findViewById(R.id.et_id);

        if (getIntent().getStringExtra("lan") != null) {
            if (getIntent().getStringExtra("lan").equals("ko")) {
                ko.setChecked(true);
            } else {
                en.setChecked(true);
            }

        }
        setListeners();
    }


    public void setListeners() {


        btnSign.setOnClickListener(view -> {
            String id = etId.getText().toString();
            if (id.isEmpty()) {
                Toast myToast = Toast.makeText(this.getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT);
                myToast.show();
            } else {
                // id 로 내부에 저장소 만들기
                if (checkDirPermission()) {
                    String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + id;
                    File file = new File(dirPath);
                    if (!file.exists()) {
                        // 처음 유저
                        file.mkdirs();
                        startActivity(new Intent(SignInActivity.this, PageActivity.class));
                        MySharedPreference.setId(SignInActivity.this, id);
                    } else {
                        // 기존 유저
                        startActivity(new Intent(SignInActivity.this, PageActivity.class));
                        MySharedPreference.setId(SignInActivity.this, id);
                    }
                }
            }


        });


        radioGroup.setOnCheckedChangeListener((radioGroup, check) -> {
            switch (check) {
                case R.id.rb_ko:
                    languageInit();
                    setSystemLanguage("ko");
                    Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
                    intent.putExtra("lan", "ko");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    break;
                case R.id.rb_en:
                    languageInit();
                    setSystemLanguage("en");
                    Intent intent2 = new Intent(SignInActivity.this, SignInActivity.class);
                    intent2.putExtra("lan", "en");
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    overridePendingTransition(0, 0);

                    break;
            }
        });

    }


    private void languageInit() {
        String language = getSystemLanguage();
        MySharedPreference.setLan(this, language);
    }

    public String getSystemLanguage() {

        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();

        if (!language.equals("ko")) {
            return "en";
        }
        return language;
    }

    public void setSystemLanguage(String language) {

        Locale locale;

        if (language.equals("ko")) {
            locale = new Locale("ko");
            MySharedPreference.setLan(this, "ko");
        } else {
            locale = new Locale("en");
            MySharedPreference.setLan(this, "en");
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("kcn", "권한 허용 : " + permissions[i]);
                }
            }
        }
    }

    private boolean checkDirPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), readPermission) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), writePermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{readPermission, writePermission}, PERMISSION_CODE);
            return false;
        }
    }

}