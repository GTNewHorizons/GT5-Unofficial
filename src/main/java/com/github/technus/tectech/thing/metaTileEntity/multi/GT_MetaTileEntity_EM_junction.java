package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_NEUTRAL;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_HIGH;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_WRONG;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputElemental;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_junction extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    // region structure
    // use multi A energy inputs, use less power the longer it runs
    private static final String[] description = new String[] {
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.junction.hint.0"), // 1 - Classic Hatches or High Power
                                                                                  // Casing
            translateToLocal("gt.blockmachines.multimachine.em.junction.hint.1"), // 2 - Elemental Hatches or Molecular
                                                                                  // Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_junction> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_junction>builder()
            .addShape(
                    "main",
                    new String[][] { { "CCC", "C~C", "CCC" }, { "AAA", "AAA", "AAA" }, { "DDD", "DAD", "DDD" },
                            { "DDD", "DDD", "DDD" } })
            .addShape(
                    "mainBig",
                    new String[][] { { "  A  ", " CCC ", "AC~CA", " CCC ", "  A  " },
                            { " DDD ", "DAAAD", "DAAAD", "DAAAD", " DDD " },
                            { "ADDDA", "DAAAD", "DABAD", "DAAAD", "ADDDA" },
                            { " DDD ", "DAAAD", "DAAAD", "DAAAD", " DDD " },
                            { "  A  ", "DDDDD", "ADDDA", "DDDDD", "  A  " } })
            .addElement('A', ofBlock(sBlockCasingsTT, 4)).addElement('B', ofBlock(sBlockCasingsTT, 5))
            .addElement(
                    'C',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_junction::addClassicToMachineList,
                            textureOffset,
                            1,
                            sBlockCasingsTT,
                            0))
            .addElement(
                    'D',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_junction::addElementalToMachineList,
                            textureOffset + 4,
                            2,
                            sBlockCasingsTT,
                            4))
            .build();
    // endregion

    // region parameters
    private static final INameFunction<GT_MetaTileEntity_EM_junction> ROUTE_NAME = (base,
            p) -> (p.parameterId() == 0 ? translateToLocal("tt.keyword.Source") + " "
                    : translateToLocal("tt.keyword.Destination") + " ") + p.hatchId();
    private static final IStatusFunction<GT_MetaTileEntity_EM_junction> SRC_STATUS = (base, p) -> {
        double v = p.get();
        if (Double.isNaN(v)) return STATUS_WRONG;
        v = (int) v;
        if (v < 0) return STATUS_TOO_LOW;
        if (v == 0) return STATUS_NEUTRAL;
        if (v > base.eOutputHatches.size()) return STATUS_TOO_HIGH;
        return STATUS_OK;
    };
    private static final IStatusFunction<GT_MetaTileEntity_EM_junction> DST_STATUS = (base, p) -> {
        if (base.src[p.hatchId()].getStatus(false) == STATUS_OK) {
            double v = p.get();
            if (Double.isNaN(v)) return STATUS_WRONG;
            v = (int) v;
            if (v < 0) return STATUS_TOO_LOW;
            if (v == 0) return STATUS_LOW;
            if (v > base.eInputHatches.size()) return STATUS_TOO_HIGH;
            return STATUS_OK;
        }
        return STATUS_NEUTRAL;
    };
    protected Parameters.Group.ParameterIn[] src;
    protected Parameters.Group.ParameterIn[] dst;
    // endregion

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
        int meta = iGregTechTileEntity
                .getMetaIDAtSideAndDistance(iGregTechTileEntity.getFrontFacing().getOpposite(), 2);
        if (meta == 4) {
            return structureCheck_EM("main", 1, 1, 0);
        } else if (meta == 5) {
            return structureCheck_EM("mainBig", 2, 2, 0);
        }
        return false;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_InputElemental in : eInputHatches) {
            if (in.getContentHandler().hasStacks()) {
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
        double src, dst;
        for (int i = 0; i < 10; i++) {
            src = this.src[i].get();
            dst = this.dst[i].get();
            if (Double.isNaN(src) || Double.isNaN(dst)) {
                continue;
            }
            int inIndex = (int) src - 1;
            if (inIndex < 0 || inIndex >= eInputHatches.size()) {
                continue;
            }
            int outIndex = (int) dst - 1;
            GT_MetaTileEntity_Hatch_InputElemental in = eInputHatches.get(inIndex);
            if (outIndex == -1) { // param==0 -> null the content
                cleanHatchContentEM_EM(in);
            } else {
                if (outIndex < 0 || outIndex >= eOutputHatches.size()) {
                    continue;
                }
                GT_MetaTileEntity_Hatch_OutputElemental out = eOutputHatches.get(outIndex);
                out.getContentHandler().putUnifyAll(in.getContentHandler());
                in.getContentHandler().clear();
            }
        }
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.junction.name")) // Machine Type: Matter
                                                                                              // Junction
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.junction.desc.0")) // Controller block of
                                                                                               // the Matter Junction
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.junction.desc.1")) // Used to route and
                                                                                               // distribute
                // elemental matter
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.junction.desc.2")) // Needs a Parametrizer
                                                                                               // to be configured
                .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
                .addSeparator().beginStructureBlock(3, 3, 4, false)
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalOutput"),
                        translateToLocal("tt.keyword.Structure.AnyOuterMolecularCasing3rd4th"),
                        2) // Elemental Output Hatch: Any outer Molecular Casing on the 3rd or 4th slice
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalInput"),
                        translateToLocal("tt.keyword.Structure.AnyOuterMolecularCasing3rd4th"),
                        2) // Elemental Input Hatch: Any outer Molecular Casing on the 3rd or 4th slice
                .addOtherStructurePart(
                        translateToLocal("gt.blockmachines.hatch.param.tier.05.name"),
                        translateToLocal("tt.keyword.Structure.Optional") + " "
                                + translateToLocal("tt.keyword.Structure.AnyHighPowerCasingFront"),
                        2) // Parametrizer: (optional) Any High Power Casing on the front side
                .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasingFront"), 1) // Energy Hatch:
                                                                                                     // Any High Power
                                                                                                     // Casing on the
                                                                                                     // front side
                .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasingFront"), 1) // Maintenance
                                                                                                          // Hatch: Any
                                                                                                          // High Power
                                                                                                          // Casing on
                                                                                                          // the front
                                                                                                          // side
                .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    protected void parametersInstantiation_EM() {
        src = new Parameters.Group.ParameterIn[10];
        dst = new Parameters.Group.ParameterIn[10];
        for (int i = 0; i < 10; i++) {
            Parameters.Group hatch = parametrization.getGroup(i);
            src[i] = hatch.makeInParameter(0, i, ROUTE_NAME, SRC_STATUS);
            dst[i] = hatch.makeInParameter(1, i, ROUTE_NAME, DST_STATUS);
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if ((stackSize.stackSize & 1) == 1) {
            structureBuild_EM("main", 1, 1, 0, stackSize, hintsOnly);
        } else {
            structureBuild_EM("mainBig", 2, 2, 0, stackSize, hintsOnly);
        }
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_junction> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}
