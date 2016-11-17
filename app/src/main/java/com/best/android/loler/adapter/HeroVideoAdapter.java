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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.best.android.loler.R;
import com.best.android.loler.activity.VideoActivity;
import com.best.android.loler.config.NetConfig;
import com.best.android.loler.manager.PhotoManager;
import com.best.android.loler.model.VideoInfo;

import java.util.List;

/**
 * Created by BL06249 on 2015/12/1.
 */
public class HeroVideoAdapter extends RecyclerView.Adapter<HeroVideoAdapter.VideoViewHolder> implements OnItemClickListener {

    private Context context;
    private List<VideoInfo> videoList;
    private RequestQueue mQueue;

    public HeroVideoAdapter(Context context, List<VideoInfo> videoList) {
        this.context = context;
        this.videoList = videoList;
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_video, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
        holder.tvTitle.setText(videoList.get(position).title);
        holder.tvTime.setText(videoList.get(position).uploadTime);

        String url = videoList.get(position).coverUrl;
        Bitmap bitmap = PhotoManager.getInstance().getBitmapFromMemCache(url);
        if(bitmap == null) {
            holder.ivVideoPic.setImageResource(R.drawable.no_photo_120);
            loadImage(holder, url);
        }
        else{
            holder.ivVideoPic.setImageBitmap(bitmap);
        }
    }

    private void loadImage(final VideoViewHolder holder, final String url) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.ivVideoPic.setImageBitmap(response);
                        //加入缓存，但是不保存到本地
                        PhotoManager.getInstance().addBitmapToMemCache(url, response, false);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.ivVideoPic.setImageResource(R.drawable.no_photo_120);
                    }
                });
        mQueue.add(imageRequest);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("vid", videoList.get(position).vid);
        intent.putExtra("vname", videoList.get(position).title);
        context.startActivity(intent);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        ImageView ivVideoPic;
        TextView tvTime;
        TextView tvTitle;

        public VideoViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.adapter_item_video_layout);
            ivVideoPic = (ImageView)itemView.findViewById(R.id.adapter_item_video_iv_pic);
            tvTime = (TextView)itemView.findViewById(R.id.adapter_item_video_tv_time);
            tvTitle = (TextView)itemView.findViewById(R.id.adapter_item_video_tv_title);
        }
    }
}
