package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.metaTileEntity.iConstructible;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;
import static gregtech.api.enums.GT_Values.E;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_collider extends GT_MetaTileEntity_MultiblockBase_EM implements iConstructible {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private byte eTier = 0;

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"I0A0A0","I00000","I0A0A0",},
            {"H0000000","G001111100","H0000000",},
            {"F22223332222","F41155555114","F22223332222",},
            {"E2000000000002","E4155111115514","E2000000000002",},
            {"D20000E00002","D41511E11514","D20000E00002",},
            {"C2000I0002","C4151I1514","C2000I0002",},
            {"B2000K0002","B4151K1514","B2000K0002",},
            {"B200M002","A0151M1510","B200M002",},
            {"A0200M0020","A0151M1510","A0200M0020",},
            {"0020O0200","0151O1510","0020O0200",},
            {"A030O030","0151O1510","A030O030",},
            {"0030O0300","0151O1510","0030O0300",},
            {"A030O030","0151O1510","A030O030",},
            {"0020O0200","0151O1510","0020O0200",},
            {"A0200M0020","A0151M1510","A0200M0020",},
            {"B200M002","A0151M1510","B200M002",},
            {"B2000K0002","B4151K1514","B2000K0002",},
            {"C2000I0002","C4151I1514","C2000I0002",},
            {"D200002   200002","D415112 . 211514","D200002   200002",},
            {"E20!!22222!!02","E4155111115514","E20!!22222!!02",},
            {"F2222#\"#2222","F41155555114","F2222#\"#2222",},
    };
    private static final Block[] blockType = new Block[]{
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT,
            QuantumGlassBlock.INSTANCE,
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT
    };
    private static final byte[] blockMeta1 = new byte[]{4, 7, 4, 0, 4, 8};
    private static final byte[] blockMeta2 = new byte[]{4, 7, 5, 0, 6, 9};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalInputToMachineList", "addElementalOutputToMachineList", "addElementalMufflerToMachineList"};
    private static final byte[] casingTextures = new byte[]{textureOffset, textureOffset + 4, textureOffset + 4, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4, 4, 4};
    //endregion

    public GT_MetaTileEntity_EM_collider(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_collider(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_collider(this.mName);
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[textureOffset + 4], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[textureOffset + 4]};
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing >= 2;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("eTier", eTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        eTier = aNBT.getByte("eTier");
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX*2;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ*2;
        if (iGregTechTileEntity.getBlockOffset(xDir, 0, zDir) != GT_Container_CasingsTT.sBlockCasingsTT) {
            eTier = 0;
            return false;
        }

        if (iGregTechTileEntity.getMetaIDOffset(xDir, 0, zDir) == 8) {
            eTier = 1;
        } else if (iGregTechTileEntity.getMetaIDOffset(xDir, 0, zDir) == 9) {
            eTier = 2;
        } else {
            eTier = 0;
            return false;
        }

        boolean test;
        switch (eTier) {
            case 1:
                test = EM_StructureCheckAdvanced(shape, blockType, blockMeta1,addingMethods,casingTextures,blockTypeFallback,blockMetaFallback, 11, 1, 18);
                break;
            case 2:
                test = EM_StructureCheckAdvanced(shape, blockType, blockMeta2,addingMethods,casingTextures,blockTypeFallback,blockMetaFallback, 11, 1, 18);
                break;
            default:
                eTier = 0;
                return false;
        }
        if (test) return true;
        eTier = 0;
        return false;
    }

    @Override
    public void construct(int qty) {
        IGregTechTileEntity iGregTechTileEntity=getBaseMetaTileEntity();
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX*4;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ*4;
        if(iGregTechTileEntity.getBlockOffset(xDir,0,zDir).getMaterial() == Material.air)
            iGregTechTileEntity.getWorld().setBlock(
                    iGregTechTileEntity.getXCoord()+xDir,
                    iGregTechTileEntity.getYCoord(),
                    iGregTechTileEntity.getZCoord()+zDir,
                    Blocks.lapis_block);
        if ((qty & 1) == 1)
            StructureBuilder(shape, blockType, blockMeta1, 11, 1, 18, iGregTechTileEntity);
        else
            StructureBuilder(shape, blockType, blockMeta2, 11, 1, 18, iGregTechTileEntity);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Collide matter at extreme velocities.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Faster than light!!!"
        };
    }
}
