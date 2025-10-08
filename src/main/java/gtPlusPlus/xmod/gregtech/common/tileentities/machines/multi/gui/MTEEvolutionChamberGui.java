package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.gui;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.modularui2.GTGuiTextures.AO_PROGRESS_COUNT;
import static gregtech.api.modularui2.GTGuiTextures.AO_PROGRESS_INT;
import static gregtech.api.modularui2.GTGuiTextures.AO_PROGRESS_NUTRIENTS;
import static gregtech.api.modularui2.GTGuiTextures.AO_PROGRESS_REP;
import static gregtech.api.modularui2.GTGuiTextures.AO_PROGRESS_SENTIENCE;
import static gregtech.api.modularui2.GTGuiTextures.AO_PROGRESS_STR;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_ADDITION;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_CHECKMARK;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_EXPORT;
import static gregtech.api.modularui2.GTGuiTextures.OVERLAY_BUTTON_INFO;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
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
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.common.tileentities.machines.multi.artificialorganisms.MTEEvolutionChamber;

public class MTEEvolutionChamberGui extends MTEMultiBlockBaseGui {

    MTEEvolutionChamber base;

    public MTEEvolutionChamberGui(MTEMultiBlockBase base) {
        super(base);
        this.base = (MTEEvolutionChamber) base;
    }

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
        list.child(new TextWidget(""));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelintro.1")));

        list.child(new TextWidget(""));
        list.child(
            new TextWidget(
                EnumChatFormatting.UNDERLINE
                    + StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.header")));
        list.child(new TextWidget(""));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.1")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.2")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.3")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.4")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.5")));
        list.child(new TextWidget(StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelhmc.6")));

        list.child(new TextWidget(""));
        list.child(
            new TextWidget(
                EnumChatFormatting.UNDERLINE
                    + StatCollector.translateToLocal("GT5U.artificialorganisms.infopanelnetworks.header")));
        list.child(new TextWidget(""));
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

        // Iterate through all the traits to generate a ui for each one
        for (ArtificialOrganism.Trait t : ArtificialOrganism.Trait.values()) {
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

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(base, guiData, syncManager, uiSettings)
            .build();
        GenericSyncValue<ArtificialOrganism> organismSyncer = new GenericSyncValue<ArtificialOrganism>(
            () -> base.currentSpecies,
            ao -> { base.currentSpecies = ao; },
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

        panel
            // AO count progressbar
            .child(
                new ProgressWidget()
                    .value(new DoubleSyncValue(() -> (double) base.currentSpecies.getCount() / base.maxAOs))
                    .texture(AO_PROGRESS_COUNT, 16)
                    .direction(ProgressWidget.Direction.UP)
                    .size(16, 64)
                    .pos(100, 14)
                    .tooltipDynamic(
                        tt -> tt.add(
                            StatCollector.translateToLocalFormatted(
                                "GT5U.artificialorganisms.progress.count",
                                organismSyncer.getValue()
                                    .getCount(),
                                new IntSyncValue(() -> base.maxAOs).getIntValue()))))

            // Nutrient progressbar
            .child(
                new ProgressWidget()
                    .value(new DoubleSyncValue(() -> (double) base.getFillLevel() / base.INTERNAL_FLUID_TANK_SIZE))
                    .texture(AO_PROGRESS_NUTRIENTS, 16)
                    .direction(ProgressWidget.Direction.UP)
                    .size(16, 64)
                    .pos(117, 14)
                    .tooltipDynamic(
                        tt -> tt.add(
                            StatCollector.translateToLocalFormatted(
                                organismSyncer.getValue()
                                    .getFinalized() ? "GT5U.artificialorganisms.progress.nutrients"
                                        : "GT5U.artificialorganisms.progress.soup",
                                base.getFillLevel(),
                                base.INTERNAL_FLUID_TANK_SIZE))))

            // Sentience progressbar
            .child(
                new ProgressWidget().value(new DoubleSyncValue(() -> (double) base.currentSpecies.getSentience() / 100))
                    .texture(AO_PROGRESS_SENTIENCE, 32)
                    .direction(ProgressWidget.Direction.UP)
                    .size(32, 32)
                    .pos(135, 14)
                    .tooltipBuilder(tt -> {
                        tt.add(
                            StatCollector.translateToLocalFormatted(
                                "GT5U.artificialorganisms.progress.sentience",
                                organismSyncer.getValue()
                                    .getSentience()));
                        tt.markDirty();
                    }))

            // The actual itemslot for inserting cultures
            .child(
                new ItemSlot().pos(7, 60)
                    .slot(
                        new ModularSlot(limitedHandler, 0).slotGroup("culture_slot")
                            .ignoreMaxStackSize(true)
                            .filter(base::isValidCulture))
                    .setEnabledIf(ignored -> base.canAddTrait())
                    .size(16, 16))

            // This is the "insert item" button
            .child(
                new ButtonWidget<>().pos(27, 61)
                    .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                        ItemStack is = limitedHandler.getStackInSlot(0);
                        if (is != null && base.canAddTrait()) {
                            ArtificialOrganism.Trait t = ArtificialOrganism.getTraitFromItem(is);
                            if (t == null) return;

                            limitedHandler.extractItem(0, 1, false);

                            base.currentSpecies.addTrait(t);
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
                    .setEnabledIf(ignored -> base.canAddTrait()))

            // This button finalizes the aos, preventing further modification and allowing user to add primordial soup
            .child(
                new ButtonWidget<>().pos(45, 61)
                    .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> base.createNewAOs()))
                    .overlay(OVERLAY_BUTTON_CHECKMARK)
                    .addTooltipLine(StatCollector.translateToLocal("GT5U.artificialorganisms.button.finalize"))
                    .size(16, 16)
                    .setEnabledIf(ignored -> !base.currentSpecies.getFinalized()))

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
                        new DoubleSyncValue(
                            () -> ((double) base.currentSpecies.getIntelligence() / 32) + ((double) 1 / 32)))
                    .texture(AO_PROGRESS_INT, 16)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .hoverOverlay(
                        IKey.dynamic(
                            () -> EnumChatFormatting.WHITE
                                + Integer.toString(
                                    new IntSyncValue(() -> base.currentSpecies.getIntelligence()).getIntValue())
                                + "/30")
                            .alignment(Alignment.BottomCenter)
                            .shadow(true)
                            .scale(0.8F)
                            .asIcon()
                            .margin(0, 0))
                    .size(32, 8)
                    .pos(16, 6))
            .child(
                new ProgressWidget().value(
                    new DoubleSyncValue(() -> ((double) base.currentSpecies.getStrength() / 32) + ((double) 1 / 32)))
                    .texture(AO_PROGRESS_STR, 16)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .hoverOverlay(
                        IKey.dynamic(
                            () -> EnumChatFormatting.WHITE
                                + Integer
                                    .toString(new IntSyncValue(() -> base.currentSpecies.getStrength()).getIntValue())
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
                            () -> ((double) base.currentSpecies.getReproduction() / 32) + ((double) 1 / 32),
                            ignored -> {}))
                    .texture(AO_PROGRESS_REP, 16)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .hoverOverlay(
                        IKey.dynamic(
                            () -> EnumChatFormatting.WHITE
                                + Integer.toString(
                                    new IntSyncValue(() -> base.currentSpecies.getReproduction()).getIntValue())
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

            for (ArtificialOrganism.Trait t : base.currentSpecies.traits) {
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

    private void writeTraitID(PacketBuffer buf, ArtificialOrganism.Trait t) {
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
                result.addTrait(ArtificialOrganism.Trait.valueOf(traitString.toString()), true);
            }
            return result;
        }
    }
}
