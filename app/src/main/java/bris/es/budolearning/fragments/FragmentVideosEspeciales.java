package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;

import java.io.File;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.domain.VideoEspecial;
import bris.es.budolearning.task.TaskAbstract;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.task.TaskVideoEspecial;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentVideosEspeciales extends FragmentAbstract {

    private FragmentVideosEspeciales este;
    private TaskVideoEspecial taskVideoEspecial;
    private TaskUtiles taskUtiles;
    private TaskFichero taskFichero;
    private ListView mDisciplinaView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos_especiales, container, false);
        este = this;
        taskVideoEspecial = new TaskVideoEspecial(getActivity(), this);
        taskUtiles = new TaskUtiles(getActivity(), this);
        taskFichero = new TaskFichero(getActivity(), this);

        mDisciplinaView = (ListView) view.findViewById(R.id.ficheroListView);

        /*
        mDisciplinaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {
                mostrarFichero(posicion);
            }
        });
        */

        if(Utiles.esAdmin()) {
            mDisciplinaView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    if(BLSession.getInstance().getVideosEspeciales().get(position).getClub().getId() == BLSession.getInstance().getUsuario().getProfesor().getId()
                            || Utiles.esSoloAdmin()) {
                        String[] items = {"Modificar"};
                        UtilesDialog.createListDialog(este.getActivity(), "Opciones", items,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        BLSession.getInstance().setVideoEspecial(BLSession.getInstance().getVideosEspeciales().get(position));
                                        switch (item) {
                                            case 0:
                                                getActivity().getSupportFragmentManager().beginTransaction()
                                                        .replace(R.id.content_frame, new FragmentVideoEspecialDetalle())
                                                        .addToBackStack(this.getClass().getName()).commit();
                                                break;
                                        }
                                    }
                                }).show();
                    } else {
                        UtilesDialog.createErrorMessage(getActivity(), "ERROR", "No puede modificar este elemento").show();
                    }
                    return true;
                }
            });
        }


        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recargar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        visualizarMenus(menu, false, false, Utiles.esSoloAdmin(), false, false, true, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.btn_menu_nuevo:
                BLSession.getInstance().setVideoEspecial(new VideoEspecial());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentVideoEspecialDetalle())
                        .addToBackStack(this.getClass().getName()).commit();
                return true;
            case R.id.btn_menu_recargar:
                Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
                cache.remove("1:" + taskVideoEspecial.getUrl() + TaskAbstract.LIST);
                taskVideoEspecial.list(BLSession.getInstance().getUsuario(), null, mDisciplinaView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar() {
        taskVideoEspecial.list(BLSession.getInstance().getUsuario(), null, mDisciplinaView);
    }

    private void mostrarFichero(final int posicion) {
        final Fichero fichero = BLSession.getInstance().getVideosEspeciales().get(posicion).getFichero();
        BLSession.getInstance().setFichero(fichero);
        File file = new File(Utiles.getDirectorioCacheVideo() + "/" + Utiles.md5(String.valueOf(fichero.getId())));

        if(fichero.getExtension().equalsIgnoreCase("pdf") && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP){
            UtilesDialog.createErrorMessage(getActivity(),
                    "VERSION ANDROID",
                    "Su versión de Android es inferior a LOLLIPOP (5.0) por lo que no puede descargar o visualizar el archivo."
            ).show();
            if (file.exists()) {
                borrarFichero();
            }
            return;
        }

        if (file.exists()) {
            String[] items = {"Ver", "Borrar"};
            UtilesDialog.createListDialog(getActivity(),
                    fichero.getDescripcion(),
                    items,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int position) {
                            switch (position) {
                                case 0:
                                    taskFichero.downloadFile(BLSession.getInstance().getUsuario(), fichero, null, taskUtiles, 0);
                                    break;
                                case 1:
                                    File file = new File(Utiles.getDirectorioCacheVideo() + "/" + Utiles.md5(String.valueOf(fichero.getId())));
                                    if (file.exists()) {
                                        UtilesDialog.createQuestionYesNo(getActivity(),
                                                "Borrar fichero",
                                                "¿ Desea borrar el fichero ?",
                                                "Confirmar",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogo1, int id) {
                                                        borrarFichero();
                                                    }
                                                }, "Cancelar", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogo1, int id) {
                                                    }
                                                }).show();
                                    }
                                    break;
                            }
                        }
                    }).show();


        } else {
            if (Utiles.permitirDescarga(Utiles.getConfiguracion(getActivity().getApplicationContext()), getActivity().getApplicationContext())) {
                taskFichero.downloadFile(BLSession.getInstance().getUsuario(), fichero, null, taskUtiles, 0);
            } else {
                Toast.makeText(getActivity(), "Configuración Conectividad-> Ver/Descargar videos -> SOLO WIFI", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean borrarFichero() {
        File file = new File(Utiles.getDirectorioCacheVideo() + "/" + Utiles.md5(String.valueOf(BLSession.getInstance().getFichero().getId())));
        if (file.delete()) {
            recargar();
        }
        return false;
    }

}
