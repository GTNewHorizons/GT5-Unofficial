package gregtech.common;

import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof OreDictEventContainer other)) {
            return false;
        }
        return mPrefix == other.mPrefix && Objects.equals(mMaterial, other.mMaterial)
            && Objects.equals(mEvent.Name, other.mEvent.Name)
            && stackIdentityEquals(mEvent.Ore, other.mEvent.Ore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mPrefix, mMaterial, mEvent.Name, stackIdentityHash(mEvent.Ore));
    }

    private static boolean stackIdentityEquals(ItemStack left, ItemStack right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return left.getItem() == right.getItem() && left.getItemDamage() == right.getItemDamage();
    }

    private static int stackIdentityHash(ItemStack stack) {
        if (stack == null) {
            return 0;
        }
        Item item = stack.getItem();
        if (item == null) {
            return 0;
        }
        return Item.getIdFromItem(item) * 1_000_001 + stack.getItemDamage();
    }
}
