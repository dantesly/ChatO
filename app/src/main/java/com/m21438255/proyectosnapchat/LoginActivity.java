package com.m21438255.proyectosnapchat;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity {
    private EditText usuario;
    private EditText pass;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = (EditText) findViewById(R.id.usernameField);
        pass = (EditText) findViewById(R.id.passwordField);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void signUpOnClick(View v){
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);
    }

    public void signInOnClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        ParseUser.logInInBackground(usuario.getText().toString(), pass.getText().toString(),
                new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder dialogo2 = new AlertDialog.Builder(LoginActivity.this);
                    dialogo2.setTitle("Importante");
                    dialogo2.setMessage("Usuario y/o contraseña inválidos");
                    dialogo2.setCancelable(false);
                    dialogo2.setPositiveButton("OK", null);
                    dialogo2.create();
                    dialogo2.show();
                }
            }
        });
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    //commit
}
