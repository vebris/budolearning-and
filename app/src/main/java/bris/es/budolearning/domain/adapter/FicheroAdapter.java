package bris.es.budolearning.domain.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.List;

import bris.es.budolearning.Activity_Logged;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentFicheros;
import bris.es.budolearning.fragments.FragmentFicherosNuevos;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FicheroAdapter extends BaseAdapter {

    private FragmentAbstract fragment;
    private Context context;
    private List<Fichero> ficheros;
    private boolean verBotones;

    public FicheroAdapter(List<Fichero> ficheros, Context context, FragmentAbstract fragment) {
        this.context = context;
        this.ficheros = ficheros;
        this.fragment = fragment;
        this.verBotones = true;
    }
    public FicheroAdapter(List<Fichero> ficheros, Context context, FragmentAbstract fragment, boolean verBotones) {
        this.context = context;
        this.ficheros = ficheros;
        this.fragment = fragment;
        this.verBotones = verBotones;
    }

    @Override
    public int getCount() {
        return ficheros.size();
    }

    @Override
    public Fichero getItem(int location) {
        return ficheros.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView descripcion;
        TextView fecha;
        TextView tamano;
        TextView segundos;

        ImageView save;
        ImageView play;
        ImageView delete;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        final Fichero rowItem = getItem(position);
        int color;
        switch(rowItem.getActivo()){
            case 0:
                convertView = mInflater.inflate(R.layout.layout_lista_ficheros, null);
                color = Utiles.getColor(context,R.color.tertiary_dark);
                break;
            default:
                //convertView = mInflater.inflate(R.layout.layout_lista_ficheros_med, null);
                convertView = mInflater.inflate(R.layout.layout_lista_ficheros, null);
                color = Utiles.getColor(context,R.color.primary_dark);
                break;
        }
        holder = new ViewHolder();
        holder.descripcion = (TextView) convertView.findViewById(R.id.fichero_descripcion);
        holder.fecha = (TextView) convertView.findViewById(R.id.fichero_fecha);
        holder.tamano = (TextView) convertView.findViewById(R.id.fichero_tamano);
        holder.segundos = (TextView) convertView.findViewById(R.id.fichero_segundos);
        holder.save = (ImageView) convertView.findViewById(R.id.fichero_salvar);
        holder.play = (ImageView) convertView.findViewById(R.id.fichero_ver);
        holder.delete = (ImageView) convertView.findViewById(R.id.fichero_borrar);
        convertView.setTag(holder);


        // PARA MOSTRAR O NO LOS BOTONES...
        final File fichero = new File (Utiles.getDirectorioCacheVideo(), Utiles.md5(String.valueOf(rowItem.getId())));

        holder.descripcion.setText(rowItem.getDescripcion().toUpperCase());
        if(rowItem.getSegundos() > 0) {
            holder.segundos.setText(Utiles.toMinSeg(rowItem.getSegundos()));
        } else {
            holder.segundos.setText("");
            holder.segundos.setCompoundDrawables(null,null,null,null);
        }
        if (rowItem.getFecha() != null) {
            holder.fecha.setText(Utiles.getDateFormatDMA().format(rowItem.getFecha()));
        } else {
            holder.fecha.setText("");
        }
        holder.tamano.setText(Utiles.toMB(Long.valueOf(rowItem.getTamano())));

        if(verBotones) {

            if (fichero.exists()) {
                holder.save.setVisibility(View.INVISIBLE);
                holder.save.setImageBitmap(null);

                if (rowItem.getExtension().equalsIgnoreCase("PDF")) {
                    holder.play.setImageResource(android.R.drawable.ic_menu_view);
                } else {
                    holder.play.setImageResource(android.R.drawable.ic_media_play);
                }

                holder.play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BLSession.getInstance().setFichero(rowItem);
                        ((Activity_Logged) context).mostrarPublicidad(fragment);
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BLSession.getInstance().setFichero(rowItem);
                        UtilesDialog.createQuestionYesNo((Activity) context,
                                "Borrar fichero",
                                "¿ Desea borrar el fichero ?",
                                "Confirmar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        File file = new File(Utiles.getDirectorioCacheVideo() + "/" + Utiles.md5(String.valueOf(BLSession.getInstance().getFichero().getId())));
                                        if (file.delete()) {
                                            UtilesDialog.createInfoMessage((Activity) context, "BORRADO", "Borrado correcto").show();
                                            if(fragment instanceof FragmentFicheros) {
                                                ((FragmentFicheros) fragment).recargar();
                                            }
                                            if(fragment instanceof FragmentFicherosNuevos) {
                                                ((FragmentFicherosNuevos) fragment).recargar();
                                            }
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
                holder.play.setVisibility(View.INVISIBLE);
                holder.play.setImageBitmap(null);
                holder.delete.setVisibility(View.INVISIBLE);
                holder.delete.setImageBitmap(null);

                holder.save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BLSession.getInstance().setFichero(rowItem);
                        TaskFichero taskFichero = new TaskFichero((Activity) context, fragment);
                        TaskUtiles taskUtiles = new TaskUtiles((Activity) context, fragment);
                        if (Utiles.permitirDescarga(Utiles.getConfiguracion(context.getApplicationContext()), context.getApplicationContext())) {
                            taskFichero.downloadFile(BLSession.getInstance().getUsuario(), rowItem, null, taskUtiles, rowItem.getCoste());
                        } else {
                            Toast.makeText(context, "Configuración Conectividad-> Ver/Descargar videos -> SOLO WIFI", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else {
            holder.play.setVisibility(View.INVISIBLE);
            holder.play.setImageBitmap(null);
            holder.delete.setVisibility(View.INVISIBLE);
            holder.delete.setImageBitmap(null);
            holder.save.setVisibility(View.INVISIBLE);
            holder.save.setImageBitmap(null);

            convertView.findViewById(R.id.wrap_datos).setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fichero.exists()) {
                        BLSession.getInstance().setFichero(rowItem);
                        ((Activity_Logged) context).mostrarPublicidad(fragment);
                    } else {
                        BLSession.getInstance().setFichero(rowItem);
                        TaskFichero taskFichero = new TaskFichero((Activity) context, fragment);
                        TaskUtiles taskUtiles = new TaskUtiles((Activity) context, fragment);
                        if (Utiles.permitirDescarga(Utiles.getConfiguracion(context.getApplicationContext()), context.getApplicationContext())) {
                            taskFichero.downloadFile(BLSession.getInstance().getUsuario(), rowItem, null, taskUtiles, rowItem.getCoste());
                        } else {
                            Toast.makeText(context, "Configuración Conectividad-> Ver/Descargar videos -> SOLO WIFI", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }


        if(holder.segundos.getCompoundDrawablesRelative()[0] != null) holder.segundos.getCompoundDrawablesRelative()[0].mutate().setColorFilter( color, PorterDuff.Mode.MULTIPLY);
        if(holder.fecha.getCompoundDrawablesRelative()[0] != null) holder.fecha.getCompoundDrawablesRelative()[0].mutate().setColorFilter( color, PorterDuff.Mode.MULTIPLY);
        if(holder.tamano.getCompoundDrawablesRelative()[0] != null) holder.tamano.getCompoundDrawablesRelative()[0].mutate().setColorFilter( color, PorterDuff.Mode.MULTIPLY);

        return convertView;
    }


}
