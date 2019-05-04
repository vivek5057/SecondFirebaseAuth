package com.example.secondfirebaseauth;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewUser extends AppCompatActivity {

    private static final String TAG="Firebase";

    EditText email;
    EditText password;
    Button submit;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        init();
        myClick();
    }

    public void submit(){

        String myEmail = email.getText().toString();
        String myPassword = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(myEmail, myPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(NewUser.this, "Account is Created",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(NewUser.this, "Not Able To Create Account"+ task.getException(),
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void init(){
        email = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edit_text);
        submit = findViewById(R.id.submit);

    }

    public void myClick(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                submit();
            }
        });
    }
}
