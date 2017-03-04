package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class GregtechMetaBoilerBase  extends GT_MetaTileEntity_BasicTank
{
	public int mTemperature = 20;
	public int mProcessingEnergy = 0;
	public int mLossTimer = 0;
	public FluidStack mSteam = null;
	public boolean mHadNoWater = false;
	public long RI = MathUtils.randLong(5L, 30L);

	public GregtechMetaBoilerBase(final int aID, final String aName, final String aNameRegional, final String aDescription, final ITexture... aTextures)
	{
		super(aID, aName, aNameRegional, 0, 4, aDescription, aTextures);
	}

	public GregtechMetaBoilerBase(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures)
	{
		super(aName, aTier, 4, aDescription, aTextures);
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone)
	{
		ITexture[] tmp = this.mTextures[aSide >= 2 ? aSide != aFacing ? 2 : ((byte)(aActive ? 4 : 3)) : aSide][aColorIndex + 1];
		//mTextures[(aSide==aFacing?(aActive?4:3):aSide==GT_Utility.getOppositeSide(aFacing)?2:aSide==0?0:aSide==1?1:2)][aColorIndex+1];
		if((aSide!=aFacing)&&(tmp.length==2)){
			tmp = new ITexture[]{tmp[0]};
		}
		return tmp;
	}

	@Override
	public boolean isElectric()
	{
		return false;
	}

	@Override
	public boolean isPneumatic()
	{
		return false;
	}

	@Override
	public boolean isSteampowered()
	{
		return false;
	}

	@Override
	public boolean isSimpleMachine()
	{
		return false;
	}

	@Override
	public boolean isFacingValid(final byte aFacing)
	{
		return aFacing > 1;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer)
	{
		return true;
	}

	@Override
	public boolean isValidSlot(final int aIndex)
	{
		return true;
	}

	@Override
	public int getProgresstime()
	{
		return this.mTemperature;
	}

	@Override
	public int maxProgresstime()
	{
		return 500;
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer)
	{
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		if (aPlayer != null) {
			if (GT_Utility.areStacksEqual(aPlayer.getCurrentEquippedItem(), new ItemStack(Items.water_bucket, 1)))
			{
				this.fill(Materials.Water.getFluid(1000 * aPlayer.getCurrentEquippedItem().stackSize), true);
				aPlayer.getCurrentEquippedItem().func_150996_a(Items.bucket);
			}
			else
			{
				aBaseMetaTileEntity.openGUI(aPlayer);
			}
		}
		return true;
	}

	@Override
	public boolean doesFillContainers()
	{
		return true;
	}

	@Override
	public boolean doesEmptyContainers()
	{
		return true;
	}

	@Override
	public boolean canTankBeFilled()
	{
		return true;
	}

	@Override
	public boolean canTankBeEmptied()
	{
		return true;
	}

	@Override
	public boolean displaysItemStack()
	{
		return false;
	}

	@Override
	public boolean displaysStackSize()
	{
		return false;
	}

	@Override
	public boolean isFluidInputAllowed(final FluidStack aFluid)
	{
		return GT_ModHandler.isWater(aFluid);
	}

	@Override
	public FluidStack getDrainableStack()
	{
		return this.mSteam;
	}

	@Override
	public FluidStack setDrainableStack(final FluidStack aFluid)
	{
		this.mSteam = aFluid;return this.mSteam;
	}

	@Override
	public boolean allowCoverOnSide(final byte aSide, final GT_ItemStack aCover)
	{
		return GregTech_API.getCoverBehavior(aCover.toStack()).isSimpleCover();
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT)
	{
		super.saveNBTData(aNBT);
		aNBT.setInteger("mLossTimer", this.mLossTimer);
		aNBT.setInteger("mTemperature", this.mTemperature);
		aNBT.setInteger("mProcessingEnergy", this.mProcessingEnergy);
		if (this.mSteam != null) {
			try
			{
				aNBT.setTag("mSteam", this.mSteam.writeToNBT(new NBTTagCompound()));
			}
			catch (final Throwable e) {}
		}
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT)
	{
		super.loadNBTData(aNBT);
		this.mLossTimer = aNBT.getInteger("mLossTimer");
		this.mTemperature = aNBT.getInteger("mTemperature");
		this.mProcessingEnergy = aNBT.getInteger("mProcessingEnergy");
		this.mSteam = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mSteam"));
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick)
	{
		if ((aBaseMetaTileEntity.isServerSide()) && (aTick > 20L))
		{
			if (this.mTemperature <= 20)
			{
				this.mTemperature = 20;
				this.mLossTimer = 0;
			}
			if (++this.mLossTimer > 40)
			{
				this.mTemperature -= 1;
				this.mLossTimer = 0;
			}
			for (byte i = 1; (this.mSteam != null) && (i < 6); i = (byte)(i + 1)) {
				if (i != aBaseMetaTileEntity.getFrontFacing())
				{
					final IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(i);
					if (tTileEntity != null)
					{
						final FluidStack tDrained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), Math.max(1, this.mSteam.amount / 2), false);
						if (tDrained != null)
						{
							final int tFilledAmount = tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), tDrained, false);
							if (tFilledAmount > 0) {
								tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), tFilledAmount, true), true);
							}
						}
					}
				}
			}
			if ((aTick % 10L) == 0L) {
				if (this.mTemperature > 100)
				{
					if ((this.mFluid == null) || (!GT_ModHandler.isWater(this.mFluid)) || (this.mFluid.amount <= 0))
					{
						this.mHadNoWater = true;
					}
					else
					{
						if (this.mHadNoWater)
						{
							aBaseMetaTileEntity.doExplosion(2048L);
							return;
						}
						this.mFluid.amount -= 1;
						if (this.mSteam == null) {
							this.mSteam = GT_ModHandler.getSteam(150L);
						} else if (GT_ModHandler.isSteam(this.mSteam)) {
							this.mSteam.amount += 150;
						} else {
							this.mSteam = GT_ModHandler.getSteam(150L);
						}
					}
				}
				else {
					this.mHadNoWater = false;
				}
			}
			if ((this.mSteam != null) &&
					(this.mSteam.amount > 32000))
			{
				this.sendSound((byte)1);
				this.mSteam.amount = 24000;
			}
			if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) &&
					(this.mInventory[2] != null)) {
				if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.gem.get(Materials.Coal))) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.dust.get(Materials.Coal))) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.dustImpure.get(Materials.Coal))) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.crushed.get(Materials.Coal))))
				{
					this.mProcessingEnergy += 160;
					aBaseMetaTileEntity.decrStackSize(2, 1);
					if (aBaseMetaTileEntity.getRandomNumber(3) == 0) {
						aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1L));
					}
				}
				else if (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.gem.get(Materials.Charcoal)))
				{
					this.mProcessingEnergy += 160;
					aBaseMetaTileEntity.decrStackSize(2, 1);
					if (aBaseMetaTileEntity.getRandomNumber(3) == 0) {
						aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 1L));
					}
				}
				else if (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], "fuelCoke"))
				{
					this.mProcessingEnergy += 640;
					aBaseMetaTileEntity.decrStackSize(2, 1);
					if (aBaseMetaTileEntity.getRandomNumber(2) == 0) {
						aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 1L));
					}
				}
				else if ((GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.gem.get(Materials.Lignite))) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.dust.get(Materials.Lignite))) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.dustImpure.get(Materials.Lignite))) || (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.crushed.get(Materials.Lignite))))
				{
					this.mProcessingEnergy += 40;
					aBaseMetaTileEntity.decrStackSize(2, 1);
					if (aBaseMetaTileEntity.getRandomNumber(8) == 0) {
						aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1L));
					}
				}
			}
			if ((this.mTemperature < 1000) && (this.mProcessingEnergy > 0) && ((aTick % 12L) == 0L))
			{
				this.mProcessingEnergy -= 2;
				this.mTemperature += 1;
			}
			aBaseMetaTileEntity.setActive(this.mProcessingEnergy > 0);
		}
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack)
	{
		return (aIndex == 1) || (aIndex == 3);
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack)
	{
		return aIndex == 2;
	}

	@Override
	public void doSound(final byte aIndex, final double aX, final double aY, final double aZ)
	{
		if (aIndex == 1)
		{
			GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(Integer.valueOf(4)), 2, 1.0F, aX, aY, aZ);
			for (int l = 0; l < 8; l++) {
				this.getBaseMetaTileEntity().getWorld().spawnParticle("largesmoke", (aX - 0.5D) + Math.random(), aY, (aZ - 0.5D) + Math.random(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public int getCapacity()
	{
		return 16000;
	}

	@Override
	public int getTankPressure()
	{
		return 100;
	}
}
