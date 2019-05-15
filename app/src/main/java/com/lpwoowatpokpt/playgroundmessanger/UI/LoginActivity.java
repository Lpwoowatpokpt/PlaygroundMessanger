package com.lpwoowatpokpt.playgroundmessanger.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lpwoowatpokpt.playgroundmessanger.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailField, mPasswordField;
    private Button mLoginButton, mRegisterButton;

    private ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);

        mEmailField = findViewById(R.id.edtEmail);
        mPasswordField = findViewById(R.id.edtPassword);

        mLoginButton = findViewById(R.id.btnLogin);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString();
                final String password = mPasswordField.getText().toString();

                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
                            if (task.isSuccessful()){

                              String token_id = FirebaseInstanceId.getInstance().getToken();
                              String current_id = mAuth.getCurrentUser().getUid();

                              Map<String, Object>tokenMap = new HashMap<>();
                              tokenMap.put("token_id", token_id);

                              mFirestore.collection("Users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      sendToMain();
                                      progressBar.setVisibility(View.INVISIBLE);
                                  }
                              });

                            }else {
                                Toast.makeText(LoginActivity.this, "Error : " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                }
            }
        });

        mRegisterButton = findViewById(R.id.btnRegister);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });

    }
}
