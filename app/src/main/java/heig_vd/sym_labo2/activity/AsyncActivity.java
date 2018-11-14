package heig_vd.sym_labo2.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import heig_vd.sym_labo2.R;
import heig_vd.sym_labo2.communication.AsyncSendRequest;
import heig_vd.sym_labo2.communication.CommunicationEventListener;

public class AsyncActivity extends AppCompatActivity implements CommunicationEventListener {

    TextView responseTextView;
    EditText requestTextView;
    Button sendRequest;
    AsyncSendRequest asyncSendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        setTitle("Async Activity");

        /* Initialisation */
        sendRequest = (Button) findViewById(R.id.sendAsync);
        requestTextView = (EditText) findViewById(R.id.requestBody);
        responseTextView = (TextView) findViewById(R.id.responseBody);

        asyncSendRequest = new AsyncSendRequest(this);
        asyncSendRequest.setCommunicationEventListener(this);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(requestTextView, InputMethodManager.SHOW_IMPLICIT);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // "{\"x\": \"val1\",\"y\":\"val2\"}";
                String str = requestTextView.getText().toString();
                if(str != null){
                    try {
                        asyncSendRequest.sendRequest(str, "http://sym.iict.ch/rest/txt");
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(AsyncActivity.this, "Please Fill the request Body", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public boolean handleServerResponse(String response) {
        /* response */
        responseTextView.setText(response);

        return false;
    }
}
