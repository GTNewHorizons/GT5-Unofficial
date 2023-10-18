package gtPlusPlus.api.interfaces;

public interface RunnableWithInfo<V> extends Runnable {

    V getInfoData();

}
