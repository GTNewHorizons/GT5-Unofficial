package kekztech;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;
import common.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.enums.Mods;

/**
 * My GT-Meta-IDs are: 13101 - 13500
 *
 * @author kekzdealer
 */
@Mod(
        modid = KekzCore.MODID,
        name = KekzCore.NAME,
        version = KekzCore.VERSION,
        dependencies = "required-after:IC2;" + "required-after:gregtech;"
                + "required-after:tectech;"
                + "required-after:Thaumcraft;"
                + "required-after:ThaumicTinkerer;"
                + "after:bartworks;"
                + "after:dreamcraft")
public class KekzCore {

    public static final String NAME = "KekzTech";
    public static final String MODID = Mods.Names.KEKZ_TECH;
    public static final String VERSION = "GRADLETOKEN_VERSION";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @Mod.Instance(Mods.Names.KEKZ_TECH)
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
        Set<String> removedStuff = ImmutableSet.of(
                MODID + ":kekztech_tfftcasingblock_block",
                MODID + ":kekztech_tfftmultihatch_block",
                MODID + ":kekztech_tfftstoragefieldblock1_block",
                MODID + ":kekztech_tfftstoragefieldblock2_block",
                MODID + ":kekztech_tfftstoragefieldblock3_block",
                MODID + ":kekztech_tfftstoragefieldblock4_block",
                MODID + ":kekztech_tfftstoragefieldblock5_block");

        for (FMLMissingMappingsEvent.MissingMapping mapping : missingMappings) {
            if (removedStuff.contains(mapping.name)) mapping.ignore();
            else mapping.warn(); // we don't know what happened. probably warn the user.
        }
    }
}
