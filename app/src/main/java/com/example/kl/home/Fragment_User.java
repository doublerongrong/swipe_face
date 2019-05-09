package com.example.kl.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kl.home.Adapter.Detail_BonusListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class Fragment_User extends Fragment {

    private static final String TAG = "Fragment_User";
    private ImageButton InforSetting;
    private String teacher_email;
    private CardView logout;

    OnFragmentSelectedListener mCallback;//Fragment傳值

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args ;
        args = getArguments();
        teacher_email = args.getString("teacher_email");
        return inflater.inflate(R.layout.fragment_fragment__user, container, false);

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        InforSetting = (ImageButton)view.findViewById(R.id.settingBtn);
        logout = (CardView)view.findViewById(R.id.logout);

        Log.d(TAG,"teacher_id :  " + teacher_email);


        InforSetting.setOnClickListener(v -> {
            mCallback.onFragmentSelected(teacher_email, "toUserInfor");//fragment傳值
        });

        logout.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent();
            i.setClass(getActivity(),WelcomePage.class);
            startActivity(i);

        });



    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentSelectedListener) context;//fragment傳值
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Mush implement OnFragmentSelectedListener ");
        }
    }


}
