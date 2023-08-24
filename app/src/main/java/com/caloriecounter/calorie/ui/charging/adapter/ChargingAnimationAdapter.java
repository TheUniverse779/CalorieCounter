package com.caloriecounter.calorie.ui.charging.adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.ui.charging.AnimationLoader;
import com.caloriecounter.calorie.ui.charging.AnimationSetting;
import com.caloriecounter.calorie.ui.charging.Utils;
import com.caloriecounter.calorie.ui.charging.model.ChargingAnimation;


import java.util.ArrayList;

public abstract class ChargingAnimationAdapter extends RecyclerView.Adapter<ChargingAnimationAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<ChargingAnimation> chargingAnimations;

    public ChargingAnimationAdapter(Context context, ArrayList<ChargingAnimation> chargingAnimations) {
        this.context = context;
        this.chargingAnimations = chargingAnimations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_charging_animation,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChargingAnimation chargingAnimation = chargingAnimations.get(position);
        Glide.with(context)
                .load(AnimationLoader.getPreviewLink(chargingAnimation.getFileName()))
                .transition(withCrossFade())
                .into(holder.imgPreview);

        if(AnimationLoader.isDownload(chargingAnimation.getFileName())){
            holder.imgDownloaded.setVisibility(View.GONE);
        }else {
            holder.imgDownloaded.setVisibility(View.VISIBLE);
        }
        if(position>chargingAnimations.size()-(chargingAnimations.size()%2==0?2:1)){
            holder.viewFix.setVisibility(View.VISIBLE);
        }else {
            holder.viewFix.setVisibility(View.GONE);
        }
        if(chargingAnimation.getFileName().equals(AnimationSetting.getChargingAnimationFile())){
            holder.imgChecked.setVisibility(View.VISIBLE);
        }else {
            holder.imgChecked.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chargingAnimations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPreview;
        private final ImageView imgDownloaded;
        private final ImageView imgChecked;
        private final View viewFix;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPreview = itemView.findViewById(R.id.img_preview);
            imgDownloaded = itemView.findViewById(R.id.img_downloaded);
            imgChecked = itemView.findViewById(R.id.img_checked);
            viewFix = itemView.findViewById(R.id.view_fix);
            View item = itemView.findViewById(R.id.item);

            float w = (context.getResources().getDisplayMetrics().widthPixels- Utils.dp()*(float) 40)/2;
            imgPreview.getLayoutParams().width = (int) w;
            imgPreview.getLayoutParams().height = (int) (w*1080/720);
            item.setOnClickListener(v -> OnItemClick(getAdapterPosition()));
        }
    }

    public abstract void OnItemClick(int position);
}
