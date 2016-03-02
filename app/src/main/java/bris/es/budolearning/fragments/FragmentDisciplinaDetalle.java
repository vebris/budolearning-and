package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import bris.es.budolearning.R;
import bris.es.budolearning.task.TaskDisciplina;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentDisciplinaDetalle extends FragmentAbstract {

    private TextView disciplinaId;
    private EditText disciplinaDescripcion;
    private EditText disciplinaNombre;

    private TaskDisciplina taskDisciplina;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_disciplina_detalle, container, false);

        taskDisciplina = new TaskDisciplina(getActivity(), this);

        disciplinaId = (TextView) view.findViewById(R.id.disciplinaId);
        disciplinaNombre = (EditText)view.findViewById(R.id.disciplinaNombre);
        disciplinaDescripcion = (EditText)view.findViewById(R.id.disciplinaDescripcion);

        disciplinaId.setText(String.valueOf(BLSession.getInstance().getDisciplina().getId()));
        disciplinaNombre.setText(BLSession.getInstance().getDisciplina().getNombre());
        disciplinaDescripcion.setText(BLSession.getInstance().getDisciplina().getDescripcion());

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registrar_usuarioBtnActivar:
                UtilesDialog.createQuestionYesNo(getActivity(),
                        "BORRAR",
                        "¿ Está seguro de eliminar la disciplina ?",
                        "Confirmar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                taskDisciplina.delete(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina());
                            }
                        },
                        "Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        }
                ).show();

                return true;
            case R.id.registrar_usuarioBtnGuardar:
                BLSession.getInstance().getDisciplina().setId(Integer.parseInt(disciplinaId.getText().toString()));
                BLSession.getInstance().getDisciplina().setNombre(disciplinaNombre.getText().toString());
                BLSession.getInstance().getDisciplina().setDescripcion(disciplinaDescripcion.getText().toString());
                if(BLSession.getInstance().getDisciplina().getId() == 0){
                    taskDisciplina.insert(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina());
                } else {
                    taskDisciplina.update(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_guardar_borrar, menu);
        menu.findItem(R.id.registrar_usuarioBtnActivar).setIcon(android.R.drawable.ic_delete);
        super.onCreateOptionsMenu(menu, inflater);
    }

}