package bris.es.budolearning.domain;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.utiles.Utiles;

public class FicheroEspecialAdapter extends BaseAdapter {

    private Context context;
    private List<VideoEspecial> ficheros;

    public FicheroEspecialAdapter(List<VideoEspecial> ficheros, Context context) {
        this.context = context;
        this.ficheros = ficheros;
    }

    @Override
    public int getCount() {
        if(ficheros == null) return 0;
        return ficheros.size();
    }

    @Override
    public VideoEspecial getItem(int location) {
        return ficheros.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView id;
        TextView tamano;
        TextView fichero;
        TextView club;
        TextView usuario;
        TextView inicio;
        TextView fin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_video_especial, null);
            holder = new ViewHolder();
            holder.id = (ImageView) convertView.findViewById(R.id.fichero_especial_id);
            holder.tamano = (TextView) convertView.findViewById(R.id.fichero_especial_tamano);
            holder.fichero = (TextView) convertView.findViewById(R.id.fichero_especial_fichero);
            holder.club = (TextView) convertView.findViewById(R.id.fichero_especial_club);
            holder.usuario = (TextView) convertView.findViewById(R.id.fichero_especial_usuario);
            holder.inicio = (TextView) convertView.findViewById(R.id.fichero_especial_inicio);
            holder.fin = (TextView) convertView.findViewById(R.id.fichero_especial_fin);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoEspecial rowItem = getItem(position);

        File fichero = new File (Utiles.getDirectorioCacheVideo(), Utiles.md5(String.valueOf(rowItem.getFichero().getId())));

        if(fichero.exists()) {
            holder.id.setImageResource(android.R.drawable.star_big_on);
        } else {
            holder.id.setImageResource(android.R.drawable.star_big_off);
        }

        holder.fichero.setText(rowItem.getFichero().getDescripcion().replaceAll(" - ", "\r\n"));
        holder.tamano.setText(Utiles.toMB(Long.valueOf(rowItem.getFichero().getTamano())));
        holder.club.setText(rowItem.getClub().getDescripcion());
        if(rowItem.getUsuario() != null)
            holder.usuario.setText(rowItem.getUsuario().getApellido1() + " " + rowItem.getUsuario().getApellido2() + ", " + rowItem.getUsuario().getNombre());
        else
            holder.usuario.setText("");
        holder.inicio.setText(Utiles.getDateFormatDMA().format(rowItem.getInicio()));
        holder.fin.setText(Utiles.getDateFormatDMA().format(rowItem.getFin()));


        if(!Utiles.esAdmin()) {
            ((LinearLayout)convertView.findViewById(R.id.linearlayout_video_especial)).removeView(convertView.findViewById(R.id.fichero_especial_club));
            ((LinearLayout)convertView.findViewById(R.id.linearlayout_video_especial)).removeView(convertView.findViewById(R.id.fichero_especial_usuario));
        }

        return convertView;
    }


}
