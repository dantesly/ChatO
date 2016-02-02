package MyTestLogin;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import com.m21438255.proyectosnapchat.R;
import com.m21438255.proyectosnapchat.LoginActivity;
import com.parse.ParseUser;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class TestLogin extends ActivityInstrumentationTestCase2<LoginActivity> {
    private LoginActivity actividad;
    private EditText etext1;
    private EditText etext2;
    private Button login;

    public TestLogin(String pkg, Class<LoginActivity> activityClass) {
        super(pkg, activityClass);
    }
    public TestLogin(){
        super(LoginActivity.class);
    }
    protected void setUp() throws Exception{
        super.setUp();
        actividad = getActivity();
        etext1 = (EditText) actividad.findViewById(R.id.usernameField);
        etext2 = (EditText) actividad.findViewById(R.id.passwordField);
        login = (Button) actividad.findViewById(R.id.button);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLoginValues() {

        if(ParseUser.getCurrentUser()!=null)
            ParseUser.logOut();

        String usuarioPrueba = "Borja";
        String usuarioPassw = "1234abc";

        //on value 1 entry
        TouchUtils.tapView(this, etext1);
        getInstrumentation().sendStringSync(usuarioPrueba);
        // now on value2 entry
        TouchUtils.tapView(this, etext2);
        getInstrumentation().sendStringSync(usuarioPassw);
        // now on Add button
        TouchUtils.clickView(this, login);

        assertNotNull("El usuario esta logueado...", (ParseUser.getCurrentUser()));

    }
}
