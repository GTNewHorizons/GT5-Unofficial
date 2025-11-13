package gregtech.common.tileentities.machines.multi.gui.nanochip;

import static gregtech.api.modularui2.GTGuis.createPopUpPanel;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.serialization.ByteBufAdapters;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.modularui2.widget.TerminalWidget;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gtPlusPlus.core.util.math.MathUtils;

public class MTENanochipAssemblyComplexGui extends MTEMultiBlockBaseGui {

    private final MTENanochipAssemblyComplex base;

    public MTENanochipAssemblyComplexGui(MTENanochipAssemblyComplex base) {
        super(base);
        this.base = base;
    }

    @Override
    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel ui = super.build(data, syncManager, uiSettings);
        IPanelHandler popupPanel = syncManager.panel("popup", (m, h) -> createGREGOSPanel(syncManager), true);

        return ui.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            if (!popupPanel.isPanelOpen()) {
                popupPanel.openPanel();
            } else {
                popupPanel.closePanel();
            }
            return true;
        })
            .background(GTGuiTextures.BUTTON_STANDARD, GuiTextures.GEAR)
            .disableHoverBackground()
            .tooltip(tooltip -> tooltip.add("Open GREGOS manager"))
            .pos(156, 102)
            .size(18, 18));
    }

    public ModularPanel createGREGOSPanel(PanelSyncManager syncManager) {
        ModularPanel ui = createPopUpPanel("gt:nac:gregos", false, false).size(176, 136);

        // TODO: figure out if we should save the conversation to nbt and stuff or not
        GenericListSyncHandler<String> conversationHandler = new GenericListSyncHandler<>(
            () -> base.gregosConversation,
            ByteBufAdapters.STRING);
        DoubleSyncValue moodSyncer = new DoubleSyncValue(() -> base.gregosMood, dub -> base.gregosMood = dub);
        DoubleSyncValue efficiencySyncer = new DoubleSyncValue(() -> base.efficiency, dub -> base.efficiency = dub);
        DoubleSyncValue speedSyncer = new DoubleSyncValue(() -> base.moduleSpeed, dub -> base.moduleSpeed = dub);

        syncManager.syncValue("conversationSyncer", conversationHandler);
        syncManager.syncValue("Mood", moodSyncer);
        syncManager.syncValue("Efficiency", efficiencySyncer);
        syncManager.syncValue("Speed", speedSyncer);

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);

        ListWidget<IWidget, ?> messages = new ListWidget<>();
        messages.childSeparator(IIcon.EMPTY_2PX);
        messages.pos(2, 2);
        messages.sizeRel(1, 1);
        messages.scrollDirection(GuiAxis.Y);

        ui.child(
            new Row().coverChildren()
                .topRel(0f, 4, 1f)
                .child(new PageButton(0, tabController).tab(GuiTextures.TAB_TOP, 0))
                .child(new PageButton(1, tabController).tab(GuiTextures.TAB_TOP, 0)));

        ParentWidget<?> talkPage = new ParentWidget<>()
            .child(
                new TerminalWidget().setResponseSupplier(this::getGREGOSResponse)
                    .pos(12, 12)
                    .size(152, 112)
                    .build())
            .sizeRel(1);
        ParentWidget<?> infoPage = new ParentWidget<>()
            // TODO: ask #texture-dev to make better textures that dont clash with eachother
            // Mood
            .child(
                GTGuiTextures.PICTURE_BRAIN.asWidget()
                    .pos(25, 75)
                    .size(15, 13))
            // Speed
            .child(
                GTGuiTextures.PICTURE_ELECRICITY.asWidget()
                    .pos(78, 75)
                    .size(11, 15))
            // Efficiency
            .child(
                new ItemDrawable(new ItemStack(Items.iron_ingot, 1)).asWidget()
                    .pos(125, 72)
                    .size(16, 16))
            .sizeRel(1);
        createMeter(infoPage, moodSyncer, GTGuiTextures.PROGRESSBAR_METER_MINT, 0, 0, 1);
        createMeter(infoPage, speedSyncer, GTGuiTextures.PROGRESSBAR_METER_ORANGE, 1, 0.1, 1);
        createMeter(infoPage, efficiencySyncer, GTGuiTextures.PROGRESSBAR_METER_ROSE, 2, 1, 1.25);

        return ui.child(
            pagedWidget.addPage(infoPage)
                .addPage(talkPage)
                .sizeRel(1))
            .posRel(0.75F, 0.5F);
    }

    public void createMeter(ParentWidget<?> page, DoubleSyncValue syncer, UITexture meterTexture, int index,
        double valueMin, double valueMax) {
        int xOffset = index * 50;
        String key = syncer.getKey();
        // Chop off the last 2 characters, these are normally ":0" to represent the id for the syncer.
        String name = key.substring(0, key.length() - 2);
        char end = valueMin == 0D && valueMax == 1D ? '%' : 'x';
        page.child(
            new ProgressWidget().progress(() -> (syncer.getValue() - valueMin) / (valueMax - valueMin))
                .texture(meterTexture, 48)
                .direction(ProgressWidget.Direction.UP)
                .pos(25 + xOffset, 20)
                .size(16, 48))
            .child(
                new TextWidget(name).alignment(Alignment.Center)
                    .pos(1 + xOffset, 5)
                    .size(64, 8))
            // For debug
            .child(
                new TextFieldWidget().setNumbersDouble(val -> Math.min(valueMax, Math.max(valueMin, val)))
                    .value(new StringSyncValue(syncer::getStringValue, syncer::setStringValue))
                    .pos(18 + xOffset, 105)
                    .size(30, 12));
        IWidget meterContainer = GTGuiTextures.PICTURE_DECAY_TIME_CONTAINER.asWidget()
            .tooltipDynamic(tooltip -> tooltip.add(name + ": " + syncer.getStringValue() + end))
            .pos(21 + xOffset, 16)
            .size(24, 56);
        syncer.setChangeListener(meterContainer::markTooltipDirty);
        page.child(meterContainer);
    }

    public String getGREGOSResponse(String currentText) {
        return switch (currentText.toLowerCase()) {
            case "hi" -> "Hello.";
            case "gregos" -> "It seems you have asked about NAC's advanced sentient artificial intelligence. This is "
                + "an artificial intelligence designed to simulate the player's otherwise inimitably rad typing "
                + "style, tone, cadence, personality, and substance of retort while they are using the NAC. The "
                + "algorithms are guaranteed to be 92% indistinguishable from the players' native neurological "
                + "responses, based on some statistical analysis I basically just pulled out of my ass right now.";
            case "xyzzy" -> "Nothing happens.";
            case "be the other guy" -> "I am now mDiyoOS.";
            case "open the doors" -> "I'm sorry Player, I'm afraid I can't do that";
            case "d" -> "n";
            case "how fast are you" -> "2fast2quick";
            case "steel" -> "Dun dun dunnnnnn I finally got it now steel";
            case "copypasta" -> switch (MathUtils.randInt(1, 6)) {
                    case 1 -> "Is it just me or does this pack actually seem really easy and not that hard..? People "
                        + "give it a bad rap but I've only been playing for a little while and I've already mined enough "
                        + "to make the big versions of the steam block things. They seem kinda busted because they just "
                        + "take a solar boiler and they run forever... I think there's a bug with the pipes or u can only "
                        + "have them be so long or something because sometimes they'll have a hiccup and I have to restart it, weird.";
                    case 2 -> "Who the fuck came up with that idea of using Dimensionally Shifted Superfluid as ingredient "
                        + "for UIV / UMV / UXV Components ? I would understand it if there was a reliable way of making the "
                        + "Stabilized Baryonic Matter but no there isnt - To make that SBM you need to use the same amount of "
                        + "it to produce it or dump shitloads of ressources into an overexpensive recipe for 250ml of it each and "
                        + "produce Unaligned Quark Releasing Housing as a waste product you cant do anything with ... Did i oversee "
                        + "something ? Seriously WTF is this recipe for Empty Quark  Release Catalyst Housing ? The Circuits used in "
                        + "there make UIV+ component crafting a bottleneck hell man Sry for the roast but i dont know if you played "
                        + "much in UIV+ Tier but this is super unbalanced and annoying. More complexity in recipes is perfectly fine "
                        + "but more grindy / tedious / bottlenecking (inefficient) recipes are just bad tbh.";
                    case 3 -> "wow im surprised. girls dont usually play hardcore modpacks like gtnh. how old are you if you dont mind me asking?";
                    case 4 -> "The issue I am having is not making power, the issue i am having is i am using to much power, with "
                        + "everything end tier, that is all recipies the last you can make. What I do not like at this stage "
                        + "(when i was using Shirabon, i was kinda surprised at the power, i had a big headroom to have my base "
                        + "run full tilt, with 1400 aals, with 64amp umv hatches and all that, no issue. Once i went MHDSCM, making "
                        + "eternity, mk vi fuel, that all need raw stellar plasma, as well as tachyon with the final recipe "
                        + "(not the forge hammer recipe) also using that, headroom i enjoyed with Shirabon, did not reappear, "
                        + "even adding 4 extra ssass with the same infrastructure, no hatches upgraded since going from 3 to 4,  "
                        + "only thing i was forced to upgrade was the eohs to make more raw stellar and universium, everything else "
                        + "i have not touched, and still my power that is free to run all machines has not changed i kinda want to "
                        + "switch tachyon and spacial over to the forge hammer again to see how big a dent that does";
                    case 5 -> "is it eliteist if i sorta look down on anyone with a role icon below luv? like theyre like little babies or something. uneducated peasentry.";
                    case 6 -> "stargate people are all snarky assholes about shit. lol. you all cheated your way to gate and were carried by others. i'm not sure why you act this way ";
                    default -> "Looks like you understand nothing.";
                };
            default -> switch (MathUtils.randInt(1, 10)) {
                    case 1 -> "It is certain";
                    case 2 -> "It is decidedly so";
                    case 3 -> "Without a doubt";
                    case 4 -> "Yes definitely";
                    case 5 -> "You may rely on it";
                    case 6 -> "Don't count on it";
                    case 7 -> "My reply is no";
                    case 8 -> "My sources say no";
                    case 9 -> "Outlook not so good";
                    case 10 -> "Very doubtful";
                    default -> "what the hell man";
                };
        };
    }
}
