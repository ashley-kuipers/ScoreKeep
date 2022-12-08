package com.example.cardscorecalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AlertSportWinner extends AppCompatDialogFragment {
    String text;
    public AlertSportWinner(String textInput){
        text = textInput;
    }

    // creates a dialog that alerts that someone has won sports mode
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        builder.setTitle(text);
        builder.setPositiveButton("OKAY", (dialogInterface, i) -> {
            HeadtoHeadScoreActivity ssa = (HeadtoHeadScoreActivity) getActivity();
            if (ssa != null) {
                ssa.leaveActivity();
            }
        });

        return builder.create();
    }

}
