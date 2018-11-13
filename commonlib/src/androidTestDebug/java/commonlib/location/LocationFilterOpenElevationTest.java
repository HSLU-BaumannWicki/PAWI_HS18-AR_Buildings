package commonlib.location;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocationFilterOpenElevationTest {

    @Test
    public void getAltitude() {
        LocationFilterOpenElevation locationFilterOpenElevation = new LocationFilterOpenElevation();
        double altitude = locationFilterOpenElevation.getAltitude(47.24786,8.46418);
        assertEquals(482, Math.round(altitude));
    }
}