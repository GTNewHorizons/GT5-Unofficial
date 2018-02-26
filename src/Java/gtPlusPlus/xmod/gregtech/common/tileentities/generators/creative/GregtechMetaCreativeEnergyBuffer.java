package gtPlusPlus.xmod.gregtech.common.tileentities.generators.creative;

import static gregtech.api.enums.GT_Values.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GregtechMetaEnergyBuffer;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 *
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public class GregtechMetaCreativeEnergyBuffer extends GregtechMetaEnergyBuffer {


	public GregtechMetaCreativeEnergyBuffer(final String aName, final int aTier,
			final String aDescription, final ITexture[][][] aTextures, final int aSlotCount) {
		super(aName, aTier, aDescription, aTextures, aSlotCount);
		// TODO Auto-generated constructor stub
	}

	public GregtechMetaCreativeEnergyBuffer(final int aID, final String aName,
			final String aNameRegional, final int aTier, final String aDescription, final int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, CORE.GT_Tooltip};
	}

	/*
	 * MACHINE_STEEL_SIDE
	 */
	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[2][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = new ITexture[] { new GT_RenderedTexture(
					Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT) };
			rTextures[1][i + 1] = new ITexture[] {
					new GT_RenderedTexture(
							Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT),
					this.mInventory.length > 4 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
							: Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity,
			final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive,
			final boolean aRedstone) {
		return this.mTextures[aSide == aFacing ? 1 : 0][aColorIndex + 1];
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaCreativeEnergyBuffer(this.mName, this.mTier, this.mDescription,
				this.mTextures, this.mInventory.length);
	}

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public long maxEUStore() {
		return Long.MAX_VALUE;
	}

	@Override
	public long maxEUInput() {
		return V[this.mTier];
	}

	@Override
	public long maxEUOutput() {
		return V[this.mTier];
	}

	@Override
	public long maxAmperesIn() {
		return 16;
	}

	@Override
	public long maxAmperesOut() {
		return 16;
	}

	@Override public int getProgresstime()							{return Integer.MAX_VALUE;}
	@Override public int maxProgresstime()							{return Integer.MAX_VALUE;}
	@Override public boolean isAccessAllowed(final EntityPlayer aPlayer)	{return true;}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		this.getBaseMetaTileEntity().increaseStoredEnergyUnits(Integer.MAX_VALUE, true);
		if (aBaseMetaTileEntity.isServerSide()) {
			aBaseMetaTileEntity.increaseStoredEnergyUnits(Integer.MAX_VALUE, true);
		}
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public String[] getInfoData() {
		String[] infoData = super.getInfoData();
		return new String[] {
				infoData[0],
				"THIS IS A CREATIVE ITEM - FOR TESTING",
				infoData[1],
				infoData[2]
		};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}
}