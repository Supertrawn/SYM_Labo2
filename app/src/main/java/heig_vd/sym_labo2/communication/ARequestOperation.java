package heig_vd.sym_labo2.communication;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
                urlConnection.connect();

                int errorCode = urlConnection.getResponseCode();

                if(errorCode != 200){
                    inputStream = urlConnection.getErrorStream();
                    response += errorCode + inputStream.toString();
                }else{
                    inputStream = urlConnection.getInputStream();
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

    protected abstract String requestRead(InputStream inputStream);

    protected abstract void requestWriter(OutputStream outputStream, String req);

    protected abstract HttpURLConnection prepareHttpURLConnection(URL url) throws IOException;
}
