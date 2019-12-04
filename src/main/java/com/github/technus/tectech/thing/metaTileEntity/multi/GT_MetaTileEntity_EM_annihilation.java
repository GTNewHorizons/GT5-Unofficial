package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IHatchAdder;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_annihilation extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    //endregion

    //region structure
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
    private final IHatchAdder[] addingMethods = new IHatchAdder[]{this::addClassicToMachineList, this::addElementalToMachineList};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.annihilation.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.annihilation.hint.1"),//2 - Elemental Hatches or Molecular Casing
    };
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
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 5, 5, 0);
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
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12], new TT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12]};
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMeta, 5, 5, 0, getBaseMetaTileEntity(), this, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }
}