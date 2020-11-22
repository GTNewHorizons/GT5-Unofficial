package gregtech.common.tileentities.machines.multi;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.loaders.oreprocessing.ProcessingLog;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

public class GT_MetaTileEntity_PyrolyseOven extends GT_MetaTileEntity_MultiBlockBase {
	
	private int coilMetaID;
	//public static GT_CopiedBlockTexture mTextureULV = new GT_CopiedBlockTexture(Block.getBlockFromItem(ItemList.Casing_ULV.get(1).getItem()), 6, 0, Dyes.MACHINE_METAL.mRGBa);
	private final int CASING_INDEX = 1090;

    public GT_MetaTileEntity_PyrolyseOven(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PyrolyseOven(String aName) {
        super(aName);
    }

    public String[] getDescription() {
    	final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType("Coke Oven")
		.addInfo("Controller block for the Pyrolyse Oven")
		.addInfo("Industrial Charcoal producer")
		.addInfo("Processing speed scales linearly with Coil tier:")
		.addInfo("CuNi: 50%, FeAlCr: 100%, Ni4Cr: 150%, Fe50CW: 200%, etc.")
		.addInfo("EU/t is not affected by Coil tier")
		.addPollutionAmount(20 * getPollutionPerTick(null))
		.addSeparator()
		.beginStructureBlock(5, 4, 5, true)
		.addController("Front center")
		.addCasingInfo("Pyrolyse Oven Casing", 60)
		.addOtherStructurePart("Heating Coils (any tier)", "Center 3x1x3 of the bottom layer")
		.addEnergyHatch("Any bottom layer casing")
		.addMaintenanceHatch("Any bottom layer casing")
		.addMufflerHatch("Center 3x1x3 area in top layer")
		.addInputBus("Center 3x1x3 area in top layer")
		.addInputHatch("Center 3x1x3 area in top layer")
		.addOutputBus("Any bottom layer casing")
		.addOutputHatch("Any bottom layer casing")
		.toolTipFinisher("Gregtech");
		if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			return tt.getInformation();
		} else {
			return tt.getStructureInformation();
		}        
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[8][66], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[8][66]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "PyrolyseOven.png");
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        int tInputList_sS=tInputList.size();
        for (int i = 0; i < tInputList_sS - 1; i++) {
            for (int j = i + 1; j < tInputList_sS; j++) {
                if (GT_Utility.areStacksEqual((ItemStack) tInputList.get(i), (ItemStack) tInputList.get(j))) {
                    if (((ItemStack) tInputList.get(i)).stackSize >= ((ItemStack) tInputList.get(j)).stackSize) {
                        tInputList.remove(j--); tInputList_sS=tInputList.size();
                    } else {
                        tInputList.remove(i--); tInputList_sS=tInputList.size();
                        break;
                    }
                }
            }
        }
        ItemStack[] tInputs = (ItemStack[]) Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

