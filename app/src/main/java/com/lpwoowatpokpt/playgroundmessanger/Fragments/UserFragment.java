package com.lpwoowatpokpt.playgroundmessanger.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.lpwoowatpokpt.playgroundmessanger.Adapter.UsersRecyclerAdapter;
import com.lpwoowatpokpt.playgroundmessanger.Model.Users;
import com.lpwoowatpokpt.playgroundmessanger.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private RecyclerView mUserListView;

    private List<Users>usersList;
    private UsersRecyclerAdapter usersRecyclerAdapter;

    private FirebaseFirestore firebaseFirestore;

    public UserFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mUserListView = view.findViewById(R.id.recyclerUsers);

        firebaseFirestore = FirebaseFirestore.getInstance();

        usersList = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(usersList, container.getContext());

        mUserListView.setHasFixedSize(true);
        mUserListView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mUserListView.setAdapter(usersRecyclerAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        usersList.clear();

        firebaseFirestore.collection("Users").addSnapshotListener(Objects.requireNonNull(getActivity()),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshots != null;
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){

                        String user_id = doc.getDocument().getId();

                        Users users = doc.getDocument().toObject(Users.class).widthId(user_id);
                        usersList.add(users);
                        usersRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
