package heig_vd.sym_labo2.communication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * @Class       : AsyncRequestOperation
 * @Author(s)   : Michael Brouchoud, Thomas Lechaire & Kevin Pradervand
 * @Date        : 16.11.2018
 *
 * @Goal        : Compressed data when send/receive request/respond
 *
 * @Comment(s)  : -
 * @See         : ARequestOperation
 */
public class CompressedRequestOperation extends ARequestOperation {
    public CompressedRequestOperation(CommunicationEventListener listener) {
        super(listener);
    }

    @Override
    protected String requestRead(InputStream inputStream) {
        String response = "";
        Inflater inflater = new Inflater(true);
        InflaterInputStream inputStreamReader = new InflaterInputStream(inputStream, inflater);
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
        Deflater deflate = new Deflater(Deflater.DEFAULT_COMPRESSION,true);
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream, deflate);
        try {
            byte[] reqToByte = req.getBytes();
            deflaterOutputStream.write(reqToByte, 0, reqToByte.length);
            deflaterOutputStream.flush();
            deflaterOutputStream.close();
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
        urlConnection.setRequestProperty("X-Network", "CSD");
        urlConnection.setRequestProperty("X-Content-Encoding", "deflate");
        urlConnection.setRequestProperty("Content-Type", "text/plain");
        return urlConnection;
    }
}
