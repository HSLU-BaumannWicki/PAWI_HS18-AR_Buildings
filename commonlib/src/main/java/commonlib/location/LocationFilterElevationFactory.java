package commonlib.location;

public class LocationFilterElevationFactory {
    private static final String GOOGLE_API_KEY = "";

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
