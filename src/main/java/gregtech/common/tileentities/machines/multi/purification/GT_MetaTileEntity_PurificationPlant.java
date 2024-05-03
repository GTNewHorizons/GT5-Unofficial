package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitBase.WATER_BOOST_BONUS_CHANCE;
import static gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitBase.WATER_BOOST_NEEDED_FLUID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.shapes.Rectangle;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedRow;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.widget.ShutDownReasonSyncer;
import gregtech.common.gui.modularui.widget.TextButtonWidget;

public class GT_MetaTileEntity_PurificationPlant
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_PurificationPlant> {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    /**
     * Maximum distance in each axis between the purification plant main controller and the controller blocks of the
     * purification plant units.
     */
    public static final int MAX_UNIT_DISTANCE = 27;

    /**
     * Time in ticks for a full processing cycle to complete.
     */
    public static final int CYCLE_TIME_TICKS = 10 * 20; // TODO: Set to proper value after debugging

    /**
     * Stores all purification units linked to this controller.
     * Normally all units in this list should be valid and unique, if not then there is a bug where they are not being
     * unlinked properly on block destruction/relinking.
     */
    private final List<LinkedPurificationUnit> mLinkedUnits = new ArrayList<>();

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationPlant> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationPlant>builder()
        .addShape(STRUCTURE_PIECE_MAIN, PurificationPlantStructureString.STRUCTURE_STRING)
        // PLACEHOLDER: Advanced iridium machine casing
        .addElement('A', ofBlock(GregTech_API.sBlockCasings8, 7))
        // Industrial Water Plant Casing
        .addElement('B', ofBlock(GregTech_API.sBlockCasings9, 3))
        // Industrial strength concrete
        .addElement('C', ofBlock(GregTech_API.sBlockCasings9, 2))
        // Door
        .addElement('D', lazy(t -> ofBlock(GameRegistry.findBlock("IC2", "blockDoorAlloy"), 0)))
        // PLACEHOLDER: Stained glass
        .addElement('E', ofBlock(Blocks.stained_glass, 0))
        .addElement('W', ofBlock(Blocks.water, 0))
        // Material may change?
        .addElement('G', ofFrame(Materials.Tungsten))
        // Hatch space
        .addElement(
            'H',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationPlant>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .dot(1)
                        .casingIndex(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings9, 3))
                        .build()),
                ofBlock(GregTech_API.sBlockCasings9, 3)))
        .build();

    public GT_MetaTileEntity_PurificationPlant(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationPlant(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 24, 9, 20);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PurificationPlant> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Plant")
            .addInfo("Main controller block for the Water Purification Plant.")
            .addInfo(
                "Freely place " + EnumChatFormatting.YELLOW
                    + "Purification Units "
                    + EnumChatFormatting.GRAY
                    + "inside the structure.")
            .addInfo("Left click this controller with a data stick, then right click a purification unit to link.")
            .addInfo("Supplies power to linked purification units. This multiblock accepts TecTech energy hatches.")
            .addSeparator()
            .addInfo(
                "Works in fixed time processing cycles of " + EnumChatFormatting.RED
                    + CYCLE_TIME_TICKS / 20
                    + EnumChatFormatting.GRAY
                    + " seconds.")
            .addInfo("All linked units follow this cycle.")
            .addSeparator()
            .addInfo("Every recipe has a base chance of success. Success rate can be boosted")
            .addInfo("by using a portion of the target output as a secondary input.")
            .addInfo(
                EnumChatFormatting.RED + GT_Utility.formatNumbers(WATER_BOOST_NEEDED_FLUID * 100)
                    + "%"
                    + EnumChatFormatting.GRAY
                    + " of output yield will be consumed in exchange for an")
            .addInfo(
                "additive " + EnumChatFormatting.RED
                    + GT_Utility.formatNumbers(WATER_BOOST_BONUS_CHANCE * 100)
                    + "%"
                    + EnumChatFormatting.GRAY
                    + " increase to success.")
            .addInfo(
                "On recipe failure, each purification unit has a " + EnumChatFormatting.RED
                    + "50%"
                    + EnumChatFormatting.GRAY
                    + " chance")
            .addInfo("to return water of the same quality as the input or lower.")
            .addInfo(AuthorNotAPenguin)
            .beginStructureBlock(49, 9, 49, false)
            .addCasingInfoExactlyColored(
                "Industrial Strength Concrete",
                EnumChatFormatting.GRAY,
                2401,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Advanced Iridium Plated Machine Casing",
                EnumChatFormatting.GRAY,
                77,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoRangeColored(
                "Industrial Water Plant Casing",
                EnumChatFormatting.GRAY,
                71,
                72,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Tungsten Frame Box",
                EnumChatFormatting.GRAY,
                30,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "White Stained Glass",
                EnumChatFormatting.GRAY,
                6,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored("Reinforced Door", EnumChatFormatting.GRAY, 1, EnumChatFormatting.GOLD, false)
            .addController("Front center")
            .addEnergyHatch(EnumChatFormatting.GOLD + "1", 1)
            .addMaintenanceHatch(EnumChatFormatting.GOLD + "1", 1)
            .addStructureInfo("Requires water to be placed in the tank.")
            .addStructureInfo("Use the StructureLib Hologram Projector to build the structure.")
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationPlant(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings9, 3)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings9, 3)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings9, 3)) };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    private List<IHatchElement<? super GT_MetaTileEntity_PurificationPlant>> getAllowedHatches() {
        return ImmutableList.of(Maintenance, Energy, ExoticEnergy);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check self
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 24, 9, 20)) {
            return false;
        }

        if (!checkHatches()) {
            return false;
        }
        // using nano forge method of detecting hatches.
        if (!checkExoticAndNormalEnergyHatches()) {
            return false;
        }

        return true;
    }

    private boolean checkHatches() {
        return mMaintenanceHatches.size() == 1;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            // Trigger structure check of linked units, but never all in the same tick, and at most once per cycle.
            for (int i = 0; i < mLinkedUnits.size(); ++i) {
                if (aTick % CYCLE_TIME_TICKS == i) {
                    LinkedPurificationUnit unit = mLinkedUnits.get(i);
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
                    for (LinkedPurificationUnit unit : this.mLinkedUnits) {
                        if (unit.isActive()) {
                            GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity = unit.metaTileEntity();
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
                    for (LinkedPurificationUnit unit : mLinkedUnits) {
                        if (unit.isActive()) {
                            unit.metaTileEntity()
                                .stopMachine(ShutDownReasonRegistry.POWER_LOSS);
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
        mMaxProgresstime = CYCLE_TIME_TICKS;
        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);

        // Find active units and notify them that the cycle started
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity = unit.metaTileEntity();
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
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity = unit.metaTileEntity();
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
        for (LinkedPurificationUnit unit : mLinkedUnits) {
            if (unit.isActive()) {
                euT += unit.metaTileEntity()
                    .getActivePowerUsage();
            }
        }
        return euT;
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
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public void registerLinkedUnit(GT_MetaTileEntity_PurificationUnitBase<?> unit) {
        this.mLinkedUnits.add(new LinkedPurificationUnit(unit));
    }

    public void unregisterLinkedUnit(GT_MetaTileEntity_PurificationUnitBase<?> unit) {
        this.mLinkedUnits.removeIf(link -> link.metaTileEntity() == unit);
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
        // Show linked purification units and their status
        ret.add("Linked Purification Units: ");
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            String text = EnumChatFormatting.AQUA + unit.metaTileEntity()
                .getLocalName() + ": ";
            PurificationUnitStatus status = unit.metaTileEntity()
                .status();
            switch (status) {
                case ONLINE -> {
                    text = text + EnumChatFormatting.GREEN + "Online";
                }
                case DISABLED -> {
                    text = text + EnumChatFormatting.YELLOW + "Disabled";
                }
                case INCOMPLETE_STRUCTURE -> {
                    text = text + EnumChatFormatting.RED + "Incomplete Structure";
                }
            }
            ret.add(text);
        }
        return ret.toArray(new String[0]);
    }

    @Override
    public void onBlockDestroyed() {
        // When the controller is destroyed we want to notify all currently linked units
        for (LinkedPurificationUnit unit : this.mLinkedUnits) {
            unit.metaTileEntity()
                .unlinkController();
        }
        super.onBlockDestroyed();
    }

    private void drawTopText(DynamicPositionedColumn screenElements) {
        screenElements.setSynced(false)
            .setSpace(0)
            .setPos(10, 8);

        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("138", "Incomplete Structure.")).setDefaultColor(EnumChatFormatting.RED)
                    .setEnabled(widget -> !mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val));

        screenElements.widget(
            new TextWidget("Hit with Soft Mallet to start.").setDefaultColor(EnumChatFormatting.BLACK)
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && !getBaseMetaTileEntity().isActive()))
            .widget(
                new FakeSyncWidget.IntegerSyncer(
                    () -> getBaseMetaTileEntity().getErrorDisplayID(),
                    val -> getBaseMetaTileEntity().setErrorDisplayID(val)))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> getBaseMetaTileEntity().isActive(),
                    val -> getBaseMetaTileEntity().setActive(val)));
        screenElements.widget(
            new TextWidget(GT_Utility.trans("142", "Running perfectly.")).setDefaultColor(EnumChatFormatting.GREEN)
                .setEnabled(
                    widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0 && getBaseMetaTileEntity().isActive()));
        screenElements.widget(
            TextWidget.dynamicString(
                () -> getBaseMetaTileEntity().getLastShutDownReason()
                    .getDisplayString())
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(
                    widget -> shouldDisplayShutDownReason() && !getBaseMetaTileEntity().isActive()
                        && GT_Utility.isStringValid(
                            getBaseMetaTileEntity().getLastShutDownReason()
                                .getDisplayString())
                        && getBaseMetaTileEntity().wasShutdown()))
            .widget(
                new ShutDownReasonSyncer(
                    () -> getBaseMetaTileEntity().getLastShutDownReason(),
                    reason -> getBaseMetaTileEntity().setShutDownReason(reason)))
            .widget(
                new FakeSyncWidget.BooleanSyncer(
                    () -> getBaseMetaTileEntity().wasShutdown(),
                    wasShutDown -> getBaseMetaTileEntity().setShutdownStatus(wasShutDown)));
        screenElements.widget(
            TextWidget.dynamicString(this::generateCurrentRecipeInfoString)
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(widget -> (mMaxProgresstime > 0)))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> mProgresstime, val -> mProgresstime = val))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> mMaxProgresstime, val -> mMaxProgresstime = val));
    }

    private final int STATUS_WINDOW_ID = 10;

    private ModularWindow createStatusWindow(final EntityPlayer player) {
        final int windowWidth = 260;
        final int windowHeight = 200;
        ModularWindow.Builder builder = ModularWindow.builder(windowWidth, windowHeight);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(windowWidth - 15, 3));

        // Title widget
        builder.widget(
            new TextWidget(EnumChatFormatting.BOLD + "Purification Unit Status").setTextAlignment(Alignment.Center)
                .setPos(5, 10)
                .setSize(windowWidth, 8));

        int currentYPosition = 20;
        Scrollable mainDisp = new Scrollable().setVerticalScroll();

        int rowHeight = 20;
        for (int i = 0; i < this.mLinkedUnits.size(); i++) {
            mainDisp.widget(makeUnitStatusWidget(mLinkedUnits.get(i)).setPos(0, rowHeight * (i + 1)));
        }

        builder.widget(
            mainDisp.setPos(5, currentYPosition)
                .setSize(windowWidth - 10, windowHeight - currentYPosition - 5));
        return builder.build();
    }

    private Widget makeStatusWindowButton() {
        TextButtonWidget widget = (TextButtonWidget) new TextButtonWidget("Status").setLeftMargin(4)
            .setSize(40, 16)
            .setPos(10, 40);
        widget.button()
            .setOnClick(
                (clickData, w) -> {
                    if (!w.isClient()) w.getContext()
                        .openSyncedWindow(STATUS_WINDOW_ID);
                })
            .setBackground(GT_UITextures.BUTTON_STANDARD);
        widget.text()
            .setTextAlignment(Alignment.CenterLeft)
            .setDefaultColor(EnumChatFormatting.BLACK);
        return widget;
    }

    private Widget makeUnitStatusWidget(LinkedPurificationUnit unit) {
        // Draw small machine controller icon
        DynamicPositionedRow row = new DynamicPositionedRow();
        ItemStackHandler machineIcon = new ItemStackHandler(1);
        machineIcon.setStackInSlot(
            0,
            unit.metaTileEntity()
                .getStackForm(1));

        row.widget(
            SlotWidget.phantom(machineIcon, 0)
                .disableInteraction()
                .setPos(0, 0))
            .setSize(20, 20);

        // Display machine name and status
        String name = unit.metaTileEntity()
            .getLocalName();

        row.widget(
            TextWidget.dynamicString(() -> name + "  " + unit.getStatusString())
                .setSynced(false)
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(25, 0)
                .setSize(0, 20))
            .widget(new FakeSyncWidget.StringSyncer(() -> name, _name -> {}))
            .widget(
                unit.metaTileEntity()
                    .makeSyncerWidgets())
            .widget(new FakeSyncWidget.BooleanSyncer(unit::isActive, unit::setActive));;

        return row;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {

        buildContext.addSyncedWindow(STATUS_WINDOW_ID, this::createStatusWindow);

        // Draw basic recipe info
        final DynamicPositionedColumn controlTextArea = new DynamicPositionedColumn();
        drawTopText(controlTextArea);
        builder.widget(controlTextArea);

        // Draw line separator
        builder.widget(
            new Rectangle().setColor(Color.rgb(114, 120, 139))
                .asWidget()
                .setSizeProvider((screenSize, window, parent) -> new Size(window.getSize().width - 8, 2))
                .setPos(3, 32));

        // Add status window button
        builder.widget(makeStatusWindowButton());

        // Add parallel count number input

        builder.widget(createPowerSwitchButton(builder));

        // Add value syncers, note that we do this here so
        // everything is updated once the status gui opens
        addSyncers(builder);
    }

    private void addSyncers(ModularWindow.Builder builder) {
        // Sync connection list to client
        builder.widget(new FakeSyncWidget.ListSyncer<>(() -> mLinkedUnits, links -> {
            mLinkedUnits.clear();
            mLinkedUnits.addAll(links);
        }, (buffer, link) -> {
            // Try to save link data to NBT, so we can reconstruct it on client
            try {
                buffer.writeNBTTagCompoundToBuffer(link.writeLinkDataToNBT());
            } catch (IOException e) {
                GT_Log.err.println(e.getCause());
            }
        }, buffer -> {
            // Try to load link data from NBT compound as constructed above.
            try {
                return new LinkedPurificationUnit(buffer.readNBTTagCompoundFromBuffer());
            } catch (IOException e) {
                GT_Log.err.println(e.getCause());
            }
            return null;
        }));
    }

    @Override
    protected ResourceLocation getActivitySoundLoop() {
        // Probably want to use this for the actual purification units and not the main controller,
        // but I want to see it in game for now.
        return SoundResource.GT_MACHINES_PURIFICATIONPLANT_LOOP.resourceLocation;
    }
}
