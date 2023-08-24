
package com.caloriecounter.calorie.ui.newlivewallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.iinterface.DataChange;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "CardAdapter";
    private final Context context;
    private final List<WallpaperCard> cards;
    private final OnCardClickedListener listener;
    private boolean removable = false;
    private DataChange dataChange;

    public interface OnCardClickedListener {
        void onCardClicked(@NonNull final WallpaperCard wallpaperCard);
        void onApplyButtonClicked(@NonNull final WallpaperCard wallpaperCard);
        void onCardInvalid(@NonNull final WallpaperCard wallpaperCard);
    }

    CardAdapter(
        @NonNull final Context context,
        @NonNull final List<WallpaperCard> cards,
        @NonNull final OnCardClickedListener listener,
        DataChange dataChange
    ) {
        super();
        this.context = context;
        this.cards = cards;
        this.listener = listener;
        this.dataChange = dataChange;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
            R.layout.item_image, viewGroup, false
        );
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder cardViewHolder, int i) {
        final WallpaperCard card = cards.get(i);
        cardViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherApplication.trackingEvent("Click_Item_Live_Downloaded");
                listener.onCardClicked(card);
            }
        });

        Glide.with(context).load("o").into(cardViewHolder.thumbnail);

        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<Bitmap> emitter) throws Exception {
                Bitmap bitmap = Utils.createVideoThumbnailFromUri(context, card.getUri());
                if(bitmap != null) {
                    emitter.onNext(bitmap);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap o) throws Exception {
                try {
                    Glide.with(context).load(o).into(cardViewHolder.thumbnail);
                } finally {

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        try {
            if(cards.size() > 0){
                dataChange.onDataChange(false);
            }else {
                dataChange.onDataChange(true);
            }
            return cards.size();
        } catch (Exception e) {
            dataChange.onDataChange(true);
            return 0;
        }
    }
}

class CardViewHolder extends RecyclerView.ViewHolder {
    @SuppressWarnings("unused")
    private static final String TAG = "CardViewHolder";
    final ImageView thumbnail;

    CardViewHolder(View view) {
        super(view);
        thumbnail = view.findViewById(R.id.imgAvatar);
    }
}
