package com.SimiColon.MobileSearch.findphone.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Login extends AppCompatActivity implements View.OnClickListener {

    private Button register, login;
    private EditText email, pass;
    private ProgressDialog pDialog;
    private Preferences preferences;
    private String session,id;


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
                Intent i = new Intent(Activity_Login.this, ActivityReportResult.class);
                i.putExtra("user_id",id);
                startActivity(i);
                finish();
            }
        }
        initView();
    }

    private void initView() {
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
                        Intent i = new Intent(Activity_Login.this, ActivityReportResult.class);
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

}
