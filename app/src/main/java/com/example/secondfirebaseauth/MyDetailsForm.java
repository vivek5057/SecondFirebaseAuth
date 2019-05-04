package com.example.secondfirebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyDetailsForm extends AppCompatActivity {

    private static final String TAG = "CompareUserName";

    private EditText name;
    private EditText course;
    private EditText college;
    private EditText mobileNo;
    private EditText username;

    private Button save;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details_form);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        init();
        saveOnClick();
    }

    public void saveOnClick() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareUsername();
            }
        });
    }

    public void compareUsername() {
        String userId = username.getText().toString().trim();
        userReference = databaseReference.child(userId);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(MyDetailsForm.this, "Username Already Exists.", Toast.LENGTH_SHORT).show();
                } else {
                    //Reading EditText Values
                    String enterUser = username.getText().toString();
                    String enterFullName = name.getText().toString();
                    String enterCourse = course.getText().toString();
                    String enterCollege = college.getText().toString();
                    String enterMobile = mobileNo.getText().toString();

                    userEntry(enterUser, enterFullName, enterCourse, enterCollege, enterMobile);
                    startActivity(new Intent(MyDetailsForm.this, DisplayDetails.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        userReference.addListenerForSingleValueEvent(eventListener);
    }

    public void userEntry(String enterUser, String enterFullName, String enterCourse, String enterCollege, String enterMobile) {
        String userId = username.getText().toString().trim();
        UserFormDetails userFormDetails = new UserFormDetails(enterUser, enterFullName, enterCourse, enterCollege, enterMobile);
        databaseReference.child(userId).setValue(userFormDetails);

        Toast.makeText(MyDetailsForm.this, "Welcome" + " " + name.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    public void init() {
        name = findViewById(R.id.name);
        course = findViewById(R.id.course);
        college = findViewById(R.id.college_name);
        mobileNo = findViewById(R.id.mobile_no);
        username = findViewById(R.id.username);
        save = findViewById(R.id.save_button);
    }


}
