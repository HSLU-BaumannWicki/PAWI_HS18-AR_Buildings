package commonlib.location;

public class LocationFilterGoogleMapsElevation implements LocationFilterElevation {
    @Override
    public double getAltitude(double latitude, double longitude) {
        return 0;
    }
}
