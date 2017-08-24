package bris.es.budolearning.domain.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.util.List;

import bris.es.budolearning.Activity_Logged;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentFicheros;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;


public class FicheroCustomRecyclerAdapter extends RecyclerView.Adapter<FicheroCustomRecyclerAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Object item, View v);
    }

    private static Activity activity;
    private List<?> elements;
    private Object selected;
    private final OnItemClickListener listener;
    private FragmentAbstract fragment;


    public FicheroCustomRecyclerAdapter(List<?> objetos, Activity activity, FragmentAbstract fragment, Object selected, OnItemClickListener listener) {
        FicheroCustomRecyclerAdapter.activity = activity;
        this.fragment = fragment;
        this.elements = objetos;
        this.selected = selected;
        this.listener = listener;
    }

    public void setSelected (Object selected){
        this.selected = selected;
        for(int i=0;i<elements.size();i++){
            this.notifyItemChanged(i);
        }
    }
    public int getSelectedPosition (){
        return elements.indexOf(selected);
    }


    @Override
    public FicheroCustomRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.layout_lista_ficheros, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Fichero rowItem = (Fichero) elements.get(position);

        // PARA MOSTRAR O NO LOS BOTONES...
        final File fichero = new File (Utiles.getDirectorioCacheVideo(), Utiles.md5(String.valueOf(rowItem.getId())));

        viewHolder.descripcion.setText(rowItem.getDescripcion().toUpperCase());
        if(rowItem.getSegundos() > 0) {
            viewHolder.segundos.setText(Utiles.toMinSeg(rowItem.getSegundos()));
        } else {
            viewHolder.segundos.setText("");
            viewHolder.segundos.setCompoundDrawables(null,null,null,null);
        }
        if (rowItem.getFecha() != null) {
            viewHolder.fecha.setText(Utiles.getDateFormatDMA().format(rowItem.getFecha()));
        } else {
            viewHolder.fecha.setText("");
        }
        viewHolder.tamano.setText(Utiles.toMB(Long.valueOf(rowItem.getTamano())));



        if (fichero.exists()) {
            viewHolder.save.setVisibility(View.INVISIBLE);
            viewHolder.save.setImageBitmap(null);

            if (rowItem.getExtension().equalsIgnoreCase("PDF")) {
                viewHolder.play.setImageResource(android.R.drawable.ic_menu_view);
            } else {
                viewHolder.play.setImageResource(android.R.drawable.ic_media_play);
            }

            viewHolder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BLSession.getInstance().setFichero(rowItem);
                    ((Activity_Logged) FicheroCustomRecyclerAdapter.activity).mostrarPublicidad(fragment);
                }
            });
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BLSession.getInstance().setFichero(rowItem);
                    UtilesDialog.createQuestionYesNo((Activity) FicheroCustomRecyclerAdapter.activity,
                            "Borrar fichero",
                            "¿ Desea borrar el fichero ?",
                            "Confirmar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    File file = new File(Utiles.getDirectorioCacheVideo() + "/" + Utiles.md5(String.valueOf(BLSession.getInstance().getFichero().getId())));
                                    if (file.delete()) {
                                        UtilesDialog.createInfoMessage((Activity) FicheroCustomRecyclerAdapter.activity, "BORRADO", "Borrado correcto").show();
                                        ((FragmentFicheros) fragment).recargar();
                                    }
                                }
                            }, "Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                }
                            }
                    ).show();
                }
            });
        } else {
            viewHolder.play.setVisibility(View.INVISIBLE);
            viewHolder.play.setImageBitmap(null);
            viewHolder.delete.setVisibility(View.INVISIBLE);
            viewHolder.delete.setImageBitmap(null);

            viewHolder.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BLSession.getInstance().setFichero(rowItem);
                    TaskFichero taskFichero = new TaskFichero((Activity) FicheroCustomRecyclerAdapter.activity, fragment);
                    TaskUtiles taskUtiles = new TaskUtiles((Activity) FicheroCustomRecyclerAdapter.activity, fragment);
                    if (Utiles.permitirDescarga(Utiles.getConfiguracion(FicheroCustomRecyclerAdapter.activity.getApplicationContext()), FicheroCustomRecyclerAdapter.activity.getApplicationContext())) {
                        taskFichero.downloadFile(BLSession.getInstance().getUsuario(), rowItem, null, taskUtiles, rowItem.getCoste());
                    } else {
                        Toast.makeText(FicheroCustomRecyclerAdapter.activity, "Configuración Conectividad-> Ver/Descargar videos -> SOLO WIFI", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        viewHolder.bind(elements.get(position), listener);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(elements != null) return elements.size();
        return 0;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView descripcion;
        TextView fecha;
        TextView tamano;
        TextView segundos;

        ImageView save;
        ImageView play;
        ImageView delete;

        ViewHolder(View itemView) {
            super(itemView);
            descripcion = (TextView) itemView.findViewById(R.id.fichero_descripcion);
            fecha = (TextView) itemView.findViewById(R.id.fichero_fecha);
            tamano = (TextView) itemView.findViewById(R.id.fichero_tamano);
            segundos = (TextView) itemView.findViewById(R.id.fichero_segundos);
            save = (ImageView) itemView.findViewById(R.id.fichero_salvar);
            play = (ImageView) itemView.findViewById(R.id.fichero_ver);
            delete = (ImageView) itemView.findViewById(R.id.fichero_borrar);
        }
        void bind(final Object item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    RecyclerView vv = (RecyclerView) v.getParent();
                    ((FicheroCustomRecyclerAdapter)vv.getAdapter()).setSelected(item);
                    vv.scrollToPosition(((FicheroCustomRecyclerAdapter)vv.getAdapter()).getSelectedPosition());
                    //listener.onItemClick(item, v);
                }
            });
        }
    }
}
