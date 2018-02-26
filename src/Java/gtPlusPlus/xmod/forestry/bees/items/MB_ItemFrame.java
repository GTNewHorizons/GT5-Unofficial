package gtPlusPlus.xmod.forestry.bees.items;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import forestry.api.apiculture.*;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;

public class MB_ItemFrame extends Item implements IHiveFrame
{
	private final MB_FrameType type;
	private EnumRarity rarity_value = EnumRarity.uncommon;
	private final String toolTip;

	public MB_ItemFrame(final MB_FrameType frameType, final String description)
	{
		this(frameType, EnumRarity.uncommon, description);
	}

	public MB_ItemFrame(final MB_FrameType frameType, final EnumRarity rarity, final String description)
	{
		super();
		this.type = frameType;
		this.setMaxDamage(this.type.maxDamage);
		this.setMaxStackSize(1);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName("frame" + frameType.getName());
		this.rarity_value = rarity;
		this.toolTip = description;
		GameRegistry.registerItem(this, "frame" + frameType.getName());
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		if ((this.toolTip != "") || !this.toolTip.equals("")){
			list.add(EnumChatFormatting.GRAY+this.toolTip);
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(CORE.MODID + ":frame" + this.type.getName());
	}

	// --------- IHiveFrame functions -----------------------------------------

	@Override
	public ItemStack frameUsed(final IBeeHousing housing, ItemStack frame, final IBee queen, final int wear) {
		frame.setItemDamage(frame.getItemDamage() + wear);

		if (frame.getItemDamage() >= frame.getMaxDamage()) {
			// Break the frame.
			frame = null;
		}

		return frame;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		return this.rarity_value;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		if ((this.rarity_value == EnumRarity.uncommon) || (this.rarity_value == EnumRarity.common)){
			return false;
		}
		return true;
	}

	@Override
	public IBeeModifier getBeeModifier() {
		return this.type;
	}

	@Override
	public boolean isBookEnchantable(final ItemStack itemstack1, final ItemStack itemstack2)
	{
		return false;
	}

	public float getTerritoryModifier(final IBeeGenome genome, final float currentModifier) {
		return this.type.getTerritoryModifier(genome, currentModifier);
	}

	public float getMutationModifier(final IBeeGenome genome, final IBeeGenome mate, final float currentModifier) {
		return this.type.getMutationModifier(genome, mate, currentModifier);
	}

	public float getLifespanModifier(final IBeeGenome genome, final IBeeGenome mate, final float currentModifier) {
		return this.type.getLifespanModifier(genome, mate, currentModifier);
	}

	public float getProductionModifier(final IBeeGenome genome, final float currentModifier) {
		return this.type.getProductionModifier(genome, currentModifier);
	}

	public float getFloweringModifier(final IBeeGenome genome, final float currentModifier) {
		return this.type.getFloweringModifier(genome, currentModifier);
	}

	public float getGeneticDecay(final IBeeGenome genome, final float currentModifier) {
		return this.type.getGeneticDecay(genome, currentModifier);
	}

	public boolean isSealed() {
		return this.type.isSealed();
	}

	public boolean isSelfLighted() {
		return this.type.isSelfLighted();
	}

	public boolean isSunlightSimulated() {
		return this.type.isSunlightSimulated();
	}

	public boolean isHellish(){
		return this.type.isHellish();
	}

}
