package com.detrav.utils;

import appeng.recipes.game.ShapelessRecipe;
import com.detrav.items.DetravMetaGeneratedTool01;
import com.enderio.core.common.OreDict;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.internal.IGT_CraftingRecipe;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Detrav on 02.10.2016.
 */
public class DetravRepairRecipe extends ShapelessRecipe {

    public static DetravRepairRecipe INSTANCE;

    //int recipeSize;
    public DetravRepairRecipe()
    {
        super(DetravMetaGeneratedTool01.INSTANCE.getToolWithStats(0, 1, Materials._NULL, Materials._NULL, null));
        INSTANCE = this;
        //recipeSize = aRecipeSize;
    }


    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        ItemStack stack = inv.getStackInSlot(4);
        int iStack = -1;
        if(stack == null) return false;
        if(stack.getItem() instanceof GT_MetaGenerated_Tool)
            iStack = 4;
        /*
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            stack = inv.getStackInSlot(i);
            if (stack == null) continue;
            if (stack.getItem() instanceof GT_MetaGenerated_Tool) {
                iStack = i;
                break;
            }
        }*/
        if (iStack < 0) return false;
        Materials mat = GT_MetaGenerated_Tool_01.getPrimaryMaterial(stack);
        FluidStack fstack = mat.getMolten(1);
        if(fstack == null) fstack = mat.getFluid(1);
        if(fstack == null) return  false;
        int fluidID = fstack.getFluidID();
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack tStack = inv.getStackInSlot(i);
            if (tStack == null) continue;
            if (i == iStack) continue;
            GT_Recipe recipe = GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.findRecipe(null, false, 524288L, null, tStack);
            if (recipe == null)
                return false;
            FluidStack fStack = recipe.getFluidOutput(0);
            if (fStack.getFluidID() != fluidID)
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = inv.getStackInSlot(4).copy();
        int iStack = 4;
        /*for(int i =0; i<inv.getSizeInventory(); i++)
        {
            stack = inv.getStackInSlot(i);
            if(stack == null) continue;
            if(stack.getItem() instanceof GT_MetaGenerated_Tool)
            {
                iStack = i;
                break;
            }
        }*/
        float amount = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack tStack = inv.getStackInSlot(i);
            if (tStack == null) continue;
            if (i == iStack) continue;
            GT_Recipe recipe = GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.findRecipe(null, false, 524288L, null, tStack);
            FluidStack fStack = recipe.getFluidOutput(0);
            amount += fStack.amount;
        }

        amount /= 1000;

        long maxDamage = GT_MetaGenerated_Tool.getToolMaxDamage(stack);
        long damage = GT_MetaGenerated_Tool.getToolDamage(stack);
        maxDamage = (long)(maxDamage * amount);
        damage -= maxDamage;
        if(damage<0) damage = 0;
        GT_MetaGenerated_Tool.setToolDamage(stack,damage);
        //inv.setInventorySlotContents(iStack,null);
        return stack;
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }
}