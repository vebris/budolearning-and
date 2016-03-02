package bris.es.budolearning.asyncTask;

import android.annotation.TargetApi;
import android.graphics.pdf.PdfRenderer;
import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;

@Deprecated
public class old_TaskLoadPdf extends TaskAbstract {

    private static final int DEF_HEIGHT = 1024 * 2;
    private static final int DEF_WIDTH = 716 * 2;

    /*
    private String url;
    private Integer idPdf;
    private ImageView pdfView;
    private TextView pdfPage;
    private Button pdfSiguiente;
    private Button pdfAnterior;
    private int page;
    private PdfRenderer.Page mCurrentPage;
    private PdfRenderer mPdfRenderer;
    private File file;
    */

    public old_TaskLoadPdf(FragmentAbstract ff, String url, Integer idPdf, ImageView pdfView, TextView pdfPage, Button anterior, Button siguiente, int page){
        super(ff.getActivity(), true, "Descarga de fichero", "");//BLSession.getInstance().getNombreFichero());
        /*
        this.url = url;
        this.idPdf = idPdf;
        this.pdfView = pdfView;
        this.pdfPage = pdfPage;
        this.pdfSiguiente = siguiente;
        this.pdfAnterior = anterior;
        this.page = page;
        */
    }

    @Override
    protected String doInBackground(String... sUrl) {
        /*
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        file = new File(Utiles.getDirectorioDescargas(), idPdf + Constants.EXTENSION_FOTO_FILE);

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
                file = new File(Utiles.getDirectorioDescargas(), idPdf + Constants.EXTENSION_FOTO_FILE);
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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        /*
        if(result != null) {
            showPage(file);
        }
        */
    }

    @TargetApi(21)
    private void showPage(File file) {
/*
        try {
            mPdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
        }catch(Exception e){
            e.printStackTrace();
        }

        if (mPdfRenderer == null || mPdfRenderer.getPageCount() <= page
                || page < 0) {
            return;
        }
        // For closing the current page before opening another one.
        try {
            if (mCurrentPage != null) {
                mCurrentPage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Open page with specified index
        mCurrentPage = mPdfRenderer.openPage(page);
        Bitmap bitmap = Bitmap.createBitmap(DEF_WIDTH, DEF_HEIGHT, Bitmap.Config.ARGB_8888);

        // Pdf page is rendered on Bitmap
        mCurrentPage.render(bitmap, null, null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // Set rendered bitmap to ImageView
        pdfView.setImageBitmap(bitmap);

        pdfPage.setText(String.format("%d / %d", page + 1, mPdfRenderer.getPageCount()));
        pdfAnterior.setEnabled(0 < page);
        pdfSiguiente.setEnabled(page+1<mPdfRenderer.getPageCount());
*/
    }
}
