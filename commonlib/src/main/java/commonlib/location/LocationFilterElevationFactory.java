package commonlib.location;

public class LocationFilterElevationFactory {
    public static LocationFilterElevation getLocationFilterElevationImpl(){
        return new LocationFilterOpenElevation();
    }
}
