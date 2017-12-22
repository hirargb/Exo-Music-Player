package com.example.olaplaystudios.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.olaplaystudios.R;
import com.example.olaplaystudios.SongListAdapter;
import com.example.olaplaystudios.models.Songs;

import java.util.ArrayList;

/**
 * Created by Amit kumar karn on 20-12-2017.
 */

public class BrowseListFragment extends Fragment implements SongListAdapter.PlayerListener {
    private RecyclerView recyclerViewSongsList;
    private SongListAdapter songListAdapter;
    private ArrayList<Songs> songsArrayList;
    private SongListener songListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        recyclerViewSongsList = view.findViewById(R.id.songsList);
        songsArrayList = new ArrayList<>();
        songListAdapter = new SongListAdapter(songsArrayList, getContext(), this);
        recyclerViewSongsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewSongsList.setAdapter(songListAdapter);
        recyclerViewSongsList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    public static BrowseListFragment newInstance() {
        BrowseListFragment browseListFragment = new BrowseListFragment();
        return browseListFragment;
    }

    public void addItemsToSongsList(Songs song) {
        songsArrayList.add(song);
        songListAdapter.notifyItemInserted(songsArrayList.size() - 1);
    }

    @Override
    public void onPlayClicked(Songs song) {
        if (song != null)
            songListener.onPlay(song);
        else System.out.println("Song null");
    }

    @Override
    public void onPauseClicked(int songId, boolean playing) {
        songListener.onPause(songId, playing);
    }

    @Override
    public void onDownloadClicked(String url) {
        songListener.onDownload(url);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof SongListener) {
            songListener = (SongListener) getActivity();
        } else {
            throw new ClassCastException();
        }
    }

    public void filterSongs(String query) {
        System.out.println(query);
        songListAdapter.getFilter().filter(query);
    }


    public interface SongListener {

        void onPlay(Songs songs);

        void onPause(int songId, boolean playing);

        void onDownload(String url);

    }
}
