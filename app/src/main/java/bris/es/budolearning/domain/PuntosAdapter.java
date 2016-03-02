package bris.es.budolearning.domain;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.utiles.Utiles;

public class PuntosAdapter extends BaseAdapter {

    private Context context;
    private List<Puntos> clubes;

    public PuntosAdapter(List<Puntos> clubes, Context context) {
        this.context = context;
        this.clubes = clubes;
    }

    @Override
    public int getCount() {
        return clubes.size();
    }

    @Override
    public Puntos getItem(int location) {
        return clubes.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView fecha;
        //TextView nombre;
        TextView puntos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_puntos, null);
            holder = new ViewHolder();
            holder.fecha = (TextView) convertView.findViewById(R.id.puntos_fecha);
            //holder.nombre = (TextView) convertView.findViewById(R.id.puntos_nombre);
            holder.puntos = (TextView) convertView.findViewById(R.id.puntos_numero);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Puntos rowItem = getItem(position);
        if(rowItem != null && rowItem.getFecha() != null) {
            String str = rowItem.getTipo() + " " + Utiles.getDateFormatDMA().format(rowItem.getFecha());
            holder.fecha.setText(str);
        } else {
            holder.fecha.setText(rowItem!=null?rowItem.getTipo():"");
        }
        //holder.nombre.setText(rowItem.getFichero()!=null && !rowItem.getFichero().equalsIgnoreCase("null")?rowItem.getFichero():"");
        if(rowItem.getPuntos() < 0)
            holder.puntos.setTextColor(Color.RED);
        else
            holder.puntos.setTextColor(Color.BLUE);
        holder.puntos.setText(String.valueOf(rowItem.getPuntos()));
        return convertView;
    }
}
