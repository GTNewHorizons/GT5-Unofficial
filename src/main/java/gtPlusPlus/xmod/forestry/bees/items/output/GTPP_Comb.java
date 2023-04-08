package gtPlusPlus.xmod.forestry.bees.items.output;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_CombType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_DropType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_PropolisType;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class GTPP_Comb extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public GTPP_Comb() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gtpp.comb");
        GameRegistry.registerItem(this, "gtpp.comb", GTPlusPlus.ID);
    }

    public ItemStack getStackForType(GTPP_CombType type) {
        return new ItemStack(this, 1, type.mID);
    }

    public ItemStack getStackForType(GTPP_CombType type, int count) {
        return new ItemStack(this, count, type.mID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (GTPP_CombType type : GTPP_CombType.values()) {
            if (type.mShowInList) {
                list.add(this.getStackForType(type));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon("forestry:beeCombs.0");
        this.secondIcon = par1IconRegister.registerIcon("forestry:beeCombs.1");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return (pass == 0) ? itemIcon : secondIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int colour = GTPP_CombType.get(stack.getItemDamage()).getColours()[0];

        if (pass >= 1) {
            colour = GTPP_CombType.get(stack.getItemDamage()).getColours()[1];
        }

        return colour;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return GTPP_CombType.get(stack.getItemDamage()).getName();
    }

    public static void initCombsRecipes() {

        addChemicalRecipe(
                GTPP_CombType.DRAGONBLOOD,
                new ItemStack[] { GT_ModHandler.getModItem(Forestry.ID, "refractoryWax", 1L, 0),
                        GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.DRAGONBLOOD),
                        GTPP_Bees.drop.getStackForType(GTPP_DropType.DRAGONBLOOD) },
                new int[] { 3000, 1500, 500 });
        addChemicalRecipe(
                GTPP_CombType.FORCE,
                new ItemStack[] { GT_ModHandler.getModItem(Forestry.ID, "beeswax", 1L, 0),
                        GTPP_Bees.propolis.getStackForType(GTPP_PropolisType.FORCE),
                        GTPP_Bees.drop.getStackForType(GTPP_DropType.FORCE) },
                new int[] { 5000, 3000, 1000 });
    }

    public static void addChemicalRecipe(GTPP_CombType aInputStack, ItemStack[] aOutputs, int[] aChances) {
        Material aMat = aInputStack.mMaterial;
        long aEU = aMat.vVoltageMultiplier;
        int aTier = Math.max(aMat.vTier / 2, 1);
        CORE.RA.addChemicalPlantRecipe(
                new ItemStack[] { aInputStack.getStackForType(aTier), },
                new FluidStack[] {},
                aOutputs,
                new FluidStack[] {},
                aChances,
                aTier * 20 * 60,
                aEU,
                aTier);
    }
}
