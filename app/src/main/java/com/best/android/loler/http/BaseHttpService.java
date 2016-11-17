package com.best.android.loler.http;

/**
 * Created by BL06249 on 2015/11/23.
 */
public abstract class BaseHttpService {

    public interface ResponseListener {
        public void onProgress(int current, int total);

        public void onSuccess(String result);

        public void onFail(String errorMsg);
    }
}
