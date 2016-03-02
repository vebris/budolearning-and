package bris.es.budolearning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.task.TaskUsuario;

public class FragmentPassword extends FragmentAbstract {

    private View vista;
    private TaskUsuario task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_password, container, false);

        task = new TaskUsuario(getActivity(), this);

        ((TextView) vista.findViewById(R.id.password_login)).setText(BLSession.getInstance().getUsuario().getLogin());

        Button btnGuardar = (Button) vista.findViewById(R.id.btn_cambiarPassword);
        btnGuardar.setVisibility(View.INVISIBLE);

        btnGuardar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarPass();
            }
        });

        setHasOptionsMenu(true);
        return vista;
    }

    private void cambiarPass(){
        int id = BLSession.getInstance().getIdUser();
        String login = ((TextView) vista.findViewById(R.id.password_login)).getText().toString();

        EditText mOldPass = (EditText) vista.findViewById(R.id.password_old);
        EditText newPass = (EditText) vista.findViewById(R.id.password_new);
        EditText newPass2 = (EditText) vista.findViewById(R.id.password_new2);

        Usuario usuario = BLSession.getInstance().getUsuario();

        if(!newPass.getText().toString().equalsIgnoreCase(newPass2.getText().toString())) {
            newPass2.setError(getActivity().getString(R.string.contrasenas_deben_ser_iguales));
            newPass.setText("");
            newPass2.setText("");
        } else {
            if(Utiles.md5(mOldPass.getText().toString()).equalsIgnoreCase(usuario.getPassword())){
                usuario.setPassword(Utiles.md5(newPass.getText().toString()));
                task.update(BLSession.getInstance().getUsuario(), usuario);
            } else {
                Toast.makeText(getActivity(), "Error al introducir la contraseña", Toast.LENGTH_SHORT).show();
                mOldPass.setText("");
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_usuario_cambiar_pass, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.registrar_usuarioBtnGuardar:
                cambiarPass();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
