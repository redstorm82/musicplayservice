package example.com.musicplayservice.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.IOException;

import example.com.musicplayservice.IMusicPlayerAidlInterface;
import example.com.musicplayservice.R;
import example.com.musicplayservice.util.FlyLog;

/**
 * Created by shenshaohui on 2017/8/24.
 */

public class PlayerService extends Service {
    private IMusicPlayerAidlInterface playerService;
    //用于播放音乐等媒体资源
    private MediaPlayer mediaPlayer;
    private Context mContext;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        FlyLog.d();
        return mBinder;
    }

    @Override
    public void onCreate() {
        FlyLog.d("create playerService");
        super.onCreate();
        mContext = this;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            //为播放器添加播放完成时的监听器
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //发送广播到MainActivity
                    Intent intent = new Intent();
                    intent.setAction("com.complete");
                    sendBroadcast(intent);
                    FlyLog.d("mediaPlayer complete");
                }
            });
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        FlyLog.d();
        return true;
    }

    @Override
    public void onDestroy() {
        FlyLog.d();
        mBinder = null;

        super.onDestroy();
    }

    private Binder mBinder = new IMusicPlayerAidlInterface.Stub() {
        @Override
        public void start(int id, boolean loop) throws RemoteException {
            FlyLog.d();
            try {
                mediaPlayer.stop();
                String uri = "android.resource://" + getPackageName() + "/"+R.raw.m01;
                mediaPlayer.setDataSource(mContext,Uri.parse(uri));
                mediaPlayer.prepare();
                mediaPlayer.setLooping(loop);
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void stop() throws RemoteException {
            FlyLog.d();
            mediaPlayer.stop();
        }

        @Override
        public void pause() throws RemoteException {
            FlyLog.d();
            //播放器不为空，并且正在播放
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }

        @Override
        public void resume() throws RemoteException {
            FlyLog.d();
            mediaPlayer.start();
        }

        @Override
        public void reset() throws RemoteException {
            FlyLog.d();
            mediaPlayer.reset();
        }
    };

}
