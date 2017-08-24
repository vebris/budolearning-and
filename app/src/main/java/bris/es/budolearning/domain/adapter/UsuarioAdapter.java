package bris.es.budolearning.domain.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.utiles.Utiles;

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
        TextView version;
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
            holder.version = (TextView) convertView.findViewById(R.id.alumno_version);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Usuario rowItem = getItem(position);
        StringBuffer nombreUsuario = new StringBuffer();
        nombreUsuario.append(rowItem.getNombre());
        nombreUsuario.append(" ");
        nombreUsuario.append(rowItem.getApellido1());
        nombreUsuario.append(" ");
        nombreUsuario.append(rowItem.getApellido2());
        
        holder.nombre.setText(nombreUsuario);
        
        if(Utiles.esSoloAdmin()) {
            if ("USER".equalsIgnoreCase(rowItem.getRol())) {
                holder.nombre.setTextColor(Color.BLACK);
            } else{
                holder.nombre.setTextColor(Color.BLUE);
                if ("ADMINISTRADOR".equalsIgnoreCase(rowItem.getRol())) {
                    nombreUsuario.insert(0, "(A) ");
                    holder.nombre.setText(nombreUsuario);
                }
            }
        }

        holder.telefono.setText(rowItem.getTelefono());
        holder.email.setText(rowItem.getMail());
        holder.activo.setText(rowItem.getActivo()!=null&&rowItem.getActivo()?"SI":"NO");
        holder.version.setText(String.format("%s %.1f","V. ",(double)rowItem.getVersion()/10));

        holder.puntos.setText(String.valueOf(rowItem.getPuntos()));
        return convertView;
    }
}
