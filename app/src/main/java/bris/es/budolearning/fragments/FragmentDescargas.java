package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bris.es.budolearning.Activity_Logged;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Articulo;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.domain.FicherosDescargadosAdapter;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentDescargas extends FragmentAbstract {

    private ListView mFicherosDescargadosView;
    private List<File> ficheros;
    private TaskUtiles taskUtiles;
    private FragmentDescargas este;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        este = this;
        View view = inflater.inflate(R.layout.fragment_descargas, container, false);

        taskUtiles = new TaskUtiles(getActivity(), this);

        mFicherosDescargadosView = (ListView) view.findViewById(R.id.ficherosDescargadosListView);

        refrescarVista();

        mFicherosDescargadosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {
                String[] items = {"Ver", "Borrar"};
                Fichero f = FicherosDescargadosAdapter.buscarFichero(ficheros.get(posicion).getName());
                BLSession.getInstance().setFichero(f);
                String title = "";
                if(f != null) title = f.getDescripcion().toUpperCase();
                if (f == null) {
                    Articulo a = FicherosDescargadosAdapter.buscarArticulo(ficheros.get(posicion).getName());
                    f = new Fichero();
                    f.setId(a.getId());
                    f.setExtension("pdf");
                    BLSession.getInstance().setFichero(f);
                    title = a.getTitulo();
                }
                UtilesDialog.createListDialog(
                        getActivity(),
                        title,
                        items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:
                                        ((Activity_Logged)getActivity()).mostrarPublicidad(este);
                                        break;
                                    case 1:
                                        UtilesDialog.createQuestionYesNo(
                                                getActivity(),
                                                "Borrar fichero",
                                                "¿ Desea borrar el fichero ?",
                                                "Confirmar",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogo1, int id) {
                                                        ficheros.get(posicion).delete();
                                                        refrescarVista();
                                                    }
                                                },
                                                "Cancelar",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogo1, int id) {
                                                    }
                                                }).show();
                                        break;
                                }
                            }
                        }).show();
            }
        });


        ((Activity_Logged) getActivity()).setSubtitle(null);

        return view;
    }

    private void refrescarVista(){
        File directory = Utiles.getDirectorioCacheVideo();
        ficheros = Arrays.asList(directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isHidden();
            }
        }));

        Collections.sort(ficheros, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.lastModified() > rhs.lastModified()) {
                    return -1;
                } else if (lhs.lastModified() < rhs.lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });

        FicherosDescargadosAdapter adapter = new FicherosDescargadosAdapter(ficheros, getActivity());
        mFicherosDescargadosView.setAdapter(adapter);
    }


}
