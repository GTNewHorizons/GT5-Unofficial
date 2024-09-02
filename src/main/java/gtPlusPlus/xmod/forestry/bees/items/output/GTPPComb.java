package gtPlusPlus.xmod.forestry.bees.items.output;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import gregtech.api.enums.GTValues;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPCombType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPDropType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPPropolisType;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class GTPPComb extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public GTPPComb() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gtpp.comb");
        GameRegistry.registerItem(this, "gtpp.comb", GTPlusPlus.ID);
    }

    public ItemStack getStackForType(GTPPCombType type) {
        return new ItemStack(this, 1, type.mID);
    }

    public ItemStack getStackForType(GTPPCombType type, int count) {
        return new ItemStack(this, count, type.mID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (GTPPCombType type : GTPPCombType.values()) {
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
        int colour = GTPPCombType.get(stack.getItemDamage())
            .getColours()[0];

        if (pass >= 1) {
            colour = GTPPCombType.get(stack.getItemDamage())
                .getColours()[1];
        }

        return colour;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return GTPPCombType.get(stack.getItemDamage())
            .getName();
    }

    public static void initCombsRecipes() {

        addChemicalRecipe(
            GTPPCombType.DRAGONBLOOD,
            new ItemStack[] { GTModHandler.getModItem(Forestry.ID, "refractoryWax", 1L, 0),
                GTPP_Bees.propolis.getStackForType(GTPPPropolisType.DRAGONBLOOD),
                GTPP_Bees.drop.getStackForType(GTPPDropType.DRAGONBLOOD) },
            new int[] { 3000, 1500, 500 });
        addChemicalRecipe(
            GTPPCombType.FORCE,
            new ItemStack[] { GTModHandler.getModItem(Forestry.ID, "beeswax", 1L, 0),
                GTPP_Bees.propolis.getStackForType(GTPPPropolisType.FORCE),
                GTPP_Bees.drop.getStackForType(GTPPDropType.FORCE) },
            new int[] { 5000, 3000, 1000 });
    }

    public static void addChemicalRecipe(GTPPCombType aInputStack, ItemStack[] aOutputs, int[] aChances) {
        Material aMat = aInputStack.mMaterial;
        long aEU = aMat.vVoltageMultiplier;
        int aTier = Math.max(aMat.vTier / 2, 1);
        GTValues.RA.stdBuilder()
            .itemInputs(aInputStack.getStackForType(aTier))
            .itemOutputs(aOutputs)
            .duration(aTier * 20 * 60)
            .eut(aEU)
            .metadata(CHEMPLANT_CASING_TIER, aTier)
            .addTo(chemicalPlantRecipes);

    }
}
