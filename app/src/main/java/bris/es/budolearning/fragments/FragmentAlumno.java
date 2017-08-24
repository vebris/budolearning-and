package bris.es.budolearning.fragments;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.task.TaskClub;
import bris.es.budolearning.task.TaskUsuario;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentAlumno extends FragmentAbstract {

    private TaskUsuario taskUsuario;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_usuario, container, false);

        taskUsuario = new TaskUsuario(getActivity(), this);

        final Usuario usuario =
                BLSession.getInstance().getAlumnos() != null && BLSession.getInstance().getAlumnos().size() > 0 && BLSession.getInstance().getPosicionAlumno() > -1?
                        BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno()
                        )
                        :
                        BLSession.getInstance().getUsuario();

        ((TextView) view.findViewById(R.id.usuarioNombre)).setText(usuario.getNombre());
        ((TextView) view.findViewById(R.id.usuarioApellido1)).setText(usuario.getApellido1());
        ((TextView) view.findViewById(R.id.usuarioApellido2)).setText(usuario.getApellido2());
        ((TextView) view.findViewById(R.id.usuarioDNI)).setText(usuario.getDni());
        ((TextView) view.findViewById(R.id.usuarioEmail)).setText(usuario.getMail());

        ((TextView) view.findViewById(R.id.usuarioDireccion)).setText(usuario.getDireccion());
        ((TextView) view.findViewById(R.id.usuarioLocalidad)).setText(usuario.getLocalidad());
        ((TextView) view.findViewById(R.id.usuarioTelefono)).setText(usuario.getTelefono());

        ((CheckBox) view.findViewById(R.id.usuario_verPDF)).setChecked(usuario.getVerPDF());

        TaskClub taskClub = new TaskClub(getActivity(), this);
        taskClub.setNombreClub(usuario.getEntrena().getNombre());
        taskClub.list(BLSession.getInstance().getUsuario(), null, view.findViewById(R.id.usuarioEntrena));

        String[] items = new String[] {"ADMINISTRADOR","PROFESOR","USER"};
        if (!Utiles.esSoloAdmin()){
            items = new String[] {"PROFESOR","USER"};
        }
        final Spinner rol = ((Spinner) view.findViewById(R.id.usuarioRol));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rol.setAdapter(adapter);
        for(int i=0;i<items.length;i++){
            if(items[i].equalsIgnoreCase(usuario.getRol())){
                rol.setSelection(i+1);
                break;
            }
        }

        rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (((AppCompatTextView) selectedItemView).getText().equals("USER")) {
                        view.findViewById(R.id.usuarioProfesor).setVisibility(View.INVISIBLE);
                    } else {
                        view.findViewById(R.id.usuarioProfesor).setVisibility(View.VISIBLE);
                    }
                } catch(Exception e){
                    Log.d(this.getClass().getCanonicalName(), "ROL.onItemSelected =>" + e.getLocalizedMessage(), e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        TaskClub taskClub2 = new TaskClub(getActivity(), this);
        taskClub2.setNombreClub(usuario.getProfesor().getNombre());
        taskClub2.list(BLSession.getInstance().getUsuario(), null, view.findViewById(R.id.usuarioProfesor));

        if (!Utiles.esAdmin()) {
            rol.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.usuarioProfesor).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.usuario_verPDF).setVisibility(View.INVISIBLE);
        }

        setHasOptionsMenu(true);
        return view;
    }

    public void guardarUsuario(){
        Usuario usuario = new Usuario(BLSession.getInstance().getUsuario());
        if(BLSession.getInstance().getAlumnos() != null) {
            usuario = BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno());
        }
        //usuario.setId(Integer.parseInt(((TextView) view.findViewById(R.id.usuarioId)).getText().toString()));
        usuario.setNombre(((TextView) view.findViewById(R.id.usuarioNombre)).getText().toString());
        usuario.setApellido1(((TextView) view.findViewById(R.id.usuarioApellido1)).getText().toString());
        usuario.setApellido2(((TextView) view.findViewById(R.id.usuarioApellido2)).getText().toString());
        usuario.setDni(((TextView) view.findViewById(R.id.usuarioDNI)).getText().toString());
        usuario.setMail(((TextView) view.findViewById(R.id.usuarioEmail)).getText().toString());
        usuario.setDireccion(((TextView) view.findViewById(R.id.usuarioDireccion)).getText().toString());
        usuario.setLocalidad(((TextView) view.findViewById(R.id.usuarioLocalidad)).getText().toString());
        usuario.setTelefono(((TextView) view.findViewById(R.id.usuarioTelefono)).getText().toString());
        usuario.setVerPDF(((CheckBox) view.findViewById(R.id.usuario_verPDF)).isChecked());
        try {
            usuario.setEntrena(BLSession.getInstance().getClubes().get(((Spinner) view.findViewById(R.id.usuarioEntrena)).getSelectedItemPosition() - 1));
        } catch(Exception err){
            UtilesDialog.createErrorMessage(getActivity(),"Error", "Se tiene que seleccionar un club de entrenamiento.");
            return;
        }

        if(Utiles.esAdmin()) {
            if(((Spinner) view.findViewById(R.id.usuarioProfesor)).getSelectedItemPosition() > 0) {
                usuario.setProfesor(BLSession.getInstance().getClubes().get(((Spinner) view.findViewById(R.id.usuarioProfesor)).getSelectedItemPosition() -1));
            }
            if(((Spinner) view.findViewById(R.id.usuarioRol)).getSelectedItemPosition() > 0) {
                usuario.setRol(((Spinner) view.findViewById(R.id.usuarioRol)).getSelectedItem().toString());
            }

            if(usuario.getRol().equalsIgnoreCase("ADMINISTRADOR")) usuario.setProfesor(null);

            if(view.findViewById(R.id.usuarioProfesor).getVisibility() == View.INVISIBLE){
                usuario.setProfesor(null);
            }

        } else {
            usuario.setProfesor(null);
            usuario.setRol("USER");
        }

        taskUsuario.update(BLSession.getInstance().getUsuario(), usuario);
    }

    public void activarUsuario(){
        Usuario usuario = new Usuario(BLSession.getInstance().getUsuario());
        if(BLSession.getInstance().getAlumnos() != null) {
            usuario = BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno());
        }
        //usuario.setId(Integer.parseInt(((TextView) view.findViewById(R.id.usuarioId)).getText().toString()));

        usuario.setActivo(!usuario.getActivo());

        taskUsuario.activarUsuario(BLSession.getInstance().getUsuario(), usuario);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        //menu_usuario_modificar
        if(
          BLSession.getInstance().getAlumnos() == null || BLSession.getInstance().getAlumnos().size() == 0 || BLSession.getInstance().getPosicionAlumno() == -1
        ) {
            visualizarMenus(menu, false, false, false, false, true, false, false);
        } else {

            Usuario usuario = BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno());

            if(usuario.getActivo()){
                visualizarMenus(menu, false, false, false, true, true, false, false);
                menu.findItem(R.id.btn_menu_activar).setIcon(android.R.drawable.ic_delete);
            } else {
                menu.findItem(R.id.btn_menu_activar).setIcon(android.R.drawable.ic_input_add);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.btn_menu_activar:
                activarUsuario();
                return true;
            case R.id.btn_menu_guardar:
                guardarUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}