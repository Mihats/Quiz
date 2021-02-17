package space.tsig.quizgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameLevels extends AppCompatActivity {

    private static final String TAG = "12345";
    //database
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelevels);
        SharedPreferences saveEmail = getSharedPreferences("saveEmail", MODE_PRIVATE);
        final String Email = saveEmail.getString("Email" , "");
        SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
        final int level = save.getInt("Level", 1);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (Email.equals(user.name)){
                        SharedPreferences.Editor editorLevel = save.edit();
                        editorLevel.putInt("Level", user.level);
                        editorLevel.commit();
                        Log.d(TAG, "LevelLevels is: " + level);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        Button button_back = (Button) findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
            }
        });
        //first level
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (level >= 1) {
                        Intent intent = new Intent(getApplicationContext(), Level1.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {

                }
            }
        });

        //2 level
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (level >= 2) {
                        Intent intent = new Intent(getApplicationContext(), Level2.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {

                }
            }
        });

        final int[] x = {
                R.id.textView1,
                R.id.textView2
        };
        for (int i = 1; i < level; i++) {
            TextView tv = findViewById(x[i]);
            tv.setText("" + (i + 1));
        }


    }





    // system back
    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {

        }
    }
}