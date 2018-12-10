package com.example.sa.sleepanalysis.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sa.sleepanalysis.EditProfile;
import com.example.sa.sleepanalysis.LoginActivity;
import com.example.sa.sleepanalysis.R;
import com.example.sa.sleepanalysis.model.Userdetail;
import com.example.sa.sleepanalysis.model.user;
import com.example.sa.sleepanalysis.network.ApiService;
import com.example.sa.sleepanalysis.network.ApiUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profileFragment extends Fragment {


    private ApiService mAPIService;
    private TextView name, dateofBirth, email;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        Button logOut = rootView.findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        RelativeLayout editProfile = rootView.findViewById(R.id.editProfileButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfile.class));
            }
        });

        name = rootView.findViewById(R.id.user_name);
        dateofBirth = rootView.findViewById(R.id.user_birthday);
        email = rootView.findViewById(R.id.user_email);

        user user = new user();
        getuserDetail(user.getUser_id());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        user user = new user();
        getuserDetail(user.getUser_id());
    }



    private Date fromStringToDate(String stringDate, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = format.parse(stringDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getuserDetail(int user_id) {
        mAPIService = ApiUtils.getAPIService();
        mAPIService.getUserdetail(user_id).enqueue(new Callback<Userdetail>() {
            @Override
            public void onResponse(Call<Userdetail> call, Response<Userdetail> response) {

                name.setText(response.body().getData().getName());
                email.setText(response.body().getData().getEmail());

                Date mydate = fromStringToDate(response.body().getData().getDateofBirth(), "yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                dateofBirth.setText(sdf.format(mydate.getTime()));


            }

            @Override
            public void onFailure(Call<Userdetail> call, Throwable t) {
                Log.e("user detail", "onFailure: "+ t.toString() );
            }
        });


    }



}