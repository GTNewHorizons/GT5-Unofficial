package gregtech.api.task;

@FunctionalInterface
public interface CoopTask<T> {

    public void run(ICoopTaskContext<T> ctx);
}
