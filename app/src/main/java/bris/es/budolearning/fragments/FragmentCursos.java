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
import bris.es.budolearning.task.TaskCurso;
import bris.es.budolearning.task.volley.VolleyControler;

public class FragmentCursos extends FragmentAbstract {

    private TaskCurso taskCurso;
    private ListView mCursosView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cursos, container, false);

        taskCurso = new TaskCurso(getActivity(), this);

        mCursosView = (ListView) view.findViewById(R.id.cursosListView);

        mCursosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int posicion, long arg3) {
                BLSession.getInstance().setCurso(BLSession.getInstance().getCursos().get(posicion));

                final NetworkImageView imageView = new NetworkImageView(getActivity());
                UtilesDialog.createImageMessage(getActivity(), BLSession.getInstance().getCurso().getNombre(), imageView, new UtilesDialog.Listener() {
                    public void cargarImagen() {
                        taskCurso.downloadFile(BLSession.getInstance().getUsuario(), BLSession.getInstance().getCurso(), imageView);
                    }
                }).show();
            }
        });

        if (Utiles.esSoloAdmin()) {
            mCursosView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    String[] items = {"Modificar", "Cargar Imagen"};
                    UtilesDialog.createListDialog(getActivity(), "Opciones", items,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    BLSession.getInstance().setCurso(BLSession.getInstance().getCursos().get(position));
                                    switch (item) {
                                        case 0:
                                            getFragmentManager().beginTransaction()
                                                    .replace(R.id.content_frame, new FragmentCursoDetalle())
                                                    .addToBackStack(this.getClass().getName()).commit();
                                            break;
                                        case 1:
                                            Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                                            intent2.setType("image/*");
                                            getActivity().startActivityForResult(Intent.createChooser(intent2, getActivity().getResources().getString(R.string.select_video_mp4)), FragmentAbstract.CHOOSER_FOTO_CURSO);
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
        inflater.inflate(R.menu.menu, menu);
        visualizarMenus(menu, false, false, false, false, Utiles.esSoloAdmin(), true, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.btn_menu_nuevo:
                BLSession.getInstance().setCurso(null);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentCursoDetalle())
                        .addToBackStack(this.getClass().getName()).commit();
                return true;
            case R.id.btn_menu_recargar:
                Cache cache = VolleyControler.getInstance().getRequestQueue().getCache();
                cache.remove("1:" + taskCurso.getUrl() + TaskCurso.LIST);
                taskCurso.list(BLSession.getInstance().getUsuario(), null, mCursosView);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar(){
        //if(BLSession.getInstance().getCursos() == null || BLSession.getInstance().getCursos().size() == 0 )
        taskCurso.list(BLSession.getInstance().getUsuario(), null, mCursosView);
    }
}
