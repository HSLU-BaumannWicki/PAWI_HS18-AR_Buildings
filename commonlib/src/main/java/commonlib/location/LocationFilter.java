package commonlib.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import commonlib.storage.MeanRingBuffer;

public class LocationFilter implements LocationListener {
    private final static int LOCATION_ACCURACY_IN_METER = 10;
    private List<LocationFilterListener> listeners = new ArrayList<>();
    private MeanRingBuffer<Location> locationMean;
    private final LocationManager locationManager;

    public LocationFilter(MeanRingBuffer locationMean, LocationManager locationManager){
        this.locationManager = locationManager;
        this.locationMean = locationMean;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getAccuracy() <= LOCATION_ACCURACY_IN_METER) {
            Location meanLocation = this.locationMean.getNewMean(location);
            this.listeners.forEach(listener -> listener.onNewLocationUpdate(meanLocation));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void stopLocationFilter(){
        this.locationManager.removeUpdates(this);
    }

    @SuppressLint("MissingPermission")
    public void startLocationFilter(){
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void registerLocationFilterListener(LocationFilterListener listener){
        this.listeners.add(listener);
    }

    public void unregisterLocationFilterListener(LocationFilterListener listener){
        this.listeners.remove(listener);
    }
}
