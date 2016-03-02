package bris.es.budolearning.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Curso;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.task.TaskCurso;

public class FragmentCursoDetalle extends FragmentAbstract {

    private TextView cursoId;
    private EditText cursoNombre;
    private EditText cursoDescripcion;
    private EditText cursoDireccion;
    private EditText cursoProfesor;
    private EditText cursoPrecio;
    private TextView cursoInicioDia;
    private TextView cursoFinDia;
    private TextView cursoInicioHora;
    private TextView cursoFinHora;

    private TaskCurso taskCurso;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_curso_detalle, container, false);
        taskCurso = new TaskCurso(getActivity(), this);

        cursoId = (TextView) view.findViewById(R.id.cursoId);
        cursoNombre = (EditText) view.findViewById(R.id.cursoNombre);
        cursoDescripcion = (EditText)view.findViewById(R.id.cursoDescripcion);
        cursoDireccion = (EditText)view.findViewById(R.id.cursoDireccion);
        cursoProfesor = (EditText) view.findViewById(R.id.cursoProfesor);
        cursoPrecio = (EditText) view.findViewById(R.id.cursoPrecio);
        cursoInicioDia = (TextView) view.findViewById(R.id.cursoFechaInicioDia);
        cursoInicioHora = (TextView) view.findViewById(R.id.cursoFechaInicioHora);
        cursoFinDia = (TextView) view.findViewById(R.id.cursoFechaFinDia);
        cursoFinHora = (TextView) view.findViewById(R.id.cursoFechaFinHora);


        final Curso curso = BLSession.getInstance().getCurso();
        if(curso != null && curso.getId() > 0) {
            cursoId.setText(String.valueOf(curso.getId()));
            cursoNombre.setText(curso.getNombre());
            cursoDescripcion.setText(curso.getDescripcion());
            cursoDireccion.setText(curso.getDireccion());
            cursoProfesor.setText(curso.getProfesor());
            cursoPrecio.setText(curso.getPrecio());
            cursoInicioDia.setText(Utiles.getDateFormatDMA().format(curso.getInicio()));
            cursoInicioHora.setText(Utiles.getDateFormatHM().format(curso.getInicio()));
            cursoFinDia.setText(Utiles.getDateFormatDMA().format(curso.getFin()));
            cursoFinHora.setText(Utiles.getDateFormatHM().format(curso.getFin()));
        } else {
            cursoInicioDia.setText(Utiles.getDateFormatDMA().format(new Date()));
            cursoInicioHora.setText(Utiles.getDateFormatHM().format(new Date()));
            cursoFinDia.setText(Utiles.getDateFormatDMA().format(new Date()));
            cursoFinHora.setText(Utiles.getDateFormatHM().format(new Date()));
        }


        cursoInicioDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if(curso != null && curso.getInicio() != null)
                    c.setTime(curso.getInicio());
                new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cc = Calendar.getInstance();
                                cc.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                cc.set(Calendar.MONTH, monthOfYear);
                                cc.set(Calendar.YEAR, year);
                                cursoInicioDia.setText(Utiles.getDateFormatDMA().format(cc.getTime()));
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        cursoInicioHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if(curso != null && curso.getInicio() != null)
                    c.setTime(curso.getInicio());
                new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar cc = Calendar.getInstance();
                                cc.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cc.set(Calendar.MINUTE, minute);
                                cc.set(Calendar.SECOND, 0);
                                cursoInicioHora.setText(Utiles.getDateFormatHM().format(cc.getTime()));
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true
                ).show();
            }
        });
        cursoFinDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if(curso != null && curso.getInicio() != null)
                    c.setTime(curso.getFin());
                new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cc = Calendar.getInstance();
                                cc.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                cc.set(Calendar.MONTH, monthOfYear);
                                cc.set(Calendar.YEAR, year);
                                cursoFinDia.setText(Utiles.getDateFormatDMA().format(cc.getTime()));
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        cursoFinHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if(curso != null && curso.getInicio() != null)
                    c.setTime(curso.getFin());
                new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar cc = Calendar.getInstance();
                                cc.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cc.set(Calendar.MINUTE, minute);
                                cc.set(Calendar.SECOND, 0);
                                cursoFinHora.setText(Utiles.getDateFormatHM().format(cc.getTime()));
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true
                        ).show();
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registrar_usuarioBtnGuardar:
                Curso curso = BLSession.getInstance().getCurso();

                if(curso == null) curso = new Curso();

                if(cursoId.getText().length()>0) {
                    curso.setId(Integer.parseInt(cursoId.getText().toString()));
                }
                curso.setNombre(cursoNombre.getText().toString());
                curso.setDescripcion(cursoDescripcion.getText().toString());
                curso.setDireccion(cursoDireccion.getText().toString());
                curso.setProfesor(cursoProfesor.getText().toString());
                curso.setPrecio(cursoPrecio.getText().toString());
                try {
                    String inicio = cursoInicioDia.getText().toString() + " " + cursoInicioHora.getText().toString();
                    curso.setInicio(Utiles.getDateFormatDMAHM().parse(inicio));
                }catch(Exception e){
                    Log.d(getClass().getCanonicalName(), "Error fecha Inicio");
                }
                try{
                    String fin = cursoFinDia.getText().toString() + " " + cursoFinHora.getText().toString();
                    curso.setFin(Utiles.getDateFormatDMAHM().parse(fin));
                }catch(Exception e){
                    Log.d(getClass().getCanonicalName(), "Error fecha Fin");
                }

                if(curso.getId() == 0) {
                    taskCurso.insert(BLSession.getInstance().getUsuario(), curso);
                } else {
                    taskCurso.update(BLSession.getInstance().getUsuario(), curso);
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