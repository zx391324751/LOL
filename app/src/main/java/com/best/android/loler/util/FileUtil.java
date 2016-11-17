package com.best.android.loler.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by BL06249 on 2015/11/30.
 */
public class FileUtil {

    public static final String ROOT_DIR = "mnt/sdcard/LOLer";
    public static final String HERO_PHOTO_DIR_PATH = ROOT_DIR + "/heroPhoto";
    public static final String VIDEO_DIR_PATH = ROOT_DIR + "/video";
    public static final String MUSIC_DIR_PATH = ROOT_DIR + "/music";

    public static void initAllFileDir(){
        File destDir = new File(HERO_PHOTO_DIR_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        destDir = new File(VIDEO_DIR_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        destDir = new File(MUSIC_DIR_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    public static String getHeroPhotoPath(String name){
        return  HERO_PHOTO_DIR_PATH + "/" + name;
    }

}
