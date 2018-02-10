package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import static gtPlusPlus.core.util.array.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.GenericStack;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.array.AutoMap;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MatterFab;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_MassFabricator extends GregtechMeta_MultiBlockBase {

	public static int sUUAperUUM = 1;
	public static int sUUASpeedBonus = 4;
	public static int sDurationMultiplier = 3215;
	public static boolean sRequiresUUA = false;
	private int mAmplifierUsed = 0;
	private int mMatterProduced = 0;
	ItemStack scrapPile = ItemUtils.getSimpleStack(ItemUtils.getItem("IC2:itemScrap"));
	ItemStack scrapBox = ItemUtils.getSimpleStack(ItemUtils.getItem("IC2:itemScrapbox"));

	private boolean isUsingControllerCircuit = false;
	private static final Item circuit = CI.getNumberedCircuit(0).getItem();

	private static Block IC2Glass = Block.getBlockFromItem(ItemUtils.getItem("IC2:blockAlloyGlass"));
	FluidStack tempFake = FluidUtils.getFluidStack("uuamplifier", 1);
	GT_Recipe fakeRecipe;

	public int getAmplifierUsed(){
		return this.mAmplifierUsed;
	}

	public int getMatterProduced(){
		return this.mMatterProduced;
	}

	public GregtechMetaTileEntity_MassFabricator(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_MassFabricator(final String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Matter Fabricator",
				"Produces UU-Matter from UU-Amplifier",
				"Size(WxHxD): 5x4x5, Controller (Bottom center)",
				"3x1x3 Matter Generation Coils (Inside bottom 5x1x5 layer)",
				"9x Matter Generation Coils (Centered 3x1x3 area in Bottom layer)",
				"1x Input Hatch (Any bottom layer casing)",
				"1x Output Hatch (Any bottom layer casing)",
				"1x Maintenance Hatch (Any bottom layer casing)",
				"1x Muffler Hatch (Centered 3x1x3 area in Top layer)",
				"1x Energy Hatch (Any bottom layer casing)",
				"24x IC2 Reinforced Glass for the walls",
				"Matter Fabricator Casings for the edges & top (40 at least!)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(9)],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Casing_Machine_Screen_3 : TexturesGtBlock.Casing_Machine_Screen_1)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(9)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MatterFab(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
	}

	@Override
	public void onConfigLoad(final GT_Config aConfig) {
		super.onConfigLoad(aConfig);
		sDurationMultiplier = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUM_Duration_Multiplier", sDurationMultiplier);
		sUUAperUUM = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_per_UUM", sUUAperUUM);
		sUUASpeedBonus = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Speed_Bonus", sUUASpeedBonus);
		sRequiresUUA = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Requirement", sRequiresUUA);
		Materials.UUAmplifier.mChemicalFormula = ("Mass Fabricator Eff/Speed Bonus: x" + sUUASpeedBonus);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		ArrayList<ItemStack> tItems = getStoredInputs();
		ArrayList<FluidStack> tFluids = getStoredFluids();
		ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
		FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);

		if (tItems.size() == 0) {
			return false;
		}

		return checkRecipeGeneric(tItemInputs, tFluidInputs, 1, 500, 75, 100);
	}		


	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				for (int h = 0; h < 4; h++) {

					//Utils.LOG_INFO("Logging Variables - xDir:"+xDir+" zDir:"+zDir+" h:"+h+" i:"+i+" j:"+j);

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
					/*if (tTileEntity != Block.getBlockFromItem(UtilsItems.getItem("IC2:blockAlloyGlass"))) {
						Utils.LOG_INFO("h:"+h+" i:"+i+" j:"+j);
						double tX = tTileEntity.getXCoord();
						double tY = tTileEntity.getYCoord();
						double tZ = tTileEntity.getZCoord();
						Utils.LOG_INFO("Found Glass at X:"+tX+" Y:"+tY+" Z:"+tZ);
						//return false;
					}*/
					if (((i != -2) && (i != 2)) && ((j != -2) && (j != 2))) {// innerer 3x3 ohne h�he
						if (h == 0) {// innen boden (kantal coils)
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Logger.INFO("Matter Generation Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
								Logger.INFO("Matter Generation Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
						} else if (h == 3) {// innen decke (ulv casings + input + muffler)
							if ((!this.addMufflerToMachineList(tTileEntity, TAE.GTPP_INDEX(9)))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
							}
						} else {// innen air
							if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
								Logger.INFO("Make sure the inner 3x3 of the Multiblock is Air.");
								return false;
							}
						}
					} else {// Outer 5x5
						if (h == 0) {// au�en boden (controller, output, energy, maintainance, rest ulv casings)
							if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(9))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(9))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(9))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(9)))) {
								if (((xDir + i) != 0) || ((zDir + j) != 0)) {//no controller
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer.");
										return false;
									}
								}
							}
						} else {// au�en �ber boden (ulv casings)
							if (h == 1) {

								if (((i == -2) || (i == 2)) && ((j == -2) || (j == 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the second layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the second layer.");
										return false;
									}
								}

								else if (((i != -2) || (i != 2)) && ((j != -2) || (j != 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != IC2Glass) {
										Logger.INFO("Glass Casings Missing from somewhere in the second layer.");
										return false;
									}
								}
							}
							if (h == 2) {
								if (((i == -2) || (i == 2)) && ((j == -2) || (j == 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the third layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the third layer.");
										return false;
									}
								}

								else if (((i != -2) || (i != 2)) && ((j != -2) || (j != 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != IC2Glass) {
										Logger.INFO("Glass Casings Missing from somewhere in the third layer.");
										return false;
									}
								}
							}
							if (h == 3) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
									return false;
								}
							}
						}
					}
				}
			}
		}
		Logger.INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 10;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_MassFabricator(this.mName);
	}


	public GenericStack getOutputForMode(ItemStack[] aItemInputs, FluidStack[] aFluidInputs) {
		/**
		 * Modes
		 * 
		 * 19 - Make Scrap
		 * 20 - Eat Junk Item, Make UUA directly.
		 * 21 - Eat Scrap, Produce UUA
		 * 22 - Eat Power, Eat UUA if available, generate UUM.
		 * 
		 */
		
		GenericStack outputStack = new GenericStack();
		
		boolean foundScrap = false;
		boolean foundUUA = false;
		AutoMap<ItemStack> mItemsToJunk = new AutoMap<ItemStack>();
		
		// Get Mode
		if (aItemInputs.length > 0) {
			for (ItemStack h : aItemInputs) {
				if (foundScrap) {
					break;
				}
				if (h != null) {
					if (h.getItem() == this.scrapPile.getItem() || h.getItem() == this.scrapBox.getItem()) {
						this.mMode = 21;
						foundScrap = true;
					}
					else {
						mItemsToJunk.put(h);
					}
				}
			}
		}
		//Found Items in input bus to scrap
		if (mItemsToJunk.size() > 0) {
			
		}		
		
		return outputStack;
	}
	

	/**
	 * Special Recipe Handling
	 */

	private int mMode = 0;

	@Override
	public GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sMassFabFakeRecipes;
	}

	@Override
	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll) {



		// Based on the Processing Array. A bit overkill, but very flexible.
		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		this.mMode = getGUICircuit(aItemInputs);
		int parallelRecipes = 1;
		
		//Set Mode
		
		
		//Generate Loot
		
		
		GenericStack mFishOutput = getOutputForMode(aItemInputs, aFluidInputs);
		
		Logger.WARNING("Mode: "+this.mMode+" | Is loot valid? "+(mFishOutput != null));

		int jslot = 0;
		for (ItemStack x : mFishOutput) {
			if (x != null) {
				Logger.WARNING("Slot "+jslot+" in mFishOutput contains "+x.stackSize+"x "+x.getDisplayName()+".");				
			}
			else {
				Logger.WARNING("Slot "+jslot+" in mFishOutput was null.");
			}
			jslot++;
		}

		// EU discount
		//float tRecipeEUt = (32 * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};

		tTotalEUt = 8;
		Logger.WARNING("Recipe Step. [1]");

		if (parallelRecipes == 0) {
			Logger.WARNING("Recipe Step. [-1]");
			return false;
		}

		// Convert speed bonus to duration multiplier
		// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
		float tTimeFactor = 100.0f / (100.0f + 0);

		float modeMulti = 1;
		modeMulti = (this.mMode == 14 ? 5 : (this.mMode == 15 ? 1 : 20));
		this.mMaxProgresstime = (int)((60*modeMulti) * tTimeFactor);

		this.mEUt = (int)Math.ceil(tTotalEUt);

		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;

		Logger.WARNING("Recipe Step. [2]");
		// Overclock
		if (this.mEUt <= 16) {
			this.mEUt = (this.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
			this.mMaxProgresstime = (this.mMaxProgresstime / (1 << tTier - 1));
		} else {
			while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
				this.mEUt *= 4;
				this.mMaxProgresstime /= 2;
			}
		}

		if (this.mEUt > 0) {
			this.mEUt = (-this.mEUt);
		}

		this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

		Logger.WARNING("Recipe Step. [3]");
		// Collect output item types
		ItemStack[] tOutputItems = mFishOutput;


		int rslot = 0;		
		tOutputItems = removeNulls(mFishOutput);

		for (ItemStack x : tOutputItems) {
			if (x != null) {
				Logger.WARNING("rSlot "+rslot+" in mFishOutput contains "+x.stackSize+"x "+x.getDisplayName()+".");				
			}
			else {
				Logger.WARNING("rSlot "+rslot+" in mFishOutput was null.");
			}
			rslot++;
		}		

		// Commit outputs
		this.mOutputItems = tOutputItems;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		return true;
	}

}