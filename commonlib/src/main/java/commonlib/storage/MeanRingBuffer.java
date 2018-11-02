package commonlib.storage;

public interface MeanRingBuffer<T> {
    public void addObjectForMeanCalculation(T newObject);
    public T getNewMean();
    public T getNewMean(T objectToGetStored);
}
