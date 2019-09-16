package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class GT_MetaTileEntity_OilCracker extends GT_MetaTileEntity_MultiBlockBase {
    private ForgeDirection orientation;
    private int controllerX, controllerZ;

    public GT_MetaTileEntity_OilCracker(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_OilCracker(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Oil Cracking Unit",
                "Thermally cracks heavy hydrocarbons into lighter fractions",
                "Size(WxHxD): 5x3x3 (Hollow), Controller (Front center)",
                "Ring of 8 Cupronickel Coils (Each side of Controller)",
                "1x Hydrocarbon Input Bus/Hatch (Any left/right side casing)",
                "1x Steam/Hydrogen Input Hatch (Any middle ring casing)",
                "1x Cracked Hydrocarbon Output Hatch (Any left/right side casing)",
                "1x Maintenance Hatch (Any casing)",
                "1x Energy Hatch (Any casing)",
                "Clean Stainless Steel Machine Casings for the rest (18 at least!)",
                "Input/Output Hatches must be on opposite sides"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[49],
                    new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[49]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "OilCrackingUnit.png");
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<FluidStack> tInputList = getStoredFluids();
        FluidStack[] tFluidInputs = tInputList.toArray(new FluidStack[tInputList.size()]);
        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sCrakingRecipes.findRecipe(
                getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluidInputs ,new ItemStack[]{mInventory[1]});
        if (tRecipe != null && tRecipe.isRecipeInputEqual(true, tFluidInputs, new ItemStack[]{mInventory[1]})) {
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            this.mEUt = tRecipe.mEUt;
            this.mMaxProgresstime = tRecipe.mDuration;
            while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                this.mEUt *= 4;
                this.mMaxProgresstime /= 2;
            }
            if (this.mEUt > 0) {
                this.mEUt = (-this.mEUt);
            }
            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
            this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.orientation = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing());
        this.controllerX = aBaseMetaTileEntity.getXCoord();
        this.controllerZ = aBaseMetaTileEntity.getZCoord();
        int xDir = this.orientation.offsetX;
        int zDir = this.orientation.offsetZ;
        int amount = 0;
        replaceDeprecatedCoils(aBaseMetaTileEntity);
        boolean negSideInput = false, negSideOutput = false, posSideInput = false, posSideOutput = false;
        if (xDir != 0) {
            for (int i = -1; i < 2; i++) {// xDirection
                for (int j = -1; j < 2; j++) {// height
                    for (int h = -2; h < 3; h++) {
                        if (!(j == 0 && i == 0 && (h == -1 || h == 0 || h == 1))) {
                            if (h == 1 || h == -1) {
                                if (aBaseMetaTileEntity.getBlockOffset(xDir + i, j, h + zDir) != GregTech_API.sBlockCasings5) {
                                    return false;
                                }
                                if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, j, h + zDir) != 0) {
                                    return false;
                                }
                            }
                            if (h == 2 || h == -2) {
                                IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, j, h + zDir);
                                if (addInputToMachineList(tTileEntity, 49)) {
                                	if (h == -2) {
                                		negSideInput = true;
                                	} else {
                                		posSideInput = true;
                                	}
                                } else if (addOutputToMachineList(tTileEntity, 49)) {
                                	if (h == -2) {
                                		negSideOutput = true;
                                	} else {
                                		posSideOutput = true;
                                	}                                	
                                } else if (!addEnergyInputToMachineList(tTileEntity, 49) && !addMaintenanceToMachineList(tTileEntity, 49)){
                                    if (aBaseMetaTileEntity.getBlockOffset(xDir + i, j, h + zDir) != GregTech_API.sBlockCasings4) {
                                        return false;
                                    }
                                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, j, h + zDir) != 1) {
                                        return false;
                                    }
                                    amount++;
                                }
                            }
                            if (h == 0) {
                                IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, j, h + zDir);
                                if ((!addMaintenanceToMachineList(tTileEntity, 49)) && (!addInputToMachineList(tTileEntity, 49))
                                        && (!addEnergyInputToMachineList(tTileEntity, 49))) {
                                    if (!((xDir + i) == 0 && j == 0 && (h + zDir) == 0)) {
                                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, j, h + zDir) != GregTech_API.sBlockCasings4) {
                                            return false;
                                        }
                                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, j, h + zDir) != 1) {
                                            return false;
                                        }
                                        amount++;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        } else {
            for (int i = -1; i < 2; i++) {// zDirection
                for (int j = -1; j < 2; j++) {// height
                    for (int h = -2; h < 3; h++) {
                        if (!(j == 0 && i == 0 && (h == -1 || h == 0 || h == 1))) {
                            if (h == 1 || h == -1) {
                                if (aBaseMetaTileEntity.getBlockOffset(xDir + h, j, i + zDir) != GregTech_API.sBlockCasings5) {
                                    return false;
                                }
                                if (aBaseMetaTileEntity.getMetaIDOffset(xDir + h, j, i + zDir) != 0) {
                                    return false;
                                }
                            }
                            if (h == 2 || h == -2) {
                                IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + h, j, i + zDir);
                                if (addInputToMachineList(tTileEntity, 49)) {
                                	if (h == -2) {
                                		negSideInput = true;
                                	} else {
                                		posSideInput = true;
                                	}
                                } else if (addOutputToMachineList(tTileEntity, 49)) {
                                	if (h == -2) {
                                		negSideOutput = true;
                                	} else {
                                		posSideOutput = true;
                                	}                                	
                                } else if (!addEnergyInputToMachineList(tTileEntity, 49) && !addMaintenanceToMachineList(tTileEntity, 49)){
                                    if (aBaseMetaTileEntity.getBlockOffset(xDir + h, j, i + zDir) != GregTech_API.sBlockCasings4) {
                                        return false;
                                    }
                                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + h, j, i + zDir) != 1) {
                                        return false;
                                    }
                                    amount++;
                                }
                            }
                            if (h == 0) {
                                IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + h, j, i + zDir);
                                if ((!addMaintenanceToMachineList(tTileEntity, 49)) && (!addInputToMachineList(tTileEntity, 49))
                                        && (!addEnergyInputToMachineList(tTileEntity, 49))) {
                                    if (!((xDir + h) == 0 && j == 0 && (i + zDir) == 0)) {
                                        if (aBaseMetaTileEntity.getBlockOffset(xDir + h, j, i + zDir) != GregTech_API.sBlockCasings4) {
                                            return false;
                                        }
                                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + h, j, i + zDir) != 1) {
                                            return false;
                                        }
                                        amount++;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        if ((negSideInput && negSideOutput) || (posSideInput && posSideOutput) 
        		|| (negSideInput && posSideInput) || (negSideOutput && posSideOutput)) {
        	return false;
        }
        if (amount < 18) return false;
        return true;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
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

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_OilCracker(this.mName);
    }

    private void replaceDeprecatedCoils(IGregTechTileEntity aBaseMetaTileEntity) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int tX = aBaseMetaTileEntity.getXCoord() + xDir;
        int tY = (int) aBaseMetaTileEntity.getYCoord();
        int tZ = aBaseMetaTileEntity.getZCoord() + zDir;
        for (int xPos = tX - 1; xPos <= tX + 1; xPos += (xDir != 0 ? 1 : 2)) {
            for (int yPos = tY - 1; yPos <= tY + 1; yPos++) {
                for (int zPos = tZ - 1; zPos <= tZ + 1; zPos += (xDir != 0 ? 2 : 1)) {
                    if ((yPos == tY) && (xPos == tX || zPos == tZ)) {
                        continue;
                    }
                    if (aBaseMetaTileEntity.getBlock(xPos, yPos, zPos) == GregTech_API.sBlockCasings1 &&
                            aBaseMetaTileEntity.getMetaID(xPos, yPos, zPos) == 12)
                    {
                        aBaseMetaTileEntity.getWorld().setBlock(xPos, yPos, zPos, GregTech_API.sBlockCasings5, 0, 3);
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<FluidStack>();
        for (GT_MetaTileEntity_Hatch_Input tHatch : mInputHatches) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch) && tHatch.getFillableStack() != null) {
                FluidStack tStack = tHatch.getFillableStack();
                if (tStack.isFluidEqual(GT_ModHandler.getSteam(1000)) || tStack.isFluidEqual(Materials.Hydrogen.getGas(1000))) {
                    if (isHatchInMiddleRing(tHatch)) {
                        rList.add(tStack);
                    }
                } else {
                    if (!isHatchInMiddleRing(tHatch)) {
                        rList.add(tStack);
                    }
                }
            }
        }
        return rList;
    }

    private boolean isHatchInMiddleRing(GT_MetaTileEntity_Hatch_Input inputHatch){
        if (orientation == ForgeDirection.NORTH || orientation == ForgeDirection.SOUTH) {
            return inputHatch.getBaseMetaTileEntity().getXCoord() == this.controllerX;
        } else {
            return inputHatch.getBaseMetaTileEntity().getZCoord() == this.controllerZ;
        }
    }
}