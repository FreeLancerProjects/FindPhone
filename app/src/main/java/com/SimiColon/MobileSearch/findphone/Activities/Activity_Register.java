package com.SimiColon.MobileSearch.findphone.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SimiColon.MobileSearch.findphone.Model.User_Model;
import com.SimiColon.MobileSearch.findphone.R;
import com.SimiColon.MobileSearch.findphone.Services.Preferences;
import com.SimiColon.MobileSearch.findphone.Services.Service;
import com.SimiColon.MobileSearch.findphone.Services.ServiceApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jkb.vcedittext.VerificationCodeEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Activity_Register extends AppCompatActivity implements View.OnClickListener {

    private EditText name,phone,email,city,country,address,password;
    private CircleImageView profileImg;
    private final int IMAGE_REQ=120;
    private String enCodedImage="";
    private Button register;
    private ProgressDialog pDialog;
    private TextView login;
    private Preferences preferences;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String ids;
    private String vercode;
    private FirebaseAuth mAuth;
    private Dialog dialog;
    private EditText verificationCodeEditText;
    private ProgressDialog dialo;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Runnable mUpdateResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preferences = new Preferences(this);
        initView();

    }

    private void initView() {
        authn();
        CreateSignAlertDialog();
        name=findViewById(R.id.edt_name);
        phone=findViewById(R.id.edt_phone);
        email=findViewById(R.id.edt_email);
        city=findViewById(R.id.edt_city);
        country=findViewById(R.id.edt_country);
        address=findViewById(R.id.edt_address);
        password=findViewById(R.id.edt_pass);
        profileImg=findViewById(R.id.img_profile);
        register=findViewById(R.id.btn_register);
        login = findViewById(R.id.loginBtn);
        profileImg.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);



    }
    public void signUp() {
if(TextUtils.isEmpty(name.getText().toString())||TextUtils.isEmpty(password.getText().toString())||TextUtils.isEmpty(email.getText().toString())||!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()||TextUtils.isEmpty(phone.getText().toString())||TextUtils.isEmpty(city.getText().toString())

||TextUtils.isEmpty(country.getText().toString())||TextUtils.isEmpty(address.getText().toString())||!Patterns.PHONE.matcher(phone.getText().toString()).matches()
){
        if (TextUtils.isEmpty(name.getText().toString()))
        {
            name.setError(getString(R.string.name));
        }else
        {
            name.setError(null);
        }
        if (TextUtils.isEmpty(password.getText().toString()))
        {
            password.setError(getString(R.string.empty_password));
        }else
        {
            password.setError(null);
        }
        if (TextUtils.isEmpty(email.getText().toString()))
        {
            email.setError(getString(R.string.empty_email));
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
        {
            email.setError(getString(R.string.inv_email));
        }else
        {
            email.setError(null);
        }
        if (TextUtils.isEmpty(phone.getText().toString()))
        {
            phone.setError(getString(R.string.empty_phone));
        }else if (!Patterns.PHONE.matcher(phone.getText().toString()).matches())
        {
            phone.setError(getString(R.string.inv_phone));
        }else
        {
            phone.setError(null);
        }
        if (TextUtils.isEmpty(city.getText().toString()))
        {
            city.setError(getString(R.string.empty_city));
        }else
        {
            city.setError(null);
        }
        if (TextUtils.isEmpty(country.getText().toString()))
        {
            country.setError(getString(R.string.empty_country));
        }else
        {
            country.setError(null);
        }
        if (TextUtils.isEmpty(address.getText().toString()))
        {
            address.setError(getString(R.string.empty_address));
        }else {
            address.setError(null);
        }      }
else {
    sendverficationcode(phone.getText().toString(), "+20");
}




    }

    private void saveToServerDB() {
        pDialog = new ProgressDialog(Activity_Register.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("create");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);

        showpDialog();

        String uname = name.getText().toString();
        String upass = password.getText().toString();
        String uemail = email.getText().toString();
        String uphone = phone.getText().toString();
        String ucity = city.getText().toString();
        String ucountry = phone.getText().toString();
        String uaddress = address.getText().toString();
        
        Service services = ServiceApi.createClient().create(Service.class);
        Call<User_Model> userCall = services.userSignUp(uname,upass,enCodedImage,uphone,uemail,ucountry,ucity,uaddress);
        userCall.enqueue(new Callback<User_Model>() {
            @Override
            public void onResponse(Call<User_Model> call, Response<User_Model> response) {
                hidepDialog();



                    if (response.isSuccessful()) {


                        Intent i = new Intent(Activity_Register.this, MainActivity.class);
                        i.putExtra("user_id",response.body().getUser_id());
                        preferences.CreateSharedPref(response.body().getUser_id());
                        startActivity(i);
                        finish();

                    }else
                {
                    Toast.makeText(Activity_Register.this, "" +getString(R.string.faild), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<User_Model> call, Throwable t) {
                hidepDialog();
                Toast.makeText(Activity_Register.this, "" +getString(R.string.faild), Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
              signUp();
                break;

            case R.id.img_profile:
                SelectPhoto();
                break;

            case R.id.loginBtn:
                Back();
                break;
        }

    }


    private void SelectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,getString(R.string.choose_photo)),IMAGE_REQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQ && resultCode==RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                profileImg.setImageBitmap(bitmap);
                enCodedImage = enCodeImage(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String enCodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();

        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    private void Back()
    {
        Intent i = new Intent(this, Activity_Login.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Back();
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

        dialo = new ProgressDialog(Activity_Register.this);
        dialog.setCancelable(false);
        dialog.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialo.dismiss();
                dialog.dismiss();
                if(task.isSuccessful()){
                    saveToServerDB();
                }

            }
        });
    }

    public void sendverficationcode(String phone, final String phone_code) {
        dialog.show();
        Log.e("kkk",phone_code+phone);
        if(phone.startsWith("0")){
            phone=phone.replaceFirst("0","");
        }
        final String finalPhone = phone;
        mUpdateResults = new Runnable() {
            public void run() {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_code+ finalPhone,10, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,  mCallbacks);
            }
        };
        mUpdateResults.run();

    }
    public void CreateSignAlertDialog() {
        dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_login);
        LinearLayout ll = dialog.findViewById(R.id.ll);
        verificationCodeEditText=dialog.findViewById(R.id.edt_ver);
        ll.setBackgroundResource(R.drawable.custom_bg_login);
        Button confirm=dialog.findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vercode=verificationCodeEditText.getText().toString();
                if(TextUtils.isEmpty(vercode)){
                    verificationCodeEditText.setError(getResources().getString(R.string.verfication_code));
                }
                else {
                    Log.e("code",vercode);
                    verfiycode(vercode);

                }
            }
        });
    }

}
