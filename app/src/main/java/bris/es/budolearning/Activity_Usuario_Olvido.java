package bris.es.budolearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.task.TaskUsuario;
import bris.es.budolearning.utiles.BLSession;

public class Activity_Usuario_Olvido extends AppCompatActivity {

    private Activity_Usuario_Olvido esteActivity;
    private TaskUsuario taskUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskUsuario = new TaskUsuario(this, null);
        esteActivity = this;
        setContentView(R.layout.fragment_usuario_olvido);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Regenerar Contraseña");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                usuario.setMail(((TextView)findViewById(R.id.registrar_usuarioEmail)).getText().toString());
                taskUsuario.cambioContrasena(null, usuario);
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
