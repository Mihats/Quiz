package space.tsig.quizgame;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Level2 extends AppCompatActivity {

    private static final String TAG = "1234";
    Dialog dialog;
    Dialog dialogEnd;

    public int numQuestion;
    public int answer1;
    public int answer2;
    public int answer3;

    Array array = new Array();
    Random random = new Random();
    private List<List> listAnswers = new ArrayList<>();
    private List<String> listQuestions= new ArrayList<>();
    private List<Integer> listAnswerKeys= new ArrayList<>();

    public int count = 0;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAdClose;
    private AdView mAdView;

    //database
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level2);

        //database
        mDatabase = FirebaseDatabase.getInstance().getReference("level2");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    QLevel2 qLevel2 = ds.getValue(QLevel2.class);
                    String[] subStr;
                    List<String> answers = new ArrayList<>();
                    subStr = qLevel2.answers.split(",");
                    for(int i = 0; i < subStr.length; i++) {
                        answers.add(subStr[i]);
                    }
                    listAnswers.add(answers);
                    listQuestions.add(qLevel2.question);
                    listAnswerKeys.add(qLevel2.answerKey);
                }
                main();
                Log.d(TAG, "AnswersSize is: " + listAnswers.size());
                Log.d(TAG, "QuestionSize is: " + listQuestions.size());
                Log.d(TAG, "KeySize is: " + listAnswerKeys.size());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1772797776810105/3156941333");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

        mInterstitialAdClose = new InterstitialAd(this);
        mInterstitialAdClose.setAdUnitId("ca-app-pub-1772797776810105/3156941333");
        mInterstitialAdClose.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

        //closeAdd
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                try {
                    Intent intent = new Intent(getApplicationContext(), GameLevels.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
            }
        });
        //text_levels
        TextView text_levels = findViewById(R.id.text_levels);
        text_levels.setText(R.string.level2);


        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    try {
                        Intent intent = new Intent(getApplicationContext(), GameLevels.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {

                    }
                }
            }
        });


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //call dialog view
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.previewdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        dialog.setCancelable(false);

        TextView textDescr = dialog.findViewById(R.id.textdescripyion);
        textDescr.setText(R.string.leveltwo);

        //close dialog
        TextView btnclose = (TextView) dialog.findViewById(R.id.btnclose);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), GameLevels.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
                dialog.dismiss();
            }
        });

        dialog.show();

        //continue
        Button btncontinue = (Button) dialog.findViewById(R.id.btncontinue);
        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //___________________________________________________
        //call dialog view
        dialogEnd = new Dialog(this);
        dialogEnd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEnd.setContentView(R.layout.dialogenddialog);
        dialogEnd.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        dialogEnd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogEnd.setCancelable(false);

        //close dialog
        TextView btncloseEnd = (TextView) dialogEnd.findViewById(R.id.btnclose);
        btncloseEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), GameLevels.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
                dialogEnd.dismiss();
            }
        });
        //continue
        Button btncontinueEnd = (Button) dialogEnd.findViewById(R.id.btncontinue);
        btncontinueEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), Level2.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
                dialogEnd.dismiss();
            }
        });


    }

    public void main() {
        final TextView question = findViewById(R.id.question);
        final Button button_one = (Button) findViewById(R.id.button_one);
        final Button button_two = (Button) findViewById(R.id.button_two);
        final Button button_three = (Button) findViewById(R.id.button_three);
        //array for progress
        final int[] progress = {
                R.id.point1, R.id.point2, R.id.point3, R.id.point4, R.id.point5
        };


        numQuestion = random.nextInt(listQuestions.size());
        question.setText(listQuestions.get(numQuestion));

        answer1 = random.nextInt(3);


        button_one.setText(listAnswers.get(numQuestion).get(answer1).toString());
        answer2 = random.nextInt(3);
        while (answer2 == answer1) {
            answer2 = random.nextInt(3);
        }
        button_two.setText(listAnswers.get(numQuestion).get(answer2).toString());
        answer3 = random.nextInt(3);
        while (answer3 == answer1 || answer3 == answer2) {
            answer3 = random.nextInt(3);
        }
        button_three.setText(listAnswers.get(numQuestion).get(answer3).toString());
        listQuestions.remove(numQuestion);
        listAnswers.remove(numQuestion);

        //click on answer
        int correctColor = getResources().getColor(R.color.green);
        int errorColor = getResources().getColor(R.color.red);
        int backColor = getResources().getColor(R.color.black60);
        button_one.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button_two.setEnabled(false);
                    button_three.setEnabled(false);
                    System.out.println(answer1);

                    if (answer1 == listAnswerKeys.get(numQuestion)) {
                        button_one.setBackgroundColor(correctColor);
                    } else {
                        button_one.setBackgroundColor(errorColor);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (answer1 == listAnswerKeys.get(numQuestion)) {
                        if (count < 5) {
                            count++;
                        }
                        for (int i = 0; i < 5; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    } else {
                        if (count > 0) {
                            if (count == 1) {
                                count = 0;
                            } else {
                                count = count - 2;
                            }
                        }
                        for (int i = 0; i < 4; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }
                    if (count == 5) {
                        //exit from level
                        if (mInterstitialAdClose.isLoaded()) {
                            mInterstitialAdClose.show();
                            SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                                SharedPreferences.Editor editor = save.edit();
                                editor.putInt("Level", 2);
                                editor.commit();
                            }
                            dialogEnd.show();
                        } else {
                            SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                                SharedPreferences.Editor editor = save.edit();
                                editor.putInt("Level", 2);
                                editor.commit();
                            }
                            dialogEnd.show();
                        }
                    } else {
                        listAnswerKeys.remove(numQuestion);
                        numQuestion = random.nextInt(listQuestions.size());
                        question.setText(listQuestions.get(numQuestion));

                        answer1 = random.nextInt(3);
                        button_one.setText(listAnswers.get(numQuestion).get(answer1).toString());
                        button_one.setBackgroundColor(backColor);
                        answer2 = random.nextInt(3);
                        while (answer2 == answer1) {
                            answer2 = random.nextInt(3);
                        }
                        button_two.setText(listAnswers.get(numQuestion).get(answer2).toString());
                        answer3 = 3 - answer1 - answer2;
                        button_three.setText(listAnswers.get(numQuestion).get(answer3).toString());

                        button_two.setEnabled(true);
                        button_three.setEnabled(true);
                        listQuestions.remove(numQuestion);
                        listAnswers.remove(numQuestion);
                    }
                }
                return true;
            }
        });

        button_two.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button_one.setEnabled(false);
                    button_three.setEnabled(false);
                    System.out.println(answer2);
                    if (answer2 == listAnswerKeys.get(numQuestion)) {
                        button_two.setBackgroundColor(correctColor);
                    } else {
                        button_two.setBackgroundColor(errorColor);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (answer2 == listAnswerKeys.get(numQuestion)) {
                        if (count < 5) {
                            count++;
                        }
                        for (int i = 0; i < 5; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    } else {
                        if (count > 0) {
                            if (count == 1) {
                                count = 0;
                            } else {
                                count = count - 2;
                            }
                        }
                        for (int i = 0; i < 4; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }
                    if (count == 5) {
                        //exit
                        if (mInterstitialAdClose.isLoaded()) {
                            mInterstitialAdClose.show();
                            SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                                SharedPreferences.Editor editor = save.edit();
                                editor.putInt("Level", 2);
                                editor.commit();
                            }
                            dialogEnd.show();
                        } else {
                            SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                                SharedPreferences.Editor editor = save.edit();
                                editor.putInt("Level", 2);
                                editor.commit();
                            }
                            dialogEnd.show();
                        }
                    } else {
                        listAnswerKeys.remove(numQuestion);
                        numQuestion = random.nextInt(listQuestions.size());
                        question.setText(listQuestions.get(numQuestion));

                        answer1 = random.nextInt(3);
                        button_one.setText(listAnswers.get(numQuestion).get(answer1).toString());

                        answer2 = random.nextInt(3);
                        while (answer2 == answer1) {
                            answer2 = random.nextInt(3);
                        }
                        button_two.setText(listAnswers.get(numQuestion).get(answer2).toString());
                        button_two.setBackgroundColor(backColor);
                        answer3 = 3 - answer1 - answer2;
                        button_three.setText(listAnswers.get(numQuestion).get(answer3).toString());

                        button_one.setEnabled(true);
                        button_three.setEnabled(true);
                        listQuestions.remove(numQuestion);
                        listAnswers.remove(numQuestion);
                    }
                }
                return true;
            }
        });

        button_three.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button_two.setEnabled(false);
                    button_one.setEnabled(false);
                    System.out.println(answer3);
                    if (answer3 == listAnswerKeys.get(numQuestion)) {
                        button_three.setBackgroundColor(correctColor);
                    } else {
                        button_three.setBackgroundColor(errorColor);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (answer3 == listAnswerKeys.get(numQuestion)) {
                        if (count < 5) {
                            count++;
                        }
                        for (int i = 0; i < 5; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    } else {
                        if (count > 0) {
                            if (count == 1) {
                                count = 0;
                            } else {
                                count = count - 2;
                            }
                        }
                        for (int i = 0; i < 4; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }
                    if (count == 5) {
                        //exit from level
                        if (mInterstitialAdClose.isLoaded()) {
                            mInterstitialAdClose.show();
                            SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                                SharedPreferences.Editor editor = save.edit();
                                editor.putInt("Level", 2);
                                editor.commit();
                            }
                            dialogEnd.show();
                        } else {
                            SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                                SharedPreferences.Editor editor = save.edit();
                                editor.putInt("Level", 2);
                                editor.commit();
                            }
                            dialogEnd.show();
                        }
                    } else {
                        listAnswerKeys.remove(numQuestion);
                        numQuestion = random.nextInt(listQuestions.size());
                        question.setText(listQuestions.get(numQuestion));

                        answer1 = random.nextInt(3);
                        button_one.setText(listAnswers.get(numQuestion).get(answer1).toString());

                        answer2 = random.nextInt(3);
                        while (answer2 == answer1) {
                            answer2 = random.nextInt(3);
                        }
                        button_two.setText(listAnswers.get(numQuestion).get(answer2).toString());
                        answer3 = 3 - answer1 - answer2;

                        button_three.setText(listAnswers.get(numQuestion).get(answer3).toString());
                        button_three.setBackgroundColor(backColor);

                        button_two.setEnabled(true);
                        button_one.setEnabled(true);
                        listQuestions.remove(numQuestion);
                        listAnswers.remove(numQuestion);
                    }
                }
                return true;
            }
        });
    }


    // system back
    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(getApplicationContext(), GameLevels.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {

        }
    }
}