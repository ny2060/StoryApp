package org.story.storyapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    public static String FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private final String recordPermission = Manifest.permission.RECORD_AUDIO;
    private final int PERMISSION_CODE = 21;
    File fileAudio;
    File fileBg;
    Vibrator vibrator;
    // 오디오 파일 녹음 관련 변수
    private MediaRecorder mediaRecorder;
    private String audioFileName; // 오디오 녹음 생성 파일 이름
    private boolean isRecording = false;    // 현재 녹음 상태를 확인하기 위함.
    private Uri audioUri = null; // 오디오 파일 uri
    // 오디오 파일 재생 관련 변수
    private MediaPlayer mediaPlayer = null;
    private Boolean isPlaying = false;
    private int selectedRecord = 0;
    private ImageButton imgRecord;
    private ImageButton imgPlay;
    private ImageButton btnAllPage;
    private ImageView imgBg;
    private TextView audioCount;
    private ImageButton rightBtn;
    private ImageButton leftBtn;
    private final Boolean isStop = false;
    private int allAudioNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        imgPlay = findViewById(R.id.btn_play);
        imgRecord = findViewById(R.id.btn_record);
        btnAllPage = findViewById(R.id.btn_all_page);
        imgBg = findViewById(R.id.cl_canvas);
        audioCount = findViewById(R.id.tv_audio_count);
        rightBtn = findViewById(R.id.btn_arrow_right);
        leftBtn = findViewById(R.id.btn_arrow_left);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        selectedRecord = getIntent().getIntExtra("id", 0);
        if (selectedRecord < 10) {
            setAudioNext("0" + selectedRecord);
            audioCount.setText(String.valueOf(selectedRecord));
        } else {
            setAudioNext(String.valueOf(selectedRecord));
            audioCount.setText(String.valueOf(selectedRecord));
        }


        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + "story");
        File[] filenames = dir.listFiles();
        allAudioNum = filenames.length;
        if (selectedRecord == allAudioNum - 1) {
            rightBtn.setVisibility(View.INVISIBLE);
        } else if (selectedRecord == 0) {
            leftBtn.setVisibility(View.INVISIBLE);
        }
        setListeners();
    }

    private void setAudioNext(String selectedRecord) {

        fileBg = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/storing/" + selectedRecord + "_" + MySharedPreference.getId(this) + ".png");
        if (fileBg.exists()) {
            imgBg.setImageBitmap(BitmapFactory.decodeFile(fileBg.toString()));
        } else {
            fileBg = new File(FILE_PATH + "/story/" + selectedRecord + ".png");
            imgBg.setBackgroundColor(Color.WHITE);
            imgBg.setImageBitmap(BitmapFactory.decodeFile(fileBg.toString()));
        }
        // 이미 녹음 된 파일이 있으면 재생만 가능. 없으면 녹음.
        fileAudio = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/record/" + MySharedPreference.getId(this) + "_" + selectedRecord + ".mp4");
        Log.d("audioooo", fileAudio.getPath());
        if (fileAudio.exists()) {
            // 플레이만 가능
            imgPlay.setEnabled(true);
            imgPlay.setImageResource(R.drawable.play);
            isPlaying = true;
            //imgRecord.setImageResource(R.drawable.un_record);
            // imgRecord.setEnabled(false);
        } else {
            // 녹음된게 없음
            imgRecord.setImageResource(R.drawable.record);
            imgRecord.setEnabled(true);

            imgPlay.setImageResource(R.drawable.un_play);
            imgPlay.setEnabled(false);

        }
    }

    private void setListeners() {
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(100);
                if (isRecording) {
                    // 현재 녹음 중 O
                    // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
                    isRecording = false; // 녹음 상태 값
                    imgRecord.setImageResource(R.drawable.record); // 녹음 상태 아이콘 변경
                    //audioRecordImageBtn.setText("녹음 시작");
                    //audioRecordText.setText("녹음 시작"); // 녹음 상태 텍스트 변경
                    stopRecording();
                    // 녹화 이미지 버튼 변경 및 리코딩 상태 변수값 변경
                    imgRecord.setTag("end");

                    //imgRecord.setEnabled(false);
                    //imgRecord.setImageResource(R.drawable.un_record);
                    imgPlay.setEnabled(true);
                    imgPlay.setImageResource(R.drawable.play);
                    if (imgPlay.isEnabled() == true) {
                        isPlaying = true;
                    }

                }
                leftBtn.setVisibility(View.VISIBLE);
                if (selectedRecord < allAudioNum - 1) {

                    selectedRecord += 1;
                    if (selectedRecord < 10) {
                        setAudioNext("0" + selectedRecord);
                    } else {
                        setAudioNext(String.valueOf(selectedRecord));
                    }
                    audioCount.setText(String.valueOf(selectedRecord));
                }
                if (selectedRecord == allAudioNum - 1) {
                    rightBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(100);
                if (isRecording) {
                    // 현재 녹음 중 O
                    // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
                    isRecording = false; // 녹음 상태 값
                    imgRecord.setImageResource(R.drawable.record); // 녹음 상태 아이콘 변경
                    //audioRecordImageBtn.setText("녹음 시작");
                    //audioRecordText.setText("녹음 시작"); // 녹음 상태 텍스트 변경
                    stopRecording();
                    // 녹화 이미지 버튼 변경 및 리코딩 상태 변수값 변경
                    imgRecord.setTag("end");

                    //imgRecord.setEnabled(false);
                    //imgRecord.setImageResource(R.drawable.un_record);
                    imgPlay.setEnabled(true);
                    imgPlay.setImageResource(R.drawable.play);
                    if (imgPlay.isEnabled() == true) {
                        isPlaying = true;
                    }

                }
                rightBtn.setVisibility(View.VISIBLE);
                if (selectedRecord != 0) {

                    selectedRecord -= 1;
                    if (selectedRecord < 10) {
                        setAudioNext("0" + selectedRecord);
                    } else {
                        setAudioNext(String.valueOf(selectedRecord));
                    }
                    audioCount.setText(String.valueOf(selectedRecord));
                }
                if (selectedRecord == 0) {
                    leftBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnAllPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(AudioActivity.this, AllPageActivity.class));
            }
        });
        findViewById(R.id.btn_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRecord();
            }
        });
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 녹음 된 파일이 있으면 재생한다.
                String uriName;
                if (selectedRecord < 10) {
                    uriName = FILE_PATH + "/" + MySharedPreference.getId(AudioActivity.this) + "/record/" + MySharedPreference.getId(AudioActivity.this) + "_" + "0" + selectedRecord + ".mp4";

                } else {
                    uriName = FILE_PATH + "/" + MySharedPreference.getId(AudioActivity.this) + "/record/" + MySharedPreference.getId(AudioActivity.this) + "_" + selectedRecord + ".mp4";
                }
                File file = new File(uriName);

                if (isPlaying) {
                    playAudio(file);
                } else {
                    stopAudio();
                }


            }
        });
    }

    public void startRecord() {

        if (isRecording) {
            // 현재 녹음 중 O
            // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
            isRecording = false; // 녹음 상태 값
            imgRecord.setImageResource(R.drawable.record); // 녹음 상태 아이콘 변경
            //audioRecordImageBtn.setText("녹음 시작");
            //audioRecordText.setText("녹음 시작"); // 녹음 상태 텍스트 변경
            stopRecording();
            // 녹화 이미지 버튼 변경 및 리코딩 상태 변수값 변경
            imgRecord.setTag("end");

            //imgRecord.setEnabled(false);
            //imgRecord.setImageResource(R.drawable.un_record);
            imgPlay.setEnabled(true);
            imgPlay.setImageResource(R.drawable.play);
            if (imgPlay.isEnabled() == true) {
                isPlaying = true;
            }

        } else {
            // 현재 녹음 중 X
            /*절차
             *       1. Audio 권한 체크
             *       2. 처음으로 녹음 실행한건지 여부 확인
             * */
            if (checkAudioPermission()) {
                // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
                isRecording = true; // 녹음 상태 값
                imgRecord.setImageResource(R.drawable.recording);
                imgRecord.setTag("ing");

                imgPlay.setImageResource(R.drawable.un_play);
                imgPlay.setEnabled(false);
                // audioRecordImageBtn.setText("녹음 중");
                // audioRecordText.setText("녹음 중"); // 녹음 상태 텍스트 변경
                startRecording();
            }
        }
    }

    // 녹음 파일 재생
    private void playAudio(File file) {
        // imgPlay.setEnabled(false);
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgRecord.setEnabled(false);
        imgRecord.setImageResource(R.drawable.un_record);
        imgPlay.setImageResource(R.drawable.playing);
        isPlaying = false;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
            }
        });

    }

    // 녹음 파일 중지
    private void stopAudio() {
        //imgPlay.setEnabled(true);
        imgPlay.setImageResource(R.drawable.play);
        imgRecord.setEnabled(true);
        imgRecord.setImageResource(R.drawable.record);
        isPlaying = true;
        mediaPlayer.stop();
    }


    // 녹음 종료
    private void stopRecording() {
        // 녹음 종료 종료
        imgRecord.setTag("end");

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        // 파일 경로(String) 값을 Uri로 변환해서 저장
        //      - Why? : 리사이클러뷰에 들어가는 ArrayList가 Uri를 가지기 때문
        //      - File Path를 알면 File을  인스턴스를 만들어 사용할 수 있기 때문
        audioUri = Uri.parse(audioFileName);

        // 저장 후에 재생 버튼 활성화


    }


    // 녹음 시작
    private void startRecording() {

        //파일의 외부 경로 확인
        String recordPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + MySharedPreference.getId(this) + "/" + "record";
        File file = new File(recordPath);
        file.mkdirs();
        if (selectedRecord < 10) {
            audioFileName = recordPath + "/" + MySharedPreference.getId(this) + "_" + "0" + selectedRecord + ".mp4";
        } else {
            audioFileName = recordPath + "/" + MySharedPreference.getId(this) + "_" + selectedRecord + ".mp4";

        }
        //file.listFiles();
        //Media Recorder 생성 및 설정
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(audioFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //녹음 시작
        mediaRecorder.start();
    }

    // 오디오 파일 권한 체크
    private boolean checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }
}