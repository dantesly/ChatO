package com.m21438255.proyectosnapchat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {
    private EditText usuario;
    private EditText password;
    private EditText email;
    private Button btnRegistro;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        usuario = (EditText) findViewById(R.id.usernameField);
        password = (EditText) findViewById(R.id.passwordField);
        email = (EditText) findViewById(R.id.emailField);
        btnRegistro = (Button) findViewById(R.id.signupButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    public void onClickRegistro(View v){
        progressBar.setVisibility(View.VISIBLE);

        if(usuario.getText().toString().equals("") || password.getText().toString().equals("")
                || email.getText().toString().equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante");
            dialogo1.setMessage("Ha dejado campos sin completar");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton( "OK", null);
            dialogo1.create();
            dialogo1.show();
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            ParseUser user = new ParseUser();
            user.setUsername(String.valueOf(usuario.getText()));
            user.setPassword(String.valueOf(password.getText()));
            user.setEmail(String.valueOf(email.getText()));

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        e.getMessage();
                        AlertDialog.Builder dialogo = new AlertDialog.Builder(SignUpActivity.this);
                        dialogo.setTitle("Importante");
                        dialogo.setMessage(e.getMessage());
                        dialogo.setCancelable(false);
                        dialogo.setPositiveButton("OK", null);
                        dialogo.create();
                        dialogo.show();
                    }
                }
            });
        }
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
