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
import gregtech.api.util.IGT_HatchAdder;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.mechanics.structure.Structure.adders;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_wormhole extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {//TODO MAKE COMPATIBLE WITH STARGATES XD
    //region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    //endregion

    //region structure
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.wormhole.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.wormhole.hint.1"),//2 - Elemental Hatches or Molecular Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_wormhole> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_MetaTileEntity_EM_wormhole>builder()
            .addShape("main", new String[][]{
                    {"         ","         ","         ","         ","         ","    D    ","  DDDDD  ","  DGGGD  ","  DGGGD  ","  DGGGD  ","  DDDDD  "},
                    {"         ","         ","         ","    D    ","    D    ","    D    "," DDAAADD "," EABBBAE "," EABBBAE "," EABBBAE "," DDAAADD "},
                    {"         ","    D    ","    D    ","    D    ","         ","         ","DDA   ADD","DABFFFBAD","DABCCCBAD","DABFFFBAD","DDA   ADD"},
                    {"   EEE   ","   DDD   ","   F F   ","         ","         ","         ","DA     AD","GBF   FBG","GBC   CBG","GBF   FBG","DA     AD"},
                    {"   E~E   ","  DDBDD  ","  D   D  "," DD   DD "," D     D ","DD     DD","DA     AD","GBF   FBG","GBC   CBG","GBF   FBG","DA     AD"},
                    {"   EEE   ","   DDD   ","   F F   ","         ","         ","         ","DA     AD","GBF   FBG","GBC   CBG","GBF   FBG","DA     AD"},
                    {"         ","    D    ","    D    ","    D    ","         ","         ","DDA   ADD","DABFFFBAD","DABCCCBAD","DABFFFBAD","DDA   ADD"},
                    {"         ","         ","         ","    D    ","    D    ","    D    "," DDAAADD "," EABBBAE "," EABBBAE "," EABBBAE "," DDAAADD "},
                    {"         ","         ","         ","         ","         ","    D    ","  DDDDD  ","  DGGGD  ","  DGGGD  ","  DGGGD  ","  DDDDD  "}
            })
            .addElement('A', ofBlock(sBlockCasingsTT, 5))
            .addElement('B', ofBlock(sBlockCasingsTT, 10))
            .addElement('C', ofBlock(sBlockCasingsTT, 11))
            .addElement('D', ofBlock(sBlockCasingsTT, 12))
            .addElement('E', ofHatchAdderOptional(GT_MetaTileEntity_EM_wormhole::addClassicToMachineList, textureOffset, 1, sBlockCasingsTT, 0))
            .addElement('F', ofBlock(QuantumGlassBlock.INSTANCE, 0))
            .addElement('G', ofHatchAdderOptional(GT_MetaTileEntity_EM_wormhole::addElementalToMachineList, textureOffset + 4, 2, sBlockCasingsTT, 4))
            .build();

//    private static final String[][] shape = new String[][]{
//            {E, E, E, "C   ", "C . ", "C   "/*,E,E,E,*/},
//            {E, E, "D0", "C000", "B00100", "C000", "D0"/*,E,E,*/},
//            {E, E, "D0", "C2A2", "B0C0", "C2A2", "D0"/*,E,E,*/},
//            {E, "D0", "D0", E, "A00C00", E, "D0", "D0"/*,E,*/},
//            {E, "D0", E, E, "A0E0", E, E, "D0"/*,E,*/},
//            {"D0", "D0", E, E, "00E00", E, E, "D0", "D0",},
//            {"B00000", "A0033300", "003C300", "03E30", "03E30", "03E30", "003C300", "A0033300", "B00000",},
//            {"B0!!!0", "A 31113 ", "031222130", "!12C21!", "!12C21!", "!12C21!", "031222130", "A 31113 ", "B0!!!0",},
//            {"B0!!!0", "A 31113 ", "031444130", "!14C41!", "!14C41!", "!14C41!", "031444130", "A 31113 ", "B0!!!0",},
//            {"B0!!!0", "A 31113 ", "031222130", "!12C21!", "!12C21!", "!12C21!", "031222130", "A 31113 ", "B0!!!0",},
//            {"B00000", "A0033300", "003C300", "03E30", "03E30", "03E30", "003C300", "A0033300", "B00000",},
//    };
//    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT};
//    private static final byte[] blockMeta = new byte[]{12, 10, 0, 5, 11};
//    private static final IGT_HatchAdder<GT_MetaTileEntity_EM_wormhole>[] addingMethods = adders(
//            GT_MetaTileEntity_EM_wormhole::addClassicToMachineList,
//            GT_MetaTileEntity_EM_wormhole::addElementalToMachineList);
//    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
//    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
//    private static final byte[] blockMetaFallback = new byte[]{0, 4};

    //endregion

    public GT_MetaTileEntity_EM_wormhole(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_wormhole(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_wormhole(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        //return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 4, 4, 0);
        return structureCheck_EM("main", 4, 4, 0);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.wormhole.desc.0"),//It is not full of worms.
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.wormhole.desc.1")//It is full of anti-worms!!!
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_WH");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_WH_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4], new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4]};
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        //Structure.builder(shape, blockType, blockMeta, 4, 4, 0, getBaseMetaTileEntity(), getExtendedFacing(), hintsOnly);
        structureBuild_EM("main", 4, 4, 0, hintsOnly, stackSize);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_wormhole> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}