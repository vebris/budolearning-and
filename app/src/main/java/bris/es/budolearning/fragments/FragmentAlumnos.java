package bris.es.budolearning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Cache;

import java.util.ArrayList;
import java.util.List;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Club;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.task.TaskClub;
import bris.es.budolearning.task.TaskUsuario;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.Utiles;

public class FragmentAlumnos extends FragmentAbstract{

    private ListView mAlumnosView;
    private TaskClub taskClub;
    private TaskUsuario taskUsuario;
    private TaskUtiles taskUtiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alumnos, container, false);
        taskClub = new TaskClub(getActivity(), this);
        taskUsuario = new TaskUsuario(getActivity(), this);
        taskUtiles = new TaskUtiles(getActivity(), this);

        mAlumnosView = (ListView) view.findViewById(R.id.alumnosListView);

        Spinner clubes = (Spinner) view.findViewById(R.id.menu_spinner_club);
        clubes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                BLSession.getInstance().setAlumnos(new ArrayList<Usuario>());
                recargar();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        if(BLSession.getInstance().getUsuario().getRol().equalsIgnoreCase("ADMINISTRADOR")) {
            if(BLSession.getInstance().getClub() == null) BLSession.getInstance().setClub(BLSession.getInstance().getUsuario().getEntrena());
            taskClub.setNombreClub(BLSession.getInstance().getClub().getNombre());
            taskClub.list(BLSession.getInstance().getUsuario(), null, clubes);
        } else {
            Club c = BLSession.getInstance().getUsuario().getProfesor();
            List<String> txtClub = new ArrayList<String>();
            txtClub.add(c.getNombre());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, txtClub);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            clubes.setAdapter(dataAdapter);
        }

        mAlumnosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {

                taskUtiles.borrarPuntos();
                taskUtiles.borrarEstadisticas();
                BLSession.getInstance().setPosicionAlumno(posicion);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentTabsAlumnos())
                        .addToBackStack(this.getClass().getName())
                        .commit();
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        visualizarMenus(menu, false, false, false, false, true, false, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.btn_menu_recargar:
                BLSession.getInstance().setAlumnos(new ArrayList<Usuario>());
                recargar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void recargar(){
        Club club = BLSession.getInstance().getUsuario().getProfesor();
        if(Utiles.esSoloAdmin()) {
            Spinner spinnerClub = (Spinner) this.getActivity().findViewById(R.id.menu_spinner_club);
            for(Club c: BLSession.getInstance().getClubes()){
                if(c.getNombre().equalsIgnoreCase(spinnerClub.getSelectedItem().toString())){
                    club = c;
                    break;
                }
            }
        }

        if(BLSession.getInstance().getAlumnos() == null || BLSession.getInstance().getAlumnos().size() == 0) {
            Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
            cache.remove("1:" + taskUsuario.getUrl() + taskUsuario.LIST);
        }

        BLSession.getInstance().setClub(club);
        taskUsuario.list(BLSession.getInstance().getUsuario(), club, mAlumnosView);

    }
}
