package bris.es.budolearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Utiles;


public abstract class Activity_Publicidad extends AppCompatActivity {

    private final String TAG = "Activity_Publicidad";

    private InterstitialAd adMobInterstitial;
    private TaskUtiles taskUtiles;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            BLSession.setInstance((BLSession) savedInstanceState.getSerializable("BLSession"));
        }

        taskUtiles = new TaskUtiles(this, null);
    }

    public void cargarPublicidad(){
        Configuracion cfg = Utiles.getConfiguracion(getApplicationContext());

        if(cfg.isPublicidadBannerAdMob() && BLSession.getInstance().isModoPublicidad()){
            cargarPublicidadBannerAdMob();
        } else {
            AdView adView = (AdView)this.findViewById(R.id.adMobBanner);
            adView.pause();
            adView.setVisibility(View.INVISIBLE);
            ((RelativeLayout)this.findViewById(R.id.main_cc)).removeView(adView);
        }

        if(cfg.isPublicidadInterstialAdMob() && BLSession.getInstance().isModoPublicidad()){
            cargarPublicidadIntestitialAdMob();
        }

    }

    private void cargarPublicidadIntestitialAdMob(){
        adMobInterstitial = new InterstitialAd(this);
        if(adMobInterstitial != null) {
            adMobInterstitial.setAdUnitId(getString(R.string.admob_interstitial));
            adMobInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    siguiente();
                }
                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                    eliminarPublicidad();
                }
            });
            adMobInterstitial.loadAd(new AdRequest.Builder()
                    .addTestDevice("C8E1FB03CF6FBC5E76CD435B1DAC1A06")
                    .build());
        }
    }
    private void cargarPublicidadBannerAdMob(){
        AdView adMobBanner = (AdView)this.findViewById(R.id.adMobBanner);
        if(adMobBanner != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("C8E1FB03CF6FBC5E76CD435B1DAC1A06")
                    .build();
            adMobBanner.loadAd(adRequest);
            adMobBanner.setAdListener(new AdListener() {
                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                    taskUtiles.publicidad();
                    eliminarPublicidad();
                }
            });
        }
    }

    public void mostrarPublicidad() {
        if (adMobInterstitial != null && adMobInterstitial.isLoaded()) {
            mostrarPublicidadAdMob();
        } else {
            siguiente();
        }
    }

    private void mostrarPublicidadAdMob(){
        if(adMobInterstitial != null && adMobInterstitial.isLoaded()) {
            adMobInterstitial.show();
        } else {
            siguiente();
        }
    }

    public abstract void siguiente();

    public void eliminarPublicidad(){
        BLSession.getInstance().setModoPublicidad(false);
        adMobInterstitial = null;

        AdView adView = (AdView)this.findViewById(R.id.adMobBanner);
        adView.pause();
        adView.setVisibility(View.INVISIBLE);
        ((RelativeLayout) this.findViewById(R.id.main_cc)).removeView(adView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("BLSession", BLSession.getInstance());
    }
}
