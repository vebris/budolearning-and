package bris.es.budolearning.domain;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;

public class FicheroAdapter extends BaseAdapter {

    private Context context;
    private List<Fichero> ficheros;

    public FicheroAdapter(List<Fichero> ficheros, Context context) {
        this.context = context;
        this.ficheros = ficheros;
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
        ImageView id;
        TextView descripcion;
        TextView fecha;
        TextView tamano;
        ImageView nuevo;
        TextView coste;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_ficheros, null);
            holder = new ViewHolder();
            holder.id = (ImageView) convertView.findViewById(R.id.fichero_id);
            holder.descripcion = (TextView) convertView.findViewById(R.id.fichero_descripcion);
            holder.fecha = (TextView) convertView.findViewById(R.id.fichero_fecha);
            holder.tamano = (TextView) convertView.findViewById(R.id.fichero_tamano);
            holder.nuevo = (ImageView) convertView.findViewById(R.id.fichero_nuevo);
            holder.coste = (TextView) convertView.findViewById(R.id.fichero_coste);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Fichero rowItem = getItem(position);


        if(rowItem.getExtension().equalsIgnoreCase("PDF") && BLSession.getInstance().getUsuario().getRol().equalsIgnoreCase("USER")){
            convertView.setVisibility(View.INVISIBLE);
            return convertView;
        }

        holder.descripcion.setText(rowItem.getDescripcion().toUpperCase());
        if (rowItem.getFecha() != null) {
            holder.fecha.setText(Utiles.getDateFormatDMA().format(rowItem.getFecha()));
        } else {
            holder.fecha.setText("");
        }

        if (rowItem.getVisitas() == 0) {
            holder.nuevo.setVisibility(View.VISIBLE);
        } else {
            holder.nuevo.setVisibility(View.INVISIBLE);
        }

        File fichero = new File (Utiles.getDirectorioCacheVideo(), Utiles.md5(String.valueOf(rowItem.getId())));

        if(fichero.exists()) {
            if(rowItem.getExtension().equalsIgnoreCase("PDF")) {
                holder.id.setImageResource(android.R.drawable.ic_menu_view);
            } else if(rowItem.getExtension().equalsIgnoreCase("MP4")) {
                holder.id.setImageResource(android.R.drawable.ic_media_play);
            }
        } else if (rowItem.getExtension() == null || rowItem.getExtension().equalsIgnoreCase("null")){
            holder.id.setImageResource(android.R.drawable.ic_menu_upload);
        } else {
            holder.id.setImageResource(android.R.drawable.ic_menu_save);
        }

        holder.tamano.setText(Utiles.toMB(Long.valueOf(rowItem.getTamano())));

        if(!rowItem.isActivo()) {
            holder.id.setBackgroundResource(android.R.color.holo_red_light);
        } else {
            holder.id.setBackgroundResource(android.R.color.transparent);
        }

        if(rowItem.getCoste() > 0)
            holder.coste.setText(rowItem.getCoste() + " Puntos");
        else
            holder.coste.setText("");

        return convertView;
    }


}
