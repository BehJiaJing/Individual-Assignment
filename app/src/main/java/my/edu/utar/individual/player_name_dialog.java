package my.edu.utar.individual;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class player_name_dialog extends AppCompatDialogFragment {
    private EditText editplayerName;
    private Button playerUsernameSubmit;
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.player_name_input, null);

        builder.setView(view);

        editplayerName = view.findViewById(R.id.playerName);
        playerUsernameSubmit = view.findViewById(R.id.playerSubmit);

        playerUsernameSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editplayerName.getText().toString();

                // get the username and points
                if (GameResult.points != 0 && username != null) {
                    GameResult.rankingPointList_finalize.add(GameResult.points);
                    GameResult.rankingNameList_finalize.add(username);
                }

                // Update the ranking (player name, and point)
                GameResult.sorting();
                GameResult.updateRanking(GameResult.context.getApplicationContext());

                getDialog().dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement Example Dialog Listener");
        }
    }

    public interface ExampleDialogListener {
    }
}