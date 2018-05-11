package com.SimiColon.MobileSearch.findphone.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.SimiColon.MobileSearch.findphone.Adapter.AllPhonesAdapter;
import com.SimiColon.MobileSearch.findphone.Model.Report_Model;
import com.SimiColon.MobileSearch.findphone.R;
import com.SimiColon.MobileSearch.findphone.Services.Preferences;
import com.SimiColon.MobileSearch.findphone.Services.Service;
import com.SimiColon.MobileSearch.findphone.Services.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityReportResult extends AppCompatActivity {

    ArrayList<Report_Model> model;
    AllPhonesAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progBar;
    private LinearLayout nodata_container;
    private SwipeRefreshLayout sr;
    private SearchView searchView;
    private AlertDialog alertDialog;
    private String user_id;
    private Toolbar toolbar;
    private Preferences preferences;
    private int couter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_result);
        preferences = new Preferences(this);
        initView();
        getDataFromServer();
        getDataFromIntent();
        CreateAlertDialog();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String im) {
                // Toast.makeText(Home.this, ""+s, Toast.LENGTH_SHORT).show();
                searchResult(im);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                //getDataFromServer();
                return false;
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getDataFromServer();

                return false;
            }
        });


    }

    private void getDataFromIntent() {
        Intent intent=getIntent();
        if (intent!=null)
        {
            user_id=  intent.getStringExtra("user_id");

        }
    }

    private void CreateAlertDialog() {
        alertDialog = new AlertDialog.Builder(this)
                .setMessage("لاتوجد نتائج البحث هل تريد التبليغ عن هذا الجوال")
                .setCancelable(false)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(ActivityReportResult.this,ActivityReport.class);
                        intent.putExtra("user_id",user_id);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                }).create();

    }

    private void getDataFromServer() {

        Service service = ServiceApi.createClient().create(Service.class);
        Call<List<Report_Model>> call = service.getallphones();
        call.enqueue(new Callback<List<Report_Model>>() {
            @Override
            public void onResponse(Call<List<Report_Model>> call, Response<List<Report_Model> > response) {

                model.clear();
                model.addAll( response.body());


                if (model.size()>0){
                    adapter.notifyDataSetChanged();
                    progBar.setVisibility(View.GONE);
                    sr.setRefreshing(false);
                }else {
                    progBar.setVisibility(View.GONE);
                    nodata_container.setVisibility(View.VISIBLE);
                    sr.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<List<Report_Model>> call, Throwable t) {
                nodata_container.setVisibility(View.GONE);
                sr.setRefreshing(false);
            }
        });
    }

    private void searchResult(String im){
        Service service = ServiceApi.createClient().create(Service.class);

        Call<List<Report_Model>> callApi = service.findephone(im);
        callApi.enqueue(new Callback<List<Report_Model>>() {
            @Override
            public void onResponse(Call<List<Report_Model>> call, Response<List<Report_Model>> response) {




                if (model.size()>0)
                {
                    model.clear();
                    model.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }else
                    {
                        alertDialog.show();
                    }
                //  Toast.makeText(Home.this, ""+response.body().get(0).getService_title(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<Report_Model>> call, Throwable t) {
                // Toast.makeText(Home.this, "rrrrr", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        sr = findViewById(R.id.sr);
        sr.setRefreshing(false);
        searchView=findViewById(R.id.searchView);
        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        nodata_container = findViewById(R.id.nodata_container);
        sr.setColorSchemeResources(R.color.colorPrimary);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });
       
        recyclerView = findViewById(R.id.rec_reports);
        model = new ArrayList<>();
        mLayoutManager=new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new AllPhonesAdapter(this, model);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.logout)
        {
            LogOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        preferences.ClearSharedPref();
        Intent intent = new Intent(ActivityReportResult.this,Activity_Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        couter++;
        if (couter==1)
        {
            getDataFromServer();
        }else if (couter==2)
        {
            couter=0;
            super.onBackPressed();

        }
    }
}
