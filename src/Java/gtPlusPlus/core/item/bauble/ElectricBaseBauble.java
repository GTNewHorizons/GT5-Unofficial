package gtPlusPlus.core.item.bauble;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.helpers.ChargingHelper;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

@Optional.InterfaceList(value = { @Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles"),
@Optional.Interface(iface = "baubles.api.BaubleType", modid = "Baubles") })
public abstract class ElectricBaseBauble extends BaseBauble implements IElectricItem, IElectricItemManager, IBauble {

	public final int mTier;
	private final double maxValueEU;
	private final BaubleType mType;

	public ElectricBaseBauble(BaubleType aType, int aTier, double aMaxEU, String aUnlocalName) {		
		super(aType, aUnlocalName, 0);
		mType = aType;
		mTier = aTier;
		maxValueEU = aMaxEU;
		this.setUnlocalizedName(aUnlocalName);
		this.setTextureName(CORE.MODID + ":" + getTextureNameForBauble());
		this.setMaxDamage(27);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setCreativeTab(AddToCreativeTab.tabMachines);		
		if (GameRegistry.findItem(CORE.MODID, aUnlocalName) == null) {
			GameRegistry.registerItem(this, aUnlocalName);			
		}		
	}
	
	public abstract String getTextureNameForBauble();

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack itemStack = new ItemStack(this, 1);
		ItemStack charged;
		if (this.getEmptyItem(itemStack) == this) {
			charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, 0.0D, Integer.MAX_VALUE, true, false);
			itemList.add(charged);
		}
		if (this.getChargedItem(itemStack) == this) {
			charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false);
			itemList.add(charged);
		}

	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World worldObj, final Entity player, final int p_77663_4_,
			final boolean p_77663_5_) {
		super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
	}

	@Override
	public boolean canProvideEnergy(final ItemStack itemStack) {
		double aItemCharge = ElectricItem.manager.getCharge(itemStack);		
		return aItemCharge > 0;
	}

	@Override
	public final Item getChargedItem(final ItemStack itemStack) {
		final ItemStack x = itemStack.copy();
		x.setItemDamage(27);
		return x.getItem();
	}

	@Override
	public final Item getEmptyItem(final ItemStack itemStack) {
		final ItemStack x = itemStack.copy();
		x.setItemDamage(0);
		return x.getItem();
	}

	@Override
	public final double getMaxCharge(final ItemStack itemStack) {
		return maxValueEU;
	}

	@Override
	public final int getTier(final ItemStack itemStack) {
		return mTier;
	}

	@Override
	public final double getTransferLimit(final ItemStack itemStack) {
		return GT_Values.V[mTier];
	}

	@Override
	public final double getDurabilityForDisplay(final ItemStack stack) {
		return 1.0D - (this.getCharge(stack) / this.getMaxCharge(stack));
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add("");
		String aEuInfo = StatCollector.translateToLocal("GTPP.info.euInfo");
		String aTier = StatCollector.translateToLocal("GTPP.machines.tier");
		String aInputLimit = StatCollector.translateToLocal("GTPP.info.inputLimit");
		String aCurrentPower = StatCollector.translateToLocal("GTPP.info.currentPower");
		String aEU = StatCollector.translateToLocal("GTPP.info.eu");	
		String aEUT = aEU+"/t";

		list.add(EnumChatFormatting.GOLD + aEuInfo + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GRAY + aTier+": [" + EnumChatFormatting.YELLOW + this.getTier(stack)
		+ EnumChatFormatting.GRAY + "] "+aInputLimit+": [" + EnumChatFormatting.YELLOW
		+ this.getTransferLimit(stack) + EnumChatFormatting.GRAY + aEUT);
		list.add(EnumChatFormatting.GRAY + aCurrentPower +": [" + EnumChatFormatting.YELLOW + (long) this.getCharge(stack)
		+ EnumChatFormatting.GRAY + aEU +"] [" + EnumChatFormatting.YELLOW
		+ MathUtils.findPercentage(this.getCharge(stack), this.getMaxCharge(stack)) + EnumChatFormatting.GRAY
		+ "%]");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public final double charge(final ItemStack stack, final double amount, final int tier, final boolean ignoreTransferLimit,
			final boolean simulate) {
		/*if (!simulate) {
			ElectricItem.manager.charge(stack, amount, tier, true, simulate);
		}*/
		
		return ElectricItem.manager.charge(stack, amount, tier, true, simulate);
	}

	@Override
	public final double discharge(final ItemStack stack, final double amount, final int tier,
			final boolean ignoreTransferLimit, final boolean externally, final boolean simulate) {
		/*if (!simulate) {
			ElectricItem.manager.discharge(stack, amount, tier, ignoreTransferLimit, externally, simulate);
		}*/

		return ElectricItem.manager.discharge(stack, amount, tier, ignoreTransferLimit, externally, simulate);
	}

	@Override
	public final double getCharge(final ItemStack stack) {
		return ElectricItem.manager.getCharge(stack);
	}

	@Override
	public final boolean canUse(final ItemStack stack, final double amount) {
		return ElectricItem.manager.canUse(stack, amount);
	}

	@Override
	public final boolean use(final ItemStack stack, final double amount, final EntityLivingBase entity) {
		return ElectricItem.manager.use(stack, amount, entity);
	}

	@Override
	public final void chargeFromArmor(final ItemStack stack, final EntityLivingBase entity) {
		ElectricItem.manager.chargeFromArmor(stack, entity);
	}

	@Override
	public String getToolTip(final ItemStack stack) {
		//return ElectricItem.manager.getToolTip(stack);
		return null;
	}

	@Override
	public final BaubleType getBaubleType(final ItemStack arg0) {
		return mType;
	}

}
