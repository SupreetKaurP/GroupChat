package com.example.groupchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button registerButton,loginButton;
    Button forgotButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, ChatActivity.class));
        }

        else {
            setContentView(R.layout.activity_main);

            email = (EditText) findViewById(R.id.email);
            password = (EditText) findViewById(R.id.password);
            registerButton = (Button) findViewById(R.id.SignUpButton);
            loginButton = (Button) findViewById(R.id.loginButton);
            forgotButton = (Button) findViewById(R.id.ForgetPasswordButton);

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                }
            });

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new  AlertDialog.Builder(MainActivity.this);
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(50,0,0,100);

                final EditText input = new EditText(MainActivity.this);
                input.setLayoutParams(params);
                input.setGravity(Gravity.TOP | Gravity.START);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input.setLines(1);
                input.setMaxLines(1);

                layout.addView(input, params);
                alert.setMessage("Enter your registered email address.");
                alert.setTitle("Forgot Password");
                alert.setView(layout);

                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        String email = input.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    dialogInterface.dismiss();
                                    Toast.makeText(getApplicationContext(), "Email sent! Please check your email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }
        });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String emailText = email.getText().toString();
                    String passwordText = password.getText().toString();

                    if(TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)){
                        Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    signIn(emailText, passwordText);
                }
            });
        }

    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Opps!! try again.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}