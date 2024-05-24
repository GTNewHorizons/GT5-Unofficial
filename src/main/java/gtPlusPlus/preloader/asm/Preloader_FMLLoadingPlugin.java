package gtPlusPlus.preloader.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.preloader.asm.transformers.Preloader_Transformer_Handler;

@SortingIndex(10097)
@MCVersion(value = "1.7.10")
@IFMLLoadingPlugin.TransformerExclusions("gtPlusPlus.preloader")
@IFMLLoadingPlugin.Name(CORE_Preloader.NAME)
public class Preloader_FMLLoadingPlugin implements IFMLLoadingPlugin {

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { Preloader_Transformer_Handler.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return Preloader_DummyContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        CORE_Preloader.DEV_ENVIRONMENT = !(boolean) data.get("runtimeDeobfuscationEnabled");
        File mcDir = (File) data.get("mcLocation");
        if (mcDir != null && mcDir.exists()) {
            CORE_Preloader.setMinecraftDirectory(mcDir);
        }
        CORE_Preloader.DEBUG_MODE = AsmConfig.debugMode;
    }
}
