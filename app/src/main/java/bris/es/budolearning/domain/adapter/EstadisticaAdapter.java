package bris.es.budolearning.domain.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Estadistica;
import bris.es.budolearning.utiles.Utiles;

public class EstadisticaAdapter extends BaseAdapter {

    private Context context;
    private List<Estadistica> clubes;

    public EstadisticaAdapter(List<Estadistica> clubes, Context context) {
        this.context = context;
        this.clubes = clubes;
    }

    @Override
    public int getCount() {
        return clubes.size();
    }

    @Override
    public Estadistica getItem(int location) {
        return clubes.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView nombre;
        TextView visualizaciones;
        TextView ultimaVisualizacion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_estadisticas, null);
            holder = new ViewHolder();
            holder.nombre = (TextView) convertView.findViewById(R.id.estadisticas_fichero);
            holder.visualizaciones = (TextView) convertView.findViewById(R.id.estadisticas_numero);
            holder.ultimaVisualizacion = (TextView) convertView.findViewById(R.id.estadisticas_ultima_visualizacion);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Estadistica rowItem = getItem(position);

        holder.nombre.setText(rowItem.getDescFichero().toUpperCase());
        holder.visualizaciones.setText(String.valueOf(rowItem.getVisualizaciones()));
        holder.ultimaVisualizacion.setText(Utiles.getDateFormatDMAHM().format(rowItem.getFecha()));

        return convertView;
    }
}
