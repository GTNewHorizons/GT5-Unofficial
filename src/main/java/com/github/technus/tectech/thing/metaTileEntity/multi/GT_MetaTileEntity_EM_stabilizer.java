package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.IGT_HatchAdder;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.mechanics.structure.Structure.adders;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_stabilizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region structure
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.stabilizer.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.stabilizer.hint.1"),//2 - Elemental Hatches or Molecular Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_stabilizer> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_MetaTileEntity_EM_stabilizer>builder()
            .addShape("main", new String[][]{
                    {" AFA ","BCBCB","FBGBF","BCBCB"," AFA "},
                    {"AEEEA","CBBBC","BBDBB","CBBBC","AEEEA"},
                    {"FE~EF","BBBBB","GDDDG","BBBBB","FEEEF"},
                    {"AEEEA","CBBBC","BBDBB","CBBBC","AEEEA"},
                    {" AFA ","BCBCB","FBGBF","BCBCB"," AFA "}
            })
            .addElement('A', ofBlock(sBlockCasingsTT, 4))
            .addElement('B', ofBlock(sBlockCasingsTT, 5))
            .addElement('C', ofBlock(sBlockCasingsTT, 6))
            .addElement('D', ofBlock(sBlockCasingsTT, 9))
            .addElement('E', ofHatchAdderOptional(GT_MetaTileEntity_EM_stabilizer::addClassicToMachineList, textureOffset, 1, sBlockCasingsTT, 0))
            .addElement('F', ofBlock(QuantumGlassBlock.INSTANCE, 0))
            .addElement('G', ofHatchAdderOptional(GT_MetaTileEntity_EM_stabilizer::addElementalToMachineList, textureOffset + 4, 2, sBlockCasingsTT, 4))
            .build();

//    private static final String[][] shape = new String[][]{
//            {"A010", "0   0", "1 . 1", "0   0", "A010",},
//            {"23232", "32223", "22222", "32223", "23232",},
//            {"12!21", "22422", "!444!", "22422", "12!21",},
//            {"23232", "32223", "22222", "32223", "23232",},
//            {"A010", "0   0", "1   1", "0   0", "A010",},
//    };
//    private static final Block[] blockType = new Block[]{sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
//    private static final byte[] blockMeta = new byte[]{4, 0, 5, 6, 9};
//    private static final IGT_HatchAdder<GT_MetaTileEntity_EM_stabilizer>[] addingMethods = adders(
//            GT_MetaTileEntity_EM_stabilizer::addClassicToMachineList,
//            GT_MetaTileEntity_EM_stabilizer::addElementalToMachineList);
//    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
//    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
//    private static final byte[] blockMetaFallback = new byte[]{0, 4};

    //endregion

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
        //return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0);
        return structureCheck_EM("main", 2, 2, 0);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.stabilizer.desc.0"),//Alters time to stabilize matter
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.stabilizer.desc.1")//Wibbly wobbly timey wimey, stuff.
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        //Structure.builder(shape, blockType, blockMeta, 2, 2, 0, getBaseMetaTileEntity(), getExtendedFacing(), hintsOnly);
        structureBuild_EM("main", 2, 2, 0, hintsOnly, stackSize);
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