        ArrayList<FluidStack> tFluidList = getStoredFluids();
        int tFluidList_sS=tFluidList.size();
        for (int i = 0; i < tFluidList_sS - 1; i++) {
            for (int j = i + 1; j < tFluidList_sS; j++) {
                if (GT_Utility.areFluidsEqual((FluidStack) tFluidList.get(i), (FluidStack) tFluidList.get(j))) {
                    if (((FluidStack) tFluidList.get(i)).amount >= ((FluidStack) tFluidList.get(j)).amount) {
                        tFluidList.remove(j--); tFluidList_sS=tFluidList.size();
                    } else {
                        tFluidList.remove(i--); tFluidList_sS=tFluidList.size();
                        break;
                    }
                }
            }
        }
        FluidStack[] tFluids = (FluidStack[]) Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tInputList.size()]), 0, 1);
        if (tInputList.size() > 0) {
            long tVoltage = getMaxInputVoltage();
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sPyrolyseRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);

            //Dynamic recipe adding for newly found logWoods - wont be visible in nei most probably
            if(tRecipe==null){
                for(ItemStack is:tInputs) {
                    for (int id : OreDictionary.getOreIDs(is)) {
                        if (OreDictionary.getOreName(id).equals("logWood"))
                            ProcessingLog.addPyrolyeOvenRecipes(is);
                    }
                }
                tRecipe = GT_Recipe.GT_Recipe_Map.sPyrolyseRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
            }

            if (tRecipe != null && tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
                this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;

                calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, tVoltage);
                //In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                    return false;
                if (this.mEUt > 0) {
                    this.mEUt = (-this.mEUt);
                }
                this.mMaxProgresstime = Math.max(mMaxProgresstime * 2 / (1 + coilMetaID), 1);
                if (tRecipe.mOutputs.length > 0) this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
                if (tRecipe.mFluidOutputs.length > 0)
                    this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
                updateSlots();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;

        Block CasingBlock= Loader.isModLoaded("dreamcraft")? GameRegistry.findBlock("dreamcraft","gt.blockcasingsNH"): GregTech_API.sBlockCasings1;
        int CasingMeta= Loader.isModLoaded("dreamcraft")?2:0;

        replaceDeprecatedCoils(aBaseMetaTileEntity);
        boolean firstCoil = true;
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                for (int h = 0; h < 4; h++) {
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                    if ((i != -2 && i != 2) && (j != -2 && j != 2)) {// inner 3x3 without height
                        if (h == 0) {// inner floor (Cupronickel or Kanthal coils)
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings5) {
                                return false;
                            }
                            int metaID = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                            if (metaID > 8) {
                                return false;
                            } else {
                            	if (firstCoil) {
                            		this.coilMetaID = metaID;
                            		firstCoil = false;
                            	} else if (metaID != this.coilMetaID) {
                            		return false;
                            	}
                            }
                        } else if (h == 3) {// inner ceiling (ulv casings + input + muffler)
                            if ((!addInputToMachineList(tTileEntity, CASING_INDEX)) && (!addMufflerToMachineList(tTileEntity, CASING_INDEX))) {
                                if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != CasingBlock) {
                                    return false;
                                }
                                if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != CasingMeta) {
                                    return false;
                                }
                            }
                        } else {// inside air
                            if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
                                return false;
                            }
                        }
                    } else {// outer 5x5 without height
                        if (h == 0) {// outer floor (controller, output, energy, maintainance, rest ulv casings)
                            if ((!addMaintenanceToMachineList(tTileEntity, CASING_INDEX)) && (!addOutputToMachineList(tTileEntity, CASING_INDEX)) && (!addEnergyInputToMachineList(tTileEntity, CASING_INDEX))) {
                                if ((xDir + i != 0) || (zDir + j != 0)) {//no controller
                                    if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != CasingBlock) {
                                        return false;
                                    }
                                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != CasingMeta) {
                                        return false;
                                    }
                                }
                            }
                        } else {// outer above floor (ulv casings)
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != CasingBlock) {
                                return false;
                            }
                            if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != CasingMeta) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
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
        return 30;
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
        return new GT_MetaTileEntity_PyrolyseOven(this.mName);
    }

    private void replaceDeprecatedCoils(IGregTechTileEntity aBaseMetaTileEntity) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int tX = aBaseMetaTileEntity.getXCoord() + xDir * 2;
        int tY = (int) aBaseMetaTileEntity.getYCoord();
        int tZ = aBaseMetaTileEntity.getZCoord() + zDir * 2;
        for (int xPos = tX - 1; xPos <= tX + 1; xPos++) {
            for (int zPos = tZ - 1; zPos <= tZ + 1; zPos++) {
                if (aBaseMetaTileEntity.getBlock(xPos, tY, zPos) == GregTech_API.sBlockCasings1 &&
                    aBaseMetaTileEntity.getMetaID(xPos, tY, zPos) == 13)
                {
                    aBaseMetaTileEntity.getWorld().setBlock(xPos, tY, zPos, GregTech_API.sBlockCasings5, 1, 3);
                }
            }
        }
    }
}
