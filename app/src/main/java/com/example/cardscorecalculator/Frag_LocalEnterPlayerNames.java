package com.example.cardscorecalculator;

import static com.google.android.material.textfield.TextInputLayout.BOX_BACKGROUND_OUTLINE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class Frag_LocalEnterPlayerNames extends Fragment {
    LinearLayout layout_enterNames;
    Context context;
    Button b_addNames;

    public Frag_LocalEnterPlayerNames() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_enter_player_names, container, false);
        context = getContext();

        int numPlayers;
        layout_enterNames = view.findViewById(R.id.layout_enterNames);
        b_addNames = view.findViewById(R.id.b_addNames);
        ArrayList<EditText> inputs = new ArrayList<EditText>();
        ArrayList<String> names = new ArrayList<String>();

        Bundle bundle = getArguments();
        if (bundle != null){
            numPlayers = bundle.getInt("numPlayers");
        } else {
            numPlayers = 4;
        }

        for(int i=0; i < numPlayers; i++){
            EditText editText = new EditText(context, null, 0, R.style.et_filled);
            editText.setHint("Player " + (i + 1));
            editText.setPadding(150, 25, 150, 25);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 30);
            editText.setLayoutParams(lp);

            layout_enterNames.addView(editText);
            inputs.add(editText);
        }

        b_addNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i < inputs.size(); i++) {
                    // TODO: input validation - make sure every field is filled
                    names.add(inputs.get(i).getText().toString());
                }

                LocalScoreboard scores = new LocalScoreboard();
                Intent in = new Intent(context, LocalScoreboard.class);
                in.putExtra("namesArray", names);
                startActivity(in);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}