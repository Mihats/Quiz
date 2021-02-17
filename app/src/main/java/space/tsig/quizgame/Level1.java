package space.tsig.quizgame;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class Level1 extends AppCompatActivity {

    private static final String TAG = "123";
    Dialog dialog;
    Dialog dialogEnd;

    public int numLeft; //left btn
    public int numRight; //right btn

    Array array = new Array();
    Random random = new Random();
    private List<String> listData = new ArrayList<>();

    public int count = 0;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAdClose;
    private AdView mAdView;

    //database
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);


        //database
        mDatabase = FirebaseDatabase.getInstance().getReference("level1");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    QLevel1 qLevel1 = ds.getValue(QLevel1.class);
                    listData.add(qLevel1.value);
                }
                Log.d(TAG, "Value is: " + listData.size());
                main();

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
        mInterstitialAdClose = new InterstitialAd(this);
        mInterstitialAdClose.setAdUnitId("ca-app-pub-1772797776810105/3156941333");
        mInterstitialAdClose.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
        //text_levels
        TextView text_levels = findViewById(R.id.text_levels);
        text_levels.setText(R.string.level1);



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
        //continue
        Button btncontinue = (Button) dialog.findViewById(R.id.btncontinue);
        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

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

    }

    public void main(){
        final Button button_left = (Button) findViewById(R.id.button_one);
        final Button button_right = (Button) findViewById(R.id.button_right);
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


        //array for progress
        final int[] progress = {
                R.id.point1, R.id.point2, R.id.point3, R.id.point4, R.id.point5, R.id.point6, R.id.point7, R.id.point8, R.id.point9, R.id.point10,
                R.id.point11, R.id.point12, R.id.point13, R.id.point14, R.id.point15, R.id.point16, R.id.point17, R.id.point18, R.id.point19, R.id.point20
        };


        //animation
        final Animation a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);

        numLeft = random.nextInt(10);
        button_left.setText(listData.get(numLeft));

        numRight = random.nextInt(10);
        while (numLeft == numRight) {
            numRight = random.nextInt(10);
        }
        button_right.setText(listData.get(numRight));

        SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
        //click on answer
        int correctColor = getResources().getColor(R.color.green);
        int errorColor = getResources().getColor(R.color.red);
        int backColor = getResources().getColor(R.color.black60);
        button_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button_right.setEnabled(false);
                    if (numLeft > numRight) {
                        button_left.setBackgroundColor(correctColor);
                    } else {
                        button_left.setBackgroundColor(errorColor);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (numLeft > numRight) {
                        if (count < 20) {
                            count++;
                        }
                        for (int i = 0; i < 20; i++) {
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
                        for (int i = 0; i < 19; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }
                    if (count == 20) {
                        //exit from level
                        if (mInterstitialAdClose.isLoaded()) {
                            mInterstitialAdClose.show();
                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                               onChangeLevel();
                            }
                            dialogEnd.show();
                        } else {
                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                               onChangeLevel();

                            }
                            dialogEnd.show();
                        }
                    } else {
                        numLeft = random.nextInt(10);
                        button_left.setText(listData.get(numLeft));
                        button_left.startAnimation(a);
                        button_left.setBackgroundColor(backColor);

                        numRight = random.nextInt(10);
                        while (numLeft == numRight) {
                            numRight = random.nextInt(10);
                        }
                        button_right.setText(listData.get(numRight));
                        button_right.startAnimation(a);
                        button_right.setBackgroundColor(backColor);
                        button_right.setEnabled(true);
                    }
                }
                return true;
            }
        });
        button_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button_left.setEnabled(false);
                    if (numRight > numLeft) {
                        button_right.setBackgroundColor(correctColor);
                    } else {
                        button_right.setBackgroundColor(errorColor);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (numRight > numLeft) {
                        if (count < 20) {
                            count++;
                        }
                        for (int i = 0; i < 20; i++) {
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
                        for (int i = 0; i < 19; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points);
                        }
                        for (int i = 0; i < count; i++) {
                            TextView tv = findViewById(progress[i]);
                            tv.setBackgroundResource(R.drawable.style_points_green);
                        }
                    }
                    if (count == 20) {
                        //exit from level
                        if (mInterstitialAdClose.isLoaded()) {
                            mInterstitialAdClose.show();
                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                               onChangeLevel();
                            }
                            dialogEnd.show();
                        } else {

                            final int level = save.getInt("Level", 1);
                            if (level > 1) {
                                //
                            } else {
                                onChangeLevel();
                            }
                            dialogEnd.show();
                        }
                    } else {
                        numLeft = random.nextInt(10);
                        button_left.setText(listData.get(numLeft));
                        button_left.startAnimation(a);
                        button_left.setBackgroundColor(backColor);

                        numRight = random.nextInt(10);
                        while (numLeft == numRight) {
                            numRight = random.nextInt(10);
                        }
                        button_right.setText(listData.get(numRight));
                        button_right.startAnimation(a);
                        button_right.setBackgroundColor(backColor);
                        button_left.setEnabled(true);
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
    public void onChangeLevel(){
        //database
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");
        SharedPreferences saveEmail = getSharedPreferences("saveEmail", MODE_PRIVATE);
        final String Email = saveEmail.getString("Email" , "");
        Log.d(TAG, "EmailLevel1 is: " + Email);
        SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    if (Email.equals(user.name)){
                        String userId = ds.getKey();
                        Log.w(TAG, "UserId"+ userId);
                        User newUser = new User(Email, 2);
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(userId, newUser);
                        mDatabaseUser.updateChildren(childUpdates);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        Intent intent = new Intent(getApplicationContext(), Level2.class);
        startActivity(intent);
        finish();
    }
}