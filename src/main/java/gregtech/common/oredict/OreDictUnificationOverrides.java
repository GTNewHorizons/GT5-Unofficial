package gregtech.common.oredict;

import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
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
        if (GTOreDictUnificator.isBlacklisted(registration.stack)) return;

        GTOreDictUnificator.set(registration.prefix, registration.material, registration.stack, true, true);
    }

    public static void finalizeUnification() {
        // In case some recipes were created before the desired ItemStack became canonical
        GTOreDictUnificator.resetUnificationEntries();
        GTRecipe.reInit();
    }
}
