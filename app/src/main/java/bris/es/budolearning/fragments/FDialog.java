package bris.es.budolearning.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import bris.es.budolearning.R;

public class FDialog extends DialogFragment{

    public static FDialog newInstance(String title, String text) {
        FDialog frag = new FDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("text", text);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(),R.style.CustomDialog)
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("text"))
        .create();
    }
}
