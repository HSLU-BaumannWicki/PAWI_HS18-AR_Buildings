package commonlib.location.rotation;

public interface Rotator<T> {
    public T rotateByRadOverY(float rad);

    T rotateByDegOverY(float deg);

    public T rotateByRadOverX(float rad);
    public T rotateByRadOverZ(float rad);
    public T getRotatingObject();
    public void setRotatingObject(T object);
}
