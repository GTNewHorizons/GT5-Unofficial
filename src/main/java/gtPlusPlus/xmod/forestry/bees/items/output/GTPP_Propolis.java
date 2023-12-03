package gtPlusPlus.xmod.forestry.bees.items.output;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
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
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_PropolisType;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public class GTPP_Propolis extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public GTPP_Propolis() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gtpp.propolis");
        GameRegistry.registerItem(this, "gtpp.propolis", GTPlusPlus.ID);
    }

    public ItemStack getStackForType(GTPP_PropolisType type) {
        return new ItemStack(this, 1, type.mID);
    }

    public ItemStack getStackForType(GTPP_PropolisType type, int count) {
        return new ItemStack(this, count, type.mID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (GTPP_PropolisType type : GTPP_PropolisType.values()) {
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
        return GTPP_PropolisType.get(stack.getItemDamage()).getColours();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return GTPP_PropolisType.get(stack.getItemDamage()).getName();
    }

    public static void initPropolisRecipes() {
        ItemStack tDrop;
        Logger.BEES("Processing recipes for " + GTPP_Bees.sPropolisMappings.size() + " Propolis.");
        for (GTPP_PropolisType aProp : GTPP_Bees.sPropolisMappings.values()) {
            tDrop = aProp.getStackForType(1);
            if (addProcess(
                    tDrop,
                    aProp.mMaterial.getDust(1),
                    Math.min(Math.max(10000 - (aProp.mMaterial.vTier * 625), 100), 10000),
                    aProp.mMaterial.vTier * 20 * 15,
                    aProp.mMaterial.vVoltageMultiplier)) {
                Logger.BEES("Added Propolis extraction recipe for: " + aProp.getName());
            } else {
                Logger.BEES("Failed to add Propolis extraction recipe for: " + aProp.getName());
            }
        }
    }

    public static boolean addProcess(ItemStack tDrop, ItemStack aOutput, int aChance, int aDuration, int aEUt) {
        if (aOutput == null) {
            return false;
        }
        GT_Values.RA.stdBuilder().itemInputs(tDrop).itemOutputs(aOutput).outputChances(aChance)
                .duration(aDuration * TICKS).eut(aEUt).addTo(extractorRecipes);
        return true;
    }
}
