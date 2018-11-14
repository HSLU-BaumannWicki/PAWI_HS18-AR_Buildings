package commonlib.location;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class LocationFilterOpenElevation implements LocationFilterElevation  {
    private double altitude;
    private double latitude;
    private double longitude;

    /**
     * Takes in the GPS coordinates and returns the elevation for a that given point
     * @param latitude -90°< x < 90°
     * @param longitude -180° < x < 180°
     * @return Height in metres (-0.0 on error)
     */
    @Override
    public double getAltitude(double latitude, double longitude) {
        double altitude = ELEVATION_ON_ERROR;
        double roundedLatitude = Math.round(latitude * 10000) / 10000.0;
        double roundedLongitude = Math.round(longitude * 10000) / 10000.0;
        if (-90.0 < latitude && latitude < 90.0 && -180 < longitude && longitude < 180){
            if (roundedLatitude == this.latitude && roundedLongitude == this.longitude){
                altitude = this.altitude;
            }
            else {
                this.latitude = roundedLatitude;
                this.longitude = roundedLongitude;
                makeElevationRequest(latitude, longitude);
            }
        }
        return altitude;
    }

    private void makeElevationRequest(double latitude, double longitude) {
        try {
            String parameters = String.valueOf(Math.round(latitude * 1000000) / 1000000.0) + "," + String.valueOf(Math.round(longitude * 1000000) / 1000000.0);
            URL url = new URL("https://api.open-elevation.com/api/v1/lookup?locations=" + parameters);
            AsyncElevationRequest elevationRequest = new AsyncElevationRequest(this);
            elevationRequest.execute(url);
        }
        catch (MalformedURLException e) {
            Log.e("LocationFilterOpenElevation", e.getMessage());
        }
    }

    protected void setAltitude(double altitude){
        this.altitude = altitude;
    }
}
