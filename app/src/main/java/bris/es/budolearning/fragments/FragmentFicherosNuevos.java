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

public class FragmentFicherosNuevos extends FragmentAbstract {

    private TaskUtiles taskUtiles;
    private TaskFichero taskFichero;
    private FragmentFicherosNuevos este;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ficheros, container, false);
        este = this;

        taskUtiles = new TaskUtiles(getActivity(), this);
        taskFichero = new TaskFichero(getActivity(), this);

        ListView mDisciplinaView = (ListView) view.findViewById(R.id.ficheroListView);

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
        inflater.inflate(R.menu.menu, menu);
        visualizarMenus(menu, false, false, false, false, false, true, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.btn_menu_recargar:
                recargar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar() {
        taskFichero.listSinRecursos(BLSession.getInstance().getUsuario(),null, getView().findViewById(R.id.ficheroListView));
    }

}
