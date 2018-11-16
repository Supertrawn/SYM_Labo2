package heig_vd.sym_labo2.communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Class       : AsyncRequestOperation
 * @Author(s)   : Michael Brouchoud, Thomas Lechaire & Kevin Pradervand
 * @Date        : 16.11.2018
 *
 * @Goal        : Standard send/receive request/respond
 *
 * @Comment(s)  : -
 * @See         : ARequestOperation
 */
public class AsyncRequestOperation extends ARequestOperation {
    public AsyncRequestOperation(CommunicationEventListener listener) {
        super(listener);
    }

    @Override
    protected String requestRead(InputStream inputStream) {
        String response = "Error when trying to read the respond ";
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

    @Override
    protected void requestWriter(OutputStream outputStream, String req) {
        BufferedOutputStream outputStreamWriter;
        outputStreamWriter = new BufferedOutputStream(outputStream);

        try {
            byte[] reqToByte = req.getBytes();
            outputStreamWriter.write(reqToByte, 0, reqToByte.length);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected HttpURLConnection prepareHttpURLConnection(URL url) throws IOException {
        HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();

        urlConnection.setUseCaches(false);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("X-Network", "LTE"); //network speed
        urlConnection.setRequestProperty("Content-Type", "text/plain");

        return urlConnection;
    }
}
