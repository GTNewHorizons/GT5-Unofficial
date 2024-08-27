package gregtech.api.enums;

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
import java.util.Map;

public class ManualOreDictTweaks {

    private static final Map<String, Map<String, Boolean>> oredictLookupTable = new HashMap<>();

    private static final String[] gregtech = new String[] { "dustAlumina', 'dustNikolite" };
    private static final String[] enderio = new String[] { "ingotDarkSteel" };
    private static final String[] draconicevolution = new String[] { "dustDraconium" };
    private static final String[] thaumcraft = new String[] {
        "ingotThaumium', 'ingotVoid', 'nuggetThaumium', 'nuggetVoid" };
    private static final String[] projred_core = new String[] { "dustElectrotine" };
    private static final String[] translocator = new String[] { "nuggetDiamond" };
    private static final String[] hardcoreenderexpansion = new String[] { "ingotHeeEndium" };
    private static final String[] avaritia = new String[] { "ingotCosmicNeutronium', 'ingotInfinity" };
    private static final String[] randomthings = new String[] { "stickObsidian" };
    private static final String[] botania = new String[] {
        "ingotElvenElementium', 'ingotManasteel', 'ingotTerrasteel', 'nuggetElvenElementium', 'nuggetManasteel', 'nuggetTerrasteel" };

    private static final String[] modNames = { GregTech.ID, EnderIO.ID, DraconicEvolution.ID, Thaumcraft.ID,
        ProjectRedCore.ID, Translocator.ID, HardcoreEnderExpansion.ID, Avaritia.ID, RandomThings.ID, Botania.ID };
    private static final String[][] array = new String[][] { gregtech, enderio, draconicevolution, thaumcraft,
        projred_core, translocator, hardcoreenderexpansion, avaritia, randomthings, botania };

    static {
        initTweakedValues();
    }

    private static void initTweakedValues() {
        for (int i = 0; i < array.length; i++) {
            HashMap<String, Boolean> modTableLookup = new HashMap<>();
            String name = modNames[i];
            for (String oredict : array[i]) {
                modTableLookup.put(oredict, true);
            }
            oredictLookupTable.put(name, modTableLookup);
        }
    }

    public static boolean shouldOredictBeOverwritten(String modID, String oredict) {
        Map<String, Boolean> modLookupTable = oredictLookupTable.get(modID);
        if (modLookupTable == null) {
            modLookupTable = new HashMap<>();
            modLookupTable.put(oredict, false);
            oredictLookupTable.put(modID, modLookupTable);
            return false;
        }
        Boolean result = modLookupTable.get(oredict);
        if (result == null) {
            modLookupTable.put(oredict, false);
            result = false;
        }
        return result;
    }

}
