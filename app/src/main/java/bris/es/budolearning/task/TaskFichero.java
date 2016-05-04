package bris.es.budolearning.task;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import bris.es.budolearning.Activity_Mp4_View;
import bris.es.budolearning.Activity_Pdf_View;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.domain.FicheroAdapter;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.fragments.FDialog;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentFicheroDetalle;
import bris.es.budolearning.fragments.FragmentFicheros;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.task.volley.VolleyRequestInputStream;
import bris.es.budolearning.task.volley.VolleyRequestMultipart;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class TaskFichero extends TaskAbstract{

    public static final String URL_FICHERO = "/Fichero";
    private FragmentAbstract fragment;
    private Configuracion configuracion;
    private View view;

    public TaskFichero(Activity activity, FragmentAbstract fragmento){
        super(activity);
        configuracion = Utiles.getConfiguracion(activity.getApplicationContext());
        url = configuracion.getUrl() + Constants.URL_SERVICIO + URL_FICHERO;
        fragment = fragmento;
    }

    public void setView(View view){
        this.view = view;
    }

    @Override
    public void list(Usuario usuario, Object filtro, final Object view) {
        Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1:" + url + LIST);
        if(entry != null && !entry.isExpired()){
            try {
                String data = new String(entry.data, "UTF-8");
                mostrarList(new JSONObject(data), view, new Date(entry.serverDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            JsonPeticion peticion = new JsonPeticion();
            peticion.setUser(new Usuario(usuario));
            peticion.setRecurso(filtro);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    url + LIST,
                    Utiles.getGson().toJson(peticion),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            updateGeneric(jsonObject);

                            mostrarList(jsonObject , view, new Date());

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

    private void mostrarList(JSONObject jsonObject, Object view, Date fecha) {
        BLSession.getInstance().setFicheros(new ArrayList<Fichero>());
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Fichero fichero = new Fichero(jsonArray.getJSONObject(i));
                BLSession.getInstance().getFicheros().add(fichero);
            }
        } catch (JSONException je) {
            Log.e("Error Response: ", je.toString(), je);
        }

        FicheroAdapter adapter = new FicheroAdapter(
                BLSession.getInstance().getFicheros(),
                activity);
        ((ListView) view).setAdapter(adapter);
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
                            BLSession.getInstance().setFichero(new Fichero(jsonObject.getJSONObject("data")));
                        } catch (JSONException jsonE){

                        }

                        fragment.getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new FragmentFicheroDetalle())
                                .addToBackStack(FragmentFicheros.class.getName()).commit();
                        /*
                        FragmentManager fragmentManager = fragment.getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, new FragmentFicheroDetalle())
                                .addToBackStack(FragmentFicheros.class.getName())
                                .commit();
                        */
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
        peticion.setRecurso(BLSession.getInstance().getRecurso());
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

    public void downloadFile(Usuario usuario, final Object elemento, final View view){
        downloadFile(usuario, elemento, view, null, 0);
    }

    public void downloadFile(Usuario usuario, final Object elemento, View view, final TaskUtiles taskUtiles, final int coste) {
        BLSession.getInstance().setFichero((Fichero)elemento);
        String idFichero = String.valueOf(((Fichero) elemento).getId());
        String urlDownloadFile = url + DOWNLOAD_FILE + "/" + usuario.getId() + "/" + idFichero;
        if(coste == 0){
            urlDownloadFile = url + DOWNLOAD_FILE_ESPECIAL + "/" + usuario.getId() + "/" + idFichero;
        }

        final File fichero = new File(Utiles.getDirectorioCacheVideo(), Utiles.md5(String.valueOf(idFichero)));

        if (!fichero.exists()) {
            VolleyRequestInputStream request = new VolleyRequestInputStream(Request.Method.GET, urlDownloadFile,
                    new Response.Listener<byte[]>() {
                        @Override
                        public void onResponse(byte[] response) {
                            try {
                                if (response != null && response.length > 0) {
                                    writeToFile(response, fichero);
                                    verDocumento(elemento, fichero, taskUtiles, coste);
                                } else {
                                    String texto = "Puntos insuficientes, necesita " + ((Fichero) elemento).getCoste() + " para descargar el fichero y solamente tiene " + BLSession.getInstance().getPuntos();
                                    FDialog.newInstance("ERROR", texto).show(fragment.getFragmentManager(), "Dialogo");
                                }
                            } catch (Exception e) {
                                Log.e(getClass().getCanonicalName(), e.getLocalizedMessage());
                            }
                            onResponseFinished();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UtilesDialog.createErrorMessage(activity, "ERROR", volleyError.getMessage());
                            Log.e("Error Response: ", volleyError.toString());
                            onConnectionFailed(volleyError.toString());
                        }
                    }, null);
            request.setShouldCache(false);
            addToQueue(request, true);
        } else {
            try {
                byte[] b = new byte[1];
                RandomAccessFile file = new RandomAccessFile(fichero, "rw");
                file.read(b, 0, 1);
                file.close();
                file = new RandomAccessFile(fichero, "rw");
                file.write(b, 0, 1);
                file.close();
            }catch(IOException ioe){
                Log.e(this.getClass().getCanonicalName(), "Error al cambiar la fecha de modificación del fichero: " + fichero.getName());
            }

            verDocumento(elemento, fichero, taskUtiles, coste);
        }
    }

    private void verDocumento(Object elemento, File fichero, TaskUtiles taskUtiles, int coste){
        if (((Fichero) elemento).getExtension().equalsIgnoreCase("PDF")){
            Intent i = new Intent(activity, Activity_Pdf_View.class);
            fragment.getActivity().startActivity(i);
            if(taskUtiles != null) taskUtiles.visualizaciones(coste);
        } else {
            Intent i = new Intent(activity, Activity_Mp4_View.class);
            fragment.getActivity().startActivity(i);
            if(taskUtiles != null) taskUtiles.visualizaciones(coste);
            /*
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(fichero), "video/*");
            fragment.getActivity().startActivityForResult(intent, FragmentAbstract.VER_VIDEO);
            */
        }
    }

    private void writeToFile(byte[] data, File file){
        try {
            if (Utiles.tamanoCacheVideo() > configuracion.getTamanoCacheVideos()) {
                File directorio = Utiles.getDirectorioCacheVideo();
                File[] files = directorio.listFiles();
                Arrays.sort(files, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                    }
                });

                for(File f:files){
                    Log.d(getClass().toString(), f.getCanonicalPath());
                }
                for(int i=0;Utiles.tamanoCacheVideo() > configuracion.getTamanoCacheVideos();i++) {
                    if (files[i].isFile()) {
                        files[i].delete();
                    }
                }
            }

            InputStream input = new ByteArrayInputStream(data);
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
            byte data2[] = new byte[64 * 1024];
            int count;
            while ((count = input.read(data2)) != -1) {
                output.write(data2, 0, count);
            }
            output.flush();
            output.close();
            input.close();

            ((FragmentFicheros)fragment).recargar();

        }catch (IOException ioe){
            Log.e("ERROR", ioe.getLocalizedMessage(), ioe);
        }
    }

    public void uploadFile(Usuario usuario, Object elemento, File fichero){
        String urlUploadFile = url + UPLOAD_FILE;// + "/" + usuario.getId()+"/"+((Curso)elemento).getId();
        String nombreFichero = "Fichero_" + usuario.getId() + "_" + ((Fichero)elemento).getId() + fichero.getName().substring(fichero.getName().indexOf('.'));
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
