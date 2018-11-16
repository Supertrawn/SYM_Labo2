package heig_vd.sym_labo2.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import heig_vd.sym_labo2.R;
import heig_vd.sym_labo2.communication.AsyncRequestOperation;
import heig_vd.sym_labo2.communication.AsyncSendRequest;
import heig_vd.sym_labo2.utils.Utils;

public class AsyncActivity extends AppCompatActivity {

    private TextView responseTextView;
    private EditText requestTextView;
    private AsyncSendRequest asyncSendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        setTitle("Async Activity");

        /* Initialisation */
        Button sendRequest  = findViewById(R.id.sendAsync);
        requestTextView     = findViewById(R.id.requestBody);
        responseTextView    = findViewById(R.id.responseBody);

        asyncSendRequest = new AsyncSendRequest(this,
                new AsyncRequestOperation(response -> {

            /* response */
            responseTextView.setText(response);
            return false;
        }));

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm != null) {
            imm.showSoftInput(requestTextView, InputMethodManager.SHOW_IMPLICIT);

            sendRequest.setOnClickListener(view -> {

                // Must get the InputMethodManager again because it is a bad practice to modify external variable inside a lambda
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    String str = requestTextView.getText().toString();
                    if (str.compareTo("") != 0) {
                        try {
                            asyncSendRequest.sendRequest(str, Utils.TXT_URL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(AsyncActivity.this, "Please Fill the request Body", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Utils.displayContactSupportErrorMessage(AsyncActivity.this);
                }
            });
        } else {
            Utils.displayContactSupportErrorMessage(AsyncActivity.this);
        }
    }
}
