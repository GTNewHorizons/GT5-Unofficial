package gtPlusPlus.xmod.forestry.bees.items.output;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

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
import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_DropType;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class GTPP_Drop extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public GTPP_Drop() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gtpp.drop");
        GameRegistry.registerItem(this, "gtpp.drop", GTPlusPlus.ID);
    }

    public ItemStack getStackForType(GTPP_DropType type) {
        return new ItemStack(this, 1, type.mID);
    }

    public ItemStack getStackForType(GTPP_DropType type, int count) {
        return new ItemStack(this, count, type.mID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (GTPP_DropType type : GTPP_DropType.values()) {
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
        this.itemIcon = par1IconRegister.registerIcon("forestry:honeyDrop.0");
        this.secondIcon = par1IconRegister.registerIcon("forestry:honeyDrop.1");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return (pass == 0) ? itemIcon : secondIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int colour = GTPP_DropType.get(stack.getItemDamage()).getColours()[0];

        if (pass >= 1) {
            colour = GTPP_DropType.get(stack.getItemDamage()).getColours()[1];
        }

        return colour;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return GTPP_DropType.get(stack.getItemDamage()).getName();
    }

    private static final int[] sFluidOutputs = new int[] { 144, 136, 128, 120, 112, 104, 96, 88, 80, 72, 64, 48, 32, 16,
            8, 4 };

    public static void initDropsRecipes() {
        ItemStack tDrop;
        Logger.BEES("Processing recipes for " + GTPP_Bees.sDropMappings.size() + " Drops.");
        for (GTPP_DropType aDrop : GTPP_Bees.sDropMappings.values()) {
            tDrop = aDrop.getStackForType(1);
            if (addProcess(
                    tDrop,
                    new FluidStack(aDrop.mMaterial.getFluid(), sFluidOutputs[aDrop.mMaterial.vTier]),
                    aDrop.mMaterial.vTier * 20 * 30,
                    aDrop.mMaterial.vVoltageMultiplier)) {
                Logger.BEES("Added Drop extraction recipe for: " + aDrop.getName());
            } else {
                Logger.BEES("Failed to add Drop extraction recipe for: " + aDrop.getName());
            }
        }
    }

    public static boolean addProcess(ItemStack tDrop, FluidStack aOutput, int aDuration, int aEUt) {
        if (aOutput == null) {
            return false;
        }
        GT_Values.RA.stdBuilder().itemInputs(tDrop).fluidOutputs(aOutput).duration(aDuration * TICKS).eut(aEUt)
                .addTo(fluidExtractionRecipes);
        return true;
    }
}
