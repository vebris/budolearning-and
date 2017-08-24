package bris.es.budolearning;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import bris.es.budolearning.domain.Club;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.task.TaskClub;
import bris.es.budolearning.task.TaskUsuario;
import bris.es.budolearning.utiles.UtilesDialog;
import fr.ganfra.materialspinner.MaterialSpinner;


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

        Spinner clubes = (Spinner)findViewById(R.id.registrar_usuarioEntrena);

        TaskClub taskClub = new TaskClub(this, null);
        taskClub.list(new Usuario(), null, clubes);

        Button mLogin = (Button) findViewById(R.id.btn_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });

        obligarorio(R.id.registrar_usuarioPassword, R.id.registrar_usuarioPassword_wrapper);
        obligarorio(R.id.registrar_usuarioNombre, R.id.registrar_usuarioNombre_wrapper);
        obligarorio(R.id.registrar_usuarioApellido1, R.id.registrar_usuarioApellido1_wrapper);
        obligarorio(R.id.registrar_usuarioEmail,R.id.registrar_usuarioEmail_wrapper);
        obligarorio(R.id.registrar_usuarioLogin, R.id.registrar_usuarioLogin_wrapper);
    }

    private void obligarorio (final int vista, final int parent){
        final EditText view = (EditText) findViewById(vista);
        ponerAsterisco(vista,parent);

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(view.getText().toString().trim().length()==0){
                    setStyleLine(v, Utiles.getColor(esteActivity,R.color.tertiary_light));
                    ((TextInputLayout)findViewById(parent)).setError("Obligatorio");
                } else {
                    setStyleLine(v, Utiles.getColor(esteActivity,R.color.tertiary_dark));
                    ((TextInputLayout)findViewById(parent)).setError(null);
                }
            }
        });
        //view.requestFocus();
    }

    private void setStyleLine(View view, int color) {
        Drawable wrappedDrawable = view.getBackground();
        DrawableCompat.setTint(wrappedDrawable, color);
        view.setBackground(wrappedDrawable);
    }

    private void ponerAsterisco(final int vista, final int parent){
        String hint = ((TextInputLayout)findViewById(parent)).getHint().toString();
        String asterisk = " *";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(hint);
        int start = builder.length();
        builder.append(asterisk);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextInputLayout)findViewById(parent)).setHint(builder);
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


    private void registrarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setId(0);
        usuario.setActivo(false);
        usuario.setLogin(((TextView) esteActivity.findViewById(R.id.registrar_usuarioLogin)).getText().toString());
        usuario.setPassword(Utiles.md5(((TextView) esteActivity.findViewById(R.id.registrar_usuarioNombre)).getText().toString()));
        usuario.setRol("USER");
        usuario.setNombre(((TextView) esteActivity.findViewById(R.id.registrar_usuarioNombre)).getText().toString());
        usuario.setApellido1(((TextView) esteActivity.findViewById(R.id.registrar_usuarioApellido1)).getText().toString());
        usuario.setApellido2(((TextView) esteActivity.findViewById(R.id.registrar_usuarioApellido2)).getText().toString());
        usuario.setDni(((TextView) esteActivity.findViewById(R.id.registrar_usuarioDNI)).getText().toString());
        usuario.setMail(((TextView) esteActivity.findViewById(R.id.registrar_usuarioEmail)).getText().toString());
        usuario.setDireccion(((TextView) esteActivity.findViewById(R.id.registrar_usuarioDireccion)).getText().toString());
        usuario.setLocalidad(((TextView) esteActivity.findViewById(R.id.registrar_usuarioLocalidad)).getText().toString());
        usuario.setTelefono(((TextView)esteActivity.findViewById(R.id.registrar_usuarioTelefono)).getText().toString());
        int seleccionado = ((MaterialSpinner)esteActivity.findViewById(R.id.registrar_usuarioEntrena)).getSelectedItemPosition();
        if(seleccionado>0) {
            Integer idClub = BLSession.getInstance().getClubes().get(seleccionado-1).getId();
            Club club = new Club();
            club.setId(idClub);
            usuario.setEntrena(club);
        }

        if(usuario.getLogin().isEmpty()
                || usuario.getPassword().isEmpty()
                || usuario.getNombre().isEmpty()
                || usuario.getApellido1().isEmpty()
                || usuario.getMail().isEmpty()
                ) {


            esteActivity.findViewById(R.id.registrar_usuarioNombre).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioNombre).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioApellido1).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioApellido2).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioDNI).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioEmail).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioDireccion).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioLocalidad).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioTelefono).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioEntrena).requestFocus();
            esteActivity.findViewById(R.id.registrar_usuarioLogin).requestFocus();

            UtilesDialog.createErrorMessage(esteActivity, "ERROR", "Debe rellenar todos los campos obligatorios").show();
        } else {
            taskUsuario.insert(new Usuario(), usuario);
        }
    }
}
