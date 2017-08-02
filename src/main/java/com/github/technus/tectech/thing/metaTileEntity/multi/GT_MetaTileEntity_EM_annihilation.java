package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.iConstructible;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_annihilation extends GT_MetaTileEntity_MultiblockBase_EM implements iConstructible {
    //region structure
    private static final String[][] shape = new String[][]{
            {"\u0002","D000","C0   0","C0 . 0","C0   0","D000"},
            {"C01A10","C01A10","D1A1","00B101B00","11111111111","C01110","11111111111","00B101B00","D1A1","C01A10","C01A10",},
            {"C01A10","A223222322","A244242442","03442424430","12225252221","A244222442","12225252221","03442424430","A244242442","A223222322","C01A10",},
            {"D111","A244222442","A4G4","A4G4","12G21","12G21","12G21","A4G4","A4G4","A244222442","D111",},
            {"A0C0C0","03444244430","A4G4","A4G4","A4G4","02G20","A4G4","A4G4","A4G4","03444244430","A0C0C0",},
            {"00C!C00","02444544420","A4G4","A4G4","A4G4","!5G5!","A4G4","A4G4","A4G4","02444544420","00C!C00",},
            {"A0C0C0","03444244430","A4G4","A4G4","A4G4","02G20","A4G4","A4G4","A4G4","03444244430","A0C0C0",},
            {"D111","A244222442","A4G4","A4G4","12G21","12G21","12G21","A4G4","A4G4","A244222442","D111",},
            {"C01A10","A223222322","A244242442","03442424430","12225252221","A244222442","12225252221","03442424430","A244242442","A223222322","C01A10",},
            {"C01A10","C01A10","D1A1","00B101B00","11111111111","C01110","11111111111","00B101B00","D1A1","C01A10","C01A10",},
            {"\u0002","D000","C0   0","C0   0","C0   0","D000"},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT ,sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4, 5, 12, 6, 0, 10};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    //endregion

    public GT_MetaTileEntity_EM_annihilation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_annihilation(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_annihilation(this.mName);
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return EM_StructureCheckAdvanced(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 5, 5, 0);
    }

    @Override
    public void construct(int qty) {
        StructureBuilder(shape, blockType, blockMeta, 5, 5, 0,getBaseMetaTileEntity());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Things+Anti Things don't like each other.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Matter into power!"
        };
    }
}
