package heig_vd.sym_labo2.communication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class AsyncSendRequest {

    URLConnection urlConnection;
    private Context context;
    private CommunicationEventListener listener;

    /** Constructeur */
    public AsyncSendRequest(Context context){
        this.context = context;
    }

    public void sendRequest(String request, String url){
        // Check for internet connexion status
        if(!isConnectedToNetwork()){
            Toast.makeText(context, "No Internet Connection, please check network", Toast.LENGTH_SHORT).show();
        }else{
            new requestOperation().execute(request,url);
        }
    }

    public void setCommunicationEventListener(CommunicationEventListener commEventListener){
        this.listener = commEventListener;
    }

    private boolean isConnectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class requestOperation extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = "Erreur ";
            if(strings.length > 0){
                URL url;
                String urlToParse = strings[1];
                String request = strings[0];
                InputStream inputStream;
                HttpURLConnection urlConnection = null;
                try{
                    url = new URL(urlToParse);
                    urlConnection =(HttpURLConnection) url.openConnection();

                    urlConnection.setUseCaches(false);
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("X-Network", "LTE"); //network speed
                    urlConnection.setRequestProperty("Content-Type", "text/plain");

                    //write request in body
                    requestWriter(urlConnection.getOutputStream(), request);
                    //urlConnection.connect();

                    int codeErreur = urlConnection.getResponseCode();

                    if(codeErreur != 200){
                        inputStream = urlConnection.getErrorStream();
                        response += codeErreur + inputStream.toString();
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

        private String requestRead(InputStream inputStream){
            String response = "Erreur de lecture ";
            BufferedInputStream inputStreamReader = new BufferedInputStream(inputStream);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            try{
                int value;
                while((value = inputStreamReader.read(buffer)) != -1){
                    result.write(buffer,0, value);
                }

                response = result.toString("UTF-8");
            }catch(IOException e){
                e.printStackTrace();
            }finally {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

        private void requestWriter(OutputStream outputStream, String req) {
            BufferedOutputStream outputStreamWriter;
            outputStreamWriter = new BufferedOutputStream(outputStream);

            try {
                byte[] reqToByte = req.getBytes();
                outputStreamWriter.write(reqToByte, 0, reqToByte.length);
                outputStreamWriter.flush();

                if (outputStreamWriter != null){
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* in case of use for network avaliablility */
        public boolean isNetworkAvaliable(){
            try{
                int timeOut = 1500;
                InetAddress google = InetAddress.getByName("8.8.8.8");
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(google,53);
                socket.connect(socketAddress);
                socket.close();
                return true;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
