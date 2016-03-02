package bris.es.budolearning.asyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import bris.es.budolearning.Activity_Logged;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.UtilesDialog;

public abstract class TaskAbstract extends AsyncTask<String, Integer, String> {

    protected TaskAbstract esteTask;
    protected Activity activity;
    protected ProgressDialog progressDialog;
    protected boolean descarga;
    protected String tituloDialogo;
    protected String textoDialogo;
    protected boolean forzarActualizacion;

    private PowerManager.WakeLock mWakeLock;

    protected TaskAbstract(Activity activity){
        this.activity = activity;
        this.esteTask = this;
        this.descarga = false;
        this.tituloDialogo = "Conectando con el servidor";
        this.textoDialogo = "Descargando ...";
        this.forzarActualizacion = false;
    }

    protected TaskAbstract(Activity activity, boolean forzarActualizacion){
        this.activity = activity;
        this.esteTask = this;
        this.descarga = false;
        this.tituloDialogo = "Conectando con el servidor";
        this.textoDialogo = "Descargando ...";
        this.forzarActualizacion = forzarActualizacion;
    }

    protected TaskAbstract(Activity activity, boolean descarga, String tituloDialogo, String textoDialog){
        this.activity = activity;
        this.esteTask = this;
        this.descarga = descarga;
        this.tituloDialogo = tituloDialogo;
        this.textoDialogo = textoDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // instantiate it within the onCreate method
        progressDialog = UtilesDialog.createProgressDialog(activity, tituloDialogo, textoDialogo);
        if (descarga) {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }else {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setCancelable(true);
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancel download task
                progressDialog.cancel();
            }
        });
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                esteTask.cancel(true);
            }
        });
        progressDialog.show();
        lockScreenOrientation();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        unlockScreenOrientation();
        progressDialog.dismiss();

        if(activity instanceof Activity_Logged) {
            ((Activity_Logged) activity).setSubtitle(BLSession.getInstance().getFechaFichero());
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(descarga) {
            // if we get here, length is known, now set indeterminate to false
            if(progressDialog != null) {
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(values[1]);
                progressDialog.setProgress(values[0]);
            }
        }

    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
        unlockScreenOrientation();
        progressDialog.dismiss();
        if(descarga) {
            mWakeLock.release();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        unlockScreenOrientation();
        progressDialog.dismiss();
        if(descarga) {
            mWakeLock.release();
        }
    }

    private void lockScreenOrientation() {
        if(descarga) {
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
        if(descarga) {
            mWakeLock.release();
        }
    }

}
