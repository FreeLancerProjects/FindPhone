package com.SimiColon.MobileSearch.findphone.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.SimiColon.MobileSearch.findphone.R;

public class Activity_Login extends AppCompatActivity implements View.OnClickListener {

    private Button regBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }
    private void initView() {
        regBtn  = findViewById(R.id.regBtn);
        regBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.regBtn:
                Intent  intent = new Intent(this,Activity_Register.class);
                startActivity(intent);
                break;
        }
    }
}
