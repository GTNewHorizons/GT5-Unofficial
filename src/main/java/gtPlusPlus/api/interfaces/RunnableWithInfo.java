package gtPlusPlus.api.interfaces;

public interface RunnableWithInfo<V> extends Runnable {

	public V getInfoData();
	
	default public Class<?> getInfoDataType() {
		return getInfoData().getClass();
	}
	
	
}
