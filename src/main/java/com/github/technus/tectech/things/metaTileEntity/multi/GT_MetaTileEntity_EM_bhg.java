package com.github.technus.tectech.things.metaTileEntity.multi;

import com.github.technus.tectech.things.block.QuantumGlass;
import com.github.technus.tectech.things.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.elementalMatter.commonValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static gregtech.api.enums.GT_Values.E;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_bhg extends GT_MetaTileEntity_MultiblockBase_EM {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    //Time dillatation - to slow down the explosion thing but REALLY REDUCE POWER OUTPUT
    //Startcodes to startup
    //per dim disable thingies
    private static final String[] s = new String[]{
            "f202", "f020", "g0", "c5g5", "d55c55", "f0a0", "e21212",
            "d00c00", "c0g0", "b0i0", "a20i02", "a02i20", "d0024200",
            "c022c220", "b02g20", "02k20", "a03i30", "21k12"
    };
    private static final String[][] S = new String[][]{
            {E, E, E, E, E, E, s[0], s[5], s[0]},//front
            {E, E, E, E, s[2], s[0], s[6], "d0033300", s[6], s[0], s[2]},
            {E, E, E, s[2], s[1], s[1], s[7], "c033c330", s[7], s[1], s[1], s[2]},
            {E, E, s[2], s[1], s[4], s[4], s[8], "b03g30", s[8], s[4], s[4], s[1], s[2]},
            {E, s[2], s[1], s[4], s[3], s[3], s[9], s[16], s[9], s[3], s[3], s[4], s[1], s[2]},
            {E, s[0], s[1], s[4], s[3], s[3], s[10], s[16], s[10], s[3], s[3], s[4], s[1], s[0]},
            {s[0], s[6], s[7], s[8], s[9], s[10], s[17], "03k30", s[17], s[10], s[9], s[8], s[7], s[6], s[0]},
            {s[5], s[12], s[13], s[14], s[11], s[11], s[15], "a3k3", s[15], s[11], s[11], s[14], s[13], s[12], s[5]}
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
            QuantumGlass.INSTANCE};
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
        return stuctureCheck(shape, blockType, blockMeta, 7, 7, 0, iGregTechTileEntity);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                "Singularity decay based power generation.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Super unstable!!!"
        };
    }
}
