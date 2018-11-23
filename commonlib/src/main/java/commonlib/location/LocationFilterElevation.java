package commonlib.location;

public interface LocationFilterElevation {
    double ELEVATION_ON_ERROR = -Double.MAX_VALUE;

    /**
     * Returns the altitude/elevation for a given GPS location.
     *
     * @param latitude
     * @param longitude
     * @return altitude (-0d on error)
     */
    double getAltitude(double latitude, double longitude);
}
