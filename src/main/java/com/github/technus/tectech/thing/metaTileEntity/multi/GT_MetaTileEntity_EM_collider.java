package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.metaTileEntity.constructableTT;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static gregtech.api.enums.GT_Values.E;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_collider extends GT_MetaTileEntity_MultiblockBase_EM implements constructableTT {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private byte eTier =0;

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {E, "Y00000000000", E,},
            {"Y00000000000", "U0000111111111110000", "Y00000000000",},
            {"U0000222222222220000", "S00111133333333333111100", "U0000222222222220000",},
            {"S00222200000000000222200", "Q001133331111111111133331100", "S00222200000000000222200",},
            {"Q00000000K00000000", "O0011331111K1111331100", "Q00000000K00000000",},
            {"O000000S000000", "M00113311S11331100", "O000000S000000",},
            {"M000000W000000", "L0113311W1133110", "M000000W000000",},
            {"L00000[00000", "K013311[113310", "L00000[00000",},
            {"K0000_0000", "J01311_11310", "K0000_0000",},
            {"J000c000", "I0131c1310", "J000c000",},
            {"I000e000", "H0131e1310", "I000e000",},
            {"H000g000", "G0131g1310", "H000g000",},
            {"G000i000", "F0131i1310", "G000i000",},
            {"F000k000", "E0131k1310", "F000k000",},
            {"E000m000", "D0131m1310", "E000m000",},
            {"E000m000", "D0131m1310", "E000m000",},
            {"D000o000", "C0131o1310", "D000o000",},
            {"D000o000", "C0131o1310", "D000o000",},
            {"C000q000", "B0131q1310", "C000q000",},
            {"C000q000", "B0131q1310", "C000q000",},
            {"B000s000", "A0131s1310", "B000s000",},
            {"B000s000", "A0131s1310", "B000s000",},
            {"A020u020", "0131u1310", "A020u020",},
            {"A020u020", "0131u1310", "A020u020",},
            {"A020u020", "0131u1310", "A020u020",},
            {"A020u020", "0131u1310", "A020u020",},
            {"00+w020", "1310v1310", "000w020",},
            {"0000v020", "1310v1310", "++00v020",},
            {"000w020", "1310v1310", "000w020",},
            {"4444v020", "5314v1310", "4444v020",},
            {"224w020", "5314v1310", "++4w020",},
            {"+244v020", "5314v1310", "++44v020",},
            {"224w020", "5314v1310", "++4w020",},
            {"4444v020", "5314v1310", "4444v020",},
            {"000w020", "1310v1310", "000w020",},
            {"0000v020", "1310v1310", "++00v020",},
            {"000w020", "1310v1310", "000w020",},
            {"A020u020", "0131u1310", "A020u020",},
            {"A020u020", "0131u1310", "A020u020",},
            {"A020u020", "0131u1310", "A020u020",},
            {"A020u020", "0131u1310", "A020u020",},
            {"B000s000", "A0131s1310", "B000s000",},
            {"B000s000", "A0131s1310", "B000s000",},
            {"C000q000", "B0131q1310", "C000q000",},
            {"C000q000", "B0131q1310", "C000q000",},
            {"D000o000", "C0131o1310", "D000o000",},
            {"D000o000", "C0131o1310", "D000o000",},
            {"E000m000", "D0131m1310", "E000m000",},
            {"E000m000", "D0131m1310", "E000m000",},
            {"F000k000", "E0131k1310", "F000k000",},
            {"G000i000", "F0131i1310", "G000i000",},
            {"H000g000", "G0131g1310", "H000g000",},
            {"I000e000", "H0131e1310", "I000e000",},
            {"J000c000", "I0131c1310", "J000c000",},
            {"K0000_0000", "J01311_11310", "K0000_0000",},
            {"L00000[00000", "K013311[113310", "L00000[00000",},
            {"M000000W000000", "L0113311W1133110", "M000000W000000",},
            {"O000000S000000", "M00113311S11331100", "O000000S000000",},
            {"Q00000000K00000000", "O0011331111K1111331100", "Q00000000K00000000",},
            {"S00222200000000000222200", "Q001133331111111111133331100", "S00222200000000000222200",},
            {"U0000222222222220000", "S00111133333333333111100", "U0000222222222220000",},
            {"Y00000000000", "U0000111111111110000", "Y00000000000",},
            {E, "Y00000000000", E,},
    };
    private static final Block[] blockType = new Block[]{
            GT_Container_CasingsTT.sBlockCasingsTT,
            GregTech_API.sBlockCasings4,
            QuantumGlassBlock.INSTANCE,
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT
    };
    private static final byte[] blockMeta = new byte[]{3,7,0,9,3,9};

    private static final Block[] blockType2 = new Block[]{
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT,
            QuantumGlassBlock.INSTANCE,
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT
    };
    private static final byte[] blockMeta2 = new byte[]{3,6,0,8,5,9};
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
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[99], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[99]};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("eTier", eTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        eTier =aNBT.getByte("eTier");
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if(iGregTechTileEntity.getBlockOffset(0,-1,0)!=GT_Container_CasingsTT.sBlockCasingsTT){
            eTier =0;
            return false;
        } else if(iGregTechTileEntity.getMetaIDOffset(0,-1,0)==3) {
            eTier =1;
        } else if(iGregTechTileEntity.getMetaIDOffset(0,-1,0)==5) {
            eTier =2;
        } else {
            eTier =0;
            return false;
        }
        

        boolean test;
        switch (eTier){
            case 1:test=EM_StructureCheck(shape,blockType,blockMeta,0,-2,28); break;
            case 2:test=EM_StructureCheck(shape,blockType2,blockMeta2,0,-2,28); break;
            default: eTier = 0; return false;
        }
        if(test) return true;
        eTier = 0;
        return false;
    }

    @Override
    public void construct(int qty) {
        if((qty&1)==0)
            StructureBuilder(shape,blockType,blockMeta,0,-2,28,this.getBaseMetaTileEntity());
        else StructureBuilder(shape,blockType2,blockMeta2,0,-2,28,this.getBaseMetaTileEntity());
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
