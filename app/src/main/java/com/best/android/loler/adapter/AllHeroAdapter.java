package com.best.android.loler.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.best.android.loler.R;
import com.best.android.loler.activity.HeroInfoActivity;
import com.best.android.loler.activity.VideoActivity;
import com.best.android.loler.config.NetConfig;
import com.best.android.loler.manager.PhotoManager;
import com.best.android.loler.model.HeroInfo;

import java.util.List;

/**
 * Created by BL06249 on 2015/12/2.
 */
public class AllHeroAdapter extends RecyclerView.Adapter<AllHeroAdapter.HeroViewHolder> implements OnItemClickListener{

    private Context context;
    private HeroInfo heroInfos[];
    private RequestQueue mQueue;


    public AllHeroAdapter(Context context, HeroInfo heroInfos[]) {
        this.context = context;
        this.heroInfos = heroInfos;
        this.mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.adapter_item_hero, parent, false);
        HeroViewHolder heroViewHolder = new HeroViewHolder(rootView);
        return heroViewHolder;
    }

    @Override
    public void onBindViewHolder(HeroViewHolder holder, final int position) {
        String price = heroInfos[position].price;
        price = price.substring(0, price.indexOf(","));
        holder.tvPrice.setText(price);

        String url = NetConfig.getHeroPhotoUrl(heroInfos[position].enName);
        Bitmap bitmap = PhotoManager.getInstance().getBitmapFromMemCache(url);
        if(bitmap == null)
            loadImage(holder, url);
        else{
            holder.ivPhoto.setImageBitmap(bitmap);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return heroInfos.length;
    }

    private void loadImage(final HeroViewHolder holder, final String url) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.ivPhoto.setImageBitmap(response);
                        PhotoManager.getInstance().addBitmapToMemCache(url, response, true);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.ivPhoto.setImageResource(R.drawable.no_photo_120);
                    }
                });
        mQueue.add(imageRequest);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, HeroInfoActivity.class);
        intent.putExtra("hero", heroInfos[position]);
        context.startActivity(intent);
    }

    class HeroViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout relativeLayout;
        private ImageView ivPhoto;
        private TextView tvPrice;

        public HeroViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.adapter_item_hero_relativelayout);
            ivPhoto = (ImageView)itemView.findViewById(R.id.adapter_item_hero_iv_photo);
            tvPrice = (TextView)itemView.findViewById(R.id.adapter_item_hero_tv_price);
        }
    }

}
