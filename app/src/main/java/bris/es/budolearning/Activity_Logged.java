package bris.es.budolearning;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentAlumnos;
import bris.es.budolearning.fragments.FragmentArticulos;
import bris.es.budolearning.fragments.FragmentClubes;
import bris.es.budolearning.fragments.FragmentCursos;
import bris.es.budolearning.fragments.FragmentDescargas;
import bris.es.budolearning.fragments.FragmentDisciplinaReducida;
import bris.es.budolearning.fragments.FragmentDisciplinas;
import bris.es.budolearning.fragments.FragmentFicheros;
import bris.es.budolearning.fragments.FragmentFicherosNuevos;
import bris.es.budolearning.fragments.FragmentGrados;
import bris.es.budolearning.fragments.FragmentPreferences;
import bris.es.budolearning.fragments.FragmentPreferencesEspecial;
import bris.es.budolearning.fragments.FragmentRecursos;
import bris.es.budolearning.fragments.FragmentTabsAlumnos;
import bris.es.budolearning.fragments.FragmentTabsDisciplinas;
import bris.es.budolearning.fragments.FragmentTabsPagina;
import bris.es.budolearning.fragments.FragmentVideosEspeciales;
import bris.es.budolearning.menu.lateral.CustomMenuItem;
import bris.es.budolearning.menu.lateral.CustomMenuListAdapter;
import bris.es.budolearning.task.TaskArticulo;
import bris.es.budolearning.task.TaskClub;
import bris.es.budolearning.task.TaskCurso;
import bris.es.budolearning.task.TaskDisciplina;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.task.TaskGrado;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;
import bris.es.budolearning.utiles.UtilesMediaStore;

public class Activity_Logged extends Activity_Publicidad {

    int PRIMER_MENU=3;

    public static Context context;

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerPane;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // slide menu items
    private String[] navMenuTitles;

    // used to store app title
    private CharSequence mTitle;

    boolean LOG_OUT = false;
    private int numClicks = 0;

    private Fragment fragment = null;
    private Fragment fragmentPublicidad = null;

    private TaskCurso taskCurso;
    private TaskClub taskClub;
    private TaskArticulo taskArticulo;
    private TaskFichero taskFichero;
    private TaskDisciplina taskDisciplina;
    private TaskGrado taskGrado;
    private TaskUtiles taskUtiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskCurso = new TaskCurso(this, null);
        taskClub = new TaskClub(this, null);
        taskFichero = new TaskFichero(this, null);
        taskDisciplina = new TaskDisciplina(this, null);
        taskGrado = new TaskGrado(this, null);
        taskArticulo = new TaskArticulo(this, null);
        taskUtiles = new TaskUtiles(this, null);

        //taskArticulo.list(BLSession.getInstance().getUsuario(), null, null);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Activity_Logged.context = getApplicationContext();
        mTitle = getTitle();

        // Setup navigation drawer
        setupNavigationDrawer();

