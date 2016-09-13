package io.thomasprycejones.bethebestpogo.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import io.thomasprycejones.bethebestpogo.PrefManager;


public class AdsFragment extends Fragment {

    PrefManager prefManager;

    private RelativeLayout mainBackground;
    private Button buttonInterstitial;
    private Button buttonBanner;

    private AdView mAdViewRequested;
    private InterstitialAd mInterstitialAd;

    public AdsFragment() {
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
        View view = inflater.inflate(io.thomasprycejones.bethebestpogo.R.layout.fragment_ads, container, false);

        buttonBanner = (Button) view.findViewById(io.thomasprycejones.bethebestpogo.R.id.ads_buttonbanner);
        buttonInterstitial = (Button) view.findViewById(io.thomasprycejones.bethebestpogo.R.id.ads_buttoninterstitial);

        //Banner
        AdView mAdView = (AdView) view.findViewById(io.thomasprycejones.bethebestpogo.R.id.adView);
        mAdViewRequested = (AdView) view.findViewById(io.thomasprycejones.bethebestpogo.R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Interstitial
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(io.thomasprycejones.bethebestpogo.R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(adRequest);

        setButtonListeners();



        // Set UI color
        mainBackground = (RelativeLayout) view.findViewById(io.thomasprycejones.bethebestpogo.R.id.ads_mainBackground);
        String team = prefManager.isWhatTeam();
        setTeamsAndColors(team);
        return view;
    }

    private void setTeamsAndColors(String team){
        switch (team) {
            case "Valor":
                int myColor_valor = getResources().getColor(io.thomasprycejones.bethebestpogo.R.color.ValorBG);
                mainBackground.setBackgroundColor(myColor_valor);
                break;
            case "Mystic":
                int myColor_mystic = getResources().getColor(io.thomasprycejones.bethebestpogo.R.color.MysticBG);
                mainBackground.setBackgroundColor(myColor_mystic);
                break;
            case "Instinct":
                int myColor_instinct = getResources().getColor(io.thomasprycejones.bethebestpogo.R.color.InstinctBG);
                mainBackground.setBackgroundColor(myColor_instinct);
                break;
            default:
                mainBackground.setBackgroundColor(Color.GRAY);
                break;
        }
    } //Set UI things

    private void setButtonListeners(){
        buttonBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdViewRequested.loadAd(adRequest);

            }
        });

        buttonInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        mInterstitialAd.show();
                    }
                });
            }
        });
    }


}
