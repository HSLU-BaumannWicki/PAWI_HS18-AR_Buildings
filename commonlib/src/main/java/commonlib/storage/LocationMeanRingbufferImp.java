package commonlib.storage;


import android.location.Location;

import java.util.OptionalDouble;
import java.util.function.ToDoubleFunction;

public class LocationMeanRingbufferImp extends RingBufferImpl<Location> {

    public LocationMeanRingbufferImp(int bufferSize) {
        super(bufferSize);
    }

    @Override
    public Location getNewMean() {
        final int lastIndex = this.getIndexOneBeforeHead();
        final Location medianLocation = new Location(this.buffer.get(lastIndex));
        this.setLongLatAlt(medianLocation);
        return medianLocation;
    }

    private void setLongLatAlt(Location newLocation){
        this.setAltitude(newLocation);
        this.setLatitude(newLocation);
        this.setLongitude(newLocation);
    }

    private void setLongitude(Location newLocation){
        double averageLongitute = this.getAverage(Location::getLongitude);
        newLocation.setLongitude(averageLongitute);
    }

    private void setLatitude(Location newLocation){
        double averageLatitude = this.getAverage(Location::getLatitude);
        newLocation.setLatitude(averageLatitude);
    }

    private void setAltitude(Location newLocation){
        double averageAltitude = this.getAverage(Location::getAltitude);
        newLocation.setAltitude(averageAltitude);
    }

    private double getAverage(ToDoubleFunction<Location> function){
        OptionalDouble value = this.buffer.stream().mapToDouble(function).average();
        return value.orElse(0);
    }

    private int getIndexOneBeforeHead(){
        return (this.head + this.size - 1) % this.size;
    }
}
