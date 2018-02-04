package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.*;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntityGeneratorArray extends GregtechMeta_MultiBlockBase {

	GT_Recipe mLastRecipe;

	public GregtechMetaTileEntityGeneratorArray(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityGeneratorArray(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityGeneratorArray(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Generator Array",
				"Runs supplied generators as if placed in the world",
				"Size(WxHxD): 3x3x3 (Hollow), Controller (Front centered)",
				"1x Input Hatch/Bus (Any casing)",
				"1x Output Hatch/Bus (Any casing)",
				"1x Maintenance Hatch (Any casing)",
				"1x Energy Hatch (Any casing)",
				"Robust Tungstensteel Machine Casings for the rest (16 at least!)",
		"Place up to 16 Single Block GT Generators into the Controller Inventory"};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[48], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[48]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ProcessingArray.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		if (mInventory[1] == null) return null;
		String tmp = mInventory[1].getUnlocalizedName().replaceAll("gt\\.blockmachines\\.basicgenerator\\.", "");
		if (tmp.startsWith("steamturbine")) {
			return Gregtech_Recipe_Map.sSteamTurbineFuels;
		} 
		else if (tmp.startsWith("diesel")) {
			return GT_Recipe.GT_Recipe_Map.sDieselFuels;
		} 
		else if (tmp.startsWith("gasturbine")) {
			return GT_Recipe.GT_Recipe_Map.sTurbineFuels;
		} 
		else if (tmp.startsWith("semifluid")) {
			return GT_Recipe.GT_Recipe_Map.sDenseLiquidFuels;
		} 
		else if (tmp.startsWith("rtg")) {
			return Gregtech_Recipe_Map.sRTGFuels;
		} 


		tmp = mInventory[1].getUnlocalizedName().replaceAll("gt\\.blockmachines\\.advancedgenerator\\.", "");
		if (tmp.startsWith("rocket")) {
			return GT_Recipe.GT_Recipe_Map.sDieselFuels;
		} 
		else if (tmp.startsWith("geothermalFuel")) {
			return Gregtech_Recipe_Map.sGeoThermalFuels;
		}     

		return null;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		if (aStack != null && aStack.getUnlocalizedName().startsWith("gt.blockmachines.")) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	public String mMachine = "";
	@Override
	public boolean checkRecipe(ItemStack aStack) {
		if (!isCorrectMachinePart(mInventory[1])) {
			return false;
		}
		GT_Recipe.GT_Recipe_Map map = getRecipeMap();
		if (map == null) return false;
		ArrayList<ItemStack> tInputList = getStoredInputs();
		int tTier = 0;
		if (mInventory[1].getUnlocalizedName().endsWith("1")) {
			tTier = 1;
		}else if (mInventory[1].getUnlocalizedName().endsWith("2")) {
			tTier = 2;
		}else if (mInventory[1].getUnlocalizedName().endsWith("3")) {
			tTier = 3;
		}else if (mInventory[1].getUnlocalizedName().endsWith("4")) {
			tTier = 4;
		}else if (mInventory[1].getUnlocalizedName().endsWith("5")) {
			tTier = 5;
		}else if (mInventory[1].getUnlocalizedName().endsWith("6")) {
			tTier = 6;
		}else if (mInventory[1].getUnlocalizedName().endsWith("7")) {
			tTier = 7;
		}else if (mInventory[1].getUnlocalizedName().endsWith("8")) {
			tTier = 8;
		}

		int fuelConsumption = 0;
		int fuelValue = 0;
		int fuelRemaining = 0;
		boolean boostEu = false;

		if(!mMachine.equals(mInventory[1].getUnlocalizedName()))mLastRecipe=null;
		mMachine = mInventory[1].getUnlocalizedName();
		Logger.WARNING("mMachine: "+mMachine);
		ArrayList<FluidStack> tFluids = getStoredFluids();
		Collection<GT_Recipe> tRecipeList = this.getRecipeMap().mRecipeList;
		Logger.WARNING("tRecipeList: "+tRecipeList);

		if(tFluids.size() > 0 && tRecipeList != null) { //Does input hatch have a diesel fuel?
			Logger.WARNING("1");
			for (FluidStack hatchFluid1 : tFluids) { //Loops through hatches
				Logger.WARNING("2");
				for(GT_Recipe aFuel : tRecipeList) { //Loops through diesel fuel recipes
					Logger.WARNING("3");
					/*if (aFuel != null){
						for (FluidStack x : aFuel.mFluidInputs){
							if (x != null){
								Utils.LOG_WARNING("Recipe: "+x.getLocalizedName());								
							}
						}
					}*/
					FluidStack tLiquid;
					tLiquid = FluidUtils.getFluidStack(aFuel.mFluidInputs[0], aFuel.mFluidInputs[0].amount);
					Logger.WARNING("5");
					fuelConsumption = aFuel.mFluidInputs[0].amount;
					if(depleteInput(tLiquid)) { //Deplete that amount
						Logger.WARNING("6");
						boostEu = true;
						Logger.WARNING("7");                               

						fuelValue = aFuel.mEUt*aFuel.mDuration;
						fuelRemaining = hatchFluid1.amount; //Record available fuel
						this.mEUt = mEfficiency < 2000 ? 0 : 2048; //Output 0 if startup is less than 20%
						//this.mProgresstime = 1;
						this.mMaxProgresstime = 20;
						this.mEfficiencyIncrease = 9500;
						Logger.WARNING("9");
						return true;
					}
				}
				// }
				//}
			}
		}
		this.mEUt = 0;
		this.mEfficiency = 0;
		return false;
	}

	public static ItemStack[] clean(final ItemStack[] v) {
		List<ItemStack> list = new ArrayList<ItemStack>(Arrays.asList(v));
		list.removeAll(Collections.singleton(null));
		return list.toArray(new ItemStack[list.size()]);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = -1; h < 2; h++) {
					if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!addMaintenanceToMachineList(tTileEntity, 48)) && (!addInputToMachineList(tTileEntity, 48)) && (!addOutputToMachineList(tTileEntity, 48)) && (!addDynamoToMachineList(tTileEntity, 48))) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings4) {
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 0) {
								return false;
							}
							tAmount++;
						}
					}
				}
			}
		}
		return tAmount >= 16;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}
}
