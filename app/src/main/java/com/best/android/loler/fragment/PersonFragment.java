package com.best.android.loler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.best.android.loler.R;

/**
 * Created by BL06249 on 2015/11/23.
 */
public class PersonFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static PersonFragment newInstance(int position) {
        PersonFragment f = new PersonFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_person, container, false);
        return rootView;
    }

}
