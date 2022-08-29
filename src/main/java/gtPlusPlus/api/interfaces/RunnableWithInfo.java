package gtPlusPlus.api.interfaces;

public interface RunnableWithInfo<V> extends Runnable {

    public V getInfoData();

    public default Class<?> getInfoDataType() {
        return getInfoData().getClass();
    }
}
