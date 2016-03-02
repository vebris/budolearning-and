package bris.es.budolearning.domain;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;
import bris.es.budolearning.R;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.task.TaskClub;

public class ClubAdapter extends BaseAdapter {

    public Activity activity;
    private List<Club> clubes;
    private TaskClub taskClub;

    public ClubAdapter(List<Club> clubes, Activity activity) {
        this.activity = activity;
        this.clubes = clubes;
        this.taskClub = new TaskClub(activity, null);
    }

    @Override
    public int getCount() {
        return clubes.size();
    }

    @Override
    public Club getItem(int location) {
        return clubes.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        NetworkImageView id;
        TextView nombre;
        TextView descripcion;
        TextView direccion;
        TextView localidad;
        TextView email;
        TextView telefono;
        TextView web;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater)
                activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_lista_clubes, null);
            holder = new ViewHolder();
            holder.id = (NetworkImageView) convertView.findViewById(R.id.club_id);
            holder.nombre = (TextView) convertView.findViewById(R.id.club_nombre);
            holder.descripcion = (TextView) convertView.findViewById(R.id.club_descripcion);
            holder.direccion = (TextView) convertView.findViewById(R.id.club_direccion);
            holder.localidad = (TextView) convertView.findViewById(R.id.club_localidad);
            holder.email = (TextView) convertView.findViewById(R.id.club_email);
            holder.telefono = (TextView) convertView.findViewById(R.id.club_telefono);
            holder.web = (TextView) convertView.findViewById(R.id.club_web);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Club rowItem = getItem(position);

        taskClub.downloadFile(BLSession.getInstance().getUsuario(), rowItem, holder.id);

        holder.nombre.setText(rowItem.getNombre().toUpperCase());
        holder.descripcion.setText(rowItem.getDescripcion());
        if(rowItem.getDireccion() != null && !rowItem.getDireccion().equalsIgnoreCase("null")) {
            holder.direccion.setText(rowItem.getDireccion());
        } else {
            holder.direccion.setText("");
        }
        if(rowItem.getLocalidad() != null && !rowItem.getLocalidad().equalsIgnoreCase("null")) {
            holder.localidad.setText(rowItem.getLocalidad());
        } else {
            holder.localidad.setText("");
        }
        if(rowItem.getEmail() != null && !rowItem.getEmail().equalsIgnoreCase("null")) {
            holder.email.setText(rowItem.getEmail());
        }else {
            holder.email.setText("");
        }
        if(rowItem.getTelefono() != null && !rowItem.getTelefono().equalsIgnoreCase("null")) {
            holder.telefono.setText(rowItem.getTelefono());
        } else {
            holder.telefono.setText("");
        }
        if(rowItem.getWeb() != null && !rowItem.getWeb().equalsIgnoreCase("null")) {
            holder.web.setText(rowItem.getWeb());
        } else {
            holder.web.setText("");
        }


        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{holder.email.getText().toString()});
                myIntent.putExtra(Intent.EXTRA_SUBJECT, "INFORMACION");
                myIntent.setType("message/rfc822");
                activity.startActivity(Intent.createChooser(myIntent, "Elige un cliente de Email:"));
            }
        });
        holder.web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = holder.web.getText().toString();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(Intent.createChooser(myIntent, "Elige navegador:"));
            }
        });
        holder.telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "tel:" + holder.telefono.getText().toString();
                Intent myIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                activity.startActivity(myIntent);
            }
        });

        return convertView;
    }
}