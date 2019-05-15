package com.lpwoowatpokpt.playgroundmessanger.UI;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lpwoowatpokpt.playgroundmessanger.R;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private TextView user_id_view;

    private ProgressBar progressBar;

    private String mUserId;
    private String mCurrentId;
    private String mUserName;

    private EditText mMessageView;
    private Button mSendButton;

    private FirebaseFirestore firebaseFirestore;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        user_id_view = findViewById(R.id.user_name_view);
        mMessageView = findViewById(R.id.edtMessage);
        mSendButton = findViewById(R.id.sendBtn);

        progressBar = findViewById(R.id.progressBar);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mCurrentId = FirebaseAuth.getInstance().getUid();

        mUserId = getIntent().getStringExtra("user_id");
        mUserName = getIntent().getStringExtra("user_name");
        user_id_view.setText(getString(R.string.send_to) + mUserName);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageView.getText().toString();

                if (!TextUtils.isEmpty(message)){

                    progressBar.setVisibility(View.VISIBLE);

                    Map<String, Object>notificationMessage = new HashMap<>();
                    notificationMessage.put("message", message);
                    notificationMessage.put("from", mCurrentId);

                    firebaseFirestore.collection("Users/" + mUserId + "/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressBar.setVisibility(View.INVISIBLE);
                            mMessageView.setText("");
                            Toast.makeText(SendActivity.this, "Notification was sent", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SendActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
