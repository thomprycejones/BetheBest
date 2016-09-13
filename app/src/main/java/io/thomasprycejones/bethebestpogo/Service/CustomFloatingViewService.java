package io.thomasprycejones.bethebestpogo.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import io.thomasprycejones.bethebestpogo.DeleteActionActivity;
import io.thomasprycejones.bethebestpogo.PrefManager;

import java.lang.ref.WeakReference;

import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;


public class CustomFloatingViewService extends Service implements FloatingViewListener {

    private static final String TAG = "CustomFloatingViewService";
    private static final int NOTIFICATION_ID = 908114;
    private IBinder mCustomFloatingViewServiceBinder;
    private FloatingViewManager mFloatingViewManager;
    private Boolean isShowing = false;
    private WebViewService mWebViewService;
    boolean mBounded;
    private PrefManager prefManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        prefManager = new PrefManager(this);


        if (mFloatingViewManager != null) {
            return START_STICKY;
        }

        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mCustomFloatingViewServiceBinder = new CustomFloatingViewServiceBinder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final ImageView iconView = (ImageView) inflater.inflate(io.thomasprycejones.bethebestpogo.R.layout.widget_floathead, null, false);

        mFloatingViewManager = new FloatingViewManager(this, this);
        mFloatingViewManager.setFixedTrashIconImage(io.thomasprycejones.bethebestpogo.R.drawable.ic_trash_fixed);
        mFloatingViewManager.setActionTrashIconImage(io.thomasprycejones.bethebestpogo.R.drawable.ic_trash_action);
        // Setting Options(you can change options at any time)
        loadDynamicOptions();
        // Initial Setting Options (you can't change options after created.)
        final FloatingViewManager.Options options = loadOptions(metrics);
        mFloatingViewManager.addViewToWindow(iconView, options);

        startForeground(NOTIFICATION_ID, createNotification());

        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
                if (mBounded==true)
                {
                    mWebViewService.setVisible();
                }
            }
        });


        //UI Color
        String team = prefManager.isWhatTeam();
        setTeamsAndColors(team, iconView);

        return START_REDELIVER_INTENT;
    }

    //Set up the UI colors
    private void setTeamsAndColors(String team, ImageView iconView){
        switch (team) {
            case "Valor":
                int myColor_valor = getResources().getColor(io.thomasprycejones.bethebestpogo.R.color.Valor);
                iconView.setImageResource(io.thomasprycejones.bethebestpogo.R.mipmap.ic_pokevalor);
                break;
            case "Mystic":
                int myColor_mystic = getResources().getColor(io.thomasprycejones.bethebestpogo.R.color.Mystic);
                iconView.setImageResource(io.thomasprycejones.bethebestpogo.R.mipmap.ic_pokemystic);
                break;
            case "Instinct":
                int myColor_instinct = getResources().getColor(io.thomasprycejones.bethebestpogo.R.color.Instinct);
                iconView.setImageResource(io.thomasprycejones.bethebestpogo.R.mipmap.ic_pokeinstinct);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        destroy();
        stopService();
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mCustomFloatingViewServiceBinder;
    }


    @Override
    public void onFinishFloatingView() {
        stopSelf();
    }


    private void destroy() {
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mFloatingViewManager = null;
        }
    }


    private Notification createNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(io.thomasprycejones.bethebestpogo.R.drawable.ic_notificationball);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE);

        // PendingIntent作成
        final Intent notifyIntent = new Intent(this, DeleteActionActivity.class);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(notifyPendingIntent);

        return builder.build();
    }


    private void loadDynamicOptions() {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        final String displayModeSettings = sharedPref.getString("settings_display_mode", "");
        if ("Always".equals(displayModeSettings)) {
            mFloatingViewManager.setDisplayMode(FloatingViewManager.DISPLAY_MODE_SHOW_ALWAYS);
        } else if ("FullScreen".equals(displayModeSettings)) {
            mFloatingViewManager.setDisplayMode(FloatingViewManager.DISPLAY_MODE_HIDE_FULLSCREEN);
        } else if ("Hide".equals(displayModeSettings)) {
            mFloatingViewManager.setDisplayMode(FloatingViewManager.DISPLAY_MODE_HIDE_ALWAYS);
        }

    }


    private FloatingViewManager.Options loadOptions(DisplayMetrics metrics) {
        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        // Shape
        final String shapeSettings = sharedPref.getString("settings_shape", "");
        if ("Circle".equals(shapeSettings)) {
            options.shape = FloatingViewManager.SHAPE_CIRCLE;
        } else if ("Rectangle".equals(shapeSettings)) {
            options.shape = FloatingViewManager.SHAPE_RECTANGLE;
        }

        // Margin
        final String marginSettings = sharedPref.getString("settings_margin", String.valueOf(options.overMargin));
        options.overMargin = Integer.parseInt(marginSettings);

        // MoveDirection
        final String moveDirectionSettings = sharedPref.getString("settings_move_direction", "");
        if ("Default".equals(moveDirectionSettings)) {
            options.moveDirection = FloatingViewManager.MOVE_DIRECTION_DEFAULT;
        } else if ("Left".equals(moveDirectionSettings)) {
            options.moveDirection = FloatingViewManager.MOVE_DIRECTION_LEFT;
        } else if ("Right".equals(moveDirectionSettings)) {
            options.moveDirection = FloatingViewManager.MOVE_DIRECTION_RIGHT;
        } else if ("Fix".equals(moveDirectionSettings)) {
            options.moveDirection = FloatingViewManager.MOVE_DIRECTION_NONE;
        }

        // Init X/Y
        final String initXSettings = sharedPref.getString("settings_init_x", "");
        final String initYSettings = sharedPref.getString("settings_init_y", "");
        if (!TextUtils.isEmpty(initXSettings) && !TextUtils.isEmpty(initYSettings)) {
            final int offset = (int) (48 + 8 * metrics.density);
            options.floatingViewX = (int) (metrics.widthPixels * Float.parseFloat(initXSettings) - offset);
            options.floatingViewY = (int) (metrics.heightPixels * Float.parseFloat(initYSettings) - offset);
        }

        // Initial Animation
        final boolean animationSettings = sharedPref.getBoolean("settings_animation", options.animateInitialMove);
        options.animateInitialMove = animationSettings;

        return options;
    }


    public static class CustomFloatingViewServiceBinder extends Binder {

        /**
         * CustomFloatingViewService
         */
        private final WeakReference<CustomFloatingViewService> mService;

        /**
         * コンストラクタ
         *
         * @param service CustomFloatingViewService
         */
        CustomFloatingViewServiceBinder(CustomFloatingViewService service) {
            mService = new WeakReference<>(service);
        }

        /**
         * CustomFloatingViewServiceを取得します。
         *
         * @return CustomFloatingViewService
         */
        public CustomFloatingViewService getService() {
            return mService.get();
        }
    }


    public void startService() {
        Intent mIntent = new Intent(this, WebViewService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }


    public void stopService() {
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            mWebViewService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            WebViewService.LocalBinder mLocalBinder = (WebViewService.LocalBinder)service;
            mWebViewService = mLocalBinder.getServerInstance();
        }
    };

}
