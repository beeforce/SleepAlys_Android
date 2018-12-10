package com.example.buri_paoton.sleepanalysis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.buri_paoton.sleepanalysis.model.user;
import com.example.buri_paoton.sleepanalysis.network.AccessToken;
import com.example.buri_paoton.sleepanalysis.network.ApiService;
import com.example.buri_paoton.sleepanalysis.network.ApiUtils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Vibrator vibrator;
    private Animation anim;
    private EditText email, password;
    private TextInputLayout emailTextlayout, passwordTextlayout;
    private SweetAlertDialog pDialog;

    private ApiService mAPIService;
    private Call<AccessToken> call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Animation
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
        anim.setRepeatCount(2);

        //vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //TextinputLayout
        emailTextlayout =  findViewById(R.id.emailTextlayout);
        passwordTextlayout =  findViewById(R.id.passwordTextlayout);

        //EditText
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);




        Button signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkEmail() || !checkpassword() ) {
                    email.setAnimation(anim);
                    password.setAnimation(anim);
                    checkEmail();
                    checkpassword();
                    vibrator.vibrate(120);
                    return;
                } else {
                    mAPIService = ApiUtils.getAPIService();
                    String emails = email.getText().toString();
                    String passwords = password.getText().toString();

                    RequestBody emailR = RequestBody.create(MultipartBody.FORM, emails);
                    RequestBody passwordR = RequestBody.create(MultipartBody.FORM, passwords);


                    mAPIService.login(emailR, passwordR).enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            if (response.body().isSuccess()){
                                user user = new user();
                                user.setUser_id(response.body().getUser_id());
                                Log.e("login", "onResponse: " + response.body().getUser_id() );
                                showProgressDialogSuccessRegister();
                            }else {
                                showProgressDialogLoginfail();
                            }

                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                            showProgressDialogfailconnection();

                        }
                    });
                }

            }
        });

        Button signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, signUpActivity.class));
                finish();
            }
        });

        TextView forgotPassword = findViewById(R.id.forgetPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
                finish();
            }
        });

    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean checkEmail() {
        if (email.getText().toString().trim().isEmpty()) {
            emailTextlayout.setErrorEnabled(true);
            emailTextlayout.setError("Please input Username!!");
            email.setError("Please input Password!!");
            return false;
        }
        if (!isValidEmail(email.getText().toString())) {
            emailTextlayout.setErrorEnabled(true);
            emailTextlayout.setError("Please input a correct Username!!");
            email.setError("Please input a correct Username!!");
            return false;
        }
        emailTextlayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkpassword() {
        if (password.getText().toString().trim().isEmpty()) {
            passwordTextlayout.setErrorEnabled(true);
            passwordTextlayout.setError("Please input Password!!");
            password.setError("Please input Password!!");
            return false;
        }
        passwordTextlayout.setErrorEnabled(false);
        return true;
    }

    private void showProgressDialogSuccessRegister() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Login Successfully!!");
        pDialog.show();
        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity(new Intent(LoginActivity.this, Main2Activity.class));
                finish();
            }
        });
    }

    private void showProgressDialogLoginfail() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("Incorrect Email or Password!!");
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
}
