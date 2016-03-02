package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Tipo;
import bris.es.budolearning.task.TaskRecurso;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentRecursoDetalle extends FragmentAbstract {

    private TextView recursoId;
    private EditText recursoNombre;
    private Spinner recursoTipo;
    private CheckBox recursoEnPrograma;

    private TaskRecurso taskRecurso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recurso_detalle, container, false);

        taskRecurso = new TaskRecurso(getActivity(), this);

        recursoId = (TextView) view.findViewById(R.id.recursoId);
        recursoNombre = (EditText)view.findViewById(R.id.recursoNombre);
        recursoTipo = (Spinner)view.findViewById(R.id.recursoTipo);
        recursoEnPrograma = (CheckBox)view.findViewById(R.id.recursoEnPrograma);

        recursoId.setText(String.valueOf(BLSession.getInstance().getRecurso().getId()));
        recursoNombre.setText(BLSession.getInstance().getRecurso().getNombre());
        List<String> txtTipos = new ArrayList<>();
        txtTipos.add("HOJOUNDO");
        txtTipos.add("KATA");
        txtTipos.add("KUMI HOJOUNDO");
        txtTipos.add("KUMIWAZA");
        txtTipos.add("KATA OYO");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, txtTipos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recursoTipo.setAdapter(dataAdapter);
        if(BLSession.getInstance().getRecurso().getTipo() != null) {
            recursoTipo.setSelection(txtTipos.indexOf(BLSession.getInstance().getRecurso().getTipo().getNombre()));
        }

        recursoEnPrograma.setChecked(BLSession.getInstance().getRecurso().isEnPrograma());

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registrar_usuarioBtnActivar:
                UtilesDialog.createQuestionYesNo(getActivity(),
                        "BORRAR",
                        "¿ Está seguro de eliminar el Recurso ?",
                        "Confirmar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                taskRecurso.delete(BLSession.getInstance().getUsuario(), BLSession.getInstance().getRecurso());
                            }
                        },
                        "Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        }
                ).show();

                return true;
            case R.id.registrar_usuarioBtnGuardar:
                BLSession.getInstance().getRecurso().setId(Integer.parseInt(recursoId.getText().toString()));
                BLSession.getInstance().getRecurso().setNombre(recursoNombre.getText().toString());
                BLSession.getInstance().getRecurso().setTipo(new Tipo(recursoTipo.getSelectedItem().toString()));
                BLSession.getInstance().getRecurso().setEnPrograma(recursoEnPrograma.isChecked());

                if(BLSession.getInstance().getRecurso().getId() == 0){
                    taskRecurso.insert(BLSession.getInstance().getUsuario(), BLSession.getInstance().getRecurso());
                } else {
                    taskRecurso.update(BLSession.getInstance().getUsuario(), BLSession.getInstance().getRecurso());
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