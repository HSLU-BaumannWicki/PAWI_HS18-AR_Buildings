package eu.kudan.ar.location;

import android.location.Location;

import com.jme3.math.Vector3f;

public class MyLocation extends Location {
    public MyLocation(String provider) {
        super(provider);
    }

    public MyLocation(Location l) {
        super(l);
    }

    public float latitudeDistanceInMetersTo(final Location location){
        Location locationWithSameLongAndAlt = new Location(location);
        locationWithSameLongAndAlt.setLongitude(this.getLongitude());
        return this.distanceTo(locationWithSameLongAndAlt);
    }

    public float longitudeDistanceInMetersTo(final Location location){
        Location locationWithSameLatAndAlt = new Location(location);
        locationWithSameLatAndAlt.setLatitude(this.getLatitude());
        return this.distanceTo(locationWithSameLatAndAlt);
    }

    public float heightDistanceInMeters(final Location location){
        return (float) (this.getAltitude() - location.getAltitude());
    }

    public static Vector3f getDistancesBetween(Location l, Location b){
        MyLocation myLocL = new MyLocation(l);
        return new Vector3f(myLocL.latitudeDistanceInMetersTo(b), myLocL.heightDistanceInMeters(b)-50, myLocL.longitudeDistanceInMetersTo(b));
    }
}
