package gtPlusPlus.core.item.general;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gtPlusPlus.core.item.base.BaseItemWithDamageValue;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;

public class BufferCore extends BaseItemWithDamageValue{

	public int coreTier = 0;

	public BufferCore(final String unlocalizedName, final int i) {
		super(unlocalizedName+i);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(32);
		this.coreTier = i;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack stack) {
		return super.getItemStackDisplayName(stack)/*+" ["+GT_Values.VN[this.coreTier-1]+"]."*/;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(EnumChatFormatting.GRAY+"A key crafting component for "+GT_Values.VN[this.coreTier-1]+" Applicances");
	}

	public final int getCoreTier() {
		return this.coreTier;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {		

		int[] mTierTypes = new int[] {
				Utils.rgbtoHexValue(200, 180, 180),
				Utils.rgbtoHexValue(142, 153, 161),
				Utils.rgbtoHexValue(230, 121, 75),
				Utils.rgbtoHexValue(215, 156, 70),
				Utils.rgbtoHexValue(97, 97, 96), //EV
				Utils.rgbtoHexValue(202, 202, 201),
				Utils.rgbtoHexValue(247, 159, 157),
				Utils.rgbtoHexValue(181, 223, 223),
				Utils.rgbtoHexValue(187, 219, 185),
		};
		
		if (this.coreTier == 10){
			return Utils.rgbtoHexValue(MathUtils.randInt(220, 250), MathUtils.randInt(221, 251), MathUtils.randInt(220, 250));
		}

		return mTierTypes[this.coreTier-1];
	}

}
