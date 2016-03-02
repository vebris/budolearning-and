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
import android.widget.GridView;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Grado;
import bris.es.budolearning.slidingtabs.PagerItem;
import bris.es.budolearning.task.TaskGrado;
import bris.es.budolearning.task.TaskRecurso;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.task.TaskUsuario;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentGrados extends FragmentAbstract {

    private TaskGrado taskGrado;
    private FragmentGrados este;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grados, container, false);
        este = this;
        taskGrado = new TaskGrado(getActivity(), this);

        GridView mGradoView = (GridView) view.findViewById(R.id.gradoListView);

        mGradoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {
                BLSession.getInstance().setGrado(BLSession.getInstance().getGrados().get(posicion));

                new TaskRecurso(getActivity(), este).borrarList();

                int POS = 2;
                while(BLSession.getInstance().getTabsDisciplinas().size()>POS){
                    BLSession.getInstance().getTabsDisciplinas().remove(POS);
                }
                BLSession.getInstance().getTabsDisciplinas().add(new PagerItem(FragmentRecursos.class, BLSession.getInstance().getGrado().getNombre()));
                BLSession.getInstance().recargarTabs(POS);

            }
        });

        if(Utiles.esSoloAdmin()) {
            mGradoView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final String[] items = {"Cargar Imagen"};
                    final Grado grado = BLSession.getInstance().getGrados().get(position);
                    BLSession.getInstance().setGrado(grado);
                    UtilesDialog.createListDialog(este.getActivity(), "Opciones", items,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    switch (item) {
                                        case 0:
                                            Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                                            intent2.setType("image/*");
                                            getActivity().startActivityForResult(Intent.createChooser(intent2, getActivity().getResources().getString(R.string.select_video_mp4)), FragmentAbstract.CHOOSER_FOTO_GRADO);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recargar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.menu_recargar:
                taskGrado.borrarList();
                taskGrado.list(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina(), getView().findViewById(R.id.gradoListView));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar(){
        taskGrado.list(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina(), getView().findViewById(R.id.gradoListView));
    }
}
