package com.example.groupchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    chatAdapter adapter;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messageDb;
    List<chatData> messages;
    EditText textMessage;
    Button sendButton;
    User u;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    public void init() {

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        u = new User();

        textMessage = (EditText) findViewById(R.id.typeMessage);
        sendButton = (Button) findViewById(R.id.send);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        sendButton.setOnClickListener(this);
        messages = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser currentUser = auth.getCurrentUser();
        u.setName(currentUser.getEmail());
        u.setUid(currentUser.getUid());

        database.getReference("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u = snapshot.getValue(User.class);
                u.setUid(currentUser.getUid());
                AllMethods.name = u.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messageDb = database.getReference("messages");
        messageDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatData message = snapshot.getValue(chatData.class);
                message.setKey(snapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatData message = snapshot.getValue(chatData.class);
                message.setKey(snapshot.getKey());

                List<chatData> newMessages = new ArrayList<>();

                for(chatData m : messages) {
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    }
                    else {
                        newMessages.add(m);
                    }
                }

                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                chatData message = snapshot.getValue(chatData.class);
                message.setKey(snapshot.getKey());

                List<chatData> newMessages = new ArrayList<>();

                for(chatData m : messages) {
                    if(!m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayMessages(List<chatData> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new chatAdapter(this, list, messageDb);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(!TextUtils.isEmpty(textMessage.getText().toString())) {
            chatData message = new chatData(textMessage.getText().toString(), u.getName());
            textMessage.setText("");
            messageDb.push().setValue(message);
        }
        else {
            Toast.makeText(getApplicationContext(),"You can't send empty message!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menulogOut){
            auth.signOut();
            finish();
            startActivity(new Intent(ChatActivity.this, MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}
