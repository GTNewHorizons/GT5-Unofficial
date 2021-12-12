package gtPlusPlus.core.item.general.rfchargingpack;

import java.util.List;

import cofh.api.energy.ItemEnergyContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gtPlusPlus.core.util.math.MathUtils;

public class ChargingPackBase extends ItemEnergyContainer {

	protected final int mCapacityMax;
	protected final byte mTier;

	public ChargingPackBase(byte tier) {
		this(tier, (tier == 1 ? 4000000 : tier == 2 ? 8000000 : tier == 3 ? 16000000 : tier == 4 ? 32000000 : 64000000));
	}

	private ChargingPackBase(byte tier, int maxStorage) {
		super(maxStorage);
		mTier = tier;
		mCapacityMax = maxStorage;
	}

	public int getMaxEnergyInput(ItemStack container)
	{
		return this.maxReceive;
	}

	public int getMaxEnergyExtracted(ItemStack container)
	{
		return this.maxExtract;
	}

	@Override
	public void onUpdate(ItemStack aStack, World aWorld, Entity aEnt, int p_77663_4_, boolean p_77663_5_) {
		super.onUpdate(aStack, aWorld, aEnt, p_77663_4_, p_77663_5_);

		ItemEnergyContainer current = this;
		int currentStored = 0;
		if (current != null) {
			currentStored = current.getEnergyStored(aStack);
		}
		if (currentStored > 0) {
			if (aEnt instanceof EntityPlayer) {
				if (((EntityPlayer) aEnt).inventory != null) {
					for (ItemStack invStack : ((EntityPlayer) aEnt).inventory.mainInventory) {
						if (invStack != null) {
							if (invStack.getItem() instanceof ItemEnergyContainer) {
								if (current != null) {
									currentStored = current.getEnergyStored(aStack);
									if (currentStored > 0) {
										int mTransLimit;
										int mMaxStorage;
										int mCurrent;
										mTransLimit = getMaxEnergyInput(invStack);
										mMaxStorage = current.getMaxEnergyStored(invStack);
										mCurrent = current.getEnergyStored(invStack);										
										if (mCurrent+mTransLimit <= mMaxStorage) {
											current.extractEnergy(aStack, current.receiveEnergy(invStack, mTransLimit, false), false);
										}										
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
		list.add(EnumChatFormatting.RED+"RF Information");
		list.add(EnumChatFormatting.GRAY+"Extraction Rate: [" +EnumChatFormatting.RED+ this.maxExtract + EnumChatFormatting.GRAY + "Rf/t]" + " Insert Rate: [" +EnumChatFormatting.RED+ this.maxReceive+EnumChatFormatting.GRAY+"Rf/t]");
		list.add(EnumChatFormatting.GRAY+"Current Charge: ["+EnumChatFormatting.RED+this.getEnergyStored(stack) + EnumChatFormatting.GRAY + "Rf / " + this.getMaxEnergyStored(stack)+"Rf] "+EnumChatFormatting.RED+MathUtils.findPercentage(this.getEnergyStored(stack), this.getMaxEnergyStored(stack))+EnumChatFormatting.GRAY+"%");
		super.addInformation(stack, p_77624_2_, list, p_77624_4_);
	}

}
