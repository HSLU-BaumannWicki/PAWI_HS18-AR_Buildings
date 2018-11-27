package commonlib.location;

public interface LocationFilterElevation {
    double ELEVATION_ON_ERROR = -Double.MAX_VALUE;

    /**
     * Returns the altitude/elevation for a given GPS location.
     * 
     * @return altitude as a double (ELEVATION_ON_ERROR on error)
     */
    double getAltitude(double latitude, double longitude);
}
