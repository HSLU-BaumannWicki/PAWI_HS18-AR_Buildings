package commonlib.location;

public class LocationFilterElevationFactory {
    private static final String GOOGLE_API_KEY = "AIzaSyDOqv8SKsh9_tXfxOp0aYXsYFYxkZpWN_Q";

    public LocationFilterElevation getLocationFilterElevationImpl(){
        return this.getGoogleElevation();
    }

    private LocationFilterElevation getGoogleElevation(){
        return new LocationFilterGoogleMapsElevation(GOOGLE_API_KEY);
    }

    private LocationFilterElevation getOpenElevation(){
        return new LocationFilterOpenElevation();
    }
}
