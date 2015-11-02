package co.iyubinest.challengegeofire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity
{

    private Firebase firebase;

    EditText emailEditText;

    EditText passwordEditText;

    private Firebase.ValueResultHandler<Map<String, Object>> registerCallback
        = new Firebase.ValueResultHandler<Map<String, Object>>()
    {

        @Override
        public void onSuccess (Map<String, Object> stringObjectMap)
        {
            finishRegister();
        }

        @Override
        public void onError (FirebaseError firebaseError)
        {
            showError();
        }
    };

    private void showError ()
    {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    private void finishRegister ()
    {
        finish();
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://demomapas.firebaseio.com/");
    }

    public void register (View view)
    {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        firebase.createUser(email, password, registerCallback);
    }

    public static void start (Context context)
    {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
