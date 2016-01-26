package com.m21438255.proyectosnapchat;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;



/**
 * Created by Adrian on 12/01/2016.
 */
public class InboxFragment extends ListFragment {
    ProgressBar spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar3);
        spinner.setVisibility(View.GONE);
        
        return rootView;
    }
}

