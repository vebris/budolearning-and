package bris.es.budolearning.utiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import bris.es.budolearning.R;
import bris.es.budolearning.task.volley.VolleyControler;

public class Utiles {

    public static boolean esSoloAdmin(){
        return "ADMINISTRADOR".equalsIgnoreCase(BLSession.getInstance().getUsuario().getRol());
    }
    public static boolean esAdmin(){
        return "ADMINISTRADOR".equalsIgnoreCase(BLSession.getInstance().getUsuario().getRol()) ||
                "PROFESOR".equalsIgnoreCase(BLSession.getInstance().getUsuario().getRol()) ;
    }

    public static SimpleDateFormat getDateFormatDMAHM() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ROOT);
    }
    public static SimpleDateFormat getDateFormatDMAHMS() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ROOT);
    }
    public static SimpleDateFormat getDateFormatMA(){
        return new SimpleDateFormat("MMMM/yyyy", Locale.ROOT);
    }
    public static SimpleDateFormat getDateFormatDMA() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);
    }
    public static SimpleDateFormat getDateFormatHM() {
        return new SimpleDateFormat("HH:mm", Locale.ROOT);
    }

    public static void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) VolleyControler.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 0);
    }

    public static Configuracion getConfiguracion(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Configuracion configuracion = new Configuracion();
        configuracion.setRecordarUsuario(prefs.getBoolean("preferences_general_recordar_usuario", true));
        configuracion.setUrl(prefs.getString("preferences_url", context.getResources().getString(R.string.pref_default_url)));

        configuracion.setUsoRedes(prefs.getString("preferences_download_redes", context.getResources().getString(R.string.pref_default_download_redes)));


        configuracion.setPublicidadBannerAdMob(prefs.getBoolean("pref_PublicidadBannerAdMob", Boolean.parseBoolean(context.getResources().getString(R.string.pref_default_especial_publi_banner_adMob))));
        configuracion.setPublicidadInterstialAdMob(prefs.getBoolean("pref_PublicidadInterstialAdMob", Boolean.parseBoolean(context.getResources().getString(R.string.pref_default_especial_publi_intert_adMob))));
        configuracion.setVerUltimaDescarga(prefs.getBoolean("preferences_general_ver_ultima_descarga", Boolean.parseBoolean(context.getResources().getString(R.string.pref_default_ver_ultima_descarga))));

        configuracion.setTamanoCachePeticiones(Integer.parseInt(prefs.getString("preferences_limite_cache_peticiones", context.getResources().getString(R.string.pref_default_download_limite_cache_peticiones))));
        configuracion.setTamanoCacheImagenes(Integer.parseInt(prefs.getString("preferences_limite_cache_imagenes", context.getResources().getString(R.string.pref_default_download_limite_cache_imagenes))));
        configuracion.setTamanoCacheVideos(Integer.parseInt(prefs.getString("preferences_limite_cache_videos", context.getResources().getString(R.string.pref_default_download_limite_cache_videos))));

        return configuracion;
    }


    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    /*
    private static boolean isConnectedWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        return networkInfo != null && networkInfo.isConnected();
    }
    */

    private static boolean isConnected3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String md5(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(), 0, s.length());
        return new BigInteger(1, m.digest()).toString(16);
    }

    public static String encrypt(String msg){
        //return new String (Base64.decode(msg.getBytes(), Base64.DEFAULT));
        return msg;
    }
    public static String decrypt(String msg){
        //return new String (Base64.encode(msg.getBytes(), Base64.DEFAULT));
        return msg;
    }

    public static void borrarFicheros(String extension){
        File directory = Utiles.getDirectorioCacheVideo();
        if(directory.exists()) {
            File[] files = directory.listFiles();
            if(files.length > 0) {
                for (File f : files) {
                    Log.d(Utiles.class.getCanonicalName(), f.getAbsolutePath() + " --> BORRADO");
                    if(extension == null || f.getAbsolutePath().endsWith(extension)) {
                        f.delete();
                    }
                }
            } else {
                Log.d(Utiles.class.getCanonicalName(), directory.getAbsolutePath() + " NO TIENE FICHEROS." );
            }
        } else {
            Log.d(Utiles.class.getCanonicalName(), directory.getAbsolutePath() + " NO EXISTE" );
        }
    }

    public static double tamanoCachePeticiones(){
        return redondear(dirSize(Utiles.getDirectorioCache(), null)/1024.0, 2);
    }
    public static double tamanoCacheImagenes(){
        return redondear((dirSize(Utiles.getDirectorioCacheImagenes(), ".0")/1024.0),2);
    }
    public static double tamanoCacheVideo(){
        return redondear((dirSize(Utiles.getDirectorioCacheVideo(), null)/1024.0),2);
    }


    private static double dirSize(File dir, String extension){
        double useSpace = 0.0;
        if(dir.exists()) {
            File[] listFiles = dir.listFiles();
            for (File file : listFiles) {
                if (file.isFile()) {
                    if(extension == null || file.getAbsolutePath().endsWith(extension)) {
                        useSpace += file.length() / 1024.0;
                    }
                } else {
                    useSpace += dirSize(file, extension);
                }
            }
        }
        return useSpace;
    }

    private static double redondear( double numero, int decimales ) {
        return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
    }

    public static boolean permitirDescarga(Configuracion configuracion, Context context){
        return !(isConnected3G(context) && configuracion.getUsoRedes() == Configuracion.SOLO_WIFI);
    }

    public static String toMB(long number){
        DecimalFormat format = new DecimalFormat("0.00 MB");
        return format.format(number / 1024F / 1024F);
    }

    public static File getDirectorioDescargas(){
        // Crear el directorio en caso de que no exista
        File directory = Environment.getExternalStoragePublicDirectory(Constants.DIRECTORIO_DESCARGAS);
        if(!directory.exists()){
            directory.mkdir();
        }
        return directory;
    }
    public static File getDirectorioCache(){
        // Crear el directorio en caso de que no exista
        File directory = Environment.getExternalStoragePublicDirectory(Constants.DIRECTORIO_DESCARGAS + Constants.DIRECTORIO_CACHE_PETICIONES);
        if(!directory.exists()){
            directory.mkdir();
        }
        return directory;
    }
    public static File getDirectorioCacheVideo(){
        // Crear el directorio en caso de que no exista
        File directory = Environment.getExternalStoragePublicDirectory(Constants.DIRECTORIO_DESCARGAS + Constants.DIRECTORIO_CACHE_VIDEO);
        if(!directory.exists()){
            directory.mkdir();
        }
        return directory;
    }
    public static File getDirectorioCacheImagenes(){
        // Crear el directorio en caso de que no exista
        File directory = Environment.getExternalStoragePublicDirectory(Constants.DIRECTORIO_DESCARGAS + Constants.DIRECTORIO_CACHE_IMAGENES);
        if(!directory.exists()){
            directory.mkdir();
        }
        return directory;
    }

    public static Gson getGson(){
        try {
            JsonSerializer<Date> ser = new JsonSerializer<Date>() {
                @Override
                public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                    return src == null ? null : new JsonPrimitive(src.getTime());
                }
            };

            JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return json == null ? null : new Date(json.getAsLong());
                }
            };

            return new GsonBuilder().registerTypeAdapter(Date.class, ser).registerTypeAdapter(Date.class, deser).create();
        } catch (JsonSyntaxException | JsonIOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static long copy(BufferedInputStream from, BufferedOutputStream to) throws IOException {
        byte[] buf = new byte[512000];
        long total = 0;
        while(true) {
            int r = from.read(buf);
            if(r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }
}
