package gregtech.common.items;

import static gregtech.api.enums.Mods.ExtraBees;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.enums.Mods.MagicBees;
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
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import forestry.api.recipes.RecipeManagers;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;

public class ItemDrop extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public ItemDrop() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gt.drop");
        GameRegistry.registerItem(this, "gt.drop", GregTech.ID);
    }

    public ItemStack getStackForType(DropType type) {
        return new ItemStack(this, 1, type.ordinal());
    }

    public ItemStack getStackForType(DropType type, int count) {
        return new ItemStack(this, count, type.ordinal());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (DropType type : DropType.values()) {
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
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("forestry:honeyDrop.0");
        this.secondIcon = iconRegister.registerIcon("forestry:honeyDrop.1");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return (pass == 0) ? itemIcon : secondIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int meta = Math.max(0, Math.min(DropType.values().length - 1, stack.getItemDamage()));
        int colour = DropType.values()[meta].getColours()[0];

        if (pass >= 1) {
            colour = DropType.values()[meta].getColours()[1];
        }

        return colour;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return DropType.values()[stack.getItemDamage()].getLocalizedName();
    }

    public void initDropsRecipes() {
        ItemStack tDrop;

        tDrop = getStackForType(DropType.OIL);
        RecipeManagers.squeezerManager.addRecipe(
            40,
            new ItemStack[] { tDrop },
            Materials.OilHeavy.getFluid(100L),
            GTModHandler.getModItem(Forestry.ID, "propolis", 1L, 0),
            30);
        tDrop = getStackForType(DropType.COOLANT);
        RecipeManagers.squeezerManager.addRecipe(
            40,
            new ItemStack[] { tDrop },
            GTModHandler.getIC2Coolant(100),
            GTModHandler.getModItem(MagicBees.ID, "propolis", 1L, 0),
            30);
        tDrop = getStackForType(DropType.HOT_COOLANT);
        RecipeManagers.squeezerManager.addRecipe(
            40,
            new ItemStack[] { tDrop },
            new FluidStack(FluidRegistry.getFluid("ic2hotcoolant"), 100),
            GTModHandler.getModItem(MagicBees.ID, "propolis", 1L, 2),
            30);
        tDrop = getStackForType(DropType.SNOW_QUEEN);
        addProcessMV(
            tDrop,
            Materials.FierySteel.getFluid(200L),
            GTModHandler.getModItem(NewHorizonsCoreMod.ID, "SnowQueenBloodDrop", 1L, 0),
            1500,
            48);
        tDrop = getStackForType(DropType.LAPIS);
        RecipeManagers.squeezerManager.addRecipe(
            400,
            new ItemStack[] { tDrop },
            GTModHandler.getIC2Coolant(100),
            GTModHandler.getModItem(MagicBees.ID, "propolis", 1L, 3),
            30);
        tDrop = getStackForType(DropType.HYDRA);
        addProcessMV(
            tDrop,
            Materials.FierySteel.getFluid(50L),
            GTModHandler.getModItem(MagicBees.ID, "propolis", 1L, 2),
            3000,
            8);
        tDrop = getStackForType(DropType.OXYGEN);
        RecipeManagers.squeezerManager.addRecipe(
            400,
            new ItemStack[] { tDrop },
            Materials.LiquidOxygen.getGas(100),
            GTModHandler.getModItem(ExtraBees.ID, "propolis", 1L, 2),
            30);
        tDrop = getStackForType(DropType.ENDERGOO);
        if (HardcoreEnderExpansion.isModLoaded())
            addProcessHV(tDrop, new FluidStack(FluidRegistry.getFluid("endergoo"), 500), GTValues.NI, 1000);
    }

    public void addProcessLV(ItemStack tDrop, FluidStack aOutput, ItemStack aOutput2, int aChance, int aEUt) {
        GTValues.RA.stdBuilder()
            .itemInputs(tDrop)
            .itemOutputs(aOutput2)
            .outputChances(aChance)
            .fluidOutputs(aOutput)
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(aEUt)
            .addTo(fluidExtractionRecipes);
    }

    public void addProcessLV(ItemStack tDrop, FluidStack aOutput, ItemStack aOutput2, int aChance, int aDuration,
        int aEUt) {
        GTValues.RA.stdBuilder()
            .itemInputs(tDrop)
            .itemOutputs(aOutput2)
            .outputChances(aChance)
            .fluidOutputs(aOutput)
            .duration(aDuration)
            .eut(aEUt)
            .addTo(fluidExtractionRecipes);
    }

    public void addProcessMV(ItemStack tDrop, FluidStack aOutput, ItemStack aOutput2, int aChance, int aEUt) {
        GTValues.RA.stdBuilder()
            .itemInputs(tDrop)
            .itemOutputs(aOutput2)
            .outputChances(aChance)
            .fluidOutputs(aOutput)
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(aEUt)
            .addTo(fluidExtractionRecipes);
    }

    public void addProcessHV(ItemStack tDrop, FluidStack aOutput, ItemStack aOutput2, int aChance) {
        GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
        recipeBuilder.itemInputs(tDrop);
        if (aOutput2 != GTValues.NI) {
            recipeBuilder.itemOutputs(aOutput2)
                .outputChances(aChance);
        }
        recipeBuilder.fluidOutputs(aOutput)
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);
    }
}
