package bris.es.budolearning.domain;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.utiles.BLSession;

public class RecursoAdapter extends BaseAdapter {

    private Context context;
    private List<Recurso> recursos;

    public RecursoAdapter(List<Recurso> recursos, Context context) {
        this.context = context;
        this.recursos = recursos;
    }

    @Override
    public int getCount() {
        return recursos.size();
    }

    @Override
    public Recurso getItem(int location) {
        return recursos.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView id;
        TextView nombre;
        TextView descripcion;
        TextView enPrograma;
        ImageView nuevo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_recursos, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.recurso_id);
            holder.nombre = (TextView) convertView.findViewById(R.id.recurso_nombre);
            holder.descripcion = (TextView) convertView.findViewById(R.id.recurso_tipo);
            holder.enPrograma = (TextView) convertView.findViewById(R.id.recurso_enPrograma);
            holder.nuevo = (ImageView) convertView.findViewById(R.id.recurso_nuevo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Recurso rowItem = getItem(position);

        /*
        if(rowItem.getFicheros().size() == rowItem.getNumFicheros()) {
            holder.id.setText(String.valueOf(rowItem.getNumFicheros()));
            holder.id.setTextColor(convertView.getResources().getColor(R.color.color_text_darkgrey));
        } else {
            holder.id.setText(String.valueOf(rowItem.getFicheros().size()) + "*");
            holder.id.setTextColor(convertView.getResources().getColor(R.color.list_background_pressed));
        }
        */

        int numFicheros;
        if(BLSession.getInstance().getUsuario().getVerPDF()) {
            numFicheros = rowItem.getNumVideos() + rowItem.getNumPdf();
        } else {
            numFicheros = rowItem.getNumVideos();
        }
        holder.id.setText(String.valueOf(numFicheros));
        holder.id.setTextColor(convertView.getResources().getColor(R.color.color_text_darkgrey));


        boolean nuevo = false;
        /*
        for(Fichero f: rowItem.getFicheros()){
            if(f.getVisitas() == 0) {
                nuevo = true;
                break;
            }
        }
        */

        if(nuevo){
            holder.nuevo.setVisibility(View.VISIBLE);
        } else {
            holder.nuevo.setVisibility(View.INVISIBLE);
        }

        if(rowItem.getNombre().startsWith("+"))
            holder.nombre.setText(rowItem.getNombre().replace("+","").toUpperCase());
        else
            holder.nombre.setText(rowItem.getNombre().toUpperCase());

        if(rowItem.getTipo() != null)
            holder.descripcion.setText(rowItem.getTipo().getNombre().toUpperCase());

        if(rowItem.isEnPrograma()) {
            if(rowItem.getNombre().startsWith("+")) {
                holder.enPrograma.setText("Opcional");
            } else {
                holder.enPrograma.setText("En Programa");
            }
        }else {
            holder.enPrograma.setText("Conocimientos");
        }

        return convertView;
    }


}
