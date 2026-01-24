package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.getTier;
import static gregtech.api.util.GTUtility.validMTEList;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTScannerResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchObjectHolder;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

/**
 * Created by danie_000 on 17.12.2016.
 * Updated by C0bra5 on 11.01.2026.
 */
public class MTEResearchStation extends TTMultiblockBase implements ISurvivalConstructable {

    // region variables
    public static final int MODE_RESEARCH_STATION = 0;
    public static final int MODE_SCANNER = 1;

    public static final int PACKET_LOSS_GRACE_WINDOW = 20;
    public static final int PACKET_LOSS_DECAY_WINDOW = 80;
    public static final int PACKET_LOSS_FULL_WINDOW = PACKET_LOSS_GRACE_WINDOW + PACKET_LOSS_DECAY_WINDOW;

    private final ArrayList<MTEHatchObjectHolder> eHolders = new ArrayList<>();
    private ItemStack holderStackToConsume = null;
    private ItemStack researchOutputForGUI = null;
    private long computationRemaining = 0, computationRequired = 0;
    private int ticksUntilPacketLossFail = PACKET_LOSS_FULL_WINDOW;
    private long packetLossDecayFrom = 0;

    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.research.hint.0"), // 1 - Classic/Data Hatches or
        // Computer casing
        translateToLocal("gt.blockmachines.multimachine.em.research.hint.1"), // 2 - Holder Hatch
        translateToLocal("gt.blockmachines.multimachine.em.research.hint.2"), // 3 - Output Bus, Input Hatch or Advanced
        // Computer Casing
    };
    // endregion

    // region structure
    private static final IStructureDefinition<MTEResearchStation> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEResearchStation>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "   ", " A ", " A ", "AAA", "AAA", "AAA", "AAA" },
                    { "AAA", "ACA", "ACA", "ACA", "BCB", "BCB", "BBB" },
                    { "   ", " C ", "   ", "   ", "AFA", "CCC", "DDD" },
                    { "   ", " E ", "   ", "   ", "A~A", "CCC", "DDD" },
                    { "   ", " C ", "   ", "   ", "AFA", "CCC", "DDD" },
                    { "AAA", "ACA", "ACA", "ACA", "BCB", "BCB", "BBB" },
                    { "   ", " A ", " A ", "AAA", "AAA", "AAA", "AAA" } }))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsTT, 1))
        .addElement('B', ofBlock(TTCasingsContainer.sBlockCasingsTT, 2))
        .addElement('C', ofBlock(TTCasingsContainer.sBlockCasingsTT, 3))
        .addElement(
            'D',
            buildHatchAdder(MTEResearchStation.class)
                .atLeast(
                    Energy.or(HatchElement.EnergyMulti),
                    Maintenance,
                    HatchElement.InputData,
                    OutputBus,
                    InputHatch)
                .casingIndex(BlockGTCasingsTT.textureOffset + 1)
                .hint(1)
                .buildAndChain(ofBlock(TTCasingsContainer.sBlockCasingsTT, 1)))
        .addElement('E', HolderHatchElement.INSTANCE.newAny(BlockGTCasingsTT.textureOffset + 3, 2))
        .addElement(
            'F',
            buildHatchAdder(MTEResearchStation.class).anyOf(OutputBus, InputHatch, Maintenance)
                .casingIndex(BlockGTCasingsTT.textureOffset + 1)
                .hint(3)
                .buildAndChain(ofBlock(TTCasingsContainer.sBlockCasingsTT, 3)))

        .build();

    public final boolean addHolderToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchObjectHolder) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eHolders.add((MTEHatchObjectHolder) aMetaTileEntity);
        }
        return false;
    }

    private enum HolderHatchElement implements IHatchElement<MTEResearchStation> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchObjectHolder.class);
        }

        @Override
        public IGTHatchAdder<? super MTEResearchStation> adder() {
            return MTEResearchStation::addHolderToMachineList;
        }

        @Override
        public long count(MTEResearchStation t) {
            return t.eHolders.size();
        }
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        unlockHolders();
        eHolders.clear();

        if (!structureCheck_EM("main", 1, 3, 4)) {
            return false;
        }

        if (iGregTechTileEntity.isActive()) {
            lockHolders();
        } else {
            unlockHolders();
        }

        return eHolders.size() == 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 1, 3, 4, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece("main", stackSize, 1, 3, 4, elementBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<MTEResearchStation> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
    // endregion structure

    // region ctor and definitions

    public MTEResearchStation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEResearchStation(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEResearchStation(mName);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // Machine Type: Research Station, Scanner
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.research.type"))
            // Used to scan Data Sticks for Assembling Line Recipes
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.1"))
            // Needs to be fed with computation to work
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.2"))
            // Does not consume the item until the Data Stick is written
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.3"))
            // Use screwdriver to change mode
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.4"))
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.5"))
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.6"))
            .addTecTechHatchInfo()
            .beginStructureBlock(3, 7, 7, false)
            // Object Holder: Center of the front pillar
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.holder.tier.09.name"),
                translateToLocal("tt.keyword.Structure.CenterPillar"),
                2)
            // Optical Connector: Any Computer Casing on the backside of the main body
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.DataConnector"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasingBackMain"),
                1)
            // Energy Hatch: Any Computer Casing on the backside of the main body
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingBackMain"), 1)
            // Maintenance Hatch: Any Computer Casing on the backside of the main body
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingBackMain"), 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
                                 int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][3],
                new TTRenderedExtendedFacingTexture(aActive ? TTMultiblockBase.ScreenON : TTMultiblockBase.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][3] };
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(scannerFakeRecipes, TecTechRecipeMaps.researchStationFakeRecipes);
    }

    @Override
    protected boolean supportsSlotAutomation(int aSlot) {
        return aSlot == getControllerSlotIndex();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
    // endregion ctor and definitions

    // region void protection
    @Override
    public boolean protectsExcessItem() {
        return !eSafeVoid;
    }
    // endregion

    // region machine mode
    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public int nextMachineMode() {
        if (this.machineMode == MODE_RESEARCH_STATION) return MODE_SCANNER;
        return MODE_RESEARCH_STATION;
    }

    @Override
    public void setMachineMode(int aIndex) {
        switch (aIndex) {
            case MODE_RESEARCH_STATION, MODE_SCANNER -> this.machineMode = aIndex;
            default -> this.machineMode = MODE_RESEARCH_STATION;
        }
    }

    @Override
    public String getMachineModeName() {
        return getMachineModeName(this.machineMode);
    }

    private static String getMachineModeName(int mode) {
        if (mode == MODE_RESEARCH_STATION)
            return StatCollector.translateToLocal("gt.blockmachines.multimachine.em.research.mode.Assembly_line");
        return StatCollector.translateToLocal("gt.blockmachines.multimachine.em.research.mode.Scanner");
    }

    @Override
    public int getMachineMode() {
        return super.getMachineMode();
    }

    // endregion machine mode

    // region holder utils
    private void unlockHolders() {
        for (MTEHatchObjectHolder holder : this.eHolders) {
            IGregTechTileEntity mte = holder.getBaseMetaTileEntity();
            if (mte != null) {
                mte.setActive(false);
            }
        }
    }

    private void lockHolders() {
        for (MTEHatchObjectHolder holder : this.eHolders) {
            IGregTechTileEntity mte = holder.getBaseMetaTileEntity();
            if (mte != null) {
                mte.setActive(true);
            }
        }
    }

    private void exploderHolders() {
        for (MetaTileEntity holder : eHolders) {
            IGregTechTileEntity mte = holder.getBaseMetaTileEntity();
            if (mte != null) {
                mte.doExplosion(V[9]);
            }
        }
    }

    private @Nullable ItemStack getHolderStack() {
        if (this.eHolders.isEmpty() || this.eHolders.get(0).mInventory[0] == null) return null;
        ItemStack stack = this.eHolders.get(0).mInventory[0];
        if (GTUtility.isStackInvalid(stack) || stack.stackSize <= 0) return null;
        return stack;
    }
    // endregion holder utils

    // region input fetching
    private @Nullable ItemStack getResearchSpecialStack() {
        ItemStack stack = this.getControllerSlot();
        if (GTUtility.isStackInvalid(stack) || stack.stackSize <= 0) return null;
        return stack;
    }

    private @Nullable FluidStack getResearchFluid() {
        return this.getStoredFluids()
            .stream()
            .filter(f -> f.getFluid() != null && f.amount > 0)
            .findFirst()
            .orElse(null);
    }
    // endregion input fetching

    // region event handlers
    @Override
    public void onRemoval() {
        super.onRemoval();
        unlockHolders();
    }

    @Override
    protected void extraExplosions_EM() {
        this.exploderHolders();
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        this.resetProgress();
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (eHolders == null || eHolders.isEmpty() || eHolders.get(0).mInventory[0] == null)
            stopMachine(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE);
        if (computationRemaining <= 0) {
            computationRemaining = 0;
            mProgresstime = mMaxProgresstime;
            return true;
        } else {
            // packet loss mitigation
            if (eAvailableData >= eRequiredData) {
                this.packetLossDecayFrom = this.computationRemaining;
                this.ticksUntilPacketLossFail = PACKET_LOSS_FULL_WINDOW;
            }
            computationRemaining -= eAvailableData;
            mProgresstime = 1;
            return super.onRunningTick(aStack);
        }
    }

    @Override
    protected boolean checkComputationTimeout() {
        // this is only to mitigate packet loss, not the event of data being below expected.
        // this might be adjusted if the issue on large serves causes sub-rate packets to be sent.
        if (this.eAvailableData != 0) {
            return super.checkComputationTimeout();
        }
        --this.ticksUntilPacketLossFail;
        // if the counter reaches zero, fail with a full comp loss.
        if (this.ticksUntilPacketLossFail <= 0) {
            this.ticksUntilPacketLossFail = 0;
            // fail completely
            stopMachine(SimpleShutDownReason.ofCritical("computation_loss"));
            return false;
        }
        // if we are within the grace window, just consume power as usual.
        if (this.ticksUntilPacketLossFail >= PACKET_LOSS_DECAY_WINDOW) {
            return true;
        }
        // else loose 1/DECAY_WINDOW_PROGRESS of progress every tick.
        long diff = this.computationRequired - this.packetLossDecayFrom;
        double mult = 1.0d - (double) this.ticksUntilPacketLossFail / PACKET_LOSS_DECAY_WINDOW;
        this.computationRemaining = this.packetLossDecayFrom + (long) (diff * mult);
        return true;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
                                        ItemStack aTool) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }
    // endregion event handlers

    // region recipe check
    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        resetProgress();
        CheckRecipeResult result;
        ItemStack holderStack = getHolderStack();
        if (GTUtility.isStackInvalid(holderStack) || holderStack.stackSize <= 0) {
            return SimpleCheckRecipeResult.ofFailure("no_research_item");
        }
        // null controller stack if invalid
        ItemStack specialStack = this.getResearchSpecialStack();
        result = switch (this.machineMode) {
            case MODE_SCANNER -> findSBScannerRecipe(holderStack, specialStack);
            case MODE_RESEARCH_STATION -> findResearchStationRecipe(holderStack, specialStack);
            default -> CheckRecipeResultRegistry.INTERNAL_ERROR;
        };
        // fail-safes
        if (!result.wasSuccessful()) {
            this.resetProgress();
        } else {
            this.mMaxProgresstime = 20;
            this.mEfficiencyIncrease = 10000;
            this.lockHolders();
        }
        return result;
    }

    private void resetProgress() {
        this.eComputationTimeout = MAX_COMPUTATION_TIMEOUT;
        this.researchOutputForGUI = null;
        this.holderStackToConsume = null;
        this.mOutputItems = null;
        this.mMaxProgresstime = 0;
        this.mEfficiencyIncrease = 0;
        this.mEUt = 0;
        this.computationRequired = this.computationRemaining = this.packetLossDecayFrom = 0;
        this.ticksUntilPacketLossFail = PACKET_LOSS_FULL_WINDOW;
        this.unlockHolders();
    }

    private CheckRecipeResult findSBScannerRecipe(ItemStack aHolderStack, ItemStack aSpecialStack) {
        // only accepts one fluid so consider going though multiple times?
        // but doesn't seem too relevant since the only possible thing would be honey anyway.
        FluidStack fluid = getResearchFluid();
        // try to find a recipe
        GTScannerResult result = RecipeMaps.scannerHandlers.findRecipe(this, aHolderStack, aSpecialStack, fluid);
        // check if the result was found.
        if (result == null) return CheckRecipeResultRegistry.NO_RECIPE;
        // abort if req were not met internally.
        if (result.isNotMet()) return SimpleCheckRecipeResult.ofFailure("wrongRequirements");
        // check if we can output
        if (this.protectsExcessItem() && result.output != null
            && !this.canOutputAll(new ItemStack[] { result.output })) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }
        // calc computation/update req
        long computation;
        if (result instanceof GTScannerResult.ALScannerResult alResult) {
            this.researchOutputForGUI = alResult.alRecipe.mOutput == null ? null : alResult.alRecipe.mOutput.copy();
            computation = getComputationForScannerResult(
                alResult.alRecipe.mResearchTime,
                alResult.alRecipe.mResearchVoltage);
        } else {
            this.researchOutputForGUI = result.output == null ? null : result.output.copy();
            computation = getComputationForScannerResult(result.duration, result.eut);
        }
        if (fluid != null) fluid.amount -= result.fluidConsume;
        if (aSpecialStack != null) {
            aSpecialStack.stackSize -= result.specialConsume;
            if (aSpecialStack.stackSize <= 0) {
                this.mInventory[this.getControllerSlotIndex()] = null;
            }
        }
        // store params
        this.holderStackToConsume = GTUtility.copyAmount(result.inputConsume, aHolderStack);
        this.computationRemaining = this.computationRequired = this.packetLossDecayFrom = computation;
        this.mEUt = -getEUtForScannerResult(result.eut);
        this.mOutputItems = result.output == null ? null : new ItemStack[] { result.output };
        this.eRequiredData = 1;
        this.eAmpereFlow = 1;
        return SimpleCheckRecipeResult.ofSuccess("scanning");
    }

    // public static for nei/fake recipes
    public static long getComputationForScannerResult(long aResearchTime, long aResearchEUt) {
        return (long) (aResearchTime * GTUtility.powInt(2, getTier(aResearchEUt) - 1));
    }

    // public static for nei/fake recipes
    public static int getEUtForScannerResult(int eut) {
        // minimum of UV
        return Math.max(Math.abs(eut), (int) TierEU.RECIPE_UV);
    }

    private CheckRecipeResult findResearchStationRecipe(ItemStack aHolderStack, ItemStack aSpecialStack) {
        // controller slot must be a data stick.
        if (!ItemList.Tool_DataStick.isStackEqual(aSpecialStack, false, true))
            return CheckRecipeResultRegistry.NO_DATA_STICKS;
        for (TecTechRecipeMaps.TTResearchStationALRecipe assRecipe : TecTechRecipeMaps.researchableALRecipeList) {
            if (GTUtility.areStacksEqual(assRecipe.mResearchItem, aHolderStack, true)) {
                // generate datastick
                ItemStack output = ItemList.Tool_DataStick.get(1);
                output.setTagCompound(new NBTTagCompound());
                output.getTagCompound()
                    .setString(
                        "author",
                        EnumChatFormatting.BLUE + "Tec"
                            + EnumChatFormatting.DARK_BLUE
                            + "Tech"
                            + EnumChatFormatting.WHITE
                            + " Assembly Line Recipe Generator");
                AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(output, assRecipe);
                // check if we can output
                if (this.protectsExcessItem() && !this.canOutputAll(new ItemStack[] { output })) {
                    return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                }
                // consume
                aSpecialStack.stackSize -= 1;
                if (aSpecialStack.stackSize <= 0) {
                    this.mInventory[this.getControllerSlotIndex()] = null;
                }
                // set params
                this.researchOutputForGUI = assRecipe.mOutput == null ? null : assRecipe.mOutput.copy();
                this.holderStackToConsume = GTUtility.copyAmount(assRecipe.mResearchItem.stackSize, aHolderStack);
                this.computationRequired = this.computationRemaining = this.packetLossDecayFrom = assRecipe.mComputation
                    * 20L;
                this.mOutputItems = new ItemStack[] { output };
                // should probably fix what ever causes this at the root instead of here.
                this.mEUt = Math.min(assRecipe.mEUt, -assRecipe.mEUt);
                this.eRequiredData = assRecipe.mComputationRequiredPerSec;
                this.eAmpereFlow = assRecipe.mAmperage;
                return SimpleCheckRecipeResult.ofSuccess("researching");
            }
        }
        return CheckRecipeResultRegistry.NO_RECIPE;
    }
    // endregion recipe check

    // region outputting
    @Override
    public void outputAfterRecipe_EM() {
        unlockHolders();
        // abort early if tracker is null for some reason
        if (this.holderStackToConsume != null) {
            ItemStack holderStack = this.getHolderStack();
            if (GTUtility.areStacksEqual(this.holderStackToConsume, holderStack, false)
                && this.holderStackToConsume.stackSize <= holderStack.stackSize) {
                holderStack.stackSize -= this.holderStackToConsume.stackSize;
                // we have to manually wipe it since it doesn't auto-clear if stack size <= 0
                if (holderStack.stackSize <= 0) {
                    this.eHolders.get(0).mInventory[0] = null;
                }
            } else {
                this.mOutputItems = null;
            }
            this.researchOutputForGUI = null;
            this.holderStackToConsume = null;
        }
        this.computationRequired = this.computationRemaining = 0;
    }

    @Override
    protected void addClassicOutputs_EM() {
        super.addClassicOutputs_EM();
        // jic
        this.resetProgress();
    }

    // endregion outputting

    // region nbt
    private final static String NBT_OLD_MODE = "eMachineType";
    private final static String NBT_COMP_REMAIN = "eComputationRemaining";
    private final static String NBT_COMP_TOTAL = "eComputationRequired";
    private final static String NBT_HOLDER = "eHold";
    private final static String NBT_RESEARCH = "eResearchOutput";
    private final static String NBT_PACKET_LOSS_IN = "ePacketFailIn";
    private final static String NBT_PACKET_LOSS_DECAY = "ePacketLossDecay";

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (this.computationRequired > 0) {
            if (this.holderStackToConsume != null) {
                NBTTagCompound iNBT = new NBTTagCompound();
                this.holderStackToConsume.writeToNBT(iNBT);
                aNBT.setTag(NBT_HOLDER, iNBT);
            }
            if (this.researchOutputForGUI != null) {
                NBTTagCompound iNBT = new NBTTagCompound();
                this.researchOutputForGUI.writeToNBT(iNBT);
                aNBT.setTag(NBT_RESEARCH, iNBT);
            }
        }
        aNBT.setLong(NBT_COMP_REMAIN, computationRemaining);
        aNBT.setLong(NBT_COMP_TOTAL, computationRequired);
        aNBT.setInteger(NBT_PACKET_LOSS_IN, this.ticksUntilPacketLossFail);
        aNBT.setLong(NBT_PACKET_LOSS_DECAY, this.packetLossDecayFrom);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey(NBT_OLD_MODE, 8)) {
            // abort the recipe if one was ongoing, this just makes the migration infinitely simpler
            // since the old version didn't track any output values.
            this.resetProgress();
            if (aNBT.getString(NBT_OLD_MODE)
                .equals("Scanner")) {
                this.machineMode = MODE_SCANNER;
            } else {
                this.machineMode = MODE_RESEARCH_STATION;
            }
        } else {
            computationRemaining = aNBT.getLong(NBT_COMP_REMAIN);
            computationRequired = aNBT.getLong(NBT_COMP_TOTAL);
            ticksUntilPacketLossFail = aNBT.getInteger(NBT_PACKET_LOSS_IN);
            packetLossDecayFrom = aNBT.getLong(NBT_PACKET_LOSS_DECAY);
            if (computationRequired > 0) {
                if (aNBT.hasKey(NBT_HOLDER, 10)) {
                    this.holderStackToConsume = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(NBT_HOLDER));
                }
                if (aNBT.hasKey(NBT_RESEARCH, 10)) {
                    this.researchOutputForGUI = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(NBT_RESEARCH));
                }
            }
        }
    }
    // endregion nbt

    // region scanner output
    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchEnergy tHatch : validMTEList(mEnergyHatches)) {
            IGregTechTileEntity mte = tHatch.getBaseMetaTileEntity();
            if (mte == null) continue;
            storedEnergy += mte.getStoredEU();
            maxEnergy += mte.getEUCapacity();
        }
        for (MTEHatchEnergyMulti tHatch : validMTEList(eEnergyMulti)) {
            IGregTechTileEntity mte = tHatch.getBaseMetaTileEntity();
            if (mte == null) continue;
            storedEnergy += mte.getStoredEU();
            maxEnergy += mte.getEUCapacity();
        }

        String connectionStatus;
        if (this.mMaxProgresstime <= 0) {
            connectionStatus = translateToLocalFormatted("tt.infodata.multi.connection_health.inactive");
        } else if (this.ticksUntilPacketLossFail >= PACKET_LOSS_FULL_WINDOW) {
            connectionStatus = EnumChatFormatting.GREEN
                + translateToLocalFormatted("tt.infodata.multi.connection_health.established")
                + EnumChatFormatting.RESET;
        } else if (this.ticksUntilPacketLossFail >= PACKET_LOSS_DECAY_WINDOW) {
            connectionStatus = EnumChatFormatting.YELLOW
                + translateToLocalFormatted("tt.infodata.multi.connection_health.waiting")
                + EnumChatFormatting.RESET;
        } else {
            connectionStatus = EnumChatFormatting.RED
                + translateToLocalFormatted("tt.infodata.multi.connection_health.decoherence")
                + EnumChatFormatting.RESET;
        }

        return new String[] { translateToLocal("tt.keyphrase.Energy_Hatches") + ":",
            EnumChatFormatting.GREEN + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            (mEUt <= 0 ? translateToLocal("tt.keyphrase.Probably_uses") + ": "
                : translateToLocal("tt.keyphrase.Probably_makes") + ": ") + EnumChatFormatting.RED
                + formatNumber(Math.abs(mEUt))
                + EnumChatFormatting.RESET
                + " EU/t "
                + translateToLocal("tt.keyword.at")
                + " "
                + EnumChatFormatting.RED
                + formatNumber(eAmpereFlow)
                + EnumChatFormatting.RESET
                + " A",
            translateToLocal("tt.keyphrase.Tier_Rating") + ": "
                + EnumChatFormatting.YELLOW
                + VN[getMaxEnergyInputTier_EM()]
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.GREEN
                + VN[getMinEnergyInputTier_EM()]
                + EnumChatFormatting.RESET
                + " "
                + translateToLocal("tt.keyphrase.Amp_Rating")
                + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(eMaxAmpereFlow)
                + EnumChatFormatting.RESET
                + " A",
            translateToLocal("tt.keyword.Problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + translateToLocal("tt.keyword.Efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            translateToLocal("tt.keyword.PowerPass") + ": "
                + EnumChatFormatting.BLUE
                + ePowerPass
                + EnumChatFormatting.RESET
                + " "
                + translateToLocal("tt.keyword.SafeVoid")
                + ": "
                + EnumChatFormatting.BLUE
                + eSafeVoid,
            translateToLocal("tt.keyphrase.Computation_Available") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(this.eAvailableData)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + formatNumber(this.eRequiredData)
                + EnumChatFormatting.RESET,
            translateToLocal("tt.keyphrase.Computation_Remaining") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(this.computationRemaining / 20L)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + formatNumber(getComputationRequired()),
            translateToLocalFormatted("tt.infodata.multi.connection_health", connectionStatus),
            translateToLocalFormatted("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }
    // endregion scanner output

    // region gui

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.researching_item",
                            this.researchOutputForGUI == null ? "" : this.researchOutputForGUI.getDisplayName()))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> this.computationRequired > 0 && this.researchOutputForGUI != null))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.research_progress",
                            getComputationConsumed(),
                            getComputationRequired(),
                            formatNumber(getComputationProgress())))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> this.computationRequired > 0 && this.researchOutputForGUI != null))
            .widget(new TextWidget().setStringSupplier(() -> {
                    if (this.ticksUntilPacketLossFail >= PACKET_LOSS_DECAY_WINDOW) {
                        return EnumChatFormatting.YELLOW
                            + translateToLocalFormatted("tt.infodata.multi.connection_health.waiting")
                            + EnumChatFormatting.RESET;
                    }
                    return EnumChatFormatting.RED
                        + translateToLocalFormatted("tt.infodata.multi.connection_health.decoherence")
                        + EnumChatFormatting.RESET;
                })
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(
                    widget -> this.computationRequired > 0 && this.researchOutputForGUI != null
                        && this.ticksUntilPacketLossFail < PACKET_LOSS_FULL_WINDOW))
            .widget(
                new FakeSyncWidget.LongSyncer(
                    () -> this.computationRequired,
                    aLong -> this.computationRequired = aLong))
            .widget(
                new FakeSyncWidget.LongSyncer(
                    () -> this.computationRemaining,
                    aLong -> this.computationRemaining = aLong))
            .widget(
                new FakeSyncWidget.ItemStackSyncer(
                    () -> this.researchOutputForGUI,
                    aStack -> this.researchOutputForGUI = aStack))
            .widget(
                new FakeSyncWidget.IntegerSyncer(
                    () -> this.ticksUntilPacketLossFail,
                    aLong -> this.ticksUntilPacketLossFail = aLong));
    }

    private long getComputationConsumed() {
        return (this.computationRequired - this.computationRemaining) / 20L;
    }

    private long getComputationRequired() {
        return this.computationRequired / 20L;
    }

    private double getComputationProgress() {
        return 100d
            * (getComputationRequired() > 0d ? (double) getComputationConsumed() / getComputationRequired() : 0d);
    }

    // endregion gui

    // region MUI2

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMultiBlockBaseGui<MTEResearchStation>(this) {

            @Override
            protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager,
                                                                      ModularPanel parent) {

                GenericSyncValue<ItemStack> outputSyncer = new GenericSyncValue<>(
                    () -> researchOutputForGUI,
                    val -> researchOutputForGUI = val,
                    com.gtnewhorizons.modularui.common.internal.network.NetworkUtils::readItemStack,
                    com.gtnewhorizons.modularui.common.internal.network.NetworkUtils::writeItemStack,
                    (a, b) -> GTUtility.areStacksEqual(a,b,true) && a.stackSize == b.stackSize,
                    null);

                LongSyncValue computationReqSyncer = new LongSyncValue(() -> computationRequired, val -> computationRequired = val);
                LongSyncValue computationRemSyncer = new LongSyncValue(() -> computationRemaining, val -> computationRemaining = val);
                IntSyncValue ticksUntilPacketLossFailSyncger = new IntSyncValue(() -> ticksUntilPacketLossFail, val -> ticksUntilPacketLossFail = val);


                syncManager.syncValue("outputName", outputSyncer);
                syncManager.syncValue("computationRequired", computationReqSyncer);
                syncManager.syncValue("computationRemaining", computationRemSyncer);
                syncManager.syncValue("ticksUntilPacketLossFail", ticksUntilPacketLossFailSyncger);

                ListWidget<IWidget, ?> terminal = super.createTerminalTextWidget(syncManager, parent);
                terminal.child(
                        IKey.dynamic(
                                () -> {
                                    if (researchOutputForGUI == null) return "";
                                    return StatCollector.translateToLocalFormatted("GT5U.gui.text.researching_item", researchOutputForGUI.getDisplayName());
                                }
                            )
                            .asWidget()
                            .setEnabledIf(
                                ignored -> outputSyncer.getValue() != null))
                    .child(IKey.dynamic(
                        () -> StatCollector.translateToLocalFormatted(
                                "GT5U.gui.text.research_progress",
                                getComputationConsumed(),
                                getComputationRequired(),
                                formatNumber(getComputationProgress()))
                        )
                        .asWidget()
                        .setEnabledIf(ignored -> computationRequired > 0 && researchOutputForGUI != null)
                    )
                    .child(IKey.dynamic(() -> {
                            if (ticksUntilPacketLossFail >= PACKET_LOSS_DECAY_WINDOW) {
                                return EnumChatFormatting.YELLOW
                                    + translateToLocalFormatted("tt.infodata.multi.connection_health.waiting")
                                    + EnumChatFormatting.RESET;
                            }
                            return EnumChatFormatting.RED
                                + translateToLocalFormatted("tt.infodata.multi.connection_health.decoherence")
                                + EnumChatFormatting.RESET;

                        })
                        .asWidget()
                        .setEnabledIf(
                            ignored -> computationRequired > 0 && researchOutputForGUI != null
                                && ticksUntilPacketLossFail < PACKET_LOSS_FULL_WINDOW));
                return terminal;
            }
        }.withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_RESEARCH,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_SCANNER);
    }

    @Override
    protected boolean useMui2() {

        return true;
    }

    // endregion MUI2

    // region waila
    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
                                int z) {
        tag.setBoolean("hasProblems", (getIdealStatus() - getRepairStatus()) > 0);
        tag.setFloat("efficiency", this.mEfficiency / 100.0F);
        tag.setBoolean("incompleteStructure", (getErrorDisplayID() & 64) != 0);
        tag.setInteger("machineMode", this.machineMode);
        tag.setLong("computation", getComputationConsumed());
        tag.setLong("computationRequired", getComputationRequired());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
                             IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(getMachineModeName(tag.getInteger("machineMode")));
        currentTip.add(
            StatCollector.translateToLocalFormatted(
                "gt.blockmachines.multimachine.em.research.computation",
                tag.getInteger("computation"),
                tag.getInteger("computationRequired")));
    }
    // endregion waila

}
