package commonlib.location;

import android.os.SystemClock;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationFilterOpenElevationTest {

    @Test
    public void getAltitude() {
        LocationFilterOpenElevation locationFilterOpenElevation = new LocationFilterOpenElevation();
        locationFilterOpenElevation.getAltitude(47.24786,8.46418);
        SystemClock.sleep(20000);
        double altitude = locationFilterOpenElevation.getAltitude(47.24786,8.46418);
        assertEquals(482, Math.round(altitude));
    }
}