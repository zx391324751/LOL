package com.best.android.loler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.best.android.loler.R;
import com.best.android.loler.adapter.FreeHeroAdapter;
import com.best.android.loler.adapter.HeroVideoAdapter;
import com.best.android.loler.config.Constants;
import com.best.android.loler.httpService.BaseHttpService;
import com.best.android.loler.httpService.QueryHeroService;
import com.best.android.loler.httpService.QueryVideoListService;
import com.best.android.loler.model.HeroInfo;
import com.best.android.loler.model.VideoInfo;
import com.best.android.loler.util.ToastUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BL06249 on 2015/11/23.
 */
public class NewsFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;

    private RecyclerView rvFreeHero;
    private RecyclerView rvVideo;

    private List<VideoInfo>videoList;
    private HeroVideoAdapter heroVideoAdapter;

    private int pageNum;

    public static NewsFragment newInstance(int position) {
        NewsFragment f = new NewsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        pageNum = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        query();
    }

    private void initView(View rootView) {
        //init RecycleView
        rvFreeHero = (RecyclerView)rootView.findViewById(R.id.fragment_news_rv_free_hero);
        LinearLayoutManager linearLayoutManagerH = new LinearLayoutManager(getActivity());
        linearLayoutManagerH.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvFreeHero.setLayoutManager(linearLayoutManagerH);

        rvVideo = (RecyclerView)rootView.findViewById(R.id.fragment_news_rv_video);
        LinearLayoutManager linearLayoutManagerV = new LinearLayoutManager(getActivity());
        linearLayoutManagerV.setOrientation(LinearLayoutManager.VERTICAL);
        rvVideo.setLayoutManager(linearLayoutManagerV);

        rvVideo.addOnScrollListener(onScrollListener);
    }

    private void query() {
        if(videoList == null){
            videoList = new ArrayList<VideoInfo>();
        }
        heroVideoAdapter = new HeroVideoAdapter(getActivity(), videoList);
        rvVideo.setAdapter(heroVideoAdapter);

        QueryHeroService queryHeroService = new QueryHeroService(getActivity());
        queryHeroService.send(freeHeroServiceResponseListener, Constants.FREE_HERO);

        QueryVideoListService queryVideoListService = new QueryVideoListService(getActivity());
        queryVideoListService.send(videoListServiceResponseListener, pageNum);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
            //获取最后一个完全显示的ItemPosition
            int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
            int totalItemCount = manager.getItemCount();
            // 判断是否滚动到底部，并且是向右滚动
            if (lastVisibleItem == (totalItemCount -1)) {
                pageNum ++;
                QueryVideoListService queryVideoListService = new QueryVideoListService(getActivity());
                queryVideoListService.send(videoListServiceResponseListener, pageNum);
            }
        }
    };

    BaseHttpService.ResponseListener freeHeroServiceResponseListener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            initFreeHeroJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            ToastUtil.showShortMsg(getActivity(), Constants.QUERY_ERROR);
        }
    };

    BaseHttpService.ResponseListener videoListServiceResponseListener = new BaseHttpService.ResponseListener() {
        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            initVideoListJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            ToastUtil.showShortMsg(getActivity(), Constants.QUERY_ERROR);
        }
    };

    private void initVideoListJson(String result) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            VideoInfo videoInfo[] = objectMapper.readValue(result, VideoInfo[].class);
            int size = videoList.size();
            for(int i=0; i<videoInfo.length; i++){
                boolean isExist = false;
                for(int k=0; k<size; k++){
                    if(videoInfo[i].vid == videoList.get(k).vid){
                        isExist = true;
                        break;
                    }
                }
                if(!isExist)
                    videoList.add(videoInfo[i]);
            }
            heroVideoAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showShortMsg(getActivity(), Constants.JSON_ERROR);
        }
    }

    private void initFreeHeroJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(!jsonObject.has("free")){
                ToastUtil.showShortMsg(getActivity(), Constants.NO_FREE_HERO);
                return;
            }
            String freeHeroJson = jsonObject.getString("free");
            ObjectMapper objectMapper = new ObjectMapper();
            HeroInfo heroInfos[] = objectMapper.readValue(freeHeroJson, HeroInfo[].class);
            FreeHeroAdapter freeHeroAdapter = new FreeHeroAdapter(getActivity(), heroInfos);
            rvFreeHero.setAdapter(freeHeroAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showShortMsg(getActivity(), Constants.JSON_ERROR);
        } catch (JsonParseException e) {
            e.printStackTrace();
            ToastUtil.showShortMsg(getActivity(), Constants.JSON_ERROR);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            ToastUtil.showShortMsg(getActivity(), Constants.JSON_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showShortMsg(getActivity(), Constants.JSON_ERROR);
        }

    }

}
