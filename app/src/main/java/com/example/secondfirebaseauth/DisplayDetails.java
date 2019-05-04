package com.example.secondfirebaseauth;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayDetails extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private EditText searchByNo;
    private TextView displayName;
    private TextView displayCourse;
    private TextView displayCollege;
    private TextView displayNumber;
    private Button showButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        init();
        onShow();
    }
    
    public void init() {

        searchByNo = findViewById(R.id.search_edit_text);
        displayName = findViewById(R.id.display_name);
        displayCollege = findViewById(R.id.display_college);
        displayCourse = findViewById(R.id.display_course);
        displayNumber = findViewById(R.id.display_number);
        showButton = findViewById(R.id.show_button);
    }

    public void onShow() {

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readDetails();
            }
        });

    }

    public void readDetails() {

        String userId = searchByNo.getText().toString().trim();//-Ladz8Z4g-YFyWv7ACJt"

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserFormDetails userFormDetails = dataSnapshot.getValue(UserFormDetails.class);

                displayName.setText(userFormDetails.userName);
                displayCourse.setText(userFormDetails.currentCourse);
                displayCollege.setText(userFormDetails.collegeName);
                displayNumber.setText(userFormDetails.mobileNo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayDetails.this, "" + databaseError.toException(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
