package commonlib.storage;

public interface MeanRingBuffer<T> {
    void addObjectForMeanCalculation(T newObject);
    T getNewMean();
    T getNewMean(T objectToGetStored);
}
