package gtPlusPlus.core.item.general;

import gtPlusPlus.core.item.base.BaseItemWithDamageValue;
import gtPlusPlus.core.lib.CORE;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BufferCore extends BaseItemWithDamageValue{
	
	public int coreTier = 0;
	
	public BufferCore(String unlocalizedName, int i) {
		super(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(2);
		this.coreTier = i;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack)+" ["+CORE.VOLTAGES[this.coreTier-1]+"].";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		list.add(EnumChatFormatting.GRAY+"A key crafting component for making energy buffers.");
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final int getCoreTier() {
		return coreTier;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		//Figure Out Damage
		String s = String.format("%X", HEX_OxFFFFFF);
		//Utils.LOG_INFO(s);
		//String rgb = Utils.hex2Rgb(s);
		//Utils.LOG_INFO(rgb);
		if (coreTier == 1){
			HEX_OxFFFFFF = 0x4d4d4d;
		}
		else if (coreTier == 2){
			HEX_OxFFFFFF = 0x666666;
		}
		else if (coreTier == 3){
			HEX_OxFFFFFF = 0x8c8c8c;
		}
		else if (coreTier == 4){
			HEX_OxFFFFFF = 0xa6a6a6;
		}
		else if (coreTier == 5){
			HEX_OxFFFFFF = 0xcccccc;
		}
		else if (coreTier == 6){
			HEX_OxFFFFFF = 0xe6e6e6;
		}
		else if (coreTier == 7){
			HEX_OxFFFFFF = 0xffffcc;
		}
		else if (coreTier == 8){
			HEX_OxFFFFFF = 0xace600;
		}
		else if (coreTier == 9){
			HEX_OxFFFFFF = 0xffff00;
		}
		else if (coreTier == 10){
			HEX_OxFFFFFF = 0xff0000;
		}
		else {
			HEX_OxFFFFFF = 0xffffff;
		}

		
		return HEX_OxFFFFFF;
	}
	
}
