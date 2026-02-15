// WHAT????
package gregtech.common.gui.modularui.multiblock;

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
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CategoryList;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.google.common.collect.ImmutableList;

import gregtech.api.modularui2.GTGuis;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.artificialorganisms.MTEEvolutionChamber;

public class MTEEvolutionChamberGui extends MTEMultiBlockBaseGui<MTEEvolutionChamber> {

    public MTEEvolutionChamberGui(MTEEvolutionChamber base) {
        super(base);
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
            createInfoTextWidget("GT5U.artificialorganisms.infopanelintro.header", EnumChatFormatting.UNDERLINE));
        list.child(new TextWidget<>(""));
        list.child(createInfoTextWidget("GT5U.artificialorganisms.infopanelintro.1", null));
        list.child(new TextWidget<>(""));
        list.child(createInfoTextWidget("GT5U.artificialorganisms.infopanelhmc.header", EnumChatFormatting.UNDERLINE));
        list.child(new TextWidget<>(""));
        list.child(createInfoTextWidget("GT5U.artificialorganisms.infopanelhmc.1", null));
        list.child(createInfoTextWidget("GT5U.artificialorganisms.infopanelhmc.2", null));
        list.child(createInfoTextWidget("GT5U.artificialorganisms.infopanelhmc.3", null));
        list.child(createInfoTextWidget("GT5U.artificialorganisms.infopanelhmc.4", null));
        list.child(createInfoTextWidget("GT5U.artificialorganisms.infopanelhmc.5", null));
        list.child(createInfoTextWidget("GT5U.artificialorganisms.infopanelhmc.6", null));
        list.child(new TextWidget<>(""));
        list.child(
            createInfoTextWidget("GT5U.artificialorganisms.infopanelnetworks.header", EnumChatFormatting.UNDERLINE));
        list.child(new TextWidget<>(""));
        list.child(createInfoTextWidget(" GT5U.artificialorganisms.infopanelnetworks.1", null));
        list.child(createInfoTextWidget(" GT5U.artificialorganisms.infopanelnetworks.2", null));
        popup.child(list);
        return popup;
    }

    private final int maxTextWidgetWidth = 168 - 4;

    private TextWidget<?> createInfoTextWidget(String langkey, EnumChatFormatting specifier) {

        return new TextWidget<>((specifier == null ? "" : specifier) + StatCollector.translateToLocal(langkey))
            .maxWidth(maxTextWidgetWidth);
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
                        t.texture.asWidget()
                            .size(10, 10)
                            .addTooltipStringLines(
                                ImmutableList.of(
                                    EnumChatFormatting.UNDERLINE + "Trait",
                                    StatCollector.translateToLocal(t.descLocKey)))));
        }

        return popup;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.registerSlotGroup("culture_slot", 1);

        GenericSyncValue<ArtificialOrganism> organismSyncer = GenericSyncValue.builder(ArtificialOrganism.class)
            .getter(() -> multiblock.currentSpecies)
            .setter(ao -> multiblock.currentSpecies = ao)
            .adapter(new ArtificialOrganismAdapter())
            .build();
        syncManager.syncValue("ao", organismSyncer);

        // AO Count syncers
        IntSyncValue aoCapacitySyncer = new IntSyncValue(() -> multiblock.maxAOs);
        syncManager.syncValue("aoCapacity", aoCapacitySyncer);

        // Nutrient syncers
        IntSyncValue fillLevelSyncer = new IntSyncValue(multiblock::getFillLevel);
        syncManager.syncValue("fillLevel", fillLevelSyncer);
        IntSyncValue internalTankCapacitySyncer = new IntSyncValue(() -> multiblock.INTERNAL_FLUID_TANK_SIZE);
        syncManager.syncValue("internalTankCapacity", internalTankCapacitySyncer);

    }

    @SuppressWarnings("unchecked")
    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(multiblock, guiData, syncManager, uiSettings)
            .build();

        // This row displays the currently active traits
        // TODO: change members in the trait row to dynamic drawawbles or adjacent, stuttering bad
        Row traitRow = new Row();
        traitRow.pos(5, 41)
            .size(50, 10)
            .childPadding(6);

        // The popup panel which shows trait details
        IPanelHandler traitPanel = syncManager
            .syncedPanel("trait_listing", true, (p_syncManager, syncHandler) -> getTraitPopup());
        // The "tutorial" popup panel
        IPanelHandler infoPanel = syncManager
            .syncedPanel("info_panel", true, (p_syncManager, syncHandler) -> getInfoPopup());

        // AO count Syncers
        GenericSyncValue<ArtificialOrganism> organismSyncer = syncManager.findSyncHandler("ao", GenericSyncValue.class);
        IntSyncValue aoCapacitySyncer = syncManager.findSyncHandler("aoCapacity", IntSyncValue.class);
        for (ArtificialOrganism.Trait t : multiblock.currentSpecies.traits) {
            traitRow.child(
                new DynamicDrawable(() -> t.texture)

                    .asWidget()
                    .size(10, 10)
                    .background()
                    .addTooltipStringLines(
                        ImmutableList.of(
                            EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal(t.nameLocKey),
                            StatCollector.translateToLocal(t.descLocKey))));
        }
        // Nutrient Syncers
        IntSyncValue fillLevelSyncer = syncManager.findSyncHandler("fillLevel", IntSyncValue.class);
        IntSyncValue internalTankCapacitySyncer = syncManager
            .findSyncHandler("internalTankCapacity", IntSyncValue.class);

        panel
            // AO count progressbar
            .child(
                new ProgressWidget().value(
                    new DoubleSyncValue(
                        () -> (double) organismSyncer.getValue()
                            .getCount() / aoCapacitySyncer.getIntValue()))
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
                                aoCapacitySyncer.getIntValue()))))

            // Nutrient progressbar
            .child(
                new ProgressWidget()
                    .value(
                        new DoubleSyncValue(
                            () -> (double) fillLevelSyncer.getIntValue() / internalTankCapacitySyncer.getIntValue()))
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
                                fillLevelSyncer.getIntValue(),
                                internalTankCapacitySyncer.getIntValue()))))

            // Sentience progressbar
            .child(
                new ProgressWidget().value(
                    new DoubleSyncValue(
                        () -> (double) organismSyncer.getValue()
                            .getSentience() / 100))
                    .texture(AO_PROGRESS_SENTIENCE, 32)
                    .direction(ProgressWidget.Direction.UP)
                    .size(32, 32)
                    .pos(135, 14)
                    .tooltipDynamic(tt -> {
                        tt.add(
                            StatCollector.translateToLocalFormatted(
                                "GT5U.artificialorganisms.progress.sentience",
                                organismSyncer.getValue()
                                    .getSentience()));
                    }))

            // The actual itemslot for inserting cultures
            // todo: little cute mushroom icon please - chrom
            .child(
                new ItemSlot().pos(7, 60)
                    .slot(
                        new ModularSlot(multiblock.limitedHandler, 0).slotGroup("culture_slot")
                            .ignoreMaxStackSize(true)
                            .filter(multiblock::isValidCulture))
                    .setEnabledIf(ignored -> multiblock.mMachine && multiblock.canAddTrait())
                    .size(16, 16))

            // This is the "insert item" button
            .child(
                new ButtonWidget<>().pos(27, 61)
                    .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                        ItemStack is = multiblock.limitedHandler.getStackInSlot(0);
                        if (is != null && multiblock.canAddTrait()) {
                            ArtificialOrganism.Trait t = ArtificialOrganism.getTraitFromItem(is);
                            if (t == null) return;

                            multiblock.limitedHandler.extractItem(0, 1, false);

                            multiblock.currentSpecies.addTrait(t);
                            traitRow.child(
                                new DynamicDrawable(() -> t.texture)

                                    .asWidget()
                                    .size(10, 10)
                                    .background()
                                    .addTooltipStringLines(
                                        ImmutableList.of(
                                            EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal(t.nameLocKey),
                                            StatCollector.translateToLocal(t.descLocKey))));

                            if (syncManager.isClient()) {
                                panel.scheduleResize();
                            }
                        }
                    }))
                    .overlay(OVERLAY_BUTTON_ADDITION)
                    .addTooltipLine(StatCollector.translateToLocal("GT5U.artificialorganisms.button.addculture"))
                    .size(16, 16)
                    .setEnabledIf(ignored -> multiblock.mMachine && multiblock.canAddTrait()))
            // these are currently enabled with mMachine, if you want to change this just remove it - chrom

            // This button finalizes the aos, preventing further modification and allowing user to add primordial soup
            .child(
                new ButtonWidget<>().pos(45, 61)
                    .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> multiblock.createNewAOs()))
                    .overlay(OVERLAY_BUTTON_CHECKMARK)
                    .addTooltipLine(StatCollector.translateToLocal("GT5U.artificialorganisms.button.finalize"))
                    .size(16, 16)
                    .setEnabledIf(ignored -> multiblock.mMachine && !multiblock.currentSpecies.getFinalized()))

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
                new ProgressWidget().value(
                    new DoubleSyncValue(
                        () -> ((double) organismSyncer.getValue()
                            .getIntelligence() / 32) + ((double) 1 / 32)))
                    .texture(AO_PROGRESS_INT, 16)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .hoverOverlay(
                        IKey.dynamic(
                            () -> EnumChatFormatting.WHITE + Integer.toString(
                                new IntSyncValue(
                                    () -> organismSyncer.getValue()
                                        .getIntelligence()).getIntValue())
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
                    new DoubleSyncValue(
                        () -> ((double) organismSyncer.getValue()
                            .getStrength() / 32) + ((double) 1 / 32)))
                    .texture(AO_PROGRESS_STR, 16)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .hoverOverlay(
                        IKey.dynamic(
                            () -> EnumChatFormatting.WHITE + Integer.toString(
                                new IntSyncValue(
                                    () -> organismSyncer.getValue()
                                        .getStrength()).getIntValue())
                                + "/30")
                            .alignment(Alignment.BottomCenter)
                            .shadow(true)
                            .scale(0.8F)
                            .asIcon()
                            .margin(0, 0))
                    .size(32, 8)
                    .pos(16, 18))
            .child(
                new ProgressWidget().value(
                    new DoubleSyncValue(
                        () -> ((double) organismSyncer.getValue()
                            .getReproduction() / 32) + ((double) 1 / 32),
                        ignored -> {}))
                    .texture(AO_PROGRESS_REP, 16)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .hoverOverlay(
                        IKey.dynamic(
                            () -> EnumChatFormatting.WHITE + Integer.toString(
                                new IntSyncValue(
                                    () -> organismSyncer.getValue()
                                        .getReproduction()).getIntValue())
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
