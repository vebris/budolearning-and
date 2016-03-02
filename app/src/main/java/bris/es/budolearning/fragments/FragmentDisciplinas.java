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
import bris.es.budolearning.domain.Disciplina;
import bris.es.budolearning.slidingtabs.PagerItem;
import bris.es.budolearning.task.TaskDisciplina;
import bris.es.budolearning.task.TaskGrado;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentDisciplinas extends FragmentAbstract {

    private TaskDisciplina taskDisciplina;
    private FragmentDisciplinas este;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disciplinas, container, false);
        este = this;

        taskDisciplina = new TaskDisciplina(getActivity(), this);

        GridView mDisciplinaView = (GridView) view.findViewById(R.id.disciplinaListView);

        mDisciplinaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {
                BLSession.getInstance().setDisciplina(BLSession.getInstance().getDisciplinas().get(posicion));

                new TaskGrado(getActivity(), este).borrarList();

                int POS = 1;
                while (BLSession.getInstance().getTabsDisciplinas().size() > POS) {
                    BLSession.getInstance().getTabsDisciplinas().remove(POS);
                }
                BLSession.getInstance().getTabsDisciplinas().add(new PagerItem(FragmentGrados.class, BLSession.getInstance().getDisciplina().getNombre()));
                BLSession.getInstance().recargarTabs(POS);

            }
        });

        if(Utiles.esSoloAdmin()) {
            mDisciplinaView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final String[] items = {"Modificar", "Cargar Imagen"};
                    final Disciplina disciplina = BLSession.getInstance().getDisciplinas().get(position);
                    BLSession.getInstance().setDisciplina(disciplina);
                    UtilesDialog.createListDialog(este.getActivity(), "Opciones", items,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    switch (item) {
                                        case 0:
                                            taskDisciplina.select(BLSession.getInstance().getUsuario(), disciplina);
                                            break;
                                        case 1:
                                            Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                                            intent2.setType("image/*");
                                            getActivity().startActivityForResult(Intent.createChooser(intent2, getActivity().getResources().getString(R.string.select_video_mp4)), FragmentAbstract.CHOOSER_FOTO_DISCIPLINA);
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
        if (Utiles.esSoloAdmin()) {
            inflater.inflate(R.menu.menu_recargar_anadir, menu);
        } else {
            inflater.inflate(R.menu.menu_recargar, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.menu_nuevo:
                BLSession.getInstance().setDisciplina(new Disciplina());
                taskDisciplina.select(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina());
                return true;
            case R.id.menu_recargar:
                taskDisciplina.borrarList();
                taskDisciplina.list(BLSession.getInstance().getUsuario(), null, getView().findViewById(R.id.disciplinaListView));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar(){
        taskDisciplina.list(BLSession.getInstance().getUsuario(), null, getView().findViewById(R.id.disciplinaListView));
    }
}
