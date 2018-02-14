package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.concurrent.TimeUnit;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.players.FakeFarmer;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_TreeFarmer;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_TreeFarmer;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class GregtechMetaTileEntityTreeFarm extends GregtechMeta_MultiBlockBase {

	public final static int TEX_INDEX = 31;
	
	/**
	 * Farm AI
	 */	
	private EntityPlayerMP farmerAI;
	public EntityPlayerMP getFakePlayer() {
		return this.farmerAI;
	}
	

	public GregtechMetaTileEntityTreeFarm(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityTreeFarm(final String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Tree Farmer",
				"How to get your first logs without an axe.",
				"Size(WxHxD): 15x2x15",
				"Purple: Farm Keeper Blocks",
				"Dark Purple: Dirt/Grass/Podzol/Humus",
				"Light Blue: Fence/Fence Gate",
				"Blue/Yellow: Controller",
				"1x Input Bus (anywhere)",
				"1x Output Bus (anywhere)",
				"1x Input Hatch (anywhere)",
				"1x Energy Hatch (anywhere)",
				"1x Maintenance Hatch (anywhere)",
				CORE.GT_Tooltip
		};
	}

	@Override
	public long maxEUStore() {
		return 3244800; //13*13*150*128
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == 0) {
			return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Acacia_Log)};
			}
		if (aSide == 1) {
			return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Acacia_Log), new GT_RenderedTexture(true ? TexturesGtBlock.Overlay_Machine_Vent_Fast : TexturesGtBlock.Overlay_Machine_Vent)};
		}
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Farm_Manager)};
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}


	@Override
	public void loadNBTData(NBTTagCompound arg0) {
		super.loadNBTData(arg0);
	}


	@Override
	public void saveNBTData(NBTTagCompound arg0) {
		super.saveNBTData(arg0);
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean allowCoverOnSide(final byte aSide, final GT_ItemStack aCoverID) {
		return (GregTech_API.getCoverBehavior(aCoverID.toStack()).isSimpleCover()) && (super.allowCoverOnSide(aSide, aCoverID));
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityTreeFarm(this.mName);
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_TreeFarmer(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "TreeFarmer.png");
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_TreeFarmer(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide() || aBaseMetaTileEntity.getWorld().isRemote) {
			Logger.WARNING("Doing nothing Client Side.");
			return false;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}
	
	public Block getCasingBlock() {
		return ModBlocks.blockCasings2Misc;
	}


	public byte getCasingMeta() {
		return 15;
	}


	public byte getCasingTextureIndex() {
		return (byte) TAE.GTPP_INDEX(31);
	}

	@Override
	public int getMaxEfficiency(ItemStack p0) {
		return 10000;
	}


	@Override
	public boolean isGivingInformation() {
		return true;
	}
	
	@Override
	public String[] getInfoData() {
		String[] mSuper = super.getInfoData();
		String[] mDesc = new String[mSuper.length+1];		
		mDesc[0] = "Yggdrasil"; // Machine name		
		for (int i=0;i<mSuper.length;i++) {
			mDesc[i+1] = mSuper[i];
		}
		return mDesc;
	};

	@Override
	public boolean explodesOnComponentBreak(ItemStack p0) {
		return false;
	}
	@Override
	public boolean onRunningTick(final ItemStack aStack) {
		return super.onRunningTick(aStack);
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		//Do Main Multi Logic first
		super.onPostTick(aBaseMetaTileEntity, aTick);
		
		//Do Tree Farm logic next on server side
		if (aBaseMetaTileEntity.isServerSide()) {
			
			
			//Set Forestry Fake player Sapling Planter
			if (this.farmerAI == null) {
				this.farmerAI = new FakeFarmer(MinecraftServer.getServer().worldServerForDimension(this.getBaseMetaTileEntity().getWorld().provider.dimensionId));
			}
			
			
			
			

		}
		//Client Side - do nothing
	}


	@Override
	public boolean checkRecipe(ItemStack p0) {
		return false;
	}


	@Override
	public boolean checkMachine(IGregTechTileEntity p0, ItemStack p1) {
		return false;
	}


	@Override
	public int getPollutionPerTick(ItemStack arg0) {
		return 0;
	}


	@Override
	public void onServerStart() {
		// TODO Auto-generated method stub
		super.onServerStart();
	}

}