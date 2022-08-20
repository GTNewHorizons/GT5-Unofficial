package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_stabilizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal(
                "gt.blockmachines.multimachine.em.stabilizer.hint.0"), // 1 - Classic Hatches or High Power Casing
        translateToLocal(
                "gt.blockmachines.multimachine.em.stabilizer.hint.1"), // 2 - Elemental Hatches or Molecular Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_stabilizer> STRUCTURE_DEFINITION =
            IStructureDefinition.<GT_MetaTileEntity_EM_stabilizer>builder()
                    .addShape("main", transpose(new String[][] {
                        {" AFA ", "BCBCB", "FBGBF", "BCBCB", " AFA "},
                        {"AEEEA", "CBBBC", "BBDBB", "CBBBC", "AEEEA"},
                        {"FE~EF", "BBBBB", "GDDDG", "BBBBB", "FEEEF"},
                        {"AEEEA", "CBBBC", "BBDBB", "CBBBC", "AEEEA"},
                        {" AFA ", "BCBCB", "FBGBF", "BCBCB", " AFA "}
                    }))
                    .addElement('A', ofBlock(sBlockCasingsTT, 4))
                    .addElement('B', ofBlock(sBlockCasingsTT, 5))
                    .addElement('C', ofBlock(sBlockCasingsTT, 6))
                    .addElement('D', ofBlock(sBlockCasingsTT, 9))
                    .addElement('F', ofBlock(QuantumGlassBlock.INSTANCE, 0))
                    .addElement(
                            'E',
                            ofHatchAdderOptional(
                                    GT_MetaTileEntity_EM_stabilizer::addClassicToMachineList,
                                    textureOffset,
                                    1,
                                    sBlockCasingsTT,
                                    0))
                    .addElement(
                            'G',
                            ofHatchAdderOptional(
                                    GT_MetaTileEntity_EM_stabilizer::addElementalToMachineList,
                                    textureOffset + 4,
                                    2,
                                    sBlockCasingsTT,
                                    4))
                    .build();
    // endregion

    public GT_MetaTileEntity_EM_stabilizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_stabilizer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_stabilizer(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 2, 2, 0);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal(
                        "gt.blockmachines.multimachine.em.stabilizer.name")) // Machine Type: Elemental Stabilizer
                .addInfo(translateToLocal(
                        "gt.blockmachines.multimachine.em.stabilizer.desc.0")) // Controller block of the
                // Elemental Stabilizer
                .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
                .addSeparator()
                .beginStructureBlock(5, 5, 5, false)
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.Elemental"),
                        translateToLocal("tt.keyword.Structure.SideCenter"),
                        2) // Elemental Hatch: Side center
                .addEnergyHatch(
                        translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"),
                        1) // Energy Hatch: Any High Power Casing
                .addMaintenanceHatch(
                        translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"),
                        1) // Maintenance Hatch: Any High Power Casing
                .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 2, 2, 0, stackSize, hintsOnly);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_stabilizer> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}
