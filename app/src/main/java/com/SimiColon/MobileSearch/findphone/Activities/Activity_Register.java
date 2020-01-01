package com.SimiColon.MobileSearch.findphone.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.SimiColon.MobileSearch.findphone.Model.User_Model;
import com.SimiColon.MobileSearch.findphone.R;
import com.SimiColon.MobileSearch.findphone.Services.Preferences;
import com.SimiColon.MobileSearch.findphone.Services.Service;
import com.SimiColon.MobileSearch.findphone.Services.ServiceApi;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preferences = new Preferences(this);
        initView();

    }

    private void initView() {

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
        }else
        {
            address.setError(null);
        }

        saveToServerDB();



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
}
