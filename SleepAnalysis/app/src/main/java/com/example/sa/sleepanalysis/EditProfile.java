package com.example.sa.sleepanalysis;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sa.sleepanalysis.model.Userdetail;
import com.example.sa.sleepanalysis.model.user;
import com.example.sa.sleepanalysis.network.AccessToken;
import com.example.sa.sleepanalysis.network.ApiService;
import com.example.sa.sleepanalysis.network.ApiUtils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    private Vibrator vibrator;
    private Animation anim;
    private EditText password, repassword, name, birthdate;
    private TextInputLayout passwordTextlayout, passwordagainTextlayout, nameTextlayout,
            birthdateTextlayout;

    private SweetAlertDialog pDialog;
    private ApiService mAPIService;
    private TextView email;
    private user user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Animation
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
        anim.setRepeatCount(2);

        //vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //TextinputLayout
        passwordTextlayout =  findViewById(R.id.passwordTextlayout);
        passwordagainTextlayout =  findViewById(R.id.passwordagainTextlayout);
        nameTextlayout = findViewById(R.id.nameTextlayout);
        birthdateTextlayout = findViewById(R.id.birthdatelayout);

        //EditText
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.passwordagain);
        name = findViewById(R.id.nameText);
        birthdate = findViewById(R.id.birthdate);

        //TextView
        email = findViewById(R.id.email);

        user = new user();
        getuserDetail(user.getUser_id());

        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkpassword() || !checkpasswordagain() || !checkname() || !checkbirthdate()) {
                    password.setAnimation(anim);
                    repassword.setAnimation(anim);
                    name.setAnimation(anim);
                    birthdate.setAnimation(anim);
                    checkpassword();
                    checkpasswordagain();
                    checkname();
                    checkbirthdate();
                    vibrator.vibrate(120);
                    return;
                } else {
                    mAPIService = ApiUtils.getAPIService();
                    String passwords = password.getText().toString();
                    String names = name.getText().toString();
                    String dateofBirths = birthdate.getText().toString();

                    Date mydate = fromStringToDate(dateofBirths, "dd/MM/yyyy");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    RequestBody id = RequestBody.create(MultipartBody.FORM, ""+user.getUser_id());
                    RequestBody passwordR = RequestBody.create(MultipartBody.FORM, passwords);
                    RequestBody nameR = RequestBody.create(MultipartBody.FORM, names);
                    RequestBody dateofBirthR = RequestBody.create(MultipartBody.FORM, sdf.format(mydate.getTime()));

                    mAPIService.updateUserdetail(id, nameR, passwordR, dateofBirthR).enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            showProgressDialogSuccessRegister();
                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                            Log.e("update userdetail", "onFailure: "+t.toString() );
                        }
                    });
                }
            }
        });



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
                password.setText(response.body().getData().getPassword());
                repassword.setText(response.body().getData().getPassword());

                Date mydate = fromStringToDate(response.body().getData().getDateofBirth(), "yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                birthdate.setText(sdf.format(mydate.getTime()));


            }

            @Override
            public void onFailure(Call<Userdetail> call, Throwable t) {
                Log.e("user detail", "onFailure: "+ t.toString() );
            }
        });


    }

    private boolean checkpassword() {
        if (password.getText().toString().trim().isEmpty()) {
            passwordTextlayout.setErrorEnabled(true);
            passwordTextlayout.setError("Please input Password!!");
            return false;
        }
        passwordTextlayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkpasswordagain() {
        if (repassword.getText().toString().trim().isEmpty()) {
            passwordagainTextlayout.setErrorEnabled(true);
            passwordagainTextlayout.setError("Please input Password!!");
            return false;
        } else if (!password.getText().toString().equals(repassword.getText().toString())) {
            passwordagainTextlayout.setErrorEnabled(true);
            passwordagainTextlayout.setError("Password does not match the Confirm Password!!");
            return false;
        }
        passwordagainTextlayout.setErrorEnabled(false);
        return true;
    }
    private boolean checkname() {
        if (name.getText().toString().trim().isEmpty()) {
            nameTextlayout.setErrorEnabled(true);
            nameTextlayout.setError("Please input Name!!");
            return false;
        }
        nameTextlayout.setErrorEnabled(false);
        return true;
    }
    private boolean checkbirthdate() {
        if (birthdate.getText().toString().trim().isEmpty()) {
            birthdateTextlayout.setErrorEnabled(true);
            birthdateTextlayout.setError("Please input Date of birth!!");
            return false;
        }
        birthdateTextlayout.setErrorEnabled(false);
        return true;
    }
    private void showProgressDialogSuccessRegister() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Update Completed!!");
        pDialog.show();
        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }
}
