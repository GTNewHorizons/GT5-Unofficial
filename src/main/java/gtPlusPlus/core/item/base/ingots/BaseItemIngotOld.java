package gtPlusPlus.core.item.base.ingots;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemIngotOld extends Item {

    protected int colour;
    protected String materialName;
    protected String unlocalName;

    public BaseItemIngotOld(final String unlocalizedName, final String materialName, final int colour,
        final int sRadioactivity) {
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.unlocalName = unlocalizedName;
        this.setMaxStackSize(64);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemIngot");
        this.colour = colour;
        this.materialName = materialName;
        this.sRadiation = sRadioactivity;
        GameRegistry.registerItem(this, unlocalizedName);
        String temp = "";
        if (this.unlocalName.contains("itemIngot")) {
            temp = this.unlocalName.replace("itemI", "i");
        } else if (this.unlocalName.contains("itemHotIngot")) {
            temp = this.unlocalName.replace("itemHotIngot", "ingotHot");
        }
        if ((temp != null) && !temp.equals("")) {
            GTOreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
        }
        // this.generateCompressorRecipe();
    }

    @Override
    public String getItemStackDisplayName(final ItemStack p_77653_1_) {

        return (this.materialName + " Ingot");
    }

    public final String getMaterialName() {
        return this.materialName;
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
        if (this.colour == 0) {
            return MathUtils.generateSingularRandomHexValue();
        }
        return this.colour;
    }

    private void generateCompressorRecipe() {
        if (this.unlocalName.contains("itemIngot")) {
            final ItemStack tempStack = ItemUtils.getSimpleStack(this, 9);
            ItemStack tempOutput = null;
            String temp = this.getUnlocalizedName()
                .replace("item.itemIngot", "block");
            Logger.WARNING("Unlocalized name for OreDict nameGen: " + this.getUnlocalizedName());
            if (this.getUnlocalizedName()
                .contains("item.")) {
                temp = this.getUnlocalizedName()
                    .replace("item.", "");
                Logger.WARNING("Generating OreDict Name: " + temp);
            }
            temp = temp.replace("itemIngot", "block");
            Logger.WARNING("Generating OreDict Name: " + temp);
            if ((temp != null) && !temp.equals("")) {
                tempOutput = ItemUtils.getItemStackOfAmountFromOreDict(temp, 1);
                if (tempOutput != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(tempStack)
                        .itemOutputs(tempOutput)
                        .duration(15 * SECONDS)
                        .eut(2)
                        .addTo(compressorRecipes);
                }
            }
        } else if (this.unlocalName.contains("itemHotIngot")) {
            return;
        }
    }

    protected final int sRadiation;

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.sRadiation, world, entityHolding);
    }
}
