package bris.es.budolearning.task;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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
import bris.es.budolearning.domain.Curso;
import bris.es.budolearning.domain.adapter.CursoAdapter;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentCursos;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.task.volley.VolleyRequestMultipart;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class TaskCurso extends TaskAbstract{

    private static final String URL_CURSO = "/Curso";
    private FragmentAbstract fragment;
    private Configuracion configuracion;

    public TaskCurso(Activity activity, FragmentAbstract fragmento){
        super(activity);
        configuracion = Utiles.getConfiguracion(activity.getApplicationContext());
        url = configuracion.getUrl() + Constants.URL_SERVICIO + URL_CURSO;
        fragment = fragmento;
    }

    @Override
    public void list(Usuario usuario, Object filtro, final Object view) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setData(filtro);

        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1:" + url + LIST);
        if(entry != null && !entry.isExpired()){
            try {
                String data = new String(entry.data, "UTF-8");
                // Gestionar datos, como convertir a XML, JSON, bitmap, etc.
                mostrarList(new JSONObject(data), view, new Date(entry.serverDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
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
                    UtilesDialog.createErrorMessage(activity,"ERROR",volleyError.getMessage());
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
        BLSession.getInstance().setCursos(new ArrayList<Curso>());
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                BLSession.getInstance().getCursos().add(new Curso(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException je) {
            Log.e("Error Response: ", je.toString(), je);
        }

        if (view instanceof ListView) {
            CursoAdapter adapter = new CursoAdapter(
                    BLSession.getInstance().getCursos(),
                    activity);
            ((ListView) view).setAdapter(adapter);
        }
    }

    @Override
    public void select(Usuario usuario, Object filtro) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setData(filtro);
        // Define your request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
            url + SELECT,
            Utiles.getGson().toJson(peticion),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    updateGeneric(jsonObject);

                    FragmentManager fragmentManager = fragment.getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new FragmentCursos())
                            .addToBackStack(FragmentCursos.class.getName())
                            .commit();
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
                    UtilesDialog.createErrorMessage(activity,"ERROR",volleyError.getMessage());
                    Log.e("Error Response: ", volleyError.toString());
                    onConnectionFailed(volleyError.toString());
                }
            }
        );
        request.setShouldCache(false);
        addToQueue(request, false);
    }

    @Override
    public void insert(Usuario usuario, Object elemento) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setData(elemento);
        // Define your request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
            url + INSERT,
            Utiles.getGson().toJson(peticion),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    updateGeneric(jsonObject);

                    try {
                        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
                        cache.remove("1:" + url + LIST);
                        UtilesDialog.createInfoMessage(activity, "OK", jsonObject.getString("msg")).show();
                    } catch (JSONException je) {
                        Log.e("Error Response: ", je.toString(), je);
                    }
                    onResponseFinished();
                    activity.onBackPressed();
                    Utiles.hideKeyboard();
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
                    UtilesDialog.createErrorMessage(activity,"ERROR",volleyError.getMessage());
                    Log.e("Error Response: ", volleyError.toString());
                    onConnectionFailed(volleyError.toString());
                }
            }
        );
        request.setShouldCache(false);
        addToQueue(request, false);
    }

    @Override
    public void update(Usuario usuario, Object elemento) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setData(elemento);
        // Define your request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
            url + UPDATE,
            Utiles.getGson().toJson(peticion),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    updateGeneric(jsonObject);

                    try {
                        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
                        cache.remove("1:" + url + LIST);
                        UtilesDialog.createInfoMessage(activity, "OK", jsonObject.getString("msg")).show();
                    } catch (JSONException je) {
                        Log.e("Error Response: ", je.toString(), je);
                    }
                    onResponseFinished();
                    activity.onBackPressed();
                    Utiles.hideKeyboard();
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
        request.setShouldCache(false);
        addToQueue(request, false);
    }

    @Override
     public void delete(Usuario usuario, Object elemento) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setData(elemento);
        // Define your request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + DELETE,
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        updateGeneric(jsonObject);

                        try {
                            Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
                            cache.remove("1:" + url + LIST);
                            UtilesDialog.createInfoMessage(activity, "OK", jsonObject.getString("msg")).show();
                        } catch (JSONException je) {
                            Log.e("Error Response: ", je.toString(), je);
                        }
                        onResponseFinished();
                        activity.onBackPressed();
                        Utiles.hideKeyboard();
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
                UtilesDialog.createErrorMessage(activity,"ERROR",volleyError.getMessage());
                Log.e("Error Response: ", volleyError.toString());
                onConnectionFailed(volleyError.toString());
            }
        }
        );
        request.setShouldCache(false);
        addToQueue(request, false);
    }

    public void downloadFile(Usuario usuario, Object elemento, View view){
        String urlDownloadFile = url + DOWNLOAD_FILE + "/" + usuario.getId()+"/"+((Curso)elemento).getId();
        ((NetworkImageView) view).setImageUrl(urlDownloadFile, VolleyControler.getInstance().getImageLoader());
    }

    public void uploadFile(Usuario usuario, Object elemento, File fichero){
        String urlUploadFile = url + UPLOAD_FILE;// + "/" + usuario.getId()+"/"+((Curso)elemento).getId();
        String nombreFichero = "Curso_" + usuario.getId() + "_" + ((Curso)elemento).getId() + ".jpg";
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
