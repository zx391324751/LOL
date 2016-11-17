package com.best.android.loler.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.best.android.loler.LOLApplication;
import com.best.android.loler.R;
import com.best.android.loler.http.BaseHttpService;
import com.best.android.loler.http.GetRoomDetailService;
import com.best.android.loler.model.DanmuServer;
import com.best.android.loler.model.RoomInfo;
import com.douyu.lib.xdanmuku.x.DanmuClient;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by BL06249 on 2016/1/13.
 */
public class ZhiboVideoActivity extends AppCompatActivity {

    private RoomInfo roomInfo;
    private VideoView videoView;
    private ProgressBar progressBar;
    private String hslUrl;
    private DanmuServer[] servers;
    private DanmuClient danmuClient;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_video_zhibo_iv_back:
                    ZhiboVideoActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_zhibo);

        this.danmuClient = LOLApplication.danmuClient;

        roomInfo = (RoomInfo) getIntent().getSerializableExtra("RoomInfo");
        GetRoomDetailService getRoomDetailService = new GetRoomDetailService(this);
        getRoomDetailService.send(listener, roomInfo.roomId);

        initView();
    }

    private void initView() {
        findViewById(R.id.activity_video_zhibo_iv_back).setOnClickListener(onClickListener);
        videoView = (VideoView)findViewById(R.id.activity_video_zhibo_vview);
        videoFullScreen();

        progressBar = (ProgressBar)findViewById(R.id.activity_video_zhibo_pb);
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
        progressBar.setVisibility(View.VISIBLE);
        Uri uri = null;
        if (hslUrl != null)
            uri = Uri.parse(hslUrl);
        //uri为null，则无法播放，提示信息并退出
        if(uri == null){
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "直播地址无效", Toast.LENGTH_SHORT).show();
            return;
        }
        videoView.setVideoURI(uri);
        videoView.start();

        //视频播放完成的监听
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                Toast.makeText(ZhiboVideoActivity.this, "该主播暂未直播", Toast.LENGTH_SHORT).show();
                play();
            }
        });
        //视频播放出错监听
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(ZhiboVideoActivity.this, "直播出错", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //视频缓冲监听
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    BaseHttpService.ResponseListener listener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            parseHslJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            Toast.makeText(ZhiboVideoActivity.this, "地址解析出错", Toast.LENGTH_SHORT).show();
        }
    };

    private void parseHslJson(String result) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSONObject jsonObject = new JSONObject(result);
            hslUrl = jsonObject.getJSONObject("data").getString("hls_url");
            String serverStr = jsonObject.getJSONObject("data").getString("servers");
            servers = objectMapper.readValue(serverStr, DanmuServer[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        play();
        connectDanmuServer();
    }

    private void connectDanmuServer() {
        if(servers != null && danmuClient!=null){
            System.out.println("ip = " + servers[0].ip + "   port = " + servers[0].port);
            danmuClient.connect(this, servers[0].ip, servers[0].port);
//            danmuClient.reConnect(servers[0].ip, servers[0].port);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.stopPlayback();
        videoView.seekTo(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
