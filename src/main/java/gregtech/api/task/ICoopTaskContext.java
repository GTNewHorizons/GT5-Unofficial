package gregtech.api.task;

public interface ICoopTaskContext<T> {

    public boolean shouldYield();

    public void stop(T value);
}
