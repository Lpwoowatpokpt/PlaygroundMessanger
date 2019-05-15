package com.lpwoowatpokpt.playgroundmessanger.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lpwoowatpokpt.playgroundmessanger.R;
import com.lpwoowatpokpt.playgroundmessanger.UI.LoginActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private Button mLogoutButon;
    private CircleImageView avatar;
    private TextView mUserLabel;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore mFirestore;
    private String mUserId;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        mFirestore = FirebaseFirestore.getInstance();

        mUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        mLogoutButon = view.findViewById(R.id.logOutButton);
        mLogoutButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map<String, Object>tokenMapRemove = new HashMap<>();
                tokenMapRemove.put("token_id", FieldValue.delete());

                mFirestore.collection("Users").document(mUserId).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseAuth.signOut();

                        Intent loginIntent = new Intent(container.getContext(), LoginActivity.class);
                        startActivity(loginIntent);
                    }
                });


            }
        });

        avatar = view.findViewById(R.id.avatar);
        mUserLabel = view.findViewById(R.id.userLabel);

        mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String user_name = documentSnapshot.getString("name");
                String user_image = documentSnapshot.getString("image");



                mUserLabel.setText(user_name);
                Glide.with(container.getContext()).load(user_image).into(avatar);

            }
        });

        return view;
    }

}
