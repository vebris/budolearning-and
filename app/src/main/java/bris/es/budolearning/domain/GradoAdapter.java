package bris.es.budolearning.domain;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import bris.es.budolearning.R;
import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentGrados;
import bris.es.budolearning.fragments.FragmentRecursos;
import bris.es.budolearning.slidingtabs.PagerItem;
import bris.es.budolearning.task.TaskGrado;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.UtilesDialog;

public class GradoAdapter extends BaseAdapter {

    private Activity activity;
    private List<Grado> grados;
    private TaskGrado taskGrado;

    public GradoAdapter(List<Grado> grados, Activity activity) {
        this.activity = activity;
        this.grados = grados;
        taskGrado = new TaskGrado(activity, null);
    }

    @Override
    public int getCount() {
        return grados.size();
    }

    @Override
    public Grado getItem(int location) {
        return grados.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView id;
        TextView nombre;
        //TextView descripcion;
        ImageView nuevo;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_grados, null);
            holder = new ViewHolder();
            holder.id = (ImageView) convertView.findViewById(R.id.grado_id);
            holder.nombre = (TextView) convertView.findViewById(R.id.grado_nombre);
            holder.nuevo = (ImageView) convertView.findViewById(R.id.grado_nuevo);
            //holder.descripcion = (TextView) convertView.findViewById(R.id.grado_descripcion);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Grado rowItem = getItem(position);

        boolean nuevo = false;
        /*
        for(Recurso r: rowItem.getRecursos()){
            for(Fichero f: r.getFicheros()) {
                if (f.getVisitas() == 0) {
                    nuevo = true;
                    break;
                }
            }
            if(nuevo) break;
        }
        */

        if(nuevo){
            holder.nuevo.setVisibility(View.VISIBLE);
        } else {
            holder.nuevo.setVisibility(View.INVISIBLE);
        }

        //holder.id.setText("");
        holder.nombre.setText(rowItem.getNombre().toUpperCase());
        //holder.descripcion.setText(rowItem.getDescripcion());

        taskGrado.downloadFile(BLSession.getInstance().getUsuario(), rowItem, holder.id);


        return convertView;
    }


}
