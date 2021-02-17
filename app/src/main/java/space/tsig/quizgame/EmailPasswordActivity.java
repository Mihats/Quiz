package space.tsig.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmailPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "345";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText ETemail;
    private EditText ETpassword;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }

            }
        };


        ETemail = (EditText) findViewById(R.id.et_email);
        ETpassword = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_registration).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_sign_in)
        {
            signin(ETemail.getText().toString(),ETpassword.getText().toString());
        }else if (view.getId() == R.id.btn_registration)
        {
            registration(ETemail.getText().toString(),ETpassword.getText().toString());
        }

    }

    public void signin(String email , String password)
    {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    SharedPreferences saveEmail = getSharedPreferences("saveEmail", MODE_PRIVATE);
                    final String Email = saveEmail.getString("Email" , email);
                    SharedPreferences.Editor editor = saveEmail.edit();
                    editor.putString("Email", email);
                    editor.commit();
                    //database
                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                User user = ds.getValue(User.class);
                                if (email.equals(user.name)){
                                    SharedPreferences save = getSharedPreferences("Save", MODE_PRIVATE);
                                    final int level = save.getInt("Level", user.level);
                                    SharedPreferences.Editor editorLevel = save.edit();
                                    editorLevel.putInt("Level", user.level);
                                    editorLevel.commit();
                                    Log.d(TAG, "LevelSignIn is: " + level);
                                }
                                Log.d(TAG, "LevelSignIn is: " + 34);
                            }
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                }else
                    Toast.makeText(EmailPasswordActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void registration (String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(EmailPasswordActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    //database
                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                    User newUser = new User(email, 1);
                    mDatabase.push().setValue(newUser);
                }
                else
                    Toast.makeText(EmailPasswordActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
            }
        });

    }
}