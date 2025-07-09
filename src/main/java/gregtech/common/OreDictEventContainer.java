package gregtech.common;

import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTUtility;

public class OreDictEventContainer {

    public final OreDictionary.OreRegisterEvent mEvent;
    public final OrePrefixes mPrefix;
    public final Materials mMaterial;
    public final String mModID;

    public OreDictEventContainer(OreDictionary.OreRegisterEvent aEvent, OrePrefixes aPrefix, Materials aMaterial,
        String aModID) {
        this.mEvent = aEvent;
        this.mPrefix = aPrefix;
        this.mMaterial = aMaterial;
        this.mModID = ((aModID == null) || (aModID.equals("UNKNOWN")) ? null : aModID);
    }

    public static void registerRecipes(OreDictEventContainer ore) {
        if ((ore.mEvent.Ore == null) || (ore.mEvent.Ore.getItem() == null)
            || (ore.mPrefix == null)
            || (ore.mPrefix.isIgnored(ore.mMaterial))) {
            return;
        }
        if (ore.mEvent.Ore.stackSize != 1) {
            ore.mEvent.Ore.stackSize = 1;
        }

        ore.mPrefix.processOre(
            ore.mMaterial == null ? Materials._NULL : ore.mMaterial,
            ore.mEvent.Name,
            ore.mModID,
            GTUtility.copyAmount(1, ore.mEvent.Ore));
    }
}
