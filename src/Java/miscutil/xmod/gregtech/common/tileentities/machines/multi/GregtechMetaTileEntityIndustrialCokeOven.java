package miscutil.xmod.gregtech.common.tileentities.machines.multi;

import static miscutil.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks.GTID;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;
import java.util.Arrays;

import miscutil.core.block.ModBlocks;
import miscutil.core.lib.CORE;
import miscutil.xmod.gregtech.api.gui.GUI_MultiMachine;
import miscutil.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import miscutil.xmod.gregtech.api.util.GregtechRecipe;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntityIndustrialCokeOven
        extends GregtechMeta_MultiBlockBase {
    private int mLevel = 0;

    public GregtechMetaTileEntityIndustrialCokeOven(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntityIndustrialCokeOven(String aName) {
        super(aName);
    }

    @Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntityIndustrialCokeOven(this.mName);
    }

    @Override
	public String[] getDescription() {
        return new String[]{"Processes Logs and Coal into Charcoal and Coal Coke.",
        		"Controller Block for the Industrial Coke Oven",
        		"Size: 3x3x3 (Hollow)",
        		"Controller (front middle at bottom)",
        		"8x Heat Resistant/Proof Coils (middle Layer, hollow)",
        		"1x Input (one of bottom)",
        		"1x Output (one of bottom)", 
        		"1x Energy Hatch (one of bottom)",
        		"1x Maintenance Hatch (one of bottom)",
        		"1x Muffler Hatch (top middle)",
        		"Structural Coke Oven Casings for the rest",
        		CORE.GT_Tooltip};
    }

    @Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+1], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[GTID+1]};
    }

    @Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "CokeOven.png");
    }

    @Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes;
        
    }

   /* @Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }*/

    @Override
	public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        for (int i = 0; i < tInputList.size() - 1; i++) {
            for (int j = i + 1; j < tInputList.size(); j++) {
                if (GT_Utility.areStacksEqual((ItemStack) tInputList.get(i), (ItemStack) tInputList.get(j))) {
                    if (((ItemStack) tInputList.get(i)).stackSize >= ((ItemStack) tInputList.get(j)).stackSize) {
                        tInputList.remove(j--);
                    } else {
                        tInputList.remove(i--);
                        break;
                    }
                }
            }
        }
        ItemStack[] tInputs = (ItemStack[]) Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

        ArrayList<FluidStack> tFluidList = getStoredFluids();
        for (int i = 0; i < tFluidList.size() - 1; i++) {
            for (int j = i + 1; j < tFluidList.size(); j++) {
                if (GT_Utility.areFluidsEqual((FluidStack) tFluidList.get(i), (FluidStack) tFluidList.get(j))) {
                    if (((FluidStack) tFluidList.get(i)).amount >= ((FluidStack) tFluidList.get(j)).amount) {
                        tFluidList.remove(j--);
                    } else {
                        tFluidList.remove(i--);
                        break;
                    }
                }
            }
        }
        FluidStack[] tFluids = (FluidStack[]) Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tInputList.size()]), 0, 1);
        if (tInputList.size() > 0) {
            long tVoltage = getMaxInputVoltage();
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
            GT_Recipe tRecipe = GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
            if ((tRecipe != null) && (this.mLevel >= tRecipe.mSpecialValue) && (tRecipe.isRecipeInputEqual(true, tFluids, tInputs))) {
                this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;
                if (tRecipe.mEUt <= 16) {
                    this.mEUt = (tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
                    this.mMaxProgresstime = (tRecipe.mDuration / (1 << tTier - 1));
                } else {
                    this.mEUt = tRecipe.mEUt;
                    this.mMaxProgresstime = tRecipe.mDuration;
                    while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                        this.mEUt *= 4;
                        this.mMaxProgresstime /= 2;
                    }
                }
                if (this.mEUt > 0) {
                    this.mEUt = (-this.mEUt);
                }
                this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
                this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
                updateSlots();
                //Utils.LOG_INFO("Coke oven: True");
                return true;
            }
        }
        //Utils.LOG_INFO("Coke oven: False");
        return false;
    }
	/*public boolean checkRecipe(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (!tInputList.isEmpty()) {
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(getMaxInputVoltage()));

            int j = 0;
            this.mOutputItems = new ItemStack[12 * this.mLevel];
            for (int i = 0; (i < 100) && (j < this.mOutputItems.length); i++) {
                if (null != (this.mOutputItems[j] = GT_ModHandler.getSmeltingOutput((ItemStack) tInputList.get(i % tInputList.size()), true, null))) {
                    j++;
                }
            }
            if (j > 0) {
                this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;

                this.mEUt = (-4 * (1 << tTier - 1) * (1 << tTier - 1) * this.mLevel);
                this.mMaxProgresstime = Math.max(1, 512 / (1 << tTier - 1));
            }
            updateSlots();
            return true;
        }
        return false;
    }*/

    @Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int xr = aBaseMetaTileEntity.getXCoord();
    	int yr = aBaseMetaTileEntity.getYCoord();
    	int zr = aBaseMetaTileEntity.getZCoord();
        this.mLevel = 0;
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
            return false;
        }
        addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 2, zDir), GTID+1);

        byte tUsedMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, 1, zDir);
        switch (tUsedMeta) {
            case 2:
                this.mLevel = 1;
                break;
            case 3:
                this.mLevel = 2;
                break;
            default:
                return false;
        }
        this.mOutputItems = new ItemStack[12 * this.mLevel];
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i != 0) || (j != 0)) {
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j) != ModBlocks.blockCasingsMisc) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j) != tUsedMeta) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j) != ModBlocks.blockCasingsMisc) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j) != 1) {
                        return false;
                    }
                }
            }
        }
        for (int i = -1; i < 2; i++) {
        	xr = aBaseMetaTileEntity.getXCoord();
        	yr = aBaseMetaTileEntity.getYCoord();
        	zr = aBaseMetaTileEntity.getZCoord();
        	//Utils.LOG_WARNING("STEP 1 - x ["+xr+"]  y ["+yr+"]  z ["+zr+"]");
            for (int j = -1; j < 2; j++) {
            	xr = aBaseMetaTileEntity.getXCoord();
            	yr = aBaseMetaTileEntity.getYCoord();
            	zr = aBaseMetaTileEntity.getZCoord();
            	//Utils.LOG_WARNING("STEP 2 - x ["+xr+"]  y ["+yr+"]  z ["+zr+"]");
                if ((xDir + i != 0) || (zDir + j != 0)) {
                	xr = aBaseMetaTileEntity.getXCoord();
                	yr = aBaseMetaTileEntity.getYCoord();
                	zr = aBaseMetaTileEntity.getZCoord();
                	//Utils.LOG_WARNING("STEP 3 - x ["+xr+"]  y ["+yr+"]  z ["+zr+"]");
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
                    if ((!addMaintenanceToMachineList(tTileEntity, GTID+1)) && (!addInputToMachineList(tTileEntity, GTID+1)) && (!addOutputToMachineList(tTileEntity, GTID+1)) && (!addEnergyInputToMachineList(tTileEntity, GTID+1))) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != ModBlocks.blockCasingsMisc) {
                            return false;
                        }
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j) != 1) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
	public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
	public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

   /* @Override
	public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }*/

    @Override
	public int getAmountOfOutputs() {
        return 24;
    }

    @Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
