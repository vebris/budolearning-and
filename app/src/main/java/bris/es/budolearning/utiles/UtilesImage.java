package bris.es.budolearning.utiles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UtilesImage {

    public static void transformImage(String original, String transformada) {
        Bitmap b = BitmapFactory.decodeFile(original);
        Bitmap out = Bitmap.createScaledBitmap(b, 320, 480, false);

        File file = new File(transformada);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {
        }
    }

    public static void copyFile(String origen, String destino) throws IOException{
        File fOriginal = new File(origen);
        File fFinal = new File(destino);

        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(fOriginal);
            output = new FileOutputStream(fFinal);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}
