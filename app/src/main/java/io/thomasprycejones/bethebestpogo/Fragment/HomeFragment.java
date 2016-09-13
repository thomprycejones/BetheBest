package io.thomasprycejones.bethebestpogo.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import io.thomasprycejones.bethebestpogo.PrefManager;
import io.thomasprycejones.bethebestpogo.R;
import io.thomasprycejones.bethebestpogo.Service.CustomFloatingViewService;
import io.thomasprycejones.bethebestpogo.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    private static final String TAG = "FloatingViewControl";
    private static final int CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private static final int CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE = 101;
    private Button button_updateapk;
    private Button button_start;
    private ImageView legendarybird;
    private RelativeLayout mainBackground;
    PrefManager prefManager;

    private RequestQueue requestQueue;
    String JsonURL = "https://jsonblob.com/api/jsonBlob/57d23902e4b0dc55a4f40d87";
    int gradleVersionCode;
    String jsonVersionName;
    String updateURL;




    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(io.thomasprycejones.bethebestpogo.R.layout.fragment_home, container, false);
        button_start =(Button) view.findViewById(io.thomasprycejones.bethebestpogo.R.id.startService);
        button_updateapk =(Button) view.findViewById(R.id.home_updateapk);
        legendarybird = (ImageView)view.findViewById(io.thomasprycejones.bethebestpogo.R.id.legendaryBird);

        setButtonListeners();

        // Set UI color
        mainBackground = (RelativeLayout) view.findViewById(io.thomasprycejones.bethebestpogo.R.id.mainBackground);
        String team = prefManager.isWhatTeam();
        setTeamsAndColors(team);

        AdView mAdView = (AdView) view.findViewById(io.thomasprycejones.bethebestpogo.R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadJson();


        return  view;
    }

    //Set the listeners for buttons in this view
    private void setButtonListeners(){

        button_updateapk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isStoragePermissionGranted()){
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(updateURL));
                    String title = "Be the Best - "+jsonVersionName;
                    String fileName = title+".apk";
                    request.setDescription("Update");
                    request.setTitle(title);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    // get download service and enqueue file
                    DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.home_dowloadingtoast), Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showCustomFloatingView(getActivity(), true);
                }
                else{ showCustomFloatingView(getActivity(), true);}
            }
        });



    }


    private void setTeamsAndColors(String team){
        switch (team) {
            case "Valor":
                int myColor_valor = getResources().getColor(io.thomasprycejones.bethebestpogo.R.color.ValorBG);
                mainBackground.setBackgroundColor(myColor_valor);
                legendarybird.setImageResource(io.thomasprycejones.bethebestpogo.R.drawable.moltres);
                break;
            case "Mystic":
                int myColor_mystic = getResources().getColor(io.thomasprycejones.bethebestpogo.R.color.MysticBG);
                mainBackground.setBackgroundColor(myColor_mystic);
                legendarybird.setImageResource(io.thomasprycejones.bethebestpogo.R.drawable.articuno);
                break;
            case "Instinct":
                int myColor_instinct = getResources().getColor(io.thomasprycejones.bethebestpogo.R.color.InstinctBG);
                mainBackground.setBackgroundColor(myColor_instinct);
                legendarybird.setImageResource(io.thomasprycejones.bethebestpogo.R.drawable.zapdos);
                break;
            default:
                mainBackground.setBackgroundColor(Color.GRAY);
                break;
        }
    } //Set UI things





    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHATHEAD_OVERLAY_PERMISSION_REQUEST_CODE) {
            //showChatHead(getActivity(), false);
        } else if (requestCode == CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE) {
            showCustomFloatingView(getActivity(), false);
        }
    }

    @SuppressLint("NewApi")
    private void showCustomFloatingView(Context context, boolean isShowOverlayPermission) {


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startService(new Intent(context, CustomFloatingViewService.class));
            return;
        }

        if (Settings.canDrawOverlays(context)) {
            context.startService(new Intent(context, CustomFloatingViewService.class));
            return;
        }

        if (isShowOverlayPermission) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }


    private void loadJson(){
        requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest arrayreq = new JsonArrayRequest(Request.Method.GET,JsonURL,null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray arrayNews) {
                        try {
                            for (int i = 0; i < arrayNews.length(); i++) {
                                JSONObject jb =(JSONObject) arrayNews.get(i);
                                final int jsonVersionCode = jb.getInt("versionCode");
                                jsonVersionName = jb.getString("versionName");
                                updateURL = jb.getString("download");
                                onJsonLoad(jsonVersionCode);
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        requestQueue.add(arrayreq);
    }

    void onJsonLoad(int jsonVersionCode){
        gradleVersionCode =  BuildConfig.VERSION_CODE;
        if (gradleVersionCode != jsonVersionCode)
        {
            Toast toast = Toast.makeText(getContext(),getResources().getString(R.string.home_newversiontoast), Toast.LENGTH_LONG);
            toast.show();
            button_updateapk.setVisibility(View.VISIBLE);
        }
        else{button_updateapk.setVisibility(View.GONE);}
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;

            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }


    }



}
