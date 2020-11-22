package com.anguomob.total.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.anguomob.total.bean.AnguoAdParams;
import com.anguomob.total.R;
import com.anguomob.total.utils.PackageUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class OtherAppAdapter extends RecyclerView.Adapter<OtherAppAdapter.ViewHolder> {

    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other_app, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    private static final String TAG = "OtherAppAdapter";
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final AnguoAdParams anguoAdParams = mDataList.get(position);
        Glide.with(context)
                .load(anguoAdParams.getLogo_url())
                .into(holder.ivLogo);
        holder.tvTitle.setText(anguoAdParams.getName());
        holder.tvDesc.setText(anguoAdParams.getApp_desc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isApplicationAvilible(context, anguoAdParams.getPackage_name())) {
                    PackageUtils.openPackage(context, anguoAdParams.getPackage_name());
                } else {
                    Log.e(TAG, "onClick:不可以直接打开"+anguoAdParams.getName() );
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(anguoAdParams.getDown_app_url()));
                    context.startActivity(it);
                }

            }
        });

    }

    /**
     * 判断手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<AnguoAdParams> mDataList;

    public void setData(List<AnguoAdParams> mNoteList) {
        this.mDataList = mNoteList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivLogo;
        private TextView tvTitle;
        private TextView tvDesc;

        public ViewHolder(View view) {
            super(view);
            ivLogo = itemView.findViewById(R.id.ivIOALogo);
            tvTitle = itemView.findViewById(R.id.tvIOATitle);
            tvDesc = itemView.findViewById(R.id.tvIODesc);
        }
    }
}
