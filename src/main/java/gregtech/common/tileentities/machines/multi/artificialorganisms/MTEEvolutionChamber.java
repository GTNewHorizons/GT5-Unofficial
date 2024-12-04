package gregtech.common.tileentities.machines.multi.artificialorganisms;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GTValues.AuthorFourIsTheNumber;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.DynamicTextWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.api.objects.ArtificialOrganism.Trait;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.tileentities.machines.multi.artificialorganisms.hatches.MTEHatchBioOutput;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class MTEEvolutionChamber extends MTEExtendedPowerMultiBlockBase<MTEEvolutionChamber>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEEvolutionChamber> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEvolutionChamber>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "BBB", "AAA", "B~B" }, { "BBB", "A A", "BBB" }, { "BBB", "AAA", "BBB" } })
        .addElement(
            'B',
            ofChain(
                buildHatchAdder(MTEEvolutionChamber.class).adder(MTEEvolutionChamber::addBioHatch)
                    .hatchClass(MTEHatchBioOutput.class)
                    .shouldReject(t -> !(t.bioHatch == null))
                    .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                    .dot(2)
                    .buildAndChain(
                        onElementPass(MTEEvolutionChamber::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings2, 0))),
                buildHatchAdder(MTEEvolutionChamber.class)
                    .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch, OutputHatch)
                    .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                    .dot(1)
                    .buildAndChain(
                        onElementPass(MTEEvolutionChamber::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings2, 0)))))
        .addElement('A', Glasses.chainAllGlasses())
        .build();

    MTEHatchBioOutput bioHatch;
    ArtificialOrganism currentSpecies;
    private int intelligence;
    private int strength;
    private int count;
    private int sentience;

    private int maxAOs;

    public MTEEvolutionChamber(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEEvolutionChamber(String aName) {
        super(aName);
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEvolutionChamber(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Artificial Organism Creator")
            .addInfo("Controller Block for the Hyperaccelerated Macroevolution Chamber")
            .addInfo("Used to create and maintain Artificial Organisms")
            .addInfo(AuthorFourIsTheNumber)
            .addSeparator()
            .beginStructureBlock(7, 5, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 85, false)
            .addCasingInfoExactly("Steel Pipe Casing", 24, false)
            .addInputBus("Any Solid Steel Casing", 1)
            .addOutputBus("Any Solid Steel Casing", 1)
            .addInputHatch("Any Solid Steel Casing", 1)
            .addOutputHatch("Any Solid Steel Casing", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        bioHatch = null;
        mEnergyHatches.clear();
        maxAOs = 50000;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0)) return false;
        return mCasingAmount >= 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (!aBaseMetaTileEntity.isServerSide() || aTick % 20 != 0 || currentSpecies == null) return;

        if (currentSpecies.getCount() < maxAOs) currentSpecies.doReproduction();
        updateSpecies();
    }

    private void updateSpecies() {
        intelligence = currentSpecies.getIntelligence();
        strength = currentSpecies.getStrength();
        count = currentSpecies.getCount();
        sentience = currentSpecies.getSentience();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
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

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    private boolean addBioHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchBioOutput) {
                ((MTEHatchBioOutput) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                if (bioHatch == null) {
                    bioHatch = (MTEHatchBioOutput) aMetaTileEntity;
                    return true;
                }
            }
        }
        return false;
    }

    private void createNewAOs(Trait trait) {
        currentSpecies = new ArtificialOrganism(trait.baseInt, trait.baseStr, trait.baseRep, 100, 0);
        if (bioHatch != null) bioHatch.currentSpecies = currentSpecies;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (bioHatch != null) {
            if (bioHatch.pipenetwork != null) PlayerUtils.messagePlayer(aPlayer, bioHatch.pipenetwork.toString());
        }
    }

    // UI Pit of Doom

    protected ItemStackHandler inputSlotHandler = new ItemStackHandler(1);
    private static final int TRAIT_WINDOW_ID = 9;

    Trait activeTraitWindow;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(24, 4)
                .setSize(170, 85));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        screenElements.setSynced(false)
            .setSpace(0)
            .setPos(34, 7);
        screenElements.widget(new TextWidget("Current Species: ").setDefaultColor(COLOR_TEXT_WHITE.get()));
        screenElements.widget(
            new DynamicTextWidget(() -> new Text("Intelligence: " + intelligence)).setSynced(false)
                .setDefaultColor(COLOR_TEXT_WHITE.get()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> intelligence, val -> intelligence = val))
            .widget(
                new DynamicTextWidget(() -> new Text("Strength: " + strength)).setSynced(false)
                    .setDefaultColor(COLOR_TEXT_WHITE.get()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> strength, val -> strength = val))
            .widget(
                new DynamicTextWidget(() -> new Text("Count: " + count)).setSynced(false)
                    .setDefaultColor(COLOR_TEXT_WHITE.get()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> count, val -> count = val))
            .widget(
                new DynamicTextWidget(() -> new Text("Sentience: " + sentience)).setSynced(false)
                    .setDefaultColor(COLOR_TEXT_WHITE.get()))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> sentience, val -> sentience = val));
        // screenElements.setEnabled(widget -> currentSpecies != null);

        builder.widget(createPurgeButton(builder));
        builder.widget(createSentienceBar(builder));
        builder.widget(createTraitWindowButton(builder, Trait.Photosynthetic, new Pos2d(4, 4)));
        builder.widget(createTraitWindowButton(builder, Trait.HiveMind, new Pos2d(4, 20)));
        builder.widget(createTraitWindowButton(builder, Trait.Laborer, new Pos2d(4, 36)));
        builder.widget(createTraitWindowButton(builder, Trait.Decaying, new Pos2d(4, 52)));
        builder.widget(screenElements);

        // Windows
        buildContext.addSyncedWindow(TRAIT_WINDOW_ID, this::createTraitWindow);
    }

    private Widget createSentienceBar(IWidgetBuilder<?> builder) {
        return new ProgressBar().setProgress(() -> (float) sentience / 100)
            .setDirection(ProgressBar.Direction.UP)
            .setTexture(GTUITextures.PROGRESSBAR_SENTIENCE, 64)
            .setSynced(true, false)
            .setSize(16, 64)
            .setPos(173, 120);
    }

    private ModularWindow createTraitWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(getGUIWidth(), getGUIHeight())
            .setDraggable(false);

        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(24, 4)
                .setSize(170, 85))
            .widget(createTraitItemWidget(new ItemStack(activeTraitWindow.cultureItem, 1)).setPos(95, 50))
            .widget(createBuildAOsButton().setPos(95, 66))
            .widget(
                new DynamicPositionedColumn().setSynced(false)
                    .setSpace(0)
                    .setPos(34, 7));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        screenElements.setSynced(false)
            .setSpace(0)
            .setPos(34, 7);
        screenElements.widget(
            new TextWidget(
                EnumChatFormatting.UNDERLINE
                    + StatCollector.translateToLocal("GT5U.artificialorganisms.traitname" + activeTraitWindow.id))
                        .setDefaultColor(COLOR_TEXT_WHITE.get())
                        .setTextAlignment(Alignment.Center));
        screenElements.widget(
            new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.traitdesc" + activeTraitWindow.id))
                .setDefaultColor(COLOR_TEXT_WHITE.get()));
        builder.widget(screenElements);

        return builder.build();
    }

    private ButtonWidget createTraitWindowButton(IWidgetBuilder<?> builder, Trait trait, Pos2d pos) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            activeTraitWindow = trait;
            if (!widget.isClient()) widget.getContext()
                .openSyncedWindow(TRAIT_WINDOW_ID);
        })
            .setPlayClickSound(supportsVoidProtection())
            .setBackground(
                () -> new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                    new ItemDrawable(new ItemStack(trait.cultureItem)) })
            .addTooltip(StatCollector.translateToLocal("GT5U.artificialorganisms.traitname" + trait.id))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(pos)
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    private ButtonWidget createPurgeButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            intelligence = 0;
            strength = 0;
            count = 0;
            sentience = 0;
            currentSpecies = null;
        })
            .setPlayClickSound(supportsVoidProtection())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(getVoidingMode().buttonTexture);
                ret.add(getVoidingMode().buttonOverlay);
                if (!supportsVoidProtection()) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip("Purge tank")
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(26, 91)
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    private ButtonWidget createBuildAOsButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                ItemStack inputItem = inputSlotHandler.getStackInSlot(0);
                if (inputItem == null) return;
                if (inputItem.getItem() == activeTraitWindow.cultureItem) {
                    inputItem.stackSize -= 1;
                    createNewAOs(activeTraitWindow);
                }
            }
        })
            .setPlayClickSound(supportsVoidProtection())
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(getVoidingMode().buttonTexture);
                ret.add(getVoidingMode().buttonOverlay);
                if (!supportsVoidProtection()) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_FORBIDDEN);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip("Create Population")
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    public Widget createTraitItemWidget(final ItemStack costStack) {
        // Item slot
        ItemStack handlerStack = costStack.copy();
        handlerStack.stackSize = 1;
        return new SlotWidget(inputSlotHandler, 0).setAccess(true, true)
            .setFilter((stack) -> stack.getItem() == costStack.getItem())
            .setRenderStackSize(false)
            .setPos(26, 91)
            .setSize(16, 16)
            .setBackground(() -> new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED, new ItemDrawable(costStack) });
    }
}
