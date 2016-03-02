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

public class UsuarioAdapter extends BaseAdapter {

    private Context context;
    private List<Usuario> alumnos;

    public UsuarioAdapter(List<Usuario> alumnos, Context context) {
        this.context = context;
        this.alumnos = alumnos;
    }

    @Override
    public int getCount() {
        return alumnos.size();
    }

    @Override
    public Usuario getItem(int location) {
        return alumnos.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView id;
        TextView nombre;
        TextView email;
        TextView telefono;
        TextView activo;
        TextView puntos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_alumnos, null);
            holder = new ViewHolder();
            holder.id = (ImageView) convertView.findViewById(R.id.alumno_id);
            holder.nombre = (TextView) convertView.findViewById(R.id.alumno_nombre);
            holder.telefono = (TextView) convertView.findViewById(R.id.alumno_telefono);
            holder.email = (TextView) convertView.findViewById(R.id.alumno_email);
            holder.activo = (TextView) convertView.findViewById(R.id.alumno_activo);
            holder.puntos = (TextView) convertView.findViewById(R.id.alumno_puntos);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Usuario rowItem = getItem(position);

        holder.nombre.setText(rowItem.getNombre() + " " + rowItem.getApellido1() + " " + rowItem.getApellido2());
        holder.telefono.setText(rowItem.getTelefono());
        holder.email.setText(rowItem.getMail());
        holder.activo.setText(rowItem.getActivo()!=null&&rowItem.getActivo()?"SI":"NO");
        holder.puntos.setText(String.valueOf(rowItem.getPuntos()));
        return convertView;
    }
}
