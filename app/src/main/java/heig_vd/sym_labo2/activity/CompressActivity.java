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
import heig_vd.sym_labo2.communication.AsyncSendRequest;
import heig_vd.sym_labo2.communication.CompressedRequestOperation;
import heig_vd.sym_labo2.utils.Utils;

/**
 * @Class       : CompressActivity
 * @Author(s)   : Michael Brouchoud, Thomas Lechaire & Kevin Pradervand
 * @Date        : 16.11.2018
 *
 * @Goal        : Async Compress Request Activity Test
 *
 * @Comment(s)  : -
 * @See         : AppCompatActivity
 */
public class CompressActivity extends AppCompatActivity {
    private TextView responseTextView;
    private EditText requestTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress);
        setTitle("Compressed Activity");

        /* Initialisation */
        Button sendRequest  = findViewById(R.id.sendAsync);
        requestTextView     = findViewById(R.id.requestBody);
        responseTextView    = findViewById(R.id.responseBody);

        //Keyboard management
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm != null) {
            imm.showSoftInput(requestTextView, InputMethodManager.SHOW_IMPLICIT);

            sendRequest.setOnClickListener(view -> {

                // Must get the InputMethodManager again because it is a bad practice
                // to modify external variable inside a lambda
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    String str = requestTextView.getText().toString();
                    AsyncSendRequest asyncSendRequest = new AsyncSendRequest(this,
                            new CompressedRequestOperation(response -> {
                        /* response */
                        responseTextView.setText(response);
                    }));

                    if (str.compareTo("") != 0) {
                        try {
                            asyncSendRequest.sendRequest(str, Utils.TXT_URL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                       Utils.displayPleaseFillTheRequestBodyToal(CompressActivity.this);
                    }
                } else {
                    Utils.displayContactSupportErrorMessageToast(CompressActivity.this);
                }
            });
        } else {
            Utils.displayContactSupportErrorMessageToast(CompressActivity.this);
        }
    }
}
