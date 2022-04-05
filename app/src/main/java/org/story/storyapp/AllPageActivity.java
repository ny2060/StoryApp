package org.story.storyapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class AllPageActivity extends AppCompatActivity {


    public static String FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    ArrayList<Page> storyList = new ArrayList<>();
    ImageView playIcon;
    private AudioAdapter audioAdapter;
    private int selectedRecord = 0;
    private Boolean isPlaying = false;
    private MediaPlayer mediaPlayer = null;
    private RecyclerView audioRecyclerView;
    private ProgressDialog mProgressDialog;

    public static void sort(File[] filterResult) {
        // 파일명으로 정렬한다.
        Arrays.sort(filterResult, new Comparator() {

            public int compare(Object arg0, Object arg1) {

                File file1 = (File) arg0;

                File file2 = (File) arg1;

                return file1.getName().compareToIgnoreCase(file2.getName());

            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_page);
        audioRecyclerView = findViewById(R.id.rv_audio_page);
        initAdapter();
        setListeners();
        setAudioListeners();
    }

    public void setAudioListeners() {
        audioAdapter.setOnRecordClickListener((view, position) -> selectedRecord = position);
        audioAdapter.setOnMoveClickListener((view, position) -> {
            // 개별 페이지로 이동
            Intent intent = new Intent(AllPageActivity.this, PageActivity.class);
            intent.putExtra("id", position);
            intent.putExtra("idCover", position);
            startActivity(intent);
        });
    }

    public void startAudioMerge(ArrayList<String> file) {
        ArrayList<Movie> inMovies = new ArrayList<>();

        try {
            for (int i = 0; i < file.size(); i++) {
                inMovies.add(MovieCreator.build(new FileDataSourceImpl(file.get(i))));
            }
            // 오디오 파일 구분
            List<Track> audioTracks = new LinkedList<>();
            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
                    if (t.getHandler().equals("soun")) {
                        audioTracks.add(t);
                    }
                }
            }

            Movie output = new Movie();
            if (audioTracks.size() > 0) {
                try {
                    output.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Container out = new DefaultMp4Builder().build(output);
            FileChannel fc = null;
            try {
                fc = new FileOutputStream(FILE_PATH + "/" + MySharedPreference.getId(this) + "/" + "allStoryAudio.mp4").getChannel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                out.writeContainer(fc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert fc != null;
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException ignored) {

        }

    }

    public void setListeners() {
        findViewById(R.id.btn_submit).setOnClickListener(view -> {
            // 제출하기 버튼 , 오디오 파일 & Pdf 파일
            new SaveAll().execute();
        });
        findViewById(R.id.btn_record).setOnClickListener(view -> {
            // 아무것도 선택이 안되어있으면 처음부터
            Intent intent = new Intent(AllPageActivity.this, AudioActivity.class);
            intent.putExtra("id", selectedRecord);
            startActivity(intent);
        });

        audioAdapter.setOnItemClickListener((view, position) -> {

            String uriName = String.valueOf(storyList.get(position).record);
            selectedRecord = position;

            if (!uriName.equals("")) {

                File file = new File(uriName);
                if (isPlaying) {
                    // 음성 녹화 파일이 여러개를 클릭했을 때 재생중인 파일의 Icon을 비활성화(비 재생중)으로 바꾸기 위함.
                    if (playIcon == view) {
                        // 같은 파일을 클릭했을 경우
                        stopAudio();
                    } else {
                        // 다른 음성 파일을 클릭했을 경우
                        // 기존의 재생중인 파일 중지
                        stopAudio();

                        // 새로 파일 재생하기
                        playIcon = (ImageView) view;
                        playAudio(file);
                    }
                } else {
                    playIcon = (ImageView) view;
                    playAudio(file);
                }
            }
        });

    }

    // 녹음 파일 재생
    private void playAudio(File file) {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isPlaying = true;
        mediaPlayer.setOnCompletionListener(mp -> stopAudio());

    }

    // 녹음 파일 중지
    private void stopAudio() {
        isPlaying = false;
        mediaPlayer.stop();
    }

    public void layoutToImage(File[] files) {
        String dirpath;

        try {
            Document document = new Document();
            dirpath = FILE_PATH + "/" + MySharedPreference.getId(this);

            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/allStoryPdf.pdf"));
            document.open();

            for (int i = 0; i < files.length; i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(files[i].toString());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                files[i].createNewFile();
                FileOutputStream fo = new FileOutputStream(files[i]);
                fo.write(bytes.toByteArray());


                Image image = Image.getInstance(files[i].toString());
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / image.getWidth()) * 80;
                image.scalePercent(scaler);
                image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                document.add(image);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void initAdapter() {
        // 내 저장소에서 사진과 오디오 불러오기
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + "story");
        File[] filenames = dir.listFiles();

        File dirRecord = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + MySharedPreference.getId(this) + "/" + "record");
        File[] recordnames = dirRecord.listFiles();

        sort(filenames);
        if (dirRecord.exists()) {
            sort(recordnames);
        }
        storyList.clear();
        String id;
        for (int i = 0; i < filenames.length; i++) {
            if (i < 10) {
                id = "0" + i;
            } else {
                id = String.valueOf(i);
            }
            File storing = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/storing/" + id + "_" + MySharedPreference.getId(this) + ".png");
            if (storing.exists()) {
                File fileRecord = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/record/" + MySharedPreference.getId(this) + "_" + id + ".mp4");
                if (fileRecord.exists()) {
                    storyList.add(new Page(storing.toString(), fileRecord.toString()));
                } else {
                    storyList.add(new Page(storing.toString(), ""));
                }
            } else {
                File fileRecord = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/record/" + MySharedPreference.getId(this) + "_" + id + ".mp4");
                if (fileRecord.exists()) {
                    storyList.add(new Page(filenames[i].toString(), fileRecord.toString()));
                } else {
                    storyList.add(new Page(filenames[i].toString(), ""));
                }
            }
        }

        audioAdapter = new AudioAdapter(this, storyList);
        audioRecyclerView.setAdapter(audioAdapter);
        GridLayoutManager mLayoutManager = new GridLayoutManager(AllPageActivity.this, 4);
        //mLayoutManager.setItemPrefetchEnabled(false);
        audioRecyclerView.setLayoutManager(mLayoutManager);
        audioAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }*/

    @SuppressLint("StaticFieldLeak")
    private class SaveAll extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(AllPageActivity.this);
            mProgressDialog.setMessage("saving....");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
            if (result != null) {
                Toast.makeText(AllPageActivity.this, getString(R.string.pdf_success), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            File dir = new File(FILE_PATH + "/" + MySharedPreference.getId(AllPageActivity.this) + "/record");
            if (dir.exists()) {
                File[] files = dir.listFiles();
                sort(files);
                ArrayList<String> filePath = new ArrayList<>();
                for (File file : files) {
                    filePath.add(file.toString());
                }
                startAudioMerge(filePath);
            }
            // pdf 만들기
            File dirPdf = new File(FILE_PATH + "/" + MySharedPreference.getId(AllPageActivity.this) + "/storing");
            if (dirPdf.exists()) {
                File[] filesPdf = dirPdf.listFiles();
                sort(filesPdf);
                layoutToImage(filesPdf);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //
            }
            return true;
        }
    }
}
