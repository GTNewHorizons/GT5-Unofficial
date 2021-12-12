package gtPlusPlus.xmod.gregtech.common.items.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.items.MetaCustomCoverItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class MetaItemCoverCasings extends MetaCustomCoverItem {

	public MetaItemCoverCasings() {
		super(CORE.MODID, Textures.BlockIcons.MACHINECASINGS_SIDE.length, "Gt Machine Casings", Textures.BlockIcons.MACHINECASINGS_SIDE, null);
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		for (int i = 0; i < icons.length; i++) {
			this.icons[i] = reg.registerIcon(CORE.MODID+":"+"covers/"+i);
		}
	}

	public boolean hide() {
		return false;
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.icons[MathUtils.balance(meta, 0, 15)];
	}

	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		return EnumChatFormatting.LIGHT_PURPLE + GT_Values.VOLTAGE_NAMES[MathUtils.balance(tItem.getItemDamage(), 0, GT_Values.VOLTAGE_NAMES.length-1)]+" Machine Plate Cover"; //super.getItemStackDisplayName(tItem);
	}

}
