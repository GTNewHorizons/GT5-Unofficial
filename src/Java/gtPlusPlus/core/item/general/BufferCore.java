package gtPlusPlus.core.item.general;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.item.base.BaseItemWithDamageValue;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class BufferCore extends BaseItemWithDamageValue{

	public int coreTier = 0;

	public BufferCore(final String unlocalizedName, final int i) {
		super(unlocalizedName+i);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(2);
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
		list.add(EnumChatFormatting.GRAY+"A key crafting component for making energy buffers.");
	}

	public final int getCoreTier() {
		return this.coreTier;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {
		//Figure Out Damage
		final String s = String.format("%X", HEX_OxFFFFFF);
		//Utils.LOG_INFO(s);
		//String rgb = Utils.hex2Rgb(s);
		//Utils.LOG_INFO(rgb);
		if (this.coreTier == 1){
			HEX_OxFFFFFF = 0x4d4d4d;
		}
		else if (this.coreTier == 2){
			HEX_OxFFFFFF = 0x666666;
		}
		else if (this.coreTier == 3){
			HEX_OxFFFFFF = 0x8c8c8c;
		}
		else if (this.coreTier == 4){
			HEX_OxFFFFFF = 0xa6a6a6;
		}
		else if (this.coreTier == 5){
			HEX_OxFFFFFF = 0xcccccc;
		}
		else if (this.coreTier == 6){
			HEX_OxFFFFFF = 0xe6e6e6;
		}
		else if (this.coreTier == 7){
			HEX_OxFFFFFF = 0xffffcc;
		}
		else if (this.coreTier == 8){
			HEX_OxFFFFFF = 0xace600;
		}
		else if (this.coreTier == 9){
			HEX_OxFFFFFF = 0xffff00;
		}
		/*else if (coreTier == 10){
			HEX_OxFFFFFF = 0xff0000;
		}*/
		else if (this.coreTier == 10){
			HEX_OxFFFFFF = Utils.rgbtoHexValue(MathUtils.randInt(220, 250), MathUtils.randInt(221, 251), MathUtils.randInt(220, 250));
		}
		else {
			HEX_OxFFFFFF = 0xffffff;
		}


		return HEX_OxFFFFFF;
	}

}
