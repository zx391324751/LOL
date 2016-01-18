package com.best.android.loler.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.LruCache;

import com.best.android.loler.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by BL06249 on 2015/11/30.
 */
public class PhotoManager {

    private static PhotoManager mInstance = new PhotoManager();
    private static LruCache<String, Bitmap> mMemoryCache;

    private PhotoManager(){
        //获取应用程序的最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());

        //用最大内存的1/4来存储图片
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            //获取每张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }

        };
    }

    public static PhotoManager getInstance(){
        return mInstance;
    }

    //从cashe拿数据，没有则从本地拿数据
    public Bitmap getBitmapFromMemCache(String key) {
        Bitmap bitmap = mMemoryCache.get(key);
        if(bitmap == null){
            bitmap = getBitmapFromLocation(key);
        }
        return bitmap;
    }

    //从本地读取数据
    public Bitmap getBitmapFromLocation(String url){
        //url格式不能作为文件名保存.(万恶的斜杠和点)
        String fileName = url.replace('/', '_');
        fileName = fileName.replace('.', '_');
        String path = FileUtil.HERO_PHOTO_DIR_PATH + "/" + fileName + ".jpg";
        File file = new File(path);
        if(file.exists()){
            return BitmapFactory.decodeFile(path);
        }
        return null;
    }

    //添加数据到cache(一级缓存)，isSaveLocation字段表示图片是否保存到本地
    public void addBitmapToMemCache(String key, Bitmap bitmap, boolean isSaveLocation){
        mMemoryCache.put(key, bitmap);
        if(isSaveLocation)
            addBitmapToLocation(key, bitmap);
    }

    //保存数据到本地 (二级缓存)
    public void addBitmapToLocation(String url, Bitmap bitmap){
        //url格式不能作为文件名保存.(万恶的斜杠和点)
        String fileName = url.replace('/', '_');
        fileName = fileName.replace('.', '_');
        File file = new File(FileUtil.HERO_PHOTO_DIR_PATH + "/" + fileName + ".jpg");
        if(file.exists()){
            return;
        }
        try {
            file.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException e 异常");
        }

    }
}
