package com.detrav.utils;

import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.enums.Materials;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Detrav on 30.10.2016.
 */
public class PortableAnvilInventory extends InventoryBasic {
    public PortableAnvilInventory(ItemStack me,String p_i1561_1_, boolean p_i1561_2_, int p_i1561_3_) {
        super(p_i1561_1_, p_i1561_2_, p_i1561_3_);
        meStack = me;
    }

    ItemStack meStack;

    public void setInventorySlotContents(int slot, ItemStack stack) {
        super.setInventorySlotContents(slot, stack);
        if (super.getStackInSlot(2) != null) return;
        ItemStack gtTool = super.getStackInSlot(0);
        if (gtTool != null) {
            if ((gtTool.getItem() instanceof GT_MetaGenerated_Tool)) {
                Materials mat = GT_MetaGenerated_Tool_01.getPrimaryMaterial(gtTool);
                FluidStack fstack = mat.getMolten(1);
                if (fstack == null) fstack = mat.getFluid(1);
                if (fstack != null) {
                    int fluidID = fstack.getFluidID();
                    ItemStack gtMaterial = super.getStackInSlot(1);
                    if (gtMaterial != null) {
                        GT_Recipe recipe = GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.findRecipe(null, false, 524288L, null, gtMaterial);
                        if (recipe != null) {
                            FluidStack fStack = recipe.getFluidOutput(0);
                            if (fStack.getFluidID() == fluidID) {
                                ItemStack gtCopy = gtTool;
                                //make copy
                                float amount = fStack.amount / 1000f;
                                long maxDamage = GT_MetaGenerated_Tool.getToolMaxDamage(gtCopy);
                                long damage = GT_MetaGenerated_Tool.getToolDamage(gtCopy);
                                if (damage == 0) {
                                    super.setInventorySlotContents(2, null);
                                    return;
                                }
                                float flevel = DetravMetaGeneratedTool01.INSTANCE.getLevel(meStack,mat.mToolQuality);
                                int level = ((int)flevel + 1)*((int)flevel + 1);
                                long repair = (long) (maxDamage * amount * (flevel+1));
                                repair = Math.min(repair,damage);
                                damage -= repair;

                                float delta = ((float)repair) / ((float)maxDamage) / ((float)level) * 2.7f;
                                flevel += delta;
                                DetravMetaGeneratedTool01.INSTANCE.setLevelToItemStack(meStack,mat.mToolQuality,flevel);

                                GT_MetaGenerated_Tool.setToolDamage(gtCopy, damage);
                                if (gtMaterial.stackSize > 1)
                                    gtMaterial.stackSize -= 1;
                                else
                                    super.setInventorySlotContents(1, null);
                                super.setInventorySlotContents(2, gtCopy);
                                super.setInventorySlotContents(0, null);
                                return;
                            }
                        }
                    }
                }
            }
        }
        super.setInventorySlotContents(2, null);
    }
}