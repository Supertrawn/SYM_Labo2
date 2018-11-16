package heig_vd.sym_labo2.communication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class AsyncSendRequest {
    private Context context;
    private ARequestOperation requestOperation;

    public AsyncSendRequest(Context context, ARequestOperation requestOperation) {
        this.context = context;
        this.requestOperation = requestOperation;
    }

    public void sendRequest(String request, String url){
        // Check for internet connexion status
        if(!isConnectedToNetwork()){
            Toast.makeText(context, "No Internet Connection, please check network", Toast.LENGTH_SHORT).show();
        }else{
            requestOperation.execute(request, url);
        }
    }

    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
