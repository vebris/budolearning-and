package bris.es.budolearning.asyncTask;

import android.content.Context;

import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.utiles.BLSession;

/*
import android.content.Context;
import com.belladati.httpclientandroidlib.HttpEntity;
import com.belladati.httpclientandroidlib.HttpResponse;
import com.belladati.httpclientandroidlib.client.HttpClient;
import com.belladati.httpclientandroidlib.client.methods.HttpPost;
import com.belladati.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import com.belladati.httpclientandroidlib.entity.mime.content.ByteArrayBody;
import com.belladati.httpclientandroidlib.impl.client.HttpClientBuilder;
import com.belladati.httpclientandroidlib.util.EntityUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentClubes;
import bris.es.budolearning.fragments.FragmentCursos;
import bris.es.budolearning.fragments.FragmentFicheros;
import bris.es.budolearning.fragments.FragmentTabsDisciplinas;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Configuracion;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.task.TaskUsuario;
*/
@Deprecated
public class old_TaskFileUpload extends TaskAbstract {
    private Context context;
    private FragmentAbstract fragment;
    private String filePath;
    public old_TaskFileUpload(FragmentAbstract ff, Context context, String filePath) {
        super(ff.getActivity(), false, "Subida de fichero", filePath);
        this.fragment = ff;
        this.context = context;
        this.filePath = filePath;
    }

    @Override
    protected String doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
/*
    private Context context;
    private FragmentAbstract fragment;
    private String filePath;



    @Override
    protected String doInBackground(String... params) {
        return uploadFile();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            if(fragment instanceof FragmentFicheros) {
                TaskUsuario taskUsuario = new TaskUsuario(fragment.getActivity(), fragment);
                taskUsuario.select(BLSession.getInstance().getUsuario(), BLSession.getInstance().getUsuario());
            }
            if(fragment instanceof FragmentTabsDisciplinas) {
                TaskUsuario taskUsuario = new TaskUsuario(fragment.getActivity(), fragment);
                taskUsuario.select(BLSession.getInstance().getUsuario(), BLSession.getInstance().getUsuario());
            }
            if(fragment instanceof FragmentClubes){
                ((FragmentClubes)fragment).recargar();
            }
            if(fragment instanceof FragmentCursos){
                ((FragmentCursos)fragment).recargar();
            }
        }

        File f = null;

        if(BLSession.getInstance().getNombreFichero() != null){
            f = new File(BLSession.getInstance().getNombreFichero());
            if(f.exists()) {
                f.delete();
                BLSession.getInstance().setNombreFichero(null);
            }
        }
        if(BLSession.getInstance().getFotoUri() != null){
            f = new File(BLSession.getInstance().getFotoUri());
            if(f.exists()) {
                f.delete();
                BLSession.getInstance().setFotoUri(null);
            }
        }
        if(BLSession.getInstance().getVideoUri() != null){
            f = new File(BLSession.getInstance().getVideoUri());
            if(f.exists()) {
                f.delete();
                BLSession.getInstance().setVideoUri(null);
            }
        }
    }

    private String uploadFile() {
        String responseString;

        Configuracion configuracion = Utiles.getConfiguracion(context.getApplicationContext());

        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(configuracion.getUrl() + Constants.URL_SERVICIO + Constants.REST_UPLOAD_FICHERO);

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

            File sourceFile = new File(filePath);
            int size = (int) sourceFile.length();
            byte[] bytes = new byte[size];
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(sourceFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();

            // Adding file data to http body
            entityBuilder.addPart("uploadedFile", new ByteArrayBody(bytes, filePath.substring(filePath.lastIndexOf('/')+1)));
            // Extra parameters if you want to pass to server

            //totalSize = entity.getContentLength();
            httppost.setEntity(entityBuilder.build());

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "  + statusCode;
            }

        } catch (IOException e) {
            responseString = e.toString();
        }
        return responseString;
    }
*/
}