        LOG_OUT = false;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            setTitle(navMenuTitles[PRIMER_MENU]);
            displayView(PRIMER_MENU);
        } else {
            setTitle(savedInstanceState.getCharSequence("title"));
        }

        cargarPublicidad();

    }

    private void menuPrincipal() {
        navMenuTitles = getResources().getStringArray(R.array.menu_items_left);
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.menu_icons_left);
        ArrayList<CustomMenuItem> navDrawerItems = new ArrayList<CustomMenuItem>();
        // Agregar opciones de menu
        int pos = 0;
        // HISTORIA
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), Utiles.esSoloAdmin()));
        // ESPECIAL
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), Utiles.esSoloAdmin()));
        // Ficheros sin Recurso (Nuevos)
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), Utiles.esSoloAdmin()));
        // DISCIPLINAS
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), true));
        // ARTICULOS
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), Utiles.esSoloAdmin()));
        // CURSOS
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), true));
        // CLUBES
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), true));
        // ALUMNOS
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), Utiles.esAdmin()));
        // DESCARGAS
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1),Utiles.esSoloAdmin()));
        // MIS DATOS
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), true));
        // CONFIGURACION
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos++, -1), true));
        // SALIR
        navDrawerItems.add(pos, new CustomMenuItem(navMenuTitles[pos], navMenuIcons.getResourceId(pos, -1), true));

        //navDrawerItems.add(new CustomMenuItem(navMenuTitlesLeft[2], navMenuIcons.getResourceId(2, -1)));//, true, "1"));

        // Recycle the typed array
        navMenuIcons.recycle();

        CustomMenuListAdapter adapter = new CustomMenuListAdapter(this, navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayView(position);
            }
        });
    }

    public void updatePoints() {
        ((TextView) findViewById(R.id.tool_puntos)).setText(String.valueOf(BLSession.getInstance().getPuntos()));
    }

    private void setupNavigationDrawer() {
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        //@drawable/ic_launcher
        ImageView img = (ImageView) findViewById(R.id.header_id);
        TextView txtNombre = (TextView) findViewById(R.id.header_nombre);
        TextView txtMail = (TextView) findViewById(R.id.header_mail);

        if (BLSession.getInstance() == null
                || BLSession.getInstance().getUsuario() == null
                || BLSession.getInstance().getUsuario().getApellido1() == null) {
            finish();
        }

        img.setImageResource(R.drawable.ic_drawer);

        String nombre =
                BLSession.getInstance().getUsuario().getApellido1() + " " +
                        BLSession.getInstance().getUsuario().getApellido2() + ", " +
                        BLSession.getInstance().getUsuario().getNombre();
        txtNombre.setText(nombre);
        txtMail.setText(BLSession.getInstance().getUsuario().getMail());

        updatePoints();


        LinearLayout profileBox = (LinearLayout) findViewById(R.id.profileBox);
        profileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numClicks == 10) {
                    setTitle("Preferencias Especiales");
                    fragment = new FragmentPreferencesEspecial();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment).disallowAddToBackStack().commit();
                    mDrawerLayout.closeDrawer(mDrawerPane);
                } else {
                    numClicks++;
                }
            }
        });

        menuPrincipal();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(mTitle);
                mDrawerList.smoothScrollToPositionFromTop(0, 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(mTitle);
                numClicks = 0;
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the menu content by replacing fragments
        BLSession.getInstance().setPosicionAlumno(-1);
        if (position != 11) setTitle(navMenuTitles[position]);
        switch (position) {
            case 0:
                fragment = new FragmentTabsPagina();
                break;
            case 1:
                fragment = new FragmentVideosEspeciales();
                break;
            case 2:
                fragment = new FragmentFicherosNuevos();
                break;
            case 3:
                if(!Utiles.getConfiguracion(this).isVistaReducida()) {
                    if (FragmentTabsDisciplinas.mSlidingTabLayout != null)
                        FragmentTabsDisciplinas.mSlidingTabLayout.removeAllViews();
                    if (FragmentTabsDisciplinas.mViewPager != null)
                        FragmentTabsDisciplinas.mViewPager.removeAllViews();
                    BLSession.getInstance().setTabsDisciplinas(null);
                    fragment = new FragmentTabsDisciplinas();
                } else {
                    fragment = new FragmentDisciplinaReducida();
                }
                break;
            case 4:
                fragment = new FragmentArticulos();
                break;
            case 5:
                fragment = new FragmentCursos();
                break;
            case 6:
                fragment = new FragmentClubes();
                break;
            case 7:
                BLSession.getInstance().setFichero(null);
                fragment = new FragmentAlumnos();
                break;
            case 8:
                fragment = new FragmentDescargas();
                break;
            case 9:
                BLSession.getInstance().setAlumnos(null);
                fragment = new FragmentTabsAlumnos();
                ((FragmentTabsAlumnos) fragment).setAlumno(false);
                (new TaskUtiles(this, null)).borrarPuntos();
                (new TaskUtiles(this, null)).borrarEstadisticas();
                break;
            case 10:
                fragment = new FragmentPreferences();
                break;
            case 11:
                LOG_OUT = true;
                salir();
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).disallowAddToBackStack().commit();
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerPane);
        } else {
            Log.e(this.getClass().getCanonicalName(), "Activity_Logged - Error cuando se creo el fragment " + position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.btn_menu_nuevo).setVisible(false);
        menu.findItem(R.id.btn_menu_activar).setVisible(false);
        menu.findItem(R.id.btn_menu_guardar).setVisible(false);
        menu.findItem(R.id.btn_menu_recargar).setVisible(false);
        menu.findItem(R.id.btn_menu_atras).setVisible(false);
        this.invalidateOptionsMenu();
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
        ((TextView) findViewById(R.id.tool_title)).setText(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void salir() {
        UtilesDialog.createQuestionYesNo(this,
                "Salir",
                "¿ Desea salir de la aplicación ?",
                "Confirmar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        BLSession.getInstance().setAutoLogin(false);
                        finish();
                    }
                },
                "Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        mDrawerLayout.closeDrawer(mDrawerPane);
                        LOG_OUT = false;
                    }
                }
        ).show();
    }

    @Override
    public void siguiente() {
        if ((fragmentPublicidad instanceof FragmentFicheros
                || fragmentPublicidad instanceof FragmentRecursos
                || fragmentPublicidad instanceof FragmentGrados
                || fragmentPublicidad instanceof FragmentDisciplinas
                || fragmentPublicidad instanceof FragmentDescargas
                || fragmentPublicidad instanceof FragmentDisciplinaReducida
                || fragmentPublicidad instanceof FragmentFicherosNuevos
            ) && BLSession.getInstance().getFichero() != null) {
            if (BLSession.getInstance().getFichero().getExtension().equalsIgnoreCase("PDF")) {
                Intent i = new Intent(this, Activity_View_Pdf.class);
                fragmentPublicidad.getActivity().startActivity(i);
            } else {
                Intent i = new Intent(this, Activity_View_Mp4.class);
                fragmentPublicidad.getActivity().startActivity(i);
            }

            if (BLSession.getInstance().getFichero().getTamano() != null)
                taskUtiles.visualizaciones(1);

        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            salir();
        } else {
            super.onBackPressed();
            if (LOG_OUT) onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case FragmentAbstract.VER_VIDEO:
                //File file = new File(Utiles.getDirectorioDescargas() +"/"+ BLSession.getInstance().getIdFichero() + Constants.EXTENSION_VIDEO_FILE );
                //file.delete();
                return;
            case FragmentAbstract.CHOOSER_FOTO_CURSO:
                if (resultCode == Activity.RESULT_OK) {
                    String path = UtilesMediaStore.getPath(this, data.getData());
                    taskCurso.uploadFile(BLSession.getInstance().getUsuario(), BLSession.getInstance().getCurso(), new File(path));
                }
                break;
            case FragmentAbstract.CHOOSER_FOTO_CLUB:
                if (resultCode == Activity.RESULT_OK) {
                    String path = UtilesMediaStore.getPath(this, data.getData());
                    taskClub.uploadFile(BLSession.getInstance().getUsuario(), BLSession.getInstance().getClub(), new File(path));
                }
                break;
            case FragmentAbstract.CHOOSER_FOTO_DISCIPLINA:
                if (resultCode == Activity.RESULT_OK) {
                    String path = UtilesMediaStore.getPath(this, data.getData());
                    taskDisciplina.uploadFile(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina(), new File(path));
                }
                break;
            case FragmentAbstract.CHOOSER_FOTO_GRADO:
                if (resultCode == Activity.RESULT_OK) {
                    String path = UtilesMediaStore.getPath(this, data.getData());
                    taskGrado.uploadFile(BLSession.getInstance().getUsuario(), BLSession.getInstance().getGrado(), new File(path));
                }
                break;
            case FragmentAbstract.CHOOSER_PDF_ARTICULO:
                if (resultCode == Activity.RESULT_OK) {
                    String path = UtilesMediaStore.getPath(this, data.getData());
                    taskArticulo.uploadFile(BLSession.getInstance().getUsuario(), BLSession.getInstance().getArticulo(), new File(path));
                }
                break;
            case FragmentAbstract.CHOOSER_VIDEO:
                if (resultCode == Activity.RESULT_OK) {
                    String path = UtilesMediaStore.getPath(this, data.getData());
                    taskFichero.uploadFile(BLSession.getInstance().getUsuario(), BLSession.getInstance().getFichero(), new File(path));
                }
                break;
        }
    }

    public void mostrarPublicidad(FragmentAbstract fragmentPublicidad) {
        this.fragmentPublicidad = fragmentPublicidad;
        mostrarPublicidad();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("title", mTitle);
    }

}