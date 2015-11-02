package co.iyubinest.challengegeofire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity
{

    private Firebase firebase;

    private EditText emailEditText;

    private EditText passwordEditText;

    private Firebase.AuthResultHandler authCallback = new Firebase.AuthResultHandler()
    {

        @Override
        public void onAuthenticated (AuthData authData)
        {
            goMapActivity(authData.getUid());
        }

        @Override
        public void onAuthenticationError (FirebaseError firebaseError)
        {
            showError();
        }
    };

    private void showError ()
    {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    private void goMapActivity (String uid)
    {
        finish();
        MapsActivity.start(this, uid);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://demomapas.firebaseio.com/");

    }

    public void login (View view)
    {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        firebase.authWithPassword(email, password, authCallback);
    }

    public void register (View view)
    {
        RegisterActivity.start(this);
    }

    public static void start (Context context)
    {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
