package com.SimiColon.MobileSearch.findphone.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.SimiColon.MobileSearch.findphone.Model.User_Model;
import com.SimiColon.MobileSearch.findphone.R;
import com.SimiColon.MobileSearch.findphone.Services.Preferences;
import com.SimiColon.MobileSearch.findphone.Services.Service;
import com.SimiColon.MobileSearch.findphone.Services.ServiceApi;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jkb.vcedittext.VerificationCodeEditText;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Login extends AppCompatActivity implements View.OnClickListener {

    private Button register, login;
    private EditText email, pass;
    private ProgressDialog pDialog;
    private Preferences preferences;
    private String session,id;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String ids;
    private String vercode;
    private FirebaseAuth mAuth;
    private Dialog dialog;
    private VerificationCodeEditText verificationCodeEditText;
    private ProgressDialog dialo;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Runnable mUpdateResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = new Preferences(this);
        session = preferences.getSession();
        if (!TextUtils.isEmpty(session)&&session!=null)
        {
            id = preferences.getId();
            if (!TextUtils.isEmpty(id) &&id !=null)
            {
                Intent i = new Intent(Activity_Login.this, MainActivity.class);
                i.putExtra("user_id",id);
                startActivity(i);
                finish();
            }
        }
        initView();
    }

    private void initView() {
        authn();
        email = findViewById(R.id.edt_email);
        pass = findViewById(R.id.edt_password);
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.btn_register);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_register:
                Intent intent = new Intent(this, Activity_Register.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_login:
                Login();
                break;
        }
    }

    private void Login() {

        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError(getString(R.string.empty_email));
        } else if (TextUtils.isEmpty(pass.getText().toString())) {
            pass.setError(getString(R.string.empty_password));
        } else {
            loginByServer();

        }
    }

    private void loginByServer() {

        pDialog = new ProgressDialog(Activity_Login.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage(getString(R.string.login));
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);
        showpDialog();
        final String uemail = email.getText().toString();
        final String upass = pass.getText().toString();


        Service service = ServiceApi.createClient().create(Service.class);
        Call<User_Model> userCall = service.userLogIn(uemail, upass);

        userCall.enqueue(new Callback<User_Model>() {
            @Override
            public void onResponse(Call<User_Model> call, Response<User_Model> response) {
                hidepDialog();

                if (response.isSuccessful()) {

                  if (response.body().getMessage()==1) {

                      String user_id=response.body().getUser_id();
                        Intent i = new Intent(Activity_Login.this, MainActivity.class);
                        i.putExtra("user_id",user_id);
                        preferences.CreateSharedPref(user_id);
                        startActivity(i);
                        finish();
                  } else {
                      Toast.makeText(Activity_Login.this, getString(R.string.faild), Toast.LENGTH_SHORT).show();
                      /*Intent i = new Intent(Activity_Login.this, MainActivity.class);
                      startActivity(i);
                      finish();*/
                  }
                } else {
                    Toast.makeText(Activity_Login.this,getString(R.string.faild), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<User_Model> call, Throwable t) {
                hidepDialog();
                Toast.makeText(Activity_Login.this, getString(R.string.faild), Toast.LENGTH_SHORT).show();

                Log.d("onFailure", t.toString());
            }
        });
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void authn() {

        mAuth= FirebaseAuth.getInstance();
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //  super.onCodeSent(s, forceResendingToken);
                ids=s;
                mResendToken=forceResendingToken;
                Log.e("authid",ids);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) { ;
//phoneAuthCredential.getProvider();

                if(phoneAuthCredential.getSmsCode()!=null){
                    verificationCodeEditText.setText(phoneAuthCredential.getSmsCode());
                    siginwithcredental(phoneAuthCredential);}
                else {
                    siginwithcredental(phoneAuthCredential);
                }



            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("llll",e.getMessage());
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                //   super.onCodeAutoRetrievalTimeOut(s);
                Log.e("data",s);
                //   mUpdateResults.run();


            }
        };

    }
    private void verfiycode(String code) {

        if(ids!=null){
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(ids,code);

            siginwithcredental(credential);}
    }

    private void siginwithcredental(PhoneAuthCredential credential) {

        dialo = new ProgressDialog(Activity_Login.this);
        dialog.setCancelable(false);
        dialog.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialo.dismiss();
                dialog.dismiss();
                if(task.isSuccessful()){

                }

            }
        });
    }

    public void sendverficationcode(final String phone, final String phone_code) {
        dialog.show();
        Log.e("kkk",phone_code+phone);
        mUpdateResults = new Runnable() {
            public void run() {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_code+phone,10, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,  mCallbacks);
            }
        };
        mUpdateResults.run();

    }
}
