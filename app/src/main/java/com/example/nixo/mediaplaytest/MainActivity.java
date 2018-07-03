package com.example.nixo.mediaplaytest;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,ItemOnClickListener{

    private  SeekBar seekBar;
    private final static String TAG = "Nixo";
    private int vol;
    private int index;
    private ArrayList<String> MusicName = new ArrayList<>();
    private RecyclerView recyclerView;
    public static ArrayList<String> pathList = new ArrayList<>();
    private TextView noMusic;
    private static Button prv,next,pause;

    /**
     * 播放 - 1
     * 暂停 - 2
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.volBar);
        noMusic = findViewById(R.id.noMusic);
        prv = findViewById(R.id.prv);
        next = findViewById(R.id.next);
        pause = findViewById(R.id.pause);
        prv.setOnClickListener(this);
        next.setOnClickListener(this);
        pause.setOnClickListener(this);
        final AudioManager manager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        vol = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekBar.setProgress(vol);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC,progress/10,0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        getMusic("/netease/cloudmusic/Music");
        Log.i(TAG, "onCreate: "+MusicName.size());
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(new MusicAdapter(this,MusicName,this));
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prv :{
                if(index == 0){
                    Toast.makeText(this, "已经是第一首了喔", Toast.LENGTH_SHORT).show();
                }else{
                    index --;

                    pause.setBackgroundResource(R.drawable.pause);
                    Intent intent = new Intent(this,MyService.class);
                    intent.putExtra("index",index);
                    intent.putExtra("tag",1);
                    startService(intent);
                }
                break;
            }
            case R.id.next:{
                if(index == MusicName.size()){
                    Toast.makeText(this, "已经是最后一首了喔", Toast.LENGTH_SHORT).show();
                }else {
                    index++;
                    pause.setBackgroundResource(R.drawable.pause);
                    Intent intent = new Intent(this, MyService.class);
                    intent.putExtra("index", index);
                    intent.putExtra("tag",1);
                    startService(intent);
                }
                break;
            }

            case R.id.pause:{
                if(!StaticClass.isStart){
                    StaticClass.isStart = true;

                    pause.setBackgroundResource(R.mipmap.start2);
                    Intent intent = new Intent(this, MyService.class);
                    intent.putExtra("index",index);
                    intent.putExtra("tag",2);
                    startService(intent);
                }else{
                    StaticClass.isStart = false;
                    pause.setBackgroundResource(R.mipmap.stop);
                    Intent intent = new Intent(this, MyService.class);
                    intent.putExtra("tag",1);
                    intent.putExtra("index",index);
                    startService(intent);
                }

                break;
            }

        }
    }

    public void getMusic(String path){
        String musicPath = Environment.getExternalStorageDirectory().getPath()+ path;
        File f = new File(musicPath);
        pause.setBackgroundResource(R.drawable.pause);
        Log.i(TAG, "getMusic: "+musicPath);
        File [] fl = f.listFiles();
        if(fl == null){
            noMusic.setVisibility(View.VISIBLE);
        }else{
            for (int i = 0; i < fl.length; i++) {
                String name = fl[i].getName();
                String suffix = name.substring(name.length()-3,name.length());
                if(suffix.equalsIgnoreCase("mp3")){
                    MusicName.add(name);
                    pathList.add(musicPath+"/"+name);
                }
            }
        }
    }

    @Override
    public void getMusicPosition(int position) {
        index = position;
        Log.i(TAG, "getMusicPosition: "+index);

        Intent intent = new Intent(this,MyService.class);
        intent.putExtra("index",index);
        intent.putExtra("tag",1);
        startService(intent);
    }
}
