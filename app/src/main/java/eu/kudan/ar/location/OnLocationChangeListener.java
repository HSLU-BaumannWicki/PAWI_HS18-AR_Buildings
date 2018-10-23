package eu.kudan.ar.location;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class OnLocationChangeListener implements LocationStorageListener {
    Location lastLocation;
    private LocationFound locationfound;

    public OnLocationChangeListener(LocationFound locationFound){
        this.locationfound = locationFound;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getAccuracy() <= 10) {
            this.lastLocation = location;
            locationfound.locationUpdate(this.lastLocation);
        }
        Log.i("DEBUG", location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("DEBUG", "onStatusChanged " + status);

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("DEBUG", "onProviderEnabled " + provider);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("DEBUG", "onProviderDisabled " + provider);
    }

    public Location getLastLocation(){
        if(this.lastLocation == null){
            return new Location("");
        }
        return this.lastLocation;
    }
}
