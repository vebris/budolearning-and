package bris.es.budolearning.utiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import bris.es.budolearning.R;

public class UtilesDialog {

    public static ProgressDialog createProgressDialog(Activity activity, String title, String message){
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.CustomDialogTheme);
        ProgressDialog progressDialog = new ProgressDialog(ctw);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }

    public static AlertDialog createAlertMessage(Activity activity, String title, String message){
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.CustomDialogTheme);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctw);
        dialog.setTitle(title);
        dialog.setMessage(message);
        return dialog.create();
    }
    public static AlertDialog createErrorMessage(Activity activity, String title, String message){
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.CustomDialogErrorTheme);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctw);
        dialog.setTitle(title);
        dialog.setMessage(message);
        return dialog.create();
    }

    public static AlertDialog createListDialog(Activity activity, String title, String[] items, DialogInterface.OnClickListener itemListener){
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.CustomDialogTheme);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctw);
        dialog.setTitle(title);
        dialog.setItems(items, itemListener);
        return dialog.create();
    }

    public static AlertDialog createQuestionYesNo(Activity activity, String title, String message,
        String yesText, DialogInterface.OnClickListener yesListener,
        String noText, DialogInterface.OnClickListener noListener){

        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.CustomDialogTheme);
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ctw);
        dialogo.setTitle(title);
        dialogo.setMessage(message);
        dialogo.setCancelable(false);

        dialogo.setCancelable(false);
        dialogo.setPositiveButton(yesText, yesListener);
        dialogo.setNegativeButton(noText, noListener);

        return dialogo.create();
    }

    public static AlertDialog createImageMessage(Activity activity, String titulo, NetworkImageView imageView, Listener listener){

        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.CustomDialogErrorTheme);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ctw);

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels

        if(width > height){
            imageView.setMinimumHeight(width * 4/5);
            imageView.setMinimumWidth(width * 4/5);
        } else {
            imageView.setMinimumHeight(height * 4/5);
            imageView.setMinimumWidth(height * 4/5);
        }

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        dialog.setView(imageView);
        dialog.setTitle(titulo);

        listener.cargarImagen();

        dialog.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                dlg.dismiss();
            }
        });

        return dialog.create();

    }


    public interface Listener{
        void cargarImagen();
    }
}
