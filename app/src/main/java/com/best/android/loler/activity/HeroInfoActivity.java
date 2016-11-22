package com.best.android.loler.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.best.android.loler.R;
import com.best.android.loler.adapter.ListviewCzAdapter;
import com.best.android.loler.config.Constants;
import com.best.android.loler.config.NetConfig;
import com.best.android.loler.http.BaseHttpService;
import com.best.android.loler.http.LOLBoxApi;
import com.best.android.loler.manager.ImageLoadManager;
import com.best.android.loler.manager.PhotoManager;
import com.best.android.loler.model.HeroInfo;
import com.best.android.loler.model.LOLczInfo;
import com.best.android.loler.model.SkillInfo;
import com.best.android.loler.util.ToastUtil;
import com.best.android.loler.view.SkillIndicatorLayout;
import com.best.android.loler.view.SquareImageView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by BL06249 on 2015/12/2.
 */
public class HeroInfoActivity extends AppCompatActivity {

    private HeroInfo heroInfo;
    private final String skills[]={"B","Q","W","E","R"};
    private SkillInfo skillInfo[];
    private ImageView ivSkill[];

    private SkillIndicatorLayout skillIndicatorLayout;

    private TextView tvSkillName;
    private TextView tvSkillCooldown;
    private TextView tvSkillCost;
    private TextView tvSkillDescription;

    private List<LOLczInfo> listCzInfo;
    private ImageView ivPreCz[];
    private ImageView ivMidCz[];
    private ImageView ivEndCz[];
    private ImageView ivNfCz[];

