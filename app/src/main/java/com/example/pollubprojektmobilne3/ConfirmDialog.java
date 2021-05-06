package com.example.pollubprojektmobilne3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmDialog extends AppCompatDialogFragment {
    private ConfirmDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Usuwanie")
                .setMessage("Czy na pewno chcesz usunąć dane?")
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() { //adding No button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() { //adding yes button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //calling onYesClicked() in context which called dialog
                        listener.onYesClicked();
                    }
                });
        return builder.create();
    }

    //creating interface
    public interface ConfirmDialogListener{
        void onYesClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement ConfirmDialogListener");
        }
    }
}
