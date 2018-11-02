package commonlib.storage;

public class FloatMeanRingBuffer extends RingBufferImpl<Float> {

    public FloatMeanRingBuffer(final int bufferSize){
        super(bufferSize);
    }
    
    @Override
    public Float getNewMean() {
        this.buffer.sort(Float::compareTo);
        float median;
        if (this.buffer.size() % 2 == 0)
            median = (this.buffer.get(this.buffer.size()/2) + this.buffer.get(this.buffer.size()/2 - 1)) / 2;
        else
            median = this.buffer.get(this.buffer.size()/2);
        return median;
    }
}
