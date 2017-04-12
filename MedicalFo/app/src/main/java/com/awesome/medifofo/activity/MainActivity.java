package com.awesome.medifofo.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.medifofo.Person;
import com.awesome.medifofo.R;
import com.awesome.medifofo.adapter.GridAdapter;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Eunsik on 03/26/2017.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridView;
    Button symptom_main_btn1;
    Button symptom_main_btn2;
    Button symptom_main_btn3;
    Button find_hospital;
    Button more_confirm;
    CheckBox detail_symptom_cb1;
    CheckBox detail_symptom_cb2;
    CheckBox detail_symptom_cb3;
    private String tmp_st_main;
    private int currentPosition;
    private int tmp_st_scale;
    private StringBuilder tmp_st_sub;
    private EditText tmp_st_comment;
    private Dialog levelDialog;
    private Dialog moreDialog;
    private long pressTime;

    private int image[] = {
            R.drawable.head, R.drawable.face, R.drawable.eye, R.drawable.nouse, R.drawable.ear, R.drawable.tongue,
            R.drawable.jaw, R.drawable.neck, R.drawable.breast, R.drawable.belly, R.drawable.back, R.drawable.spine,
            R.drawable.arm, R.drawable.elbow, R.drawable.hand, R.drawable.finger, R.drawable.leg,
            R.drawable.hip, R.drawable.ankle, R.drawable.sole, R.drawable.man, R.drawable.woman,
            R.drawable.digestive, R.drawable.respiratory, R.drawable.heart
    };

    final String[ /* For UI */][ /* For Naver Maps */] st_place = {
            {"머리", "내과"},
            {"얼굴", "외과"},
            {"식도", "내과"},
            {"가슴", ""},
            {"배", ""},
            {"등", "정형외과"},
            {"다리", "정형외과"},
            {"팔", "정형외과"},
            {"발", "정형외과"},
            {"위", "내과"},
            {"폐", "내과"},
            {"손", "외과"},
            {"간", "내과"},
            {"엉덩이", "비뇨기과"},
            {"턱", "내과"},
            {"치아", "치과"},
            {"생식기 (남자)", ""},
            {"생식기 (여자)", ""},
            {"목", "소아과"},
            {"코", "소아과"},
            {"발바닥", "피부과"},
            {"손가락", "정형외과"},
            {"혀", "내과"},
            {"척추", "정형외과"},
            {"귀", "이비인후과"},
            {"팔근육", "정형외과"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView myPicture = (ImageView) findViewById(R.id.myPicture);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.sharedPreferenceFile, 0);
        new ImageLoadTask(sharedPreferences.getString("URL", ""), myPicture).execute();

        TextView myName = (TextView) findViewById(R.id.myName);
        myName.setText(sharedPreferences.getString("NAME", ""));

        TextView myAge = (TextView) findViewById(R.id.myAge);
        if (PersonalInputActivity.userAge == null) {
            SharedPreferences sf = getSharedPreferences(PersonalInputActivity.sfYear, 1);
            myAge.setText(sf.getString("YEAR", "") + " years old");
        } else {
            myAge.setText(PersonalInputActivity.userAge + " years old");
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        /* Dialog 부분
        levelDialog = new Dialog(getApplicationContext());
        levelDialog.setTitle("Select level:");
        levelDialog.setContentView(R.layout.dialog_evaluation);

        moreDialog = new Dialog(getApplicationContext());
        moreDialog.setContentView(R.layout.dialog_more);
        */


        final GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), R.layout.row, image);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.invalidateViews();
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                /*
                ((TextView) levelDialog.findViewById(R.id.evaluation_title)).setText(st_place[position][0]);
                currentPosition = position;
                levelDialog.show();
                */
                Intent intent = new Intent(getApplicationContext(), SymptomListActivity.class);
                intent.putExtra("POSITION", position); // Pass gridview's position
                startActivity(intent);
            }
        });

        /*
        symptom_main_btn1 = (Button) levelDialog.findViewById(R.id.symptom_main_btn1);
        symptom_main_btn2 = (Button) levelDialog.findViewById(R.id.symptom_main_btn2);
        symptom_main_btn3 = (Button) levelDialog.findViewById(R.id.symptom_main_btn3);
        find_hospital = (Button) levelDialog.findViewById(R.id.find_hospital);

        more_confirm = (Button) moreDialog.findViewById(R.id.more_confirm);
        more_confirm.setOnClickListener(this);

        symptom_main_btn1.setOnClickListener(this);
        symptom_main_btn2.setOnClickListener(this);
        symptom_main_btn3.setOnClickListener(this);
        find_hospital.setOnClickListener(this);

        detail_symptom_cb1 = (CheckBox) moreDialog.findViewById(R.id.checkbox_btn1);
        detail_symptom_cb2 = (CheckBox) moreDialog.findViewById(R.id.checkbox_btn2);
        detail_symptom_cb3 = (CheckBox) moreDialog.findViewById(R.id.checkbox_btn3);

        tmp_st_comment = (EditText) moreDialog.findViewById(R.id.symptom_comment);
        tmp_st_scale = 10;

        */
        /* SeekBar 부분, 통증 선택하기 */
        /*
        final TextView levelTxt = (TextView) levelDialog.findViewById(R.id.level_txt);
        final SeekBar levelSeek = (SeekBar) levelDialog.findViewById(R.id.level_seek);

        levelSeek.setMax(10);
        levelSeek.setProgress(10);

        levelSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //change to progress
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                levelTxt.setText(Integer.toString(progress));
            }

            //methods to implement but not necessary to amend
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        */

        /*
        final TextView moreTxt = (TextView) moreDialog.findViewById(R.id.more_txt);
        final SeekBar moreSeek = (SeekBar) moreDialog.findViewById(R.id.more_seek);

        moreSeek.setMax(10);
        moreSeek.setProgress(10);

        moreSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //change to progress
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moreTxt.setText(Integer.toString(progress));
                tmp_st_scale = progress;
            }

            //methods to implement but not necessary to amend
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button okBtn = (Button) levelDialog.findViewById(R.id.level_cancel);
        Button goDialogBtn;

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //respond to level

                //int chosenLevel = levelSeek.getProgress();
                levelDialog.dismiss();
            }
        });

        level_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDialog.show();
            }
        });
        */

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginActivity();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.symptom_main_btn1:
                tmp_st_main = symptom_main_btn1.getText().toString();
                moreDialog.show();
                break;

            case R.id.symptom_main_btn2:
                tmp_st_main = symptom_main_btn2.getText().toString();
                moreDialog.show();
                break;

            case R.id.symptom_main_btn3:
                tmp_st_main = symptom_main_btn3.getText().toString();
                moreDialog.show();
                break;

            case R.id.more_confirm:
                levelDialog.dismiss();
                tmp_st_sub = new StringBuilder();
                tmp_st_sub.append(detail_symptom_cb1.isChecked() ? "true " : "false ");
                tmp_st_sub.append(detail_symptom_cb2.isChecked() ? "true " : "false ");
                tmp_st_sub.append(detail_symptom_cb3.isChecked() ? "true " : "false ");

                Person.st_main = tmp_st_main;
                Person.st_place = st_place[currentPosition][0];
                Person.st_scale = tmp_st_scale;
                Person.st_sub = tmp_st_sub.toString();
                Person.st_comment = tmp_st_comment.getText().toString();

                // = new PersonalSymptomFragment.HttpAsyncTask();
                //httpAsyncTask.execute("http://igrus.mireene.com/applogin/stchange.php");
                //moreDialog.dismiss();
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_logout:
                this.logOut();
                goLoginActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        LoginManager.getInstance().logOut();
        goLoginActivity();
    }

    private void goLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }


    @Override
    public void onBackPressed() {
        if (pressTime == 0) {
            Toast.makeText(MainActivity.this, "Press again to exit.", Toast.LENGTH_LONG).show();
            pressTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressTime);

            if (seconds > 2000)
                pressTime = 0;
            else
                finish();
        }
    }


    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            imageView.setImageBitmap(getCircleBitmap(result));
        }

        private Bitmap getCircleBitmap(Bitmap bitmap) {
            final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);

            final int color = Color.BLACK;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            bitmap.recycle();

            return output;
        }
    }
}