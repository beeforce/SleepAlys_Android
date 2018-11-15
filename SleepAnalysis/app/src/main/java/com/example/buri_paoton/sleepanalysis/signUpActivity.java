package com.example.buri_paoton.sleepanalysis;

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

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class signUpActivity extends AppCompatActivity {

    private Vibrator vibrator;
    private Animation anim;
    private EditText email, password, repassword, name, birthdate;
    private TextInputLayout emailTextlayout, passwordTextlayout, passwordagainTextlayout, nameTextlayout,
                            birthdateTextlayout;

    private SweetAlertDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Animation
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
        anim.setRepeatCount(2);

        //vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //TextinputLayout
        emailTextlayout =  findViewById(R.id.emailTextlayout);
        passwordTextlayout =  findViewById(R.id.passwordTextlayout);
        passwordagainTextlayout =  findViewById(R.id.passwordagainTextlayout);
        nameTextlayout = findViewById(R.id.nameTextlayout);
        birthdateTextlayout = findViewById(R.id.birthdatelayout);

        //EditText
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.passwordagain);
        name = findViewById(R.id.nameText);
        birthdate = findViewById(R.id.birthdate);




        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkEmail() || !checkpassword() || !checkpasswordagain() || !checkname() || !checkbirthdate()) {
                    email.setAnimation(anim);
                    password.setAnimation(anim);
                    repassword.setAnimation(anim);
                    name.setAnimation(anim);
                    birthdate.setAnimation(anim);
                    checkEmail();
                    checkpassword();
                    checkpasswordagain();
                    checkname();
                    checkbirthdate();
                    vibrator.vibrate(120);
                    return;
                } else {
                    showProgressDialogSuccessRegister();
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
            emailTextlayout.setError("Please input Username!!");
            return false;
        }
        if (!isValidEmail(email.getText().toString())) {
            emailTextlayout.setErrorEnabled(true);
            emailTextlayout.setError("Please input a correct Username!!");
            return false;
        }
        emailTextlayout.setErrorEnabled(false);
        return true;
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
        pDialog.setTitleText("Register Successfully!!");
        pDialog.show();
        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity(new Intent(signUpActivity.this, Main2Activity.class));
                    finish();
            }
        });
    }
}
