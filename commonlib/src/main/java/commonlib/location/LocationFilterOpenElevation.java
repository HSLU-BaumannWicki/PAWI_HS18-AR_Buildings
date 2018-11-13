package commonlib.location;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LocationFilterOpenElevation implements LocationFilterElevation {

    @Override
    public double getAltitude(double latitude, double longitude) {
        double altitude = -100000;
        if (-90.0 < latitude && latitude < 90.0 && -180 < longitude && longitude < 180){
            altitude = makeElevationRequest(latitude, longitude);
        }
        return altitude;
    }

    private double makeElevationRequest(double latitude, double longitude) {
        double result = -100000;
        try {
            String parameters = String.valueOf(Math.round(latitude * 1000000.0) / 1000000.0) + "," + String.valueOf(Math.round(longitude * 1000000.0) / 1000000.0);
            URL url = new URL("https://api.open-elevation.com/api/v1/lookup?locations=" + parameters);
            String response = null;

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                Log.e("LocationFilterOpenElevation", "HTTPS Error Code " + String.valueOf(responseCode));
            }

            InputStream stream = connection.getInputStream();
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
        }
        catch (MalformedURLException e){
            Log.e("LocationFilterOpenElevation", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
