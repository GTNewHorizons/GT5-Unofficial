package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.GTValues.AuthorNotAPenguin;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_GLOW;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBase.WATER_BOOST_BONUS_CHANCE;
import static gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBase.WATER_BOOST_NEEDED_FLUID;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.MTEPurificationPlantGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class MTEPurificationPlant extends MTEExtendedPowerMultiBlockBase<MTEPurificationPlant>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "main_survival";

    /**
     * Maximum distance in each axis between the purification plant main controller and the controller blocks of the
     * purification plant units.
     */
    public static final int MAX_UNIT_DISTANCE = 32;

    /**
     * Time in ticks for a full processing cycle to complete.
     */
    public static final int CYCLE_TIME_TICKS = 120 * SECONDS;

    public List<LinkedPurificationUnit> getLinkedUnits() {
        return linkedUnits;
    }

    /**
     * Stores all purification units linked to this controller. Normally all units in this list should be valid and
     * unique, if not then there is a bug where they are not being unlinked properly on block destruction/relinking.
     */
    private final List<LinkedPurificationUnit> linkedUnits = new ArrayList<>();

    /**
     * Debug mode is an operational mode that does not produce output or consume input, but cuts down processing time
     * for players to more easily debug their automation setups.
     */
    private boolean debugMode = false;
    public static final int CYCLE_TIME_IN_DEBUG = 30 * SECONDS;

    private static final IStructureDefinition<MTEPurificationPlant> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPurificationPlant>builder()
        .addShape(STRUCTURE_PIECE_MAIN, PurificationPlantStructureString.STRUCTURE_STRING)
        // Create an identical structure for survival autobuild, with water replaced with air
        .addShape(
            STRUCTURE_PIECE_MAIN_SURVIVAL,
            Arrays.stream(PurificationPlantStructureString.STRUCTURE_STRING)
                .map(
                    sa -> Arrays.stream(sa)
                        .map(s -> s.replaceAll("F", " "))
                        .toArray(String[]::new))
                .toArray(String[][]::new))
        // Superplasticizer-treated high strength concrete
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings9, 3))
        // Sterile Water Plant Casing
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings9, 4))
        // Reinforced Sterile Water Plant Casing
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings9, 5))
        // Tinted Industrial Glass
        .addElement('D', ofBlockAnyMeta(GregTechAPI.sBlockTintedGlass, 0))
        .addElement('F', ofAnyWater())
        .addElement('G', ofFrame(Materials.Tungsten))
        // Hatch space
        .addElement(
            'H',
            ofChain(
                lazy(
                    t -> GTStructureUtility.<MTEPurificationPlant>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .hint(1)
                        .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings9, 4))
                        .build()),
                ofBlock(GregTechAPI.sBlockCasings9, 4)))
        .build();

    public MTEPurificationPlant(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPurificationPlant(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = survivalBuildPiece(STRUCTURE_PIECE_MAIN_SURVIVAL, stackSize, 3, 6, 0, elementBudget, env, true);
        if (built == -1) {
            GTUtility.sendChatToPlayer(
                env.getActor(),
                EnumChatFormatting.GREEN + "Auto placing done ! Now go place the water yourself !");
            return 0;
        }
        return built;
    }

    @Override
    public IStructureDefinition<MTEPurificationPlant> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Purification Plant, WPP")
            .addInfo("Main controller block for the Water Purification Plant")
            .addInfo(
                "Freely place " + EnumChatFormatting.YELLOW
                    + "Purification Units "
                    + EnumChatFormatting.GRAY
                    + "within "
                    + EnumChatFormatting.RED
                    + MAX_UNIT_DISTANCE
                    + EnumChatFormatting.GRAY
                    + " blocks along each axis")
            .addInfo("Left click this controller with a data stick, then right click a purification unit to link")
            .addInfo("Supplies power to linked purification units")
            .addTecTechHatchInfo()
            .addSeparator()
            .addInfo(
                "Works in fixed time processing cycles of " + EnumChatFormatting.RED
                    + CYCLE_TIME_TICKS / SECONDS
                    + EnumChatFormatting.GRAY
                    + " seconds")
            .addInfo("All linked units follow this cycle")
            .addSeparator()
            .addInfo("Every recipe has a base chance of success. Success rate can be boosted")
            .addInfo("by using a portion of the target output as a secondary input")
            .addInfo(
                EnumChatFormatting.RED + GTUtility.formatNumbers(WATER_BOOST_NEEDED_FLUID * 100)
                    + "%"
                    + EnumChatFormatting.GRAY
                    + " of output yield will be consumed in exchange for an")
            .addInfo(
                "additive " + EnumChatFormatting.RED
                    + GTUtility.formatNumbers(WATER_BOOST_BONUS_CHANCE * 100)
                    + "%"
                    + EnumChatFormatting.GRAY
                    + " increase to success")
            .addInfo(
                "On recipe failure, each purification unit has a " + EnumChatFormatting.RED
                    + "50%"
                    + EnumChatFormatting.GRAY
                    + " chance")
            .addInfo("to return water of the same quality as the input or lower")
            .addSeparator()
            .addInfo("Every purification unit has a configuration window to configure maximum parallel amount")
            .addInfo(
                "This will only scale purified water input, ALL fluid output and power usage. Other catalysts and outputs are unchanged")
            .addInfo("Toggle debug mode to reduce cycle time to 30s but disable water I/O")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "Contaminants and ionized particles in water can cause significant imperfections in delicate")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "processes related to the cutting and engraving of silicon wafers and chips. It is crucial that")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "the water is systematically purified through a series of increasingly precise and complex")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "purification processes, and this multiblock is the heart of the operation")
            .beginStructureBlock(7, 9, 8, false)
            .addController("Front center")
            .addCasingInfoExactlyColored(
                "Superplasticizer-Treated High Strength Concrete",
                EnumChatFormatting.GRAY,
                56,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoRangeColored(
                "Sterile Water Plant Casing",
                EnumChatFormatting.GRAY,
                71,
                72,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Reinforced Sterile Water Plant Casing",
                EnumChatFormatting.GRAY,
                77,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Tungsten Frame Box",
                EnumChatFormatting.GRAY,
                30,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Tinted Industrial Glass",
                EnumChatFormatting.GRAY,
                6,
                EnumChatFormatting.GOLD,
                false)
            .addEnergyHatch(EnumChatFormatting.GOLD + "1", 1)
            .addMaintenanceHatch(EnumChatFormatting.GOLD + "1", 1)
            .addStructureInfo("Requires water to be placed in the tank.")
            .toolTipFinisher(AuthorNotAPenguin);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPurificationPlant(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings9, 4)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings9, 4)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PURIFICATION_PLANT)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PURIFICATION_PLANT_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings9, 4)) };
    }

    private List<IHatchElement<? super MTEPurificationPlant>> getAllowedHatches() {
        return ImmutableList.of(Maintenance, Energy, ExoticEnergy);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check self
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 6, 0)) {
            return false;
        }

        // Check hatches
        if (!checkHatches()) {
            return false;
        }

        // using nano forge method of detecting hatches.
        return checkExoticAndNormalEnergyHatches();
    }

    private boolean checkHatches() {
        // Exactly one maintenance hatch is required
        return mMaintenanceHatches.size() == 1;
    }

    public boolean debugModeOn() {
        return debugMode;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            // Trigger structure check of linked units, but never all in the same tick, and at most once per cycle.
            for (int i = 0; i < linkedUnits.size(); ++i) {
                if (aTick % CYCLE_TIME_TICKS == i) {
                    LinkedPurificationUnit unit = linkedUnits.get(i);
                    boolean structure = unit.metaTileEntity()
                        .checkStructure(true);
                    // If unit was active but deformed, set as inactive
                    if (unit.isActive() && !structure) {
                        unit.setActive(false);
                        // Also remember to recalculate power usage, since otherwise the deformed unit will
                        // keep drawing power
                        this.lEUt = -calculateEffectivePowerUsage();
                    }
                }
            }
        }
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        updateCycleProgress();
        // Calculate efficiency based on maintenance issues
        if (mMaxProgresstime > 0) {
            mEfficiency = Math.max(
                0,
                Math.min(
                    mEfficiency + mEfficiencyIncrease,
                    getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
        }
    }

    private void updateCycleProgress() {
        // Since the plant does not run recipes directly, we just continuously loop the base cycle
        if (mMachine) {
            // cycle is running, so simply advance it
            if (mMaxProgresstime > 0) {
                // onRunningTick is responsible for draining power
                if (onRunningTick(mInventory[1])) {
                    markDirty();
                    mProgresstime += 1;
                    // Update progress time for active units
                    for (LinkedPurificationUnit unit : this.linkedUnits) {
                        if (unit.isActive()) {
                            MTEPurificationUnitBase<?> metaTileEntity = unit.metaTileEntity();
                            metaTileEntity.mProgresstime = mProgresstime;
                        }
                    }
                    // Cycle finished
                    if (mProgresstime >= mMaxProgresstime) {
                        this.endCycle();
                    }
                } else {
                    // Power drain failed, shut down all other units due to power loss.
                    // Note that we do not need to shut down self, as this is done in
                    // onRunningTick already
                    for (LinkedPurificationUnit unit : linkedUnits) {
                        if (unit.isActive()) {
                            unit.metaTileEntity()
                                .stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                            unit.setActive(false);
                        }
                    }
                }
            }

            // No cycle running, start a new cycle if the machine is turned on
            if (mMaxProgresstime == 0 && isAllowedToWork()) {
                this.startCycle();
            }
        }
    }

    private void startCycle() {
        mProgresstime = 0;
        // Debug mode uses shorter cycles
        if (debugMode) mMaxProgresstime = CYCLE_TIME_IN_DEBUG;
        else mMaxProgresstime = CYCLE_TIME_TICKS;
        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);

        // Find active units and notify them that the cycle started
        for (LinkedPurificationUnit unit : this.linkedUnits) {
            MTEPurificationUnitBase<?> metaTileEntity = unit.metaTileEntity();
            PurificationUnitStatus status = metaTileEntity.status();
            // Unit needs to be online to be considered active.
            if (status == PurificationUnitStatus.ONLINE) {
                // Perform recipe check for unit and start it if successful
                if (metaTileEntity.doPurificationRecipeCheck()) {
                    unit.setActive(true);
                    metaTileEntity.startCycle(mMaxProgresstime, mProgresstime);
                }
            }
        }

        // After activating all units, calculate power usage
        lEUt = -calculateEffectivePowerUsage();
    }

    private void endCycle() {
        mMaxProgresstime = 0;

        // Mark all units as inactive and reset their progress time
        for (LinkedPurificationUnit unit : this.linkedUnits) {
            MTEPurificationUnitBase<?> metaTileEntity = unit.metaTileEntity();
            // If this unit was active, end the cycle
            if (unit.isActive()) {
                metaTileEntity.endCycle();
            }
            unit.setActive(false);
        }
    }

    /**
     * Calculate power usage of all units
     */
    private long calculateEffectivePowerUsage() {
        long euT = 0;
        for (LinkedPurificationUnit unit : linkedUnits) {
            if (unit.isActive()) {
                euT += unit.metaTileEntity()
                    .getActualPowerUsage();
            }
        }
        return euT;
    }

    public void registerLinkedUnit(MTEPurificationUnitBase<?> unit) {
        LinkedPurificationUnit link = new LinkedPurificationUnit(unit);
        // Make sure to mark it as active if it is running a recipe. This happens on server restart and fixes
        // waterline multiblocks not resuming their progress until the next cycle.
        link.setActive(unit.mMaxProgresstime > 0);
        this.linkedUnits.add(link);
    }

    public void unregisterLinkedUnit(MTEPurificationUnitBase<?> unit) {
        this.linkedUnits.removeIf(link -> link.metaTileEntity() == unit);
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;

        // Save link data to data stick, very similar to Crafting Input Buffer.

        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", "PurificationPlant");
        tag.setInteger("x", aBaseMetaTileEntity.getXCoord());
        tag.setInteger("y", aBaseMetaTileEntity.getYCoord());
        tag.setInteger("z", aBaseMetaTileEntity.getZCoord());

        dataStick.stackTagCompound = tag;
        dataStick.setStackDisplayName(
            "Purification Plant Link Data Stick (" + aBaseMetaTileEntity
                .getXCoord() + ", " + aBaseMetaTileEntity.getYCoord() + ", " + aBaseMetaTileEntity.getZCoord() + ")");
        aPlayer.addChatMessage(new ChatComponentText("Saved Link Data to Data Stick"));
    }

    @Override
    public String[] getInfoData() {
        var ret = new ArrayList<String>();
        ret.add(
            translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(recipesDone)
                + EnumChatFormatting.RESET);
        // Show linked purification units and their status
        ret.add(translateToLocal("GT5U.infodata.purification_plant.linked_units"));
        for (LinkedPurificationUnit unit : this.linkedUnits) {
            String text = EnumChatFormatting.AQUA + unit.metaTileEntity()
                .getLocalName() + ": ";
            PurificationUnitStatus status = unit.metaTileEntity()
                .status();
            switch (status) {
                case ONLINE -> {
                    text = text + EnumChatFormatting.GREEN
                        + translateToLocal("GT5U.infodata.purification_plant.linked_units.status.online");
                }
                case DISABLED -> {
                    text = text + EnumChatFormatting.YELLOW
                        + translateToLocal("GT5U.infodata.purification_plant.linked_units.status.disabled");
                }
                case INCOMPLETE_STRUCTURE -> {
                    text = text + EnumChatFormatting.RED
                        + translateToLocal("GT5U.infodata.purification_plant.linked_units.status.incomplete");
                }
            }
            ret.add(text);
        }
        return ret.toArray(new String[0]);
    }

    @Override
    public void onBlockDestroyed() {
        // When the controller is destroyed we want to notify all currently linked units
        for (LinkedPurificationUnit unit : this.linkedUnits) {
            unit.metaTileEntity()
                .unlinkController();
        }
        super.onBlockDestroyed();
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEPurificationPlantGui(this);
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        return debugMode;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        debugMode = aNBT.getBoolean("debugMode");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("debugMode", debugMode);

    }

}
