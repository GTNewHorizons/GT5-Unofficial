package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_SpawnEventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GregtechMetaTileEntityChunkLoader extends GT_MetaTileEntity_TieredMachineBlock {

	public int mRange = 16;

	public GregtechMetaTileEntityChunkLoader(final int aID, final String aName, final String aNameRegional,
			final int aTier) {
		super(aID, aName, aNameRegional, aTier, 0, "Reprells nasty Creatures. Range: " + (4 + 12 * aTier)
				+ " unpowered / " + (16 + 48 * aTier) + " powered");
	}

	public GregtechMetaTileEntityChunkLoader(final String aName, final int aTier, final int aInvSlotCount,
			final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return false;
	}

	@Override
	public long getMinimumStoredEU() {
		return 512;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return new ITexture[] {
				Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1],
				aSide != 1 ? null
						: aActive ? new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE)
								: new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER)
		};
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		return null;
	}

	@Override
	public boolean isEnetInput() {
		return true;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return true;
	}

	@Override
	public boolean isInputFacing(final byte aSide) {
		return true;
	}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean isTeleporterCompatible() {
		return false;
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
	}

	@Override
	public long maxAmperesIn() {
		return 2;
	}

	@Override
	public long maxEUInput() {
		return GT_Values.V[this.mTier];
	}

	@Override
	public long maxEUStore() {
		return 512 + GT_Values.V[this.mTier] * 50;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityChunkLoader(this.mName, this.mTier, this.mInventory.length, this.mDescription,
				this.mTextures);
	}

	@Override
	public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
		final int[] tCoords = new int[] {
				aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord(),
				aBaseMetaTileEntity.getWorld().provider.dimensionId
		};
		GT_SpawnEventHandler.mobReps.add(tCoords);
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {
		if (aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.isServerSide()) {
			final int[] tCoords = new int[] {
					aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord(),
					aBaseMetaTileEntity.getWorld().provider.dimensionId
			};
			if (aTimer % 600 == 0 && !GT_SpawnEventHandler.mobReps.contains(tCoords)) {
				GT_SpawnEventHandler.mobReps.add(tCoords);
			}
			if (aBaseMetaTileEntity.isUniversalEnergyStored(this.getMinimumStoredEU())
					&& aBaseMetaTileEntity.decreaseStoredEnergyUnits(1 << this.mTier * 2, false)) {
				this.mRange = 16 + 48 * this.mTier;
			}
			else {
				this.mRange = 4 + 12 * this.mTier;
			}
		}
	}

	@Override
	public void onRemoval() {
		final int[] tCoords = new int[] {
				this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(),
				this.getBaseMetaTileEntity().getZCoord(), this.getBaseMetaTileEntity().getWorld().provider.dimensionId
		};
		GT_SpawnEventHandler.mobReps.remove(tCoords);
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
	}
}
