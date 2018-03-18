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
import android.widget.Toast;

import com.SimiColon.MobileSearch.findphone.Model.Report_Model;
import com.SimiColon.MobileSearch.findphone.R;
import com.SimiColon.MobileSearch.findphone.Services.Service;
import com.SimiColon.MobileSearch.findphone.Services.ServiceApi;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;


public class ActivityReport extends AppCompatActivity implements View.OnClickListener {

    EditText imei,brand,owner,statue,phone,email,address,description;
    CircleImageView phoneImage;
    private final int IMAGE_REQ=120;
    private String enCodedImage="";
    Button send;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        initView();

    }
  
    private void initView() {

        imei=findViewById(R.id.edt_imei);
        brand=findViewById(R.id.edt_brand);
        owner=findViewById(R.id.edt_owner);
        statue=findViewById(R.id.edt_statue);
        phone=findViewById(R.id.edt_phone);
        email=findViewById(R.id.edt_email);
        address=findViewById(R.id.edt_address);
        description=findViewById(R.id.edt_desc);
        phoneImage=findViewById(R.id.img_phone);
        send=findViewById(R.id.btn_send);

       phoneImage.setOnClickListener(this);
        send.setOnClickListener(this);



    }
    public void sendReport() {

        if (TextUtils.isEmpty(imei.getText().toString()))
        {
            imei.setError(getString(R.string.name));
        }else
        {
            imei.setError(null);
        }
        if (TextUtils.isEmpty(brand.getText().toString()))
        {
            brand.setError(getString(R.string.empty_password));
        }else
        {
            brand.setError(null);
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
        if (TextUtils.isEmpty(statue.getText().toString()))
        {
            statue.setError(getString(R.string.empty_city));
        }else
        {
            statue.setError(null);
        }
        if (TextUtils.isEmpty(description.getText().toString()))
        {
            description.setError(getString(R.string.empty_country));
        }else
        {
            description.setError(null);
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
        pDialog = new ProgressDialog(ActivityReport.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("send");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);

        showpDialog();

        final String uimei = imei.getText().toString();
        String ubrand = brand.getText().toString();
        String uemail = email.getText().toString();
        String uphone = phone.getText().toString();
        String uaddress = address.getText().toString();
        String uowner = owner.getText().toString();
        String ustatue = statue.getText().toString();
        String udesc = description.getText().toString();
        Service services = ServiceApi.createClient().create(Service.class);
        Call<Report_Model> userCall = services.reportphone(uimei,ubrand,uowner,ustatue,uphone,uemail,uaddress,enCodedImage,udesc);
        userCall.enqueue(new Callback<Report_Model>() {
            @Override
            public void onResponse(Call<Report_Model> call, Response<Report_Model> response) {
                hidepDialog();



                if (response.isSuccessful()) {

                    Toast.makeText(ActivityReport.this, ""+uimei, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityReport.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else
                {
                    Toast.makeText(ActivityReport.this, "555" +getString(R.string.faild), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Report_Model> call, Throwable t) {
                hidepDialog();
                Toast.makeText(ActivityReport.this, "" +getString(R.string.faild), Toast.LENGTH_SHORT).show();

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
            case R.id.btn_send:
                sendReport();
                break;

            case R.id.img_phone:
                SelectPhoto();
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
                phoneImage.setImageBitmap(bitmap);
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
}
