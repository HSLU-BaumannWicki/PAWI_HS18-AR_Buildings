package commonlib.location;

import android.location.Location;

import com.jme3.math.Vector3f;

public class LocationDistanceCalculator {
    private Location location;

    private float latitudeDistanceInMetersTo(final Location location){
        Location locationWithSameLongAndAlt = new Location(location);
        locationWithSameLongAndAlt.setLongitude(this.location.getLongitude());
        float negative = 1;
        if(locationWithSameLongAndAlt.getLatitude() > this.location.getLatitude()){
            negative = -1;
        }
        return this.location.distanceTo(locationWithSameLongAndAlt) * negative;
    }

    private float longitudeDistanceInMetersTo(final Location location){
        Location locationWithSameLatAndAlt = new Location(location);
        locationWithSameLatAndAlt.setLatitude(this.location.getLatitude());
        float negative = 1;
        if(locationWithSameLatAndAlt.getLongitude() > this.location.getLongitude()){
            negative = -1;
        }
        return this.location.distanceTo(locationWithSameLatAndAlt) * negative;
    }

    private float heightDistanceInMeters(final Location location){
        return (float) (location.getAltitude() - this.location.getAltitude());
    }

    public Vector3f getDistancesBetween(Location a, Location b){
        this.location = a;
        return new Vector3f(this.latitudeDistanceInMetersTo(b), this.heightDistanceInMeters(b), this.longitudeDistanceInMetersTo(b));
    }
}
