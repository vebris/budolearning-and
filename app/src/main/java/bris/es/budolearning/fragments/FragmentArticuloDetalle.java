package bris.es.budolearning.fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import bris.es.budolearning.domain.Articulo;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.task.TaskArticulo;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentArticuloDetalle extends FragmentAbstract {

    private TextView articuloId;
    private EditText articuloTitulo;
    private EditText articuloAutor;
    private TextView articuloFecha;
    private CheckBox articuloActivo;
    private CheckBox articuloVisibleUsuarios;
    private TaskArticulo taskArticulo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_articulo_detalle, container, false);
        taskArticulo = new TaskArticulo(getActivity(), this);

        articuloId = (TextView) view.findViewById(R.id.articuloId);
        articuloTitulo = (EditText) view.findViewById(R.id.articuloTitulo);
        articuloAutor = (EditText)view.findViewById(R.id.articuloAutor);
        articuloFecha = (TextView)view.findViewById(R.id.articuloFecha);
        articuloActivo = (CheckBox) view.findViewById(R.id.articuloActivo);
        articuloVisibleUsuarios = (CheckBox) view.findViewById(R.id.articuloVisibleUsuarios);

        final Articulo art = BLSession.getInstance().getArticulo();
        if(art.getId() > 0) {
            articuloId.setText(String.valueOf(art.getId()));
            articuloTitulo.setText(art.getTitulo());
            articuloAutor.setText(art.getAutor());
            articuloFecha.setText(Utiles.getDateFormatDMA().format(art.getFecha()));
            articuloActivo.setChecked(art.getActivo());
            articuloVisibleUsuarios.setChecked(art.getVisibleUsuarios());
        } else {
            articuloId.setText(String.valueOf(art.getId()));
            articuloFecha.setText(Utiles.getDateFormatDMA().format(new Date()));
            articuloActivo.setChecked(true);
            articuloVisibleUsuarios.setChecked(false);
        }

        articuloFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(art.getFecha());
                new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cc = Calendar.getInstance();
                                cc.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                cc.set(Calendar.MONTH, monthOfYear);
                                cc.set(Calendar.YEAR, year);
                                articuloFecha.setText(Utiles.getDateFormatDMA().format(cc.getTime()));
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
            case R.id.registrar_usuarioBtnActivar:
                UtilesDialog.createQuestionYesNo(getActivity(),
                        "BORRAR",
                        "¿ Está seguro de eliminar el artículo ?",
                        "Confirmar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                taskArticulo.delete(BLSession.getInstance().getUsuario(), BLSession.getInstance().getArticulo());
                            }
                        },
                        "Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        }
                ).show();

                return true;
            case R.id.registrar_usuarioBtnGuardar:
                Articulo art = BLSession.getInstance().getArticulo();
                if(articuloId.getText().length()>0) {
                    art.setId(Integer.parseInt(articuloId.getText().toString()));
                }
                art.setTitulo(articuloTitulo.getText().toString());
                art.setAutor(articuloAutor.getText().toString());
                art.setActivo(articuloActivo.isChecked());
                art.setVisibleUsuarios(articuloVisibleUsuarios.isChecked());

                try {
                    art.setFecha(Utiles.getDateFormatDMA().parse(articuloFecha.getText().toString()));
                }catch(java.text.ParseException pe){
                    Log.e("Error parseo: ", pe.getMessage());
                }

                if(art.getId() == 0) {
                    taskArticulo.insert(BLSession.getInstance().getUsuario(), art);
                } else {
                    taskArticulo.update(BLSession.getInstance().getUsuario(), art);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_guardar_borrar, menu);
        menu.findItem(R.id.registrar_usuarioBtnActivar).setIcon(android.R.drawable.ic_delete);
        super.onCreateOptionsMenu(menu, inflater);
    }

}