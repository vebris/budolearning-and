package bris.es.budolearning.domain;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.task.TaskDisciplina;

public class DisciplinaAdapter extends BaseAdapter {

    private Activity activity;
    private List<Disciplina> disciplinas;
    private TaskDisciplina taskDisciplina;

    public DisciplinaAdapter(List<Disciplina> disciplinas, Activity activity) {
        this.activity = activity;
        this.disciplinas = disciplinas;
        taskDisciplina = new TaskDisciplina(activity, null);
    }

    @Override
    public int getCount() {
        return disciplinas.size();
    }

    @Override
    public Disciplina getItem(int location) {
        return disciplinas.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        NetworkImageView id;
        TextView nombre;
        ImageView nuevo;
        //TextView descripcion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_disciplinas, null);
            holder = new ViewHolder();
            holder.id = (NetworkImageView) convertView.findViewById(R.id.disciplina_id);
            holder.nombre = (TextView) convertView.findViewById(R.id.disciplina_nombre);
            holder.nuevo = (ImageView) convertView.findViewById(R.id.disciplina_nuevo);
            //holder.descripcion = (TextView) convertView.findViewById(R.id.disciplina_descripcion);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Disciplina rowItem = getItem(position);

        boolean nuevo = false;
        /*
        for(Grado g: rowItem.getGrados()) {
            for (Recurso r : g.getRecursos()) {
                for (Fichero f : r.getFicheros()) {
                    if (f.getVisitas() == 0) {
                        nuevo = true;
                        break;
                    }
                }
                if (nuevo) break;
            }
            if (nuevo) break;
        }
        */

        if(nuevo){
            holder.nuevo.setVisibility(View.VISIBLE);
        } else {
            holder.nuevo.setVisibility(View.INVISIBLE);
        }

        taskDisciplina.downloadFile(BLSession.getInstance().getUsuario(), rowItem, holder.id);

        holder.nombre.setText(rowItem.getNombre().toUpperCase());

        return convertView;
    }
}
