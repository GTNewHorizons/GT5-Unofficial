package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.metaTileEntity.constructableTT;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static gregtech.api.enums.GT_Values.E;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_bhg extends GT_MetaTileEntity_MultiblockBase_EM implements constructableTT {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    //Time dillatation - to slow down the explosion thing but REALLY REDUCE POWER OUTPUT
    //Startcodes to startup
    //per dim disable thingies
    private static final String[] s = new String[]{
            "F202", "F020", "G0", "C5G5", "D55C55", "F0A0", "E21212",
            "D00C00", "C0G0", "B0I0", "A20I02", "A02I20", "D0024200",
            "C022C220", "B02G20", "02K20", "A03I30", "21K12"
    };
    private static final String[][] S = new String[][]{
            {E, E, E, E, E, E, s[0], s[5], s[0]},//front
            {E, E, E, E, s[2], s[0], s[6], "D0033300", s[6], s[0], s[2]},
            {E, E, E, s[2], s[1], s[1], s[7], "C033C330", s[7], s[1], s[1], s[2]},
            {E, E, s[2], s[1], s[4], s[4], s[8], "B03G30", s[8], s[4], s[4], s[1], s[2]},
            {E, s[2], s[1], s[4], s[3], s[3], s[9], s[16], s[9], s[3], s[3], s[4], s[1], s[2]},
            {E, s[0], s[1], s[4], s[3], s[3], s[10], s[16], s[10], s[3], s[3], s[4], s[1], s[0]},
            {s[0], s[6], s[7], s[8], s[9], s[10], s[17], "03K30", s[17], s[10], s[9], s[8], s[7], s[6], s[0]},
            {s[5], s[12], s[13], s[14], s[11], s[11], s[15], "A3K3", s[15], s[11], s[11], s[14], s[13], s[12], s[5]}
    };
    private static final String[][] shape = new String[][]{
            S[0], S[1], S[2], S[3], S[4], S[5], S[6], S[7], S[6], S[5], S[4], S[3], S[2], S[1], S[0]
    };
    private static final Block[] blockType = new Block[]{
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT,
            GT_Container_CasingsTT.sBlockCasingsTT,
            QuantumGlassBlock.INSTANCE};
    private static final byte[] blockMeta = new byte[]{3, 4, 5, 6, 7, 0};

    public GT_MetaTileEntity_EM_bhg(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_bhg(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_bhg(this.mName);
    }

    @Override
    public void construct(int qty) {
        StructureBuilder(shape,blockType,blockMeta,7,7,0,this.getBaseMetaTileEntity());
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG_ACTIVE");
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
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return EM_StructureCheck(shape, blockType, blockMeta, 7, 7, 0);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Singularity based power generation.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Super unstable!!!"
        };
    }
}
