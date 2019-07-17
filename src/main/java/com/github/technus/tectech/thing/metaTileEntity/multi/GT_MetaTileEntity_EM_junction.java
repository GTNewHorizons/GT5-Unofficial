package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputElemental;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IHatchAdder;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;
import static gregtech.api.enums.GT_Values.E;

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
    private static final String[][] shapeBig = new String[][]{
            {E, "A   ", "A . ", "A   ",},
            {"A!!!", "!000!", "!010!", "!000!", "A!!!",},
            {"!!!!!", "!000!", "!000!", "!000!", "!!!!!",},
            {"A!!!", "!000!", "!000!", "!000!", "A!!!",},
            {"A!!!", "!!!!!", "!!!!!", "!!!!!", "A!!!",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT,sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4,5};
    private final IHatchAdder[] addingMethods = new IHatchAdder[]{this::addClassicToMachineList, this::addElementalToMachineList};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Hatches or Molecular Casing",
    };
    //endregion

    //region parameters
    private static final INameFunction<GT_MetaTileEntity_EM_junction> ROUTE_NAME=
            (base,p)->(p.parameterId()==0?"Source ":"Destination ")+p.hatchId();
    private static final IStatusFunction<GT_MetaTileEntity_EM_junction> SRC_STATUS =
            (base,p)-> {
                double v = p.get();
                if (Double.isNaN(v)) return STATUS_WRONG;
                v=(int)v;
                if (v < 0) return STATUS_TOO_LOW;
                if (v == 0) return STATUS_NEUTRAL;
                if (v >= base.eInputHatches.size()) return STATUS_TOO_HIGH;
                return STATUS_OK;
            };
    private static final IStatusFunction<GT_MetaTileEntity_EM_junction> DST_STATUS =
            (base,p)->{
                if(base.src[p.hatchId()].getStatus(false)== STATUS_OK){
                    double v = p.get();
                    if (Double.isNaN(v)) return STATUS_WRONG;
                    v=(int)v;
                    if (v < 0) return STATUS_TOO_LOW;
                    if (v == 0) return STATUS_LOW;
                    if (v >= base.eInputHatches.size()) return STATUS_TOO_HIGH;
                    return STATUS_OK;
                }
                return STATUS_NEUTRAL;
            };
    protected Parameters.Group.ParameterIn[] src;
    protected Parameters.Group.ParameterIn[] dst;
    //endregion

    public GT_MetaTileEntity_EM_junction(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_junction(String aName) {
        super(aName);
    }

    @Override
    protected void parametersInstantiation_EM() {
        src=new Parameters.Group.ParameterIn[10];
        dst=new Parameters.Group.ParameterIn[10];
        for (int i = 0; i < 10; i++) {
            Parameters.Group hatch = parametrization.getGroup(i);
            src[i] = hatch.makeInParameter(0, i, ROUTE_NAME, SRC_STATUS);
            dst[i] = hatch.makeInParameter(1, i, ROUTE_NAME, DST_STATUS);
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_junction(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int meta=iGregTechTileEntity.getMetaIDAtSide(GT_Utility.getOppositeSide(iGregTechTileEntity.getFrontFacing()));
        if(meta==4){
            return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
        }else if(meta==5){
            return structureCheck_EM(shapeBig, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0);
        }
        return false;
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
        double src,dst;
        for (int i = 0; i < 10; i++) {
            src= this.src[i].get();
            dst= this.dst[i].get();
            if(Double.isNaN(src) || Double.isNaN(dst)) {
                continue;
            }
            int inIndex = (int)src - 1;
            if (inIndex < 0 || inIndex >= eInputHatches.size()) {
                continue;
            }
            int outIndex = (int)dst - 1;
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
