package com.best.android.loler.manager;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.best.android.loler.LOLApplication;
import com.best.android.loler.R;

/**
 * Created by BL06249 on 2016/1/6.
 */
public class ImageLoadManager {

    private static RequestQueue mQueue = Volley.newRequestQueue(LOLApplication.getInstance().getApplicationContext());

    public static void loadImage(final ImageView imageView, final String url, final boolean isSaveLocation) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        PhotoManager.getInstance().addBitmapToMemCache(url, response, isSaveLocation);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.no_photo_120);
                    }
                });
        mQueue.add(imageRequest);
    }
}
