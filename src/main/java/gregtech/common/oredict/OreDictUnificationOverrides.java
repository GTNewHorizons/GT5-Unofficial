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
    private static final HashMap<String, OreDictRegistration> candidates = new HashMap<>();

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

    static void capture(OreDictRegistration registration) {
        if (registration.modId == null) return;
        if (!registration.modId.equals(preferredMods.get(registration.oreName))) return;
        if (!registration.prefix.isUnifiable()) return;

        candidates.put(registration.oreName, registration);
    }

    public static void apply() {
        for (OreDictRegistration registration : candidates.values()) {
            if (GTOreDictUnificator.isBlacklisted(registration.stack)) continue;
            GTOreDictUnificator.set(registration.prefix, registration.material, registration.stack, true, true);
        }

        candidates.clear();
        GTOreDictUnificator.resetUnificationEntries();
        GTRecipe.reInit();
    }
}
