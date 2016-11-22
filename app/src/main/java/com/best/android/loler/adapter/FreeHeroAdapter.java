package com.best.android.loler.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.best.android.loler.R;
import com.best.android.loler.config.NetConfig;
import com.best.android.loler.http.LOLBoxApi;
import com.best.android.loler.manager.PhotoManager;
import com.best.android.loler.model.HeroInfo;

import java.util.List;

/**
 * Created by BL06249 on 2015/11/26.
 */
public class FreeHeroAdapter extends RecyclerView.Adapter<FreeHeroAdapter.FreeHeroViewHolder> {

    private Context context;
    private HeroInfo[] heroInfos;
    private RequestQueue mQueue;

    public FreeHeroAdapter(Context context, HeroInfo[] heroInfos){
        this.context = context;
        this.heroInfos = heroInfos;
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public FreeHeroAdapter.FreeHeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_free_hero, parent, false);
        FreeHeroViewHolder holder = new FreeHeroViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FreeHeroViewHolder holder, int position) {
        holder.tvName.setText(heroInfos[position].cnName);

        String url = LOLBoxApi.getHeroPhotoUrl(heroInfos[position].enName);
        Bitmap bitmap = PhotoManager.getInstance().getBitmapFromMemCache(url);
        if(bitmap == null)
            loadImage(holder, url);
        else{
            holder.ivPhoto.setImageBitmap(bitmap);
        }
    }

    private void loadImage(final FreeHeroViewHolder holder, final String url) {
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
    public int getItemCount() {
        return heroInfos.length;
    }

    class FreeHeroViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivPhoto;
        public FreeHeroViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.free_hero_tv_name);
            ivPhoto = (ImageView) view.findViewById(R.id.free_hero_iv_photo);
        }
    }

}
