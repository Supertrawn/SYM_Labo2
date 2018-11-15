package heig_vd.sym_labo2.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;

import heig_vd.sym_labo2.R;
import heig_vd.sym_labo2.communication.AsyncSendRequest;
import heig_vd.sym_labo2.model.Authors;
import heig_vd.sym_labo2.utils.Utils;

public class SerialActivity extends AppCompatActivity {

    String req;
    EditText id, firstName, lastName;
    TextView responseBody;
    Button sendButton;
    Gson serializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial);
        setTitle("Serial Activity");

        //To test
        firstName   =  (EditText)   findViewById(R.id.firstName);
        lastName     = (EditText)   findViewById(R.id.lastName);
        id           = (EditText)   findViewById(R.id.id);
        responseBody = (TextView)   findViewById(R.id.responseBody);
        sendButton   = (Button)     findViewById(R.id.sendAsync);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(id, InputMethodManager.SHOW_IMPLICIT);

        id.requestFocus();

        sendButton.setOnClickListener(view -> {
            //hidekeyboard
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            // read request form EditText(s)
            req = serialize(parseInterger(id.getText().toString()),firstName.getText().toString(), lastName.getText().toString());
            AsyncSendRequest request = new AsyncSendRequest(SerialActivity.this);
            request.sendRequest(req, Utils.JSON_URL);

            request.setCommunicationEventListener(response -> {
                Authors authorResponse = serializer.fromJson(response, Authors.class);
                responseBody.setText(authorResponse.display());
                return false;
            });
        });
    }

    public int parseInterger(String futureInt){
        return Integer.parseInt(futureInt);
    }

    public String serialize(int id, String firstName, String lastName){
        /* Serialization with Gson library */
        Authors author = new Authors(id, firstName,lastName);
        serializer = new Gson();
        return serializer.toJson(author);
    }
}
