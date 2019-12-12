package gtPlusPlus.core.item.chemistry.general;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemGenericChemBase extends Item {

	final protected IIcon base[];
	
	final private int aMetaSize = 6;
	
	/*
	 * 0 - Red Metal Catalyst //FeCu
	 * 1 - Yellow Metal Catalyst //WNi
	 * 2 - Blue Metal Catalyst //CoTi
	 * 3 - Orange Metal Catalyst //Vanadium Pd
	 * 4 - Purple Metal Catalyst //IrIdium Ruthenium
	 * 5 - Brown Metal Catalyst //NiAl
	 * 
	 */
	
	public ItemGenericChemBase() {
		this.setHasSubtypes(true);
		this.setNoRepair();
		this.setMaxStackSize(64);
		this.setMaxDamage(0);		
		base = new IIcon[aMetaSize];		
		this.setUnlocalizedName("BasicGenericChemItem");
		GameRegistry.registerItem(this, this.getUnlocalizedName());
	}
	
	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return super.shouldRotateAroundWhenRendering();
	}

	@Override
	public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
		super.onUpdate(p_77663_1_, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
	}

	@Override
	public String getItemStackDisplayName(ItemStack aStack) {
		return super.getItemStackDisplayName(aStack);
	}

	@Override
	public void addInformation(ItemStack aStack, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
		try {
			
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		super.addInformation(aStack, p_77624_2_, aList, p_77624_4_);
	}

	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
		return EnumRarity.common;
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return false;
	}

	@Override
	public void getSubItems(Item aItem, CreativeTabs p_150895_2_, List aList) {
		for (int i=0;i<aMetaSize;i++) {			
			aList.add(ItemUtils.simpleMetaStack(aItem, i, 1));			
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return false;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public int getDisplayDamage(ItemStack stack) {
		return stack.getItemDamage();
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 0;
	}

	@Override
	public void registerIcons(final IIconRegister u) {
		for (int i=0;i<this.aMetaSize;i++) {
			String aPath = CORE.MODID + ":" + "science/general/MetaItem1/"+i;
			this.base[i] = u.registerIcon(aPath);										
		}
	}	
	
	
	@Override
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		return this.base[damage];		
	}

	@Override
	public IIcon getIconFromDamage(int damage) {
		return this.base[damage];	
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		return this.base[stack.getItemDamage()];
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return this.base[stack.getItemDamage()];	
	}

	@Override
    public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
	

}
