package kekztech;

import com.google.common.collect.ImmutableSet;
import common.CommonProxy;
import common.tileentities.*;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * My GT-Meta-IDs are: 13101 - 13500
 *
 * @author kekzdealer
 */
@Mod(
        modid = KekzCore.MODID,
        name = KekzCore.NAME,
        version = KekzCore.VERSION,
        dependencies = "required-after:IC2;"
                + "required-after:gregtech;"
                + "required-after:tectech;"
                + "required-after:Thaumcraft;"
                + "required-after:ThaumicTinkerer;"
                + "after:bartworks;"
                + "after:dreamcraft")
public class KekzCore {

    public static final String NAME = "KekzTech";
    public static final String MODID = "kekztech";
    public static final String VERSION = "GRADLETOKEN_VERSION";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @Mod.Instance("kekztech")
    public static KekzCore instance;

    @SidedProxy(clientSide = "client.ClientProxy", serverSide = "kekztech.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onMissingMapping(FMLMissingMappingsEvent event) {
        List<FMLMissingMappingsEvent.MissingMapping> missingMappings = event.get();

        // intentionally not a static final field to save a bit of ram.
        Set<String> removedBlocks = ImmutableSet.of(
                "kekztech_tfftcasingblock_block",
                "kekztech_tfftmultihatch_block",
                "kekztech_tfftstoragefieldblock1_block",
                "kekztech_tfftstoragefieldblock2_block",
                "kekztech_tfftstoragefieldblock3_block",
                "kekztech_tfftstoragefieldblock4_block",
                "kekztech_tfftstoragefieldblock5_block");

        for (FMLMissingMappingsEvent.MissingMapping mapping : missingMappings) {
            if (mapping.type == GameRegistry.Type.BLOCK) {
                if (removedBlocks.contains(mapping.name)) mapping.ignore();
            }
        }
    }
}
