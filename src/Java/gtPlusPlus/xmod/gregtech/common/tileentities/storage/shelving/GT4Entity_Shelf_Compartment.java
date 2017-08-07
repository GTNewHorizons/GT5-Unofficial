package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class GT4Entity_Shelf_Compartment extends GT4Entity_Shelf {
	public static IIcon[] sIconList = new IIcon['?'];

	public GT4Entity_Shelf_Compartment(final int aID, final String aName, final String aNameRegional, final String aDescription) {
		super(aID, aName, aNameRegional, aDescription);
	}
	
	public GT4Entity_Shelf_Compartment(String mName, String[] mDescriptionArray, ITexture[][][] mTextures) {
		super(mName, mDescriptionArray, mTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf_Compartment(this.mName, this.mDescriptionArray, this.mTextures);
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, int aCoverID) {
		return aSide != getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (aSide == getBaseMetaTileEntity().getFrontFacing()) {
			this.mType = ((byte) ((this.mType + 1) % 16));
		}
	}

	public IIcon getTextureIcon(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		return aSide == aFacing ? sIconList[this.mType] : null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister aBlockIconRegister) {
		for (int i = 0; i < 32; i++) {
			sIconList[i] = aBlockIconRegister.registerIcon(CORE.MODID + ":" + "TileEntities/Compartment/" + i);
		}
	}

	@Override
	public void onLeftclick(EntityPlayer aPlayer) {
		if ((this.mInventory[0] != null) && (this.mInventory[0].stackSize > 0)) {
			ItemStack tOutput = GT_Utility.copy(new Object[] { this.mInventory[0] });
			if (!aPlayer.isSneaking()) {
				tOutput.stackSize = 1;
			}
			getBaseMetaTileEntity().decrStackSize(0, tOutput.stackSize);
			EntityItem tEntity = new EntityItem(getBaseMetaTileEntity().getWorld(),
					getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D,
					getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D,
					getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D, tOutput);
			tEntity.motionX = 0.0D;
			tEntity.motionY = 0.0D;
			tEntity.motionZ = 0.0D;
			getBaseMetaTileEntity().getWorld().spawnEntityInWorld(tEntity);
		}
	}

	@Override
	public void onRightclick(EntityPlayer aPlayer) {
		ItemStack tStack = aPlayer.inventory.getStackInSlot(aPlayer.inventory.currentItem);
		if (tStack == null) {
			if ((this.mInventory[0] != null) && (this.mInventory[0].stackSize > 0)) {
				aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, this.mInventory[0]);
				getBaseMetaTileEntity().setInventorySlotContents(0, null);
			}
		}
		else if (this.mInventory[0] == null) {
			aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
			getBaseMetaTileEntity().setInventorySlotContents(0, tStack);
		}
	}

	public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == 0;
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[3][17][];
		for (byte i = -1; i < 16; ++i) {
			final ITexture[] tmp0 = { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_STEEL_BOTTOM,
					Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
			rTextures[0][i + 1] = tmp0;
			final ITexture[] tmp2 = { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_STEEL_TOP,
					Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
			rTextures[1][i + 1] = tmp2;
			final ITexture[] tmp3 = { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_STEEL_SIDE,
					Dyes.getModulation(i, Dyes._NULL.mRGBa)) };
			rTextures[2][i + 1] = tmp3;
		}
		return rTextures;
	}
}
