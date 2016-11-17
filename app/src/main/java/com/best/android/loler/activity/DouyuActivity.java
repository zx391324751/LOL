package com.best.android.loler.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.best.android.loler.R;
import com.best.android.loler.adapter.GroupListAdapter;
import com.best.android.loler.http.BaseHttpService;
import com.best.android.loler.http.GetRoomGroupService;
import com.best.android.loler.model.GroupInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BL06249 on 2016/1/11.
 */
public class DouyuActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<GroupInfo>listRooms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyu);


        GetRoomGroupService getRoomGroupService = new GetRoomGroupService(this);
        getRoomGroupService.send(listener);

        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.activity_douyu_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        findViewById(R.id.activity_douyu_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DouyuActivity.this.finish();
            }
        });
    }

    BaseHttpService.ResponseListener listener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            parseRoomJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            Toast.makeText(DouyuActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
        }
    };

    private void parseRoomJson(String result) {
        listRooms = new ArrayList<GroupInfo>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSONObject jsonObject = new JSONObject(result);
            String data = jsonObject.getString("data");
            GroupInfo[] roomInfos = objectMapper.readValue(data, GroupInfo[].class);
            for (int i=0; roomInfos!=null && i<roomInfos.length; i++){
                listRooms.add(roomInfos[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GroupListAdapter roomListAdapter = new GroupListAdapter(this, listRooms);
        recyclerView.setAdapter(roomListAdapter);
    }

}
