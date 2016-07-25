package miscutil.core.lib;

import miscutil.core.util.aeonbits.owner.Config;

public interface ConfigHandler extends Config{

	@DefaultValue("false")
	boolean debugMode();
	
	@DefaultValue("true")
	boolean disableEnderIOIntegration();
	
	
}
