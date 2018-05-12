package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_Adv_EBF extends GregtechMeta_MultiBlockBase {
    
    
	public static int CASING_TEXTURE_ID;
	public static String mHotFuelName = "Blazing Pyrotheum";
	public static String mCasingName = "Advanced Blast Furnace Casing";
    
    private int mHeatingCapacity = 0;
    private int controllerY;
    private FluidStack[] pollutionFluidStacks = new FluidStack[]{Materials.CarbonDioxide.getGas(1000), 
    		Materials.CarbonMonoxide.getGas(1000), Materials.SulfurDioxide.getGas(1000)};

    public GregtechMetaTileEntity_Adv_EBF(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 10);
		mHotFuelName = FluidUtils.getFluidStack("pyrotheum", 1).getLocalizedName();
		mCasingName = GregtechItemList.Casing_AdvancedVacuum.get(1).getDisplayName();
    }

    public GregtechMetaTileEntity_Adv_EBF(String aName) {
        super(aName);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 10);
		mHotFuelName = FluidUtils.getFluidStack("pyrotheum", 1).getLocalizedName();
		mCasingName = GregtechItemList.Casing_AdvancedVacuum.get(1).getDisplayName();
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Adv_EBF(this.mName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Advanced Electric Blast Furnace",
				"Consumes 1L of "+mHotFuelName+"/t during operation",
                "Size(WxHxD): 3x4x3 (Hollow), Controller (Front middle bottom)",
                "16x Heating Coils (Two middle Layers, hollow)",
                "1x Input Hatch/Bus (Any bottom layer casing)",
                "1x Output Hatch/Bus (Any bottom layer casing)",
                "1x Energy Hatch (Any bottom layer casing)",
                "1x Maintenance Hatch (Any bottom layer casing)",
                "1x Muffler Hatch (Top middle)",
                "1x Output Hatch to recover CO2/CO/SO2 (optional, any top layer casing),",
                "    Recovery scales with Muffler Hatch tier",
                 mCasingName+"s for the rest",
                "Each 900K over the min. Heat Capacity grants 5% speedup (multiplicatively)",
                "Each 1800K over the min. Heat Capacity allows for one upgraded overclock",
                "Upgraded overclocks reduce recipe time to 25% and increase EU/t to 400%",
                "Causes " + 20 * getPollutionPerTick(null) + " Pollution per second"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[11], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[11]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ElectricBlastFurnace.png");
    }

    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sBlastRecipes;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    public boolean checkRecipe(ItemStack aStack) {
        return checkRecipeGeneric(4, 100, 100); //Will have to clone the logic from parent class to handle heating coil tiers.
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
    	controllerY = aBaseMetaTileEntity.getYCoord();
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

        this.mHeatingCapacity = 0;
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
            return false;
        }
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 2, zDir)) {
            return false;
        }
        if (!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir), 11)) {
            return false;
        }
        byte tUsedMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, 2, zDir);
        switch (tUsedMeta) {
            case 0:
                this.mHeatingCapacity = 1800;
                break;
            case 1:
                this.mHeatingCapacity = 2700;
                break;
            case 2:
                this.mHeatingCapacity = 3600;
                break;
            case 3:
                this.mHeatingCapacity = 4500;
                break;
            case 4:
                this.mHeatingCapacity = 5400;
                break;
            case 5:
                this.mHeatingCapacity = 7200;
                break;
            case 6:
                this.mHeatingCapacity = 9001;
                break;
            default:
                return false;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i != 0) || (j != 0)) {
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j) != GregTech_API.sBlockCasings5) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j) != tUsedMeta) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j) != GregTech_API.sBlockCasings5) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j) != tUsedMeta) {
                        return false;
                    }
                    if (!addOutputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 3, zDir + j), 11)) {
                    	if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 3, zDir + j) != GregTech_API.sBlockCasings1) {
                    		return false;
                    	}
                    	if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 3, zDir + j) != 11) {
                    		return false;
                    	}
                    }
                }
            }
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((xDir + i != 0) || (zDir + j != 0)) {
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
                    if ((!addMaintenanceToMachineList(tTileEntity, 11)) && (!addInputToMachineList(tTileEntity, 11)) && (!addOutputToMachineList(tTileEntity, 11)) && (!addEnergyInputToMachineList(tTileEntity, 11))) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != GregTech_API.sBlockCasings1) {
                            return false;
                        }
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j) != 11) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
        return 50;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
    
    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        int targetHeight;
        FluidStack tLiquid = aLiquid.copy();
        boolean isOutputPollution = false;
        for (FluidStack pollutionFluidStack : pollutionFluidStacks) {
        	if (tLiquid.isFluidEqual(pollutionFluidStack)) {
        		isOutputPollution = true;
        		break;
        	}
        }
    	if (isOutputPollution) {
    		targetHeight = this.controllerY + 3;
    		int pollutionReduction = 0;
            for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
                if (isValidMetaTileEntity(tHatch)) {
                	pollutionReduction = 100 - tHatch.calculatePollutionReduction(100);
                	break;
                }
            }
            tLiquid.amount = tLiquid.amount * (pollutionReduction + 5) / 100;
    	} else {
    		targetHeight = this.controllerY;
    	}
        for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
            if (isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid) ? tHatch.outputsSteam() : tHatch.outputsLiquids()) {
            	if (tHatch.getBaseMetaTileEntity().getYCoord() == targetHeight) {
            		int tAmount = tHatch.fill(tLiquid, false);
                	if (tAmount >= tLiquid.amount) {
                    	return tHatch.fill(tLiquid, true) >= tLiquid.amount;
                	} else if (tAmount > 0) {
                    	tLiquid.amount = tLiquid.amount - tHatch.fill(tLiquid, true);
                	}
            	}
            }
        }
        return false;
    }

	@Override
	public boolean hasSlotInGUI() {
		// TODO Auto-generated method stub
		return false;
	}

}