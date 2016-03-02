package bris.es.budolearning.task;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Grado;
import bris.es.budolearning.domain.GradoAdapter;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.task.volley.VolleyRequestMultipart;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class TaskGrado extends TaskAbstract{

    private static final String URL_GRADO = "/Grado";
    private FragmentAbstract fragment;
    private Configuracion configuracion;

    public TaskGrado(Activity activity, FragmentAbstract fragmento){
        super(activity);
        configuracion = Utiles.getConfiguracion(activity.getApplicationContext());
        url = configuracion.getUrl() + Constants.URL_SERVICIO + URL_GRADO;
        fragment = fragmento;
    }

    @Override
    public void list(Usuario usuario, Object filtro, final Object view) {
        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1:" + url + LIST);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                mostrarList(new JSONObject(data), view, new Date(entry.serverDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            JsonPeticion peticion = new JsonPeticion();
            peticion.setUser(new Usuario(usuario));
            peticion.setDisciplina(filtro);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    url + LIST,
                    Utiles.getGson().toJson(peticion),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            updateGeneric(jsonObject);

                            mostrarList(jsonObject, view, new Date());

                            onResponseFinished();
                        }

                        @Override
                        protected void finalize() throws Throwable {
                            super.finalize();
                            onConnectionFinished();
                        }

                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // Show Error message
                    UtilesDialog.createErrorMessage(activity, "ERROR", volleyError.getMessage());
                    Log.e("Error Response: ", volleyError.toString());
                    onConnectionFailed(volleyError.toString());
                }
            }
            );
            request.setShouldCache(true);
            addToQueue(request, false);
        }
    }

    private void mostrarList(JSONObject jsonObject, final Object view, Date fecha){
        BLSession.getInstance().setGrados(new ArrayList<Grado>());
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Grado grado = new Grado(jsonArray.getJSONObject(i), false);
                BLSession.getInstance().getGrados().add(grado);
            }
        } catch (JSONException je) {
            Log.e("Error Response: ", je.toString(), je);
        }

        GradoAdapter adapter = new GradoAdapter(
                BLSession.getInstance().getGrados(),
                activity);
        ((GridView) view).setAdapter(adapter);
    }

    @Override
    public void select(Usuario usuario, Object filtro) {
    }

    @Override
    public void insert(Usuario usuario, Object elemento) {
    }

    @Override
    public void update(Usuario usuario, Object elemento) {
    }

    @Override
    public void delete(Usuario usuario, Object elemento) {
    }

    public void downloadFile(Usuario usuario, Object elemento, View view){
        String urlDownloadFile = url + DOWNLOAD_FILE + "/"+usuario.getId()+"/"+((Grado)elemento).getId();
        ((NetworkImageView) view).setImageUrl(urlDownloadFile, VolleyControler.getInstance().getImageLoader());
        ((NetworkImageView) view).setDefaultImageResId(R.anim.loading_animation);
        ((NetworkImageView) view).setErrorImageResId(R.drawable.no_image);
    }

    public void uploadFile(Usuario usuario, Object elemento, File fichero){
        String urlUploadFile = url + UPLOAD_FILE;// + "/" + usuario.getId()+"/"+((Curso)elemento).getId();
        String nombreFichero = "Grado_" + usuario.getId() + "_" + ((Grado)elemento).getId() + ".jpg";
        VolleyRequestMultipart request = new VolleyRequestMultipart(
                urlUploadFile,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UtilesDialog.createErrorMessage(activity,"ERROR",volleyError.getMessage());
                        Log.e("Error Response: ", volleyError.toString());
                        onConnectionFailed(volleyError.toString());
                    }
                },
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UtilesDialog.createErrorMessage(activity,"OK","Fichero subido correctamente.");
                        onResponseFinished();
                    }
                    @Override
                    protected void finalize() throws Throwable {
                        super.finalize();
                        onConnectionFinished();
                    }
                },
                fichero,
                nombreFichero,
                new HashMap<String,String>()
        );
        request.setShouldCache(false);
        addToQueue(request, true);
    }
}
