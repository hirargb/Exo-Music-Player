package com.example.olaplaystudios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.olaplaystudios.models.Songs;
import com.example.olaplaystudios.utils.AppConfig;
import com.example.olaplaystudios.utils.ImageLoader;
import com.example.olaplaystudios.widgets.MusicVisualizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AMIT KUMAR KARN on 16-12-2017.
 */

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> implements Filterable {
    private List<Songs> songsArrayList;
    private List<Songs> songsArrayListFiltered;
    private Context context;
    private PlayerListener playerListener;
    public SongListAdapter(ArrayList<Songs> songsArrayList, Context context, PlayerListener playerListener) {
        this.songsArrayList = songsArrayList;
        this.context = context;
        this.playerListener = playerListener;
        this.songsArrayListFiltered = songsArrayList;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_song_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, final int position) {
        final Songs songs = songsArrayList.get(position);
        Glide.with(context).load(songs.getSongImageUrl()).into(holder.thumbnail);
        holder.songName.setText(songs.getSongName());
        holder.artistName.setText(songs.getArtistNames());
        if (AppConfig.PlayingSongId == songs.getSongID()) {
            holder.musicVisual.setVisibility(View.VISIBLE);
        }else{
            holder.musicVisual.setVisibility(View.GONE);
        }

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songs.isPlaying() && AppConfig.PlayingSongId == songs.getSongID()) {
                    playerListener.onPauseClicked(songs.getSongID(), false);
                    holder.play.setImageResource(R.drawable.media_play_symbol);
                    holder.musicVisual.setVisibility(View.GONE);
                    songs.setPlaying(false);
                } else{
                    playerListener.onPlayClicked(songs);
                    songs.setPlaying(true);
                    AppConfig.PlayingSongId = songs.getSongID();
                    AppConfig.CURRENT_SONG = songs;
                    holder.play.setImageResource(R.drawable.music_player_pause);
                    holder.musicVisual.setVisibility(View.VISIBLE);
                }
            }
        });

        /*holder.play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (AppConfig.PlayingSongId == songs.getSongID()) {
                        playerListener.onPauseClicked(songs.getSongID(), true);
                    } else {
                        playerListener.onPlayClicked(songs);
                        AppConfig.PlayingSongId = songs.getSongID();
                    }
                    holder.musicVisual.setVisibility(View.VISIBLE);
                } else {
                    playerListener.onPauseClicked(songs.getSongID(), false);
                    holder.musicVisual.setVisibility(View.GONE);
                }
            }
        });*/
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerListener.onDownloadClicked(songs.getStreamUrl());
            }
        });
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOverflowMenu(view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                System.out.println("Filter: "+charSequence);
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    songsArrayListFiltered = songsArrayList;
                } else {
                    List<Songs> songsAListFiltered = new ArrayList<>();
                    for (Songs songs : songsArrayList) {
                        if (songs.getSongName().toLowerCase().contains(charString.toLowerCase()) || songs.getSongName().toLowerCase().contains(charString)) {
                            songsAListFiltered.add(songs);
                        }
                    }

                    songsArrayListFiltered = songsAListFiltered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = songsArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                songsArrayListFiltered = (ArrayList<Songs>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, download, overflow;
        TextView songName, artistName;
        MusicVisualizer musicVisual;
        private ImageButton play;

        public SongViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            play = itemView.findViewById(R.id.play);
            overflow = itemView.findViewById(R.id.overflow);
            download = itemView.findViewById(R.id.download);
            songName = itemView.findViewById(R.id.songName);
            artistName = itemView.findViewById(R.id.artistName);
            musicVisual = itemView.findViewById(R.id.musicVisual);
        }
    }

    private void showOverflowMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_song, popup.getMenu());
        //popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    public interface PlayerListener {
        void onPlayClicked(Songs song);

        void onPauseClicked(int songId, boolean isPlaying);

        void onDownloadClicked(String url);
    }
}
