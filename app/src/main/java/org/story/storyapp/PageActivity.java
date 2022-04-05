package org.story.storyapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class PageActivity extends AppCompatActivity {

    public static String FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

    MyView myView;
    File file;
    File fileStoring;
    File fileInner;
    Vibrator vibrator;
    ImageButton rightPage;
    ImageButton leftPage;
    TextView pageCount;
    Button btnCoverStart;
    ImageButton btnNextStart;
    LinearLayout llCoverPalette;
    LinearLayout llPalette;
    ImageView storyBg;

    int currentItem;
    int pageAllCount;
    int width;
    String fileId;
    View.OnClickListener mclick = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.story_9e2f24:
                    myView.changeColor("#9e2f24");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_de6f38:
                    myView.changeColor("#de6f38");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_7c563f:
                    myView.changeColor("#7c563f");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_c28b62:
                    myView.changeColor("#c28b62");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_ffc7a1:
                    myView.changeColor("#ffc7a1");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_f0c952:
                    myView.changeColor("#f0c952");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_d5dd54:
                    myView.changeColor("#d5dd54");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_72a256:
                    myView.changeColor("#72a256");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_3c7032:
                    myView.changeColor("#3c7032");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_91b8d9:
                    myView.changeColor("#91b8d9");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_4c6bbd:
                    myView.changeColor("#4c6bbd");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_304699:
                    myView.changeColor("#304699");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_6b33b8:
                    myView.changeColor("#6b33b8");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_bd7ce4:
                    myView.changeColor("#bd7ce4");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_d37590:
                    myView.changeColor("#d37590");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_f6cdd5:
                    myView.changeColor("#f6cdd5");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_8c8c8c:
                    myView.changeColor("#8c8c8c");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_1b1b1b:
                    myView.changeColor("#1b1b1b");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.story_c6c6c6:
                    myView.changeColor("#c6c6c6");
                    myView.setDrawingStroke(15);
                    break;
                case R.id.pencil:
                    myView.changeColor("#000000");
                    myView.setDrawingStroke(5);
                    vibrator.vibrate(100);
                    break;
                case R.id.eraser:
                    myView.enableEraser();
                    vibrator.vibrate(100);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);


        int id = getIntent().getIntExtra("id", 0);
        if (id < 10) {
            fileId = "0" + id;
        } else {
            fileId = String.valueOf(id);
        }
        currentItem = id;

        setDeviceRotation();
        initView();
        checkFileOrigin();
        checkNextPage();
        setFirstPage();
        setListeners();

    }

    public void checkFileOrigin() {
        File fileNum = new File(FILE_PATH + "/story"); // 모든 파일 개수
        if (fileNum.exists()) {
            pageAllCount = fileNum.listFiles().length;
        } else {
            Toast.makeText(this, "파일 경로를 확인 해 주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void checkNextPage() {
        if (currentItem + 1 == pageAllCount) {
            rightPage.setVisibility(View.INVISIBLE);
        } else if (currentItem == 0) {
            leftPage.setVisibility(View.INVISIBLE);
        }
    }

    public void setDeviceRotation() {
        //디바이스 회전시 값 유지를 하기위한 코드
        Resources r = Resources.getSystem();
        Configuration config = r.getConfiguration();
        onConfigurationChanged(config);
        //onConfigurationChanged
        //오버라이드를 했을경우 회전시마다 onCreate메소드를 호출하므로
    }

    public void setFirstPage() {
        if (currentItem == 0) {
            setCoverPage("00");
        } else {
            pageCount.setText(String.valueOf(currentItem));
            file = new File(FILE_PATH + "/story/" + fileId + ".png"); // 배경
            fileStoring = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/image/" + fileId + "_" + MySharedPreference.getId(this) + ".png"); // 수정 할 파일
            fileInner = new File(FILE_PATH + "/story_inner/" + fileId + ".png");
            if (fileStoring.exists()) { // 그리던 파일이 있을 때
                myView.setShape(fileInner, file, fileStoring);
            } else {
                myView.setShape(fileInner, file, null);
            }
        }
        myView.setDrawingStroke(5);
        myView.changeColor("#000000");
    }

    public void initView() {
        rightPage = findViewById(R.id.btn_arrow_right);
        leftPage = findViewById(R.id.btn_arrow_left);
        pageCount = findViewById(R.id.tv_page_count);
        //storyBg = findViewById(R.id.iv_canvas);
        btnCoverStart = findViewById(R.id.btn_cover_play);
        llCoverPalette = findViewById(R.id.ll_cover_plette);
        myView = findViewById(R.id.canvas);
        llPalette = findViewById(R.id.ll_pallete);
        btnNextStart = findViewById(R.id.btn_all_next);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        findViewById(R.id.story_9e2f24).setOnClickListener(mclick);
        findViewById(R.id.story_de6f38).setOnClickListener(mclick);
        findViewById(R.id.story_7c563f).setOnClickListener(mclick);
        findViewById(R.id.story_c28b62).setOnClickListener(mclick);
        findViewById(R.id.story_ffc7a1).setOnClickListener(mclick);
        findViewById(R.id.story_f0c952).setOnClickListener(mclick);
        findViewById(R.id.story_d5dd54).setOnClickListener(mclick);
        findViewById(R.id.story_72a256).setOnClickListener(mclick);
        findViewById(R.id.story_3c7032).setOnClickListener(mclick);
        findViewById(R.id.story_91b8d9).setOnClickListener(mclick);

        findViewById(R.id.story_4c6bbd).setOnClickListener(mclick);
        findViewById(R.id.story_304699).setOnClickListener(mclick);
        findViewById(R.id.story_6b33b8).setOnClickListener(mclick);
        findViewById(R.id.story_bd7ce4).setOnClickListener(mclick);
        findViewById(R.id.story_d37590).setOnClickListener(mclick);
        findViewById(R.id.story_f6cdd5).setOnClickListener(mclick);
        findViewById(R.id.story_1b1b1b).setOnClickListener(mclick);
        findViewById(R.id.story_8c8c8c).setOnClickListener(mclick);
        findViewById(R.id.story_c6c6c6).setOnClickListener(mclick);

        findViewById(R.id.pencil).setOnClickListener(mclick);
        findViewById(R.id.eraser).setOnClickListener(mclick);
    }

    public void setCoverPage(String pageNum) {
        pageCount.setText("0");
        if (getIntent().getIntExtra("idCover", -1) == 0) {
            llCoverPalette.setVisibility(View.VISIBLE);
            llPalette.setVisibility(View.INVISIBLE);
            pageCount.setVisibility(View.VISIBLE);
            rightPage.setVisibility(View.VISIBLE);
            leftPage.setVisibility(View.INVISIBLE);
            btnCoverStart.setVisibility(View.INVISIBLE);
            btnNextStart.setVisibility(View.VISIBLE);
        } else {
            llCoverPalette.setVisibility(View.VISIBLE);
            llPalette.setVisibility(View.INVISIBLE);
            pageCount.setVisibility(View.INVISIBLE);
            rightPage.setVisibility(View.INVISIBLE);
            leftPage.setVisibility(View.INVISIBLE);
            btnCoverStart.setVisibility(View.VISIBLE);
            btnNextStart.setVisibility(View.INVISIBLE);
        }
        file = new File(FILE_PATH + "/story/" + pageNum + ".png"); // 배경
        fileStoring = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/image/" + pageNum + "_" + MySharedPreference.getId(this) + ".png"); // 수정 할 파일
        fileInner = new File(FILE_PATH + "/story_inner/" + pageNum + ".png");
        if (fileStoring.exists()) { // 그리던 파일이 있을 때
            myView.setShape(fileInner, file, fileStoring);
        } else {
            myView.setShape(fileInner, file, null);
        }
    }

    public void setPage() {
        myView.clearDrawing();
        pageCount.setText(String.valueOf(1));
        file = new File(FILE_PATH + "/story/" + "01" + ".png"); // 배경
        fileStoring = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/image/" + "01" + "_" + MySharedPreference.getId(this) + ".png"); // 수정 할 파일
        fileInner = new File(FILE_PATH + "/story_inner/" + "01" + ".png");
        if (fileStoring.exists()) { // 그리던 파일이 있을 때
            myView.setShape(fileInner, file, fileStoring);
        } else {
            myView.setShape(fileInner, file, null);
        }
        currentItem = 1;
    }

    public int getStatusBarHeight(Context context) {
        int screenSizeType = (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
        int statusbar = 0;
        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusbar = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusbar;
    }

    public void captureActivity(Activity context, View view) {
        if (context == null) return;
        leftPage.setVisibility(View.INVISIBLE);
        rightPage.setVisibility(View.INVISIBLE);
        pageCount.setVisibility(View.INVISIBLE);
        btnNextStart.setVisibility(View.INVISIBLE);
        btnCoverStart.setVisibility(View.INVISIBLE);
        llCoverPalette.setVisibility(View.INVISIBLE);
        View root = context.getWindow().getDecorView().getRootView();

        root.setDrawingCacheEnabled(true);
        root.buildDrawingCache();
        Bitmap screenshot = root.getDrawingCache();

        int[] location = new int[2];
        root.getLocationInWindow(location);

        Bitmap bmp = Bitmap.createBitmap(screenshot, (int) view.getX(), (int) view.getY() + getStatusBarHeight(this), view.getMeasuredWidth(), view.getMeasuredHeight());
        //new CaptureAsyncTask(this).execute(bmp);
        CanvasIO.saveBitmap(PageActivity.this, bmp, currentItem, "storing");

        if (bmp != null) {
            leftPage.setVisibility(View.VISIBLE);
            rightPage.setVisibility(View.VISIBLE);
            pageCount.setVisibility(View.VISIBLE);
            btnNextStart.setVisibility(View.VISIBLE);
        }
    }


    public void setListeners() {
        findViewById(R.id.btn_cover_pencil).setOnClickListener(view -> {
            myView.setDrawingStroke(5);
            myView.changeColor("#000000");
            vibrator.vibrate(100);
        });
        findViewById(R.id.btn_cover_eraser).setOnClickListener(view -> {
            myView.enableEraser();
            vibrator.vibrate(100);
        });
        findViewById(R.id.btn_cover_play).setOnClickListener(view -> {
            vibrator.vibrate(100);
            llPalette.setVisibility(View.VISIBLE);
            llCoverPalette.setVisibility(View.INVISIBLE);
            pageCount.setVisibility(View.VISIBLE);
            btnCoverStart.setVisibility(View.INVISIBLE);
            rightPage.setVisibility(View.VISIBLE);
            leftPage.setVisibility(View.VISIBLE);
            btnNextStart.setVisibility(View.VISIBLE);


            new SaveAsyncTask(this).execute(myView.getCurrentCanvasColor());
            captureActivity(PageActivity.this, myView);

        });
        findViewById(R.id.btn_all_next).setOnClickListener(view -> {
            vibrator.vibrate(100);

            CanvasIO.saveBitmap(PageActivity.this, myView.getCurrentCanvasColor(), currentItem, "image");
            captureActivity(PageActivity.this, myView);

            finish();
            startActivity(new Intent(PageActivity.this, AllPageActivity.class));

        });

        rightPage.setOnClickListener(view -> {
            vibrator.vibrate(100);
            CanvasIO.saveBitmap(PageActivity.this, myView.getCurrentCanvasColor(), currentItem, "image");
            captureActivity(PageActivity.this, myView);

            llCoverPalette.setVisibility(View.INVISIBLE);
            llPalette.setVisibility(View.VISIBLE);

            String nextId;

            if (currentItem < pageAllCount - 1) {
                myView.clearDrawing();
                currentItem = currentItem + 1;
                pageCount.setText(String.valueOf(currentItem));
                if (currentItem < 10) {
                    nextId = "0" + currentItem;
                } else {
                    nextId = String.valueOf(currentItem);
                }
                file = new File(FILE_PATH + "/story/" + nextId + ".png"); // 배경
                fileStoring = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/image/" + nextId + "_" + MySharedPreference.getId(this) + ".png"); // 수정 할 파일
                fileInner = new File(FILE_PATH + "/story_inner/" + nextId + ".png");
                if (fileStoring.exists()) { // 그리던 파일이 있을 때
                    myView.setShape(fileInner, file, fileStoring);
                } else {
                    myView.setShape(fileInner, file, null);
                }

            }
            if (currentItem + 1 == pageAllCount) {
                rightPage.setVisibility(View.INVISIBLE);
            }


        });
        leftPage.setOnClickListener(view -> {
            vibrator.vibrate(100);
            CanvasIO.saveBitmap(PageActivity.this, myView.getCurrentCanvasColor(), currentItem, "image");
            captureActivity(PageActivity.this, myView);

            if (currentItem != 0) {
                myView.clearDrawing();
                currentItem = currentItem - 1;
                pageCount.setText(String.valueOf(currentItem));
                String nextId;
                if (currentItem < 10) {
                    nextId = "0" + currentItem;
                } else {
                    nextId = String.valueOf(currentItem);

                }
                file = new File(FILE_PATH + "/story/" + nextId + ".png"); // 배경
                fileStoring = new File(FILE_PATH + "/" + MySharedPreference.getId(this) + "/image/" + nextId + "_" + MySharedPreference.getId(this) + ".png"); // 수정 할 파일
                fileInner = new File(FILE_PATH + "/story_inner/" + nextId + ".png");
                if (fileStoring.exists()) { // 그리던 파일이 있을 때
                    myView.setShape(fileInner, file, fileStoring);
                } else {
                    myView.setShape(fileInner, file, null);
                }

            }
            if (currentItem == 0) {
                leftPage.setVisibility(View.INVISIBLE);
                llCoverPalette.setVisibility(View.VISIBLE);
                llPalette.setVisibility(View.INVISIBLE);
                myView.setDrawingStroke(5);
                myView.changeColor("#000000");
            }

        });

    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //현재 디바이스의 방향성을 체크...
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
            case Configuration.ORIENTATION_PORTRAIT:
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (currentItem == 0) {
            finish();
        } else {
            finish();
            startActivity(new Intent(PageActivity.this, AllPageActivity.class));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SaveAsyncTask extends AsyncTask<Bitmap, Void, Boolean> {
        private final Context mContext;
        private ProgressDialog mProgressDialog;

        public SaveAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(PageActivity.this);
            mProgressDialog.setMessage(getString(R.string.save));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Bitmap... bitmaps) {

            try {
                String recordPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + MySharedPreference.getId(mContext) + "/" + "image";
                File file = new File(recordPath);
                file.mkdirs();
                //File file = new File(recordPath+"/"+id+"drawing.png");
                //file.mkdirs();
                //file.createNewFile();
                FileOutputStream fos;
                if (currentItem < 10) {
                    fos = new FileOutputStream(recordPath + "/" + 0 + currentItem + "_" + MySharedPreference.getId(mContext) + ".png");
                } else {
                    fos = new FileOutputStream(recordPath + "/" + currentItem + "_" + MySharedPreference.getId(mContext) + ".png");
                }
                Log.d("qqqqqqq", file.getName());
                bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                return true;
            } catch (Exception ignored) {

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressDialog.dismiss();
            if (aBoolean) {

                setPage();

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class CaptureAsyncTask extends AsyncTask<Bitmap, Void, Boolean> {
        private final Context mContext;
        private ProgressDialog mProgressDialog;

        public CaptureAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(PageActivity.this);
            mProgressDialog.setMessage("파일 저장중");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Bitmap... bitmaps) {

            try {
                String recordPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + MySharedPreference.getId(mContext) + "/" + "storing";
                File file = new File(recordPath);
                file.mkdirs();
                FileOutputStream fos;
                if (currentItem < 10) {
                    fos = new FileOutputStream(recordPath + "/" + 0 + currentItem + "_" + MySharedPreference.getId(mContext) + ".png");
                } else {
                    fos = new FileOutputStream(recordPath + "/" + currentItem + "_" + MySharedPreference.getId(mContext) + ".png");
                }
                bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                return true;
            } catch (Exception ignored) {

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressDialog.dismiss();
            if (aBoolean) {
            }
        }
    }
}