package gtPlusPlus.xmod.gregtech.common.items.covers;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.items.MetaCustomCoverItem;

public class MetaItemCoverCasings extends MetaCustomCoverItem {

    public MetaItemCoverCasings() {
        super(
            GTPlusPlus.ID,
            Textures.BlockIcons.MACHINECASINGS_SIDE.length,
            "Gt Machine Casings",
            Textures.BlockIcons.MACHINECASINGS_SIDE,
            null);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        for (int i = 0; i < icons.length; i++) {
            this.icons[i] = reg.registerIcon(GTPlusPlus.ID + ":" + "covers/" + i);
        }
    }

    @Override
    public boolean hide() {
        return false;
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[MathUtils.balance(meta, 0, 15)];
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {
        return EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocalFormatted(
            "item.itemCustomMetaCover.miscutils.GtMachineCasings",
            GTValues.VOLTAGE_NAMES[MathUtils.balance(tItem.getItemDamage(), 0, GTValues.VOLTAGE_NAMES.length - 1)]); // super.getItemStackDisplayName(tItem);
    }
}
