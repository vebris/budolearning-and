package bris.es.budolearning.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.utiles.Utiles;

public class FragmentFicheroDetalle extends FragmentAbstract {

    private TextView ficheroId;
    private EditText ficheroDescripcion;
    private EditText ficheroFecha;
    private CheckBox ficheroActivo;
    private EditText ficheroCoste;
    private TaskFichero taskFichero;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_fichero_detalle, container, false);
        taskFichero = new TaskFichero(getActivity(), this);

        ficheroId = (TextView) view.findViewById(R.id.ficheroId);
        ficheroDescripcion = (EditText)view.findViewById(R.id.ficheroDescripcion);
        ficheroFecha = (EditText)view.findViewById(R.id.ficheroFecha);
        ficheroActivo = (CheckBox) view.findViewById(R.id.ficheroActivo);
        ficheroCoste = (EditText)view.findViewById(R.id.ficheroCoste);

        final Fichero fichero = BLSession.getInstance().getFichero();
        if(fichero != null && fichero.getId() > 0) {
            ficheroId.setText(String.valueOf(fichero.getId()));
            ficheroDescripcion.setText(fichero.getDescripcion());
            if(fichero.getFecha() != null)
                ficheroFecha.setText(Utiles.getDateFormatDMA().format(fichero.getFecha()));
            else
                ficheroFecha.setText(Utiles.getDateFormatDMA().format(new Date()));

            ficheroActivo.setChecked(fichero.isActivo());
            ficheroCoste.setText(String.valueOf(fichero.getCoste()));
        }


        ficheroFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if(fichero != null && fichero.getFecha() != null)
                    c.setTime(fichero.getFecha());
                else
                    c.setTime(new Date());
                new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cc = Calendar.getInstance();
                                cc.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                cc.set(Calendar.MONTH, monthOfYear);
                                cc.set(Calendar.YEAR, year);
                                ficheroFecha.setText(Utiles.getDateFormatDMA().format(cc.getTime()));
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registrar_usuarioBtnGuardar:
                Fichero fichero = BLSession.getInstance().getFichero();

                if(fichero == null) fichero = new Fichero();

                if(ficheroId.getText().length()>0) {
                    fichero.setId(Integer.parseInt(ficheroId.getText().toString()));
                }
                fichero.setDescripcion(ficheroDescripcion.getText().toString());
                try {
                    fichero.setFecha(Utiles.getDateFormatDMA().parse(ficheroFecha.getText().toString()));
                }catch(Exception e){
                    fichero.setFecha(new Date());
                }
                fichero.setCoste(Integer.parseInt(ficheroCoste.getText().toString()));
                fichero.setActivo(ficheroActivo.isChecked());

                if(fichero.getId() == 0) {
                    fichero.setTamano("0");
                    fichero.setFecha(new Date());
                    taskFichero.insert(BLSession.getInstance().getUsuario(), fichero);
                } else {
                    fichero.setFechaModificacion(new Date());
                    taskFichero.update(BLSession.getInstance().getUsuario(), fichero);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_guardar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}