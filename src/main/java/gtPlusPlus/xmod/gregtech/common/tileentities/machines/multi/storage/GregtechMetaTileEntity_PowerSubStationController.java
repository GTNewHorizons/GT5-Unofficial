package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onlyIf;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Dynamo;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase.GTPPHatchElement.TTDynamo;
import static gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase.GTPPHatchElement.TTEnergy;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ChannelDataAccessor;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.forge.PlayerMainInvWrapper;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.MovingAverageLong;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

public class GregtechMetaTileEntity_PowerSubStationController extends
    GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_PowerSubStationController> implements ISurvivalConstructable {

    private static enum TopState {
        MayBeTop,
        Top,
        NotTop
    }

    protected long mAverageEuUsage = 0;
    protected final MovingAverageLong mAverageEuAdded = new MovingAverageLong(20);
    protected final MovingAverageLong mAverageEuConsumed = new MovingAverageLong(20);
    protected long mTotalEnergyAdded = 0;
    protected long mTotalEnergyConsumed = 0;
    protected long mTotalEnergyLost = 0;
    protected boolean mIsOutputtingPower = false;
    protected long mBatteryCapacity = 0;

    private final int ENERGY_TAX = 5;

    private int mCasing;
    private int[] cellCount = new int[6];
    private TopState topState = TopState.MayBeTop;
    private static IStructureDefinition<GregtechMetaTileEntity_PowerSubStationController> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_PowerSubStationController(final int aID, final String aName,
        final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_PowerSubStationController(final String aName) {
        super(aName);
    }

    @Override
    public String getMachineType() {
        return "Energy Buffer";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Consumes " + this.ENERGY_TAX + "% of the average voltage of all energy type hatches")
            .addInfo("Does not require maintenance")
            .addInfo(
                "Can be built with variable height between " + (CELL_HEIGHT_MIN + 2) + "-" + (CELL_HEIGHT_MAX + 2) + "")
            .addInfo("Hatches can be placed nearly anywhere")
            .addInfo("HV Energy/Dynamo Hatches are the lowest tier you can use")
            .addInfo("Supports voltages >= UHV using MAX tier components.")
            .addSeparator()
            .addController("Bottom Center")
            .addCasingInfoMin("Sub-Station External Casings", 10, false)
            .addDynamoHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addSubChannelUsage("capacitor", "Vanadium Capacitor Cell Tier")
            .addSubChannelUsage("height", "Height of structure")
            .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(24)),
                new GT_RenderedTexture(
                    aActive ? Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE
                        : Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER) };
        }
        if (side == this.getBaseMetaTileEntity()
            .getBackFacing()) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(24)),
                mIsOutputtingPower ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[(int) this.getOutputTier()]
                    : Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[(int) this.getInputTier()] };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(23)) };
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        // if (mBatteryCapacity <= 0) return false;
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    private void checkMachineProblem(String msg, int xOff, int yOff, int zOff) {
        final IGregTechTileEntity te = this.getBaseMetaTileEntity();
        final Block tBlock = te.getBlockOffset(xOff, yOff, zOff);
        final byte tMeta = te.getMetaIDOffset(xOff, yOff, zOff);
        String name = tBlock.getLocalizedName();
        String problem = msg + ": (" + xOff + ", " + yOff + ", " + zOff + ") " + name + ":" + tMeta;
        checkMachineProblem(problem);
    }

    private void checkMachineProblem(String msg) {
        if (!AsmConfig.disableAllLogging) {
            Logger.INFO("Power Sub-Station problem: " + msg);
        }
    }

    public static int getCellTier(Block aBlock, int aMeta) {
        if (aBlock == ModBlocks.blockCasings2Misc && aMeta == 7) {
            return 4;
        } else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 4) {
            return 5;
        } else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 5) {
            return 6;
        } else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 6) {
            return 7;
        } else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 7) {
            return 8;
        } else if (aBlock == ModBlocks.blockCasings3Misc && aMeta == 8) {
            return 9;
        } else {
            return -1;
        }
    }

    public static int getMetaFromTier(int tier) {
        if (tier == 4) return 7;
        if (tier >= 5 && tier <= 9) return tier - 1;
        return 0;
    }

    public static Block getBlockFromTier(int tier) {
        return switch (tier) {
            case 4 -> ModBlocks.blockCasings2Misc;
            case 5, 6, 7, 8, 9 -> ModBlocks.blockCasings3Misc;
            default -> null;
        };
    }

    public static int getMaxHatchTier(int aCellTier) {
        switch (aCellTier) {
            case 9 -> {
                return GT_Values.VOLTAGE_NAMES[9].equals("Ultimate High Voltage") ? 15 : 9;
            }
            default -> {
                if (aCellTier < 4) {
                    return 0;
                } else {
                    return aCellTier;
                }
            }
        }
    }

    public static final int CELL_HEIGHT_MAX = 16;
    public static final int CELL_HEIGHT_MIN = 2;

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_PowerSubStationController> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_PowerSubStationController>builder()
                .addShape(
                    mName + "bottom",
                    transpose(new String[][] { { "BB~BB", "BBBBB", "BBBBB", "BBBBB", "BBBBB" } }))
                .addShape(
                    mName + "layer",
                    transpose(new String[][] { { "CCCCC", "CIIIC", "CIIIC", "CIIIC", "CCCCC" } }))
                .addShape(mName + "mid", transpose(new String[][] { { "CCCCC", "CHHHC", "CHHHC", "CHHHC", "CCCCC" } }))
                .addShape(mName + "top", transpose(new String[][] { { "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT" } }))
                .addElement(
                    'C',
                    buildHatchAdder(GregtechMetaTileEntity_PowerSubStationController.class)
                        .atLeast(Energy.or(TTEnergy), Dynamo.or(TTDynamo), Maintenance)
                        .disallowOnly(ForgeDirection.UP, ForgeDirection.DOWN)
                        .casingIndex(TAE.GTPP_INDEX(24))
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 8))))
                .addElement(
                    'B',
                    buildHatchAdder(GregtechMetaTileEntity_PowerSubStationController.class)
                        .atLeast(Energy.or(TTEnergy), Dynamo.or(TTDynamo), Maintenance)
                        .disallowOnly(ForgeDirection.UP)
                        .casingIndex(TAE.GTPP_INDEX(24))
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 8))))
                .addElement(
                    'T',
                    buildHatchAdder(GregtechMetaTileEntity_PowerSubStationController.class)
                        .atLeast(Energy.or(TTEnergy), Dynamo.or(TTDynamo), Maintenance)
                        .disallowOnly(ForgeDirection.DOWN)
                        .casingIndex(TAE.GTPP_INDEX(24))
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 8))))
                .addElement(
                    'I',
                    withChannel(
                        "cell",
                        ofChain(
                            onlyIf(
                                x -> x.topState != TopState.NotTop,
                                onElementPass(
                                    x -> x.topState = TopState.Top,
                                    ofHatchAdderOptional(
                                        GregtechMetaTileEntity_PowerSubStationController::addPowerSubStationList,
                                        TAE.GTPP_INDEX(24),
                                        1,
                                        ModBlocks.blockCasings2Misc,
                                        8))),
                            onlyIf(
                                x -> x.topState != TopState.Top,
                                onElementPass(
                                    x -> x.topState = TopState.NotTop,
                                    ofChain(
                                        onElementPass(x -> ++x.cellCount[0], ofCell(4)),
                                        onElementPass(x -> ++x.cellCount[1], ofCell(5)),
                                        onElementPass(x -> ++x.cellCount[2], ofCell(6)),
                                        onElementPass(x -> ++x.cellCount[3], ofCell(7)),
                                        onElementPass(x -> ++x.cellCount[4], ofCell(8)),
                                        onElementPass(x -> ++x.cellCount[5], ofCell(9))))))))
                .addElement('H', ofCell(4))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    public static <T> IStructureElement<T> ofCell(int aIndex) {
        return new IStructureElement<T>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                int meta = world.getBlockMetadata(x, y, z);
                int tier = getCellTier(block, meta);
                return aIndex == tier;
            }

            public int getIndex(int size) {
                if (size > 6) size = 6;
                return size + 3;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(
                    world,
                    x,
                    y,
                    z,
                    getBlockFromTier(getIndex(trigger.stackSize)),
                    getMetaFromTier(getIndex(trigger.stackSize)));
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(
                    x,
                    y,
                    z,
                    getBlockFromTier(getIndex(trigger.stackSize)),
                    getMetaFromTier(getIndex(trigger.stackSize)),
                    3);
            }

            @Nullable
            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(getBlockFromTier(trigger.stackSize), getMetaFromTier(trigger.stackSize));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                Block block = world.getBlock(x, y, z);
                int meta = world.getBlockMetadata(x, y, z);
                int tier = getCellTier(block, meta);
                if (tier >= 0) return PlaceResult.SKIP;
                return StructureUtility.survivalPlaceBlock(
                    getBlockFromTier(getIndex(trigger.stackSize)),
                    getMetaFromTier(getIndex(trigger.stackSize)),
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int layer = Math.min(stackSize.stackSize + 3, 18);
        log("Layer: " + layer);
        log("Building 0");
        buildPiece(mName + "bottom", stackSize, hintsOnly, 2, 0, 0);
        log("Built 0");
        for (int i = 1; i < layer - 1; i++) {
            log("Building " + i);
            buildPiece(mName + "mid", stackSize, hintsOnly, 2, i, 0);
            log("Built " + i);
        }
        log("Building " + (layer - 1));
        buildPiece(mName + "top", stackSize, hintsOnly, 2, layer - 1, 0);
        log("Built " + (layer - 1));
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int layer = Math.min(ChannelDataAccessor.getChannelData(stackSize, "height") + 3, 18);
        int built;
        built = survivialBuildPiece(mName + "bottom", stackSize, 2, 0, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        for (int i = 1; i < layer - 1; i++) {
            built = survivialBuildPiece(mName + "mid", stackSize, 2, i, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        return survivialBuildPiece(mName + "top", stackSize, 2, layer - 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mEnergyHatches.clear();
        mDynamoHatches.clear();
        mTecTechEnergyHatches.clear();
        mTecTechDynamoHatches.clear();
        mAllEnergyHatches.clear();
        mAllDynamoHatches.clear();
        for (int i = 0; i < 6; i++) {
            cellCount[i] = 0;
        }
        log("Checking 0");
        if (!checkPiece(mName + "bottom", 2, 0, 0)) {
            log("Failed on Layer 0");
            return false;
        }
        log("Pass 0");
        int layer = 1;
        topState = TopState.MayBeTop;
        while (true) {
            if (!checkPiece(mName + "layer", 2, layer, 0)) return false;
            layer++;
            if (topState == TopState.Top) break; // top found, break out
            topState = TopState.MayBeTop;
            if (layer > 18) return false; // too many layers
        }
        int level = 0;
        for (int i = 0; i < 6; i++) {
            if (cellCount[i] != 0) {
                if (level == 0) {
                    level = i + 4;
                } else {
                    return false;
                }
            }
        }
        int tier = getMaxHatchTier(level);
        long volSum = 0;
        for (GT_MetaTileEntity_Hatch hatch : mAllDynamoHatches) {
            if (hatch.mTier > tier || hatch.mTier < 3) {
                return false;
            }
            volSum += (8L << (hatch.mTier * 2));
        }
        for (GT_MetaTileEntity_Hatch hatch : mAllEnergyHatches) {
            if (hatch.mTier > tier || hatch.mTier < 3) {
                return false;
            }
            volSum += (8L << (hatch.mTier * 2));
        }
        mBatteryCapacity = getCapacityFromCellTier(level) * cellCount[level - 4];
        if (mAllEnergyHatches.size() + mAllDynamoHatches.size() > 0) {
            mAverageEuUsage = volSum / (mAllEnergyHatches.size() + mAllDynamoHatches.size());
        } else mAverageEuUsage = 0;
        return true;
    }

    public final boolean addPowerSubStationList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else {
                if (isThisHatchMultiDynamo(aMetaTileEntity)) {
                    return addToMachineList(aTileEntity, aBaseCasingIndex);
                } else if (isThisHatchMultiEnergy(aMetaTileEntity)) {
                    return addToMachineList(aTileEntity, aBaseCasingIndex);
                }
            }
        }
        return false;
    }

    // Define storage capacity of smallest cell tier (EV) and compute higher tiers from it
    private static final long CELL_TIER_EV_CAPACITY = 100 * 1000 * 1000;
    private static final long CELL_TIER_MULTIPLIER = 4; // each tier's capacity is this many times the previous tier

    public static long getCapacityFromCellTier(int aOverallCellTier) {
        // Use integer math instead of `Math.pow` to avoid range/precision errors
        if (aOverallCellTier < 4) return 0;
        aOverallCellTier -= 4;
        long capacity = CELL_TIER_EV_CAPACITY;
        while (aOverallCellTier > 0) {
            capacity *= CELL_TIER_MULTIPLIER;
            aOverallCellTier--;
        }
        return capacity;
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_PowerSubStationController(this.mName);
    }

    // mTotalEnergyAdded
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setLong("mAverageEuUsage", this.mAverageEuUsage);
        this.mAverageEuAdded.write(aNBT, "mAverageEuAdded");
        this.mAverageEuConsumed.write(aNBT, "mAverageEuConsumed");

        // Usage Stats
        aNBT.setLong("mTotalEnergyAdded", this.mTotalEnergyAdded);
        aNBT.setLong("mTotalEnergyLost", this.mTotalEnergyLost);
        aNBT.setLong("mTotalEnergyConsumed", this.mTotalEnergyConsumed);
        aNBT.setLong("mTotalRunTime", this.mTotalRunTime);
        aNBT.setBoolean("mIsOutputtingPower", this.mIsOutputtingPower);
        aNBT.setLong("mBatteryCapacity", this.mBatteryCapacity);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {

        // Best not to get a long if the Tag Map is holding an int
        if (aNBT.hasKey("mAverageEuUsage")) {
            this.mAverageEuUsage = aNBT.getLong("mAverageEuUsage");
        }
        switch (aNBT.func_150299_b("mAverageEuAdded")) {
            case NBT.TAG_BYTE_ARRAY -> this.mAverageEuAdded.read(aNBT, "mAverageEuAdded");
            case NBT.TAG_LONG -> this.mAverageEuAdded.set(aNBT.getLong("mAverageEuAdded"));
        }
        switch (aNBT.func_150299_b("mAverageEuConsumed")) {
            case NBT.TAG_BYTE_ARRAY -> this.mAverageEuConsumed.read(aNBT, "mAverageEuConsumed");
            case NBT.TAG_LONG -> this.mAverageEuConsumed.set(aNBT.getLong("mAverageEuConsumed"));
        }

        // Usage Stats
        this.mTotalEnergyAdded = aNBT.getLong("mTotalEnergyAdded");
        this.mTotalEnergyLost = aNBT.getLong("mTotalEnergyLost");
        this.mTotalEnergyConsumed = aNBT.getLong("mTotalEnergyConsumed");
        this.mTotalRunTime = aNBT.getLong("mTotalRunTime");

        this.mIsOutputtingPower = aNBT.getBoolean("mIsOutputtingPower");

        this.mBatteryCapacity = aNBT.getLong("mBatteryCapacity");

        super.loadNBTData(aNBT);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        this.mProgresstime = 0;
        this.mMaxProgresstime = 200;
        this.lEUt = 0;
        this.mEfficiencyIncrease = 10000;
        return SimpleCheckRecipeResult.ofSuccess("managing_power");
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    private long drawEnergyFromHatch(MetaTileEntity aHatch) {
        long stored = aHatch.getEUVar();
        long voltage = aHatch.maxEUInput() * aHatch.maxAmperesIn();

        if (voltage > stored || (voltage + this.getEUVar() > this.mBatteryCapacity)) {
            return 0;
        }

        if (this.getBaseMetaTileEntity()
            .increaseStoredEnergyUnits(voltage, false)) {
            aHatch.setEUVar((stored - voltage));
            this.mTotalEnergyAdded += voltage;
            return voltage;
        }
        return 0;
    }

    private long addEnergyToHatch(MetaTileEntity aHatch) {
        long voltage = aHatch.maxEUOutput() * aHatch.maxAmperesOut();

        if (aHatch.getEUVar() > aHatch.maxEUStore() - voltage) {
            return 0;
        }

        if (this.getBaseMetaTileEntity()
            .decreaseStoredEnergyUnits(voltage, false)) {
            aHatch.getBaseMetaTileEntity()
                .increaseStoredEnergyUnits(voltage, false);
            this.mTotalEnergyConsumed += voltage;
            return voltage;
        }
        return 0;
    }

    private long computeEnergyTax() {
        float mTax = mAverageEuUsage * (ENERGY_TAX / 100f);

        // Increase tax up to 2x if machine is not fully repaired (does not actually work at the moment, mEfficiency is
        // always 0)
        // mTax = mTax * (1f + (10000f - mEfficiency) / 10000f);

        return MathUtils.roundToClosestLong(mTax);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        // First, decay overcharge (1% of stored energy plus 1000 EU per tick)
        if (this.getEUVar() > this.mBatteryCapacity) {
            long energy = (long) (this.getEUVar() * 0.990f) - 1000;
            this.setEUVar(energy);
        }

        // Pay Tax
        long mDecrease = computeEnergyTax();
        this.mTotalEnergyLost += Math.min(mDecrease, this.getEUVar());
        this.setEUVar(Math.max(0, this.getEUVar() - mDecrease));

        long aInputAverage = 0;
        long aOutputAverage = 0;
        // Input Power
        for (GT_MetaTileEntity_Hatch THatch : filterValidMTEs(this.mDischargeHatches)) {
            aInputAverage += drawEnergyFromHatch(THatch);
        }
        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(this.mAllEnergyHatches)) {
            aInputAverage += drawEnergyFromHatch(tHatch);
        }

        // Output Power
        for (GT_MetaTileEntity_Hatch THatch : filterValidMTEs(this.mChargeHatches)) {
            aOutputAverage += addEnergyToHatch(THatch);
        }
        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(this.mAllDynamoHatches)) {
            aOutputAverage += addEnergyToHatch(tHatch);
        }
        // reset progress time
        mProgresstime = 0;

        this.mAverageEuAdded.sample(aInputAverage);
        this.mAverageEuConsumed.sample(aOutputAverage);

        return true;
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        // Not applicable to this machine
        return true;
    }

    @Override
    public boolean addEnergyOutput(long aEU) {
        // Not applicable to this machine
        return true;
    }

    @Override
    public long maxEUStore() {
        return mBatteryCapacity;
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public String[] getExtraInfoData() {
        String mode;
        if (mIsOutputtingPower) {
            mode = EnumChatFormatting.GOLD + "Output" + EnumChatFormatting.RESET;
        } else {
            mode = EnumChatFormatting.BLUE + "Input" + EnumChatFormatting.RESET;
        }

        String storedEnergyText;
        if (this.getEUVar() > this.mBatteryCapacity) {
            storedEnergyText = EnumChatFormatting.RED + GT_Utility.formatNumbers(this.getEUVar())
                + EnumChatFormatting.RESET;
        } else {
            storedEnergyText = EnumChatFormatting.GREEN + GT_Utility.formatNumbers(this.getEUVar())
                + EnumChatFormatting.RESET;
        }

        int errorCode = this.getBaseMetaTileEntity()
            .getErrorDisplayID();
        boolean mMaint = (errorCode != 0);

        return new String[] { "Ergon Energy - District Sub-Station", "Stored EU: " + storedEnergyText,
            "Capacity: " + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(this.maxEUStore())
                + EnumChatFormatting.RESET,
            "Running Costs: " + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(this.computeEnergyTax())
                + EnumChatFormatting.RESET
                + " EU/t",
            "Controller Mode: " + mode,
            "Requires Maintenance: " + (!mMaint ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                + mMaint
                + EnumChatFormatting.RESET
                + " | Code: ["
                + (!mMaint ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                + errorCode
                + EnumChatFormatting.RESET
                + "]",
            "----------------------", "Stats for Nerds",
            "Average Input: " + EnumChatFormatting.BLUE
                + GT_Utility.formatNumbers(this.getAverageEuAdded())
                + EnumChatFormatting.RESET
                + " EU",
            "Average Output: " + EnumChatFormatting.GOLD
                + GT_Utility.formatNumbers(this.getAverageEuConsumed())
                + EnumChatFormatting.RESET
                + " EU",
            "Total Input: " + EnumChatFormatting.BLUE
                + GT_Utility.formatNumbers(this.mTotalEnergyAdded)
                + EnumChatFormatting.RESET
                + " EU",
            "Total Output: " + EnumChatFormatting.GOLD
                + GT_Utility.formatNumbers(this.mTotalEnergyConsumed)
                + EnumChatFormatting.RESET
                + " EU",
            "Total Costs: " + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(this.mTotalEnergyLost)
                + EnumChatFormatting.RESET
                + " EU", };
    }

    @Override
    public void explodeMultiblock() {
        // TODO Auto-generated method stub
        super.explodeMultiblock();
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        // TODO Auto-generated method stub
        super.doExplosion(aExplosionPower);
    }

    @Override
    public long getMaxInputVoltage() {
        return 32768;
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return !mIsOutputtingPower;
    }

    @Override
    public boolean isEnetOutput() {
        return mIsOutputtingPower;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return (side == this.getBaseMetaTileEntity()
            .getBackFacing() && !mIsOutputtingPower);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return (side == this.getBaseMetaTileEntity()
            .getBackFacing() && mIsOutputtingPower);
    }

    @Override
    public long maxAmperesIn() {
        return 32;
    }

    @Override
    public long maxAmperesOut() {
        return 32;
    }

    @Override
    public long maxEUInput() {
        return 32768;
    }

    @Override
    public long maxEUOutput() {
        return 32768;
    }

    public final long getAverageEuAdded() {
        return this.mAverageEuAdded.get();
    }

    public final long getAverageEuConsumed() {
        return this.mAverageEuConsumed.get();
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mIsOutputtingPower = !mIsOutputtingPower;
        if (mIsOutputtingPower) {
            PlayerUtils.messagePlayer(aPlayer, "Sub-Station is now outputting power from the controller.");
        } else {
            PlayerUtils.messagePlayer(aPlayer, "Sub-Station is now inputting power into the controller.");
        }
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    @Override
    public int getGUIWidth() {
        return 196;
    }

    @Override
    public int getGUIHeight() {
        return 191;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                .setSize(17, 17)
                .setPos(175, 166));
    }

    private long clientEUIn, clientEUOut, clientEULoss, clientEUStored;
    private float clientProgress;

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(4, 4)
                .setSize(149, 149))
            .widget(new SlotWidget(inventoryHandler, 0).setPos(154, 4))
            .widget(
                new SlotWidget(inventoryHandler, 1).setAccess(true, false)
                    .setPos(154, 22))
            .widget(
                SlotGroup.ofItemHandler(new PlayerMainInvWrapper(buildContext.getPlayer().inventory), 9)
                    .endAtSlot(8)
                    .build()
                    .setPos(7, 166))
            .widget(
                TextWidget
                    .dynamicString(
                        () -> getBaseMetaTileEntity().getErrorDisplayID() == 0
                            ? getBaseMetaTileEntity().isActive() ? "Running perfectly" : "Turn on with Mallet"
                            : "")
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 8))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> getBaseMetaTileEntity().isActive(),
                    val -> getBaseMetaTileEntity().setActive(val)))
            .widget(
                new FakeSyncWidget.IntegerSyncer(
                    () -> getBaseMetaTileEntity().getErrorDisplayID(),
                    val -> getBaseMetaTileEntity().setErrorDisplayID(val)))
            .widget(
                new TextWidget("In").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(178, 10))
            .widget(
                new TextWidget("Out").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(176, 28))
            .widget(new FakeSyncWidget.LongSyncer(this::getAverageEuAdded, val -> clientEUIn = val))
            .widget(
                new TextWidget().setStringSupplier(() -> "Avg In: " + numberFormat.format(clientEUIn) + " EU")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(new FakeSyncWidget.LongSyncer(this::getAverageEuConsumed, val -> clientEUOut = val))
            .widget(
                new TextWidget().setStringSupplier(() -> "Avg Out: " + numberFormat.format(clientEUOut) + " EU")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30))
            .widget(new FakeSyncWidget.LongSyncer(this::computeEnergyTax, val -> clientEULoss = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(() -> "Powerloss: " + numberFormat.format(clientEULoss) + " EU per tick")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 40))
            .widget(
                new DrawableWidget().setDrawable(GTPP_UITextures.PICTURE_ENERGY_FRAME)
                    .setPos(4, 155)
                    .setSize(149, 7))
            .widget(new FakeSyncWidget.FloatSyncer(this::getProgress, val -> clientProgress = val))
            .widget(
                new ProgressBar().setProgress(this::getProgress)
                    .setTexture(GTPP_UITextures.PROGRESSBAR_PSS_ENERGY, 147)
                    .setDirection(ProgressBar.Direction.RIGHT)
                    .setPos(5, 156)
                    .setSize(147, 5))
            .widget(
                new TextWidget("Stored:").setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 132))
            .widget(
                new FakeSyncWidget.LongSyncer(() -> getBaseMetaTileEntity().getStoredEU(), val -> clientEUStored = val))
            .widget(new TextWidget().setTextSupplier(() -> {
                int colorScale = (int) (clientProgress * 100 * 2.55);
                return new Text(numberFormat.format(clientEUStored) + " EU")
                    .color(Utils.rgbtoHexValue((255 - colorScale), colorScale, 0));
            })
                .setPos(10, 142))
            .widget(
                new TextWidget().setStringSupplier(() -> numberFormat.format(clientProgress * 100) + "%")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(70, 155));
    }

    private float getProgress() {
        return (float) getBaseMetaTileEntity().getStoredEU() / getBaseMetaTileEntity().getEUCapacity();
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
