package commonlib.location;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationFilterGoogleMapsElevationTest {

    @Test
    public void getAltitude() {
        LocationFilterGoogleMapsElevation locationFilterGoogleMapsElevation = new LocationFilterGoogleMapsElevation();
        double altitude = locationFilterGoogleMapsElevation.getAltitude(47.24786,8.46418);
        assertEquals(495, Math.round(altitude));
    }
}