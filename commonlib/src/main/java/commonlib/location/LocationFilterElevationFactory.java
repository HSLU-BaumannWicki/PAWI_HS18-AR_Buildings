package commonlib.location;

public class LocationFilterElevationFactory {
    public LocationFilterElevation getLocationFilterElevationImpl(){
        return new LocationFilterOpenElevation();
    }
}
