package bris.es.budolearning.fragments;

import android.os.Bundle;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import bris.es.budolearning.R;

public class FragmentPreferencesEspecial extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_especial);


    }

}
