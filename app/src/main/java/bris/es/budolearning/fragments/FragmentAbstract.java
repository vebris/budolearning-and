package bris.es.budolearning.fragments;


import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import bris.es.budolearning.R;

public class FragmentAbstract extends Fragment {

    private String title;

    public static final int VER_VIDEO = 99;

    public static final int CHOOSER_VIDEO =100;
    public static final int CHOOSER_FOTO_CLUB =200;
    public static final int CHOOSER_FOTO_DISCIPLINA =201;
    public static final int CHOOSER_FOTO_GRADO = 202;
    public static final int CHOOSER_FOTO_USUARIO =203;
    public static final int CHOOSER_FOTO_CURSO =204;

    public static final int CHOOSER_PDF_ARTICULO =300;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.menu_atras);
        if(item != null) {
            item.setVisible(false);
            if(this.getActivity() != null){
                this.getActivity().invalidateOptionsMenu();
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.menu_atras:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
