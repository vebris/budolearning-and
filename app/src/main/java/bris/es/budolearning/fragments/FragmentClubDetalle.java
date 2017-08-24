package bris.es.budolearning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Club;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.task.TaskClub;

public class FragmentClubDetalle extends FragmentAbstract {

    private TextView clubId;
    private EditText clubNombre;
    private EditText clubDescripcion;
    private EditText clubDireccion;
    private EditText clubLocalidad;
    private EditText clubEmail;
    private EditText clubTelefono;
    private EditText clubWeb;
    private EditText clubProfesor;

    private TaskClub taskClub;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club_detalle, container, false);
        taskClub = new TaskClub(getActivity(), this);

        clubId = (TextView) view.findViewById(R.id.clubId);
        clubNombre = (EditText) view.findViewById(R.id.clubNombre);
        clubDescripcion = (EditText)view.findViewById(R.id.clubDescripcion);
        clubDireccion = (EditText)view.findViewById(R.id.clubDireccion);
        clubLocalidad = (EditText) view.findViewById(R.id.clubLocalidad);
        clubEmail = (EditText) view.findViewById(R.id.clubEmail);
        clubTelefono = (EditText) view.findViewById(R.id.clubTelefono);
        clubWeb = (EditText) view.findViewById(R.id.clubWeb);
        clubProfesor = (EditText) view.findViewById(R.id.clubProfesor);


        Club club = BLSession.getInstance().getClub();
        if(club != null && club.getId() > 0) {
            clubId.setText(String.valueOf(club.getId()));
            clubNombre.setText(club.getNombre());
            clubDescripcion.setText(club.getDescripcion());
            clubDireccion.setText(club.getDireccion());
            clubLocalidad.setText(club.getLocalidad());
            clubEmail.setText(club.getEmail());
            clubTelefono.setTag(club.getTelefono());
            clubWeb.setTag(club.getWeb());
            clubProfesor.setTag(club.getNombre());
        }

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_menu_guardar:
                Club club = BLSession.getInstance().getClub();

                if(club == null) club = new Club();

                if(clubId.getText().length()>0) {
                    club.setId(Integer.parseInt(clubId.getText().toString()));
                }
                club.setNombre(clubNombre.getText().toString());
                club.setDescripcion(clubDescripcion.getText().toString());
                club.setDireccion(clubDireccion.getText().toString());
                club.setLocalidad(clubLocalidad.getText().toString());
                club.setEmail(clubEmail.getText().toString());
                club.setTelefono(clubTelefono.getText().toString());
                club.setWeb(clubWeb.getText().toString());
                club.setProfesor(clubProfesor.getText().toString());

                if(club.getId() == 0) {
                    taskClub.insert(BLSession.getInstance().getUsuario(), club);
                } else {
                    taskClub.update(BLSession.getInstance().getUsuario(), club);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        visualizarMenus(menu, false, false, false, false, true, false, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

}