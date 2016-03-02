package bris.es.budolearning.task;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import bris.es.budolearning.Activity_Login;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Estadistica;
import bris.es.budolearning.domain.EstadisticaAdapter;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.domain.Puntos;
import bris.es.budolearning.domain.PuntosAdapter;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class TaskUtiles extends TaskAbstract{

    private static final String URL_USUARIO = "/Utiles";
    private FragmentAbstract fragment;
    private Configuracion configuracion;
    private static String FICHERO_VISUALIZACIONES = "visualizaciones.bl";

    public TaskUtiles(Activity activity, FragmentAbstract fragmento){
        super(activity);
        configuracion = Utiles.getConfiguracion(activity.getApplicationContext());
        url = configuracion.getUrl() + Constants.URL_SERVICIO + URL_USUARIO;
        fragment = fragmento;
    }

    public void checkVersion(int version){

        JsonPeticion peticion = new JsonPeticion();
        peticion.setVersion(version);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + "/checkVersion",
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        updateGeneric(jsonObject);

                        if(activity instanceof Activity_Login){
                            ((Activity_Login) activity).pedidoVersion = true;
                        }

                        try {
                            if(jsonObject.getBoolean("success")){
                                UtilesDialog.createQuestionYesNo(activity,
                                        "Importante",
                                        "Nueva versión encontrada ¿ Desea descargar la actualización ?",
                                        "Confirmar",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogo1, int id) {
                                                Configuracion configuracion = Utiles.getConfiguracion(activity.getApplication());
                                                String url = configuracion.getUrl() + Constants.URL_SERVICIO + Constants.REST_DOWNLOAD_LAST_VERSION + "/";
                                                final DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                                                Uri uri = Uri.parse(url);
                                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                                                request.setVisibleInDownloadsUi(true);
                                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                                request.setDescription("Nueva versión BudoLearning");
                                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "budolearning.apk");
                                                final long enqueue = downloadManager.enqueue(request);

                                                BroadcastReceiver receiver = new BroadcastReceiver() {
                                                    @Override
                                                    public void onReceive(Context context, Intent intent) {
                                                        String action = intent.getAction();
                                                        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                                                            intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                                                            DownloadManager.Query query = new DownloadManager.Query();
                                                            query.setFilterById(enqueue);
                                                            Cursor c = downloadManager.query(query);
                                                            if (c.moveToFirst()) {
                                                                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                                                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                                                                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                                                                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    installIntent.setDataAndType(
                                                                            Uri.parse(c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))),
                                                                            "application/vnd.android.package-archive");
                                                                    activity.startActivity(installIntent);
                                                                }
                                                            }
                                                        }
                                                    }
                                                };

                                                activity.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                            }
                                        },
                                        "Cancelar",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogo1, int id) {
                                            }
                                        }).show();
                            } else {
                                //    Toast.makeText(activity.getApplicationContext(), "No hay nueva version", Toast.LENGTH_LONG).show();
                                Log.d(this.getClass().toString(), "Error check version");
                            }
                        }catch (JSONException je){
                            Log.d(this.getClass().toString(), "Error check version", je);
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
                UtilesDialog.createErrorMessage(activity, "ERROR", volleyError.getMessage()).show();
                Log.e("Error Response: ", volleyError.toString());
                onConnectionFailed(volleyError.toString());
            }
        }
        );
        request.setShouldCache(false);
        addToQueue(request, false);
    }

    private JsonArray leerFichero(){
        JsonElement datos;
        try {
            JsonParser parser = new JsonParser();
            FileReader fileReader = new FileReader(activity.getFilesDir() + "/" + FICHERO_VISUALIZACIONES);
            datos = parser.parse(fileReader);
        } catch (Exception e){
            datos = new JsonArray();
        }
        if(datos == null || datos.toString().equals("null")) datos=new JsonArray();
        return datos.getAsJsonArray();
    }
    private void escribirFichero(JsonArray jsonArray){
        try {
            FileWriter fileWriter = new FileWriter(activity.getFilesDir() + "/" + FICHERO_VISUALIZACIONES);
            fileWriter.write(jsonArray.toString());
            fileWriter.close();
        } catch (Exception e){
            Log.e(getClass().getCanonicalName(), "Error al escribir fichero de visualizaciones " + this.getClass().getCanonicalName());
        }
    }

    public void publicidad(){
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(BLSession.getInstance().getUsuario()));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + "/publicidad",
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        updateGeneric(jsonObject);
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
                UtilesDialog.createErrorMessage(activity, "ERROR", volleyError.getMessage()).show();
                Log.e("Error Response: ", volleyError.toString());
                onConnectionFailed(volleyError.toString());
            }
        }
        );
        request.setShouldCache(false);
        addToQueue(request, false);
    }

    public void visualizaciones(int coste){

        if(BLSession.getInstance().getFichero() == null) return;

        JsonArray jsonArray = leerFichero();

        JSONObject jsonObject = new JSONObject();
        try {
            Fichero f = new Fichero();
            f.setId(BLSession.getInstance().getFichero().getId());
            jsonObject.put("fichero", new JSONObject(new Gson().toJson(f)));
            jsonObject.put("usuario", new JSONObject(new Gson().toJson(new Usuario(BLSession.getInstance().getUsuario()))));
            jsonObject.put("fecha", Calendar.getInstance().getTimeInMillis());
            jsonObject.put("descargado", 0);
            jsonObject.put("extension", "Android Local");
            jsonObject.put("coste", coste);
        } catch (JSONException e) {
           Log.e("Error visualizacion: ", BLSession.getInstance().getFichero().getId() + " " +
                   BLSession.getInstance().getUsuario().getId() + " ==> " + e.getLocalizedMessage(), e);
        }

        JsonParser parser = new JsonParser();
        jsonArray.add(parser.parse(jsonObject.toString()));
        escribirFichero(jsonArray);

        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(BLSession.getInstance().getUsuario()));
        peticion.setVisualizaciones(jsonArray);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + "/visualizaciones",
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        updateGeneric(jsonObject);

                        try {
                            if (jsonObject.getBoolean("success")) {
                                File f = new File(activity.getFilesDir() + "/" + FICHERO_VISUALIZACIONES);
                                f.delete();
                            } else {
                                Log.d(this.getClass().toString(), "Error visualizaciones");
                            }
                        }catch (JSONException je){
                            Log.d(this.getClass().toString(), "Error visualizaciones", je);
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
                UtilesDialog.createErrorMessage(activity, "ERROR", volleyError.getMessage()).show();
                Log.e("Error Response: ", volleyError.toString());
                onConnectionFailed(volleyError.toString());
            }
        }
        );
        request.setShouldCache(false);
        addToQueue(request, false);
    }

    public void borrarEstadisticas(){
        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        cache.remove("1:" + url + "/estadisticas");
    }
    public void estadisticas(Usuario usuario, Object filtro, final Object view) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setData(filtro);

        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1:" + url + "/estadisticas");
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                JSONArray jsonArray = new JSONObject(data).getJSONArray("data");
                mostrarEstadisticas(jsonArray, view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    url + "/estadisticas",
                    Utiles.getGson().toJson(peticion),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            updateGeneric(jsonObject);

                            try {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                mostrarEstadisticas(jsonArray, view);
                            } catch (JSONException je) {
                                UtilesDialog.createErrorMessage(activity, "ERROR", je.getMessage());
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
    private void mostrarEstadisticas(JSONArray jsonArray, Object view){
        List<Estadistica> estadisticas = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                estadisticas.add(new Estadistica(jsonArray.getJSONObject(i)));
            }catch(JSONException je){
                Log.d(this.getClass().toString(), "Error mostrarEstadisticas", je);
            }
        }
        EstadisticaAdapter adapter = new EstadisticaAdapter(
                estadisticas,
                activity);
        ((ListView) view).setAdapter(adapter);
    }

    public void borrarPuntos(Usuario user, Usuario usuario, Object puntos, final Object view) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(user));
        peticion.setData(usuario);
        peticion.setPuntos(puntos);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + "/borrarPuntos",
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //updateGeneric(jsonObject);
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            int total = jsonObject.getInt("puntos");
                            mostrarPuntos(jsonArray, view, total);
                        } catch (JSONException je) {
                            UtilesDialog.createErrorMessage(activity, "ERROR", je.getMessage());
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
                UtilesDialog.createErrorMessage(activity, "ERROR", volleyError.getMessage());
                Log.e("Error Response: ", volleyError.toString());
                onConnectionFailed(volleyError.toString());
            }
        });
        request.setShouldCache(true);
        addToQueue(request, false);
    }

    public void borrarPuntos(){
        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        cache.remove("1:" + url + "/listadoPuntos");
    }
    public void puntos(Usuario usuario, Object filtro, final Object view) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setData(filtro);
        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1:" + url + "/listadoPuntos");
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                JSONArray jsonArray = new JSONObject(data).getJSONArray("data");
                int total = new JSONObject(data).getInt("puntos");
                mostrarPuntos(jsonArray, view, total);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    url + "/listadoPuntos",
                    Utiles.getGson().toJson(peticion),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            //updateGeneric(jsonObject);

                            try {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                int total = jsonObject.getInt("puntos");
                                mostrarPuntos(jsonArray, view, total);
                            } catch (JSONException je) {
                                UtilesDialog.createErrorMessage(activity, "ERROR", je.getMessage());
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
                    UtilesDialog.createErrorMessage(activity, "ERROR", volleyError.getMessage());
                    Log.e("Error Response: ", volleyError.toString());
                    onConnectionFailed(volleyError.toString());
                }
            });
            request.setShouldCache(true);
            addToQueue(request, false);
        }
    }
    private void mostrarPuntos(JSONArray jsonArray, Object view, int total){
        List<Puntos> puntos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                puntos.add(new Puntos(jsonArray.getJSONObject(i)));
            }catch(JSONException je){
                Log.d(this.getClass().toString(), "Error mostrarPuntos", je);
            }
        }
        BLSession.getInstance().setListPuntos(puntos);
        ((TextView)((LinearLayout)((View)view).getParent()).findViewById(R.id.puntos_total)).setText(String.valueOf(total));
        PuntosAdapter adapter = new PuntosAdapter(
                puntos,
                activity);
        ((ListView) view).setAdapter(adapter);
    }

    public void bonus(Usuario usuario, Object filtro, final Object view) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setData(filtro);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + "/bonus",
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //updateGeneric(jsonObject);

                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            int total = jsonObject.getInt("puntos");
                            mostrarPuntos(jsonArray, view, total);
                        } catch (JSONException je) {
                            UtilesDialog.createErrorMessage(activity, "ERROR", je.getMessage());
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
                UtilesDialog.createErrorMessage(activity, "ERROR", volleyError.getMessage());
                Log.e("Error Response: ", volleyError.toString());
                onConnectionFailed(volleyError.toString());
            }
        });
        request.setShouldCache(false);
        addToQueue(request, false);
    }

    @Override
    public void list(Usuario usuario, Object filtro, Object view) {

    }

    @Override
    public void select(Usuario usuario, Object elemento) {

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

}
