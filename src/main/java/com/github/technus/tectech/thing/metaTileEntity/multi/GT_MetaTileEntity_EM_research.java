package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Holder;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;

import static com.github.technus.tectech.thing.casing.GT_Container_CasingsTT.sBlockCasingsTT;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.V;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_research extends GT_MetaTileEntity_MultiblockBase_EM {
    private final ArrayList<GT_MetaTileEntity_Hatch_Holder> eHolders = new ArrayList<>();

    //region structure
    private static final String[][] shape = new String[][]{
            {E, "000", E, E, E, "000", E,},
            {"A0", "010", "A1", "A!", "A1", "010", "A0",},
            {"A0", "010", E, E, E, "010", "A0",},
            {"000", "010", E, E, E, "010", "000",},
            {"000", "212", "010", "0+0", "010", "212", "000",},
            {"000", "212", "111", "111", "111", "212", "000",},
            {"000", "222", "   ", "   ", "   ", "222", "000",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{1, 3, 2};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addHolderToMachineList"};
    private static final byte[] casingTextures = new byte[]{textureOffset + 3, textureOffset + 3};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, Blocks.air};
    private static final byte[] blockMetaFallback = new byte[]{3, 0};
    //endregion

    public GT_MetaTileEntity_EM_research(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_research(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_research(this.mName);
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Holder rack : eHolders)
            if (isValidMetaTileEntity(rack))
                rack.getBaseMetaTileEntity().setActive(false);
        eHolders.clear();

        if (!EM_StructureCheckAdvanced(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 3, 4))
            return false;

        for (GT_MetaTileEntity_Hatch_Holder rack : eHolders)
            if (isValidMetaTileEntity(rack))
                rack.getBaseMetaTileEntity().setActive(iGregTechTileEntity.isActive());
        return eHolders.size() == 1;
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        //for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
        //    r.getBaseMetaTileEntity().setActive(true);
        //}//Look in Computer code
        return false;
    }

    @Override
    protected void EM_extraExplosions() {
        for (MetaTileEntity tTileEntity : eHolders) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing >= 2;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[textureOffset + 3], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[textureOffset + 3]};
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Philosophers didn't even...",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "dream about it!"
        };
    }


    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders)
            r.getBaseMetaTileEntity().setActive(false);
    }

    @Override
    protected void EM_stopMachine() {
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders)
            r.getBaseMetaTileEntity().setActive(false);
    }

    //NEW METHOD
    public final boolean addHolderToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Holder) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eHolders.add((GT_MetaTileEntity_Hatch_Holder) aMetaTileEntity);
        }
        return false;
    }

    public static void run() {
        try {
            adderMethodMap.put("addHolderToMachineList", GT_MetaTileEntity_EM_research.class.getMethod("addHolderToMachineList", IGregTechTileEntity.class, int.class));
        } catch (NoSuchMethodException e) {
            if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
        }
    }
}
