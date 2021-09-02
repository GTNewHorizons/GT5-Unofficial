package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sHintCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_annihilation extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.annihilation.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.annihilation.hint.1"),//2 - Elemental Hatches or Molecular Casing
    };
    //endregion

    //region structure
    private static final IStructureDefinition<GT_MetaTileEntity_EM_annihilation> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_MetaTileEntity_EM_annihilation>builder()
                    .addShape("main", new String[][]{
                            {"           ","   AB BA   ","   AB BA   ","    BBB    "," A   A   A ","AA   F   AA"," A   A   A ","    BBB    ","   AB BA   ","   AB BA   ","           "},
                            {"           ","   AB BA   "," EECEEECEE "," EGGEEEGGE ","ACGGGEGGGCA","AEGGGDGGGEA","ACGGGEGGGCA"," EGGEEEGGE "," EECEEECEE ","   AB BA   ","           "},
                            {"           ","    B B    "," EGGEGEGGE "," G       G "," G       G "," G       G "," G       G "," G       G "," EGGEGEGGE ","    B B    ","           "},
                            {"    AAA    ","AA  BAB  AA","ACGGEGEGGCA"," G       G "," G       G "," G       G "," G       G "," G       G ","ACGGEGEGGCA","AA  BAB  AA","    AAA    "},
                            {"   AHHHA   ","BBBBBBBBBBB","BEEEDEDEEEB","BE       EB"," G       G "," G       G "," G       G ","BE       EB","BEEEDEDEEEB","BBBBBBBBBBB","   AHHHA   "},
                            {"   AH~HA   ","   ABBBA   "," EGGEEEGGE ","BE       EB","AE       EA","FD       DF","AE       EA","BE       EB"," EGGEEEGGE ","   ABBBA   ","   AHHHA   "},
                            {"   AHHHA   ","BBBBBBBBBBB","BEEEDEDEEEB","BE       EB"," G       G "," G       G "," G       G ","BE       EB","BEEEDEDEEEB","BBBBBBBBBBB","   AHHHA   "},
                            {"    AAA    ","AA  BAB  AA","ACGGEGEGGCA"," G       G "," G       G "," G       G "," G       G "," G       G ","ACGGEGEGGCA","AA  BAB  AA","    AAA    "},
                            {"           ","    B B    "," EGGEGEGGE "," G       G "," G       G "," G       G "," G       G "," G       G "," EGGEGEGGE ","    B B    ","           "},
                            {"           ","   AB BA   "," EECEEECEE "," EGGEEEGGE ","ACGGGEGGGCA","AEGGGDGGGEA","ACGGGEGGGCA"," EGGEEEGGE "," EECEEECEE ","   AB BA   ","           "},
                            {"           ","   AB BA   ","   AB BA   ","    BBB    "," A   A   A ","AA   F   AA"," A   A   A ","    BBB    ","   AB BA   ","   AB BA   ","           "}
                    })
                    .addElement('A', ofBlock(sBlockCasingsTT, 4))
                    .addElement('B', ofBlock(sBlockCasingsTT, 5))
                    .addElement('C', ofBlock(sBlockCasingsTT, 6))
                    .addElement('D', ofBlock(sBlockCasingsTT, 10))
                    .addElement('E', ofBlock(sBlockCasingsTT, 12))
                    .addElement('F', ofHatchAdderOptional(GT_MetaTileEntity_MultiblockBase_EM::addElementalToMachineList, textureOffset + 4, sHintCasingsTT, 0, sBlockCasingsTT, 4))
                    .addElement('G', ofBlock(QuantumGlassBlock.INSTANCE, 0))
                    .addElement('H', ofHatchAdderOptional(GT_MetaTileEntity_EM_annihilation::addClassicToMachineList, textureOffset, sHintCasingsTT, 0, sBlockCasingsTT, 0))
                    .build();

    /*
    private static final String[][] shape = new String[][]{
            {"\u0002", "D000", "C0   0", "C0 . 0", "C0   0", "D000"},
            {"C01A10", "C01A10", "D1A1", "00B101B00", "11111111111", "C01110", "11111111111", "00B101B00", "D1A1", "C01A10", "C01A10",},
            {"C01A10", "A223222322", "A244242442", "03442424430", "12225252221", "A244222442", "12225252221", "03442424430", "A244242442", "A223222322", "C01A10",},
            {"D111", "A244222442", "A4G4", "A4G4", "12G21", "12G21", "12G21", "A4G4", "A4G4", "A244222442", "D111",},
            {"A0C0C0", "03444244430", "A4G4", "A4G4", "A4G4", "02G20", "A4G4", "A4G4", "A4G4", "03444244430", "A0C0C0",},
            {"00C!C00", "02444544420", "A4G4", "A4G4", "A4G4", "!5G5!", "A4G4", "A4G4", "A4G4", "02444544420", "00C!C00",},
            {"A0C0C0", "03444244430", "A4G4", "A4G4", "A4G4", "02G20", "A4G4", "A4G4", "A4G4", "03444244430", "A0C0C0",},
            {"D111", "A244222442", "A4G4", "A4G4", "12G21", "12G21", "12G21", "A4G4", "A4G4", "A244222442", "D111",},
            {"C01A10", "A223222322", "A244242442", "03442424430", "12225252221", "A244222442", "12225252221", "03442424430", "A244242442", "A223222322", "C01A10",},
            {"C01A10", "C01A10", "D1A1", "00B101B00", "11111111111", "C01110", "11111111111", "00B101B00", "D1A1", "C01A10", "C01A10",},
            {"\u0002", "D000", "C0   0", "C0   0", "C0   0", "D000"},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4, 5, 12, 6, 0, 10};
    private static final IGT_HatchAdder<GT_MetaTileEntity_EM_annihilation>[] addingMethods = adders(
            GT_MetaTileEntity_EM_annihilation::addClassicToMachineList,
            GT_MetaTileEntity_EM_annihilation::addElementalToMachineList);
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    */

    //endregion

    public GT_MetaTileEntity_EM_annihilation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_annihilation(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_annihilation(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        //return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 5, 5, 0);
        return structureCheck_EM("main", 5, 5, 0);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.annihilation.desc.0"),//Things+Anti Things don't like each other.
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.annihilation.desc.1")//Matter into power!
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_ANNIHILATION");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_ANNIHILATION_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12], new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12]};
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        //Structure.builder(shape, blockType, blockMeta, 5, 5, 0, getBaseMetaTileEntity(), getExtendedFacing(), hintsOnly);
        structureBuild_EM("main", 5, 5, 0, hintsOnly, stackSize);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_annihilation> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}