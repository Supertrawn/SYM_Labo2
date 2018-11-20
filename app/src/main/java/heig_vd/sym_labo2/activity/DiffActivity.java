package heig_vd.sym_labo2.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import heig_vd.sym_labo2.R;
import heig_vd.sym_labo2.communication.AsyncRequestOperation;
import heig_vd.sym_labo2.communication.AsyncSendRequest;
import heig_vd.sym_labo2.utils.Utils;

/**
 * @Class       : DiffActivity
 * @Author(s)   : Michael Brouchoud, Thomas Lechaire & Kevin Pradervand
 * @Date        : 16.11.2018
 *
 * @Goal        : Differed send Request Activity Test
 *
 * @Comment(s)  : -
 * @See         : AppCompatActivity
 */
public class DiffActivity extends AppCompatActivity {

    private TextView responseTextView;
    private EditText requestTextView;
    private List<String> requestList;
    private boolean threadIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        setTitle("Diff Activity");
        requestList = new ArrayList<>();

        /* Initialisation */
        Button sendRequest  = findViewById(R.id.sendAsync);
        requestTextView     = findViewById(R.id.requestBody);
        responseTextView    = findViewById(R.id.responseBody);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm != null) {
            imm.showSoftInput(requestTextView, InputMethodManager.SHOW_IMPLICIT);

            sendRequest.setOnClickListener(view -> {
                // "{\"x\": \"val1\",\"y\":\"val2\"}";
                String str = requestTextView.getText().toString();
                if (str.compareTo("") != 0) {
                    requestList.add(str);
                    try {
                        // Start thread to wait before send request if not the case
                        if (!threadIsRunning) {
                            threadIsRunning = true;
                            new Thread(new Runnable() {
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo ni = cm.getActiveNetworkInfo();

                                @Override
                                public void run() {
                                    // Check if a network is available
                                    while (ni == null) {
                                        try {
                                            Thread.sleep(10000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                        if(cm != null) {
                                            ni = cm.getActiveNetworkInfo();
                                        }
                                    }

                                    // Send all messages in the requestList
                                    try {
                                        AsyncSendRequest asyncSendRequest;
                                        for (String request : requestList) {
                                            asyncSendRequest = new AsyncSendRequest(DiffActivity.this, new AsyncRequestOperation(response -> {
                                                /* response */
                                                responseTextView.setText(response);
                                                return false;
                                            }));
                                            asyncSendRequest.sendRequest(request, Utils.TXT_URL);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    threadIsRunning = false;
                                    requestList.clear();
                                }
                            }).start();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DiffActivity.this, "Please Fill the request Body", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
