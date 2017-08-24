package bris.es.budolearning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.Utiles;

public class FragmentEstadisticas extends FragmentAbstract {

    private ListView mEstadisticasListView;
    private TaskUtiles taskUtiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        taskUtiles = new TaskUtiles(getActivity(), this);

        mEstadisticasListView = (ListView) view.findViewById(R.id.estadisticasListView);
        Usuario usuario;
        if(BLSession.getInstance().getAlumnos() != null && BLSession.getInstance().getAlumnos().size() > 0 && BLSession.getInstance().getPosicionAlumno() > -1) {
             usuario = BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno());
        } else {
            usuario = BLSession.getInstance().getUsuario();
        }

        final String nombreAlumno = usuario.getNombre() + " " +
                usuario.getApellido1() + " " +
                usuario.getApellido2();

        ((TextView) view.findViewById(R.id.estadisticas_nombre_usuario)).setText(nombreAlumno);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -3);
        ((TextView) view.findViewById(R.id.estadisticas_fecha)).setText(Utiles.getDateFormatDMA().format(c.getTime()));

        recargar();

        setHasOptionsMenu(true);

        return view;
    }

    public void recargar(){
        Usuario usuario;
        if(BLSession.getInstance().getAlumnos() != null && BLSession.getInstance().getAlumnos().size() > 0 && BLSession.getInstance().getPosicionAlumno() > -1) {
            usuario = BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno());
        } else {
            usuario = BLSession.getInstance().getUsuario();
        }
        taskUtiles.estadisticas(BLSession.getInstance().getUsuario(), usuario, mEstadisticasListView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        visualizarMenus(menu, false, false, false, false, false, false, true);
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
