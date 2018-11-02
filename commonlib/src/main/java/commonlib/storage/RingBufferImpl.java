package commonlib.storage;

import java.util.ArrayList;
import java.util.List;

public abstract class RingBufferImpl<T> implements MeanRingBuffer<T> {
    protected final List<T> buffer;
    protected int head;
    protected int size;

    public RingBufferImpl(final int bufferSize){
        this.head = 0;
        this.size = bufferSize;
        this.buffer = new ArrayList<>();
    }

    @Override
    public void addObjectForMeanCalculation(T newObject) {
        if(this.head >= this.buffer.size()){
            this.buffer.add(newObject);
        }else{
            this.buffer.set(this.head, newObject);
        }
        this.head = (this.head + 1) % this.size;
    }

    @Override
    public T getNewMean(T objectToGetStored) {
        this.addObjectForMeanCalculation(objectToGetStored);
        return this.getNewMean();
    }
}
