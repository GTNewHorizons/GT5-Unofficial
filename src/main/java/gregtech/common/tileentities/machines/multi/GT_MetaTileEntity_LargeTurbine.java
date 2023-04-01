package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW_EMPTY;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.LightingHelper;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.render.GT_RenderUtil;

public abstract class GT_MetaTileEntity_LargeTurbine extends
        GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_LargeTurbine> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<GT_MetaTileEntity_LargeTurbine>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<GT_MetaTileEntity_LargeTurbine> computeValue(Class<?> type) {
            return StructureDefinition.<GT_MetaTileEntity_LargeTurbine>builder().addShape(STRUCTURE_PIECE_MAIN,
                            transpose(new String[][] { { "     ", "     ", "     ", "     ", "     ", },
                                    { " --- ", " ccc ", " hhh ", " hhh ", " hhh ", },
                                    { " --- ", " c~c ", " h-h ", " h-h ", " hdh ", },
                                    { " --- ", " ccc ", " hhh ", " hhh ", " hhh ", },
                                    { "     ", "     ", "     ", "     ", "     ", }, }))
                    .addElement('c', lazy(t -> ofBlock(t.getCasingBlock(), t.getCasingMeta())))
                    .addElement('d', lazy(t -> Dynamo.newAny(t.getCasingTextureIndex(), 1))).addElement('h',
                            lazy(t -> buildHatchAdder(GT_MetaTileEntity_LargeTurbine.class).atLeast(Maintenance,
                                            InputHatch,
                                            OutputHatch,
                                            OutputBus,
                                            InputBus,
                                            Muffler).casingIndex(t.getCasingTextureIndex()).dot(2)
                                    .buildAndChain(t.getCasingBlock(), t.getCasingMeta()))).build();
        }
    };

    protected int baseEff = 0;
    protected int optFlow = 0;
    protected double realOptFlow = 0;
    protected int storedFluid = 0;
    protected int counter = 0;
    protected boolean looseFit = false;
    protected int overflowMultiplier = 0;
    protected float[] flowMultipliers = new float[] { 1, 1, 1 };

    // client side stuff
    protected boolean mHasTurbine;
    // mMachine got overwritten by StructureLib extended facing query response
    // so we use a separate field for this
    protected boolean mFormed;

    public GT_MetaTileEntity_LargeTurbine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeTurbine(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_LargeTurbine> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 2, 2, 1) && mMaintenanceHatches.size() == 1
                && mMufflerHatches.isEmpty() == (getPollutionPerTick(null) == 0);
    }

    public abstract Block getCasingBlock();

    public abstract byte getCasingMeta();

    public abstract int getCasingTextureIndex();

    public boolean isNewStyleRendering() {
        return false;
    }

    public IIconContainer[] getTurbineTextureActive() {
        return TURBINE_NEW_ACTIVE;
    }

    public IIconContainer[] getTurbineTextureFull() {
        return TURBINE_NEW;
    }

    public IIconContainer[] getTurbineTextureEmpty() {
        return TURBINE_NEW_EMPTY;
    }

    @Override
    public boolean renderInWorld(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer) {
        if (!isNewStyleRendering() || !mFormed) return false;
        int[] tABCCoord = new int[] { -1, -1, 0 };
        int[] tXYZOffset = new int[3];
        byte tFacing = getBaseMetaTileEntity().getFrontFacing();
        ExtendedFacing tExtendedFacing = getExtendedFacing();
        ForgeDirection tDirection = tExtendedFacing.getDirection();
        LightingHelper tLighting = new LightingHelper(aRenderer);

        // for some reason +x and -z need this field set to true, but not any other sides
        if (tFacing == 2 || tFacing == 5) aRenderer.field_152631_f = true;
        Block tBlock = getCasingBlock();

        IIconContainer[] tTextures;
        if (getBaseMetaTileEntity().isActive()) tTextures = getTurbineTextureActive();
        else if (hasTurbine()) tTextures = getTurbineTextureFull();
        else tTextures = getTurbineTextureEmpty();

        assert tTextures != null && tTextures.length == tABCCoord.length;

        for (int i = 0; i < 9; i++) {
            if (i != 4) { // do not draw ourselves again.
                tExtendedFacing.getWorldOffset(tABCCoord, tXYZOffset);
                // since structure check passed, we can assume it is turbine casing
                int tX = tXYZOffset[0] + aX;
                int tY = tXYZOffset[1] + aY;
                int tZ = tXYZOffset[2] + aZ;
                // we skip the occlusion test, as we always require a working turbine to have a block of air before it
                // so the front face cannot be occluded whatsoever in the most cases.
                Tessellator.instance.setBrightness(
                        tBlock.getMixedBrightnessForBlock(
                                aWorld,
                                aX + tDirection.offsetX,
                                tY + tDirection.offsetY,
                                aZ + tDirection.offsetZ));
                tLighting.setupLighting(tBlock, tX, tY, tZ, tFacing)
                         .setupColor(tFacing, Dyes._NULL.mRGBa);
                GT_RenderUtil.renderBlockIcon(
                        aRenderer,
                        tBlock,
                        tX + tDirection.offsetX * 0.01,
                        tY + tDirection.offsetY * 0.01,
                        tZ + tDirection.offsetZ * 0.01,
                        tTextures[i].getIcon(),
                        tFacing);
            }
            if (++tABCCoord[0] == 2) {
                tABCCoord[0] = -1;
                tABCCoord[1]++;
            }
        }

        aRenderer.field_152631_f = false;
        return false;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mHasTurbine = (aValue & 0x1) != 0;
        mFormed = (aValue & 0x2) != 0;
        super.onValueUpdate(aValue);
    }

    @Override
    public byte getUpdateData() {
        return (byte) ((hasTurbine() ? 1 : 0) | (mMachine ? 2 : 0));
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity tTileEntity, int aBaseCasingIndex) {
        return addMaintenanceToMachineList(tTileEntity, getCasingTextureIndex())
                || addInputToMachineList(tTileEntity, getCasingTextureIndex())
                || addOutputToMachineList(tTileEntity, getCasingTextureIndex())
                || addMufflerToMachineList(tTileEntity, getCasingTextureIndex());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if ((counter & 7) == 0 && (aStack == null || !(aStack.getItem() instanceof GT_MetaGenerated_Tool)
                || aStack.getItemDamage() < 170
                || aStack.getItemDamage() > 179)) {
            stopMachine();
            return false;
        }
        ArrayList<FluidStack> tFluids = getStoredFluids();
        if (!tFluids.isEmpty()) {

            if (baseEff == 0 || optFlow == 0
                    || counter >= 512
                    || this.getBaseMetaTileEntity()
                           .hasWorkJustBeenEnabled()
                    || this.getBaseMetaTileEntity()
                           .hasInventoryBeenModified()) {
                counter = 0;
                baseEff = GT_Utility.safeInt(
                        (long) ((5F + ((GT_MetaGenerated_Tool) aStack.getItem()).getToolCombatDamage(aStack)) * 1000F));
                optFlow = GT_Utility.safeInt(
                        (long) Math.max(
                                Float.MIN_NORMAL,
                                ((GT_MetaGenerated_Tool) aStack.getItem()).getToolStats(aStack)
                                                                          .getSpeedMultiplier()
                                        * GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mToolSpeed
                                        * 50));

                overflowMultiplier = getOverflowMultiplier(aStack);

                flowMultipliers[0] = GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mSteamMultiplier;
                flowMultipliers[1] = GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mGasMultiplier;
                flowMultipliers[2] = GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mPlasmaMultiplier;

                if (optFlow <= 0 || baseEff <= 0) {
                    stopMachine(); // in case the turbine got removed
                    return false;
                }
            } else {
                counter++;
            }
        }

        int newPower = fluidIntoPower(tFluids, optFlow, baseEff, overflowMultiplier, flowMultipliers); // How much the
                                                                                                       // turbine should
                                                                                                       // be producing
                                                                                                       // with this flow
        int difference = newPower - this.mEUt; // difference between current output and new output

        // Magic numbers: can always change by at least 10 eu/t, but otherwise by at most 1 percent of the difference in
        // power level (per tick)
        // This is how much the turbine can actually change during this tick
        int maxChangeAllowed = Math.max(10, GT_Utility.safeInt((long) Math.abs(difference) / 100));

        if (Math.abs(difference) > maxChangeAllowed) { // If this difference is too big, use the maximum allowed change
            int change = maxChangeAllowed * (difference > 0 ? 1 : -1); // Make the change positive or negative.
            this.mEUt += change; // Apply the change
        } else this.mEUt = newPower;

        if (this.mEUt <= 0) {
            // stopMachine();
            this.mEUt = 0;
            this.mEfficiency = 0;
            return false;
        } else {
            this.mMaxProgresstime = 1;
            this.mEfficiencyIncrease = 10;
            // Overvoltage is handled inside the MultiBlockBase when pushing out to dynamos. no need to do it here.
            return true;
        }
    }

    abstract int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff, int overflowMultiplier,
            float[] flowMultipliers);

    abstract float getOverflowEfficiency(int totalFlow, int actualOptimalFlow, int overflowMultiplier);

    // Gets the maximum output that the turbine currently can handle. Going above this will cause the turbine to explode
    public long getMaximumOutput() {
        long aTotal = 0;
        for (GT_MetaTileEntity_Hatch_Dynamo aDynamo : mDynamoHatches) {
            if (isValidMetaTileEntity(aDynamo)) {
                long aVoltage = aDynamo.maxEUOutput();
                aTotal = aDynamo.maxAmperesOut() * aVoltage;
            }
        }
        return aTotal;
    }

    public int getOverflowMultiplier(ItemStack aStack) {
        int aOverflowMultiplier = 0;
        int toolQualityLevel = GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mToolQuality;
        if (toolQualityLevel >= 6) {
            aOverflowMultiplier = 3;
        } else if (toolQualityLevel >= 3) {
            aOverflowMultiplier = 2;
        } else {
            aOverflowMultiplier = 1;
        }
        return aOverflowMultiplier;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return 0;
        }
        if (aStack.getItem() instanceof GT_MetaGenerated_Tool_01) {
            return 10000;
        }
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
            }
        }

        String tRunning = mMaxProgresstime > 0
                ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.running.true")
                        + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.running.false")
                        + EnumChatFormatting.RESET;
        String tMaintainance = getIdealStatus() == getRepairStatus()
                ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.maintenance.false")
                        + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.maintenance.true")
                        + EnumChatFormatting.RESET;
        int tDura = 0;

        if (mInventory[1] != null && mInventory[1].getItem() instanceof GT_MetaGenerated_Tool_01) {
            tDura = GT_Utility.safeInt(
                    (long) (100.0f / GT_MetaGenerated_Tool.getToolMaxDamage(mInventory[1])
                            * (GT_MetaGenerated_Tool.getToolDamage(mInventory[1])) + 1));
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity()
                                      .getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity()
                                   .getEUCapacity();
            }
        }
        String[] ret = new String[] {
                // 8 Lines available for information panels
                tRunning + ": "
                        + EnumChatFormatting.RED
                        + GT_Utility.formatNumbers(((long) mEUt * mEfficiency) / 10000)
                        + EnumChatFormatting.RESET
                        + " EU/t", /* 1 */
                tMaintainance, /* 2 */
                StatCollector.translateToLocal("GT5U.turbine.efficiency") + ": "
                        + EnumChatFormatting.YELLOW
                        + (mEfficiency / 100F)
                        + EnumChatFormatting.RESET
                        + "%", /* 2 */
                StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                        + EnumChatFormatting.GREEN
                        + GT_Utility.formatNumbers(storedEnergy)
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + /* 3 */ EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(maxEnergy)
                        + EnumChatFormatting.RESET
                        + " EU",
                StatCollector.translateToLocal("GT5U.turbine.flow") + ": "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(GT_Utility.safeInt((long) realOptFlow))
                        + EnumChatFormatting.RESET
                        + " L/t"
                        + /* 4 */ EnumChatFormatting.YELLOW
                        + " ("
                        + (looseFit ? StatCollector.translateToLocal("GT5U.turbine.loose")
                                : StatCollector.translateToLocal("GT5U.turbine.tight"))
                        + ")", /* 5 */
                StatCollector.translateToLocal("GT5U.turbine.fuel") + ": "
                        + EnumChatFormatting.GOLD
                        + GT_Utility.formatNumbers(storedFluid)
                        + EnumChatFormatting.RESET
                        + "L", /* 6 */
                StatCollector.translateToLocal("GT5U.turbine.dmg") + ": "
                        + EnumChatFormatting.RED
                        + tDura
                        + EnumChatFormatting.RESET
                        + "%", /* 7 */
                StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                        + EnumChatFormatting.GREEN
                        + mPollutionReduction
                        + EnumChatFormatting.RESET
                        + " %" /* 8 */
        };
        if (!this.getClass()
                 .getName()
                 .contains("Steam"))
            ret[4] = StatCollector.translateToLocal("GT5U.turbine.flow") + ": "
                    + EnumChatFormatting.YELLOW
                    + GT_Utility.safeInt((long) realOptFlow)
                    + EnumChatFormatting.RESET
                    + " L/t";
        return ret;
    }

    public boolean hasTurbine() {
        return getBaseMetaTileEntity() != null && getBaseMetaTileEntity().isClientSide() ? mHasTurbine
                : this.getMaxEfficiency(mInventory[1]) > 0;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 2, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 2, 1, elementBudget, env, false, true);
    }
}
