package gregtech.common.oredict;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.enums.Mods.MagicBees;
import static gregtech.api.enums.Mods.ProjectRedCore;
import static gregtech.api.enums.Mods.RandomThings;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.Translocator;

import java.util.HashMap;

import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;

public final class OreDictUnificationOverrides {

    // oreName -> modId
    private static final HashMap<String, String> preferredMods = new HashMap<>();
    private static boolean unificationComplete = false;

    static {
        add(GregTech.ID, "dustAlumina");
        add(EnderIO.ID, "ingotDarkSteel");
        add(DraconicEvolution.ID, "dustDraconium");
        add(Thaumcraft.ID, "ingotThaumium", "ingotVoid", "nuggetThaumium", "nuggetVoid");
        add(ProjectRedCore.ID, "dustElectrotine");
        add(Translocator.ID, "nuggetDiamond");
        add(HardcoreEnderExpansion.ID, "ingotHeeEndium");
        add(Avaritia.ID, "ingotCosmicNeutronium", "ingotInfinity");
        add(RandomThings.ID, "stickObsidian");
        add(
            Botania.ID,
            "ingotElvenElementium",
            "ingotManasteel",
            "ingotTerrasteel",
            "nuggetElvenElementium",
            "nuggetManasteel",
            "nuggetTerrasteel");

        // After the OreDict improvements, some items naturally changed the unificated item stack.
        // By default, it's taken by whichever mod who registered an oredict first.
        // Restore the initial unification state so players don't have to deal with useless items
        add(
            GregTech.ID,
            "blockAmber",
            "blockLead",
            "blockMagnesium",
            "blockMithril",
            "blockPlatinum",
            "blockTungsten",
            "ingotBloodInfusedIron",
            "ingotIchorium",
            "nuggetIchorium",
            "rawOreMeteoricIron",
            "stickDesh",
            "cellCreosote");
        add(BartWorks.ID, "blockSalt");
        add(MagicBees.ID, "nuggetEmerald");
    }

    private OreDictUnificationOverrides() {}

    private static void add(String modId, String... oreNames) {
        for (String oreName : oreNames) {
            preferredMods.put(oreName, modId);
        }
    }

    static void handle(OreDictRegistration registration) {
        if (registration.modId == null) return;
        if (!registration.modId.equals(preferredMods.get(registration.oreName))) return;
        if (!registration.prefix.isUnifiable()) return;

        if (unificationComplete) {
            // If you see this log, consider fixing the mod that registered an OreDict entry too late
            GT_FML_LOGGER.warn(
                "Late OreDict override for {} detected after unification completed. Existing recipes may now contain stale items",
                registration.oreName);
        }

        GTOreDictUnificator.set(registration.prefix, registration.material, registration.stack, true, true);
    }

    public static void finalizeUnification() {
        unificationComplete = true;
        GTRecipe.reInit();
    }
}
