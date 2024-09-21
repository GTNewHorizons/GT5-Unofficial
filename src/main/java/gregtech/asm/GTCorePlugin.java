package gregtech.asm;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;

import bartworks.common.configs.Configuration;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import gregtech.mixin.Mixin;
import gtPlusPlus.core.config.ASMConfiguration;
import gtPlusPlus.preloader.PreloaderCore;
import gtPlusPlus.preloader.asm.PreloaderDummyContainer;
import gtPlusPlus.preloader.asm.transformers.Preloader_Transformer_Handler;

@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE) // Load as late as possible (after fastcraft/OptiFine).
@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({ "bartworks.ASM", "gtPlusPlus.preloader", "gregtech.asm" })
@IFMLLoadingPlugin.Name("GregTech 5 Unofficial core plugin")
public class GTCorePlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    static {
        try {
            ConfigurationManager.registerConfig(Configuration.class);
            ConfigurationManager.registerConfig(ASMConfiguration.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    public static File minecraftDir;
    private static Boolean islwjgl3Present = null;

    public GTCorePlugin() {
        // Injection Code taken from CodeChickenLib
        if (minecraftDir != null) return; // get called twice, once for IFMLCallHook
        minecraftDir = (File) FMLInjectionData.data()[6];
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { Preloader_Transformer_Handler.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return PreloaderDummyContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        // GT++
        PreloaderCore.DEV_ENVIRONMENT = !(boolean) data.get("runtimeDeobfuscationEnabled");
        File mcDir = (File) data.get("mcLocation");
        if (mcDir != null && mcDir.exists()) {
            PreloaderCore.setMinecraftDirectory(mcDir);
        }
        PreloaderCore.DEBUG_MODE = ASMConfiguration.debug.debugMode;
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String getMixinConfig() {
        return "mixins.gregtech.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        return Mixin.getEarlyMixins(loadedCoreMods);
    }

    public static boolean islwjgl3Present() {
        if (islwjgl3Present == null) {
            try {
                final String className = "org/lwjgl/system/Platform.class";
                islwjgl3Present = ClassLoader.getSystemClassLoader()
                    .getResource(className) != null;
            } catch (Exception e) {
                islwjgl3Present = Boolean.FALSE;
            }
        }
        return islwjgl3Present;
    }

}
