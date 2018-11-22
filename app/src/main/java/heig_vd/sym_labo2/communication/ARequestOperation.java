package heig_vd.sym_labo2.communication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Class       : ARequestOperation
 * @Author(s)   : Michael Brouchoud, Thomas Lechaire & Kevin Pradervand
 * @Date        : 16.11.2018
 *
 * @Goal        : Common send/receive request/respond
 *
 * @Comment(s)  : -
 * @See         : AsyncTask
 */
public abstract class ARequestOperation extends AsyncTask<String, Void, String> {
    private CommunicationEventListener listener;

    public ARequestOperation(CommunicationEventListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "Error ";
        if(strings.length > 0){
            String urlToParse = strings[1];
            String request = strings[0];
            InputStream inputStream;
            HttpURLConnection urlConnection = null;
            try{
                urlConnection = prepareHttpURLConnection(new URL(urlToParse));

                //write request in body
                requestWriter(urlConnection.getOutputStream(), request);
                Log.d("SEND SIZE:",urlConnection.getHeaderField("Content-length"));
                urlConnection.connect();

                int errorCode = urlConnection.getResponseCode();

                if(errorCode != 200){
                    inputStream = urlConnection.getErrorStream();
                    response += errorCode + inputStream.toString();
                }else{
                    inputStream = urlConnection.getInputStream();
                    Log.d("RECEIVE SIZE:",urlConnection.getHeaderField("Content-length"));
                    response = requestRead(inputStream);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.handleServerResponse(s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    /**
     * @brief Read the respond from the InputStream
     * @param inputStream The request to read
     * @return The respond of the request
     */
    protected abstract String requestRead(InputStream inputStream);

    /**
     * @brief Send request
     * @param outputStream OutputStream to use
     * @param req The request to send
     */
    protected abstract void requestWriter(OutputStream outputStream, String req);

    /**
     * @brief Prepare the prepare HttpURLConnection
     * @param url The url to prepare
     * @return The prepareHttpURLConnection
     * @throws IOException
     */
    protected abstract HttpURLConnection prepareHttpURLConnection(URL url) throws IOException;
}
