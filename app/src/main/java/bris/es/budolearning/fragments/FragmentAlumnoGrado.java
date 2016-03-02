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
import android.widget.TextView;
import java.util.ArrayList;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Disciplina;
import bris.es.budolearning.domain.Grado;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.domain.UsuarioDisciplinaGrado;
import bris.es.budolearning.domain.UsuarioDisciplinaGradoAdapter;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;
import bris.es.budolearning.task.TaskUsuario;

public class FragmentAlumnoGrado extends FragmentAbstract {

    public FragmentAlumnoGrado esteFragmento;
    private ListView mAlumnoGradoView;
    private TaskUsuario taskUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alumno_grado, container, false);
        taskUsuario = new TaskUsuario(getActivity(), this);
        esteFragmento = this;
        mAlumnoGradoView = (ListView) view.findViewById(R.id.alumnosGradoListView);

        final Usuario usuario = BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno());
        final Usuario profesor = BLSession.getInstance().getUsuario();

        profesor.setDisciplinas(BLSession.getInstance().getDisciplinas());

        final String nombreAlumno = usuario.getNombre() + " " +
                usuario.getApellido1() + " " +
                usuario.getApellido2();
        ((TextView) view.findViewById(R.id.alumnosGrado_nombre)).setText(nombreAlumno);

        if(usuario.getId() != profesor.getId() || Utiles.esSoloAdmin()) {
            mAlumnoGradoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {
                    UtilesDialog.createQuestionYesNo(esteFragmento.getActivity(),
                            "Subir Grado",
                            " ¿ Desea subir de grado al Alumno " + nombreAlumno + " en la disciplina " + BLSession.getInstance().getUsuarioDisciplinaGrados().get(posicion).getDescDisciplina().toUpperCase() + " ?",
                            "Confirmar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    Disciplina disciplina = new Disciplina();
                                    disciplina.setId(BLSession.getInstance().getUsuarioDisciplinaGrados().get(posicion).getIdDisciplina());
                                    Grado grado = new Grado();
                                    grado.setId(BLSession.getInstance().getUsuarioDisciplinaGrados().get(posicion).getIdGrado());

                                    taskUsuario.subirGrado(
                                            profesor,
                                            usuario,
                                            disciplina,
                                            grado);
                                }
                            },
                            "Cancelar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                }
                            }).show();

                }
            });
        }
        refrescar(usuario, profesor);

        setHasOptionsMenu(true);

        return view;
    }

    public void refrescar(Usuario usuario, Usuario profesor) {
        BLSession.getInstance().setUsuarioDisciplinaGrados(new ArrayList<UsuarioDisciplinaGrado>());
        int max = 0;//"ADMINISTRADOR".equalsIgnoreCase(usuario.getRol())?3:"PROFESOR".equalsIgnoreCase(usuario.getRol())?2:1;
        for (Disciplina dp : profesor.getDisciplinas()) {
            boolean insertada = false;
            for (Disciplina d : usuario.getDisciplinas()) {
                if (d.getId() == dp.getId()) {
                    UsuarioDisciplinaGrado udg = new UsuarioDisciplinaGrado();
                    udg.setIdDisciplina(dp.getId());
                    udg.setDescDisciplina(dp.getNombre());
                    if (d.getGrados() != null && d.getGrados().size() > max) {
                        udg.setIdGrado(d.getGrados().get(max).getId());
                        udg.setDescGrado(d.getGrados().get(max).getNombre());
                    } else {
                        udg.setIdGrado(1);
                        udg.setDescGrado("BLANCO");
                    }
                    BLSession.getInstance().getUsuarioDisciplinaGrados().add(udg);
                    insertada = true;
                    break;
                }
            }
            if (!insertada) {
                UsuarioDisciplinaGrado udg = new UsuarioDisciplinaGrado();
                udg.setIdDisciplina(dp.getId());
                udg.setDescDisciplina(dp.getDescripcion());
                udg.setIdGrado(1);
                udg.setDescGrado("BLANCO");
                BLSession.getInstance().getUsuarioDisciplinaGrados().add(udg);
            }
        }

        UsuarioDisciplinaGradoAdapter adapter = new UsuarioDisciplinaGradoAdapter(
                BLSession.getInstance().getUsuarioDisciplinaGrados()
                , getActivity());
        mAlumnoGradoView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_atras, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}