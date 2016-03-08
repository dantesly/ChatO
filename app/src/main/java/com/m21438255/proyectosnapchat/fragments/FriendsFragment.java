package com.m21438255.proyectosnapchat.fragments;

/**
 * Created by 21438255 on 13/01/2016.
 */
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.m21438255.proyectosnapchat.ParseConstants;
import com.m21438255.proyectosnapchat.R;
import com.m21438255.proyectosnapchat.adapters.UserAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;


/**
 * Created by Adrian on 12/01/2016.
 */
public class FriendsFragment extends android.support.v4.app.Fragment {
    ProgressBar spinner;
    TextView txt;
    String[] usernames;

    List<ParseUser> mUsers;
    ParseRelation<ParseUser> mFriendsRelation;
    ParseUser mCurrentUser;
    protected GridView mGridView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        txt=(TextView) rootView.findViewById(R.id.empty);

        spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar4);
        spinner.setVisibility(View.GONE);

        mGridView = (GridView)rootView.findViewById(R.id.FriendsGrid);
        TextView emptyTextView = (TextView)rootView.findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyTextView);

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
                    if(mGridView.getAdapter()==null) {
                        UserAdapter adapter = new UserAdapter(getActivity(), mUsers);
                        mGridView.setAdapter(adapter);
                    }else{
                        ((UserAdapter)mGridView.getAdapter()).refill(mUsers);
                    }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.editFriendsErrorTitle)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}

