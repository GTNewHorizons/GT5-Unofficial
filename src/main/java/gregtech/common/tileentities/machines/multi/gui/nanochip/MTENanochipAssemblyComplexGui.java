package gregtech.common.tileentities.machines.multi.gui.nanochip;

import java.awt.*;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widget.scroll.ScrollData;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gtPlusPlus.core.util.math.MathUtils;

public class MTENanochipAssemblyComplexGui extends MTEMultiBlockBaseGui {

    private final MTENanochipAssemblyComplex base;
    protected boolean isTalkModeActive = false;
    protected TerminalTextListWidget textList = new TerminalTextListWidget();

    String fieldHintTalk = "Type 'talk' to enter talk mode";
    String fieldHintExit = "Type 'exit' to exit talk mode";

    public MTENanochipAssemblyComplexGui(MTENanochipAssemblyComplex base) {
        super(base);
        this.base = base;
    }

    // TODO: this implementation kinda super sucks. once things in basegui are more solid we should refactor this method
    // TODO: there a bit so this is a little easier to do
    @Override
    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow row = super.createTerminalRow(panel, syncManager);
        List<IWidget> aunts = row.getChildren();
        // what a terrible way to add to the parent background widget
        if (!aunts.isEmpty() && (aunts.get(0) instanceof ParentWidget<?>parent)) {
            List<IWidget> sisters = parent.getChildren();
            // what a terrible way to change the logo widget
            if (!sisters.isEmpty() && (sisters.get(1) instanceof SingleChildWidget<?>logo)) {
                logo.setEnabledIf(a -> !isTalkModeActive);
            }
            Dimension mainTerminalDimensions = getTerminalDimensions();
            textList.setEnabledIf(a -> isTalkModeActive)
                .childSeparator(
                    IDrawable.EMPTY.asIcon()
                        .height(2))
                .size(mainTerminalDimensions.width - 10, mainTerminalDimensions.height - 8);
            parent.child(textList);

        }
        return row.child(createGREGOSMeterPages(panel, syncManager));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        return super.createTerminalTextWidget(syncManager).setEnabledIf(flow -> !isTalkModeActive);
    }

    @Override
    protected IWidget createPanelGap(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap)
            .child(createTalkTextField(panel, syncManager))
            .childIf(base.supportsPowerPanel(), createPowerPanelButton(syncManager, panel));
    }

    public IWidget createTalkTextField(ModularPanel panel, PanelSyncManager syncManager) {
        Dimension mainTerminalDimensions = super.getTerminalDimensions();
        return new TerminalTextFieldWidget(textList).hintText(fieldHintTalk)
            .setFocusOnGuiOpen(true)
            .size(mainTerminalDimensions.width - 27, 10);
    }

    @Override
    protected Dimension getTerminalDimensions() {
        Dimension base = super.getTerminalDimensions();
        return new Dimension(base.width - getMeterViewerDimensions().width, base.height);
    }

    public Dimension getMeterViewerDimensions() {
        Dimension base = super.getTerminalDimensions();
        return new Dimension(56, base.height);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("talk", new BooleanSyncValue(() -> isTalkModeActive, b -> isTalkModeActive = b));
        syncManager.syncValue("mood", 0, new DoubleSyncValue(() -> base.gregosMood, dub -> base.gregosMood = dub));
        syncManager.syncValue("eff", 0, new DoubleSyncValue(() -> base.efficiency, dub -> base.efficiency = dub));
        syncManager.syncValue("speed", 0, new DoubleSyncValue(() -> base.moduleSpeed, dub -> base.moduleSpeed = dub));
    }

    public IWidget createGREGOSMeterPages(ModularPanel panel, PanelSyncManager syncManager) {
        Dimension area = getMeterViewerDimensions();

        DoubleSyncValue moodSyncer = (DoubleSyncValue) syncManager.getSyncHandler("mood:0");
        DoubleSyncValue effSyncer = (DoubleSyncValue) syncManager.getSyncHandler("eff:0");
        DoubleSyncValue speedSyncer = (DoubleSyncValue) syncManager.getSyncHandler("speed:0");

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);

        return new ParentWidget<>()
            // preventative comment so spotless doesnt move child call up
            .child(
                new Column().coverChildren()
                    .rightRel(0f, 0, 1f)
                    .child(
                        new PageButton(0, tabController).tab(GuiTextures.TAB_RIGHT, -1)
                            .overlay(
                                GTGuiTextures.PICTURE_BRAIN.asIcon()
                                    .size(15, 13)))
                    .child(
                        new PageButton(1, tabController).tab(GuiTextures.TAB_RIGHT, 0)
                            .overlay(
                                GTGuiTextures.PICTURE_ELECRICITY.asIcon()
                                    .size(11, 15)))
                    .child(
                        new PageButton(2, tabController).tab(GuiTextures.TAB_RIGHT, 0)
                            .overlay(new ItemDrawable(new ItemStack(Items.iron_ingot, 1)).asIcon())))
            .child(
                pagedWidget
                    // preventative comment so spotless doesnt move addPage call up
                    .addPage(createMeter("Mood", moodSyncer, GTGuiTextures.PROGRESSBAR_METER_ROSE, 0, 1))
                    .addPage(createMeter("Speed", speedSyncer, GTGuiTextures.PROGRESSBAR_METER_ORANGE, 0.1, 1))
                    .addPage(createMeter("Efficiency", effSyncer, GTGuiTextures.PROGRESSBAR_METER_MINT, 1, 1.25)))
            .size(area.width, area.height);
    }

    public ParentWidget<?> createMeter(String name, DoubleSyncValue syncer, UITexture meter, double min, double max) {
        boolean isPercentage = (min == 0D && max == 1D);
        char end = isPercentage ? '%' : 'x';
        return new ParentWidget<>().child(
            new ProgressWidget().progress(() -> (syncer.getValue() - min) / (max - min))
                .texture(meter, 48)
                .direction(ProgressWidget.Direction.UP)
                .pos(20, 20)
                .size(16, 48))
            .child(
                new TextWidget(name).alignment(Alignment.Center)
                    .pos(0, 5)
                    .size(56, 8))
            .child(
                GTGuiTextures.PICTURE_DECAY_TIME_CONTAINER.asWidget()
                    .tooltipDynamic(tooltip -> {
                        double amount = isPercentage ? syncer.getDoubleValue() * 100 : syncer.getDoubleValue();
                        tooltip.add(name + ": " + amount + end);
                    })
                    .tooltipAutoUpdate(true)
                    .pos(16, 16)
                    .size(24, 56))
            // Un-comment for debug purposes
            // .child(
            // new TextFieldWidget().setNumbersDouble(val -> Math.min(max, Math.max(min, val)))
            // .value(new StringSyncValue(syncer::getStringValue, syncer::setStringValue))
            // .pos(12, 70)
            // .size(30, 12))
            .sizeRel(1);
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
            case "knock knock" -> "Who's there?";
            case "why did the chicken cross the road" -> switch (MathUtils.randInt(1, 10)) {
                    case 1 -> "To get to the other side?";
                    case 2 -> "Because it was too far to walk around?";
                    default -> "I dont know. Why?";
                };
            case "copypasta" -> switch (MathUtils.randInt(1, 6)) {
                    case 1 -> "Is it just me or does this pack actually seem really easy and not that hard..? People give it a bad rap but I've only been playing for a little while and I've already mined enough to make the big versions of the steam block things. They seem kinda busted because they just take a solar boiler and they run forever... I think there's a bug with the pipes or u can only have them be so long or something because sometimes they'll have a hiccup and I have to restart it, weird.";
                    case 2 -> "Who the fuck came up with that idea of using Dimensionally Shifted Superfluid as ingredient for UIV / UMV / UXV Components ? I would understand it if there was a reliable way of making the Stabilized Baryonic Matter but no there isnt - To make that SBM you need to use the same amount of it to produce it or dump shitloads of ressources into an overexpensive recipe for 250ml of it each and produce Unaligned Quark Releasing Housing as a waste product you cant do anything with ... Did i oversee something ? Seriously WTF is this recipe for Empty Quark  Release Catalyst Housing ? The Circuits used in there make UIV+ component crafting a bottleneck hell man Sry for the roast but i dont know if you played much in UIV+ Tier but this is super unbalanced and annoying. More complexity in recipes is perfectly fine but more grindy / tedious / bottlenecking (inefficient) recipes are just bad tbh.";
                    case 3 -> "wow im surprised. girls dont usually play hardcore modpacks like gtnh. how old are you if you dont mind me asking?";
                    case 4 -> "The issue I am having is not making power, the issue i am having is i am using to much power, with everything end tier, that is all recipies the last you can make. What I do not like at this stage (when i was using Shirabon, i was kinda surprised at the power, i had a big headroom to have my base run full tilt, with 1400 aals, with 64amp umv hatches and all that, no issue. Once i went MHDSCM, making eternity, mk vi fuel, that all need raw stellar plasma, as well as tachyon with the final recipe (not the forge hammer recipe) also using that, headroom i enjoyed with Shirabon, did not reappear, even adding 4 extra ssass with the same infrastructure, no hatches upgraded since going from 3 to 4,  only thing i was forced to upgrade was the eohs to make more raw stellar and universium, everything else i have not touched, and still my power that is free to run all machines has not changed i kinda want to switch tachyon and spacial over to the forge hammer again to see how big a dent that does";
                    case 5 -> "is it eliteist if i sorta look down on anyone with a role icon below luv? like theyre like little babies or something. uneducated peasentry.";
                    case 6 -> "stargate people are all snarky assholes about shit. lol. you all cheated your way to gate and were carried by others. i'm not sure why you act this way ";
                    default -> throw new IllegalStateException("uh oh, thats no good!");
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

    public class TerminalTextFieldWidget extends TextFieldWidget {

        TerminalTextListWidget parentList;

        public TerminalTextFieldWidget(TerminalTextListWidget parent) {
            super();
            parentList = parent;
        }

        @Override
        public @NotNull Result onKeyPressed(char character, int keyCode) {
            if (keyCode == Keyboard.KEY_RETURN) {
                String text = this.getText();
                if (text.isEmpty()) return Result.IGNORE;
                // Reset the text box to be blank
                this.handler.clear();
                if (!checkForKeywords(text) && isTalkModeActive) {
                    // base.gregosMood = Math.min(1, base.gregosMood + 0.05);
                    parentList.child(createPlayerTextWidget(text));
                    parentList.child(createResponseTextWidget(getGREGOSResponse(text)));
                }
                return Result.SUCCESS;
            } else return super.onKeyPressed(character, keyCode);
        }

        public boolean checkForKeywords(String text) {
            return switch (text.toLowerCase()) {
                case "talk" -> {
                    List<IWidget> texts = parentList.getChildren();
                    if (texts.isEmpty()) parentList.child(createGenericTextWidget("Ask GREGOS a question"));
                    this.hintText(fieldHintExit);
                    isTalkModeActive = true;
                    yield true;
                }
                case "exit" -> {
                    this.hintText(fieldHintTalk);
                    isTalkModeActive = false;
                    yield true;
                }
                default -> false;
            };
        }

        public TextWidget createResponseTextWidget(String text) {
            return new TextWidget(text).color(parentList.responseTextColor);
        }

        public TextWidget createPlayerTextWidget(String text) {
            return new TextWidget(text) {

                @Override
                public int getDefaultWidth() {
                    return Math.max(super.getDefaultWidth(), getParentArea().width - 4);
                }
            }.alignment(Alignment.CenterRight)
                .color(parentList.playerTextColor);
        }

        public TextWidget createGenericTextWidget(String text) {
            return new TextWidget(text) {

                @Override
                public int getDefaultWidth() {
                    return Math.max(super.getDefaultWidth(), getParentArea().width - 4);
                }
            }.alignment(Alignment.Center)
                .color(parentList.genericTextColor);
        }
    }

    public class TerminalTextListWidget extends ListWidget<IWidget, TerminalTextListWidget> {

        public int playerTextColor = Color.WHITE.main;
        public int responseTextColor = Color.CYAN.main;
        public int genericTextColor = Color.LIME.main;

        public TerminalTextListWidget() {
            super();
        }

        @Override
        public void onChildAdd(IWidget child) {
            super.onChildAdd(child);
            WidgetTree.resize(this.getParent());
            ScrollData data = this.getScrollData();
            // Scroll to the bottom
            data.scrollTo(getScrollArea(), data.getScrollSize());
        }
    }

}
