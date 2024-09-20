package gregtech.api.util;

import java.util.Map;
import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import gregtech.api.interfaces.internal.IGTCraftingRecipe;

public class GTShapelessRecipe extends ShapelessOreRecipe implements IGTCraftingRecipe {

    public final boolean mRemovableByGT, mKeepingNBT, overwriteNBT;
    private final Enchantment[] mEnchantmentsAdded;
    private final int[] mEnchantmentLevelsAdded;

    public GTShapelessRecipe(ItemStack aResult, boolean aRemovableByGT, boolean aKeepingNBT, boolean overwriteNBT,
        Enchantment[] aEnchantmentsAdded, int[] aEnchantmentLevelsAdded, Object... aRecipe) {
        super(aResult, aRecipe);
        mEnchantmentsAdded = aEnchantmentsAdded;
        mEnchantmentLevelsAdded = aEnchantmentLevelsAdded;
        this.overwriteNBT = overwriteNBT;
        mRemovableByGT = aRemovableByGT;
        mKeepingNBT = aKeepingNBT;
    }

    @Override
    public boolean matches(InventoryCrafting aGrid, World aWorld) {
        if (mKeepingNBT) {
            ItemStack tStack = null;
            for (int i = 0; i < aGrid.getSizeInventory(); i++) {
                if (aGrid.getStackInSlot(i) != null) {
                    if (tStack != null) {
                        if ((tStack.hasTagCompound() != aGrid.getStackInSlot(i)
                            .hasTagCompound()) || (tStack.hasTagCompound()
                                && !tStack.getTagCompound()
                                    .equals(
                                        aGrid.getStackInSlot(i)
                                            .getTagCompound())))
                            return false;
                    }
                    tStack = aGrid.getStackInSlot(i);
                }
            }
        }
        return super.matches(aGrid, aWorld);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting aGrid) {
        ItemStack rStack = super.getCraftingResult(aGrid);
        if (rStack != null) {
            // Update the Stack
            GTUtility.updateItemStack(rStack);

            // Keeping NBT
            if (mKeepingNBT) for (int i = 0; i < aGrid.getSizeInventory(); i++) {
                if (aGrid.getStackInSlot(i) != null && aGrid.getStackInSlot(i)
                    .hasTagCompound()) {
                    rStack.setTagCompound(
                        (NBTTagCompound) aGrid.getStackInSlot(i)
                            .getTagCompound()
                            .copy());
                    break;
                }
            }

            // Overwrite NBT
            if (overwriteNBT) {
                for (int i = 0; i < aGrid.getSizeInventory(); i++) {
                    ItemStack item = aGrid.getStackInSlot(i);
                    if (GTUtility.areStacksEqual(item, rStack, true) && item.hasTagCompound()) {
                        NBTTagCompound inputNBT = (NBTTagCompound) item.getTagCompound()
                            .copy();
                        if (rStack.hasTagCompound()) {
                            @SuppressWarnings("unchecked")
                            Set<Map.Entry<String, NBTBase>> set = rStack.getTagCompound().tagMap.entrySet();
                            for (Map.Entry<String, NBTBase> e : set) {
                                inputNBT.setTag(e.getKey(), e.getValue());
                            }
                        }
                        rStack.setTagCompound(inputNBT);
                        break;
                    }
                }
            }

            // Charge Values
            if (GTModHandler.isElectricItem(rStack)) {
                GTModHandler.dischargeElectricItem(rStack, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, true);
                int tCharge = 0;
                for (int i = 0; i < aGrid.getSizeInventory(); i++) tCharge += GTModHandler.dischargeElectricItem(
                    aGrid.getStackInSlot(i),
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE,
                    true,
                    true,
                    true);
                if (tCharge > 0) GTModHandler.chargeElectricItem(rStack, tCharge, Integer.MAX_VALUE, true, false);
            }

            // Add Enchantments
            for (int i = 0; i < mEnchantmentsAdded.length; i++) GTUtility.ItemNBT.addEnchantment(
                rStack,
                mEnchantmentsAdded[i],
                EnchantmentHelper.getEnchantmentLevel(mEnchantmentsAdded[i].effectId, rStack)
                    + mEnchantmentLevelsAdded[i]);

            // Update the Stack again
            GTUtility.updateItemStack(rStack);
        }
        return rStack;
    }

    @Override
    public boolean isRemovable() {
        return mRemovableByGT;
    }
}
