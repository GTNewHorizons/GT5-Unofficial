package gtPlusPlus.core.item.base.plates;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemPlate_OLD extends Item {

    protected final int colour;
    protected final int sRadiation;
    protected final String materialName;
    protected final String unlocalName;
    protected final String chemicalNotation;

    public BaseItemPlate_OLD(final String unlocalizedName, final String materialName, final int colour,
        final int sRadioactivity) {
        this(unlocalizedName, materialName, "NullFormula", colour, sRadioactivity);
    }

    public BaseItemPlate_OLD(final String unlocalizedName, final String materialName, final String mChemicalFormula,
        final int colour, final int sRadioactivity) {
        this.setUnlocalizedName("itemPlate" + unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.unlocalName = "itemPlate" + unlocalizedName;
        this.setMaxStackSize(64);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemPlate");
        this.setMaxStackSize(64);
        this.colour = colour;
        this.materialName = materialName;
        if (mChemicalFormula.equals("") || mChemicalFormula.equals("NullFormula")) {
            this.chemicalNotation = StringUtils.subscript(materialName);
        } else {
            this.chemicalNotation = StringUtils.subscript(mChemicalFormula);
        }
        this.sRadiation = sRadioactivity;
        GameRegistry.registerItem(this, "itemPlate" + unlocalizedName);
        String temp;
        if (this.unlocalName.toLowerCase()
            .contains("itemplate")) {
            temp = this.unlocalName.replace("itemP", "p");
            if ((temp != null) && !temp.equals("")) {
                GTOreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
            }
        }
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

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.sRadiation, world, entityHolding);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        if (this.sRadiation > 0) {
            list.add(GTPPCore.GT_Tooltip_Radioactive.get());
        }
        if (StringUtils.containsSuperOrSubScript(this.chemicalNotation)) {
            list.add(this.chemicalNotation);
        }
        super.addInformation(stack, aPlayer, list, bool);
    }
}
