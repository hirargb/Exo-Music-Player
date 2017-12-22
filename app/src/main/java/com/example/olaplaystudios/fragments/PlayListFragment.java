package com.example.olaplaystudios.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olaplaystudios.R;

/**
 * Created by Amit kumar karn on 20-12-2017.
 */

public class PlayListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist,container,false);
        return view;
    }

    public static PlayListFragment newInstance(){
        return new PlayListFragment();
    }
}
