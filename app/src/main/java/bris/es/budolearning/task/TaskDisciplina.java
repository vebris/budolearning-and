package bris.es.budolearning.task;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

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
import java.util.HashMap;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Disciplina;
import bris.es.budolearning.domain.adapter.DisciplinaAdapter;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.domain.adapter.CustomRecyclerAdapter;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentDisciplinaDetalle;
import bris.es.budolearning.fragments.FragmentDisciplinas;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.task.volley.VolleyRequestMultipart;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class TaskDisciplina extends TaskAbstract{

    private static final String URL_ARTICULO = "/Disciplina";
    private FragmentAbstract fragment;
    private Configuracion configuracion;

    public TaskDisciplina(Activity activity, FragmentAbstract fragmento){
        super(activity);
        configuracion = Utiles.getConfiguracion(activity.getApplicationContext());
        url = configuracion.getUrl() + Constants.URL_SERVICIO + URL_ARTICULO;
        fragment = fragmento;
    }

    public void list(Usuario usuario, Object filtro, final Object view, final CustomRecyclerAdapter.OnItemClickListener listener) {
        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1:" + url + LIST);
        if(entry != null && !entry.isExpired()){
            try {
                String data = new String(entry.data, "UTF-8");
                mostrarList(new JSONObject(data), view, listener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            JsonPeticion peticion = new JsonPeticion();
            peticion.setUser(new Usuario(usuario));
            peticion.setClub(filtro);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    url + LIST,
                    Utiles.getGson().toJson(peticion),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            updateGeneric(jsonObject);

                            mostrarList(jsonObject, view, listener);

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

    @Override
    public void list(Usuario usuario, Object filtro, final Object view) {
        list(usuario, filtro, view, null);
    }

    private void mostrarList(JSONObject jsonObject, final Object view, CustomRecyclerAdapter.OnItemClickListener listener){
        BLSession.getInstance().setDisciplinas(new ArrayList<Disciplina>());
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Disciplina disciplina = new Disciplina(jsonArray.getJSONObject(i), false);
                BLSession.getInstance().getDisciplinas().add(disciplina);
            }
        } catch (JSONException je) {
            Log.e("Error Response: ", je.toString(), je);
        }

        if(view instanceof GridView) {
            DisciplinaAdapter adapter = new DisciplinaAdapter(
                    BLSession.getInstance().getDisciplinas(),
                    activity);
            ((GridView) view).setAdapter(adapter);
        }

        if(view instanceof RecyclerView) {
            CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(
                    BLSession.getInstance().getDisciplinas(),
                    activity,
                    null,
                    listener);
            ((RecyclerView) view).setAdapter(adapter);
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

                        try {
                            BLSession.getInstance().setDisciplina(new Disciplina(jsonObject.getJSONObject("data"), false));
                        } catch (JSONException jsonE){

                        }

                        fragment.getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new FragmentDisciplinaDetalle())
                                .addToBackStack(FragmentDisciplinas.class.getName()).commit();

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
                        Toast.makeText(activity, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
        String urlDownloadFile = url + DOWNLOAD_FILE + "/"+usuario.getId()+"/"+((Disciplina)elemento).getId();
        ((NetworkImageView) view).setImageUrl(urlDownloadFile, VolleyControler.getInstance().getImageLoader());
        ((NetworkImageView) view).setDefaultImageResId(android.R.anim.cycle_interpolator);
        ((NetworkImageView) view).setErrorImageResId(R.drawable.no_image);
    }

    public void uploadFile(Usuario usuario, Object elemento, File fichero){
        String urlUploadFile = url + UPLOAD_FILE;// + "/" + usuario.getId()+"/"+((Curso)elemento).getId();
        String nombreFichero = "Disciplina_" + usuario.getId() + "_" + ((Disciplina)elemento).getId() + ".jpg";
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
