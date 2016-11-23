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
import com.best.android.loler.config.Constants;
import com.best.android.loler.http.BaseHttpService;
import com.best.android.loler.http.GetRoomGroupService;
import com.best.android.loler.model.GroupInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BL06249 on 2016/1/11.
 */
public class DouyuActivity extends LoLBaseActivity {

    @BindView(R.id.activity_douyu_rv)
    RecyclerView recyclerView;
    List<GroupInfo>listRooms;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_douyu);
        setTitle("斗鱼TV");
        ButterKnife.bind(this);

        GetRoomGroupService getRoomGroupService = new GetRoomGroupService(this);
        getRoomGroupService.send(listener);
        initViews();
    }

    private void initViews() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
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
            Toast.makeText(DouyuActivity.this, Constants.QUERY_ERROR, Toast.LENGTH_SHORT).show();
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
