package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.scroll.ScrollData;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MTENanochipAssemblyComplexGui extends MTEMultiBlockBaseGui<MTENanochipAssemblyComplex> {

    protected TerminalTextListWidget textList = new TerminalTextListWidget();

    String fieldHintTalk = "Type 'talk' to enter talk mode";
    String fieldHintExit = "Type 'exit' to exit talk mode";

    public MTENanochipAssemblyComplexGui(MTENanochipAssemblyComplex base) {
        super(base);
    }

    @Override
    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createTerminalRow(panel, syncManager).child(createGREGOSMeterPages(panel, syncManager));
    }

    @Override
    protected ParentWidget<?> createTerminalParentWidget(ModularPanel panel, PanelSyncManager syncManager) {
        textList.setEnabledIf(a -> multiblock.isTalkModeActive)
            .childSeparator(
                IDrawable.EMPTY.asIcon()
                    .height(2))
            .size(getTerminalWidgetWidth() - 6, getTerminalWidgetHeight() - 8);
        return super.createTerminalParentWidget(panel, syncManager).child(textList);
    }

    // disables hoverable in talk mode
    @Override
    protected boolean shouldShutdownReasonBeDisplayed(String shutdownString) {
        return super.shouldShutdownReasonBeDisplayed(shutdownString) && !multiblock.isTalkModeActive;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return super.createTerminalTextWidget(syncManager, parent).setEnabledIf(flow -> !multiblock.isTalkModeActive);
    }

    @Override
    protected Widget<? extends Widget<?>> makeLogoWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return super.makeLogoWidget(syncManager, parent).size(24)
            .setEnabledIf(a -> !multiblock.isTalkModeActive);
    }

    @Override
    protected Flow createPanelGap(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(getTextBoxToInventoryGap())
            .child(createTalkTextField(panel, syncManager));
    }

    public IWidget createTalkTextField(ModularPanel panel, PanelSyncManager syncManager) {
        return new TerminalTextFieldWidget(textList, syncManager).setFocusOnGuiOpen(true)
            .size(getTerminalRowWidth() - 27, 14);
    }

    @Override
    protected int getTerminalWidgetWidth() {
        return super.getTerminalWidgetWidth() - getMeterViewerWidth();
    }

    public int getMeterViewerWidth() {
        return 56;
    }

    public int getMeterViewerHeight() {
        return getTerminalWidgetHeight();
    }

    @Override
    protected int getMufflerPosFromRightOutwards() {
        return 12;
    }

    @Override
    protected int getMufflerPosFromTop() {
        return getTerminalRowHeight() - 6;
    }

    @Override
    protected ToggleButton createMuffleButton() {
        return super.createMuffleButton().disableHoverBackground();
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "talk",
            0,
            new BooleanSyncValue(() -> multiblock.isTalkModeActive, b -> multiblock.isTalkModeActive = b));
        syncManager
            .syncValue("mood", 0, new DoubleSyncValue(() -> multiblock.gregosMood, dub -> multiblock.gregosMood = dub));
        syncManager
            .syncValue("eff", 0, new DoubleSyncValue(() -> multiblock.efficiency, dub -> multiblock.efficiency = dub));
        syncManager.syncValue(
            "speed",
            0,
            new DoubleSyncValue(() -> multiblock.moduleSpeed, dub -> multiblock.moduleSpeed = dub));
    }

    public IWidget createGREGOSMeterPages(ModularPanel panel, PanelSyncManager syncManager) {

        DoubleSyncValue moodSyncer = syncManager.findSyncHandler("mood", DoubleSyncValue.class);
        DoubleSyncValue effSyncer = syncManager.findSyncHandler("eff", DoubleSyncValue.class);
        DoubleSyncValue speedSyncer = syncManager.findSyncHandler("speed", DoubleSyncValue.class);

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);

        return new ParentWidget<>()
            // preventative comment so spotless doesnt move child call up
            .child(
                new Column().coverChildren()
                    .rightRel(0f, 0, 1f)
                    .child(
                        new PageButton(0, tabController).excludeAreaInRecipeViewer()
                            .tab(GuiTextures.TAB_RIGHT, -1)
                            .overlay(
                                GTGuiTextures.PICTURE_BRAIN.asIcon()
                                    .size(15, 13)))
                    .child(
                        new PageButton(1, tabController).excludeAreaInRecipeViewer()
                            .tab(GuiTextures.TAB_RIGHT, 0)
                            .overlay(
                                GTGuiTextures.PICTURE_ELECRICITY.asIcon()
                                    .size(11, 15)))
                    .child(
                        new PageButton(2, tabController).excludeAreaInRecipeViewer()
                            .tab(GuiTextures.TAB_RIGHT, 0)
                            .overlay(new ItemDrawable(new ItemStack(Items.iron_ingot, 1)).asIcon())))
            .child(
                pagedWidget
                    // preventative comment so spotless doesnt move addPage call up
                    .addPage(createMeter("Mood", moodSyncer, GTGuiTextures.PROGRESSBAR_METER_ROSE, 0, 1))
                    .addPage(createMeter("Speed", speedSyncer, GTGuiTextures.PROGRESSBAR_METER_ORANGE, 0.1, 1))
                    .addPage(createMeter("Efficiency", effSyncer, GTGuiTextures.PROGRESSBAR_METER_MINT, 1, 1.25))
                    .sizeRel(1F))
            .size(getMeterViewerWidth(), getMeterViewerHeight());
    }

    public ParentWidget<?> createMeter(String name, DoubleSyncValue syncer, UITexture meter, double min, double max) {
        boolean isPercentage = (min == 0D && max == 1D);
        char end = isPercentage ? '%' : 'x';
        return new ParentWidget<>().child(
            new TextWidget<>(name).leftRel(0.5F)
                .top(5))
            .child(
                new ProgressWidget().progress(() -> (syncer.getValue() - min) / (max - min))
                    .texture(meter, 48)
                    .direction(ProgressWidget.Direction.UP)
                    .posRel(0.5F, 0.5F)
                    .size(16, 48))
            .child(
                GTGuiTextures.PICTURE_DECAY_TIME_CONTAINER.asWidget()
                    .tooltipDynamic(tooltip -> {
                        double amount = isPercentage ? syncer.getDoubleValue() * 100 : syncer.getDoubleValue();
                        tooltip.add(name + ": " + amount + end);
                    })
                    .tooltipAutoUpdate(true)
                    .posRel(0.5F, 0.5F)
                    .size(24, 56))
            // Un-comment for debug purposes
            // .child(
            // new TextFieldWidget().setNumbersDouble(val -> Math.min(max, Math.max(min, val)))
            // .value(new StringSyncValue(syncer::getStringValue, syncer::setStringValue))
            // .pos(12, 70)
            // .size(30, 12))
            .sizeRel(1);
    }

    List<String> NOptions = Arrays.asList("Not", "Never", "Now", "Nearly", "Nanochip", "Nearing", "Noc", "NAC", "NotAPenguin", "Nano", "Nine");
    List<String> AOptions = Arrays.asList("Abbydullah", "Apple", "Awesome", "Assembly", "Advanced", "Actually", "Always", "Almost", "About", "Auynonymous", "Alright", "Admin", "A", "Alkalus", "Alastor", "Applied", "Angry");
    List<String> COptions = Arrays.asList("Chrom", "Croup", "Complex", "Coming", "Casing", "Completed", "Circuit", "CAL", "Challenged", "Coded", "ChatGPT", "Cosmic", "Cloud", "GregTech... wait", "chochem");
    public String getGREGOSResponse(String currentText) {
        return switch (currentText.toLowerCase()) {
            case "help" -> "List of commands: contributors, gregos, copypasta, joke, what is nac, nac, clear";
            case "contributors" -> EnumChatFormatting.YELLOW + "Lead:\n"
                + EnumChatFormatting.RESET
                + " NotAPenguin\n"
                + EnumChatFormatting.YELLOW
                + "Programming:\n"
                + EnumChatFormatting.RESET
                + " JurreJelle\n FourIsTheNumber\n PureBluez\n TheEpicGamer274\n Nockyx\n Serenibyss\n Chrom\n SpicierSpace153\n"
                + EnumChatFormatting.YELLOW
                + "Textures:\n"
                + EnumChatFormatting.RESET
                + " June\n "
                + " Auynonymous\n"
                + EnumChatFormatting.YELLOW
                + "Idea:\n"
                + EnumChatFormatting.RESET
                + " Sampsa\n"
                + EnumChatFormatting.YELLOW
                + "Structure:\n"
                + EnumChatFormatting.RESET
                + " Deleno";
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
            case "joke" -> switch(MathUtils.randInt(1,10)) {
                case 1 -> "No time for jokes.";
                case 2 -> "A rolling golem gathers no rust.";
                case 3 -> "He was destroyed!";
                case 4 -> "A literal line... of asses...";
                case 5 -> "I miss when waterline was dogshit";
                case 6 -> "Waiter! Waiter! More lineslop please!";
                case 7 -> "Don't even joke, lad.";
                default -> "lol what im a robot dawg wtf do i know";
            };
            case "why did the chicken cross the road" -> switch (MathUtils.randInt(1, 10)) {
                    case 1 -> "To get to the other side?";
                    case 2 -> "Because it was too far to walk around?";
                    default -> "I dont know. Why?";
                };
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
            case "what is nac", "nac" -> {
                yield "NAC stands for: "
                    + NOptions.get(MathUtils.randInt(0, NOptions.size() - 1))
                    + " "
                    + AOptions.get(MathUtils.randInt(0, AOptions.size() - 1))
                    + " "
                    + COptions.get(MathUtils.randInt(0, COptions.size() - 1));
            }
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

    private class TerminalTextFieldWidget extends TextFieldWidget {

        TerminalTextListWidget list;
        PanelSyncManager syncManager;

        public TerminalTextFieldWidget(TerminalTextListWidget parent, PanelSyncManager manager) {
            super();
            list = parent;
            syncManager = manager;
        }

        @Override
        public void onInit() {
            super.onInit();
            BooleanSyncValue talkSyncer = syncManager.findSyncHandler("talk", BooleanSyncValue.class);
            talkSyncer.setChangeListener(this::updateHintText);
        }

        @Override
        public @NotNull Result onKeyPressed(char character, int keyCode) {
            if (keyCode == Keyboard.KEY_RETURN) {
                String text = this.getText();
                if (text.isEmpty()) return Result.IGNORE;
                // Reset the text box to be blank
                this.handler.clear();
                if (!checkForKeywords(text) && multiblock.isTalkModeActive) {
                    DoubleSyncValue moodSyncer = syncManager.findSyncHandler("mood", DoubleSyncValue.class);
                    moodSyncer.setValue(Math.min(1, moodSyncer.getValue() + 0.05));
                    list.child(createPlayerTextWidget(text));
                    list.child(createResponseTextWidget(getGREGOSResponse(text)));
                    if(text.equals("clear")) {
                        list.removeAll();
                    }
                }
                updateHintText();
                return Result.SUCCESS;
            } else return super.onKeyPressed(character, keyCode);
        }

        public boolean checkForKeywords(String text) {
            BooleanSyncValue talkSyncer = syncManager.findSyncHandler("talk", BooleanSyncValue.class);
            return switch (text.toLowerCase()) {
                case "talk" -> {
                    talkSyncer.setValue(true);
                    yield true;
                }
                case "exit" -> {
                    talkSyncer.setValue(false);
                    yield true;
                }
                default -> false;
            };
        }

        public TextWidget<?> createResponseTextWidget(String text) {
            return new TextWidget<>(text).color(list.responseTextColor)
                .anchorLeft(0)
                .width(getTerminalWidgetWidth() - 6);
        }

        public TextWidget<?> createPlayerTextWidget(String text) {
            return new TextWidget<>(text).right(6)
                .color(list.playerTextColor);
        }

        public void updateHintText() {
            BooleanSyncValue talkSyncer = syncManager.findSyncHandler("talk", BooleanSyncValue.class);
            this.hintText(talkSyncer.getValue() ? fieldHintExit : fieldHintTalk);
        }
    }

    private class TerminalTextListWidget extends ListWidget<IWidget, TerminalTextListWidget> {

        public int playerTextColor = Color.WHITE.main;
        public int responseTextColor = Color.CYAN.main;
        public int genericTextColor = Color.LIME.main;

        public TerminalTextListWidget() {
            super();
        }

        @Override
        public void onInit() {
            super.onInit();
            this.child(
                new TextWidget<>("Ask GREGOS a question").left(6)
                    .color(genericTextColor));
        }

        @Override
        public void postResize() {
            super.onResized();
            ScrollData data = this.getScrollData();
            // Scroll to the bottom
            data.scrollTo(getScrollArea(), data.getScrollSize());
        }
    }

}
