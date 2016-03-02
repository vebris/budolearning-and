package bris.es.budolearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bris.es.budolearning.domain.Club;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.task.TaskClub;
import bris.es.budolearning.task.TaskUsuario;


public class Activity_Usuario_Registrar extends AppCompatActivity {

    private TaskUsuario taskUsuario;
    private Activity_Usuario_Registrar esteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        esteActivity = this;

        taskUsuario = new TaskUsuario(this, null);

        setContentView(R.layout.fragment_usuario_registrar);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Nuevo Usuario");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner roles = (Spinner)findViewById(R.id.registrar_usuarioRol);
        List<String> stringList = new ArrayList<>();
        stringList.add("USUARIO");
        stringList.add("PROFESOR");
        stringList.add("ADMINISTRADOR");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roles.setAdapter(dataAdapter);

        Spinner clubes = (Spinner)findViewById(R.id.registrar_usuarioEntrena);

        TaskClub taskClub = new TaskClub(this, null);
        taskClub.list(new Usuario(), null, clubes);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.registrar_usuarioBtnGuardar:
                // app icon in action bar clicked; goto parent activity.
                Usuario usuario = new Usuario();
                usuario.setId(0);
                usuario.setActivo(false);
                usuario.setLogin(((TextView) esteActivity.findViewById(R.id.registrar_usuarioLogin)).getText().toString());
                usuario.setPassword(Utiles.md5(((TextView) esteActivity.findViewById(R.id.registrar_usuarioNombre)).getText().toString()));
                usuario.setRol(((Spinner) esteActivity.findViewById(R.id.registrar_usuarioRol)).getSelectedItem().toString());
                usuario.setNombre(((TextView) esteActivity.findViewById(R.id.registrar_usuarioNombre)).getText().toString());
                usuario.setApellido1(((TextView) esteActivity.findViewById(R.id.registrar_usuarioApellido1)).getText().toString());
                usuario.setApellido2(((TextView) esteActivity.findViewById(R.id.registrar_usuarioApellido2)).getText().toString());
                usuario.setDni(((TextView) esteActivity.findViewById(R.id.registrar_usuarioDNI)).getText().toString());
                usuario.setMail(((TextView) esteActivity.findViewById(R.id.registrar_usuarioEmail)).getText().toString());
                usuario.setDireccion(((TextView) esteActivity.findViewById(R.id.registrar_usuarioDireccion)).getText().toString());
                usuario.setLocalidad(((TextView) esteActivity.findViewById(R.id.registrar_usuarioLocalidad)).getText().toString());
                usuario.setTelefono(((TextView)esteActivity.findViewById(R.id.registrar_usuarioTelefono)).getText().toString());
                Integer idClub = BLSession.getInstance().getClubes().get(((Spinner)esteActivity.findViewById(R.id.registrar_usuarioEntrena)).getSelectedItemPosition()).getId();
                Club club = new Club();
                club.setId(idClub);
                usuario.setEntrena(club);
                taskUsuario.insert(new Usuario(), usuario);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guardar, menu);
        return true;
    }
}
