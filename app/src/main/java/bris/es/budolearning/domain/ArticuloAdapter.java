package bris.es.budolearning.domain;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.utiles.Utiles;

public class ArticuloAdapter extends BaseAdapter {

    private Context context;
    private List<Articulo> articulos;

    public ArticuloAdapter(List<Articulo> articulos, Context context) {
        this.context = context;
        this.articulos = articulos;
    }

    @Override
    public int getCount() {
        return articulos.size();
    }

    @Override
    public Articulo getItem(int location) {
        return articulos.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView fecha;
        TextView titulo;
        TextView autor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_articulos, null);
            holder = new ViewHolder();
            holder.fecha = (TextView) convertView.findViewById(R.id.articulo_fecha);
            holder.titulo = (TextView) convertView.findViewById(R.id.articulo_titulo);
            holder.autor = (TextView) convertView.findViewById(R.id.articulo_autor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Articulo rowItem = getItem(position);

        holder.fecha.setText(Utiles.getDateFormatMA().format(rowItem.getFecha()));
        holder.titulo.setText(rowItem.getTitulo().toUpperCase());
        int color;
        if(!rowItem.getActivo())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                color = context.getResources().getColor(R.color.md_red_500, context.getTheme());
            else
                color = context.getResources().getColor(R.color.md_red_500);
        else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                color = context.getResources().getColor(R.color.color_text_darkgrey, context.getTheme());
            else
                color = context.getResources().getColor(R.color.color_text_darkgrey);
        holder.titulo.setTextColor(color);

        holder.autor.setText(rowItem.getAutor());

        return convertView;
    }
}
