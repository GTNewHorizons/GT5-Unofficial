package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_BronzeWorkbench;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_BronzeWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_MetaTileEntity_BronzeCraftingTable
extends GT_MetaTileEntity_AdvancedCraftingTable
{
	public GT_MetaTileEntity_BronzeCraftingTable(int aID, String aName, String aNameRegional, int aTier)
	{
		super(aID, aName, aNameRegional, aTier);
	}

	public GT_MetaTileEntity_BronzeCraftingTable(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
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
	public boolean isTransformerUpgradable()
	{
		return false;
	}

	@Override
	public boolean isBatteryUpgradable()
	{
		return false;
	}

	@Override
	public boolean isEnetInput()
	{
		return false;
	}

	@Override
	public boolean isInputFacing(byte aSide)
	{
		return false;
	}

	@Override
	public long maxEUInput()
	{
		return 0;
	}

	@Override
	public long maxEUStore()
	{
		return 0;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
	{
		return new GT_MetaTileEntity_BronzeCraftingTable(mName, mTier, mDescription, mTextures);
	}

	@Override
	public void onRightclick(EntityPlayer aPlayer)
	{
		getBaseMetaTileEntity().openGUI(aPlayer, 161);
	}

	@SuppressWarnings({ "static-method", "unused" })
	public boolean allowCoverOnSide(byte aSide, int aCoverID)
	{
		return GregTech_API.getCoverBehavior(aCoverID).isSimpleCover();
	}

	@Override
	public int rechargerSlotStartIndex()
	{
		return 0;
	}

	@Override
	public int rechargerSlotCount()
	{
		return 0;
	}

	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone)
	{
		if (aSide == 0) {
			return 315;
		}
		if (aSide == 1) {
			return 317;
		}
		if ((aFacing == 0) || (aFacing == 1)) {
			return 318;
		}
		if ((aFacing == 2) || (aFacing == 3)) {
			return 319;
		}
		return 320;
	}

	@Override
	public String[] getDescription()
	{
		return new String[] {"For the smaller Projects"};
	}

	@Override
	public int getCapacity()
	{
		return 16000;
	}
	
	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity)
	{
		return new CONTAINER_BronzeWorkbench(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity)
	{
		return new GUI_BronzeWorkbench(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return aSide == 1 ? new ITexture[]{ new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CRAFTING)} : new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZE_SIDE), new GT_RenderedTexture(Textures.BlockIcons.VOID)};
	}
}
