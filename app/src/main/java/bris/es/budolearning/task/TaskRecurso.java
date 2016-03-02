package bris.es.budolearning.task;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.Date;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Grado;
import bris.es.budolearning.domain.Recurso;
import bris.es.budolearning.domain.RecursoAdapter;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentRecursoDetalle;
import bris.es.budolearning.fragments.FragmentRecursos;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class TaskRecurso extends TaskAbstract{

    private static final String URL_GRADO = "/Recurso";
    private FragmentAbstract fragment;
    private Configuracion configuracion;

    public TaskRecurso(Activity activity, FragmentAbstract fragmento){
        super(activity);
        configuracion = Utiles.getConfiguracion(activity.getApplicationContext());
        url = configuracion.getUrl() + Constants.URL_SERVICIO + URL_GRADO;
        fragment = fragmento;
    }

    public void list(Usuario usuario, Object disciplina, Object grado, final Object view) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setDisciplina(disciplina);
        peticion.setGrado(grado);
        list(usuario, peticion, view);
    }

    @Override
    public void list(Usuario usuario, Object filtro, final Object view) {
        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1:" + url + LIST);
        if(entry != null){
            try {
                String data = new String(entry.data, "UTF-8");
                mostrarList(new JSONObject(data), view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            JsonPeticion peticion = (JsonPeticion) filtro;
            peticion.setUser(new Usuario(usuario));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    url + LIST,
                    Utiles.getGson().toJson(peticion),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            updateGeneric(jsonObject);

                            mostrarList(jsonObject, view);

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

    private void mostrarList(JSONObject jsonObject, Object view) {
        BLSession.getInstance().setRecursos(new ArrayList<Recurso>());
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Recurso recurso = new Recurso(jsonArray.getJSONObject(i));
                BLSession.getInstance().getRecursos().add(recurso);
            }
        } catch (JSONException je) {
            Log.e("Error Response: ", je.toString(), je);
        }

        RecursoAdapter adapter = new RecursoAdapter(
                BLSession.getInstance().getRecursos(),
                activity);
        ((ListView)view).setAdapter(adapter);
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

                        try {
                            BLSession.getInstance().setRecurso(new Recurso(jsonObject.getJSONObject("data")));
                        } catch (JSONException jsonE){

                        }

                        fragment.getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new FragmentRecursoDetalle())
                                .addToBackStack(FragmentRecursos.class.getName()).commit();

                        onResponseFinished();
                        updateSubtitle(new Date());
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
        peticion.setDisciplina(BLSession.getInstance().getDisciplina());
        peticion.setGrado(BLSession.getInstance().getGrado());
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
                            UtilesDialog.createAlertMessage(activity, "OK", jsonObject.getString("msg")).show();
                        } catch (JSONException je) {
                            Log.e("Error Response: ", je.toString(), je);
                        }
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
    public void update(Usuario usuario, Object elemento) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setData(elemento);
        peticion.setDisciplina(BLSession.getInstance().getDisciplina());
        peticion.setGrado(BLSession.getInstance().getGrado());
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
                            UtilesDialog.createAlertMessage(activity, "OK", jsonObject.getString("msg")).show();
                        } catch (JSONException je) {
                            Log.e("Error Response: ", je.toString(), je);
                        }
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
                            UtilesDialog.createAlertMessage(activity, "OK", jsonObject.getString("msg")).show();
                        } catch (JSONException je) {
                            Log.e("Error Response: ", je.toString(), je);
                        }
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

    public void downloadFile(Usuario usuario, Object elemento, View view){
        String urlDownloadFile = url + DOWNLOAD_FILE + "/"+usuario.getId()+"/"+((Grado)elemento).getId();
        ((NetworkImageView) view).setImageUrl(urlDownloadFile, VolleyControler.getInstance().getImageLoader());
        ((NetworkImageView) view).setDefaultImageResId(R.anim.loading_animation);
        ((NetworkImageView) view).setErrorImageResId(R.drawable.no_image);
    }

}
