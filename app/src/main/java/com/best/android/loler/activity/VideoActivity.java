package com.best.android.loler.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.best.android.loler.R;
import com.best.android.loler.config.Constants;
import com.best.android.loler.config.NetConfig;
import com.best.android.loler.http.LOLBoxApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by BL06249 on 2015/12/1.
 */
public class VideoActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private Button btnPlay;
    private SeekBar seekBar;
    private VideoView videoView;
    private RelativeLayout layoutTop;
    private LinearLayout layoutBottom;
    private ProgressBar progressBar;
    private Button btnBD;
    private Button btnHD;
    private ImageView ivBatteryState;
    //电量的7种状态
    private int[] BATTERY_STATES = {R.drawable.battery_charging, R.drawable.battery_full, R.drawable.battery_100
        ,R.drawable.battery_80, R.drawable.battery_50, R.drawable.battery_20, R.drawable.battery_10};

    private String BDVideoUrl[]; //标清播放地址
    private String HDVideoUrl[]; //高清播放地址

    //播放类型:HD,BD
    static final int HD_VIDEO = 1001;
    static final int BD_VIDEO = 1000;
    private int videoType;
    //当前视频是否正在播放，本来想用videoview的isPlaying()方法，但是发现该方法一直返回false。
    private boolean isPlaying;

    private AudioManager audioManager;
    // 最大声音
    private int maxVolume;
    // 当前声音
    private int currentVolume = -1;
    // 当前亮度
    private float brightness = -1f;

    volatile int touchCount = 0;
    private Handler mHandler = new Handler();
    private GestureDetector gestureDetector;
    //手势类型 : 垂直移动,水平移动,没有移动
    private int gestureType;
    static final int MOVE_NONE = 1004;
    static final int MOVE_VERTICAL = 1002;
    static final int MOVE_HORIZONTAL = 1003;
    //移动视频的时刻， 持续移动，
    // 只有在移动结束的时候才定位到移动的时刻上面去播放视频，
    // 那么需要记录下时间，在手势抬起的时候，在根据该时间变量定位视频播放位置
    private int moveTime = 0;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_video_iv_back:
                    VideoActivity.this.finish();
                    break;
                case R.id.activity_video_btn_BD:
                    pause();
                    videoType = BD_VIDEO;
                    btnBD.setBackgroundColor(VideoActivity.this.getResources().getColor(R.color.grey_a5));
                    btnHD.setBackgroundResource(0);
                    play();
                    break;
                case R.id.activity_video_btn_HD:
                    pause();
                    videoType = HD_VIDEO;
                    btnHD.setBackgroundColor(VideoActivity.this.getResources().getColor(R.color.grey_a5));
                    btnBD.setBackgroundResource(0);
                    play();
                    break;
                case R.id.activity_video_ibtn_play:
                    Boolean isPlay = (Boolean)btnPlay.getTag();
                    if (isPlay == null)
                        return;
                    if(isPlay)
                        play();
                    else
                        pause();
                    break;
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (videoView != null && videoView.isPlaying()) {
                videoView.seekTo(seekBar.getProgress() * 1000);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        gestureDetector = new GestureDetector(this, gestureListener);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        initView();
        queryUrl();
    }

    private void initView() {
        tvTitle = (TextView)findViewById(R.id.activity_video_tv_title);
        btnPlay = (Button) findViewById(R.id.activity_video_ibtn_play);
        btnBD = (Button) findViewById(R.id.activity_video_btn_BD);
        btnHD = (Button) findViewById(R.id.activity_video_btn_HD);
        ivBatteryState = (ImageView)findViewById(R.id.activity_video_iv_battery);

        seekBar = (SeekBar)findViewById(R.id.activity_video_seekbar);
        layoutTop = (RelativeLayout)findViewById(R.id.activity_video_layout_top);
        layoutBottom = (LinearLayout)findViewById(R.id.activity_video_layout_bottom);
        videoView = (VideoView)findViewById(R.id.activity_video_view);
        videoFullScreen();
        progressBar = (ProgressBar)findViewById(R.id.activity_video_progress_bar);
        tvTotalTime = (TextView)findViewById(R.id.activity_video_tv_total_time);
        tvCurrentTime = (TextView)findViewById(R.id.activity_video_tv_current_time);

        findViewById(R.id.activity_video_iv_back).setOnClickListener(onClickListener);
        btnHD.setOnClickListener(onClickListener);
        btnBD.setOnClickListener(onClickListener);
        btnPlay.setOnClickListener(onClickListener);

        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBar.setEnabled(true);
    }

    //查询视频的播放地址:分为高清和标清
    private void queryUrl() {
        String vid = getIntent().getStringExtra("vid");
        String vName = getIntent().getStringExtra("vname");
        if(vName != null)
            tvTitle.setText(vName);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConfig.LOLBOX_BASE_URL_2)
                .build();
        LOLBoxApi.LOLVideoService service = retrofit.create(LOLBoxApi.LOLVideoService.class);
        Call<ResponseBody> call = service.getVideoInfo("f", vid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String result = null;
                try {
                    result = response.body().string();
                    initVideoAddressJson(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(VideoActivity.this, Constants.JSON_ERROR, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(VideoActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //解析视频地址
    private void initVideoAddressJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String isSucceed = jsonObject.getString("message");
            if("success".equals(isSucceed)){
                if(jsonObject.has("result")) {
                    jsonObject = jsonObject.getJSONObject("result");
                    if(jsonObject.has("items")){
                        jsonObject = jsonObject.getJSONObject("items");
                        JSONObject jsonObjectSD;
                        JSONObject jsonObjectHD;
                        //标清视频url
                        if(jsonObject.has("350")){
                            jsonObjectSD = jsonObject.getJSONObject("350");
                            if(jsonObjectSD.has("transcode")){
                                jsonObjectSD = jsonObjectSD.getJSONObject("transcode");
                                if(jsonObjectSD.has("urls")){
                                    JSONArray urls = jsonObjectSD.getJSONArray("urls");
                                    final int len = urls.length();
                                    BDVideoUrl = new String[len];
                                    for(int i=0; i<urls.length(); i++){
                                        BDVideoUrl[i] = urls.getString(i);
                                    }
                                }
                            }
                        }
                        //高清视频url
                        if(jsonObject.has("1000")){
                            jsonObjectHD = jsonObject.getJSONObject("1000");
                            if(jsonObjectHD.has("transcode")){
                                jsonObjectHD = jsonObjectHD.getJSONObject("transcode");
                                if(jsonObjectHD.has("urls")){
                                    JSONArray urls = jsonObjectHD.getJSONArray("urls");
                                    final int len = urls.length();
                                    HDVideoUrl = new String[len];
                                    for(int i=0; i<urls.length(); i++){
                                        HDVideoUrl[i] = urls.getString(i);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if((BDVideoUrl != null && BDVideoUrl.length>0)){
            videoType = BD_VIDEO;
            play();
            return;
        } else{
            if((HDVideoUrl != null && HDVideoUrl.length >0)){
                videoType = HD_VIDEO;
                play();
                return;
            }
        }
        Toast.makeText(this, "播放地址无效", Toast.LENGTH_SHORT).show();
    }

    //横向播放视频设置全屏
    private void videoFullScreen(){
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);
    }

    //播放
    private void play(){
        showMenuLayout();
        progressBar.setVisibility(View.VISIBLE);
        Uri uri = null;
        //获取视频播放地址String，并转为URI格式
        if(videoType == BD_VIDEO && BDVideoUrl != null && BDVideoUrl.length > 0)
            uri = Uri.parse(BDVideoUrl[0]);
        if(videoType == HD_VIDEO && HDVideoUrl != null && HDVideoUrl.length > 0)
            uri = Uri.parse(HDVideoUrl[0]);
        //uri为null，则无法播放，提示信息并退出
        if(uri == null){
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "该视频暂时无法播放", Toast.LENGTH_SHORT).show();
            return;
        }
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.seekTo(seekBar.getProgress() * 1000);

        //视频播放完成的监听
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        //视频播放出错监听
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(VideoActivity.this, "视频播放出错", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //视频缓冲监听
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.INVISIBLE);
                int totalTime = videoView.getDuration() / 1000;
                seekBar.setMax(totalTime);
                String allDate = getDateFormat(totalTime);
                tvTotalTime.setText(allDate);
            }
        });
        isPlaying = true;
        createUpdateThread();
        btnPlay.setText("暂停");
        btnPlay.setTag(false);
    }

    //创建更新时间进度条线程，实际是在主线程每隔0.5秒更新seekbar，因为是每隔0.5秒，并不会造成UI阻塞
    private void createUpdateThread() {
        VideoActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (videoView != null) {
                    int currentPosition = videoView.getCurrentPosition() / 1000;
                    if(seekBar != null) {
                        seekBar.setProgress(currentPosition);
                        String currentDate = getDateFormat(currentPosition);
                        tvCurrentTime.setText(currentDate);
                    }
                }
                mHandler.postDelayed(this, 500);
            }
        });
    }

    //时间格式 time:秒
    private String getDateFormat(int time){
        String timeStr = "";
        int hour = time / 60 / 60;
        if(hour >= 0 && hour <= 9){
            timeStr = timeStr + "0" + hour + ":";
        } else {
            timeStr = time + hour + ":";
        }
        int minute = (time - hour * 60 * 60) / 60;
        if(minute >= 0 && minute <= 9){
            timeStr = timeStr + "0" + minute + ":";
        } else {
            timeStr = timeStr + minute + ":";
        }
        int second = time % 60;
        if(second >= 0 && second <= 9){
            timeStr = timeStr + "0" + second;
        } else {
            timeStr = timeStr + second;
        }
        return  timeStr;
    }

    //暂停
    private void pause(){
        videoView.pause();
        btnPlay.setText("播放");
        btnPlay.setTag(true);
        isPlaying = false;
    }

    //停止
    private void stop(){
        videoView.stopPlayback();
        videoView.seekTo(0);
        seekBar.setProgress(0);
        btnPlay.setText("播放");
        btnPlay.setTag(true);
        isPlaying = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }
        return super.onTouchEvent(event);
    }

    // 手势结束
    private void endGesture() {
        currentVolume = -1;
        brightness = -1.0f;
        if(moveTime != 0){
            int currentTime = videoView.getCurrentPosition() /1000;
            videoView.seekTo( (currentTime + moveTime) * 1000);
            moveTime = 0;
        }
        gestureType = MOVE_NONE;
        findViewById(R.id.layout_video_light).setVisibility(View.INVISIBLE);
        findViewById(R.id.layout_video_voice).setVisibility(View.INVISIBLE);
        findViewById(R.id.layout_video_time).setVisibility(View.INVISIBLE);
    }

    //滑动改变声音大小
    private void onVolumeSlide(float percent) {
        findViewById(R.id.layout_video_time).setVisibility(View.INVISIBLE);
        findViewById(R.id.layout_video_light).setVisibility(View.INVISIBLE);
        findViewById(R.id.layout_video_voice).setVisibility(View.VISIBLE);
        TextView tvVoice = (TextView)findViewById(R.id.video_voice_level_tv_percent);

        if (currentVolume < 0) {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume < 0)
                currentVolume = 0;
        }

        int index = (int) (percent * maxVolume) + currentVolume;
        if (index > maxVolume)
            index = maxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        tvVoice.setText("音量 : " + index);
    }

    //滑动改变亮度
    private void onBrightnessSlide(float percent) {
        findViewById(R.id.layout_video_time).setVisibility(View.INVISIBLE);
        findViewById(R.id.layout_video_voice).setVisibility(View.INVISIBLE);
        findViewById(R.id.layout_video_light).setVisibility(View.VISIBLE);
        TextView tvVoice = (TextView)findViewById(R.id.video_light_level_tv_percent);

        if (brightness < 0) {
            brightness = getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f)
                brightness = 0.50f;
            if (brightness < 0.01f)
                brightness = 0.01f;
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        int lightPercent = (int) (lpa.screenBrightness * 100);
        tvVoice.setText("亮度 : " +  lightPercent + "%");
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);
    }

    //水平滑动记录时间刻度
    private void onTimeSlide(float precent){
        findViewById(R.id.layout_video_time).setVisibility(View.VISIBLE);
        findViewById(R.id.layout_video_voice).setVisibility(View.INVISIBLE);
        findViewById(R.id.layout_video_light).setVisibility(View.INVISIBLE);
        TextView tvTime = (TextView)findViewById(R.id.video_time_tv);

        int maxTime = 120; //两分钟
        moveTime = (int) (maxTime * precent);
        String playTimeStr = getDateFormat(Math.abs(moveTime));
        if(moveTime < 0){
            playTimeStr = " - " + playTimeStr;
        } else {
            playTimeStr = " + " + playTimeStr;
        }
        tvTime.setText(playTimeStr);
    }

    //定义屏幕的手势滑动
    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            layoutTop.setVisibility(View.VISIBLE);
            layoutBottom.setVisibility(View.VISIBLE);
            showMenuLayout();
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //按下的坐标点
            float downX = e1.getX();
            float downY = e1.getY();
            //移动点的Y轴位置
            int moveY = (int) e2.getRawY();
            int moveX = (int) e2.getRawX();
            //获取屏幕大小
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            int xLen = (int)Math.abs(moveX - downX);
            int yLen = (int)Math.abs(moveY - downY);
            //移动动作是连续的，如果移动起先是左右方向的，那么规定接下来的移动都是左右的，反之垂直
            if(gestureType == MOVE_NONE || gestureType == 0) {
                if (xLen < yLen) {
                    gestureType = MOVE_VERTICAL;
                } else {
                    gestureType = MOVE_HORIZONTAL;
                }
            }
            //如果是垂直的，判断垂直移动的位置
            if(gestureType == MOVE_VERTICAL) {
                if (downX > windowWidth / 2.0)      // 右边垂直滑动,改变音量
                    onVolumeSlide((downY - moveY) / windowHeight);
                else if (downX < windowWidth / 2.0)     // 左边垂直滑动，改变亮度
                    onBrightnessSlide((downY - moveY) / windowHeight);

            } else if(gestureType == MOVE_HORIZONTAL){
                onTimeSlide((moveX - downX) / windowWidth);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };

    //弹出上面和下面的菜单，并在5秒后隐藏
    private void showMenuLayout(){
        layoutTop.setVisibility(View.VISIBLE);
        layoutBottom.setVisibility(View.VISIBLE);
        touchCount ++;
        if(touchCount > 10000)
            touchCount = 0;
        mHandler.postDelayed(new MyRunnable(touchCount), 5 * 1000);
    }

    class MyRunnable implements Runnable{

        private int position;

        public MyRunnable(int position){
            this.position = position;
        }

        @Override
        public void run() {
            if(position == touchCount) {
                layoutTop.setVisibility(View.GONE);
                layoutBottom.setVisibility(View.GONE);
            }
        }
    }

    //电量监听
