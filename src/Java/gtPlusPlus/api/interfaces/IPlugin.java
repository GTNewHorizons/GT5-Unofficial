package gtPlusPlus.api.interfaces;

public interface IPlugin {
	
	public String getPluginName();

	public boolean preInit();
	public boolean init();
	public boolean postInit();
	
}
