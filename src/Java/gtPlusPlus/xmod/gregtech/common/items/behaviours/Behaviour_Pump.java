package gtPlusPlus.xmod.gregtech.common.items.behaviours;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.items.behaviors.Behaviour_None;
import gtPlusPlus.api.objects.Logger;

public class Behaviour_Pump
extends Behaviour_None {

	private final int mCosts;
	private FluidStack mStoredFluid;
	private final String mTooltip = GT_LanguageManager.addStringLocalization("gt.behaviour.pump", "Sucks in Machine Input Fluid tank contents on Rightclick");

	public Behaviour_Pump(final int aCosts) {
		this.mCosts = aCosts;
	}

	@Override
	public boolean onItemUseFirst(final GT_MetaBase_Item aItem, final ItemStack aStack, final EntityPlayer aPlayer, final World aWorld, final int aX, final int aY, final int aZ, final int aSide, final float hitX, final float hitY, final float hitZ) {
		if (aWorld.isRemote) {
			return false;
		}
		else {
			final Block aBlock = aWorld.getBlock(aX, aY, aZ);
			if (aBlock == null) {
				return false;
			}
			//final byte aMeta = (byte) aWorld.getBlockMetadata(aX, aY, aZ);
			TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
			if (tTileEntity == null) {
				return false;
			}
			if ((tTileEntity instanceof IGregTechTileEntity)) {
				Logger.INFO("Right Clicking on GT Tile - Behaviour Class.");
				if (((IGregTechTileEntity) tTileEntity).getTimer() < 50L) {
					Logger.INFO("Returning False - Behaviour Class. Timer < 50");
					return false;
				}
				else if ((!aWorld.isRemote) && (!((IGregTechTileEntity) tTileEntity).isUseableByPlayer(aPlayer))) {
					Logger.INFO("Returning True - Behaviour Class. NotUsable()");
					return true;
				}
				else {
					Logger.INFO("Trying to find Stored Fluid - Behaviour Class.");
					FluidStack aStored = getStoredFluidOfGTMachine((IGregTechTileEntity)tTileEntity);
					if (aStored != null) {
						this.mStoredFluid = aStored;
						boolean b = setStoredFluidOfGTMachine((IGregTechTileEntity)tTileEntity);
						Logger.INFO("Cleared Tank? "+b);
						Logger.INFO("Returning True - Behaviour Class.");
						return true;						
					}
					else {
						Logger.INFO("Found no valid Fluidstack - Behaviour Class.");
					}
				}

			}
		}
		Logger.INFO("Returning False - Behaviour Class.");
		return false;
	}

	public List<String> getAdditionalToolTips(final GT_MetaBase_Item aItem, final List<String> aList, final ItemStack aStack) {
		aList.add(this.mTooltip);
		aList.add("Stored Fluid: "+(mStoredFluid != null ? mStoredFluid.getLocalizedName()+" - "+mStoredFluid.amount+"L" : "None"));		
		return aList;
	}

	@Override
	public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		// TODO Auto-generated method stub
		return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
	}

	@Override
	public void onUpdate(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, Entity aPlayer, int aTimer,
			boolean aIsInHand) {
		// TODO Auto-generated method stub
		super.onUpdate(aItem, aStack, aWorld, aPlayer, aTimer, aIsInHand);
	}

	public FluidStack getStoredFluidOfGTMachine(IGregTechTileEntity aTileEntity) {
		if (aTileEntity == null) {
			return null;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();;
		if (aMetaTileEntity == null) {
			return null;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_BasicTank) {
			Logger.INFO("Tile Was Instanceof BasicTank.");
			return getStoredFluidOfGTMachine((GT_MetaTileEntity_BasicTank) aMetaTileEntity);
		}
		else {
			return null;
		}
	}

	public FluidStack getStoredFluidOfGTMachine(GT_MetaTileEntity_BasicTank aTileEntity) {	
		FluidStack f = aTileEntity.mFluid;
		Logger.INFO("Returning Fluid stack from tile. Found: "+(f != null ? f.getLocalizedName()+" - "+f.amount+"L" : "Nothing"));		
		return f.copy();
	}

	public boolean setStoredFluidOfGTMachine(IGregTechTileEntity aTileEntity) {
		Logger.INFO("Trying to clear Tile's tank. - Behaviour Class. [1]");
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();;
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_BasicTank) {
			Logger.INFO("Trying to clear Tile's tank. - Behaviour Class. [2]");
			return setStoredFluidOfGTMachine((IGregTechTileEntity) aMetaTileEntity);
		}
		else {
			return false;
		}
	}

	public boolean setStoredFluidOfGTMachine(GT_MetaTileEntity_BasicTank aTileEntity) {
		try {
			aTileEntity.mFluid = null;	
			boolean b = aTileEntity.mFluid == null;
			Logger.INFO("Trying to clear Tile's tank. - Behaviour Class. [3] "+b);
			return aTileEntity.mFluid == null;
		}
		catch (Throwable t) {
			Logger.INFO("Trying to clear Tile's tank. FAILED - Behaviour Class. [x]");
			return false;
		}
	}

	public synchronized final FluidStack getStoredFluid() {
		return mStoredFluid;
	}

	public synchronized final void setStoredFluid(FluidStack mStoredFluid) {
		this.mStoredFluid = mStoredFluid;
	}
}
