package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GregtechMetaTileEntity_IsaMill extends GregtechMeta_MultiBlockBase {

	protected int fuelConsumption = 0;
	protected int fuelValue = 0;
	protected int fuelRemaining = 0;
	protected boolean boostEu = false;

	private static ITexture frontFace;
	private static ITexture frontFaceActive;

	public GregtechMetaTileEntity_IsaMill(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		frontFaceActive = new GT_RenderedTexture(new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE5"));
		frontFace = new GT_RenderedTexture(new CustomIcon("iconsets/Grinder/GRINDER5"));
	}

	public GregtechMetaTileEntity_IsaMill(String aName) {
		super(aName);
	}

	public String[] getTooltip() {
		return new String[]{
				"Controller Block for the Large Grinding Machine",
				"Engine Intake Casings must not be obstructed in front (only air blocks)",
				"Supply Semifluid Fuels and 2000L of Lubricant per hour to run.",
				"Supply 80L of Oxygen per second to boost output (optional).",
				"Default: Produces 2048EU/t at 100% efficiency",
				"Boosted: Produces 6144EU/t at 150% efficiency",
				"Size(WxHxD): 3x3x4, Controller (front centered)",
				"3x3x4 of Stable Titanium Machine Casing (hollow, Min 16!)",
				"All hatches except dynamo can replace any Stable Titanium casing in middle two segments",
				"2x Steel Gear Box Machine Casing inside the Hollow Casing",
				"8x Engine Intake Machine Casing (around controller)",
				"2x Input Hatch (Fuel/Lubricant)",
				"1x Maintenance Hatch",
				"1x Muffler Hatch",
				"1x Dynamo Hatch (back centered)",
		};
	}


	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return new ITexture[]{
				Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(2)],
				aFacing == aSide ? aActive ? frontFaceActive : frontFace : Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(2)]};
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return getMaxEfficiency(aStack) > 0;
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeDieselEngine.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe_Map.sMaceratorRecipes;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		return checkRecipeGeneric();
	}

	@Override
	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		byte tSide = aBaseMetaTileEntity.getBackFacing();
		int aTileX = aBaseMetaTileEntity.getXCoord();
		int aTileY = aBaseMetaTileEntity.getYCoord();
		int aTileZ = aBaseMetaTileEntity.getZCoord();
		boolean xFacing = (tSide == 4 || tSide == 5);
		boolean zFacing = (tSide == 2 || tSide == 3);
		int aCasingCount = 0;
		// Check Intake Hatches
		for (int aHorizontalOffset = -1; aHorizontalOffset < 2; aHorizontalOffset++) {
			for (int aVerticalOffset = -1; aVerticalOffset < 2; aVerticalOffset++) {
				if (aHorizontalOffset == 0 && aVerticalOffset == 0) {
					continue;
				}			
				int aX = !xFacing ? (aTileX + aHorizontalOffset) : aTileX;
				int aY = aTileY + aVerticalOffset;
				int aZ = !zFacing ? (aTileZ + aHorizontalOffset) : aTileZ;
				Block aIntakeBlock = aBaseMetaTileEntity.getBlock(aX, aY, aZ);
				int aIntakeMeta = aBaseMetaTileEntity.getMetaID(aX, aY, aZ);				
				if (!isValidBlockForStructure(null, 0, false, aIntakeBlock, aIntakeMeta, getIntakeBlock(), getIntakeMeta())) {
					return false; // Not intake casing surrounding controller					
				}
			}
		}
		// Check Casings
		int aStartDepthOffset = (tSide == 2 || tSide == 4) ? -1 : 1;
		int aFinishDepthOffset = (tSide == 2 || tSide == 4) ? -8 : 8;
		for (int aDepthOffset = aStartDepthOffset; aDepthOffset != aFinishDepthOffset;) {
			for (int aHorizontalOffset = -1; aHorizontalOffset < 2; aHorizontalOffset++) {
				for (int aVerticalOffset = -1; aVerticalOffset < 2; aVerticalOffset++) {
					if (aHorizontalOffset == 0 && aVerticalOffset == 0) {
						continue;
					}				
					int aX = !xFacing ? (aTileX + aHorizontalOffset) : (aTileX + aDepthOffset);
					int aY = aTileY + aVerticalOffset;
					int aZ = !zFacing ? (aTileZ + aHorizontalOffset) : (aTileZ + aDepthOffset);
					Block aCasingBlock = aBaseMetaTileEntity.getBlock(aX, aY, aZ);
					int aCasingMeta = aBaseMetaTileEntity.getMetaID(aX, aY, aZ);	
					IGregTechTileEntity aTileEntity = getBaseMetaTileEntity().getIGregTechTileEntity(aX, aY, aZ);
					if (aTileEntity != null) {
						final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
						if (aMetaTileEntity != null) {
							if (aMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill) {
								Logger.INFO("Don't be cheeky, only one controller per Mill.");
								return false;
							}
						}
					}

					if (!isValidBlockForStructure(aTileEntity, getCasingTextureIndex(), true, aCasingBlock, aCasingMeta, getCasingBlock(), getCasingMeta())) {
						Logger.INFO("Bad casing.");
						return false; // Not valid casing					
					}
					else {
						if (aTileEntity == null) {
							aCasingCount++;
						}
					}
				}
			}
			// Count Backwards for 2 axis
			if (aStartDepthOffset == -1) {
				aDepthOffset--;
			}
			// Count Forwards for 2 axis
			else {
				aDepthOffset++;
			}
		}

		// Check Gear Boxes
		for (int aInternalDepthAxis = 1; aInternalDepthAxis < 7; aInternalDepthAxis++) {
			if(aBaseMetaTileEntity.getBlockAtSideAndDistance(tSide, aInternalDepthAxis) != getGearboxBlock() || aBaseMetaTileEntity.getMetaIDAtSideAndDistance(tSide, aInternalDepthAxis) != getGearboxMeta()) {
				Logger.INFO("Missing Gearbox at depth "+aInternalDepthAxis);
				return false;
			}
		}
		return aCasingCount >= 48;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockCasings5Misc;
	}

	public byte getCasingMeta() {
		return 0;
	}

	public Block getIntakeBlock() {
		return ModBlocks.blockCasings5Misc;
	}

	public byte getIntakeMeta() {
		return 0;
	}

	public Block getGearboxBlock() {
		return ModBlocks.blockCasings5Misc;
	}

	public byte getGearboxMeta() {
		return 2;
	}

	public byte getCasingTextureIndex() {
		return 66;
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IsaMill(this.mName);
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 1;
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return boostEu ? 20000 : 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 64;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public String[] getExtraInfoData() {
		return new String[]{
				"IsaMill Grinding Machine",
				"Current Efficiency: " + (mEfficiency / 100) + "%",
				getIdealStatus() == getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance"
		};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public String getMachineType() {
		return "Grinding Machine";
	}

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}
}
