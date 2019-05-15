package com.lpwoowatpokpt.playgroundmessanger.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
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
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lpwoowatpokpt.playgroundmessanger.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private CircleImageView avatar;
    private EditText mEmailField, mNameField, mPasswordField;
    private Button mRegisterButton, mLoginPageBtn;

    private Uri imageUri;

    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageUri = null;

        progressBar = findViewById(R.id.progressBar);

        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        avatar = findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }
        });

        mEmailField = findViewById(R.id.edtEmail);
        mNameField = findViewById(R.id.edtName);
        mPasswordField = findViewById(R.id.edtPassword);

        mRegisterButton = findViewById(R.id.btnRegister);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri!= null){

                    progressBar.setVisibility(View.VISIBLE);

                    final String name = mNameField.getText().toString();
                    String email = mEmailField.getText().toString();
                    String password = mPasswordField.getText().toString();

                    if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    final String userId = mAuth.getCurrentUser().getUid();

                                    final StorageReference user_profile = mStorage.child(userId + ".jpg");

                                    user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {
                                                        if (uploadTask.isSuccessful()){


                                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    if (taskSnapshot.getMetadata() != null) {
                                                                        if (taskSnapshot.getMetadata().getReference() != null) {
                                                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                @Override
                                                                                public void onSuccess(Uri uri) {
                                                                                    final String imageUrl = uri.toString();


                                                                                    String token_id = FirebaseInstanceId.getInstance().getToken();

                                                                                            final Map<String, Object>userMap = new HashMap<>();
                                                                                            userMap.put("name",name);
                                                                                            userMap.put("image", imageUrl);
                                                                                            assert token_id != null;
                                                                                            userMap.put("token_id", token_id);

                                                                                            mFirestore.collection("Users").document(userId).set(userMap)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                                                            sendToMain();
                                                                                                        }
                                                                                                    });
                                                                                        }




                                                                            });
                                                                        }
                                                                    }
                                                                }});
                                            }else {
                                                Toast.makeText(RegisterActivity.this, "Error : " + uploadTask.getException().getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });

                                }else {
                                    Toast.makeText(RegisterActivity.this, "Error : " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);

                                }
                                }
                        });
                    }
                }
            }
        });

        mLoginPageBtn = findViewById(R.id.btnLogin);

        mLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE){

            assert data != null;
            imageUri = data.getData();
            avatar.setImageURI(imageUri);

        }

    }
}
