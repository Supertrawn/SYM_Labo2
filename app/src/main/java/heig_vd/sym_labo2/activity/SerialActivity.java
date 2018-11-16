package heig_vd.sym_labo2.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import heig_vd.sym_labo2.R;
import heig_vd.sym_labo2.communication.AsyncRequestOperation;
import heig_vd.sym_labo2.communication.AsyncSendRequest;
import heig_vd.sym_labo2.model.Authors;
import heig_vd.sym_labo2.utils.Utils;

/**
 * @Class       : SerialActivity
 * @Author(s)   : Michael Brouchoud, Thomas Lechaire & Kevin Pradervand
 * @Date        : 16.11.2018
 *
 * @Goal        : Async Serial Request Activity Test
 *
 * @Comment(s)  : -
 * @See         : AppCompatActivity
 */
public class SerialActivity extends AppCompatActivity {

    private String req;
    private EditText id, firstName, lastName;
    private TextView responseBody;
    private Gson serializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial);
        setTitle("Serial Activity");

        /* Initialisation */
        Button sendButton   = findViewById(R.id.sendAsync);
        firstName           = findViewById(R.id.firstName);
        lastName            = findViewById(R.id.lastName);
        id                  = findViewById(R.id.id);
        responseBody        = findViewById(R.id.responseBody);

        final InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm != null) {
            imm.showSoftInput(id, InputMethodManager.SHOW_IMPLICIT);

            id.requestFocus();

            sendButton.setOnClickListener(view -> {

                //hidekeyboard
                // Must get the InputMethodManager again because it is a bad practice
                // to modify external variable inside a lambda
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    // read request form EditText(s)
                    req = serialize(parseInteger(
                            id.getText().toString()),
                            firstName.getText().toString(),
                            lastName.getText().toString());

                    AsyncSendRequest asyncSendRequest = new AsyncSendRequest(
                            SerialActivity.this,
                                    new AsyncRequestOperation(response -> {
                        Authors authorResponse = serializer.fromJson(response, Authors.class);
                        responseBody.setText(authorResponse.display());
                    }));
                    asyncSendRequest.sendRequest(req, Utils.JSON_URL);
                }
                else {
                    Utils.displayContactSupportErrorMessageToast(SerialActivity.this);
                }
            });
        } else {
            Utils.displayContactSupportErrorMessageToast(SerialActivity.this);
        }
    }

    public int parseInteger(String futureInt){
        return Integer.parseInt(futureInt);
    }

    public String serialize(int id, String firstName, String lastName){
        /* Serialization with Gson library */
        Authors author = new Authors(id, firstName,lastName);
        serializer = new Gson();
        return serializer.toJson(author);
    }
}
