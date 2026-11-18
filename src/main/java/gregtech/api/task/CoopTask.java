package gregtech.api.task;

@FunctionalInterface
public interface CoopTask<T> {

    void run(ICoopTaskContext<T> ctx);
}
