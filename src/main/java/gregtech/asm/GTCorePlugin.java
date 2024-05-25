package gregtech.asm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraftforge.common.config.Configuration;

import com.github.bartimaeusnek.bartworks.ASM.BWCore;
import com.github.bartimaeusnek.bartworks.ASM.BWCoreTransformer;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;

import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import gregtech.mixin.Mixin;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.preloader.asm.Preloader_DummyContainer;
import gtPlusPlus.preloader.asm.transformers.Preloader_Transformer_Handler;

@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE) // Load as late as possible (after fastcraft/OptiFine).
@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({ "com.github.bartimaeusnek.bartworks.ASM", "gtPlusPlus.preloader",
    "gregtech.asm" })
@IFMLLoadingPlugin.Name("GregTech 5 Unofficial core plugin")
@SuppressWarnings("unused") // loaded by FML
public class GTCorePlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    public static final String BWCORE_PLUGIN_NAME = "BartWorks ASM Core Plugin";
    public static File minecraftDir;

    public GTCorePlugin() {
        // Injection Code taken from CodeChickenLib
        if (minecraftDir != null) return; // get called twice, once for IFMLCallHook
        minecraftDir = (File) FMLInjectionData.data()[6];
        // do all the configuration already now...
        new ConfigHandler(new Configuration(new File(new File(minecraftDir, "config"), "bartworks.cfg")));
        BWCoreTransformer.shouldTransform[2] = false;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { BWCoreTransformer.class.getName(), Preloader_Transformer_Handler.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        FMLInjectionData.containers.add(BWCore.class.getName());
        return Preloader_DummyContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        // GT++
        CORE_Preloader.DEV_ENVIRONMENT = !(boolean) data.get("runtimeDeobfuscationEnabled");
        File mcDir = (File) data.get("mcLocation");
        if (mcDir != null && mcDir.exists()) {
            CORE_Preloader.setMinecraftDirectory(mcDir);
        }
        CORE_Preloader.DEBUG_MODE = AsmConfig.debugMode;

        // Bartworks
        if (data.get("runtimeDeobfuscationEnabled") != null) {
            BWCoreTransformer.obfs = (boolean) data.get("runtimeDeobfuscationEnabled");
        }
        if (data.get("coremodList") != null) {
            for (Object o : (ArrayList) data.get("coremodList")) {
                if (o.toString()
                    .contains("MicdoodlePlugin")) {
                    BWCoreTransformer.shouldTransform[2] = ConfigHandler.enabledPatches[2];
                    break;
                }
            }
        }
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
}
