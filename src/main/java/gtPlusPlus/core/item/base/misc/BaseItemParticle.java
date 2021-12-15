package gtPlusPlus.core.item.base.misc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public abstract class BaseItemParticle extends CoreItem {
	
	protected static final Map<Integer, Integer> aColourMap = new LinkedHashMap<Integer, Integer> ();
	private final int aMaxCount;
	
	public BaseItemParticle(String aType, int aCount, EnumRarity aRarity) {
		super("particle"+aType, aType, AddToCreativeTab.tabOther, 64, 0, new String[] {}, aRarity, EnumChatFormatting.DARK_AQUA, false, null);
		this.setTextureName(CORE.MODID + ":" + "science/Atom");
		aMaxCount = aCount;
		this.setHasSubtypes(true);
		
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return aColourMap.get(stack.getItemDamage());
	}	
	
	public int getColorFromParentClass(ItemStack stack, int aaa) {
		return super.getColorFromItemStack(stack, aaa);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < aMaxCount; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	

	@Override
	public final String getItemStackDisplayName(final ItemStack tItem) {
		String aReturnValue = super.getItemStackDisplayName(tItem);
		String[] a2 = getAffixes();
		aReturnValue = (a2[0] + aReturnValue + a2[1]);
		return aReturnValue;
	}
	
	public abstract String[] getAffixes();
	
}
