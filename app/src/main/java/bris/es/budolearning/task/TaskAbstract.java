package bris.es.budolearning.task;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import bris.es.budolearning.Activity_Logged;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public abstract class TaskAbstract {

    protected String url;
    protected Activity activity;

    public static String LIST = "/list";
    public static String SELECT = "/select";
    public static String INSERT = "/insert";
    public static String UPDATE = "/update";
    public static String DELETE = "/delete";
    public static String UPLOAD_FILE = "/insertFile";
    public static String DOWNLOAD_FILE = "/downloadFile";
    public static String DOWNLOAD_FILE_ESPECIAL = "/downloadFileEspecial";

    private Dialog pDialog;
    private PowerManager.WakeLock mWakeLock;
    private boolean descarga = false;

    public TaskAbstract(Activity activity){
        this.activity = activity;
    }

    public boolean isDescarga() {
        return descarga;
    }

    public void setDescarga(boolean descarga) {
        this.descarga = descarga;
    }

    public void addToQueue(Request request, boolean descarga) {
        if(Utiles.isConnected(activity)) {
            setDescarga(descarga);
            onPreStartConnection(request);
            VolleyControler.getInstance().addToRequestQueue(request);
        } else {
            UtilesDialog.createErrorMessage(activity, "ERROR", "No hay acceso a internet.").show();
        }
    }

    public void onPreStartConnection(final Request request) {
        if(pDialog != null) return;
        pDialog = UtilesDialog.createProgressDialog(activity, "Conectando Servidor", "Cargando...");

        if (isDescarga()) {
            if(pDialog instanceof ProgressDialog) ((ProgressDialog)pDialog).setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }else {
            if(pDialog instanceof ProgressDialog) ((ProgressDialog)pDialog).setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        pDialog.setCancelable(true);

        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        if(pDialog != null) pDialog.show();
        lockScreenOrientation();
    }

    public void onResponseFinished() {
        if(pDialog != null) pDialog.hide();
        unlockScreenOrientation();
        pDialog = null;
    }

    public void onConnectionFinished() {
        if(pDialog != null) pDialog.hide();
        unlockScreenOrientation();
        pDialog = null;
    }

    public void onConnectionFailed(String error) {
        if(pDialog != null) pDialog.hide();
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
        pDialog = null;
    }

    public String getUrl(){return url;}


    public abstract void list(Usuario usuario, Object filtro, Object view);
    public void borrarList(){
        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        cache.remove("1:" + url + LIST);
    }
    public abstract void select(Usuario usuario, Object elemento);
    public void borrarSelect(){
        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        cache.remove("1:" + url + SELECT);
    }
    public abstract void insert(Usuario usuario, Object elemento);
    public abstract void update(Usuario usuario, Object elemento);
    public abstract void delete(Usuario usuario, Object elemento);


    private void lockScreenOrientation() {
        if(isDescarga()) {
            PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
        }

        int currentOrientation = activity.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        if(isDescarga()) {
            mWakeLock.release();
        }
    }

    protected void updateGeneric(JSONObject jsonObject){
        int puntos;
        try { puntos = jsonObject.getInt("puntos"); } catch(JSONException je) {puntos = 0;}
        BLSession.getInstance().setPuntos(puntos);
        if(activity instanceof Activity_Logged) {
            ((Activity_Logged) activity).updatePoints();
        }
    }

}