//    public class BatteryChangedReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context,Intent intent){
//            // 是否在充电
//            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//            int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);//当前电量
//            int total = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);//总电量
//            int percent = currentLevel*100/total;
//            setBatteryState(status, percent);
//        }
//    }
//
//    //设置电量状态图片
//    private void setBatteryState(int status, int percent){
//        if(ivBatteryState != null) {
//            if (status == BatteryManager.BATTERY_STATUS_CHARGING && percent >= 99) {
//                ivBatteryState.setImageResource(BATTERY_STATES[0]);
//            } else if (status == BatteryManager.BATTERY_STATUS_CHARGING && percent < 99) {
//                ivBatteryState.setImageResource(BATTERY_STATES[1]);
//            } else if (percent >= 99) {
//                ivBatteryState.setImageResource(BATTERY_STATES[2]);
//            } else if (percent >= 80) {
//                ivBatteryState.setImageResource(BATTERY_STATES[3]);
//            } else if (percent >= 50) {
//                ivBatteryState.setImageResource(BATTERY_STATES[4]);
//            } else if (percent >= 20) {
//                ivBatteryState.setImageResource(BATTERY_STATES[5]);
//            } else if (percent >= 0) {
//                ivBatteryState.setImageResource(BATTERY_STATES[6]);
//            }
//        }
//    }

}
