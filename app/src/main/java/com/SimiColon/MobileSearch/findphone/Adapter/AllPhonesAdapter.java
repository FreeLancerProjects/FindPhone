package com.SimiColon.MobileSearch.findphone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.SimiColon.MobileSearch.findphone.Activities.ActivityReportResult;
import com.SimiColon.MobileSearch.findphone.Activities.ReportDetail;
import com.SimiColon.MobileSearch.findphone.Model.Report_Model;
import com.SimiColon.MobileSearch.findphone.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;




public class AllPhonesAdapter extends RecyclerView.Adapter<AllPhonesAdapter.Holder> {
    Context context;
    ArrayList<Report_Model> reports;
    Report_Model report_model;
    ActivityReportResult activityReportResult;
    private Target target;

    public AllPhonesAdapter(Context context, ArrayList<Report_Model> reports) {
        this.context = context;
        this.reports = reports;
        activityReportResult = (ActivityReportResult) context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
         report_model=reports.get(position);
         holder.card.setTag(position);

        holder.BindData(report_model);



    }

    @Override
    public int getItemCount() {
        return  reports.size();
    }



    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView imei, brand,statue;
        ImageView image;
        CardView card;


         Holder(View itemView) {
            super(itemView);

            imei=itemView.findViewById(R.id.txt_imei);
            brand=itemView.findViewById(R.id.txt_brand);
            image=itemView.findViewById(R.id.img_phone);
            statue = itemView.findViewById(R.id.txt_statue);
            card=itemView.findViewById(R.id.card);

            card.setOnClickListener(this);
        }

         void BindData(Report_Model report_model)
        {
            imei.setText(report_model.getImei());
            brand.setText(report_model.getBrand());
            statue.setText(report_model.getStatue());

            Picasso.with(context).load("http://mobilost.semicolonsoft.com/uploads/images/"+report_model.getPhoto()).into(image);

           /* target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    image.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            if (report_model.getPhoto()==null|| TextUtils.isEmpty(report_model.getPhoto())||report_model.getPhoto().equals("0"))
            {
                Picasso.with(context).load(R.drawable.phone_icon).into(target);
            }else
                {
                    Picasso.with(context).load(Uri.parse("http://mobilost.semicolonsoft.com/uploads/images/"+report_model.getPhoto())).into(target);

                }
*/
        }

        @Override
        public void onClick(View view) {

            int position =getAdapterPosition();
            report_model = reports.get(position);
            Intent i = new Intent(context,ReportDetail.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("imei"       ,report_model.getImei());
            i.putExtra("brand"      ,report_model.getBrand());
            i.putExtra("owner"      ,report_model.getOwner());
            i.putExtra("statu"      ,report_model.getStatue());
            i.putExtra("phone"      ,report_model.getPhone());
            i.putExtra("email"      ,report_model.getEmail());
            i.putExtra("adress"     ,report_model.getAdress());
            i.putExtra("photo"      ,report_model.getPhoto());
            i.putExtra("description",report_model.getDescription());
            context.startActivity(i);


        }
    }
}
