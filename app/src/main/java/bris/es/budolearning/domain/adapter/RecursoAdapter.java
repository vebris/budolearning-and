package bris.es.budolearning.domain.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Recurso;
import bris.es.budolearning.task.TaskRecurso;
import bris.es.budolearning.utiles.BLSession;


public class RecursoAdapter extends BaseAdapter {

    private Activity activity;
    private List<Recurso> recursos;
    private TaskRecurso taskRecurso;

    public RecursoAdapter(List<Recurso> recursos, Activity activity) {
        this.activity = activity;
        this.recursos = recursos;
        taskRecurso = new TaskRecurso(activity, null);
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
        ImageView img;
        TextView id;
        TextView nombre;
        TextView descripcion;
        TextView enPrograma;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Recurso rowItem = getItem(position);

        convertView = mInflater.inflate(R.layout.layout_lista_recursos, null);
        /*
        switch(rowItem.getActivo()){
            case 0:
                convertView = mInflater.inflate(R.layout.layout_lista_recursos_no, null);
                break;
            default:
                if(rowItem.isEnPrograma())
                    convertView = mInflater.inflate(R.layout.layout_lista_recursos, null);
                else
                    convertView = mInflater.inflate(R.layout.layout_lista_recursos_med, null);
                break;
        }
        */
        holder = new ViewHolder();
        holder.img = (ImageView) convertView.findViewById(R.id.recurso_img);
        holder.id = (TextView) convertView.findViewById(R.id.recurso_id);
        holder.nombre = (TextView) convertView.findViewById(R.id.recurso_nombre);
        holder.descripcion = (TextView) convertView.findViewById(R.id.recurso_tipo);
        holder.enPrograma = (TextView) convertView.findViewById(R.id.recurso_enPrograma);
        convertView.setTag(holder);


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

        taskRecurso.downloadFile(BLSession.getInstance().getUsuario(), rowItem, holder.img);

        return convertView;
    }


}
