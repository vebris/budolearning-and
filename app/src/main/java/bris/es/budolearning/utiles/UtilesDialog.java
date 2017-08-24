package bris.es.budolearning.utiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.domain.adapter.FicheroAdapter;
import bris.es.budolearning.fragments.FragmentAbstract;

public class UtilesDialog {

    public static Dialog createProgressDialog(Activity activity, String title, String message){
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.AppTheme);
        ProgressDialog progressDialog = new ProgressDialog(ctw);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        progressDialog.setCustomTitle(createTitle(activity, title, R.color.primary_dark));
        return progressDialog;
    }

    public static Dialog createInfoMessage(Activity activity, String title, String message){
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.AppTheme);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctw);
        dialog.setTitle(title);
        setMessage(dialog, message);
        Dialog dialogReturn = dialog.create();
        dialogReturn.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        ((AlertDialog) dialogReturn).setCustomTitle(createTitle(activity, title, R.color.primary_dark));
        return dialogReturn;
    }

    public static Dialog createErrorMessage(Activity activity, String title, String message){
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.AppThemeError);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctw);
        dialog.setTitle(title);
        setMessage(dialog, message);
        Dialog dialogReturn = dialog.create();
        dialogReturn.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        ((AlertDialog) dialogReturn).setCustomTitle(createTitle(activity, title, R.color.tertiary_dark));
        return dialogReturn;
    }

    public static Dialog createQuestionYesNo(Activity activity, String title, String message,
                                             String yesText, DialogInterface.OnClickListener yesListener,
                                             String noText, DialogInterface.OnClickListener noListener){

        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.AppTheme);
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ctw);
        dialogo.setTitle(title);
        setMessage(dialogo, message);
        dialogo.setCancelable(true);

        dialogo.setPositiveButton(yesText, yesListener);
        dialogo.setNegativeButton(noText, noListener);

        Dialog dialogReturn = dialogo.create();
        dialogReturn.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        ((AlertDialog) dialogReturn).setCustomTitle(createTitle(activity, title, R.color.primary_dark));
        return dialogReturn;

    }

    public static AlertDialog createImageMessage(Activity activity, String titulo, NetworkImageView imageView, Listener listener){

        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.AppTheme);
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

        dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                dlg.dismiss();
            }
        });

        AlertDialog dialogReturn = dialog.create();
        dialogReturn.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        ((AlertDialog) dialogReturn).setCustomTitle(createTitle(activity, titulo, R.color.primary_dark));
        return dialogReturn;

    }


    public static AlertDialog createListDialog(Activity activity, String title, String[] items, DialogInterface.OnClickListener itemListener){
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.AppTheme);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctw);
        dialog.setTitle(title);
        dialog.setItems(items, itemListener);

        AlertDialog dialogReturn = dialog.create();
        dialogReturn.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        ((AlertDialog) dialogReturn).setCustomTitle(createTitle(activity, title, R.color.primary_dark));
        return dialogReturn;
    }

    private static void setMessage(AlertDialog.Builder dialog, String mensaje){
        if(mensaje == null) return;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            dialog.setMessage(Html.fromHtml(mensaje,0));
        } else {
            dialog.setMessage(Html.fromHtml(mensaje));
        }
    }

    public interface Listener{
        void cargarImagen();
    }


    public static Dialog createVideoExtraMessage(Activity activity, FragmentAbstract fragment, String title, List<Fichero> ficheros){

        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.AppTheme);
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ctw);

        LayoutInflater inflater = activity.getLayoutInflater();
        View generalView = inflater.inflate(R.layout.fragment_ficheros, null);
        ListView lv = (ListView) generalView.findViewById(R.id.ficheroListView);

        FicheroAdapter ficheroAdapter = new FicheroAdapter(ficheros, activity, fragment, false);
        lv.setAdapter(ficheroAdapter);

        dialogo.setView(generalView);
        dialogo.setCancelable(true);
        dialogo.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                dlg.dismiss();
            }
        });

        Dialog dialogReturn = dialogo.create();
        dialogReturn.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        ((AlertDialog) dialogReturn).setCustomTitle(createTitle(activity, title, R.color.primary_dark));
        return dialogReturn;

    }

    private static View createTitle(Activity activity, String title, int resource){
        TextView tv = new TextView(activity);
        tv.setText(title);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(activity.getResources().getColor(resource));
        tv.setTextSize(activity.getResources().getDimension(R.dimen.tituloAlert));
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return tv;
    }
}

