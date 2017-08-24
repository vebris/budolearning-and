package bris.es.budolearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.task.TaskUsuario;
import bris.es.budolearning.utiles.Utiles;

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
        setTitle("¡Olvidó su  Contraseña!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button mLogin = (Button) findViewById(R.id.btn_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = new Usuario();
                usuario.setMail(((TextView)findViewById(R.id.registrar_usuarioEmail)).getText().toString());
                taskUsuario.cambioContrasena(null, usuario);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.btn_menu_nuevo).setVisible(false);
        menu.findItem(R.id.btn_menu_activar).setVisible(false);
        menu.findItem(R.id.btn_menu_guardar).setVisible(false);
        menu.findItem(R.id.btn_menu_recargar).setVisible(false);
        menu.findItem(R.id.btn_menu_atras).setVisible(false);
        this.invalidateOptionsMenu();
        return true;
    }

}
