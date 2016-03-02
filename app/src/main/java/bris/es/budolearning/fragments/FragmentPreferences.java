package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import bris.es.budolearning.R;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;
import bris.es.budolearning.task.TaskUtiles;
import bris.es.budolearning.task.volley.VolleyControler;

public class FragmentPreferences extends PreferenceFragment {

    private TaskUtiles taskUtiles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskUtiles = new TaskUtiles(getActivity(), null);

        addPreferencesFromResource(R.xml.settings_general_info);
        addPreferencesFromResource(R.xml.settings_general_cache);
        addPreferencesFromResource(R.xml.settings_general_download);

        Preference myVersion = findPreference( "preferences_general_version" );
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            myVersion.setSummary(packageInfo.versionName + " (" + packageInfo.versionCode + ")");
        }catch (PackageManager.NameNotFoundException nnfe){
            Log.e("", "");
        }

        myVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                try {
                    PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    taskUtiles.checkVersion(packageInfo.versionCode);
                } catch (PackageManager.NameNotFoundException nnfe) {
                    Log.e("", "", nnfe);
                }
                return true;
            }
        });

        controlPreferencias(findPreference("preferences_url"), "");

        // ------------------
        // CACHE PETICIONES
        // ------------------
        Preference limiteCachePeticiones = findPreference("preferences_limite_cache_peticiones");
        controlPreferencias(limiteCachePeticiones, " MB (" + "%)");
        limiteCachePeticiones.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue + " MB (" + "%)");
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()).edit();
                editor.putString("preferences_limite_cache_peticiones", newValue.toString());
                editor.apply();
                int sizeCache = Integer.parseInt(newValue.toString()) * 1024 * 1024;
                if (sizeCache < 0) sizeCache = Integer.MAX_VALUE;
                Utiles.getConfiguracion(getContext()).setTamanoCachePeticiones(sizeCache);
                VolleyControler.getInstance().setCache(null);
                return VolleyControler.getInstance().initializeQueue();
            }
        });
        Preference limpiarCachePeticiones = findPreference( "preferences_limpiar_cache_peticiones" );
        limpiarCachePeticiones.setSummary(Utiles.tamanoCachePeticiones() + " MB");
        limpiarCachePeticiones.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference pref) {
                UtilesDialog.createQuestionYesNo(getActivity(),
                        "ELIMINAR",
                        "¿ Desea eliminar los temporales ?",
                        "Confirmar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo, int id) {
                                VolleyControler.getInstance().clearCache();
                                pref.setSummary(Utiles.tamanoCachePeticiones() + " MB");
                            }
                        },
                        "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo, int id) {
                            }
                        }
                ).show();
                return true;
            }
        });

        // ------------------
        // CACHE IMAGENES
        // ------------------
        Preference limiteCacheImagenes = findPreference("preferences_limite_cache_imagenes");
        controlPreferencias(limiteCacheImagenes, " MB");
        limiteCacheImagenes.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue + " MB");
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()).edit();
                editor.putString("preferences_limite_cache_imagenes", newValue.toString());
                editor.apply();
                int sizeCache = Integer.parseInt(newValue.toString()) * 1024 * 1024;
                if (sizeCache < 0) sizeCache = Integer.MAX_VALUE;
                Utiles.getConfiguracion(getContext()).setTamanoCacheImagenes(sizeCache);
                VolleyControler.getInstance().createImageCache();
                return true;
            }
        });
        Preference limpiarCacheImagenes = findPreference( "preferences_limpiar_cache_imagenes" );
        limpiarCacheImagenes.setSummary(Utiles.tamanoCacheImagenes() + " MB");
        limpiarCacheImagenes.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference pref) {
                UtilesDialog.createQuestionYesNo(getActivity(),
                        "ELIMINAR",
                        "¿ Desea eliminar los temporales ?",
                        "Confirmar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo, int id) {
                                VolleyControler.getInstance().clearImages();
                                pref.setSummary(Utiles.tamanoCacheImagenes() + " MB");
                            }
                        },
                        "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo, int id) {
                            }
                        }
                ).show();
                return true;
            }
        });


        Preference limiteCacheVideos= findPreference("preferences_limite_cache_videos");
        controlPreferencias(limiteCacheVideos, " MB");

        Preference limpiarCacheVideos = findPreference( "preferences_limpiar_cache_videos" );
        limpiarCacheVideos.setSummary(Utiles.tamanoCacheVideo() + " MB");
        limpiarCacheVideos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference pref) {
                UtilesDialog.createQuestionYesNo(
                        getActivity(),
                        "ELIMINAR",
                        "¿ Desea eliminar los vídeos?",
                        "Confirmar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo, int id) {
                                Utiles.borrarFicheros(null);
                                pref.setSummary(Utiles.tamanoCacheVideo() + " MB");
                            }
                        },
                        "Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo, int id) {
                            }
                        }
                ).show();
                return true;
            }
        });
    }

    private void controlPreferencias(Preference preference, final String aditionalText){
        preference.setSummary(preference.getPreferenceManager().getSharedPreferences().getString(preference.getKey(), getResources().getString(R.string.pref_default_url)) + " " + aditionalText);
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue + " " + aditionalText);
                return true;
            }
        });
    }
}
