package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class GT4Entity_Shelf_Compartment
extends GT4Entity_Shelf
{
	public static IIcon[] sIconList = new IIcon['?'];

	public GT4Entity_Shelf_Compartment(int aID, String aName, String aNameRegional)
	{
		super(aID, aName, aNameRegional);
	}

	public GT4Entity_Shelf_Compartment(String mName) {
		super(mName);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
	{
		return new GT4Entity_Shelf_Compartment(this.mName);
	}

	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone)
	{
		if (aSide == 0) {
			return 32;
		}
		if (aSide == 1) {
			return 29;
		}
		return 40;
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, int aCoverID)
	{
		return aSide != getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ)
	{
		if (aSide == getBaseMetaTileEntity().getFrontFacing()) {
			this.mType = ((byte)((this.mType + 1) % 16));
		}
	}

	public IIcon getTextureIcon(byte aSide, byte aFacing, boolean aActive, boolean aRedstone)
	{
		return aSide == aFacing ? sIconList[this.mType] : null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister aBlockIconRegister)
	{
		for (int i = 0; i < 32; i++) {
			sIconList[i] = aBlockIconRegister.registerIcon("gregtech_addon:tile.Compartment/" + i);
		}
	}

	@Override
	public void onLeftclick(EntityPlayer aPlayer)
	{
		if ((this.mInventory[0] != null) && (this.mInventory[0].stackSize > 0))
		{
			ItemStack tOutput = GT_Utility.copy(new Object[] { this.mInventory[0] });
			if (!aPlayer.isSneaking()) {
				tOutput.stackSize = 1;
			}
			getBaseMetaTileEntity().decrStackSize(0, tOutput.stackSize);
			EntityItem tEntity = new EntityItem(getBaseMetaTileEntity().getWorld(), getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D, getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D, getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D, tOutput);
			tEntity.motionX = 0.0D;
			tEntity.motionY = 0.0D;
			tEntity.motionZ = 0.0D;
			getBaseMetaTileEntity().getWorld().spawnEntityInWorld(tEntity);
		}
	}

	@Override
	public void onRightclick(EntityPlayer aPlayer)
	{
		ItemStack tStack = aPlayer.inventory.getStackInSlot(aPlayer.inventory.currentItem);
		if (tStack == null)
		{
			if ((this.mInventory[0] != null) && (this.mInventory[0].stackSize > 0))
			{
				aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, this.mInventory[0]);
				getBaseMetaTileEntity().setInventorySlotContents(0, null);
			}
		}
		else if (this.mInventory[0] == null)
		{
			aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
			getBaseMetaTileEntity().setInventorySlotContents(0, tStack);
		}
	}

	public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack)
	{
		return aIndex == 0;
	}
}
