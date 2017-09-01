package example.com.musicplayservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;

import example.com.musicplayservice.service.PlayerService;
import example.com.musicplayservice.util.FlyLog;

public class MainActivity extends AppCompatActivity {
    private IMusicPlayerAidlInterface playerService;
    private boolean binded;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        //this.startService(new Intent(this,PlayerService.class));
        binded = bindService(new Intent(mContext, PlayerService.class), conn, mContext.BIND_AUTO_CREATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    playerService.start(1,true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binded) {
            FlyLog.d("====unbindService");
           /* try {
                playerService.stop();
                playerService.reset();
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
            unbindService(conn);
            binded = false;
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FlyLog.d("====onServiceConnected");
            playerService = IMusicPlayerAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            FlyLog.d("onServiceDisconnected");
            try {
                FlyLog.d();
                playerService.stop();
                playerService.reset();
                playerService = null;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
}
