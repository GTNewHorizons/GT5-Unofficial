package gregtech.common.items;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class ItemPropolis extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public ItemPropolis() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gt.propolis");
        GameRegistry.registerItem(this, "gt.propolis", GregTech.ID);
    }

    public ItemStack getStackForType(PropolisType type) {
        return new ItemStack(this, 1, type.ordinal());
    }

    public ItemStack getStackForType(PropolisType type, int count) {
        return new ItemStack(this, count, type.ordinal());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (PropolisType type : PropolisType.values()) {
            if (type.showInList) {
                list.add(this.getStackForType(type));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("forestry:propolis.0");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int meta = Math.max(0, Math.min(PropolisType.values().length - 1, stack.getItemDamage()));
        return PropolisType.values()[meta].getColours();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return PropolisType.values()[stack.getItemDamage()].getName();
    }

    public void initPropolisRecipes() {
        ItemStack tPropolis;

        tPropolis = getStackForType(PropolisType.End);
        addProcessHV(tPropolis, GTModHandler.getModItem(HardcoreEnderExpansion.ID, "end_powder", 1, 0));
        tPropolis = getStackForType(PropolisType.Stardust);
        addProcessHV(tPropolis, GTModHandler.getModItem(HardcoreEnderExpansion.ID, "stardust", 1, 0));
        tPropolis = getStackForType(PropolisType.Ectoplasma);
        addProcessEV(tPropolis, GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EctoplasmaChip", 1, 0));
        tPropolis = getStackForType(PropolisType.Arcaneshard);
        addProcessEV(tPropolis, GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.ArcaneShardChip", 1, 0));
        tPropolis = getStackForType(PropolisType.Dragonessence);
        addProcessIV(tPropolis, GTModHandler.getModItem(HardcoreEnderExpansion.ID, "essence", 16, 0));
        tPropolis = getStackForType(PropolisType.Enderman);
        addProcessIV(tPropolis, GTModHandler.getModItem(HardcoreEnderExpansion.ID, "enderman_head", 1, 0));
        tPropolis = getStackForType(PropolisType.Silverfish);
        addProcessEV(tPropolis, GTModHandler.getModItem(HardcoreEnderExpansion.ID, "silverfish_blood", 1, 0));
        tPropolis = getStackForType(PropolisType.Endium);
        addProcessHV(tPropolis, GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.HeeEndium, 1));
        tPropolis = getStackForType(PropolisType.Fireessence);
        addProcessIV(tPropolis, GTModHandler.getModItem(HardcoreEnderExpansion.ID, "essence", 16, 1));

        // addRecipe(tDrop, aOutput, aOutput2, aChance, aDuration, aEUt);
    }

    public void addProcessHV(ItemStack tPropolis, ItemStack aOutput2) {
        GTValues.RA.stdBuilder()
            .itemInputs(tPropolis)
            .itemOutputs(aOutput2)
            .outputChances(5000)
            .fluidOutputs(FluidRegistry.getFluidStack("endergoo", 100))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);
    }

    public void addProcessEV(ItemStack tPropolis, ItemStack aOutput2) {
        GTValues.RA.stdBuilder()
            .itemInputs(tPropolis)
            .itemOutputs(aOutput2)
            .outputChances(2500)
            .fluidOutputs(FluidRegistry.getFluidStack("endergoo", 200))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);
    }

    public void addProcessIV(ItemStack tPropolis, ItemStack aOutput2) {
        GTValues.RA.stdBuilder()
            .itemInputs(tPropolis)
            .itemOutputs(aOutput2)
            .outputChances(1500)
            .fluidOutputs(FluidRegistry.getFluidStack("endergoo", 300))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidExtractionRecipes);
    }
}
