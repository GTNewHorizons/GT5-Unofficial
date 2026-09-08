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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

final class OreDictUnificationOverrides {

    private static final HashMap<String, HashSet<String>> overrides = new HashMap<>();

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
        overrides.put(modId, new HashSet<>(Arrays.asList(oreNames)));
    }

    static boolean contains(String modId, String oreName) {
        HashSet<String> modOverrides = overrides.get(modId);
        return modOverrides != null && modOverrides.contains(oreName);
    }
}
