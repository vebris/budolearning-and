package bris.es.budolearning;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;
import bris.es.budolearning.widget.ZoomableImageView;

public class Activity_Pdf_View extends AppCompatActivity  {

    private ZoomableImageView pdfView;
    private TextView pdfPage;
    private Button pdfPrevius;
    private Button pdfNext;
    private int currentPage = 0;
    private PdfRenderer.Page mCurrentPage;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.fragment_pdf_viewer);

        if(savedInstanceState != null) {
            currentPage = savedInstanceState.getInt("page");
        }

        pdfView = (ZoomableImageView) findViewById(R.id.pdfView);
        pdfPage = (TextView) findViewById(R.id.pdf_page);
        pdfPrevius = (Button) findViewById(R.id.pdf_previous);
        pdfNext = (Button) findViewById(R.id.pdf_next);



        pdfPrevius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage--;
                showPage();
            }
        });

        pdfNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage++;
                showPage();
            }
        });

        file = new File(Utiles.getDirectorioCacheVideo(), Utiles.md5(String.valueOf(BLSession.getInstance().getFichero().getId())));

        showPage();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", currentPage);
    }

    public TextView getPdfPage() {
        return pdfPage;
    }
    public Button getPdfPrevius() {
        return pdfPrevius;
    }
    public Button getPdfNext() {
        return pdfNext;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public ZoomableImageView getPdfView() {
        return pdfView;
    }
    public PdfRenderer.Page getmCurrentPage() {
        return mCurrentPage;
    }
    public void setmCurrentPage(PdfRenderer.Page mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    private static final int DEF_HEIGHT = 1024 * 2;
    private static final int DEF_WIDTH = 716 * 2;
    @TargetApi(21)
    private void showPage() {
        try {

            PdfRenderer mPdfRenderer;
            try {
                mPdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
            } catch (Exception e) {
                Log.e(getClass().getCanonicalName(), e.getLocalizedMessage());
                return;
            }
            int page = getCurrentPage();
            if (mPdfRenderer == null || mPdfRenderer.getPageCount() <= page || page < 0) {
                return;
            }

            // For closing the current page before opening another one.
            try {
                if (getmCurrentPage()!= null) {
                    getmCurrentPage().close();
                }
            } catch (Exception e) {
                Log.e(getClass().getCanonicalName(), e.getLocalizedMessage());
            }
            // Open page with specified index
            setmCurrentPage(mPdfRenderer.openPage(page));
            Bitmap bitmap = Bitmap.createBitmap(DEF_WIDTH, DEF_HEIGHT, Bitmap.Config.ARGB_8888);

            // Pdf page is rendered on Bitmap
            getmCurrentPage().render(bitmap, null, null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            // Set rendered bitmap to ImageView
            getPdfView().setImageBitmap(bitmap);

            getPdfPage().setText(String.format("%d / %d", page + 1, mPdfRenderer.getPageCount()));
            getPdfPrevius().setEnabled(0 < page);
            getPdfNext().setEnabled(page + 1 < mPdfRenderer.getPageCount());

            //file.delete();
        }catch(Exception e){
            UtilesDialog.createErrorMessage(this, "No se ha podido visualizar el archivo", e.getMessage());
        }
    }


}