    private PopupWindow popupWindow;
    private ListView lvCz;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.activity_heroinfo_layout_cz){
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                setPopupWindowBackground(0.5f);
            } else if(v.getId() == R.id.popup_window_cz_select_layout_cancel){
                popupWindow.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroinfo);

        initView();
        initHeroInfo();
        initSkillPhoto();
        initPopupWindow();

        queryHeroDetail(heroInfo.enName);
        queryHeroCz(heroInfo.enName);
    }

    private void queryHeroCz(String enName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConfig.LOLBOX_BASE_URL_3)
                .build();
        LOLBoxApi.LOLHeroService service = retrofit.create(LOLBoxApi.LOLHeroService.class);
        Call<ResponseBody> call = service.getHeroCZInfo(enName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    parseCzInfoJson(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.showShortMsg(HeroInfoActivity.this, Constants.JSON_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtil.showShortMsg(HeroInfoActivity.this, Constants.QUERY_ERROR);
            }
        });
    }

    private void queryHeroDetail(String enName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConfig.LOLBOX_BASE_URL_1)
                .build();
        LOLBoxApi.LOLHeroService service = retrofit.create(LOLBoxApi.LOLHeroService.class);
        Call<ResponseBody>call = service.getHeroDetailInfo(enName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    parseSkillJson(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.showShortMsg(HeroInfoActivity.this, Constants.JSON_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtil.showShortMsg(HeroInfoActivity.this, Constants.QUERY_ERROR);
            }
        });
    }

    private void initView() {
        skillIndicatorLayout = (SkillIndicatorLayout)findViewById(R.id.activity_heroinfo_skill_indicator);
        skillIndicatorLayout.addOnSelectedChangedListener(new SkillIndicatorLayout.OnSelectedChangedListener() {
            @Override
            public void changed(int position) {
                updataSkillText(position);
            }
        });

        tvSkillName = (TextView)findViewById(R.id.activity_heroinfo_tv_skill_name);
        tvSkillCost = (TextView)findViewById(R.id.activity_heroinfo_tv_skill_cast);
        tvSkillCooldown = (TextView)findViewById(R.id.activity_heroinfo_tv_skill_cooldown);
        tvSkillDescription = (TextView)findViewById(R.id.activity_heroinfo_tv_skill_description);

        findViewById(R.id.activity_heroinfo_layout_cz).setOnClickListener(onClickListener);
        initCzImageView();
    }

    private void initCzImageView() {
        ivPreCz = new SquareImageView[6];
        ivMidCz = new SquareImageView[6];
        ivEndCz = new SquareImageView[6];
        ivNfCz = new SquareImageView[6];
        ivPreCz[0] = (SquareImageView)findViewById(R.id.layout_equipment_pre_0);
        ivPreCz[1] = (SquareImageView)findViewById(R.id.layout_equipment_pre_1);
        ivPreCz[2] = (SquareImageView)findViewById(R.id.layout_equipment_pre_2);
        ivPreCz[3] = (SquareImageView)findViewById(R.id.layout_equipment_pre_3);
        ivPreCz[4] = (SquareImageView)findViewById(R.id.layout_equipment_pre_4);
        ivPreCz[5] = (SquareImageView)findViewById(R.id.layout_equipment_pre_5);

        ivMidCz[0] = (SquareImageView)findViewById(R.id.layout_equipment_mid_0);
        ivMidCz[1] = (SquareImageView)findViewById(R.id.layout_equipment_mid_1);
        ivMidCz[2] = (SquareImageView)findViewById(R.id.layout_equipment_mid_2);
        ivMidCz[3] = (SquareImageView)findViewById(R.id.layout_equipment_mid_3);
        ivMidCz[4] = (SquareImageView)findViewById(R.id.layout_equipment_mid_4);
        ivMidCz[5] = (SquareImageView)findViewById(R.id.layout_equipment_mid_5);

        ivEndCz[0] = (SquareImageView)findViewById(R.id.layout_equipment_end_0);
        ivEndCz[1] = (SquareImageView)findViewById(R.id.layout_equipment_end_1);
        ivEndCz[2] = (SquareImageView)findViewById(R.id.layout_equipment_end_2);
        ivEndCz[3] = (SquareImageView)findViewById(R.id.layout_equipment_end_3);
        ivEndCz[4] = (SquareImageView)findViewById(R.id.layout_equipment_end_4);
        ivEndCz[5] = (SquareImageView)findViewById(R.id.layout_equipment_end_5);

        ivNfCz[0] = (SquareImageView)findViewById(R.id.layout_equipment_nf_0);
        ivNfCz[1] = (SquareImageView)findViewById(R.id.layout_equipment_nf_1);
        ivNfCz[2] = (SquareImageView)findViewById(R.id.layout_equipment_nf_2);
        ivNfCz[3] = (SquareImageView)findViewById(R.id.layout_equipment_nf_3);
        ivNfCz[4] = (SquareImageView)findViewById(R.id.layout_equipment_nf_4);
        ivNfCz[5] = (SquareImageView)findViewById(R.id.layout_equipment_nf_5);
    }

    private void initHeroInfo() {
        heroInfo = (HeroInfo) getIntent().getSerializableExtra("hero");
        TextView tvName = (TextView)findViewById(R.id.activity_heroinfo_tv_name);
        tvName.setText(heroInfo.cnName);
        ImageView ivPhoto = (ImageView)findViewById(R.id.activity_heroinfo_iv_photo);
        String url = LOLBoxApi.getHeroPhotoUrl(heroInfo.enName);
        Bitmap bitmap = PhotoManager.getInstance().getBitmapFromMemCache(url);
        if(bitmap != null)
            ivPhoto.setImageBitmap(bitmap);
        TextView tvLocation = (TextView)findViewById(R.id.activity_heroinfo_tv_location);
        tvLocation.setText(heroInfo.location);

        String price[] = heroInfo.price.split(",");
        if (price.length >= 2) {
            TextView tvTickets = (TextView) findViewById(R.id.activity_heroinfo_tv_tickets);
            tvTickets.setText("点券:" + price[1]);
            TextView tvGold = (TextView)findViewById(R.id.activity_heroinfo_tv_gold);
            tvGold.setText("金币:" + price[0]);
        }
    }

    private void initSkillPhoto() {
        ivSkill = new ImageView[5];
        ivSkill[0] = (ImageView)findViewById(R.id.activity_heroinfo_iv_skill_B);
        ivSkill[1] = (ImageView)findViewById(R.id.activity_heroinfo_iv_skill_Q);
        ivSkill[2] = (ImageView)findViewById(R.id.activity_heroinfo_iv_skill_W);
        ivSkill[3] = (ImageView)findViewById(R.id.activity_heroinfo_iv_skill_E);
        ivSkill[4] = (ImageView)findViewById(R.id.activity_heroinfo_iv_skill_R);
        for(int i =0 ; i<skills.length; i++){
            String skill = heroInfo.enName + "_" + skills[i];
            Bitmap bitmap = PhotoManager.getInstance().getBitmapFromMemCache(skill);
            if(bitmap != null) {
                ivSkill[i].setImageBitmap(bitmap);
            } else {
                ImageLoadManager.loadImage(ivSkill[i], LOLBoxApi.getSkillPhotoUrl(skill), false);
            }
        }
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_window_cz_select, null);
        popupWindow = new PopupWindow(view);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.alpha_bg));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setPopupWindowBackground(1.0f);
            }
        });
        lvCz = (ListView) view.findViewById(R.id.popup_window_cz_select_lv);
        lvCz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updataCzText(position);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.popup_window_cz_select_layout_cancel).setOnClickListener(onClickListener);
    }

    //改变屏幕背景亮度
    private void setPopupWindowBackground(float value){
        final Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = value;
        window.setAttributes(params);
    }

    BaseHttpService.ResponseListener heroDetailListener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            parseSkillJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            ToastUtil.showShortMsg(HeroInfoActivity.this, "数据加载失败");
        }
    };

    private void parseSkillJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            ObjectMapper objectMapper = new ObjectMapper();
            skillInfo = new SkillInfo[5];
            for(int i=0; i<5; i++){
                skillInfo[i] = new SkillInfo();
                String skillKey = heroInfo.enName + "_" + skills[i];
                skillInfo[i] = objectMapper.readValue(jsonObject.getString(skillKey), SkillInfo.class);
            }
            updataSkillText(0);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updataSkillText(int position){
        if(skillInfo != null && skillInfo[position] != null && skillInfo.length>position){
            tvSkillName.setText(skillInfo[position].skillName);
            tvSkillCooldown.setText("冷却时间:" + skillInfo[position].coolDown);
            tvSkillCost.setText("法力消耗:" + skillInfo[position].cost);
            tvSkillDescription.setText(skillInfo[position].description + "\n" + skillInfo[position].effect);
        }
    }

    BaseHttpService.ResponseListener heroCzListener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            parseCzInfoJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            ToastUtil.showShortMsg(HeroInfoActivity.this, "英雄出装信息加载失败");
        }
    };

    private void parseCzInfoJson(String result) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LOLczInfo[] czInfos = objectMapper.readValue(result, LOLczInfo[].class);
            if(czInfos != null){
                listCzInfo = new ArrayList<LOLczInfo>();
                for(int i=0; i<czInfos.length; i++) {
                    listCzInfo.add(czInfos[i]);
                }
            }
            updataCzText(0);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updataCzText(int position) {
        if(listCzInfo != null && listCzInfo.get(position) != null){
            LOLczInfo czInfo = listCzInfo.get(position);
            ((TextView)findViewById(R.id.activity_heroinfo_tv_author)).setText(czInfo.author + "/" + czInfo.lolServer);
            ((TextView)findViewById(R.id.activity_heroinfo_tv_time)).setText(czInfo.time);
            ListviewCzAdapter czAdapter = new ListviewCzAdapter(this, listCzInfo);
            lvCz.setAdapter(czAdapter);

            String preZbUrl[] = czInfo.preCZ.split(",");
            ((TextView)findViewById(R.id.layout_equipment_tv_pre_explain)).setText("前期:" + czInfo.preExplain);
            for(int i=0; i<6; i++){
                if(i >= preZbUrl.length){
                    ivPreCz[i].setVisibility(View.INVISIBLE);
                } else {
                    ImageLoadManager.loadImage(ivPreCz[i], LOLBoxApi.getLolZbPhotoUrl(preZbUrl[i]), false);
                }
            }

            String midZbUrl[] = czInfo.midCZ.split(",");
            ((TextView)findViewById(R.id.layout_equipment_tv_mid_explain)).setText("中期:" + czInfo.midExplain);
            for(int i=0; i<6; i++){
                if(i >= midZbUrl.length){
                    ivMidCz[i].setVisibility(View.INVISIBLE);
                } else {
                    ImageLoadManager.loadImage(ivMidCz[i], LOLBoxApi.getLolZbPhotoUrl(midZbUrl[i]), false);
                }
            }

            String endZbUrl[] = czInfo.endCZ.split(",");
            ((TextView)findViewById(R.id.layout_equipment_tv_end_explain)).setText("后期:" + czInfo.endExplain);
            for(int i=0; i<6; i++){
                if(i >= endZbUrl.length){
                    ivEndCz[i].setVisibility(View.INVISIBLE);
                } else {
                    ImageLoadManager.loadImage(ivEndCz[i], LOLBoxApi.getLolZbPhotoUrl(endZbUrl[i]), false);
                }
            }

            String nfZbUrl[] = czInfo.nfCZ.split(",");
            ((TextView)findViewById(R.id.layout_equipment_tv_nf_explain)).setText("逆风出装:" + czInfo.nfExplain);
            for(int i=0; i<6; i++){
                if(i >= nfZbUrl.length){
                    ivNfCz[i].setVisibility(View.INVISIBLE);
                } else {
                    ImageLoadManager.loadImage(ivNfCz[i], LOLBoxApi.getLolZbPhotoUrl(nfZbUrl[i]), false);
                }
            }
        }
    }

}
