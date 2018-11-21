package commonlib.location;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AsyncElevationRequest extends AsyncTask<URL, Void, Double>{
    private AsyncElevationRequestCallee callee;

    public AsyncElevationRequest(AsyncElevationRequestCallee callee){
        this.callee = callee;
    }

    @Override
    protected Double doInBackground(URL... urls) {
        HttpsURLConnection connection;
        double result = LocationFilterElevation.ELEVATION_ON_ERROR;
        try {
            connection = (HttpsURLConnection) urls[0].openConnection();
            connection.setRequestProperty("http.agent", System.getProperty("http.agent"));
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = null;
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                Log.e("LocationFilterOpenElevation", "HTTPS Error Code " + String.valueOf(responseCode));
            }
            else {
                stream = connection.getInputStream();
            }


            if (stream != null){
                JsonParser jsonParser = new JsonFactory().createParser(stream);
                while (jsonParser.nextToken() != JsonToken.END_OBJECT){
                    String name = jsonParser.getCurrentName();
                    if ("elevation".equals(name)){
                        jsonParser.nextToken();
                        result = jsonParser.getDoubleValue();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Double result) {
        if (result != LocationFilterElevation.ELEVATION_ON_ERROR) callee.setAltitude(result);
    }
}
