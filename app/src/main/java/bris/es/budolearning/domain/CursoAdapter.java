package bris.es.budolearning.domain;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.utiles.Utiles;

public class CursoAdapter extends BaseAdapter {

    private Activity activity;
    private List<Curso> cursos;

    public CursoAdapter(List<Curso> clubes, Activity activity) {
        this.activity = activity;
        this.cursos = clubes;
    }

    @Override
    public int getCount() {
        return cursos.size();
    }

    @Override
    public Curso getItem(int location) {
        return cursos.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView id;
        TextView nombre;
        TextView descripcion;
        TextView direccion;
        TextView profesor;
        TextView precio;
        TextView fechaInicio;
        TextView fechaFin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_cursos, null);
            holder = new ViewHolder();
            holder.id = (ImageView) convertView.findViewById(R.id.curso_id);
            holder.nombre = (TextView) convertView.findViewById(R.id.curso_nombre);
            holder.descripcion = (TextView) convertView.findViewById(R.id.curso_descripcion);
            holder.direccion = (TextView) convertView.findViewById(R.id.curso_direccion);
            holder.profesor = (TextView) convertView.findViewById(R.id.curso_profesor);
            holder.precio = (TextView) convertView.findViewById(R.id.curso_precio);
            holder.fechaInicio = (TextView) convertView.findViewById(R.id.curso_inicio);
            holder.fechaFin = (TextView) convertView.findViewById(R.id.curso_fin);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Curso rowItem = getItem(position);

        holder.nombre.setText(rowItem.getNombre());
        holder.descripcion.setText(rowItem.getDescripcion());
        holder.direccion.setText(rowItem.getDireccion());
        holder.profesor.setText(rowItem.getProfesor());
        holder.fechaInicio.setText(Utiles.getDateFormatDMAHM().format(rowItem.getInicio()));
        holder.fechaFin.setText(Utiles.getDateFormatDMAHM().format(rowItem.getFin()));
        holder.precio.setText(rowItem.getPrecios());


        return convertView;
    }
}
