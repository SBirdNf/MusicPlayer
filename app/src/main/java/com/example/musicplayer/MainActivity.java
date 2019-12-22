package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Timer timer;
    ListView listView;
    SeekBar seekBar;
    private String[] data = { "music.mp3"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);
        ListView listView = (ListView) findViewById(R.id.listview);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //获取托拽的进度
                int progress=seekBar.getProgress();
                //设置播放的进度
                mediaPlayer.seekTo(progress);

            }
        });



        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            initMediaPlayer();//初始化MediaPlayer
        }
    }

    private void initMediaPlayer(){
        try{
            File file = new File(Environment.getExternalStorageDirectory(),"music.mp3");
            mediaPlayer.setDataSource(file.getPath());//指定音频文件路径
            mediaPlayer.prepare();//让mediaplayer进入准备状态
            mediaPlayer.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void  onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }else{
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();//开始播放
                }
                break;
            case  R.id.pause:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();//暂停播放
                }
            case  R.id.stop:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.reset();//停止播放
                    initMediaPlayer();
                }
                break;
                default:
                    break;
        }
    }

    private void getProgress() {

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                //获取歌曲的进度
                int p = mediaPlayer.getCurrentPosition();

                //将获取歌曲的进度赋值给seekbar
                seekBar.setProgress(p);
            }
        }, 0, 2000);
    }



    protected void onDestory(){
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }


}
