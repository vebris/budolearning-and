package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentPuntos extends FragmentAbstract {

    private ListView mPuntosListView;
    private TaskUtiles taskUtiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puntos, container, false);
        taskUtiles = new TaskUtiles(getActivity(), this);

        mPuntosListView = (ListView) view.findViewById(R.id.puntosListView);
        final Usuario usuario;
        if(BLSession.getInstance().getAlumnos() != null && BLSession.getInstance().getAlumnos().size() > 0 && BLSession.getInstance().getPosicionAlumno() > -1) {
             usuario = BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno());
        } else {
            usuario = BLSession.getInstance().getUsuario();
            view.findViewById(R.id.puntos_total).setVisibility(View.INVISIBLE);
        }

        final String nombreAlumno = usuario.getNombre() + " " +
                usuario.getApellido1() + " " +
                usuario.getApellido2();

        ((TextView) view.findViewById(R.id.puntos_nombre_usuario)).setText(nombreAlumno);

        if(Utiles.esSoloAdmin()) {
            mPuntosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    String[] items = {"Borrar"};
                    UtilesDialog.createListDialog(getActivity(), "Opciones", items,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    switch (item) {
                                        case 0:
                                            taskUtiles.borrarPuntos(BLSession.getInstance().getUsuario(), usuario, BLSession.getInstance().getListPuntos().get(position), mPuntosListView);
                                            break;
                                    }
                                }
                            }).show();
                    return true;

                }
            });
        }

                view.findViewById(R.id.puntos_total).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Usuario usuario;
                        if (BLSession.getInstance().getAlumnos() != null && BLSession.getInstance().getAlumnos().size() > 0 && BLSession.getInstance().getPosicionAlumno() > -1) {
                            usuario = BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno());
                        } else {
                            usuario = BLSession.getInstance().getUsuario();
                        }
                        if (usuario.getId() != BLSession.getInstance().getUsuario().getId() || Utiles.esSoloAdmin()) {
                            UtilesDialog.createQuestionYesNo(getActivity(),
                                    "Bonus 50 Puntos.",
                                    "¿ Desea asignar bonus al usuario ?",
                                    "Confirmar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogo1, int id) {
                                            taskUtiles.bonus(BLSession.getInstance().getUsuario(), usuario, mPuntosListView);
                                        }
                                    },
                                    "Cancelar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogo1, int id) {

                                        }
                                    }
                            ).show();
                        }
                    }
                });


                recargar();

                setHasOptionsMenu(true);

                return view;
            }

            public void recargar() {
                Usuario usuario;
                if (BLSession.getInstance().getAlumnos() != null && BLSession.getInstance().getAlumnos().size() > 0 && BLSession.getInstance().getPosicionAlumno() > -1) {
                    usuario = BLSession.getInstance().getAlumnos().get(BLSession.getInstance().getPosicionAlumno());
                } else {
                    usuario = BLSession.getInstance().getUsuario();
                }
                taskUtiles.puntos(BLSession.getInstance().getUsuario(), usuario, mPuntosListView);
            }

            @Override
            public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
                inflater.inflate(R.menu.menu_atras, menu);
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
