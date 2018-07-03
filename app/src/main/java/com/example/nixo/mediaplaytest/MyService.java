package com.example.nixo.mediaplaytest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MyService extends Service {
    public MyService() {
    }
    MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
        //从资源文件加载媒体文件
        mediaPlayer = new MediaPlayer();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int index = intent.getIntExtra("index",0);
        int tag = intent.getIntExtra("tag",1);
        Log.i("Nixo", "onStartCommand: "+MainActivity.pathList.get(index));
        if(tag == 1){
            try {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                mediaPlayer.setDataSource(MainActivity.pathList.get(index));
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(tag == 2){
            mediaPlayer.pause();
        }




        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
