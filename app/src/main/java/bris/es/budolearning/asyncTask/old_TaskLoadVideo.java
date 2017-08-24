package bris.es.budolearning.asyncTask;

import android.content.Context;
import bris.es.budolearning.fragments.FragmentFicheros;
import bris.es.budolearning.utiles.BLSession;

/*
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.ListView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.adapter.FicheroAdapter;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
*/

@Deprecated
public class old_TaskLoadVideo extends TaskAbstract {

    private Context context;
    private FragmentFicheros fragmentFichero;
    private boolean verTrasDescarga;

    public old_TaskLoadVideo(FragmentFicheros ff, Context context, boolean verTrasDescarga) {
        super(ff.getActivity(), true, "Descarga de fichero", "");//BLSession.getInstance().getNombreFichero());
        this.fragmentFichero = ff;
        this.context = context;
        this.verTrasDescarga = verTrasDescarga;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        /*
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(sUrl[1]);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) total, fileLength);
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        */
        return Boolean.TRUE.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        /*
        if (Boolean.TRUE.toString().equalsIgnoreCase(result)) {
            ListView list=(ListView)fragmentFichero.getView().findViewById(R.id.ficheroListView);
            FicheroAdapter adapter = new FicheroAdapter(
                    BLSession.getInstance().getUsuario().getDisciplina(BLSession.getInstance().getIdDisciplina()).getGrado(BLSession.getInstance().getIdGrado()).getRecurso(BLSession.getInstance().getIdRecurso()).getFicheros(),
                    activity);
            list.setAdapter(adapter);
        }

        if (verTrasDescarga){
            Intent intent = new Intent(Intent.ACTION_VIEW );
            String url = Utiles.getDirectorioDescargas() +"/"+ Utiles.md5(String.valueOf(BLSession.getInstance().getIdFichero()));
            intent.setDataAndType(Uri.parse(url), "video/*");
            context.startActivity(intent);
        }
        */
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        /*
        File file = new File(Utiles.getDirectorioDescargas() +"/"+ Utiles.md5(String.valueOf(BLSession.getInstance().getIdFichero())));
        file.delete();
        */
    }
}