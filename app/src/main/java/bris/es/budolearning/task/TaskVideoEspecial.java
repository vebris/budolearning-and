package bris.es.budolearning.task;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import bris.es.budolearning.domain.FicheroEspecialAdapter;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.domain.VideoEspecial;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class TaskVideoEspecial extends TaskAbstract {

    public static final String URL_FICHERO = "/VideoEspecial";
    private FragmentAbstract fragment;
    private Configuracion configuracion;

    public TaskVideoEspecial(Activity activity, FragmentAbstract fragmento) {
        super(activity);
        configuracion = Utiles.getConfiguracion(activity.getApplicationContext());
        url = configuracion.getUrl() + Constants.URL_SERVICIO + URL_FICHERO;
        fragment = fragmento;
    }

    @Override
    public void list(Usuario usuario, Object filtro, final Object view) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setClub(usuario.getEntrena());

        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1:" + url + LIST);
        if(entry != null && !entry.isExpired()){
            try {
                String data = new String(entry.data, "UTF-8");
                // Gestionar datos, como convertir a XML, JSON, bitmap, etc.
                mostrarList(new JSONObject(data), view, new Date(entry.serverDate));
            } catch (Exception e) {
                Log.e(getClass().getCanonicalName(), e.getLocalizedMessage());
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

    private void mostrarList(JSONObject jsonObject, final Object view, Date fecha) {
        BLSession.getInstance().setVideosEspeciales(new ArrayList<VideoEspecial>());
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                BLSession.getInstance().getVideosEspeciales().add(new VideoEspecial(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException je) {
            Log.e("Error Response: ", je.toString(), je);
        }
        Collections.sort(BLSession.getInstance().getVideosEspeciales(), new Comparator<VideoEspecial>() {
            @Override
            public int compare(VideoEspecial lhs, VideoEspecial rhs) {
                int comparator = rhs.getFin().compareTo(lhs.getFin());
                if (comparator == 0)
                    comparator = lhs.getInicio().compareTo(rhs.getInicio());
                return comparator;
            }
        });
        if (view instanceof ListView) {
            FicheroEspecialAdapter adapter = new FicheroEspecialAdapter(
                    BLSession.getInstance().getVideosEspeciales(),
                    activity);
            ((ListView) view).setAdapter(adapter);
        }
        updateSubtitle(fecha);
    }

    @Override
    public void select(Usuario usuario, Object filtro) {
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
                            UtilesDialog.createAlertMessage(activity, "OK", jsonObject.getString("msg")).show();
                        } catch (JSONException je) {
                            UtilesDialog.createErrorMessage(activity, "ERROR", "Error al insertar usuario");
                        }
                        onResponseFinished();
                        updateSubtitle(new Date());
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
                            UtilesDialog.createAlertMessage(activity, "OK", jsonObject.getString("msg")).show();
                        } catch (JSONException je) {
                            Log.e("Error Response: ", je.toString(), je);
                        }
                        onResponseFinished();
                        updateSubtitle(new Date());
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
                        updateSubtitle(new Date());
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
}
