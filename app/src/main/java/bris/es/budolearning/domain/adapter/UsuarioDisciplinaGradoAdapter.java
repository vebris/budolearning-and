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
import bris.es.budolearning.domain.UsuarioDisciplinaGrado;

public class UsuarioDisciplinaGradoAdapter extends BaseAdapter {

    private Context context;
    private List<UsuarioDisciplinaGrado> elementos;

    public UsuarioDisciplinaGradoAdapter(List<UsuarioDisciplinaGrado> elementos, Context context) {
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public UsuarioDisciplinaGrado getItem(int location) {
        return elementos.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView disciplina;
        TextView grado;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_alumno_grado, null);
            holder = new ViewHolder();
            holder.disciplina = (TextView) convertView.findViewById(R.id.alumno_disciplina);
            holder.grado = (TextView) convertView.findViewById(R.id.alumno_grado);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UsuarioDisciplinaGrado rowItem = getItem(position);

        holder.disciplina.setText(rowItem.getDescDisciplina().toUpperCase());
        holder.grado.setText(rowItem.getDescGrado().toUpperCase());

        return convertView;
    }
}
