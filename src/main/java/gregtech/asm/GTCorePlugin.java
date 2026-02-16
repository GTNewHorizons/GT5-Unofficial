package gregtech.asm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import com.gtnewhorizon.gtnhmixins.builders.IMixins;

import bartworks.common.configs.Configuration;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import gregtech.api.util.scanner.ScannerConfig;
import gregtech.common.pollution.PollutionConfig;
import gregtech.mixin.Mixin;
import gtPlusPlus.core.config.ASMConfiguration;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({ "gregtech.asm" })
@IFMLLoadingPlugin.Name("GregTech 5 Unofficial core plugin")
public class GTCorePlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    static {
        try {
            ConfigurationManager.registerConfig(ASMConfiguration.class);
            ConfigurationManager.registerConfig(Configuration.class);
            ConfigurationManager.registerConfig(PollutionConfig.class);
            ConfigurationManager.registerConfig(ScannerConfig.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean DEV_ENVIRONMENT;

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        DEV_ENVIRONMENT = !(boolean) data.get("runtimeDeobfuscationEnabled");
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
        return IMixins.getEarlyMixins(Mixin.class, loadedCoreMods);
    }

    public static boolean isDevEnv() {
        return DEV_ENVIRONMENT;
    }

}
