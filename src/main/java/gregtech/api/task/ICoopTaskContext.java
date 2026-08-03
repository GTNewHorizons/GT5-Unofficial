package gregtech.api.task;

public interface ICoopTaskContext<T> {

    boolean shouldYield();

    void stop(T value);
}
