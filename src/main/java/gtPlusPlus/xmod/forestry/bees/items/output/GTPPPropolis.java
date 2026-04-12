package gtPlusPlus.xmod.forestry.bees.items.output;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

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
import gtPlusPlus.xmod.forestry.bees.handler.GTPPPropolisType;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class GTPPPropolis extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public GTPPPropolis() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gtpp.propolis");
        GameRegistry.registerItem(this, "gtpp.propolis", GTPlusPlus.ID);
    }

    public ItemStack getStackForType(GTPPPropolisType type) {
        return new ItemStack(this, 1, type.mID);
    }

    public ItemStack getStackForType(GTPPPropolisType type, int count) {
        return new ItemStack(this, count, type.mID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (GTPPPropolisType type : GTPPPropolisType.values()) {
            if (type.mShowInList) {
                list.add(this.getStackForType(type));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon("forestry:propolis.0");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        return GTPPPropolisType.get(stack.getItemDamage())
            .getColours();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return GTPPPropolisType.get(stack.getItemDamage())
            .getLocalizedName();
    }

    public static void initPropolisRecipes() {
        ItemStack tDrop;
        for (GTPPPropolisType aProp : GTPP_Bees.sPropolisMappings.values()) {
            tDrop = aProp.getStackForType(1);
            ItemStack aOutput = aProp.mMaterial.getDust(1);
            if (aOutput == null) {
                continue;
            }
            GTValues.RA.stdBuilder()
                .itemInputs(tDrop)
                .itemOutputs(aOutput)
                .outputChances(Math.min(Math.max(10000 - (aProp.mMaterial.vTier * 625), 100), 10000))
                .duration(aProp.mMaterial.vTier * 15 * SECONDS)
                .eut(aProp.mMaterial.vVoltageMultiplier)
                .addTo(extractorRecipes);

        }
    }

}
