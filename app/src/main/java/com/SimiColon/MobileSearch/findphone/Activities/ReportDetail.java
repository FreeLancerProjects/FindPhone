package com.SimiColon.MobileSearch.findphone.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.SimiColon.MobileSearch.findphone.R;
import com.squareup.picasso.Picasso;

public class ReportDetail extends AppCompatActivity {

    String imei, brand, owner, statue, phone, email, address, photo;
    TextView timei, tbrand, towner, tstatue, tphone, temail, taddress;
    ImageView img,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);


        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {

            imei = intent.getStringExtra("imei");
            brand = intent.getStringExtra("brand");
            owner = intent.getStringExtra("owner");
            statue = intent.getStringExtra("statu");
            phone = intent.getStringExtra("phone");
            email = intent.getStringExtra("email");
            address = intent.getStringExtra("adress");
            photo = intent.getStringExtra("photo");

        }
    }

    private void initView() {

         timei=findViewById(R.id.txt_imei);
         tbrand =findViewById(R.id.txt_brand);
         towner =findViewById(R.id.txt_owner);
         tstatue=findViewById(R.id.txt_statue);
         tphone =findViewById(R.id.txt_phone);
         temail =findViewById(R.id.txt_email);
         taddress=findViewById(R.id.txt_address);
         img=findViewById(R.id.img_phone);
         back=findViewById(R.id.back);

         timei.setText(imei);
         tbrand.setText(brand);
         towner .setText( owner );
         tstatue.setText( statue);
         tphone .setText( phone );
         temail .setText( email );
         taddress.setText( address);

        Picasso.with(this).load("http://mobilost.semicolonsoft.com/uploads/images/"+photo).into(img);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReportDetail.this,ActivityReportResult.class);
                startActivity(intent);
            }
        });
    }
}
