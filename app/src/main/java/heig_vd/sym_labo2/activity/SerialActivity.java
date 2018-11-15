package heig_vd.sym_labo2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        firstName  =  (EditText)   findViewById(R.id.firstName);
        lastName  =  (EditText)   findViewById(R.id.lastName);
        id  =  (EditText)   findViewById(R.id.id);
        responseBody =  (TextView)   findViewById(R.id.responseBody);
        sendButton   =  (Button)     findViewById(R.id.sendAsync);



        sendButton.setOnClickListener(view -> {
            // read request form EditText(s)
            req = serialize(parseInterger(id.getText().toString()),firstName.getText().toString(), lastName.getText().toString());
            Log.e("REQ: ", req);

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
