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

public class FicherosDescargadosAdapter extends BaseAdapter {

    private Context context;
    private List<File> ficheros;

    public FicherosDescargadosAdapter(List<File> ficheros, Context context) {
        this.context = context;
        this.ficheros = ficheros;
    }

    @Override
    public int getCount() {
        return ficheros.size();
    }

    @Override
    public File getItem(int location) {
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
        TextView descargado;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_ficheros_descargados, null);
            holder = new ViewHolder();
            holder.id = (ImageView) convertView.findViewById(R.id.fichero_descargado_id);
            holder.descripcion = (TextView) convertView.findViewById(R.id.fichero_descargado_descripcion);
            holder.fecha = (TextView) convertView.findViewById(R.id.fichero_descargado_fecha);
            holder.descargado = (TextView) convertView.findViewById(R.id.fichero_descargado_descargado);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File rowItem = getItem(position);
        try {
            Fichero fichero = buscarFichero(rowItem.getName());
            if(fichero == null) {
                Articulo articulo = buscarArticulo(rowItem.getName());
                holder.descripcion.setText(articulo.getTitulo().toUpperCase());
                holder.id.setImageResource(android.R.drawable.ic_menu_view);
            } else {
                holder.descripcion.setText(fichero.getDescripcion().toUpperCase());
                if (fichero.getExtension().equalsIgnoreCase("PDF")) {
                    holder.id.setImageResource(android.R.drawable.ic_menu_view);
                } else if (fichero.getExtension().equalsIgnoreCase("MP4")) {
                    holder.id.setImageResource(R.drawable.ic_media_play);
                }
            }
            holder.fecha.setText(Utiles.getDateFormatDMA().format(rowItem.lastModified()));
            holder.descargado.setText(Utiles.toMB(rowItem.length()));
        }catch(Exception e){
            holder.descripcion.setText(rowItem.getName());
            holder.id.setImageResource(android.R.drawable.stat_notify_error);
            holder.fecha.setText(Utiles.getDateFormatDMA().format(rowItem.lastModified()));
            holder.descargado.setText(Utiles.toMB(rowItem.length()));
        }
        return convertView;
    }

    public static Fichero buscarFichero(String nombreFichero){
        for(Fichero f: BLSession.getInstance().getFicherosDescargados()){
            if(Utiles.md5(String.valueOf(f.getId())).equalsIgnoreCase(nombreFichero)){
                return f;
            }
        }
        return null;
    }

    public static Articulo buscarArticulo(String nombreFichero){
        for(Articulo a: BLSession.getInstance().getArticulos()){
            if(Utiles.md5(String.valueOf(a.getId())).equalsIgnoreCase(nombreFichero)){
                return a;
            }
        }
        return null;
    }
}