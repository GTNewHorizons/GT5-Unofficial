package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputElemental;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_junction extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"   ", " . ", "   ",},
            {"000", "000", "000",},
            {"!!!", "!0!", "!!!",},
            {"!!!", "!!!", "!!!",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Hatches or Molecular Casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_junction(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_junction(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_junction(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMeta,1, 1, 0, getBaseMetaTileEntity(),this,hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Reroutes Matter",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Axis aligned movement!"
        };
    }

    @Override
    public void parametersOutAndStatusesWrite_EM(boolean machineBusy) {
        double src, dest;
        for (int i = 0; i < 10; i++) {
            src = getParameterIn(i, 0);
            if (src <= 0) {
                setStatusOfParameterIn(i, 0, STATUS_TOO_LOW);
                setStatusOfParameterIn(i, 1, STATUS_NEUTRAL);
            } else if (src > eInputHatches.size()) {
                setStatusOfParameterIn(i, 0, STATUS_TOO_HIGH);
                setStatusOfParameterIn(i, 1, STATUS_NEUTRAL);
            } else if (Double.isNaN(src)) {
                setStatusOfParameterIn(i, 0, STATUS_WRONG);
                setStatusOfParameterIn(i, 1, STATUS_NEUTRAL);
            } else {
                setStatusOfParameterIn(i, 0, STATUS_OK);
                dest = getParameterIn(i, 1);
                if (dest < 0) {
                    setStatusOfParameterIn(i, 1, STATUS_TOO_LOW);
                } else if (dest == 0) {
                    setStatusOfParameterIn(i, 1, STATUS_LOW);
                } else if (dest > eOutputHatches.size()) {
                    setStatusOfParameterIn(i, 1, STATUS_TOO_HIGH);
                } else if (Double.isNaN(dest)) {
                    setStatusOfParameterIn(i, 1, STATUS_WRONG);
                } else {
                    setStatusOfParameterIn(i, 1, STATUS_OK);
                }
            }
        }
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_InputElemental in : eInputHatches) {
            if (in.getContainerHandler().hasStacks()) {
                mEUt = -(int) V[8];
                eAmpereFlow = 1 + (eInputHatches.size() + eOutputHatches.size() >> 1);
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                return true;
            }
        }
        return false;
    }

    @Override
    public void outputAfterRecipe_EM() {
        double src,dest;
        for (int i = 0; i < 10; i++) {
            src= getParameterIn(i,0);
            dest= getParameterIn(i,1);
            if(Double.isNaN(src) || Double.isNaN(dest)) {
                continue;
            }
            int inIndex = (int)src - 1;
            if (inIndex < 0 || inIndex >= eInputHatches.size()) {
                continue;
            }
            int outIndex = (int)dest - 1;
            GT_MetaTileEntity_Hatch_InputElemental in = eInputHatches.get(inIndex);
            if (outIndex == -1) {//param==0 -> null the content
                cleanHatchContentEM_EM(in);
            } else {
                if (outIndex < 0 || outIndex >= eOutputHatches.size()) {
                    continue;
                }
                GT_MetaTileEntity_Hatch_OutputElemental out = eOutputHatches.get(outIndex);
                out.getContainerHandler().putUnifyAll(in.getContainerHandler());
                in.getContainerHandler().clear();
            }
        }
    }
}
