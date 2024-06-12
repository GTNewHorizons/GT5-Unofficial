package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_AirIntake;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusInput;

// Glee8e - 11/12/21 - 2:15pm
// Yeah, now I see what's wrong. Someone inherited from GregtechMeta_MultiBlockBase instead of
// GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialDehydrator> as it should have been
// so any method in GregtechMetaTileEntity_IndustrialDehydrator would see generic field declared in
// GregtechMeta_MultiBlockBase without generic parameter

public abstract class GregtechMeta_MultiBlockBase<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    public static final boolean DEBUG_DISABLE_CORES_TEMPORARILY = true;

    public GT_Recipe mLastRecipe;
    protected long mTotalRunTime = 0;

    /**
     * Don't use this for recipe input check, otherwise you'll get duplicated fluids
     */
    public ArrayList<GT_MetaTileEntity_Hatch_AirIntake> mAirIntakes = new ArrayList<>();

    public ArrayList<GT_MetaTileEntity_Hatch_InputBattery> mChargeHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_OutputBattery> mDischargeHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch> mAllEnergyHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch> mAllDynamoHatches = new ArrayList<>();

    public GregtechMeta_MultiBlockBase(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMeta_MultiBlockBase(final String aName) {
        super(aName);
    }

    private static int toStackCount(Entry<ItemStack, Integer> e) {
        int tMaxStackSize = e.getKey()
            .getMaxStackSize();
        int tStackSize = e.getValue();
        return (tStackSize + tMaxStackSize - 1) / tMaxStackSize;
    }

    public long getTotalRuntimeInTicks() {
        return this.mTotalRunTime;
    }

    public abstract String getMachineType();

    public String getMachineTooltip() {
        return "Machine Type: " + EnumChatFormatting.YELLOW + getMachineType() + EnumChatFormatting.RESET;
    }

    public String[] getExtraInfoData() {
        return new String[0];
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> mInfo = new ArrayList<>();
        if (!this.getMetaName()
            .equals("")) {
            mInfo.add(this.getMetaName());
        }

        String[] extra = getExtraInfoData();

        if (extra == null) {
            extra = new String[0];
        }
        if (extra.length > 0) {
            for (String s : extra) {
                mInfo.add(s);
            }
        }

        long seconds = (this.mTotalRunTime / 20);
        int weeks = (int) (TimeUnit.SECONDS.toDays(seconds) / 7);
        int days = (int) (TimeUnit.SECONDS.toDays(seconds) - 7 * weeks);
        long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(days)
            - TimeUnit.DAYS.toHours(7L * weeks);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

        mInfo.add(getMachineTooltip());

        // Lets borrow the GTNH handling

        mInfo.add(
            StatCollector.translateToLocal("GTPP.multiblock.progress") + ": "
                + EnumChatFormatting.GREEN
                + mProgresstime / 20
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + mMaxProgresstime / 20
                + EnumChatFormatting.RESET
                + " s");

        if (!this.mAllEnergyHatches.isEmpty()) {
            long storedEnergy = getStoredEnergyInAllEnergyHatches();
            long maxEnergy = getMaxEnergyStorageOfAllEnergyHatches();
            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.energy") + ":");
            mInfo.add(
                StatCollector.translateToLocal(
                    "" + EnumChatFormatting.GREEN
                        + Long.toString(storedEnergy)
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + Long.toString(maxEnergy)
                        + EnumChatFormatting.RESET
                        + " EU"));

            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.mei") + ":");
            mInfo.add(
                StatCollector.translateToLocal(
                    "" + EnumChatFormatting.YELLOW
                        + Long.toString(getMaxInputVoltage())
                        + EnumChatFormatting.RESET
                        + " EU/t(*2A) "
                        + StatCollector.translateToLocal("GTPP.machines.tier")
                        + ": "
                        + EnumChatFormatting.YELLOW
                        + GT_Values.VN[GT_Utility.getTier(getMaxInputVoltage())]
                        + EnumChatFormatting.RESET));;
        }
        if (!this.mAllDynamoHatches.isEmpty()) {
            long storedEnergy = getStoredEnergyInAllDynamoHatches();
            long maxEnergy = getMaxEnergyStorageOfAllDynamoHatches();
            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.energy") + " In Dynamos:");
            mInfo.add(
                StatCollector.translateToLocal(
                    "" + EnumChatFormatting.GREEN
                        + Long.toString(storedEnergy)
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + Long.toString(maxEnergy)
                        + EnumChatFormatting.RESET
                        + " EU"));
        }

        if (-lEUt > 0) {
            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.usage") + ":");
            mInfo.add(
                StatCollector
                    .translateToLocal("" + EnumChatFormatting.RED + (-lEUt) + EnumChatFormatting.RESET + " EU/t"));
        } else {
            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.generation") + ":");
            mInfo.add(
                StatCollector
                    .translateToLocal("" + EnumChatFormatting.GREEN + lEUt + EnumChatFormatting.RESET + " EU/t"));
        }

        mInfo.add(
            StatCollector.translateToLocal("GTPP.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GTPP.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + Float.toString(mEfficiency / 100.0F)
                + EnumChatFormatting.RESET
                + " %");

        if (this.getPollutionPerSecond(null) > 0) {
            int mPollutionReduction = getPollutionReductionForAllMufflers();
            mInfo.add(
                StatCollector.translateToLocal("GTPP.multiblock.pollution") + ": "
                    + EnumChatFormatting.RED
                    + this.getPollutionPerSecond(null)
                    + EnumChatFormatting.RESET
                    + "/sec");
            mInfo.add(
                StatCollector.translateToLocal("GTPP.multiblock.pollutionreduced") + ": "
                    + EnumChatFormatting.GREEN
                    + mPollutionReduction
                    + EnumChatFormatting.RESET
                    + " %");
        }

        mInfo.add(
            StatCollector.translateToLocal("GTPP.CC.parallel") + ": "
                + EnumChatFormatting.GREEN
                + (getMaxParallelRecipes())
                + EnumChatFormatting.RESET);

        mInfo.add(
            "Total Time Since Built: " + EnumChatFormatting.DARK_GREEN
                + Integer.toString(weeks)
                + EnumChatFormatting.RESET
                + " Weeks, "
                + EnumChatFormatting.DARK_GREEN
                + Integer.toString(days)
                + EnumChatFormatting.RESET
                + " Days, ");
        mInfo.add(
            EnumChatFormatting.DARK_GREEN + Long.toString(hours)
                + EnumChatFormatting.RESET
                + " Hours, "
                + EnumChatFormatting.DARK_GREEN
                + Long.toString(minutes)
                + EnumChatFormatting.RESET
                + " Minutes, "
                + EnumChatFormatting.DARK_GREEN
                + Long.toString(second)
                + EnumChatFormatting.RESET
                + " Seconds.");
        mInfo.add("Total Time in ticks: " + EnumChatFormatting.DARK_GREEN + Long.toString(this.mTotalRunTime));

        String[] mInfo2 = mInfo.toArray(new String[mInfo.size()]);
        return mInfo2;
    }

    public int getPollutionReductionForAllMufflers() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : filterValidMTEs(mMufflerHatches)) {
            mPollutionReduction = Math.max(calculatePollutionReductionForHatch(tHatch, 100), mPollutionReduction);
        }
        return mPollutionReduction;
    }

    public long getStoredEnergyInAllEnergyHatches() {
        long storedEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(mAllEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
        }
        return storedEnergy;
    }

    public long getMaxEnergyStorageOfAllEnergyHatches() {
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(mAllEnergyHatches)) {
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        return maxEnergy;
    }

    public long getStoredEnergyInAllDynamoHatches() {
        long storedEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(mAllDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
        }
        return storedEnergy;
    }

    public long getMaxEnergyStorageOfAllDynamoHatches() {
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(mAllDynamoHatches)) {
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        return maxEnergy;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    private String[] aCachedToolTip;

    /*
     * private final String aRequiresMuffler = "1x Muffler Hatch"; private final String aRequiresCoreModule =
     * "1x Core Module"; private final String aRequiresMaint = "1x Maintanence Hatch";
     */

    public static final String TAG_HIDE_HATCHES = "TAG_HIDE_HATCHES";
    public static final String TAG_HIDE_MAINT = "TAG_HIDE_MAINT";
    public static final String TAG_HIDE_POLLUTION = "TAG_HIDE_POLLUTION";
    public static final String TAG_HIDE_MACHINE_TYPE = "TAG_HIDE_MACHINE_TYPE";

    public abstract int getMaxParallelRecipes();

    @Override
    public boolean isCorrectMachinePart(final ItemStack paramItemStack) {
        return true;
    }

    @Override
    public int getDamageToComponent(final ItemStack paramItemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack p0) {
        return false;
    }

    /**
     * A Static {@link Method} object which holds the current status of logging.
     */
    public static Method aLogger = null;

    public void log(String s) {
        if (!AsmConfig.disableAllLogging) {
            if (CORE_Preloader.DEBUG_MODE) {
                Logger.INFO(s);
            } else {
                Logger.MACHINE_INFO(s);
            }
        }
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GT_Utility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    public long getMaxInputEnergy() {
        long rEnergy = 0;
        if (mEnergyHatches.size() == 1) // so it only takes 1 amp is only 1 hatch is present so it works like most gt
                                        // multies
            return mEnergyHatches.get(0)
                .getBaseMetaTileEntity()
                .getInputVoltage();
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches))
            rEnergy += tHatch.getBaseMetaTileEntity()
                .getInputVoltage()
                * tHatch.getBaseMetaTileEntity()
                    .getInputAmperage();
        return rEnergy;
    }

    public boolean isMachineRunning() {
        boolean aRunning = this.getBaseMetaTileEntity()
            .isActive();
        // log("Queried Multiblock is currently running: "+aRunning);
        return aRunning;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {

        // Time Counter
        if (aBaseMetaTileEntity.isServerSide()) {
            this.mTotalRunTime++;
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mChargeHatches.clear();
                this.mDischargeHatches.clear();
                this.mAirIntakes.clear();
                this.mTecTechEnergyHatches.clear();
                this.mTecTechDynamoHatches.clear();
                this.mAllEnergyHatches.clear();
                this.mAllDynamoHatches.clear();
            }
        }

        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void explodeMultiblock() {
        MetaTileEntity tTileEntity;
        for (final Iterator<GT_MetaTileEntity_Hatch_InputBattery> localIterator = this.mChargeHatches
            .iterator(); localIterator.hasNext(); tTileEntity.getBaseMetaTileEntity()
                .doExplosion(gregtech.api.enums.GT_Values.V[8])) {
            tTileEntity = localIterator.next();
        }
        tTileEntity = null;
        for (final Iterator<GT_MetaTileEntity_Hatch_OutputBattery> localIterator = this.mDischargeHatches
            .iterator(); localIterator.hasNext(); tTileEntity.getBaseMetaTileEntity()
                .doExplosion(gregtech.api.enums.GT_Values.V[8])) {
            tTileEntity = localIterator.next();
        }
        tTileEntity = null;
        for (final Iterator<GT_MetaTileEntity_Hatch> localIterator = this.mTecTechDynamoHatches
            .iterator(); localIterator.hasNext(); tTileEntity.getBaseMetaTileEntity()
                .doExplosion(gregtech.api.enums.GT_Values.V[8])) {
            tTileEntity = localIterator.next();
        }
        tTileEntity = null;
        for (final Iterator<GT_MetaTileEntity_Hatch> localIterator = this.mTecTechEnergyHatches
            .iterator(); localIterator.hasNext(); tTileEntity.getBaseMetaTileEntity()
                .doExplosion(gregtech.api.enums.GT_Values.V[8])) {
            tTileEntity = localIterator.next();
        }

        super.explodeMultiblock();
    }

    protected boolean setGUIItemStack(ItemStack aNewGuiSlotContents) {
        boolean result = false;
        if (this.mInventory[1] == null) {
            this.mInventory[1] = aNewGuiSlotContents != null ? aNewGuiSlotContents.copy() : null;
            this.depleteInput(aNewGuiSlotContents);
            this.updateSlots();
            result = true;
        }
        return result;
    }

    public ItemStack findItemInInventory(Item aSearchStack) {
        return findItemInInventory(aSearchStack, 0);
    }

    public ItemStack findItemInInventory(Item aSearchStack, int aMeta) {
        return findItemInInventory(ItemUtils.simpleMetaStack(aSearchStack, aMeta, 1));
    }

    public ItemStack findItemInInventory(ItemStack aSearchStack) {
        if (aSearchStack != null && this.mInputBusses.size() > 0) {
            for (GT_MetaTileEntity_Hatch_InputBus bus : this.mInputBusses) {
                if (bus != null) {
                    for (ItemStack uStack : bus.mInventory) {
                        if (uStack != null) {
                            if (aSearchStack.getClass()
                                .isInstance(uStack.getItem())) {
                                return uStack;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Deplete fluid input from a set of restricted hatches. This assumes these hatches can store nothing else but your
     * expected fluid
     */
    protected boolean depleteInputFromRestrictedHatches(Collection<GT_MetaTileEntity_Hatch_CustomFluidBase> aHatches,
        int aAmount) {
        for (final GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : filterValidMTEs(aHatches)) {
            FluidStack tLiquid = tHatch.getFluid();
            if (tLiquid == null || tLiquid.amount < aAmount) {
                continue;
            }
            tLiquid = tHatch.drain(aAmount, false);
            if (tLiquid != null && tLiquid.amount >= aAmount) {
                tLiquid = tHatch.drain(aAmount, true);
                return tLiquid != null && tLiquid.amount >= aAmount;
            }
        }
        return false;
    }

    @Override
    public void updateSlots() {
        for (final GT_MetaTileEntity_Hatch_InputBattery tHatch : filterValidMTEs(this.mChargeHatches)) {
            tHatch.updateSlots();
        }
        for (final GT_MetaTileEntity_Hatch_OutputBattery tHatch : filterValidMTEs(this.mDischargeHatches)) {
            tHatch.updateSlots();
        }
        super.updateSlots();
    }

    public boolean checkHatch() {
        return mMaintenanceHatches.size() <= 1
            && (this.getPollutionPerSecond(null) > 0 ? !mMufflerHatches.isEmpty() : true);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        this.mChargeHatches.clear();
        this.mDischargeHatches.clear();
        this.mAirIntakes.clear();
        this.mTecTechEnergyHatches.clear();
        this.mTecTechDynamoHatches.clear();
        this.mAllEnergyHatches.clear();
        this.mAllDynamoHatches.clear();
    }

    public <E> boolean addToMachineListInternal(ArrayList<E> aList, final IGregTechTileEntity aTileEntity,
        final int aBaseCasingIndex) {
        return addToMachineListInternal(aList, getMetaTileEntity(aTileEntity), aBaseCasingIndex);
    }

    public <E> boolean addToMachineListInternal(ArrayList<E> aList, final IMetaTileEntity aTileEntity,
        final int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }

        // Check type
        /*
         * Class <?> aHatchType = ReflectionUtils.getTypeOfGenericObject(aList); if
         * (!aHatchType.isInstance(aTileEntity)) { return false; }
         */

        // Try setRecipeMap

        try {
            if (aTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                resetRecipeMapForHatch((GT_MetaTileEntity_Hatch) aTileEntity, getRecipeMap());
            }
            if (aTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                resetRecipeMapForHatch((GT_MetaTileEntity_Hatch) aTileEntity, getRecipeMap());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (aList.isEmpty()) {
            if (aTileEntity instanceof GT_MetaTileEntity_Hatch) {
                if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
                    log(
                        "Adding " + aTileEntity.getInventoryName()
                            + " at "
                            + new BlockPos(aTileEntity.getBaseMetaTileEntity()).getLocationString());
                }
                updateTexture(aTileEntity, aBaseCasingIndex);
                return aList.add((E) aTileEntity);
            }
        } else {
            IGregTechTileEntity aCur = aTileEntity.getBaseMetaTileEntity();
            if (aList.contains(aTileEntity)) {
                log(
                    "Found Duplicate " + aTileEntity.getInventoryName()
                        + " @ "
                        + new BlockPos(aCur).getLocationString());
                return false;
            }
            BlockPos aCurPos = new BlockPos(aCur);
            boolean aExists = false;
            for (E m : aList) {
                IGregTechTileEntity b = ((IMetaTileEntity) m).getBaseMetaTileEntity();
                if (b != null) {
                    BlockPos aPos = new BlockPos(b);
                    if (aPos != null) {
                        if (aCurPos.equals(aPos)) {
                            if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
                                log("Found Duplicate " + b.getInventoryName() + " at " + aPos.getLocationString());
                            }
                            return false;
                        }
                    }
                }
            }
            if (aTileEntity instanceof GT_MetaTileEntity_Hatch) {
                if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
                    log("Adding " + aCur.getInventoryName() + " at " + aCurPos.getLocationString());
                }
                updateTexture(aTileEntity, aBaseCasingIndex);
                return aList.add((E) aTileEntity);
            }
        }
        return false;
    }

    private IMetaTileEntity getMetaTileEntity(final IGregTechTileEntity aTileEntity) {
        if (aTileEntity == null) {
            return null;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        return aMetaTileEntity;
    }

    @Override
    public boolean addToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        return addToMachineList(getMetaTileEntity(aTileEntity), aBaseCasingIndex);
    }

    public boolean addToMachineList(final IMetaTileEntity aMetaTileEntity, final int aBaseCasingIndex) {
        if (aMetaTileEntity == null) {
            return false;
        }

        // Use this to determine the correct value, then update the hatch texture after.
        boolean aDidAdd = false;

        // Handle Custom Hatches
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBattery) {
            log("Found GT_MetaTileEntity_Hatch_InputBattery");
            aDidAdd = addToMachineListInternal(mChargeHatches, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBattery) {
            log("Found GT_MetaTileEntity_Hatch_OutputBattery");
            aDidAdd = addToMachineListInternal(mDischargeHatches, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_AirIntake) {
            aDidAdd = addToMachineListInternal(mAirIntakes, aMetaTileEntity, aBaseCasingIndex)
                && addToMachineListInternal(mInputHatches, aMetaTileEntity, aBaseCasingIndex);
        }

        // Handle TT Multi-A Energy Hatches
        else if (isThisHatchMultiEnergy(aMetaTileEntity)) {
            log("Found isThisHatchMultiEnergy");
            aDidAdd = addToMachineListInternal(mTecTechEnergyHatches, aMetaTileEntity, aBaseCasingIndex);
            updateMasterEnergyHatchList(aMetaTileEntity);
        }

        // Handle TT Multi-A Dynamos
        else if (isThisHatchMultiDynamo(aMetaTileEntity)) {
            log("Found isThisHatchMultiDynamo");
            aDidAdd = addToMachineListInternal(mTecTechDynamoHatches, aMetaTileEntity, aBaseCasingIndex);
            updateMasterDynamoHatchList(aMetaTileEntity);
        }

        // Handle Fluid Hatches using seperate logic
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)
            aDidAdd = addToMachineListInternal(mInputHatches, aMetaTileEntity, aBaseCasingIndex);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            aDidAdd = addToMachineListInternal(mOutputHatches, aMetaTileEntity, aBaseCasingIndex);

        // Process Remaining hatches using Vanilla GT Logic
        else if (aMetaTileEntity instanceof IDualInputHatch hatch) {
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            aDidAdd = addToMachineListInternal(mDualInputHatches, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus)
            aDidAdd = addToMachineListInternal(mInputBusses, aMetaTileEntity, aBaseCasingIndex);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
            aDidAdd = addToMachineListInternal(mOutputBusses, aMetaTileEntity, aBaseCasingIndex);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            aDidAdd = addToMachineListInternal(mEnergyHatches, aMetaTileEntity, aBaseCasingIndex);
            updateMasterEnergyHatchList(aMetaTileEntity);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            aDidAdd = addToMachineListInternal(mDynamoHatches, aMetaTileEntity, aBaseCasingIndex);
            updateMasterDynamoHatchList(aMetaTileEntity);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            aDidAdd = addToMachineListInternal(mMaintenanceHatches, aMetaTileEntity, aBaseCasingIndex);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
            aDidAdd = addToMachineListInternal(mMufflerHatches, aMetaTileEntity, aBaseCasingIndex);

        // return super.addToMachineList(aTileEntity, aBaseCasingIndex);
        return aDidAdd;
    }

    @Override
    public boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    @Override
    public boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    @Override
    public boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input
            || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    @Override
    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output
            || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean addAirIntakeToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_AirIntake) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean addFluidInputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        return addFluidInputToMachineList(getMetaTileEntity(aTileEntity), aBaseCasingIndex);
    }

    public boolean addFluidInputToMachineList(final IMetaTileEntity aMetaTileEntity, final int aBaseCasingIndex) {
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean clearRecipeMapForAllInputHatches() {
        return resetRecipeMapForAllInputHatches(null);
    }

    public boolean resetRecipeMapForAllInputHatches() {
        return resetRecipeMapForAllInputHatches(this.getRecipeMap());
    }

    public boolean resetRecipeMapForAllInputHatches(RecipeMap<?> aMap) {
        int cleared = 0;
        for (GT_MetaTileEntity_Hatch_Input g : this.mInputHatches) {
            if (resetRecipeMapForHatch(g, aMap)) {
                cleared++;
            }
        }
        for (GT_MetaTileEntity_Hatch_InputBus g : this.mInputBusses) {
            if (resetRecipeMapForHatch(g, aMap)) {
                cleared++;
            }
        }
        return cleared > 0;
    }

    public boolean resetRecipeMapForHatch(IGregTechTileEntity aTileEntity, RecipeMap<?> aMap) {
        try {
            final IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
            if (aMetaTileEntity == null) {
                return false;
            }
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input
                || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus
                || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Steam_BusInput) {
                return resetRecipeMapForHatch((GT_MetaTileEntity_Hatch) aMetaTileEntity, aMap);
            } else {
                return false;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public boolean resetRecipeMapForHatch(GT_MetaTileEntity_Hatch aTileEntity, RecipeMap<?> aMap) {
        if (aTileEntity == null) {
            return false;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input
            || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus
            || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Steam_BusInput) {
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = null;
                ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = aMap;
                if (aMap != null) {
                    log("Remapped Input Hatch to " + aMap.unlocalizedName + ".");
                } else {
                    log("Cleared Input Hatch.");
                }
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = null;
                ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = aMap;
                if (aMap != null) {
                    log("Remapped Input Bus to " + aMap.unlocalizedName + ".");
                } else {
                    log("Cleared Input Bus.");
                }
            } else {
                ((GT_MetaTileEntity_Hatch_Steam_BusInput) aMetaTileEntity).mRecipeMap = null;
                ((GT_MetaTileEntity_Hatch_Steam_BusInput) aMetaTileEntity).mRecipeMap = aMap;
                if (aMap != null) {
                    log("Remapped Input Bus to " + aMap.unlocalizedName + ".");
                } else {
                    log("Cleared Input Bus.");
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
        clearRecipeMapForAllInputHatches();
        onModeChangeByScrewdriver(side, aPlayer, aX, aY, aZ);
        resetRecipeMapForAllInputHatches();
    }

    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {}

    /**
     * Enable Texture Casing Support if found in GT 5.09
     */
    public boolean updateTexture(final IGregTechTileEntity aTileEntity, int aCasingID) {
        return updateTexture(getMetaTileEntity(aTileEntity), aCasingID);
    }

    /**
     * Enable Texture Casing Support if found in GT 5.09
     */
    public boolean updateTexture(final IMetaTileEntity aTileEntity, int aCasingID) {
        try { // gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch.updateTexture(int)

            final IMetaTileEntity aMetaTileEntity = aTileEntity;
            if (aMetaTileEntity == null) {
                return false;
            }
            Method mProper = ReflectionUtils.getMethod(GT_MetaTileEntity_Hatch.class, "updateTexture", int.class);
            if (mProper != null) {
                if (GT_MetaTileEntity_Hatch.class.isInstance(aMetaTileEntity)) {
                    mProper.setAccessible(true);
                    mProper.invoke(aMetaTileEntity, aCasingID);
                    // log("Good Method Call for updateTexture.");
                    return true;
                }
            } else {
                log("Bad Method Call for updateTexture.");
                if (GT_MetaTileEntity_Hatch.class.isInstance(aMetaTileEntity)) {
                    if (aCasingID <= Byte.MAX_VALUE) {
                        ((GT_MetaTileEntity_Hatch) aTileEntity).updateTexture(aCasingID);
                        log(
                            "Good Method Call for updateTexture. Used fallback method of setting mMachineBlock as casing id was <= 128.");
                        return true;
                    } else {
                        log("updateTexture returning false. 1.2");
                    }
                } else {
                    log("updateTexture returning false. 1.3");
                }
            }
            log("updateTexture returning false. 1");
            return false;
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log("updateTexture returning false.");
            log("updateTexture returning false. 2");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * TecTech Support
     */

    /**
     * This is the array Used to Store the Tectech Multi-Amp Dynamo hatches.
     */
    public ArrayList<GT_MetaTileEntity_Hatch> mTecTechDynamoHatches = new ArrayList<>();

    /**
     * This is the array Used to Store the Tectech Multi-Amp Energy hatches.
     */
    public ArrayList<GT_MetaTileEntity_Hatch> mTecTechEnergyHatches = new ArrayList<>();

    /**
     * TecTech Multi-Amp Dynamo Support
     *
     * @param aTileEntity      - The Dynamo Hatch
     * @param aBaseCasingIndex - Casing Texture
     * @return
     */
    public boolean addMultiAmpDynamoToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        final IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity == null) {
            return false;
        }
        if (isThisHatchMultiDynamo(aTileEntity)) {
            return addToMachineListInternal(mTecTechDynamoHatches, aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean isThisHatchMultiDynamo(IGregTechTileEntity aTileEntity) {
        return isThisHatchMultiDynamo(getMetaTileEntity(aTileEntity));
    }

    public boolean isThisHatchMultiDynamo(IMetaTileEntity aMetaTileEntity) {
        Class<?> mDynamoClass;
        mDynamoClass = ReflectionUtils
            .getClass("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti");
        if (mDynamoClass != null) {
            if (mDynamoClass.isInstance(aMetaTileEntity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo || isThisHatchMultiDynamo(aMetaTileEntity)) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    private boolean updateMasterDynamoHatchList(IMetaTileEntity aMetaTileEntity) {
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch aHatch) {
            return mAllDynamoHatches.add(aHatch);
        }
        return false;
    }

    /**
     * TecTech Multi-Amp Energy Hatch Support
     *
     * @param aTileEntity      - The Energy Hatch
     * @param aBaseCasingIndex - Casing Texture
     * @return
     */
    public boolean addMultiAmpEnergyToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        final IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity == null) {
            return false;
        }
        if (isThisHatchMultiEnergy(aMetaTileEntity)) {
            return addToMachineListInternal(mTecTechEnergyHatches, aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean isThisHatchMultiEnergy(IGregTechTileEntity aTileEntity) {
        return isThisHatchMultiEnergy(getMetaTileEntity(aTileEntity));
    }

    public boolean isThisHatchMultiEnergy(IMetaTileEntity aMetaTileEntity) {
        Class<?> mDynamoClass;
        mDynamoClass = ReflectionUtils
            .getClass("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti");
        if (mDynamoClass != null) {
            if (mDynamoClass.isInstance(aMetaTileEntity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy || isThisHatchMultiEnergy(aMetaTileEntity)) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    private boolean updateMasterEnergyHatchList(IMetaTileEntity aMetaTileEntity) {
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch aHatch) {
            return mAllEnergyHatches.add(aHatch);
        }
        return false;
    }

    /**
     * Pollution Management
     */
    public int calculatePollutionReductionForHatch(GT_MetaTileEntity_Hatch_Muffler hatch, int poll) {
        return hatch.calculatePollutionReduction(poll);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setLong("mTotalRunTime", this.mTotalRunTime);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mTotalRunTime = aNBT.getLong("mTotalRunTime");
        if (aNBT.hasKey("mVoidExcess")) {
            // backward compatibility
            voidingMode = aNBT.getBoolean("mVoidExcess") ? VoidingMode.VOID_ALL : VoidingMode.VOID_NONE;
        }
        if (aNBT.hasKey("mUseMultiparallelMode")) {
            // backward compatibility
            batchMode = aNBT.getBoolean("mUseMultiparallelMode");
        }
    }

    /**
     * Custom Tool Handling
     */
    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        // Do Things
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            // Logger.INFO("Right Clicked Controller.");
            ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
            if (tCurrentItem != null) {
                // Logger.INFO("Holding Item.");
                if (tCurrentItem.getItem() instanceof GT_MetaGenerated_Tool) {
                    // Logger.INFO("Is GT_MetaGenerated_Tool.");
                    int[] aOreID = OreDictionary.getOreIDs(tCurrentItem);
                    for (int id : aOreID) {
                        // Plunger
                        if (OreDictionary.getOreName(id)
                            .equals("craftingToolPlunger")) {
                            // Logger.INFO("Is Plunger.");
                            return onPlungerRightClick(aPlayer, side, aX, aY, aZ);
                        }
                    }
                }
            }
        }
        // Do Super
        boolean aSuper = super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        return aSuper;
    }

    public boolean onPlungerRightClick(EntityPlayer aPlayer, ForgeDirection side, float aX, float aY, float aZ) {
        int aHatchIndex = 0;
        PlayerUtils.messagePlayer(aPlayer, "Trying to clear " + mOutputHatches.size() + " output hatches.");
        for (GT_MetaTileEntity_Hatch_Output hatch : this.mOutputHatches) {
            if (hatch.mFluid != null) {
                PlayerUtils.messagePlayer(
                    aPlayer,
                    "Clearing " + hatch.mFluid.amount
                        + "L of "
                        + hatch.mFluid.getLocalizedName()
                        + " from hatch "
                        + aHatchIndex
                        + ".");
                hatch.mFluid = null;
            }
            aHatchIndex++;
        }
        return aHatchIndex > 0;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        if (supportsVoidProtection() && wrenchingSide == getBaseMetaTileEntity().getFrontFacing()) {
            Set<VoidingMode> allowed = getAllowedVoidingModes();
            setVoidingMode(getVoidingMode().nextInCollection(allowed));
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.gui.button.voiding_mode") + " "
                    + StatCollector.translateToLocal(getVoidingMode().getTransKey()));
            return true;
        } else return super.onSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
    }

    // Only support to use meta to tier

    /**
     * accept meta [0, maxMeta)
     *
     * @param maxMeta exclusive
     */
    public static <T> IStructureElement<T> addTieredBlock(Block aBlock, BiConsumer<T, Integer> aSetTheMeta,
        Function<T, Integer> aGetTheMeta, int maxMeta) {
        return addTieredBlock(aBlock, (t, i) -> {
            aSetTheMeta.accept(t, i);
            return true;
        }, aGetTheMeta, 0, maxMeta);
    }

    /**
     *
     * @param minMeta inclusive
     * @param maxMeta exclusive
     */
    public static <T> IStructureElement<T> addTieredBlock(Block aBlock, BiConsumer<T, Integer> aSetTheMeta,
        Function<T, Integer> aGetTheMeta, int minMeta, int maxMeta) {
        return addTieredBlock(aBlock, (t, i) -> {
            aSetTheMeta.accept(t, i);
            return true;
        }, aGetTheMeta, minMeta, maxMeta);
    }

    /**
     *
     * @param minMeta inclusive
     * @param maxMeta exclusive
     */
    public static <T> IStructureElement<T> addTieredBlock(Block aBlock, BiPredicate<T, Integer> aSetTheMeta,
        Function<T, Integer> aGetTheMeta, int minMeta, int maxMeta) {

        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block tBlock = world.getBlock(x, y, z);
                if (aBlock == tBlock) {
                    Integer currentMeta = aGetTheMeta.apply(t);
                    int newMeta = tBlock.getDamageValue(world, x, y, z) + 1;
                    if (newMeta > maxMeta || newMeta < minMeta + 1) return false;
                    if (currentMeta == 0) {
                        return aSetTheMeta.test(t, newMeta);
                    } else {
                        return currentMeta == newMeta;
                    }
                }
                return false;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, aBlock, getMeta(trigger));
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, aBlock, getMeta(trigger), 3);
            }

            private int getMeta(ItemStack trigger) {
                int meta = trigger.stackSize;
                if (meta <= 0) meta = minMeta;
                if (meta + minMeta >= maxMeta) meta = maxMeta - 1 - minMeta;
                return meta + minMeta;
            }

            @Nullable
            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(aBlock, getMeta(trigger));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                if (world.getBlock(x, y, z) == aBlock) {
                    if (world.getBlockMetadata(x, y, z) == getMeta(trigger)) {
                        return PlaceResult.SKIP;
                    }
                    return PlaceResult.REJECT;
                }
                return StructureUtility.survivalPlaceBlock(
                    aBlock,
                    getMeta(trigger),
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());
            }
        };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(getActiveOverlay())
                .extFacing()
                .build() };
            return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(getInactiveOverlay())
                .extFacing()
                .build() };
        }
        return new ITexture[] { getCasingTexture() };
    }

    protected IIconContainer getActiveOverlay() {
        return null;
    }

    protected IIconContainer getInactiveOverlay() {
        return null;
    }

    protected ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(getCasingTextureId());
    }

    protected int getCasingTextureId() {
        return 0;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (doesBindPlayerInventory()) {
            super.addUIWidgets(builder, buildContext);
        } else {
            addNoPlayerInventoryUI(builder, buildContext);
        }
    }

    private static final Materials GOOD = Materials.Uranium;
    private static final Materials BAD = Materials.Plutonium;
    private static final ConcurrentHashMap<String, ItemStack> mToolStacks = new ConcurrentHashMap<>();

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    protected void addNoPlayerInventoryUI(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(3, 4)
                .setSize(152, 159));
        for (int i = 0; i < 9; i++) {
            builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.BUTTON_STANDARD)
                    .setPos(155, 3 + i * 18)
                    .setSize(18, 18));
        }

        DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTextsNoPlayerInventory(screenElements);
        builder.widget(screenElements);

        setupToolDisplay();

        builder.widget(
            new ItemDrawable(() -> mToolStacks.get(mWrench + "WRENCH")).asWidget()
                .setPos(156, 58))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mWrench, val -> mWrench = val));
        builder.widget(
            new ItemDrawable(() -> mToolStacks.get(mCrowbar + "CROWBAR")).asWidget()
                .setPos(156, 76))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mCrowbar, val -> mCrowbar = val));
        builder.widget(
            new ItemDrawable(() -> mToolStacks.get(mHardHammer + "HARDHAMMER")).asWidget()
                .setPos(156, 94))
            .widget(
                new TextWidget("H").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(167, 103))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mHardHammer, val -> mHardHammer = val));
        builder.widget(
            new ItemDrawable(() -> mToolStacks.get(mSoftHammer + "SOFTHAMMER")).asWidget()
                .setPos(156, 112))
            .widget(
                new TextWidget("M").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(167, 121))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSoftHammer, val -> mSoftHammer = val));
        builder.widget(
            new ItemDrawable(() -> mToolStacks.get(mScrewdriver + "SCREWDRIVER")).asWidget()
                .setPos(156, 130))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mScrewdriver, val -> mScrewdriver = val));
        builder.widget(
            new ItemDrawable(() -> mToolStacks.get(mSolderingTool + "SOLDERING_IRON_LV")).asWidget()
                .setPos(156, 148))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSolderingTool, val -> mSolderingTool = val));
        builder.widget(
            new ItemDrawable(() -> mToolStacks.get(getBaseMetaTileEntity().isActive() + "GLASS")).asWidget()
                .setPos(156, 22))
            .widget(
                TextWidget.dynamicString(() -> getBaseMetaTileEntity().isActive() ? "On" : "Off")
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(157, 31))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> getBaseMetaTileEntity().isActive(),
                    val -> getBaseMetaTileEntity().setActive(val)));
    }

    protected void drawTextsNoPlayerInventory(DynamicPositionedColumn screenElements) {
        screenElements.setSynced(false)
            .setSpace(0)
            .setPos(6, 7);

        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("138", "Incomplete Structure.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal("GTPP.machines.input") + " "
                            + StatCollector.translateToLocal("GTPP.machines.tier")
                            + ": "
                            + EnumChatFormatting.GREEN
                            + GT_Values.VOLTAGE_NAMES[(int) getInputTier()])
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine && getInputTier() > 0))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal("GTPP.machines.output") + " "
                            + StatCollector.translateToLocal("GTPP.machines.tier")
                            + ": "
                            + EnumChatFormatting.GREEN
                            + GT_Values.VOLTAGE_NAMES[(int) getOutputTier()])
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine && getOutputTier() > 0))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal("GTPP.multiblock.progress") + ": "
                            + EnumChatFormatting.GREEN
                            + getBaseMetaTileEntity().getProgress() / 20
                            + EnumChatFormatting.RESET
                            + " s / "
                            + EnumChatFormatting.YELLOW
                            + getBaseMetaTileEntity().getMaxProgress() / 20
                            + EnumChatFormatting.RESET
                            + " s")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GTPP.multiblock.energy") + ":")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal(
                            "" + EnumChatFormatting.GREEN
                                + getStoredEnergyInAllEnergyHatches()
                                + EnumChatFormatting.RESET
                                + " EU / "
                                + EnumChatFormatting.YELLOW
                                + getMaxEnergyStorageOfAllEnergyHatches()
                                + EnumChatFormatting.RESET
                                + " EU"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GTPP.multiblock.usage") + ":")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine && getLastRecipeEU() > 0 && getLastRecipeDuration() > 0))
            .widget(
                TextWidget.dynamicString(
                    () -> StatCollector.translateToLocal(
                        "" + EnumChatFormatting.RED + -getLastRecipeEU() + EnumChatFormatting.RESET + " EU/t/parallel"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine && getLastRecipeEU() > 0 && getLastRecipeDuration() > 0))
            .widget(
                TextWidget.dynamicString(() -> StatCollector.translateToLocal("GTPP.multiblock.generation") + ":")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine && getLastRecipeEU() < 0 && getLastRecipeDuration() > 0))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal(
                            "" + EnumChatFormatting.GREEN
                                + getLastRecipeEU()
                                + EnumChatFormatting.RESET
                                + " EU/t/parallel"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine && getLastRecipeEU() < 0 && getLastRecipeDuration() > 0))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal("GTPP.multiblock.duration") + ": "
                            + EnumChatFormatting.RED
                            + getLastRecipeDuration()
                            + EnumChatFormatting.RESET
                            + " ticks")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine && getLastRecipeEU() != 0 && getLastRecipeDuration() > 0))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal("GTPP.multiblock.specialvalue") + ": "
                            + EnumChatFormatting.RED
                            + getLastRecipeEU()
                            + EnumChatFormatting.RESET
                            + "")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(
                        widget -> mMachine && getLastRecipeEU() != 0
                            && getLastRecipeDuration() > 0
                            && (mLastRecipe != null ? mLastRecipe.mSpecialValue : 0) > 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GTPP.multiblock.mei") + ":")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal(
                            "" + EnumChatFormatting.YELLOW
                                + getMaxInputVoltage()
                                + EnumChatFormatting.RESET
                                + " EU/t(*2A) "
                                + StatCollector.translateToLocal("GTPP.machines.tier")
                                + ": "
                                + EnumChatFormatting.YELLOW
                                + GT_Values.VN[GT_Utility.getTier(getMaxInputVoltage())]
                                + EnumChatFormatting.RESET))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal("GTPP.multiblock.efficiency") + ": "
                            + EnumChatFormatting.YELLOW
                            + (mEfficiency / 100.0F)
                            + EnumChatFormatting.RESET
                            + " %")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal("GTPP.multiblock.pollution") + ": "
                            + EnumChatFormatting.RED
                            + (getPollutionPerTick(null) * 20)
                            + EnumChatFormatting.RESET
                            + "/sec")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocal("GTPP.multiblock.pollutionreduced") + ": "
                            + EnumChatFormatting.GREEN
                            + getPollutionReductionForAllMufflers()
                            + EnumChatFormatting.RESET
                            + " %")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                new TextWidget("Total Time Since Built: ").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> "" + EnumChatFormatting.DARK_GREEN
                            + getRuntimeWeeksDisplay()
                            + EnumChatFormatting.RESET
                            + " Weeks,")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> "" + EnumChatFormatting.DARK_GREEN
                            + getRuntimeDaysDisplay()
                            + EnumChatFormatting.RESET
                            + " Days,")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> "" + EnumChatFormatting.DARK_GREEN
                            + getRuntimeHoursDisplay()
                            + EnumChatFormatting.RESET
                            + " Hours,")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> "" + EnumChatFormatting.DARK_GREEN
                            + getRuntimeMinutesDisplay()
                            + EnumChatFormatting.RESET
                            + " Minutes,")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> "" + EnumChatFormatting.DARK_GREEN
                            + getRuntimeSecondsDisplay()
                            + EnumChatFormatting.RESET
                            + " Seconds")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> mMachine));
    }

    protected int getLastRecipeEU() {
        return mLastRecipe != null ? mLastRecipe.mEUt : 0;
    }

    protected int getLastRecipeDuration() {
        return mLastRecipe != null ? mLastRecipe.mDuration : 0;
    }

    protected long getRuntimeSeconds() {
        return getTotalRuntimeInTicks() / 20;
    }

    protected long getRuntimeWeeksDisplay() {
        return TimeUnit.SECONDS.toDays(getRuntimeSeconds()) / 7;
    }

    protected long getRuntimeDaysDisplay() {
        return TimeUnit.SECONDS.toDays(getRuntimeSeconds()) - 7 * getRuntimeWeeksDisplay();
    }

    protected long getRuntimeHoursDisplay() {
        return TimeUnit.SECONDS.toHours(getRuntimeSeconds()) - TimeUnit.DAYS.toHours(getRuntimeDaysDisplay())
            - TimeUnit.DAYS.toHours(7 * getRuntimeWeeksDisplay());
    }

    protected long getRuntimeMinutesDisplay() {
        return TimeUnit.SECONDS.toMinutes(getRuntimeSeconds()) - (TimeUnit.SECONDS.toHours(getRuntimeSeconds()) * 60);
    }

    protected long getRuntimeSecondsDisplay() {
        return TimeUnit.SECONDS.toSeconds(getRuntimeSeconds()) - (TimeUnit.SECONDS.toMinutes(getRuntimeSeconds()) * 60);
    }

    protected void setupToolDisplay() {
        if (!mToolStacks.isEmpty()) return;

        mToolStacks.put(
            true + "WRENCH",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.WRENCH, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
            true + "CROWBAR",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.CROWBAR, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
            true + "HARDHAMMER",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
            true + "SOFTHAMMER",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.SOFTMALLET, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
            true + "SCREWDRIVER",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
            true + "SOLDERING_IRON_LV",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.SOLDERING_IRON_LV, 1, GOOD, Materials.Tungsten, null));

        mToolStacks.put(
            false + "WRENCH",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.WRENCH, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
            false + "CROWBAR",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.CROWBAR, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
            false + "HARDHAMMER",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
            false + "SOFTHAMMER",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.SOFTMALLET, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
            false + "SCREWDRIVER",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
            false + "SOLDERING_IRON_LV",
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.SOLDERING_IRON_LV, 1, BAD, Materials.Tungsten, null));

        ItemStack aGlassPane1 = ItemUtils.getItemStackOfAmountFromOreDict("paneGlassRed", 1);
        ItemStack aGlassPane2 = ItemUtils.getItemStackOfAmountFromOreDict("paneGlassLime", 1);
        mToolStacks.put("falseGLASS", aGlassPane1);
        mToolStacks.put("trueGLASS", aGlassPane2);
    }

    public enum GTPPHatchElement implements IHatchElement<GregtechMeta_MultiBlockBase<?>> {

        AirIntake(GregtechMeta_MultiBlockBase::addAirIntakeToMachineList, GT_MetaTileEntity_Hatch_AirIntake.class) {

            @Override
            public long count(GregtechMeta_MultiBlockBase<?> t) {
                return t.mAirIntakes.size();
            }
        },
        TTDynamo(GregtechMeta_MultiBlockBase::addMultiAmpDynamoToMachineList,
            "com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti") {

            @Override
            public long count(GregtechMeta_MultiBlockBase<?> t) {
                return t.mTecTechDynamoHatches.size();
            }
        },
        TTEnergy(GregtechMeta_MultiBlockBase::addMultiAmpEnergyToMachineList,
            "com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti") {

            @Override
            public long count(GregtechMeta_MultiBlockBase<?> t) {
                return t.mTecTechEnergyHatches.size();
            }
        },;

        @SuppressWarnings("unchecked")
        private static <T> Class<T> retype(Class<?> clazz) {
            return (Class<T>) clazz;
        }

        private final List<? extends Class<? extends IMetaTileEntity>> mMteClasses;
        private final IGT_HatchAdder<? super GregtechMeta_MultiBlockBase<?>> mAdder;

        @SafeVarargs
        GTPPHatchElement(IGT_HatchAdder<? super GregtechMeta_MultiBlockBase<?>> aAdder,
            Class<? extends IMetaTileEntity>... aMteClasses) {
            this.mMteClasses = Arrays.asList(aMteClasses);
            this.mAdder = aAdder;
        }

        GTPPHatchElement(IGT_HatchAdder<? super GregtechMeta_MultiBlockBase<?>> aAdder, String... aClassNames) {
            this.mMteClasses = Arrays.stream(aClassNames)
                .map(ReflectionUtils::getClass)
                .filter(Objects::nonNull)
                .<Class<? extends IMetaTileEntity>>map(GTPPHatchElement::retype)
                .collect(Collectors.toList());
            this.mAdder = aAdder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mMteClasses;
        }

        @Override
        public IGT_HatchAdder<? super GregtechMeta_MultiBlockBase<?>> adder() {
            return mAdder;
        }
    }
}
