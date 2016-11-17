package com.best.android.loler.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.best.android.loler.R;
import com.best.android.loler.adapter.RoomListAdapter;
import com.best.android.loler.http.BaseHttpService;
import com.best.android.loler.http.GetRoomListService;
import com.best.android.loler.model.GroupInfo;
import com.best.android.loler.model.RoomInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BL06249 on 2016/1/13.
 */
public class RoomListActivity extends AppCompatActivity {

    private GroupInfo groupInfo;
    private RecyclerView recyclerView;
    private List<RoomInfo> listRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        groupInfo = (GroupInfo) getIntent().getSerializableExtra("GroupInfo");
        GetRoomListService getRoomListService = new GetRoomListService(this);
        getRoomListService.send(listener, groupInfo.roomId, 0);

        initView();
    }

    private void initView() {
        findViewById(R.id.activity_room_list_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomListActivity.this.finish();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.activity_room_list_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    BaseHttpService.ResponseListener listener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            parseRoomListJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            Toast.makeText(RoomListActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
        }
    };

    private void parseRoomListJson(String json) {
        listRoom = new ArrayList<RoomInfo>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String data = jsonObject.getString("data");
            ObjectMapper objectMapper = new ObjectMapper();
            RoomInfo[] roomInfos = objectMapper.readValue(data, RoomInfo[].class);
            for(int i=0; roomInfos!=null && i<roomInfos.length; i++){
                listRoom.add(roomInfos[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RoomListAdapter adapter = new RoomListAdapter(this, listRoom);
        recyclerView.setAdapter(adapter);
    }
}
