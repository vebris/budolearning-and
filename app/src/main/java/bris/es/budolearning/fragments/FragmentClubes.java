package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.android.volley.Cache;
import com.android.volley.toolbox.NetworkImageView;
import bris.es.budolearning.R;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;
import bris.es.budolearning.task.TaskAbstract;
import bris.es.budolearning.task.TaskClub;
import bris.es.budolearning.task.volley.VolleyControler;


public class FragmentClubes extends FragmentAbstract {

    private FragmentClubes este;
    private ListView mClubesListView;
    private TaskClub taskClub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clubes, container, false);
        este = this;
        taskClub = new TaskClub(getActivity(), this);

        mClubesListView = (ListView) view.findViewById(R.id.clubesListView);

        mClubesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {
                BLSession.getInstance().setClub(BLSession.getInstance().getClubes().get(posicion));

                final NetworkImageView imageView = new NetworkImageView(getActivity());
                UtilesDialog.createImageMessage(getActivity(), BLSession.getInstance().getClub().getNombre() ,imageView, new UtilesDialog.Listener() {
                    public void cargarImagen() {
                        taskClub.downloadFile(BLSession.getInstance().getUsuario(), BLSession.getInstance().getClub(), imageView);
                    }
                }).show();
            }
        });

        if (Utiles.esAdmin()) {
            mClubesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    String[] items = {"Modificar", "Cargar Imagen"};
                    UtilesDialog.createListDialog(este.getActivity(), "Opciones", items,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    BLSession.getInstance().setClub(BLSession.getInstance().getClubes().get(position));
                                    switch (item) {
                                        case 0:
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.content_frame, new FragmentClubDetalle())
                                                    .addToBackStack(this.getClass().getName()).commit();
                                            break;
                                        case 1:
                                            Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                                            intent2.setType("image/*");
                                            startActivityForResult(Intent.createChooser(intent2, getResources().getString(R.string.select_video_mp4)), FragmentAbstract.CHOOSER_FOTO_CLUB);
                                            break;
                                    }
                                }
                            }).show();

                    return true;
                }
            });
        }

        recargar();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (Utiles.esSoloAdmin()) {
            inflater.inflate(R.menu.menu_recargar_anadir, menu);
        } else {
            inflater.inflate(R.menu.menu_recargar, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.menu_nuevo:
                BLSession.getInstance().setClub(null);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentClubDetalle())
                        .addToBackStack(this.getClass().getName()).commit();
                return true;
            case R.id.menu_recargar:
                Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
                cache.remove("1:" + taskClub.getUrl() + TaskAbstract.LIST);
                taskClub.list(BLSession.getInstance().getUsuario(), null, mClubesListView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar(){
        taskClub.list(BLSession.getInstance().getUsuario(), null, mClubesListView);
    }
}
