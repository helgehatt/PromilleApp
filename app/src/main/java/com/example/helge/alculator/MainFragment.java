package com.example.helge.alculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button addButton = (Button) view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        Button remButton = (Button) view.findViewById(R.id.button_rem);
        remButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_add:
                Dialog dialog = new Dialog();
                dialog.setTargetFragment(this, 1);
                dialog.show(getFragmentManager(), "Dialog");
                break;
            case R.id.button_rem:
                startActivity(new Intent(getActivity().getApplicationContext(), AddDrinkActivity.class));
                break;
        }
    }
}
