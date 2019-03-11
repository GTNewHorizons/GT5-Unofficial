package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.HatchAdder;
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

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_crafting extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    public static final String crafter="EM Crafting";
    //region structure
    private static final String[][] shape = new String[][]{
            {"A000","0   0","0 . 0","0   0","A000",},
            {"00000","00000","00000","00000","00000",},
            {"0C0","A!!!","A!1!","A!!!","0C0",},
            {"22222","22222","22122","22222","22222",},
            {"23432","33333","43134","33333","23432",},
            {"23332","33333","33533","33333","23332",},
            {"23432","33333","43134","33333","23432",},
            {"22222","22222","22122","22222","22222",},
            {"0C0","A!!!","A!1!","A!!!","0C0",},
            {"00000","00000","00000","00000","00000",},
            {"A000","0   0","0   0","0   0","A000",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT , QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4, 10, 5, 0, 6, 9};
    private final HatchAdder[] addingMethods = new HatchAdder[]{this::addClassicToMachineList, this::addElementalToMachineList};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Hatches or Molecular Casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_crafting(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_crafting(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_crafting(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_CRAFTING");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_CRAFTING_ACTIVE");
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
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMeta,2, 2, 0, getBaseMetaTileEntity(),this,hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "The most precise way of making stuff.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "(Trans-Planck process)"
        };
    }
}
