package commonlib.location;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class LocationFilterOpenElevation implements LocationFilterElevation, AsyncElevationRequestCallee {
    private double altitude;
    private double roundedLatitude;
    private double roundedLongitude;
    private final double MINIMUM_LATITUDE = -90.0;
    private final double MAXIMUM_LATITUDE = 90.0;
    private final double MINIMUM_LONGITUDE = -180.0;
    private final double MAXIMUM_LONGITUDE = 180.0;
    private final String OPEN_ELEVATION_REQUEST_URL = "https://api.open-elevation.com/api/v1/lookup?locations=";


    /**
     * Takes in the GPS coordinates and returns the elevation for a that given point
     * @return Height in metres (LocationFilterElevation.ELEVATION_ON_ERROR on error)
     */
    @Override
    public double getAltitude(double latitude, double longitude) {
        double altitude = ELEVATION_ON_ERROR;
        double roundedLatitude = scaleForRateLimiting(latitude);
        double roundedLongitude = scaleForRateLimiting(longitude);
        if (isLongitudeAndLatitudeInRange(latitude, longitude)) {
            if (isRequestAlreadySentForThisRoundedCoordinates(roundedLatitude, roundedLongitude)){
                altitude = this.altitude;
            }
            else {
                this.roundedLatitude = roundedLatitude;
                this.roundedLongitude = roundedLongitude;
                makeElevationRequest(latitude, longitude);
            }
        }
        return altitude;
    }

    private double scaleForRateLimiting(double number) {
        return Math.round(number * 10000) / 10000.0;
    }

    private boolean isRequestAlreadySentForThisRoundedCoordinates(
            double roundedLatitude, double roundedLongitude) {
        boolean returnValue = false;
        if (roundedLatitude == this.roundedLatitude && roundedLongitude == this.roundedLongitude) {
            returnValue = true;
        }
        return returnValue;
    }

    private boolean isLongitudeAndLatitudeInRange(double latitude, double longitude) {
        boolean returnValue = false;
         if (MINIMUM_LATITUDE < latitude && latitude < MAXIMUM_LATITUDE
                 && MINIMUM_LONGITUDE < longitude && longitude < MAXIMUM_LONGITUDE) {
             returnValue = true;
        }
        return returnValue;
    }

    private void makeElevationRequest(double latitude, double longitude) {
        try {
            String parameters = String.valueOf(
                    scaleForAPIRequest(latitude)) + "," + String.valueOf(scaleForAPIRequest(longitude));
            URL url = new URL(OPEN_ELEVATION_REQUEST_URL + parameters);
            AsyncElevationRequest elevationRequest = new AsyncElevationRequest(this);
            elevationRequest.execute(url);
        }
        catch (MalformedURLException e) {
            Log.e("LocationFilterOpenElevation", e.getMessage());
        }
    }

    private double scaleForAPIRequest(double number) {
        return Math.round(number * 1000000) / 1000000.0;
    }

    protected void setAltitude(double altitude){
        this.altitude = altitude;
    }
}
