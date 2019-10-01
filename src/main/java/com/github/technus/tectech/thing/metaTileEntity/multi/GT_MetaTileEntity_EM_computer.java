package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.Vec3pos;
import com.github.technus.tectech.mechanics.dataTransport.QuantumDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_computer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private final ArrayList<GT_MetaTileEntity_Hatch_Rack> eRacks = new ArrayList<>();

    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    //endregion

    //region structure
    private static final String[][] front = new String[][]{{"A  ", "A  ", "A. ", "A  ",},};
    private static final String[][] terminator = new String[][]{{"A  ", "A  ", "A  ", "A  ",},};
    private static final String[][] cap = new String[][]{{"-01", "A22", "A22", "-01",},};
    private static final String[][] slice = new String[][]{{"-01", "A!2", "A!2", "-01",},};
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{2, 1, 3};
    private final IHatchAdder[] addingMethods = new IHatchAdder[]{this::addToMachineList, this::addRackToMachineList};
    private static final short[] casingTextures = new short[]{textureOffset + 1, textureOffset + 3};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{1, 3};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.computer.hint.0"),//1 - Classic/Data Hatches or Computer casing
            translateToLocal("gt.blockmachines.multimachine.em.computer.hint.1"),//2 - Rack Hatches or Advanced computer casing
    };
    //endregion

    //region parameters
    protected Parameters.Group.ParameterIn overclock, overvolt;
    protected Parameters.Group.ParameterOut maxCurrentTemp, availableData;

    private static final INameFunction<GT_MetaTileEntity_EM_computer> OC_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.0");//Overclock ratio
    private static final INameFunction<GT_MetaTileEntity_EM_computer> OV_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgi.1");//Overvoltage ratio
    private static final INameFunction<GT_MetaTileEntity_EM_computer> MAX_TEMP_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.0");//Current max. heat
    private static final INameFunction<GT_MetaTileEntity_EM_computer> COMPUTE_NAME = (base, p) -> translateToLocal("gt.blockmachines.multimachine.em.computer.cfgo.1");//Produced computation
    private static final IStatusFunction<GT_MetaTileEntity_EM_computer> OC_STATUS =
            (base, p) -> LedStatus.fromLimitsInclusiveOuterBoundary(p.get(), 0, 1, 1, 3);
    private static final IStatusFunction<GT_MetaTileEntity_EM_computer> OV_STATUS =
            (base, p) -> LedStatus.fromLimitsInclusiveOuterBoundary(p.get(), .7, .8, 1.2, 2);
    private static final IStatusFunction<GT_MetaTileEntity_EM_computer> MAX_TEMP_STATUS =
            (base, p) -> LedStatus.fromLimitsInclusiveOuterBoundary(p.get(), -10000, 0, 0, 5000);
    private static final IStatusFunction<GT_MetaTileEntity_EM_computer> COMPUTE_STATUS = (base, p) -> {
        if (base.eAvailableData < 0) {
            return STATUS_TOO_LOW;
        }
        if (base.eAvailableData == 0) {
            return STATUS_NEUTRAL;
        }
        return STATUS_OK;
    };
    //endregion

    public GT_MetaTileEntity_EM_computer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        eCertainMode = 5;
        eCertainStatus = -128;//no-brain value
    }

    public GT_MetaTileEntity_EM_computer(String aName) {
        super(aName);
        eCertainMode = 5;
        eCertainStatus = -128;//no-brain value
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_computer(mName);
    }

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0);
        overclock = hatch_0.makeInParameter(0, 1, OC_NAME, OC_STATUS);
        overvolt = hatch_0.makeInParameter(1, 1, OV_NAME, OV_STATUS);
        maxCurrentTemp = hatch_0.makeOutParameter(0, 0, MAX_TEMP_NAME, MAX_TEMP_STATUS);
        availableData = hatch_0.makeOutParameter(1, 0, COMPUTE_NAME, COMPUTE_STATUS);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Rack rack : eRacks) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                rack.getBaseMetaTileEntity().setActive(false);
            }
        }
        eRacks.clear();
        if (!structureCheck_EM(front, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, 0)) {
            return false;
        }
        if (!structureCheck_EM(cap, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, -1)) {
            return false;
        }
        byte offset = -2, totalLen = 4;
        while (offset > -16) {
            if (!structureCheck_EM(slice, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, offset)) {
                break;
            }
            totalLen++;
            offset--;
        }
        if (totalLen > 16) {
            return false;
        }
        if (!structureCheck_EM(cap, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, ++offset)) {
            return false;
        }
        if (!structureCheck_EM(terminator, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 2, --offset)) {
            return false;
        }
        eCertainMode = (byte) Math.min(totalLen / 3, 5);
        for (GT_MetaTileEntity_Hatch_Rack rack : eRacks) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                rack.getBaseMetaTileEntity().setActive(iGregTechTileEntity.isActive());
            }
        }
        return eUncertainHatches.size() == 1;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        parametrization.setToDefaults(false, true);
        eAvailableData = 0;
        double maxTemp = 0;
        double overClockRatio = overclock.get();
        double overVoltageRatio = overvolt.get();
        if (Double.isNaN(overClockRatio) || Double.isNaN(overVoltageRatio)) {
            return false;
        }
        if (overclock.getStatus(true).isOk && overvolt.getStatus(true).isOk) {
            float eut = V[8] * (float) overVoltageRatio * (float) overClockRatio;
            if (eut < Integer.MAX_VALUE - 7) {
                mEUt = -(int) eut;
            } else {
                mEUt = -(int) V[8];
                return false;
            }
            short thingsActive = 0;
            int rackComputation;

            for (GT_MetaTileEntity_Hatch_Rack rack : eRacks) {
                if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                    continue;
                }
                if (rack.heat > maxTemp) {
                    maxTemp = rack.heat;
                }
                rackComputation = rack.tickComponents((float) overClockRatio, (float) overVoltageRatio);
                if (rackComputation > 0) {
                    eAvailableData += rackComputation;
                    thingsActive += 4;
                }
                rack.getBaseMetaTileEntity().setActive(true);
            }

            for (GT_MetaTileEntity_Hatch_InputData di : eInputData) {
                if (di.q != null)//ok for power losses
                {
                    thingsActive++;
                }
            }

            if (thingsActive > 0 && eCertainStatus == 0) {
                thingsActive += eOutputData.size();
                eAmpereFlow = 1 + (thingsActive >> 2);
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                return true;
            } else {
                eAvailableData = 0;
                mEUt = -(int) V[8];
                eAmpereFlow = 1;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                maxCurrentTemp.set(maxTemp);
                availableData.set(eAvailableData);
                return true;
            }
        }
        return false;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eOutputData.isEmpty()) {
            Vec3pos pos = new Vec3pos(getBaseMetaTileEntity());
            QuantumDataPacket pack = new QuantumDataPacket(eAvailableData / eOutputData.size()).unifyTraceWith(pos);
            if (pack == null) {
                return;
            }
            for (GT_MetaTileEntity_Hatch_InputData hatch : eInputData) {
                if (hatch.q == null || hatch.q.contains(pos)) {
                    continue;
                }
                pack = pack.unifyPacketWith(hatch.q);
                if (pack == null) {
                    return;
                }
            }

            for (GT_MetaTileEntity_Hatch_OutputData o : eOutputData) {
                o.q = pack;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                Util.intBitsToString(TecTech.RANDOM.nextInt()),
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.computer.desc")//You need it to process the number above
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_COMPUTER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][3], new TT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][3]};
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return GT_MetaTileEntity_EM_switch.activitySound;
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Rack rack : eRacks) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                rack.getBaseMetaTileEntity().setActive(false);
            }
        }
    }

    @Override
    protected void extraExplosions_EM() {
        for (MetaTileEntity tTileEntity : eRacks) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
    }

    @Override
    protected long getAvailableData_EM() {
        return eAvailableData;
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        eAvailableData = 0;
        for (GT_MetaTileEntity_Hatch_Rack rack : eRacks) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                rack.getBaseMetaTileEntity().setActive(false);
            }
        }
    }

    @Override
    protected void afterRecipeCheckFailed() {
        super.afterRecipeCheckFailed();
        for (GT_MetaTileEntity_Hatch_Rack rack : eRacks) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(rack)) {
                rack.getBaseMetaTileEntity().setActive(false);
            }
        }
    }

    public final boolean addRackToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Rack) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eRacks.add((GT_MetaTileEntity_Hatch_Rack) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        IGregTechTileEntity igt = getBaseMetaTileEntity();
        StructureBuilderExtreme(front, blockType, blockMeta, 1, 2, 0, igt, this, hintsOnly);
        StructureBuilderExtreme(cap, blockType, blockMeta, 1, 2, -1, igt, this, hintsOnly);

        byte offset = -2;
        for (int rackSlices = Math.min(stackSize, 12); rackSlices > 0; rackSlices--) {
            StructureBuilderExtreme(slice, blockType, blockMeta, 1, 2, offset--, igt, this, hintsOnly);
        }

        StructureBuilderExtreme(cap, blockType, blockMeta, 1, 2, offset--, igt, this, hintsOnly);
        StructureBuilderExtreme(terminator, blockType, blockMeta, 1, 2, offset, igt, this, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }
}