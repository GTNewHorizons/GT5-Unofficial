package kekztech;

public interface IConduit {
	
	public void setNetwork(ConduitNetworkController network);
	public ConduitNetworkController getNetwork();
	
	/**
	 * Returns the conduit's throughput capacity.
	 * 
	 * @return
	 * 			The conduit's throughput capacity.
	 */
	public Number getCapacity();
	
	/**
	 * Call when the throughput capacity was exceeded while traversing this conduit.
	 */
	public void onOverload();
	
	public boolean testForInputFilter(Object o);
	public boolean testForOutputFilter(Object o);
}
