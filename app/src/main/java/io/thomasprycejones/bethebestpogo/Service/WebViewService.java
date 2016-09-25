package io.thomasprycejones.bethebestpogo.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PixelFormat;
import android.location.Location;
import android.net.http.SslError;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;

import io.thomasprycejones.bethebestpogo.HardwareKeyWatcher;
import io.thomasprycejones.bethebestpogo.MainActivity;
import io.thomasprycejones.bethebestpogo.R;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by Thomas Pryce Jones on 30-08-2016.
 */
public class WebViewService extends Service {

    IBinder mBinder = new LocalBinder();

    private static final int NOTIFICATION_ID = 908114;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private RelativeLayout mTopView;
    DisplayMetrics metrics;
    View mView;
    private HardwareKeyWatcher mHardwareKeyWatcher;

    private WebView webView_pokemap;
    private WebView webView_silphroad;
    String lat;
    String lon;

    private Button button_silphroad;
    private Button button_fastpokemap;
    private Button button_close;
    private ImageButton button_refresh;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public WebViewService getServerInstance() {
            return WebViewService.this;
        }
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        startForeground(NOTIFICATION_ID, createNotification());

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();



        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(
                //WindowManager.LayoutParams.TYPE_INPUT_METHOD |
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        mTopView = (RelativeLayout) li.inflate(R.layout.widget_floatbody, null);
        windowManager.addView(mTopView, params);

        button_fastpokemap = (Button) mTopView.findViewById(R.id.webButton_fastpokemap);
        button_silphroad = (Button) mTopView.findViewById(R.id.webButton_silphroad);
        button_close = (Button) mTopView.findViewById(R.id.webButton_close);
        button_refresh = (ImageButton) mTopView.findViewById(R.id.webView_refresh);

        webView_pokemap = (WebView) mTopView.findViewById(R.id.webView_pokemap);
        webView_pokemap.getSettings().setJavaScriptEnabled(true);
        webView_pokemap.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView_pokemap.clearCache(true);
        webView_pokemap.getSettings().setLoadWithOverviewMode(true);
        webView_pokemap.getSettings().setUseWideViewPort(true);
        webView_pokemap.setWebChromeClient(new WebChromeClient());
        SmartLocation.with(getApplicationContext()).location()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        lat = String.valueOf(location.getLatitude());
                        lon = String.valueOf(location.getLongitude());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            webView_pokemap.loadUrl("https://fastpokemap.se/#"+lat+","+lon);
                        }
                        else{webView_pokemap.loadUrl("https://pokestumble.com/#"+lat+","+lon+",16z");}
                    }
                });




        webView_silphroad = (WebView) mTopView.findViewById(R.id.webView_silphroad);
        webView_silphroad.getSettings().setJavaScriptEnabled(true);
        webView_silphroad.loadUrl("https://thesilphroad.com/research"); //open silphroad




        setButtonListeners();


        RelativeLayout layoutback = (RelativeLayout) mTopView.findViewById(R.id.layoutback_Test);
        layoutback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTopView.setVisibility(View.GONE);
            }
        });


        mHardwareKeyWatcher = new HardwareKeyWatcher(this);
        mHardwareKeyWatcher.setOnHardwareKeysPressedListenerListener(new HardwareKeyWatcher.OnHardwareKeysPressedListener() {
            @Override
            public void onHomePressed() {
                mTopView.setVisibility(View.GONE);
            }

            @Override
            public void onRecentAppsPressed() {
                mTopView.setVisibility(View.GONE);
            }
        });
        mHardwareKeyWatcher.startWatch();



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTopView != null) {
            mTopView.removeAllViews();
            windowManager.removeView(mTopView);
        }
        stopForeground(true);
    }

    private Notification createNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_notificationball);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE);

        final Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(notifyPendingIntent);

        return builder.build();
    }

    public void setVisible(){
        mTopView.setVisibility(View.VISIBLE);
    }

    private void setTeamsAndColors(String team){
        switch (team) {
            case "Valor":
                int myColor_valor = getResources().getColor(R.color.Valor);
                break;
            case "Mystic":
                int myColor_mystic = getResources().getColor(R.color.Mystic);
                ColorStateList cslm = new ColorStateList(new int[][]{new int[0]}, new int[]{myColor_mystic});
                //button_fastpokemap.setBackgroundColor(getResources().getColor(R.color.Mystic));
                //button_silphroad.setBackgroundColor(getResources().getColor(R.color.Mystic));
                break;
            case "Instinct":
                int myColor_instinct = getResources().getColor(R.color.Instinct);
                ColorStateList csli = new ColorStateList(new int[][]{new int[0]}, new int[]{myColor_instinct});
                //button_silphroad.setBackgroundColor(getResources().getColor(R.color.Instinct));
                break;
            default:
                break;
        }
    } //Set UI things

    private void setButtonListeners(){
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView_pokemap.getVisibility() == View.VISIBLE){
                    webView_pokemap.loadUrl("https://fastpokemap.se/#"+lat+","+lon);
                }
                if (webView_silphroad.getVisibility() == View.VISIBLE){
                    webView_silphroad.loadUrl("https://thesilphroad.com/research");
                }
            }
        });

        button_silphroad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView_pokemap.setVisibility(View.GONE);
                webView_silphroad.setVisibility(View.VISIBLE);
            }
        });
        button_fastpokemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView_pokemap.setVisibility(View.VISIBLE);
                webView_silphroad.setVisibility(View.GONE);
            }
        });
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTopView.setVisibility(View.GONE);
            }
        });
    }


}
