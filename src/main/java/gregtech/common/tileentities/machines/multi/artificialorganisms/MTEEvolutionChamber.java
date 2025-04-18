package gregtech.common.tileentities.machines.multi.artificialorganisms;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GTValues.AuthorFourIsTheNumber;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_EMPTY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BIOVAT_EMPTY_GLOW;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_ADDITION;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_CHECKMARK;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_EXPORT;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_INFO;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CategoryList;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
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
import gregtech.api.modularui2.GTGuis;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.api.objects.ArtificialOrganism.Trait;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.tileentities.machines.IDualInputHatch;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEEvolutionChamber extends MTEExtendedPowerMultiBlockBase<MTEEvolutionChamber>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEEvolutionChamber> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEvolutionChamber>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "BBB", "BAB", "BAB", "BAB", "B~B" }, { "BBB", "A A", "A A", "A A", "BBB" },
                { "BBB", "BAB", "BAB", "BAB", "BBB" } })
        .addElement(
            'B',
            ofChain(
                buildHatchAdder(MTEEvolutionChamber.class)
                    .atLeast(InputBus, Maintenance, Energy, InputHatch, SpecialHatchElement.BioOutput)
                    .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(0))
                    .dot(1)
                    .build(),
                onElementPass(
                    MTEEvolutionChamber::onCasingAdded,
                    StructureUtility.ofBlocksTiered(
                        MTEEvolutionChamber::getTierFromMeta,
                        ImmutableList.of(
                            Pair.of(GregTechAPI.sBlockCasings12, 0),
                            Pair.of(GregTechAPI.sBlockCasings12, 1),
                            Pair.of(GregTechAPI.sBlockCasings12, 2)),
                        -3,
                        MTEEvolutionChamber::setCasingTier,
                        MTEEvolutionChamber::getCasingTier))))
        .addElement('A', chainAllGlasses())
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

    public ArtificialOrganism currentSpecies = new ArtificialOrganism();

    private long powerUsage = 0;
    private FluidStack nutrientUsage;

    private int casingTier;
    private int maxAOs;

    private int status = 0;

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
        if (block != GregTechAPI.sBlockCasings12) return -1;
        if (metaID < 0 || metaID > 2) return -2;
        return metaID + 1;
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
        casingTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) casingTier;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEvolutionChamber(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        int casingMeta = Math.max(casingTier - 1, 0);
        if (side == aFacing) {
            rTexture = new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingMeta)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BIOVAT_EMPTY)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_BIOVAT_EMPTY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
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
            .addInfo("Maximum tank capacity is 500000 * casing tier")
            .addInfo(AuthorFourIsTheNumber)
            .addSeparator()
            .beginStructureBlock(3, 5, 3, true)
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

    private void updateTextures() {
        getBaseMetaTileEntity().issueTextureUpdate();
        int textureID = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, casingTier - 1);
        for (MTEHatch h : mInputBusses) h.updateTexture(textureID);
        for (MTEHatch h : mInputHatches) h.updateTexture(textureID);
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(textureID);
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(textureID);
        for (MTEHatch h : mEnergyHatches) h.updateTexture(textureID);
        for (MTEHatch h : bioHatches) h.updateTexture(textureID);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        casingTier = -3;
        bioHatches.clear();
        mEnergyHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 4, 0)) return false;
        if (casingTier < 1) return false;
        updateTextures();
        maxAOs = 500000 * casingTier;
        return mCasingAmount >= 0;
    }

    private boolean useNutrients(FluidStack fluid) {
        if (fluid == null) {
            return false;
        }

        for (MTEHatchInput hatch : mInputHatches) {
            if (drain(hatch, fluid, true)) {
                return true;
            }
        }
        return false;
    }

    private void triggerNutrientLoss() {
        currentSpecies.consumeAOs(currentSpecies.getCount() / 4);
    }

    private void triggerElectricityLoss() {
        currentSpecies.increaseSentience(1);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (!aBaseMetaTileEntity.isServerSide() || aTick % 20 != 0 || currentSpecies == null || !finalizedSpecies)
            return;

        if (status == 1) {

            return;
        }

        if (currentSpecies.photosynthetic) {
            if (!aBaseMetaTileEntity.getSkyAtSideAndDistance(ForgeDirection.UP, 5)) {
                triggerElectricityLoss();
                triggerNutrientLoss();
            }
        }

        if (currentSpecies.cooperative) currentSpecies.increaseSentience(1);

        if (!drainEnergyInput(powerUsage)) triggerElectricityLoss();

        if (!useNutrients(nutrientUsage)) {
            triggerNutrientLoss();
        } else if (currentSpecies.getCount() < maxAOs) currentSpecies.doReproduction();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        currentSpecies = new ArtificialOrganism(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(currentSpecies.saveAOToCompound(aNBT));
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
            if (aMetaTileEntity instanceof MTEHatchAOOutput hatch) {
                if (currentSpecies != null) hatch.setSpecies(currentSpecies);
                return bioHatches.add(hatch);
            }
        }
        return false;
    }

    Boolean finalizedSpecies = false;

    private void createNewAOs() {

        // Generate the nutrient cost for this species

        int amount = 16;
        Fluid type = Materials.NutrientBroth.mFluid;

        if (currentSpecies.immortal) amount = 0;
        else {
            if (currentSpecies.hiveMind) {
                type = Materials.NeuralFluid.mFluid;
                amount /= 4;
            }
            if (currentSpecies.photosynthetic) amount /= 4;
            if (currentSpecies.cancerous) amount *= 64;
        }

        nutrientUsage = new FluidStack(type, amount);

        finalizedSpecies = true;
        currentSpecies.finalize(maxAOs);
        for (MTEHatchAOOutput hatch : bioHatches) hatch.setSpecies(currentSpecies);
    }

    // TODO: consider whether this should be disabled. for now, since i've overwritten the gui, it is less confusing
    @Override
    public boolean shouldCheckMaintenance() {
        return false;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("casingTier", Math.max(0, casingTier));
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

    private boolean isValidCulture(ItemStack input) {
        return ArtificialOrganism.getTraitFromItem(input) != null;
    }

    private boolean canAddTrait() {
        return !currentSpecies.getFinalized() && currentSpecies.traits.size() < casingTier;
    }

    // UI Pit of Doom
    // I've tried to comment what individual components are...

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.ORGANIC;
    }

    private static final UITexture intIcon = UITexture.builder()
        .location(GregTech.ID, "gui/picture/icon_intelligence")
        .imageSize(10, 10)
        .build();
    private static final UITexture strIcon = UITexture.builder()
        .location(GregTech.ID, "gui/picture/icon_strength")
        .imageSize(10, 10)
        .build();
    private static final UITexture repIcon = UITexture.builder()
        .location(GregTech.ID, "gui/picture/icon_reproduction")
        .imageSize(10, 10)
        .build();

    private ModularPanel getInfoPopup() {
        ModularPanel popup = new ModularPanel("info_panel").size(176, 166)
            .pos(232, 86);

        ListWidget<IWidget, CategoryList.Root> list = new ListWidget<>();
        list.size(168, 158);
        list.pos(4, 4);

        list.child(
            new TextWidget(
                EnumChatFormatting.UNDERLINE
                    + StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelintro.header")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelintro.1")));

        list.child(
            new TextWidget(
                EnumChatFormatting.UNDERLINE
                    + StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.header")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.1")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.2")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.3")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.4")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.5")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.6")));

        list.child(
            new TextWidget(
                EnumChatFormatting.UNDERLINE
                    + StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelnetworks.header")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelnetworks.1")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelnetworks.2")));

        popup.child(list);
        return popup;
    }

    private ModularPanel getTraitPopup() {
        // This list is the scrollable element that contains each trait's individual ui
        ListWidget<IWidget, CategoryList.Root> list = new ListWidget<>();
        list.size(92, 158);
        list.pos(4, 4);

        // The actual panel
        ModularPanel popup = new ModularPanel("trait_listing").size(100, 166)
            .pos(132, 86)
            .child(list);

        // Iterate through all the traits to generate a ui for each one within the list
        for (Trait t : ArtificialOrganism.Trait.values()) {
            ItemStack fakeItem = t.cultureItem;

            // The icon of the culture's item, with tooltip naming it
            list.child(
                new Row().height(16)
                    .childPadding(2)
                    .child(
                        new ItemDrawable(fakeItem).asWidget()
                            .size(12, 12)
                            .addTooltipElement(
                                StatCollector.translateToLocalFormatted(
                                    "GT5U.artificialorganisms.addculture",
                                    fakeItem.getDisplayName(),
                                    EnumChatFormatting.AQUA + StatCollector.translateToLocal(t.nameLocKey))))
                    .child(
                        IKey.str(EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal(t.nameLocKey))
                            .asWidget()));

            // Stat icons and text with the trait's value for each stat, aligned row-wise
            list.child(
                new Row().height(10)
                    .childPadding(1)
                    .child(
                        intIcon.asWidget()
                            .size(10, 10)
                            .addTooltipStringLines(
                                ImmutableList.of(
                                    EnumChatFormatting.UNDERLINE
                                        + StatCollector.translateToLocal("GT5U.artificialorganisms.intelligence"),
                                    StatCollector.translateToLocal("GT5U.artificialorganisms.intelligencedesc"))))
                    .child(
                        IKey.str(Integer.toString(t.baseInt))
                            .asWidget()
                            .width(14)
                            .alignment(Alignment.Center))
                    .child(
                        strIcon.asWidget()
                            .size(10, 10)
                            .addTooltipStringLines(
                                ImmutableList.of(
                                    EnumChatFormatting.UNDERLINE
                                        + StatCollector.translateToLocal("GT5U.artificialorganisms.strength"),
                                    StatCollector.translateToLocal("GT5U.artificialorganisms.strengthdesc"))))
                    .child(
                        IKey.str(Integer.toString(t.baseStr))
                            .asWidget()
                            .width(14)
                            .alignment(Alignment.Center))
                    .child(
                        repIcon.asWidget()
                            .size(10, 10)
                            .addTooltipStringLines(
                                ImmutableList.of(
                                    EnumChatFormatting.UNDERLINE
                                        + StatCollector.translateToLocal("GT5U.artificialorganisms.reproduction"),
                                    StatCollector.translateToLocal("GT5U.artificialorganisms.reproductiondesc"))))
                    .child(
                        IKey.str(Integer.toString(t.baseRep))
                            .asWidget()
                            .width(14)
                            .alignment(Alignment.Center))

                    // Add the unique trait icon and get the Trait's descLocKey as a tooltip
                    .child(
                        UITexture.builder()
                            .location(GregTech.ID, "gui/picture/artificial_organisms/trait_" + t.id)
                            .imageSize(10, 10)
                            .build()
                            .asWidget()
                            .size(10, 10)
                            .addTooltipStringLines(
                                ImmutableList.of(
                                    EnumChatFormatting.UNDERLINE + "Trait",
                                    StatCollector.translateToLocal(t.descLocKey)))));
        }

        return popup;
    }

    // I do not understand these interfaces, I do not understand if this is a reasonable thing to do
    // But I want my 1-item limited slot handler
    private static class LimitingItemStackHandler extends ItemStackHandler
        implements IItemHandlerModifiable, IItemHandler {

        private final int slotLimit;

        private LimitingItemStackHandler(int slots, int slotLimit) {
            super(slots);
            this.slotLimit = slotLimit;
        }

        @Override
        public int getSlotLimit(int slot) {
            return slotLimit;
        }
    }

    LimitingItemStackHandler limitedHandler = new LimitingItemStackHandler(1, 1);

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager) {

        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, data, syncManager)
            .build();
        GenericSyncValue<ArtificialOrganism> organismSyncer = new GenericSyncValue<ArtificialOrganism>(
            () -> currentSpecies,
            ao -> { currentSpecies = ao; },
            new ArtificialOrganismAdapter());
        syncManager.syncValue("ao", organismSyncer);
        // This row displays the currently active traits
        Row traitRow = new Row();
        traitRow.pos(5, 41)
            .size(50, 10)
            .childPadding(6);

        // The popup panel which shows trait details
        IPanelHandler traitPanel = syncManager
            .panel("trait_listing", (p_syncManager, syncHandler) -> getTraitPopup(), true);
        // The "tutorial" popup panel
        IPanelHandler infoPanel = syncManager.panel("info_panel", (p_syncManager, syncHandler) -> getInfoPopup(), true);

        // Inventory slot handler
        syncManager.registerSlotGroup("culture_slot", 1);

        // Defining a bunch of textures. TODO: define this in GTUITextures!
        UITexture progressBar = UITexture.builder()
            .location(GregTech.ID, "gui/progressbar/sentience_progress")
            .adaptable(1)
            .imageSize(16, 128)
            .build();

        UITexture intProgressBar = UITexture.builder()
            .location(GregTech.ID, "gui/progressbar/intelligence_bar")
            .adaptable(1)
            .imageSize(32, 16)
            .build();

        UITexture strProgressBar = UITexture.builder()
            .location(GregTech.ID, "gui/progressbar/strength_bar")
            .adaptable(1)
            .imageSize(32, 16)
            .build();

        UITexture repProgressBar = UITexture.builder()
            .location(GregTech.ID, "gui/progressbar/reproduction_bar")
            .adaptable(1)
            .imageSize(32, 16)
            .build();

        // Sentience progressbar
        panel.child(
            new ProgressWidget().value(new DoubleSyncValue(() -> (double) currentSpecies.getSentience() / 100))
                .texture(progressBar, 16)
                .direction(ProgressWidget.Direction.UP)
                .size(16, 64)
                .pos(100, 0))

            // The actual itemslot for inserting cultures
            .child(
                new ItemSlot().pos(7, 60)
                    .slot(
                        new ModularSlot(limitedHandler, 0).slotGroup("culture_slot")
                            .ignoreMaxStackSize(true)
                            .filter(this::isValidCulture))
                    .setEnabledIf(ignored -> canAddTrait())
                    .size(16, 16))
            // This is the "insert item" button
            .child(
                new ButtonWidget<>().pos(27, 61)
                    .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                        ItemStack is = limitedHandler.getStackInSlot(0);
                        if (is != null && canAddTrait()) {
                            Trait t = ArtificialOrganism.getTraitFromItem(is);
                            if (t == null) return;

                            limitedHandler.extractItem(0, 1, false);

                            currentSpecies.addTrait(t);
                            traitRow.child(
                                UITexture.builder()
                                    .location(GregTech.ID, "gui/picture/artificial_organisms/trait_" + t.id)
                                    .imageSize(10, 10)
                                    .build()
                                    .asWidget()
                                    .size(10, 10)
                                    .background()
                                    .addTooltipStringLines(
                                        ImmutableList.of(
                                            EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal(t.nameLocKey),
                                            StatCollector.translateToLocal(t.descLocKey))));

                            if (syncManager.isClient()) {
                                WidgetTree.resize(panel);
                            }
                        }
                    }))
                    .overlay(OVERLAY_BUTTON_ADDITION)
                    .addTooltipLine(StatCollector.translateToLocal("GT5U.artificialorganisms.button.addculture"))
                    .size(16, 16)
                    .setEnabledIf(ignored -> canAddTrait()))
            // This button finalizes the aos, preventing further modification and allowing user to add primordial soup
            .child(
                new ButtonWidget<>().pos(45, 61)
                    .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> createNewAOs()))
                    .overlay(OVERLAY_BUTTON_CHECKMARK)
                    .addTooltipLine(StatCollector.translateToLocal("GT5U.artificialorganisms.button.finalize"))
                    .size(16, 16)
                    .setEnabledIf(ignored -> !currentSpecies.getFinalized()))
            // Opens the trait list popup
            .child(
                new ButtonWidget<>().pos(-20, 61)
                    .syncHandler(new InteractionSyncHandler().setOnMousePressed(ignored -> traitPanel.openPanel()))
                    .overlay(OVERLAY_BUTTON_EXPORT)
                    .size(16, 16)
                    .addTooltipLine(StatCollector.translateToLocal("GT5U.artificialorganisms.button.traitlist")))
            .child(
                new ButtonWidget<>().pos(-20, 44)
                    .syncHandler(new InteractionSyncHandler().setOnMousePressed(ignored -> infoPanel.openPanel()))
                    .overlay(OVERLAY_BUTTON_INFO)
                    .size(16, 16)
                    .addTooltipLine(StatCollector.translateToLocal("GT5U.artificialorganisms.button.info")))

            // Progress bars for the three primary stats
            .child(
                new ProgressWidget()
                    .value(
                        new DoubleSyncValue(() -> ((double) currentSpecies.getIntelligence() / 32) + ((double) 1 / 32)))
                    .texture(intProgressBar, 16)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .hoverOverlay(
                        IKey.dynamic(
                            () -> EnumChatFormatting.WHITE
                                + Integer
                                    .toString(new IntSyncValue(() -> currentSpecies.getIntelligence()).getIntValue())
                                + "/30")
                            .alignment(Alignment.BottomCenter)
                            .shadow(true)
                            .scale(0.8F)
                            .asIcon()
                            .margin(0, 0))
                    .size(32, 8)
                    .pos(16, 6))
            .child(
                new ProgressWidget()
                    .value(new DoubleSyncValue(() -> ((double) currentSpecies.getStrength() / 32) + ((double) 1 / 32)))
                    .texture(strProgressBar, 16)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .hoverOverlay(
                        IKey.dynamic(
                            () -> EnumChatFormatting.WHITE
                                + Integer.toString(new IntSyncValue(() -> currentSpecies.getStrength()).getIntValue())
                                + "/30")
                            .alignment(Alignment.BottomCenter)
                            .shadow(true)
                            .scale(0.8F)
                            .asIcon()
                            .margin(0, 0))
                    .size(32, 8)
                    .pos(16, 18))
            .child(
                new ProgressWidget()
                    .value(
                        new DoubleSyncValue(
                            () -> ((double) currentSpecies.getReproduction() / 32) + ((double) 1 / 32),
                            ignored -> {}))
                    .texture(repProgressBar, 16)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .hoverOverlay(
                        IKey.dynamic(
                            () -> EnumChatFormatting.WHITE
                                + Integer
                                    .toString(new IntSyncValue(() -> currentSpecies.getReproduction()).getIntValue())
                                + "/30")
                            .alignment(Alignment.BottomCenter)
                            .shadow(true)
                            .scale(0.8F)
                            .asIcon()
                            .margin(0, 0))
                    .size(32, 8)
                    .pos(16, 30))

            // Description icons for the primary stats
            .child(
                intIcon.asWidget()
                    .pos(5, 5)
                    .size(10, 10)
                    .addTooltipStringLines(
                        ImmutableList.of(
                            EnumChatFormatting.UNDERLINE
                                + StatCollector.translateToLocal("GT5U.artificialorganisms.intelligence"),
                            StatCollector.translateToLocal("GT5U.artificialorganisms.intelligencedesc"))))
            .child(
                strIcon.asWidget()
                    .pos(5, 17)
                    .size(10, 10)
                    .addTooltipStringLines(
                        ImmutableList.of(
                            EnumChatFormatting.UNDERLINE
                                + StatCollector.translateToLocal("GT5U.artificialorganisms.strength"),
                            StatCollector.translateToLocal("GT5U.artificialorganisms.strengthdesc"))))
            .child(
                repIcon.asWidget()
                    .pos(5, 29)
                    .size(10, 10)
                    .addTooltipStringLines(
                        ImmutableList.of(
                            EnumChatFormatting.UNDERLINE
                                + StatCollector.translateToLocal("GT5U.artificialorganisms.reproduction"),
                            StatCollector.translateToLocal("GT5U.artificialorganisms.reproductiondesc"))));

        // Render the trait icons for traits previously added
        organismSyncer.setChangeListener(() -> {
            traitRow.getChildren()
                .clear();

            for (Trait t : currentSpecies.traits) {
                traitRow.child(
                    UITexture.builder()
                        .location(GregTech.ID, "gui/picture/artificial_organisms/trait_" + t.id)
                        .imageSize(10, 10)
                        .build()
                        .asWidget()
                        .size(10, 10)
                        .background()
                        .addTooltipStringLines(
                            ImmutableList.of(
                                EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal(t.nameLocKey),
                                StatCollector.translateToLocal(t.descLocKey))));
            }
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(traitRow);
            }
        });

        panel.child(traitRow);
        return panel;
    }

    private void writeTraitID(PacketBuffer buf, Trait t) {
        buf.writeInt(t.ordinal());
    }

    private static class ArtificialOrganismAdapter implements IByteBufAdapter<ArtificialOrganism> {

        @Override
        public void serialize(PacketBuffer buffer, ArtificialOrganism organism) {
            buffer.writeInt(organism.getIntelligence());
            buffer.writeInt(organism.getStrength());
            buffer.writeInt(organism.getReproduction());
            buffer.writeInt(organism.getCount());
            buffer.writeInt(organism.getSentience());
            buffer.writeBoolean(organism.getFinalized());

            buffer.writeInt(organism.traits.size());
            organism.traits.forEach(trait -> {
                String traitString = trait.toString();
                buffer.writeInt(traitString.length());
                for (int i = 0; i < traitString.length(); i++) {
                    buffer.writeChar(traitString.charAt(i));
                }
            });
        }

        @Override
        public boolean areEqual(@NotNull ArtificialOrganism t1, @NotNull ArtificialOrganism t2) {
            return t1.equals(t2);
        }

        @Override
        public ArtificialOrganism deserialize(PacketBuffer buffer) {
            ArtificialOrganism result = new ArtificialOrganism();
            result.setIntelligence(buffer.readInt());
            result.setStrength(buffer.readInt());
            result.setReproduction(buffer.readInt());
            result.setCount(buffer.readInt());
            result.setSentience(buffer.readInt());
            result.setFinalized(buffer.readBoolean());

            int traitCount = buffer.readInt();
            for (int i = 0; i < traitCount; i++) {
                int strLength = buffer.readInt();
                StringBuilder traitString = new StringBuilder();
                for (int j = 0; j < strLength; j++) {
                    traitString.append(buffer.readChar());
                }
                result.addTrait(Trait.valueOf(traitString.toString()), true);
            }
            return result;
        }
    }
}
