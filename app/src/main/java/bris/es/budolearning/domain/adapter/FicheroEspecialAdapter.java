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
import bris.es.budolearning.domain.VideoEspecial;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentVideosEspeciales;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FicheroEspecialAdapter extends BaseAdapter {

    private Context context;
    private List<VideoEspecial> ficheros;
    private FragmentAbstract fragment;

    public FicheroEspecialAdapter(List<VideoEspecial> ficheros, Context context, FragmentAbstract fragment) {
        this.context = context;
        this.ficheros = ficheros;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        if(ficheros == null) return 0;
        return ficheros.size();
    }

    @Override
    public VideoEspecial getItem(int location) {
        return ficheros.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView id;
        TextView fichero;
        TextView club;
        TextView usuario;
        TextView inicio;
        TextView fin;

        TextView fecha;
        TextView tamano;
        TextView segundos;

        ImageView save;
        ImageView play;
        ImageView delete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.layout_lista_video_especial, null);
        holder = new ViewHolder();
        holder.id = (ImageView) convertView.findViewById(R.id.fichero_especial_id);
        holder.fichero = (TextView) convertView.findViewById(R.id.fichero_especial_fichero);
        holder.club = (TextView) convertView.findViewById(R.id.fichero_especial_club);
        holder.usuario = (TextView) convertView.findViewById(R.id.fichero_especial_usuario);
        holder.inicio = (TextView) convertView.findViewById(R.id.fichero_especial_inicio);
        holder.fin = (TextView) convertView.findViewById(R.id.fichero_especial_fin);

        holder.fecha = (TextView) convertView.findViewById(R.id.fichero_especial_fecha);
        holder.tamano = (TextView) convertView.findViewById(R.id.fichero_especial_tamano);
        holder.segundos = (TextView) convertView.findViewById(R.id.fichero_especial_segundos);

        holder.save = (ImageView) convertView.findViewById(R.id.fichero_especial_salvar);
        holder.play = (ImageView) convertView.findViewById(R.id.fichero_especial_ver);
        holder.delete = (ImageView) convertView.findViewById(R.id.fichero_especial_borrar);

        convertView.setTag(holder);


        final VideoEspecial rowItem = getItem(position);

        File fichero = new File (Utiles.getDirectorioCacheVideo(), Utiles.md5(String.valueOf(rowItem.getFichero().getId())));

        if(fichero.exists()) {
            holder.id.setImageResource(android.R.drawable.star_big_on);
        } else {
            holder.id.setImageResource(android.R.drawable.star_big_off);
        }

        holder.fichero.setText(rowItem.getFichero().getDescripcion().replaceAll(" - ", "\r\n"));
        holder.club.setText(rowItem.getClub().getDescripcion());
        if(rowItem.getUsuario() != null)
            holder.usuario.setText(rowItem.getUsuario().getApellido1() + " " + rowItem.getUsuario().getApellido2() + ", " + rowItem.getUsuario().getNombre());
        else
            holder.usuario.setText("");
        holder.inicio.setText(Utiles.getDateFormatDMA().format(rowItem.getInicio()));
        holder.fin.setText(Utiles.getDateFormatDMA().format(rowItem.getFin()));



        if(!Utiles.esAdmin()) {
            ((LinearLayout)convertView.findViewById(R.id.linearlayout_video_especial)).removeView(convertView.findViewById(R.id.fichero_especial_club));
            ((LinearLayout)convertView.findViewById(R.id.linearlayout_video_especial)).removeView(convertView.findViewById(R.id.fichero_especial_usuario));
        }

        if(fichero.exists()){
            holder.save.setVisibility(View.INVISIBLE);
            holder.save.setImageBitmap(null);

            if(rowItem.getFichero().getExtension().equalsIgnoreCase("PDF")){
                holder.play.setImageResource(android.R.drawable.ic_menu_view);
            } else {
                holder.play.setImageResource(android.R.drawable.ic_media_play);
            }

            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BLSession.getInstance().setFichero(rowItem.getFichero());
                    ((Activity_Logged) context).mostrarPublicidad(fragment);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BLSession.getInstance().setFichero(rowItem.getFichero());
                    UtilesDialog.createQuestionYesNo((Activity)context,
                            "Borrar fichero",
                            "¿ Desea borrar el fichero ?",
                            "Confirmar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    File file = new File(Utiles.getDirectorioCacheVideo() + "/" + Utiles.md5(String.valueOf(BLSession.getInstance().getFichero().getId())));
                                    if (file.delete()) {
                                        UtilesDialog.createInfoMessage((Activity)context, "BORRADO", "Borrado correcto").show();
                                        ((FragmentVideosEspeciales)fragment).recargar();
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
                    BLSession.getInstance().setFichero(rowItem.getFichero());
                    TaskFichero taskFichero = new TaskFichero((Activity)context, fragment);
                    TaskUtiles taskUtiles = new TaskUtiles((Activity)context, fragment);
                    if (Utiles.permitirDescarga(Utiles.getConfiguracion(context.getApplicationContext()), context.getApplicationContext())) {
                        taskFichero.downloadFile(BLSession.getInstance().getUsuario(), rowItem, null, taskUtiles, rowItem.getFichero().getCoste());
                    } else {
                        Toast.makeText(context, "Configuración Conectividad-> Ver/Descargar videos -> SOLO WIFI", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        holder.tamano.setText(Utiles.toMB(Long.valueOf(rowItem.getFichero().getTamano())));
        if(rowItem.getFichero().getSegundos() > 0) {
            holder.segundos.setText(Utiles.toMinSeg(rowItem.getFichero().getSegundos()));
        } else {
            holder.segundos.setText("");
            holder.segundos.setCompoundDrawables(null,null,null,null);
        }
        if (rowItem.getFichero().getFecha() != null) {
            holder.fecha.setText(Utiles.getDateFormatDMA().format(rowItem.getFichero().getFecha()));
        } else {
            holder.fecha.setText("");
        }


        int color = Utiles.getColor(context,R.color.primary_dark);
        if(holder.segundos.getCompoundDrawablesRelative()[0] != null) holder.segundos.getCompoundDrawablesRelative()[0].mutate().setColorFilter( color, PorterDuff.Mode.MULTIPLY);
        if(holder.fecha.getCompoundDrawablesRelative()[0] != null) holder.fecha.getCompoundDrawablesRelative()[0].mutate().setColorFilter( color, PorterDuff.Mode.MULTIPLY);
        if(holder.tamano.getCompoundDrawablesRelative()[0] != null) holder.tamano.getCompoundDrawablesRelative()[0].mutate().setColorFilter( color, PorterDuff.Mode.MULTIPLY);


        return convertView;
    }


}
