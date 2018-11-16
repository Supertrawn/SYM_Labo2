package heig_vd.sym_labo2.communication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * @Class       : AsyncSendRequest
 * @Author(s)   : Michael Brouchoud, Thomas Lechaire & Kevin Pradervand
 * @Date        : 16.11.2018
 *
 * @Goal        : Send request
 *
 * @Comment(s)  : -
 * @See         : AsyncTask
 */
public class AsyncSendRequest {
    private Context context;
    private ARequestOperation requestOperation;

    public AsyncSendRequest(Context context, ARequestOperation requestOperation) {
        this.context = context;
        this.requestOperation = requestOperation;
    }

    /**
     * @brief Send request
     * @param request The request to send
     * @param url The url to send the request
     */
    public void sendRequest(String request, String url) throws Exception {
        // Check for internet connexion status
        if(!isConnectedToNetwork()){
            Toast.makeText(
                    context,
                    "No Internet Connection, please check network",
                    Toast.LENGTH_SHORT)
                    .show();
        }else{
            requestOperation.execute(request, url);
        }
    }

    /**
     * @brief Check if the phone is connected to internet before to send the request
     * @return true -> isConnected, false notConnected
     */
    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager == null ?
                null : connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
