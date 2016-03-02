package bris.es.budolearning.asyncTask;

import android.app.Activity;
import android.widget.ImageView;
import bris.es.budolearning.utiles.BLSession;
/*
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
*/

@Deprecated
public class old_TaskLoadImage extends TaskAbstract {

    /*
    private String url;
    private Integer idFoto;
    private ImageView imageView;
    private Dialog dialog;
    private Bitmap imagen;
    private File file;
    */

    public old_TaskLoadImage(Activity ff, String url, Integer idFoto, ImageView imageView){
        super(ff, true, "Descarga de fichero", "");//BLSession.getInstance().getNombreFichero());
        /*
        this.url = url;
        this.idFoto = idFoto;
        this.imageView = imageView;
        */
    }

    @Override
    protected void onPreExecute() {
        // Para no lanzar el esperando...
    }

    @Override
    protected String doInBackground(String... sUrl) {
        /*
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        file = new File(Utiles.getDirectorioDescargas(), idFoto + Constants.EXTENSION_FOTO_FILE);

        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                URL url = new URL(this.url);
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
                output = new FileOutputStream(file);

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
            }
            imagen = BitmapFactory.decodeFile(Utiles.getDirectorioDescargas() + "/" + idFoto + Constants.EXTENSION_FOTO_FILE);
            return Boolean.TRUE.toString();
        } catch (Exception e) {
            return Boolean.FALSE.toString();
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
        return null;
    }

    @Override
    protected void onPostExecute(final String result) {
        /*
        if(Boolean.TRUE.toString().equalsIgnoreCase(result)) {
            imageView.setImageBitmap(imagen);
            if(dialog != null) {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        } else {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            Bitmap bmp = Bitmap.createBitmap(1, 1, conf);
            imageView.setImageBitmap(bmp);
            if(dialog != null) { dialog.dismiss(); }
        }
        */
    }
}
