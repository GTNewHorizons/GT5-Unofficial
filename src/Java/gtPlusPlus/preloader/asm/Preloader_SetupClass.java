package gtPlusPlus.preloader.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLCallHook;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.preloader.Preloader_Logger;

public class Preloader_SetupClass implements IFMLCallHook {

	@Override
	public Void call() throws Exception {
		Preloader_Logger.INFO("Executing IFMLCallHook");
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

}
