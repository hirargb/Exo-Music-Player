package com.example.olaplaystudios;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.example.olaplaystudios.fragments.BrowseListFragment;
import com.example.olaplaystudios.fragments.PlayListFragment;
import com.example.olaplaystudios.models.Songs;
import com.example.olaplaystudios.storage.DBHandler;
import com.example.olaplaystudios.utils.AppConfig;
import com.example.olaplaystudios.widgets.CircularSeekBar;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements BrowseListFragment.SongListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private SimpleExoPlayer exoPlayer;
    private CircularSeekBar seekPlayerProgress;
    private ProgressBar progressBar, songLoading;
    private Handler handler;
    private ImageButton nextSong, prevSong, likeButton;
    private RelativeLayout playPeekSheet;
    private TextView artist, title, songLength;
    private ImageView album_art, album_art_circle, blurAlbumArt;
    private boolean isPlaying = false;
    private ImageButton play_pause_2, play_pause;
    private ExtractorsFactory extractorsFactory;
    private DefaultDataSourceFactory dataSourceFactory;
    ArrayList<Songs> songsArrayList;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BrowseListFragment browseListFragment;
    private PlayListFragment playListFragment;
    private SearchView searchView;
    private ConcatenatingMediaSource concatenatingMediaSource;
    private MediaSource mediaSource;
    List<MediaSource> nowPlaying = new ArrayList<>();
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        browseListFragment = BrowseListFragment.newInstance();
        playListFragment = PlayListFragment.newInstance();
        viewPager = findViewById(R.id.pager);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        setUpViewPager();
        album_art = findViewById(R.id.album_art);
        play_pause = findViewById(R.id.play_pause);
        play_pause = findViewById(R.id.play_pause);
        play_pause_2 = findViewById(R.id.play_pause_2);
        progressBar = findViewById(R.id.song_progress_normal);
        songLoading = findViewById(R.id.songLoading);
        progressBar.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.progressColor), android.graphics.PorterDuff.Mode.SRC_IN);
        title = findViewById(R.id.title);
        artist = findViewById(R.id.artist);
        songLength = findViewById(R.id.songLength);
        nextSong = findViewById(R.id.nextSong);
        prevSong = findViewById(R.id.prevSong);
        likeButton = findViewById(R.id.likeButton);
        album_art_circle = findViewById(R.id.album_art_circle);
        blurAlbumArt = findViewById(R.id.blurAlbumArt);
        playPeekSheet = findViewById(R.id.playPeekSheet);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        seekPlayerProgress = findViewById(R.id.song_progress_circular);
        dbHandler = new DBHandler(getApplicationContext());
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                setMiniPlayerAlphaProgress(slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
        try {
            fetchSongsList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TrackSelector trackSelector = new DefaultTrackSelector();

        LoadControl loadControl = new DefaultLoadControl();

        //instanciating exoplayer
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        String userAgent = Util.getUserAgent(getApplicationContext(), getApplicationInfo().className);

        // Default parameters, except allowCrossProtocolRedirects is true
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent,
                null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true
        );

        dataSourceFactory = new DefaultDataSourceFactory(
                getApplicationContext(),
                null,
                httpDataSourceFactory
        );
        extractorsFactory = new DefaultExtractorsFactory();

        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.getCurrentWindowIndex();

                Log.d(TAG, String.valueOf(AppConfig.CURRENT_SONG.getSongID()));
            }
        });
        prevSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.getCurrentWindowIndex();
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeButton.setImageResource(R.drawable.heart);
            }
        });
    }

    private void setMiniPlayerAlphaProgress(float slideOffset) {
        float alpha = 1 - slideOffset;
        playPeekSheet.setAlpha(alpha);
        // necessary to make the views below clickable
        playPeekSheet.setVisibility(alpha == 0 ? View.GONE : View.VISIBLE);
    }

    private Player.EventListener eventListener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            Log.i(TAG, "onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.i(TAG, "onTracksChanged");
            //exoPlayer.stop();

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.i(TAG, "onLoadingChanged");
            //setPlayPause(isLoading);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.i(TAG, "onPlayerStateChanged: playWhenReady = " + String.valueOf(playWhenReady)
                    + " playbackState = " + playbackState);
            switch (playbackState) {
                case Player.STATE_ENDED:
                    Log.i(TAG, "Playback ended!");
                    //Stop playback and return to start position
                    setPlayPause(false);
                    seekPlayerProgress.setProgress((int) (exoPlayer.getCurrentPosition() / 1000));
                    break;
                case Player.STATE_READY:
                    songLoading.setVisibility(View.GONE);
                    Log.i(TAG, "ExoPlayer ready! pos: " + exoPlayer.getCurrentPosition()
                            + " max: " + stringForTime((int) exoPlayer.getDuration()));
                    setProgress();
                    break;
                case Player.STATE_BUFFERING:
                    Log.i(TAG, "Playback buffering!");
                    songLoading.setVisibility(View.VISIBLE);
                    break;
                case Player.STATE_IDLE:
                    Log.i(TAG, "ExoPlayer idle!");
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }
    };

    private void setUpViewPager() {
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    void setAlbumArt(Songs songs) {
        Glide.with(getApplicationContext()).load(songs.getSongImageUrl()).into(album_art);
        Glide.with(getApplicationContext()).load(songs.getSongImageUrl()).into(album_art_circle);
        MultiTransformation multiTransformation = new MultiTransformation(new BlurTransformation(30), new ColorFilterTransformation(R.color.colorAccent));
        Glide.with(getApplicationContext()).applyDefaultRequestOptions(RequestOptions.bitmapTransform(multiTransformation)).load(songs.getSongImageUrl()).into(blurAlbumArt);
        title.setText(songs.getSongName());
        artist.setText(songs.getArtistNames());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                browseListFragment.filterSongs(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                browseListFragment.filterSongs(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = browseListFragment;
                    break;
                case 1:
                    fragment = playListFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void fetchSongsList() throws IOException {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(AppConfig.SONGS_LIST_API)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                Log.d("Songs", myResponse);
                try {
                    songsArrayList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(myResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject songObj = jsonArray.getJSONObject(i);
                        final Songs songs = new Songs();
                        songs.setSongID(i);
                        songs.setSongName(songObj.getString(AppConfig.SONG_NAME));
                        songs.setSongImageUrl(songObj.getString(AppConfig.SONG_COVER_IMAGE));
                        songs.setArtistNames(songObj.getString(AppConfig.SONG_ARTIST_NAMES));
                        songs.setStreamUrl(songObj.getString(AppConfig.SONG_URL));
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                songsArrayList.add(songs);
                                mediaSource = new ExtractorMediaSource(Uri.parse(songs.getStreamUrl()), dataSourceFactory, extractorsFactory, null, null);
                                nowPlaying.add(mediaSource);
                                browseListFragment.addItemsToSongsList(songs);
                            }
                        });
                    }
                    concatenatingMediaSource = new ConcatenatingMediaSource(nowPlaying.toArray(new MediaSource[nowPlaying.size()]));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void prepareExoPlayerFromURL(Uri uri) {
        exoPlayer.setPlayWhenReady(false);
        MediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
        exoPlayer.addListener(eventListener);
        exoPlayer.prepare(audioSource);
        initMediaControls();
        setPlayPause(true);
    }

    private void playSelectedExoPlayerFromPlaylist(int position) {
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.addListener(eventListener);
        exoPlayer.prepare(nowPlaying.get(position));
        exoPlayer.setPlayWhenReady(true);
        initMediaControls();
        setPlayPause(true);
    }

    private void initMediaControls() {
        initPlayButton();
        initSeekBar();
    }

    private void initPlayButton() {
        play_pause.requestFocus();
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlayPause(!isPlaying);
            }
        });

        play_pause_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlayPause(!isPlaying);
            }
        });
    }

    private void setPlayPause(boolean play) {
        isPlaying = play;
        exoPlayer.setPlayWhenReady(play);
        if (!isPlaying) {
            songLoading.setVisibility(View.GONE);
            play_pause_2.setImageResource(R.drawable.play_button_round);
            play_pause.setImageResource(R.drawable.play_button_round);
        } else {
            play_pause_2.setImageResource(R.drawable.round_pause_button);
            play_pause.setImageResource(R.drawable.round_pause_button);
        }
    }

    private void setProgress() {
        progressBar.setProgress(0);
        progressBar.setMax((int) exoPlayer.getDuration() / 1000);
        seekPlayerProgress.setProgress(0);
        seekPlayerProgress.setMax((int) exoPlayer.getDuration() / 1000);
        songLength.setText(stringForTime((int) exoPlayer.getCurrentPosition()));

        if (handler == null) handler = new Handler();
        //Make sure you update Seekbar on UI thread
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && isPlaying) {
                    progressBar.setMax((int) exoPlayer.getDuration() / 1000);
                    seekPlayerProgress.setMax((int) exoPlayer.getDuration() / 1000);
                    int mCurrentPosition = (int) exoPlayer.getCurrentPosition() / 1000;
                    seekPlayerProgress.setProgress(mCurrentPosition);
                    progressBar.setProgress(mCurrentPosition);
                    songLength.setText(stringForTime((int) exoPlayer.getCurrentPosition()));

                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void initSeekBar() {
        seekPlayerProgress.requestFocus();

        seekPlayerProgress.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }

                exoPlayer.seekTo(progress * 1000);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        seekPlayerProgress.setMax(0);
        seekPlayerProgress.setMax((int) exoPlayer.getDuration() / 1000);

    }

    @Override
    public void onPlay(Songs songs) {
        setAlbumArt(songs);
        playSelectedExoPlayerFromPlaylist(songs.getSongID());
        setPlayPause(true);
    }

    @Override
    public void onPause(int songId, boolean playing) {
        setPlayPause(playing);
    }

    @Override
    public void onDownload(String url) {

    }
}
