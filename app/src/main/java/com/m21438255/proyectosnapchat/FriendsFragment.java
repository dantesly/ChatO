package com.m21438255.proyectosnapchat;

/**
 * Created by 21438255 on 13/01/2016.
 */
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;


/**
 * Created by Adrian on 12/01/2016.
 */
public class FriendsFragment extends ListFragment {
    ProgressBar spinner;
    TextView txt;
    String[] usernames;

    List<ParseUser> mUsers;
    ParseRelation<ParseUser> mFriendsRelation;
    ParseUser mCurrentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        txt=(TextView) rootView.findViewById(R.id.empty);
        spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar4);
        spinner.setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(ParseConstants.MAX_USERS);


        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    mUsers = objects;
                    usernames = new String[objects.size()];
                    int i = 0;
                    for (ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    spinner.setVisibility(View.GONE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            usernames);
                    setListAdapter(adapter);
                    if (mUsers.size()>0){
                        txt.setVisibility(View.GONE);
                    }
                } else {
                    generarDialogo(e.getMessage()).show();

                }
            }
        });
    }
    protected AlertDialog generarDialogo(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
        builder.setTitle(R.string.editFriendsErrorTitle)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}

