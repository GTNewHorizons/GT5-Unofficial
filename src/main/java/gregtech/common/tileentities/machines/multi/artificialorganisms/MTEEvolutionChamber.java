package gregtech.common.tileentities.machines.multi.artificialorganisms;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_EMPTY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_EMPTY_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.factory.artificialorganisms.MTEHatchAOOutput;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.gui.modularui.multiblock.MTEEvolutionChamberGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.render.IMTERenderer;
import gregtech.common.tileentities.machines.IDualInputHatch;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEEvolutionChamber extends MTEExtendedPowerMultiBlockBase<MTEEvolutionChamber>
    implements ISurvivalConstructable, IMTERenderer {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEEvolutionChamber> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEvolutionChamber>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] {
                { "           ", " BBBCCCBBB ", " AAC   CAA ", " AAC   CAA ", " AAC   CAA ", " AAC   CAA ",
                    " AAC   CAA ", " AAC   CAA ", " AAC   CAA ", " BBB   BBB " },
                { "  CCCCCCC  ", "BBBBBBBBBBB", "A--BAAAB--A", "A--BAAAB--A", "A--BCCCB--A", "A--BAAAB--A",
                    "A--BCCCB--A", "A--BAAAB--A", "A--BAAAB--A", "BBBBB~BBBBB" },
                { " CBBBBBBBC ", "BBB-----BBB", "A---------A", "A---------A", "A---------A", "A---------A",
                    "A---------A", "A---------A", "A---------A", "BBBBBBBBBBB" },
                { " CBBBBBBBC ", "BB-------BB", "CB-------BC", "CB-------BC", "CB-------BC", "CB-------BC",
                    "CB-------BC", "CB-------BC", "CB-------BC", "BBBBBBBBBBB" },
                { " CBBBBBBBC ", "CB-------BC", " A-------A ", " A-------A ", " C-------C ", " A-------A ",
                    " C-------C ", " A-------A ", " A-------A ", " BBBBBBBBB " },
                { " CBBBBBBBC ", "CB-------BC", " A-------A ", " A-------A ", " C-------C ", " A-------A ",
                    " C-------C ", " A-------A ", " A-------A ", " BBBBBBBBB " },
                { " CBBBBBBBC ", "CB-------BC", " A-------A ", " A-------A ", " C-------C ", " A-------A ",
                    " C-------C ", " A-------A ", " A-------A ", " BBBBBBBBB " },
                { " CBBBBBBBC ", "BB-------BB", "CB-------BC", "CB-------BC", "CB-------BC", "CB-------BC",
                    "CB-------BC", "CB-------BC", "CB-------BC", "BBBBBBBBBBB" },
                { " CBBBBBBBC ", "BBB-----BBB", "A---------A", "A---------A", "A---------A", "A---------A",
                    "A---------A", "A---------A", "A---------A", "BBBBBBBBBBB" },
                { "  CCCCCCC  ", "BBBBBBBBBBB", "A--BAAAB--A", "A--BAAAB--A", "A--BCCCB--A", "A--BAAAB--A",
                    "A--BCCCB--A", "A--BAAAB--A", "A--BAAAB--A", "BBBBBBBBBBB" },
                { "           ", " BBBCCCBBB ", " AAC   CAA ", " AAC   CAA ", " AAC   CAA ", " AAC   CAA ",
                    " AAC   CAA ", " AAC   CAA ", " AAC   CAA ", " BBB   BBB " } })
        .addElement('A', chainAllGlasses())
        .addElement(
            'B',
            ofChain(
                buildHatchAdder(MTEEvolutionChamber.class)
                    .atLeast(Maintenance, Energy, InputHatch, SpecialHatchElement.BioOutput)
                    .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(61))
                    .hint(1)
                    .build(),
                onElementPass(
                    MTEEvolutionChamber::onCasingAdded,
                    StructureUtility.ofBlocksTiered(
                        MTEEvolutionChamber::getTierFromMeta,
                        ImmutableList.of(
                            Pair.of(GregTechAPI.sBlockCasings12, 6),
                            Pair.of(GregTechAPI.sBlockCasings12, 7),
                            Pair.of(GregTechAPI.sBlockCasings12, 8)),
                        -1,
                        MTEEvolutionChamber::setCasingTier,
                        MTEEvolutionChamber::getCasingTier))))
        .addElement('C', ofFrame(Materials.Netherite))
        .build();

    private enum SpecialHatchElement implements IHatchElement<MTEEvolutionChamber> {

        BioOutput(MTEEvolutionChamber::addBioHatch, MTEHatchAOOutput.class) {

            @Override
            public long count(MTEEvolutionChamber gtMetaTileEntityEvolutionChamber) {
                return gtMetaTileEntityEvolutionChamber.bioHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEEvolutionChamber> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTEEvolutionChamber> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEEvolutionChamber> adder() {
            return adder;
        }
    }

    private final ArrayList<MTEHatchAOOutput> bioHatches = new ArrayList<>();

    /** Base tank capacity for a T1 HMC. Each casing tier multiplies this by 4. */
    public static final int BASE_MAX_AOS = 50_000;
    /** AOs recovered per maintenance cycle: 25 every 5 ticks, or 100 per second. */
    public static final int BASE_REGEN = 25;
    /** AOs lost per maintenance cycle when nutrient fluid is unavailable. */
    public static final int BASE_DECAY = 1_000;
    /** Sterilization fluid that must be present in an input hatch to wipe the current species for re-selection. */
    public static final int STERILIZE_AMOUNT = 1_000;
    /** Base maintenance power fixed at UV. */
    public static final long BASE_POWER = TierEU.UV;
    /** Bse nutrient fluid consumed per maintenance cycle at Tier 1. */
    public static final int BASE_NUTRIENT_USAGE = 25;
    /** Ticks between maintenance cycles. */
    public static final int CYCLE_TICKS = 5;

    public ArtificialOrganism currentSpecies = new ArtificialOrganism();

    private int casingTier;
    public int maxAOs;

    public final int INTERNAL_FLUID_TANK_SIZE = 64000;

    boolean isFinalized = false;

    /** Tank capacity for the current casing tier, BASE_MAX_AOS * 4^(tier-1). */
    public static int getMaxAOsForTier(int tier) {
        if (tier < 1) return 0;
        return BASE_MAX_AOS << (2 * (tier - 1));
    }

    /**
     * Base maintenance power draw, fixed at UV regardless of casing tier. The casing tier scales tank capacity, not
     * running cost. The trait power modifier is applied after overclocking in {@link #onPostTick} and does not affect
     * the overclock.
     */
    public long getMaintenancePower() {
        return BASE_POWER;
    }

    public MTEEvolutionChamber(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEEvolutionChamber(String aName) {
        super(aName);
    }

    public int getCasingTier() {
        return casingTier;
    }

    public void setCasingTier(int i) {
        casingTier = i;
    }

    private static Integer getTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasings12) return null;
        if (metaID < 6 || metaID > 8) return null;
        return metaID - 5;
    }

    @Override
    public IStructureDefinition<MTEEvolutionChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        if (aValue >= 10) {
            isFinalized = true;
            aValue -= 10;
        }
        casingTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        byte update = (byte) casingTier;
        if (currentSpecies != null && currentSpecies.getFinalized()) {
            update += 10;
        }
        return update;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEvolutionChamber(this.mName);
    }

    // Returns the fill level of either nutrient broth or primordial soup
    public int getFillLevel() {
        return tank.getFluidAmount();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        int casingMeta = mMachine ? Math.max(6, getCasingTier() + 5) : 6;
        if (side == aFacing) {
            if (currentSpecies != null && currentSpecies.getFinalized()) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(
                        GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingMeta)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BIOVAT)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BIOVAT_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(
                        GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingMeta)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BIOVAT_EMPTY)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BIOVAT_EMPTY_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingMeta)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Artificial Organism Source")
            .addInfo("Used to create and maintain Artificial Organisms")
            .addInfo("Use higher tier vat casings to get more AO culture slots")
            .addInfo("Maximum tank capacity is " + BASE_MAX_AOS + " * 4^(tier-1)")
            .addInfo("Base recovery is " + (BASE_REGEN * 20 / CYCLE_TICKS) + " AOs/s)")
            .addInfo("Recovery is increased by 20% per level of Reproduction")
            .addInfo("4x the maintenance power doubles the recovery rate")
            .addInfo(
                "Consumes " + (BASE_NUTRIENT_USAGE * 20 / CYCLE_TICKS)
                    + " L of nutrient fluid/s, scaled by traits and overclock")
            .addInfo("Without nutrients, loses " + (BASE_DECAY * 20 / CYCLE_TICKS) + " AOs/s, scaled by traits")
            .addSeparator()
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 85, false)
            .addCasingInfoExactly("Steel Pipe Casing", 24, false)
            .addInputHatch("Any Vat Casing", 1)
            .addEnergyHatch("Any Vat Casing", 1, 2)
            .addMaintenanceHatch("Any Vat Casing", 1)
            .addOtherStructurePart("Bio Output Hatch", "Any Vat Casing", 2)
            .addStructureInfo("Only normal Energy Hatches are accepted; Multi-Amp and Laser Energy Hatches are not")
            .toolTipFinisher("GregTech");
        return tt;
    }

    private void updateTextures() {
        getBaseMetaTileEntity().issueTextureUpdate();

        int textureID = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, Math.max(6, casingTier));
        for (MTEHatch h : mInputBusses) h.updateTexture(textureID);
        for (MTEHatch h : mInputHatches) h.updateTexture(textureID);
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(textureID);
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(textureID);
        for (MTEHatch h : mEnergyHatches) h.updateTexture(textureID);
        for (MTEHatch h : bioHatches) h.updateTexture(textureID);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 5, 9, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 5, 9, 1, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mCasingAmount = 0;
        casingTier = -1;
        bioHatches.clear();
        mEnergyHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 5, 9, 1, errors)) return;
        if (casingTier < 1) return;

        checkHatchMin(errors, InputHatch, 1);
        checkHatchMin(errors, Energy, 1);
        checkHatchMax(errors, Energy, 2);
        checkOneMaintenanceHatch(errors);
        if (!mInputBusses.isEmpty()) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.input_bus_not_allowed"));
        }

        updateTextures();
        maxAOs = getMaxAOsForTier(casingTier);
        if (currentSpecies != null) currentSpecies.setMaxAOs(maxAOs);
    }

    private boolean useNutrients(FluidStack fluid) {
        if (fluid == null) {
            return false;
        }

        // Nutrients are collected into the internal tank
        FluidStack drained = tank.drain(fluid.amount, true);
        return drained != null && drained.amount >= fluid.amount;
    }

    private void triggerNutrientLoss() {
        currentSpecies.doDeath(BASE_DECAY);
    }

    private void triggerElectricityLoss() {
        currentSpecies.doDeath(BASE_DECAY);
    }

    /**
     * Try to sterilize if 1000L of sterilization fluid is present
     */
    private boolean trySterilize() {
        for (MTEHatchInput hatch : mInputHatches) {
            FluidStack stored = hatch.getFluid();
            if (stored == null || stored.amount < STERILIZE_AMOUNT) continue;
            if (stored.getFluid() != Materials.SterilizationFluid.mFluid) continue;
            hatch.drain(STERILIZE_AMOUNT, true);
            currentSpecies.sterilize();
            tank.drain(Integer.MAX_VALUE, true);
            return true;
        }
        return false;
    }

    /**
     * perform 4/2 overclock for recovery rate/nutrient cost
     */
    public double getOverclockMultiplier() {
        long maintenance = getMaintenancePower();
        if (maintenance <= 0) return 0;
        long supplied = getMaxInputEu();
        if (supplied <= 0) return 0;
        return Math.sqrt((double) supplied / (double) maintenance);
    }

    /** Theoretical AO recovery rate per second with current overclock and trait modifiers. */
    public int getAORecoveryRate() {
        if (currentSpecies == null || !currentSpecies.getFinalized()) return 0;
        int recoveryPerCycle = currentSpecies
            .calculateReproduction((int) Math.round(BASE_REGEN * getOverclockMultiplier()));
        return recoveryPerCycle * 20 / CYCLE_TICKS;
    }

    /** Theoretical nutrient consumption rate with current overclock and trait modifiers. */
    public int getNutrientUsageRate() {
        if (currentSpecies == null || !currentSpecies.getFinalized()) return 0;
        int usagePerCycle = (int) Math
            .round(BASE_NUTRIENT_USAGE * currentSpecies.getNutritionModifier() * getOverclockMultiplier());
        return usagePerCycle * 20 / CYCLE_TICKS;
    }

    /**
     * Runs one maintenance cycle: collect and consume nutrients, then recover AOs.
     */
    private void runMaintenanceCycle(IGregTechTileEntity aBaseMetaTileEntity, double overclock) {
        if (trySterilize()) {
            aBaseMetaTileEntity.issueTileUpdate();
            return;
        }

        boolean fluidChanged = false;
        if (tank.getFluidAmount() < tank.getCapacity()) {
            for (MTEHatchInput hatch : mInputHatches) {
                int remaining = tank.getCapacity() - tank.getFluidAmount();
                FluidStack drain = hatch.drain(remaining, true);
                if (drain == null || drain.amount <= 0) continue;
                fluidChanged = true;
                tank.fill(drain, true);
            }
        }
        if (fluidChanged) aBaseMetaTileEntity.issueTileUpdate();

        // Consume the nutrient fluid: 25 L base per cycle.
        int nutrientAmount = (int) Math.round(BASE_NUTRIENT_USAGE * currentSpecies.getNutritionModifier() * overclock);
        FluidStack nutrient = new FluidStack(currentSpecies.getNutritionFluid(), nutrientAmount);
        if (!useNutrients(nutrient)) {
            triggerNutrientLoss();
            return;
        }

        currentSpecies.doReproduction((int) Math.round(BASE_REGEN * overclock));
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (!mMachine || !aBaseMetaTileEntity.isServerSide()
            || currentSpecies == null
            || !currentSpecies.getFinalized()) return;

        currentSpecies.setMaxAOs(maxAOs);

        double overclock = getOverclockMultiplier();
        if (overclock <= 0) {
            if (aTick % CYCLE_TICKS == 0) triggerElectricityLoss();
            return;
        }

        long energyUsage = Math
            .round(getMaintenancePower() * overclock * overclock * currentSpecies.getPowerModifier());
        if (!drainEnergyInput(energyUsage)) {
            if (aTick % CYCLE_TICKS == 0) triggerElectricityLoss();
            return;
        }

        if (aTick % CYCLE_TICKS == 0) runMaintenanceCycle(aBaseMetaTileEntity, overclock);
    }

    FluidTank tank = new FluidTank(INTERNAL_FLUID_TANK_SIZE);

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = new NBTTagCompound();
        if (tank.getFluid() != null) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            tank.getFluid()
                .writeToNBT(fluidTag);
            tag.setTag("fluidTank", fluidTag);
        }
        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound tag) {
        if (tag.hasKey("fluidTank")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("fluidTank"));
            tank.setFluid(fluid);
        } else {
            tank.setFluid(null);
        }
    }

    public LimitingItemStackHandler limitedHandler = new LimitingItemStackHandler(1, 1);

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        currentSpecies = new ArtificialOrganism(aNBT);

        // so that the casing texture is applied on world load
        casingTier = aNBT.getInteger("casingTier");

        if (aNBT.hasKey("fluidTank")) {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("fluidTank"));
            tank.setFluid(fluid);
        } else {
            tank.setFluid(null);
        }
        if (limitedHandler != null) {
            limitedHandler.deserializeNBT(aNBT.getCompoundTag("inventory"));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(currentSpecies.saveAOToCompound(aNBT));
        aNBT.setInteger("casingTier", Math.max(1, casingTier));

        if (tank.getFluid() != null) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            tank.getFluid()
                .writeToNBT(fluidTag);
            aNBT.setTag("fluidTank", fluidTag);
        }
        if (limitedHandler != null) {
            aNBT.setTag("inventory", limitedHandler.serializeNBT());
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    private boolean addBioHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchAOOutput hatch) {
                if (currentSpecies != null) hatch.setSpecies(currentSpecies);
                return bioHatches.add(hatch);
            }
        }
        return false;
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d != ForgeDirection.UP && d != ForgeDirection.DOWN;
    }

    public void createNewAOs() {
        if (!canFinalize()) return;
        currentSpecies.finalize(maxAOs);
        for (MTEHatchAOOutput hatch : bioHatches) hatch.setSpecies(currentSpecies);
    }

    @Override
    public boolean shouldCheckMaintenance() {
        return false;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("casingTier", Math.max(1, casingTier));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currentTip.add("Tier: " + EnumChatFormatting.WHITE + tag.getInteger("casingTier"));
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.ORGANIC;
    }

    public boolean isValidCulture(ItemStack input) {
        return ArtificialOrganism.getTraitFromItem(input) != null;
    }

    public boolean canAddTrait() {
        return !currentSpecies.getFinalized() && currentSpecies.traits.size() < casingTier;
    }

    public boolean canFinalize() {
        return !currentSpecies.getFinalized() && !currentSpecies.traits.isEmpty();
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.ORGANIC;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<MTEEvolutionChamber> getGui() {
        return new MTEEvolutionChamberGui(this);
    }

    @Override
    public void renderTESR(double x, double y, double z, float partialTicks) {
        FluidTankInfo info = tank.getInfo();
        FluidStack fluid = info.fluid;
        if (fluid == null || fluid.amount <= 0) return;

        float fillRatio = (float) fluid.amount / INTERNAL_FLUID_TANK_SIZE;
        float fluidHeight = fillRatio * 7;

        // Get fluid icon and color
        IIcon icon = fluid.getFluid()
            .getIcon();
        int color = fluid.getFluid()
            .getColor(fluid);
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        // Bind texture
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ForgeDirection direction = getDirection();
        GL11.glTranslated(x - 3 + (direction.offsetX * -4), y + 1, z - 3 + (direction.offsetZ * -4));
        GL11.glColor4f(r, g, b, 0.99f);

        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();

        // front
        tess.addVertexWithUV(0, fluidHeight, 0, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(7, fluidHeight, 0, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(7, 0, 0, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());

        // Back face (z = 7)
        tess.addVertexWithUV(0, fluidHeight, 7, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(0, 0, 7, icon.getMinU(), icon.getMinV());
        tess.addVertexWithUV(7, 0, 7, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(7, fluidHeight, 7, icon.getMaxU(), icon.getMaxV());

        // Left face (x = 0)
        tess.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());
        tess.addVertexWithUV(0, 0, 7, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(0, fluidHeight, 7, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(0, fluidHeight, 0, icon.getMinU(), icon.getMaxV());

        // Right face (x = 7)
        tess.addVertexWithUV(7, fluidHeight, 0, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(7, fluidHeight, 7, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(7, 0, 7, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(7, 0, 0, icon.getMinU(), icon.getMinV());

        // Top face (y = fluidHeight)
        tess.addVertexWithUV(0, fluidHeight, 7, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(7, fluidHeight, 7, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(7, fluidHeight, 0, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(0, fluidHeight, 0, icon.getMinU(), icon.getMinV());

        // Bottom face (y = 0)
        tess.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());
        tess.addVertexWithUV(7, 0, 0, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(7, 0, 7, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(0, 0, 7, icon.getMinU(), icon.getMaxV());

        tess.draw();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

}
