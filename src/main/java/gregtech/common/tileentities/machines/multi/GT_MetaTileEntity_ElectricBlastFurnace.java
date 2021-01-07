package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;

public class GT_MetaTileEntity_ElectricBlastFurnace
        extends GT_MetaTileEntity_AbstractMultiFurnace {
    private int mHeatingCapacity = 0;
    private int controllerY;
    private FluidStack[] pollutionFluidStacks = new FluidStack[]{Materials.CarbonDioxide.getGas(1000),
            Materials.CarbonMonoxide.getGas(1000), Materials.SulfurDioxide.getGas(1000)};

    private static final int CASING_INDEX = 11;

    public GT_MetaTileEntity_ElectricBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_ElectricBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ElectricBlastFurnace(this.mName);
    }

    @Override
    public String[] getDescription() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Blast Furnace")
                .addInfo("Controller block for the Electric Blast Furnace")
                .addInfo("You can use some fluids to reduce recipe time. Place the circuit in the Input Bus")
                .addInfo("Each 900K over the min. Heat required multiplies EU/t by 0.95")
                .addInfo("Each 1800K over the min. Heat required allows for one upgraded overclock instead of normal")
                .addInfo("Upgraded overclocks reduce recipe time to 25% (instead of 50%) and increase EU/t to 400%")
                .addInfo("Additionally gives +100K for every tier past MV")
                .addPollutionAmount(20 * getPollutionPerTick(null))
                .addSeparator()
                .beginStructureBlock(3, 4, 3, true)
                .addController("Front bottom")
                .addCasingInfo("Heat Proof Machine Casing", 0)
                .addOtherStructurePart("Heating Coils", "Two middle Layers")
                .addEnergyHatch("Any bottom layer casing")
                .addMaintenanceHatch("Any casing")
                .addMufflerHatch("Top middle")
                .addInputBus("Any bottom layer casing")
                .addInputHatch("Any bottom layer casing")
                .addOutputBus("Any bottom layer casing")
                .addOutputHatch("Gasses, Any top layer casing")
                .addStructureInfo("Recovery amount scales with Muffler Hatch tier")
                .addOutputHatch("Platline fluids, Any bottom layer casing")
                .toolTipFinisher("Gregtech");
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            return tt.getInformation();
        } else {
            return tt.getStructureInformation();
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][CASING_INDEX], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][CASING_INDEX]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ElectricBlastFurnace.png");
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sBlastRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ItemStack[] tInputs = getCompactedInputs();
        FluidStack[] tFluids = getCompactedFluids();

        if (tInputs.length <= 0)
            return false;

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(
                getBaseMetaTileEntity(),
                false,
                gregtech.api.enums.GT_Values.V[tTier],
                tFluids,
                tInputs
        );

        if (tRecipe == null)
            return false;
        if (this.mHeatingCapacity < tRecipe.mSpecialValue)
            return false;
        if (!tRecipe.isRecipeInputEqual(true, tFluids, tInputs))
            return false;

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        int tHeatCapacityDivTiers = (mHeatingCapacity - tRecipe.mSpecialValue) / 900;
        byte overclockCount = calculateOverclockednessEBF(tRecipe.mEUt, tRecipe.mDuration, tVoltage);
        //In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return false;
        if (this.mEUt > 0) {
            this.mEUt = (-this.mEUt);
        }
        if (tHeatCapacityDivTiers > 0) {
            this.mEUt = (int) (this.mEUt * (Math.pow(0.95, tHeatCapacityDivTiers)));
            this.mMaxProgresstime >>= Math.min(tHeatCapacityDivTiers / 2, overclockCount);//extra free overclocking if possible
            if (this.mMaxProgresstime < 1)
                this.mMaxProgresstime = 1;//no eu efficiency correction
        }
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
        this.mOutputItems = new ItemStack[]{
                tRecipe.getOutput(0),
                tRecipe.getOutput(1)
        };
        this.mOutputFluids = new FluidStack[]{
                tRecipe.getFluidOutput(0)
        };
        updateSlots();
        return true;
    }

    /**
     * Calcualtes overclocked ness using long integers
     *
     * @param aEUt      - recipe EUt
     * @param aDuration - recipe Duration
     */
    protected byte calculateOverclockednessEBF(int aEUt, int aDuration, long maxInputVoltage) {
        byte mTier = (byte) Math.max(0, GT_Utility.getTier(maxInputVoltage)), timesOverclocked = 0;
        if (mTier == 0) {
            //Long time calculation
            long xMaxProgresstime = ((long) aDuration) << 1;
            if (xMaxProgresstime > Integer.MAX_VALUE - 1) {
                //make impossible if too long
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUt = aEUt >> 2;
                mMaxProgresstime = (int) xMaxProgresstime;
            }
            //return 0;
        } else {
            //Long EUt calculation
            long xEUt = aEUt;
            //Isnt too low EUt check?
            long tempEUt = Math.max(xEUt, V[1]);

            mMaxProgresstime = aDuration;

            while (tempEUt <= V[mTier - 1]) {
                tempEUt <<= 2;//this actually controls overclocking
                //xEUt *= 4;//this is effect of everclocking
                mMaxProgresstime >>= 1;//this is effect of overclocking
                xEUt = mMaxProgresstime == 0 ? xEUt >> 1 : xEUt << 2;//U know, if the time is less than 1 tick make the machine use less power
                timesOverclocked++;
            }
            if (xEUt > Integer.MAX_VALUE - 1) {
                mEUt = Integer.MAX_VALUE - 1;
                mMaxProgresstime = Integer.MAX_VALUE - 1;
            } else {
                mEUt = (int) xEUt;
                if (mEUt == 0)
                    mEUt = 1;
                if (mMaxProgresstime == 0)
                    mMaxProgresstime = 1;//set time to 1 tick
            }
        }
        return timesOverclocked;
    }

    private boolean checkMachineFunction(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        controllerY = aBaseMetaTileEntity.getYCoord();
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

        this.mHeatingCapacity = 0;

        replaceDeprecatedCoils(aBaseMetaTileEntity);
        HeatingCoilLevel heatingCap = getInitialHeatLevel(aBaseMetaTileEntity, xDir, zDir);
        if (heatingCap == null)
            return false;

        if (!checkStructure(heatingCap, xDir, zDir, aBaseMetaTileEntity))
            return false;

        this.mHeatingCapacity = (int) heatingCap.getHeat();
        this.mHeatingCapacity += 100 * (GT_Utility.getTier(getMaxInputVoltage()) - 2);
        return true;
    }

    @Override
    protected boolean checkTopLayer(int i, int j, int xDir, int zDir, IGregTechTileEntity aBaseMetaTileEntity) {
        if ((i == 0) && (j == 0)) {
            return addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir), CASING_INDEX);
        }
        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 3, zDir + j);
        if (addOutputToMachineList(tTileEntity, CASING_INDEX))
            return true;

        if (addMaintenanceToMachineList(tTileEntity, CASING_INDEX))
            return true;

        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 3, zDir + j) != GregTech_API.sBlockCasings1)
            return false;

        return aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 3, zDir + j) == CASING_INDEX;
    }

    @Override
    protected boolean checkCoils(HeatingCoilLevel heatingCap, int i, int j, int xDir, int zDir, IGregTechTileEntity aBaseMetaTileEntity) {
        if ((i == 0) && (j == 0)) {
            if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir))
                return false;

            return aBaseMetaTileEntity.getAirOffset(xDir, 2, zDir);
        }

        Block blockLow = aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j);
        if (!(blockLow instanceof IHeatingCoil))
            return false;

        Block blockHi = aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j);
        if (!(blockHi instanceof IHeatingCoil))
            return false;

        byte metaLow = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j);
        HeatingCoilLevel coilHeatLow = ((IHeatingCoil) blockLow).getCoilHeat(metaLow);
        byte metaHi = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j);
        HeatingCoilLevel coilHeatHi = ((IHeatingCoil) blockHi).getCoilHeat(metaHi);

        return heatingCap == coilHeatLow && heatingCap == coilHeatHi;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return this.checkMachineFunction(aBaseMetaTileEntity, aStack);
    }

    private void replaceDeprecatedCoils(IGregTechTileEntity aBaseMetaTileEntity) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int tX = aBaseMetaTileEntity.getXCoord() + xDir;
        int tY = aBaseMetaTileEntity.getYCoord();
        int tZ = aBaseMetaTileEntity.getZCoord() + zDir;
        int tUsedMeta;
        for (int xPos = tX - 1; xPos <= tX + 1; xPos++) {
            for (int zPos = tZ - 1; zPos <= tZ + 1; zPos++) {
                if ((xPos == tX) && (zPos == tZ))
                    continue;
                for (int yPos = tY + 1; yPos <= tY + 2; yPos++) {
                    tUsedMeta = aBaseMetaTileEntity.getMetaID(xPos, yPos, zPos);
                    if (tUsedMeta < 12)
                        continue;
                    if (tUsedMeta > 14)
                        continue;
                    if (aBaseMetaTileEntity.getBlock(xPos, yPos, zPos) != GregTech_API.sBlockCasings1)
                        continue;

                    aBaseMetaTileEntity.getWorld().setBlock(
                            xPos,
                            yPos,
                            zPos,
                            GregTech_API.sBlockCasings5,
                            tUsedMeta - 12,
                            3
                    );
                }
            }
        }
    }

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null)
            return false;
        int targetHeight;
        FluidStack tLiquid = aLiquid.copy();
        boolean isOutputPollution = false;
        for (FluidStack pollutionFluidStack : pollutionFluidStacks) {
            if (!tLiquid.isFluidEqual(pollutionFluidStack))
                continue;

            isOutputPollution = true;
            break;
        }
        if (isOutputPollution) {
            targetHeight = this.controllerY + 3;
            int pollutionReduction = 0;
            for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
                if (!isValidMetaTileEntity(tHatch))
                    continue;
                pollutionReduction = 100 - tHatch.calculatePollutionReduction(100);
                break;
            }
            tLiquid.amount = tLiquid.amount * (pollutionReduction + 5) / 100;
        } else {
            targetHeight = this.controllerY;
        }
        for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
            if (isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid) ? !tHatch.outputsSteam() : !tHatch.outputsLiquids())
                continue;

            if (tHatch.getBaseMetaTileEntity().getYCoord() != targetHeight)
                continue;

            int tAmount = tHatch.fill(tLiquid, false);
            if (tAmount >= tLiquid.amount) {
                return tHatch.fill(tLiquid, true) >= tLiquid.amount;
            } else if (tAmount > 0) {
                tLiquid.amount = tLiquid.amount - tHatch.fill(tLiquid, true);
            }
        }
        return false;
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (!isValidMetaTileEntity(tHatch))
                continue;
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (!isValidMetaTileEntity(tHatch))
                continue;
            storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
        }

        return new String[]{
                StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " + EnumChatFormatting.GREEN + Integer.toString(mProgresstime / 20) + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + Integer.toString(mMaxProgresstime / 20) + EnumChatFormatting.RESET + " s",
                StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " + EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET + " EU",
                StatCollector.translateToLocal("GT5U.multiblock.usage") + ": " + EnumChatFormatting.RED + Integer.toString(-mEUt) + EnumChatFormatting.RESET + " EU/t",
                StatCollector.translateToLocal("GT5U.multiblock.mei") + ": " + EnumChatFormatting.YELLOW + Long.toString(getMaxInputVoltage()) + EnumChatFormatting.RESET + " EU/t(*2A) " + StatCollector.translateToLocal("GT5U.machines.tier") + ": " +
                        EnumChatFormatting.YELLOW + VN[GT_Utility.getTier(getMaxInputVoltage())] + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.problems") + ": " +
                        EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " " + StatCollector.translateToLocal("GT5U.multiblock.efficiency") + ": " +
                        EnumChatFormatting.YELLOW + Float.toString(mEfficiency / 100.0F) + EnumChatFormatting.RESET + " %",
                StatCollector.translateToLocal("GT5U.EBF.heat") + ": " +
                        EnumChatFormatting.GREEN + mHeatingCapacity + EnumChatFormatting.RESET + " K",
                StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": " + EnumChatFormatting.GREEN + mPollutionReduction + EnumChatFormatting.RESET + " %"
        };
    }
}