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
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Articulo;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;
import bris.es.budolearning.task.TaskAbstract;
import bris.es.budolearning.task.TaskArticulo;
import bris.es.budolearning.task.volley.VolleyControler;

public class FragmentArticulos extends FragmentAbstract {

    private FragmentArticulos este;
    private ListView mArticulosDescargadosView;
    private TaskArticulo taskArticulo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        este = this;
        taskArticulo = new bris.es.budolearning.task.TaskArticulo(getActivity(), this);
        View view = inflater.inflate(R.layout.fragment_articulos, container, false);

        mArticulosDescargadosView = (ListView) view.findViewById(R.id.articulosListView);

        mArticulosDescargadosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {
                taskArticulo.downloadFile(BLSession.getInstance().getUsuario(), BLSession.getInstance().getArticulos().get(posicion));
            }
        });
        if (Utiles.esSoloAdmin()) {
            mArticulosDescargadosView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        String[] items = {"Modificar","Insertar documento"};
                        UtilesDialog.createListDialog(este.getActivity(), "Opciones", items,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        BLSession.getInstance().setArticulo(BLSession.getInstance().getArticulos().get(position));
                                        switch (item) {
                                            case 0:
                                                getFragmentManager().beginTransaction()
                                                        .replace(R.id.content_frame, new FragmentArticuloDetalle())
                                                        .addToBackStack(this.getClass().getName()).commit();
                                                break;
                                            case 1:
                                                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                                                intent2.setType("application/pdf");
                                                este.getActivity().startActivityForResult(Intent.createChooser(intent2, este.getActivity().getResources().getString(R.string.select_video_mp4)), FragmentAbstract.CHOOSER_PDF_ARTICULO);
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
        inflater.inflate(R.menu.menu_recargar_anadir, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.menu_nuevo:
                BLSession.getInstance().setArticulo(new Articulo());
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentArticuloDetalle())
                        .addToBackStack(this.getClass().getName()).commit();
                return true;
            case R.id.menu_recargar:
                Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
                cache.remove("1:" + taskArticulo.getUrl() + TaskAbstract.LIST);
                taskArticulo.list(BLSession.getInstance().getUsuario(), null, mArticulosDescargadosView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar(){
        taskArticulo.list(BLSession.getInstance().getUsuario(), null, mArticulosDescargadosView);
    }
}
