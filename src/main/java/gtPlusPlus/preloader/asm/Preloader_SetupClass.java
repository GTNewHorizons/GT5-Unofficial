package gtPlusPlus.preloader.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import gtPlusPlus.preloader.Preloader_Logger;
import java.util.Map;

public class Preloader_SetupClass implements IFMLCallHook {

    @Override
    public Void call() throws Exception {
        Preloader_Logger.INFO("Executing IFMLCallHook");
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}
}
