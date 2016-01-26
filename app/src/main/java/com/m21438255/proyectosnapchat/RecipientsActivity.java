package com.m21438255.proyectosnapchat;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

public class RecipientsActivity extends ActionBarActivity{
    ProgressBar load;
    String[] usernames;
    TextView txt;
    List<ParseUser> mUsers;
    ParseRelation<ParseUser> mFriendsRelation;
    ParseUser mCurrentUser;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);
        load=(ProgressBar) findViewById(R.id.progressBar6);
        load.setVisibility(View.VISIBLE);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    public void onResume(){
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            RecipientsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames);
                    mListView.setAdapter(adapter);
                    load.setVisibility(View.GONE);
                } else {
                    generarDialogo(e.getMessage()).show();

                }
            }
        });
    }

    protected AlertDialog generarDialogo(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
        builder.setTitle(R.string.editFriendsErrorTitle)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
