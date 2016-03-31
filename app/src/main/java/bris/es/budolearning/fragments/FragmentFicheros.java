package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.File;

import bris.es.budolearning.Activity_Logged;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentFicheros extends FragmentAbstract {

    private TaskUtiles taskUtiles;
    private TaskFichero taskFichero;
    private FragmentFicheros este;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ficheros, container, false);
        este = this;

        taskUtiles = new TaskUtiles(getActivity(), this);
        taskFichero = new TaskFichero(getActivity(), this);

        ListView mDisciplinaView = (ListView) view.findViewById(R.id.ficheroListView);
        mDisciplinaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {
                mostrarFichero(posicion);
            }
        });

        if(Utiles.esSoloAdmin()) {
            mDisciplinaView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    String[] items = {"Modificar", "Insertar video", "Insertar pdf"};//,"Especial"};
                    UtilesDialog.createListDialog(este.getActivity(), "Opciones", items,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    BLSession.getInstance().setFichero(BLSession.getInstance().getFicheros().get(position));
                                    switch (item) {
                                        case 0:
                                            taskFichero.select(BLSession.getInstance().getUsuario(), BLSession.getInstance().getFichero());
                                            break;
                                        case 1:
                                            Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                                            intent2.setType("video/mp4");
                                            este.getActivity().startActivityForResult(Intent.createChooser(intent2, este.getActivity().getResources().getString(R.string.select_video_mp4)), FragmentAbstract.CHOOSER_VIDEO);
                                            break;
                                        case 2:
                                            Intent intent3 = new Intent(Intent.ACTION_GET_CONTENT);
                                            intent3.setType("application/pdf");
                                            este.getActivity().startActivityForResult(Intent.createChooser(intent3, este.getActivity().getResources().getString(R.string.select_video_mp4)), FragmentAbstract.CHOOSER_VIDEO);
                                            break;
                                    }
                                }
                            }).show();
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

    public boolean borrarFichero() {
        File file = new File(Utiles.getDirectorioCacheVideo() + "/" + Utiles.md5(String.valueOf(BLSession.getInstance().getFichero().getId())));
        if (file.delete()) {
            recargar();
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (Utiles.esSoloAdmin()) {
            inflater.inflate(R.menu.menu_ficheros_admin, menu);
        } else {
            inflater.inflate(R.menu.menu_ficheros, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.menu_nuevo:
                BLSession.getInstance().setFichero(null);
                taskFichero.select(BLSession.getInstance().getUsuario(), BLSession.getInstance().getFichero());
                return true;
            case R.id.menu_recargar:
                taskFichero.borrarList();
                taskFichero.list(BLSession.getInstance().getUsuario(), BLSession.getInstance().getRecurso(), getView().findViewById(R.id.ficheroListView));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar() {
        taskFichero.list(BLSession.getInstance().getUsuario(), BLSession.getInstance().getRecurso(), getView().findViewById(R.id.ficheroListView));
    }

    private void mostrarFichero(final int posicion) {
        final Fichero fichero = BLSession.getInstance().getFicheros().get(posicion);
        BLSession.getInstance().setFichero(fichero);
        File file = new File(Utiles.getDirectorioCacheVideo() + "/" + Utiles.md5(String.valueOf(fichero.getId())));

        if(fichero.getExtension().equalsIgnoreCase("pdf") && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
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
                                    ((Activity_Logged)este.getActivity()).mostrarPublicidad(este);
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
                taskFichero.downloadFile(BLSession.getInstance().getUsuario(), fichero, null, taskUtiles, fichero.getCoste());
            } else {
                Toast.makeText(getActivity(), "Configuración Conectividad-> Ver/Descargar videos -> SOLO WIFI", Toast.LENGTH_LONG).show();
            }
        }
    }

}
