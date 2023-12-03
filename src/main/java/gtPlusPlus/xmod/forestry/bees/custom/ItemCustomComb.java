package gtPlusPlus.xmod.forestry.bees.custom;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

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
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class ItemCustomComb extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public ItemCustomComb() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gtpp.comb");
        GameRegistry.registerItem(this, "gtpp.comb", GTPlusPlus.ID);
    }

    public ItemStack getStackForType(CustomCombs type) {
        return new ItemStack(this, 1, type.ordinal());
    }

    public ItemStack getStackForType(CustomCombs type, int count) {
        return new ItemStack(this, count, type.ordinal());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (CustomCombs type : CustomCombs.values()) {
            if (type.showInList) {
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
        int meta = Math.max(0, Math.min(CustomCombs.values().length - 1, stack.getItemDamage()));
        int colour = CustomCombs.values()[meta].getColours()[0];

        if (pass >= 1) {
            colour = CustomCombs.values()[meta].getColours()[1];
        }

        return colour;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return CustomCombs.values()[stack.getItemDamage()].getName();
    }

    public void initCombsRecipes() {
        ItemStack tComb;

        tComb = getStackForType(CustomCombs.SILICON);
        addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Silicon, 1), 30);
        // addProcess(tComb, Materials.Silver, 100);
        // addProcess(tComb, Materials.Galena, 100);

        // Rubbers
        tComb = getStackForType(CustomCombs.RUBBER);
        addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Rubber, 1), 30);
        tComb = getStackForType(CustomCombs.PLASTIC);
        addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plastic, 1), 20);
        tComb = getStackForType(CustomCombs.PTFE);
        addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, GTPP_Bees.PTFE, 1), 10);
        tComb = getStackForType(CustomCombs.PBS);
        addSpecialCent(tComb, GT_OreDictUnificator.get(OrePrefixes.dustTiny, GTPP_Bees.PBS, 1), 5);

        // Fuels
        tComb = getStackForType(CustomCombs.BIOMASS);
        addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropBiomassBlob), 5);
        tComb = getStackForType(CustomCombs.PBS);
        addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropEthanolBlob), 5);

        // Misc Materials
        tComb = getStackForType(CustomCombs.FORCE);
        addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropForceGem), 5);
        tComb = getStackForType(CustomCombs.FLUORINE);
        addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropFluorineBlob), 5);
        tComb = getStackForType(CustomCombs.NIKOLITE);
        addSpecialCent(tComb, ItemUtils.getSimpleStack(GTPP_Bees.dropNikoliteDust), 5);
    }

    public void addSpecialCent(ItemStack tComb, ItemStack aOutput, int chance) {
        GT_Values.RA.stdBuilder().itemInputs(tComb).itemOutputs(aOutput, ItemList.FR_Wax.get(1))
                .outputChances(chance * 100, 3000).duration(6 * SECONDS + 8 * TICKS).eut(5).addTo(centrifugeRecipes);
    }
}
