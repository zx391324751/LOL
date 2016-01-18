package com.best.android.loler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.best.android.loler.R;
import com.best.android.loler.adapter.AllHeroAdapter;
import com.best.android.loler.adapter.FreeHeroAdapter;
import com.best.android.loler.config.Constants;
import com.best.android.loler.httpService.BaseHttpService;
import com.best.android.loler.httpService.QueryHeroService;
import com.best.android.loler.model.HeroInfo;
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
public class HeroFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;

    private RecyclerView rvAllHero;

    public static HeroFragment newInstance(int position) {
        HeroFragment f = new HeroFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hero, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        queryAllHero();
    }

    private void initView(View view) {
        rvAllHero = (RecyclerView) view.findViewById(R.id.fragment_hero_rv_all_hero);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        rvAllHero.setLayoutManager(gridLayoutManager);
    }

    private void queryAllHero() {
        QueryHeroService queryHeroService = new QueryHeroService(getActivity());
        queryHeroService.send(responseListener, Constants.ALL_HERO);
    }

    BaseHttpService.ResponseListener responseListener = new BaseHttpService.ResponseListener() {

        @Override
        public void onProgress(int current, int total) {

        }

        @Override
        public void onSuccess(String result) {
            initHeroJson(result);
        }

        @Override
        public void onFail(String errorMsg) {
            ToastUtil.showShortMsg(getActivity(), Constants.QUERY_ERROR);
        }
    };

    private void initHeroJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(!jsonObject.has("all")){
                ToastUtil.showShortMsg(getActivity(), Constants.NO_FREE_HERO);
                return;
            }
            String allHeroJson = jsonObject.getString("all");
            ObjectMapper objectMapper = new ObjectMapper();
            HeroInfo heroInfos[] = objectMapper.readValue(allHeroJson, HeroInfo[].class);
            AllHeroAdapter allHeroAdapter = new AllHeroAdapter(getActivity(), heroInfos);
            rvAllHero.setAdapter(allHeroAdapter);
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
