package gtPlusPlus.core.item.base.ingots;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;

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
        if (!temp.isEmpty()) {
            GTOreDictUnificator.registerOre(temp, new ItemStack(this));
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

    protected final int sRadiation;

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.sRadiation, world, entityHolding);
    }
}
