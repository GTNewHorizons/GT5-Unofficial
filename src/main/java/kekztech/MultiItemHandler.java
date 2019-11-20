package kekztech;

public class MultiItemHandler {
	
	private int itemTypeCapacity = 128;
	private int perTypeCapacity = 1024; 
	
	private boolean locked = true;
	
	public MultiItemHandler() {
		
	}
	
	public void setItemTypeCapacity(int itemTypeCapacity) {
		this.itemTypeCapacity = itemTypeCapacity;
	}
	
	public void setPerTypeCapacity(int perTypeCapacity) {
		this.perTypeCapacity = perTypeCapacity;
	}
	
	/**
	 * Lock internal storage in case Item Server is not running.
	 * 
	 * @param state
	 * 				Lock state.
	 */
	public void setLock(boolean state) {
		locked = state;
	}
	
	public int getItemTypeCapacity() {
		return itemTypeCapacity;
	}
	
	public int getPerTypeCapacity() {
		return perTypeCapacity;
	}
	
	
}
