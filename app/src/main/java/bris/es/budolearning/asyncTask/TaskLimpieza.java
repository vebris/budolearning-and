package bris.es.budolearning.asyncTask;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import bris.es.budolearning.utiles.Utiles;

public class TaskLimpieza extends TaskAbstract {

    private Activity content;

    public TaskLimpieza(Activity f) {
        super(f);
        this.content = f;
    }

    @Override
    protected String doInBackground(String... arg0) {
        return "true";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        File directorio = Utiles.getDirectorioDescargas();
        try {
            directorio.createNewFile();

            File[] files = directorio.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    Log.d(this.getClass().getName(), "Rename: " + f.getAbsolutePath() + " POR " + Utiles.md5(f.getName().substring(0,f.getName().indexOf("."))));
                    f.renameTo(new File(Utiles.getDirectorioCacheVideo(), Utiles.md5(f.getName().substring(0,f.getName().indexOf(".")))));
                }
            }
        }catch (IOException ioe) {
            Log.e(this.getClass().getName(), "ERROR ", ioe);
        }

        /*
        try {

            Configuracion configuracion = Utiles.getConfiguracion(activity.getApplicationContext());

            File directory = Utiles.getDirectorioDescargas();
            List<File> ficheros = Arrays.asList(directory.listFiles());
            Calendar tiempoVideos = Calendar.getInstance();
            tiempoVideos.setTime(new Date());
            tiempoVideos.add(Calendar.DATE, -configuracion.getTiempoVideos());
            for (File f : ficheros) {
                if (f.getAbsolutePath().endsWith(Constants.EXTENSION_VIDEO_FILE)){
                    if (f.lastModified() < tiempoVideos.getTimeInMillis() && !BLSession.getInstance().getUsuario().getRol().equals("ADMINISTRADOR")) {
                        f.delete();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error al limpiar ficheros", e);
        }
        */
    }
}
