package bris.es.budolearning.menu.lateral;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bris.es.budolearning.R;


public class CustomMenuListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CustomMenuItem> navDrawerItems;

    public CustomMenuListAdapter(Context context, ArrayList<CustomMenuItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(navDrawerItems.get(position).isVisible()) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.lateral_menu_list_item, null);
            }

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
            //TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

            //if(imgIcon == null || txtTitle == null || txtCount == null) {
            //    return convertView;
            //}

            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
            txtTitle.setText(navDrawerItems.get(position).getTitle());
            // displaying count check whether it set visible or not
            /*if (navDrawerItems.get(position).getCounterVisibility()) {
                txtCount.setText(navDrawerItems.get(position).getCount());
            } else {
                // hide the counter view
                txtCount.setVisibility(View.GONE);
            }
            */

        } else {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.lateral_menu_list_item_vacio, null);
            }
        }
        return convertView;
    }
}