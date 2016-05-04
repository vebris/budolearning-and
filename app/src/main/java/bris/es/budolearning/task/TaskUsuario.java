package bris.es.budolearning.task;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bris.es.budolearning.Activity_Logged;
import bris.es.budolearning.Activity_Login;
import bris.es.budolearning.R;
import bris.es.budolearning.asyncTask.TaskLimpieza;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.domain.UsuarioAdapter;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentAlumnoGrado;
import bris.es.budolearning.fragments.FragmentAlumnos;
import bris.es.budolearning.fragments.FragmentDisciplinas;
import bris.es.budolearning.fragments.FragmentFicheros;
import bris.es.budolearning.fragments.FragmentGrados;
import bris.es.budolearning.fragments.FragmentRecursos;
import bris.es.budolearning.fragments.FragmentTabsDisciplinas;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class TaskUsuario extends TaskAbstract{

    private static final String URL_USUARIO = "/Usuario";
    private FragmentAbstract fragment;
    private Configuracion configuracion;
    private int idAlumno;

    public void setIdAlumno(int idAlumno){
        this.idAlumno = idAlumno;
    }

    public TaskUsuario(Activity activity, FragmentAbstract fragmento){
        super(activity);
        configuracion = Utiles.getConfiguracion(activity.getApplicationContext());
        url = configuracion.getUrl() + Constants.URL_SERVICIO + URL_USUARIO;
        fragment = fragmento;
    }

    public void login (final String login, final String pass){
        login(login, pass, null);
    }

    public void login (final String login, final String pass, final String pass1){
        JsonPeticion peticion = new JsonPeticion();
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setPassword(pass);
        peticion.setData(usuario);
        Integer version;
        try {
            version = activity.getApplicationContext().getPackageManager().getPackageInfo(activity.getApplicationContext().getPackageName(), 0).versionCode;
        }catch(Exception e){
            version = 0;
        }
        peticion.setVersion(version);

        // Define your request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + "/login",
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        updateGeneric(jsonObject);

                        try {
                            if(jsonObject.getBoolean("success")) {
                                JSONObject jsonUsuario = jsonObject.getJSONObject("data");
                                Usuario usuario = new Usuario(jsonUsuario, true);
                                if (usuario.getId() <= 0)
                                    BLSession.inicializar();
                                else
                                    BLSession.getInstance().setUsuario(usuario);

                                if(activity instanceof Activity_Login) {
                                    if(pass != null) {
                                        ((Activity_Login) activity).guardarUserYPass(login, pass1);
                                        (new TaskLimpieza(activity)).execute();
                                        BLSession.getInstance().setModoPublicidad(true);
                                    }
                                }

                                recargarUsuario(usuario, new Date());

                                BLSession.getInstance().setDisciplinas(usuario.getDisciplinas());

                                onResponseFinished();
                                updateSubtitle(new Date());
                            } else {
                                UtilesDialog.createErrorMessage(activity, "ERROR", jsonObject.getString("msg")).show();
                            }

                        } catch (JSONException je) {
                            UtilesDialog.createErrorMessage(activity,"ERROR",je.getMessage());
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
                UtilesDialog.createErrorMessage(activity,"ERROR",volleyError.getMessage());
                Log.e("Error Response: ", volleyError.toString());
                onConnectionFailed(volleyError.toString());
            }
        }
        );
        request.setShouldCache(false);
        addToQueue(request, false);
    }

    private void recargarUsuario(Usuario usuario, Date fecha){

        if (fragment == null) {
            Intent i = new Intent(activity, Activity_Logged.class);
            activity.startActivity(i);
        } else if (fragment instanceof FragmentDisciplinas) {
            BLSession.getInstance().setUsuario(usuario);
            BLSession.getInstance().setIdUser(usuario.getId());
            ((FragmentDisciplinas) fragment).recargar();
            updateSubtitle(fecha);
        } else if (fragment instanceof FragmentGrados) {
            ((FragmentGrados) fragment).recargar();
            updateSubtitle(fecha);
        } else if (fragment instanceof FragmentRecursos) {
            ((FragmentRecursos) fragment).recargar();
            updateSubtitle(fecha);
        } else if (fragment instanceof FragmentFicheros) {
            ((FragmentFicheros) fragment).recargar();
            updateSubtitle(fecha);
        } else if (fragment instanceof FragmentAlumnoGrado){
            ((FragmentAlumnoGrado) fragment).refrescar(usuario, BLSession.getInstance().getUsuario());
            updateSubtitle(fecha);
        }
    }


    public void activarUsuario(Usuario usuario, Object filtro){
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(usuario);
        peticion.setData(filtro);
        // Define your request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + "/activarUsuario",
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        updateGeneric(jsonObject);

                        try {
                            UtilesDialog.createAlertMessage(activity, "OK", jsonObject.getString("msg"));
                        } catch (JSONException je) {
                            UtilesDialog.createErrorMessage(activity, "ERROR", "Error al activar el usuario.");
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

    public void cambioContrasena(Usuario usuario, Object filtro){
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(usuario);
        peticion.setData(filtro);
        // Define your request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + "/cambioContrasena",
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        updateGeneric(jsonObject);

                        try {
                            UtilesDialog.createAlertMessage(activity, "OK", jsonObject.getString("msg"));
                        } catch (JSONException je) {
                            UtilesDialog.createErrorMessage(activity, "ERROR", "Error al crear el usuario. Pruebe con un login diferente");
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

    public void subirGrado(final Usuario usuario, final Object filtro, Object disciplina, Object grado){
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(usuario);
        peticion.setData(filtro);
        peticion.setDisciplina(disciplina);
        peticion.setGrado(grado);
        // Define your request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url + "/subirGrado",
                Utiles.getGson().toJson(peticion),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        updateGeneric(jsonObject);

                        try {
                            if(jsonObject.getBoolean("success")){
                                UtilesDialog.createAlertMessage(activity,"OK",jsonObject.getString("msg")).show();
                                onResponseFinished();

                                select(usuario, filtro);
                            } else {
                                UtilesDialog.createErrorMessage(activity,"ERROR","No se ha podido realizar la subida de grado").show();
                            }
                        } catch (JSONException je) {
                            UtilesDialog.createErrorMessage(activity, "ERROR", "Error al crear el usuario. Pruebe con un login diferente");
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
    public void list(Usuario usuario, Object filtro, final Object view) {
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(usuario));
        peticion.setClub(filtro);
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
            // Define your request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    url + LIST,
                    Utiles.getGson().toJson(peticion),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            updateGeneric(jsonObject);

                            onResponseFinished();
                            mostrarList(jsonObject, view, new Date());
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

    private void mostrarList(JSONObject jsonObject, Object view, Date fecha) {
        BLSession.getInstance().setAlumnos(new ArrayList<Usuario>());
        List<String> txtAlumno = new ArrayList<>();
        int elegido = 0;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                Usuario alumno = new Usuario(jsonArray.getJSONObject(i), false);
                BLSession.getInstance().getAlumnos().add(alumno);
                txtAlumno.add(alumno.getApellido1() + " " + alumno.getApellido2() + ", " + alumno.getNombre());
                if(alumno.getId() == idAlumno)
                    elegido = i;
            }
        } catch (JSONException je) {
            Log.e("Error Response: ", je.toString(), je);
        }
        if(view instanceof Spinner) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, txtAlumno);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((Spinner) view).setAdapter(dataAdapter);
            ((Spinner) view).setSelection(elegido);
        }
        if(view instanceof ListView) {
            UsuarioAdapter adapter = new UsuarioAdapter(
                    BLSession.getInstance().getAlumnos(),
                    activity);
            ((ListView)view).setAdapter(adapter);
            updateSubtitle(fecha);
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

                    if(fragment instanceof FragmentAlumnos) {
                        FragmentManager fragmentManager = fragment.getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, new FragmentAlumnos())
                                .addToBackStack(FragmentAlumnos.class.getName())
                                .commit();
                    }
                    if(fragment instanceof FragmentTabsDisciplinas){
                        try {
                            Usuario usuario = new Usuario(jsonObject.getJSONObject("data"), true);
                            recargarUsuario(usuario, new Date());
                        }catch(JSONException je){
                            Log.e("FragmentTabDisciplinas", je.toString());
                        }
                    }
                    if(fragment instanceof FragmentAlumnoGrado){
                        try {
                            Usuario usuario = new Usuario(jsonObject.getJSONObject("data"), true);
                            recargarUsuario(usuario, new Date());
                        }catch(JSONException je){
                            Log.e("FragmentAlumnoGrado ", je.toString());
                        }
                    }
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
    public void update(final Usuario usuario, final Object elemento) {
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
                        //JSONObject data = jsonObject.getJSONObject("data");
                        if(usuario.getId() == ((Usuario)elemento).getId()) {
                            login(usuario.getLogin(), usuario.getPassword());
                        }
                        UtilesDialog.createAlertMessage(activity, "OK", jsonObject.getString("msg")).show();
                    } catch (JSONException je) {
                        UtilesDialog.createErrorMessage(activity, "ERROR", "Error al modificar el usuario");
                    }
                    onResponseFinished();
                    updateSubtitle(new Date());

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

                    BLSession.getInstance().setAlumnos(new ArrayList<Usuario>());
                    try {
                        UtilesDialog.createAlertMessage(activity, "OK", jsonObject.getString("msg"));
                    } catch (JSONException je) {
                        UtilesDialog.createErrorMessage(activity, "ERROR", "Error al borrar usuario.");
                    }
                    onResponseFinished();
                    updateSubtitle(new Date());
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

}
