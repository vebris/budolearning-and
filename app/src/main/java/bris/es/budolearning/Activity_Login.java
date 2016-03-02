package bris.es.budolearning;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.task.TaskUsuario;
import bris.es.budolearning.task.TaskUtiles;

public class Activity_Login extends AppCompatActivity {

    public TaskUsuario taskUsuario;
    public TaskUtiles taskUtiles;
    public boolean pedidoVersion = false;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_login);

        taskUsuario = new TaskUsuario(this, null);
        taskUtiles = new TaskUtiles(this, null);


       // Set up the login form.
       mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

       mPasswordView = (EditText) findViewById(R.id.password);
       mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
               if (id == R.id.btn_login || id == EditorInfo.IME_NULL) {
                   //mostrarPublicidad();
                   return true;
               }
               return false;
           }
       });

       Button mRegistrarUsuario = (Button) findViewById(R.id.btn_registrarUsuario);
       mRegistrarUsuario.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(getApplicationContext(), Activity_Usuario_Registrar.class);
               startActivity(i);
           }
       });

        Button mOlvidoUsuario = (Button) findViewById(R.id.btn_olvidoUsuario);
        mOlvidoUsuario.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Activity_Usuario_Olvido.class);
                startActivity(i);
            }
        });

       if(Utiles.getConfiguracion(getApplicationContext()).isRecordarUsuario()){
          String[] elementos = leerUserYPass();
           if(elementos != null) {
               mEmailView.setText(elementos[0]);
               mPasswordView.setText(elementos[1]);
           }
       }

        try {
            if (savedInstanceState == null) {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                taskUtiles.checkVersion(packageInfo.versionCode);
            } else {
                pedidoVersion = savedInstanceState.getBoolean("pedidoVersion");
            }
        }catch (PackageManager.NameNotFoundException nnfe){
            Log.e(this.getClass().getCanonicalName(), "Error al comprobar version", nnfe);
        }

        Button mLogin = (Button) findViewById(R.id.btn_login);
        mLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                //mostrarPublicidad();
            }
        });

        //cargarPublicidad();

    }

    /**
    * Attempts to sign in or register the account specified by the login form.
    * If there are form errors (invalid email, missing fields, etc.), the
    * errors are presented and no actual login attempt is made.
    */
    public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            taskUsuario.login(mEmailView.getText().toString(), Utiles.md5(mPasswordView.getText().toString()), mPasswordView.getText().toString());
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    public void guardarUserYPass(String user, String pass){
        try {
            /* Guardar usuario y pass en ficheros del sistema. */
            File directory = getApplicationContext().getFilesDir();
            if (!directory.exists()) {
                directory.mkdir();
            }
            File fichero = new File(directory, "user.pass");
            if (!fichero.exists()) {
                fichero.createNewFile();
            }

            FileWriter fw = new FileWriter(fichero);
            fw.write(Utiles.encrypt(user));
            fw.write("\n");
            fw.write(Utiles.encrypt(pass));
            fw.close();
        } catch (IOException e){
            Log.e(this.getClass().getCanonicalName(), "Error al guardarUserYPass", e);
        }
    }

    private String[] leerUserYPass(){
        String[] elements = null;
        try {
            // Leer usuario y pass en ficheros del sistema.
            File fichero = new File(getApplicationContext().getFilesDir(), "user.pass");
            if (fichero.exists()) {
                elements = new String[2];
                BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(fichero)));
                elements[0] = Utiles.decrypt(fin.readLine());
                elements[1] = Utiles.decrypt(fin.readLine());
                fin.close();
            }
        } catch (IOException e){
            Log.e(this.getClass().getCanonicalName(), "Error al leerUserYPass", e);
        }
        return elements;
    }

    public void guardarDatosUsuario(String result){
        try {
            /* Guardar usuario y pass en ficheros del sistema. */
            File directory = getApplicationContext().getFilesDir();
            if (!directory.exists()) {
                directory.mkdir();
            }
            File fichero = new File(directory, "datosUsuario.bl");
            if (!fichero.exists()) {
                fichero.createNewFile();
            }

            FileWriter fw = new FileWriter(fichero);
            fw.write(result);
            fw.close();
        } catch (IOException e){
            Log.e(this.getClass().getCanonicalName(), "Error al guardarDatosUsuario", e);
        }
    }

    public String leerDatosUsuario() {
        StringBuilder datosUsuario = new StringBuilder();
        try {
            String cadena;
            FileReader f = new FileReader(new File(getApplicationContext().getFilesDir(), "datosUsuario.bl"));
            BufferedReader b = new BufferedReader(f);
            while ((cadena = b.readLine()) != null) {
                datosUsuario.append(cadena);
            }
            b.close();
            return datosUsuario.toString();
        }catch(IOException e){
            return datosUsuario.toString();
        }
    }
/*
    @Override
    public void siguiente() {
        attemptLogin();
    }
*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("pedidoVersion", pedidoVersion);
    }
}



