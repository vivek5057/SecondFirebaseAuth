package com.example.secondfirebaseauth;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements MessageRecyclerView.ListItemClickListener {

    final private static String TAG = "TypeMessage";

    public static final String EXTRA_URL = "imageURL";
    public static final String EXTRA_USER_EMAIL = "userEmail";
    public static final int DEFAULT_MESSAGE_LENGTH = 30;
    public static final String MESSAGE_LENGTH = "message_length";

    private EditText typeMessage;
    private ImageView sendButton;
    private ImageView attachmentButton;
    private ProgressBar mainProgressBar;
    //private ProgressBar sProgressBar;
    //private ProgressBar rProgressBar;
    final static private int PICK_IMAGE_REQUEST = 1;

    List<MessageContainer> myMessageLists;
    RecyclerView recyclerView;
    MessageRecyclerView messageRecyclerView;
    LinearLayoutManager linearLayoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //init
        typeMessage = findViewById(R.id.type_message);
        sendButton = findViewById(R.id.send_button);
        attachmentButton = findViewById(R.id.choose_image);
        mainProgressBar = findViewById(R.id.loading_chats);
        //sProgressBar = findViewById(R.id.send_progress);
        //rProgressBar = findViewById(R.id.received_progress);

        //firebase init
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //recyleview
        recyclerView = findViewById(R.id.recyclerview_show_message);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        myMessageLists = new ArrayList<>();
        typeMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MESSAGE_LENGTH)});

        readMessage();
        onClickButton();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);

        //Map
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put(MESSAGE_LENGTH, DEFAULT_MESSAGE_LENGTH);
        firebaseRemoteConfig.setDefaults(defaultConfigMap);
        fetchConfig();

    }

    public void onClickButton() {

        //Send Button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeMessage.getText().toString().isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Type a message", Toast.LENGTH_SHORT).show();
                } else
                    sendMessage();
                typeMessage.setText("");
            }
        });

        //Attachment Button
        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickImage = new Intent(Intent.ACTION_GET_CONTENT);
                pickImage.setType("image/*");
                startActivityForResult(Intent.createChooser(pickImage, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            final StorageReference imageref = storageReference.child(imageUri.getLastPathSegment());
            imageref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            String email = user.getEmail();
                            String userId = databaseReference.push().getKey();
                            MessageContainer messageContainer = new MessageContainer(email, null, downloadUrl.toString());
                            databaseReference.child(userId).setValue(messageContainer);
                        }
                    });
                }
            });
        }
    }


    public void readMessage() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                MessageContainer messageContainer = dataSnapshot.getValue(MessageContainer.class);
                String email = messageContainer.chatName;
                String messageToSave = messageContainer.chatMessage;
                String image = messageContainer.chatImage;
                if (image != null) {
                    myMessageLists.add(new MessageContainer(email, null, image));

                } else {
                    myMessageLists.add(new MessageContainer(email, messageToSave, null));
                }
                messageRecyclerView = new MessageRecyclerView(ChatActivity.this, myMessageLists, ChatActivity.this);
                recyclerView.setAdapter(messageRecyclerView);
                mainProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mainProgressBar.setVisibility(View.GONE);
            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    public void sendMessage() {

        String messageToSave = typeMessage.getText().toString().trim();
        String userId = databaseReference.push().getKey();
        String email = user.getEmail();
        MessageContainer messageContainer = new MessageContainer(email, messageToSave, null);
        databaseReference.child(userId).setValue(messageContainer);

    }

    //recycleview click event
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(ChatActivity.this, FullImageActivity.class);
        MessageContainer messageContainer = myMessageLists.get(clickedItemIndex);
        intent.putExtra(EXTRA_URL, messageContainer.getChatImage());
        intent.putExtra(EXTRA_USER_EMAIL, messageContainer.getChatName());
        startActivity(intent);
    }

    public void fetchConfig() {
        long cacheExpiration = 3600;
        if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        firebaseRemoteConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseRemoteConfig.activateFetched();
                applyRetrievedLengthLimit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error fetching config", e);
                applyRetrievedLengthLimit();
            }
        });


    }

    public void applyRetrievedLengthLimit() {
        Long message_length = firebaseRemoteConfig.getLong(MESSAGE_LENGTH);
        typeMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(message_length.intValue())});
    }

}
