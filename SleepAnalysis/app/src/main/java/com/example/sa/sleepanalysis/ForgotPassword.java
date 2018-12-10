package com.example.sa.sleepanalysis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.sa.sleepanalysis.network.AccessToken;
import com.example.sa.sleepanalysis.network.ApiService;
import com.example.sa.sleepanalysis.network.ApiUtils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private Vibrator vibrator;
    private Animation anim;
    private EditText email;
    private TextInputLayout emailTextlayout;
    private SweetAlertDialog pDialog;
    private ApiService mAPIService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Animation
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
        anim.setRepeatCount(1);

        //vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //TextinputLayout
        emailTextlayout =  findViewById(R.id.emailTextlayout);

        //EditText
        email = findViewById(R.id.email);

        //button
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialogloading();
                if (!checkEmail()) {
                    email.setAnimation(anim);
                    checkEmail();
                    vibrator.vibrate(120);
                    return;
                } else {
                    mAPIService = ApiUtils.getAPIService();
                    String emails = email.getText().toString();

                    RequestBody email = RequestBody.create(MultipartBody.FORM, emails);
                    mAPIService.resetUserpassword(email).enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            dismissProgressDialogloading();
                            if (response.body().isSuccess()){
                                showProgressDialogSuccessRegister();
                            }else {
                                showProgressDialogEmailnotCorrect();
                            }
                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                            dismissProgressDialogloading();
                            showProgressDialogfailconnection();
                        }
                    });


                }
            }
        });


    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean checkEmail() {
        if (email.getText().toString().trim().isEmpty()) {
            emailTextlayout.setErrorEnabled(true);
            emailTextlayout.setError("Please input Email!!");
            return false;
        }
        if (!isValidEmail(email.getText().toString())) {
            emailTextlayout.setErrorEnabled(true);
            emailTextlayout.setError("Incorrect Email!!");
            return false;
        }
        emailTextlayout.setErrorEnabled(false);
        return true;
    }

    private void showProgressDialogSuccessRegister() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Send to email Successfully!!");
        pDialog.show();
        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void showProgressDialogEmailnotCorrect() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("Email is not Correct!!");
        pDialog.show();
        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                pDialog.dismiss();
            }
        });
    }

    private void showProgressDialogfailconnection() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("Connection Failed!!");
        pDialog.show();
        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                pDialog.dismiss();
            }
        });
    }

    private void showProgressDialogloading() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Sending Email");
        pDialog.show();
    }

    private void dismissProgressDialogloading() {
        pDialog.dismiss();
    }

}
