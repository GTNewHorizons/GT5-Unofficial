package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Vec3pos;
import com.github.technus.tectech.dataFramework.QuantumDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputData;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.Util.V;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_switch extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable{
    //region Structure
    private static final String[][] shape = new String[][]{
            {"   "," . ","   ",},
            {"   "," 0 ","   ",},
            {"   ","   ","   ",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{3};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{1};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic/Data Hatches or Computer casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_switch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_switch(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_switch(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][1], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][1]};
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilder(shape, blockType, blockMeta,1, 1, 0, getBaseMetaTileEntity(),hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        short thingsActive = 0;
        for (GT_MetaTileEntity_Hatch_InputData di : eInputData)
            if (di.q != null)
                thingsActive++;

        if (thingsActive > 0) {
            thingsActive += eOutputData.size();
            mEUt = -(int) V[7];
            eAmpereFlow = 1 + (thingsActive >> 2);
            mMaxProgresstime = 20;
            mEfficiencyIncrease = 10000;
            return true;
        }
        mMaxProgresstime = 0;
        mEfficiencyIncrease = 0;
        return false;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (eOutputData.size() > 0) {
            float total = 0;
            for (int i = 0; i < 10; i++) {//each param pair
                if (eParamsIn[i] > 0 && eParamsIn[i + 10] >= 0)
                    total += eParamsIn[i];//Total weighted div
            }
            total += total / 100F;

            final Vec3pos pos = new Vec3pos(getBaseMetaTileEntity());
            QuantumDataPacket pack = new QuantumDataPacket(pos, 0);
            for (GT_MetaTileEntity_Hatch_InputData i : eInputData) {
                if (i.q == null || i.q.contains(pos)) continue;
                pack = pack.unifyPacketWith(i.q);
                if (pack == null) return;
            }

            long remaining = pack.computation;

            for (int i = 0; i < 10; i++) {
                if (eParamsIn[i] > 0) {
                    final int outIndex = (int) (eParamsIn[i + 10]) - 1;
                    if (outIndex < 0 || outIndex > eOutputData.size()) continue;
                    GT_MetaTileEntity_Hatch_OutputData out = eOutputData.get(outIndex);
                    final long part = (long) ((pack.computation * eParamsIn[i]) / total);
                    if (part > 0) {
                        remaining -= part;
                        if (remaining > 0)
                            out.q = new QuantumDataPacket(pack, part);
                        else if (part + remaining > 0) {
                            out.q = new QuantumDataPacket(pack, part + remaining);
                            break;
                        } else break;
                    }
                }
            }
        }
    }

    @Override
    public void updateParameters_EM() {
        for (int i = 0; i < 10; i++) {
            if (eParamsIn[i] < 0) eParamsInStatus[i] = PARAM_TOO_LOW;
            else if (eParamsIn[i] == 0) eParamsInStatus[i] = PARAM_UNUSED;
            else if (eParamsIn[i] == Float.POSITIVE_INFINITY) eParamsInStatus[i] = PARAM_TOO_HIGH;
            else eParamsInStatus[i] = PARAM_OK;
        }
        for (int i = 10; i < 20; i++) {
            if (eParamsInStatus[i - 10] == PARAM_OK) {
                if ((int) eParamsIn[i] <= 0) eParamsInStatus[i] = PARAM_LOW;
                else if ((int) eParamsIn[i] > eOutputData.size()) eParamsInStatus[i] = PARAM_TOO_HIGH;
                else eParamsInStatus[i] = PARAM_OK;
            } else {
                eParamsInStatus[i] = PARAM_UNUSED;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "User controlled computation power routing",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Quality of service is a must"
        };
    }
}
