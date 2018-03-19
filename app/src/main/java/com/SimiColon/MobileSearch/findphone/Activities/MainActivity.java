package com.SimiColon.MobileSearch.findphone.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.SimiColon.MobileSearch.findphone.R;

public class MainActivity extends AppCompatActivity {

    Button report,search;

    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getDataFromIntent();
    }

    private void getDataFromIntent() {

        Intent intent=getIntent();
        user_id=  intent.getStringExtra("user_id");
    }

    private void initView() {

        report=findViewById(R.id.btn_report);
        search=findViewById(R.id.search);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ActivityReport.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ActivityReportResult.class);
                startActivity(intent);
            }
        });
    }
}
