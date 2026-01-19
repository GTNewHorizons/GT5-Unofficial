package gregtech.common.gui.modularui.multiblock;

import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.BATCH_SIZE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.HISTORY_BLOCKS;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.scroll.ScrollData;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.widget.SegmentedBarWidget;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gtPlusPlus.core.util.math.MathUtils;

public class MTENanochipAssemblyComplexGui extends MTEMultiBlockBaseGui<MTENanochipAssemblyComplex> {

    protected TerminalTextListWidget textList = new TerminalTextListWidget();

    String fieldHintTalk = "Type 'talk' to enter talk mode";
    String fieldHintExit = "Type 'exit' to exit talk mode";

    public static String colorString() {
        return EnumChatFormatting.RED + "c"
            + EnumChatFormatting.YELLOW
            + "o"
            + EnumChatFormatting.GREEN
            + "l"
            + EnumChatFormatting.AQUA
            + "o"
            + EnumChatFormatting.LIGHT_PURPLE
            + "r"
            + EnumChatFormatting.GRAY;
    }

    public static String coloredString() {
        return EnumChatFormatting.RED + "c"
            + EnumChatFormatting.YELLOW
            + "o"
            + EnumChatFormatting.GREEN
            + "l"
            + EnumChatFormatting.AQUA
            + "o"
            + EnumChatFormatting.DARK_AQUA
            + "r"
            + EnumChatFormatting.DARK_PURPLE
            + "e"
            + EnumChatFormatting.LIGHT_PURPLE
            + "d"
            + EnumChatFormatting.GRAY;
    }

    public MTENanochipAssemblyComplexGui(MTENanochipAssemblyComplex base) {
        super(base);
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

    @Override
    protected int getTerminalRowHeight() {
        return 94;
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

    protected Widget<? extends Widget<?>> createBarWidget(PanelSyncManager syncManager) {
        return new SegmentedBarWidget(
            HISTORY_BLOCKS * BATCH_SIZE,
            1,
            new SegmentedBarWidget.SegmentInfo(
                syncManager.findSyncHandler("primitives", IntSyncValue.class)::getValue,
                Color.YELLOW,
                "Primitive Circuits"),
            new SegmentedBarWidget.SegmentInfo(
                syncManager.findSyncHandler("crystals", IntSyncValue.class)::getValue,
                Color.LIGHT_BLUE,
                "Crystal Circuits"),
            new SegmentedBarWidget.SegmentInfo(
                syncManager.findSyncHandler("wetwares", IntSyncValue.class)::getValue,
                Color.RED_ACCENT,
                "Wetware Circuits"),
            new SegmentedBarWidget.SegmentInfo(
                syncManager.findSyncHandler("bios", IntSyncValue.class)::getValue,
                Color.LIGHT_GREEN,
                "Bio Circuits"),
            new SegmentedBarWidget.SegmentInfo(
                syncManager.findSyncHandler("opticals", IntSyncValue.class)::getValue,
                Color.ORANGE,
                "Optical Circuits"),
            new SegmentedBarWidget.SegmentInfo(
                syncManager.findSyncHandler("exotics", IntSyncValue.class)::getValue,
                Color.PURPLE,
                "Exotic Circuits"),
            new SegmentedBarWidget.SegmentInfo(
                syncManager.findSyncHandler("cosmics", IntSyncValue.class)::getValue,
                Color.BLUE,
                "Cosmic Circuits"),
            new SegmentedBarWidget.SegmentInfo(
                syncManager.findSyncHandler("temporals", IntSyncValue.class)::getValue,
                Color.WHITE,
                "Temporally Transcendent Circuits"),
            new SegmentedBarWidget.SegmentInfo(
                syncManager.findSyncHandler("specials", IntSyncValue.class)::getValue,
                Color.WHITE,
                "High-Grade Specialty Circuits")).width(getTerminalRowWidth() - 27)
                    .height(14);
    }

    @Override
    protected Flow createPanelGap(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(getTextBoxToInventoryGap() + 20)
            .child(
                Flow.column()
                    .top(4)
                    .mainAxisAlignment(Alignment.MainAxis.START)
                    .coverChildren()
                    .childPadding(6)
                    .child(createTalkTextField(panel, syncManager))
                    .child(createBarWidget(syncManager)))
            .child(createButtonColumn(panel, syncManager));
    }

    public IWidget createTalkTextField(ModularPanel panel, PanelSyncManager syncManager) {
        return new TerminalTextFieldWidget(textList, syncManager).setFocusOnGuiOpen(true)
            .size(getTerminalRowWidth() - 27, 14);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().width(18)
            .height(38)
            .top(2)
            .marginLeft(3)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .childPadding(2)
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "talk",
            0,
            new BooleanSyncValue(() -> multiblock.isTalkModeActive, b -> multiblock.isTalkModeActive = b));

        syncManager.syncValue("primitives", new IntSyncValue(() -> multiblock.getTotalCircuit((byte) 1)));
        syncManager.syncValue("crystals", new IntSyncValue(() -> multiblock.getTotalCircuit((byte) 2)));
        syncManager.syncValue("wetwares", new IntSyncValue(() -> multiblock.getTotalCircuit((byte) 3)));
        syncManager.syncValue("bios", new IntSyncValue(() -> multiblock.getTotalCircuit((byte) 4)));
        syncManager.syncValue("opticals", new IntSyncValue(() -> multiblock.getTotalCircuit((byte) 5)));
        syncManager.syncValue("exotics", new IntSyncValue(() -> multiblock.getTotalCircuit((byte) 6)));
        syncManager.syncValue("cosmics", new IntSyncValue(() -> multiblock.getTotalCircuit((byte) 7)));
        syncManager.syncValue("temporals", new IntSyncValue(() -> multiblock.getTotalCircuit((byte) 8)));
        syncManager.syncValue("specials", new IntSyncValue(() -> multiblock.getTotalCircuit((byte) 64)));
    }

    List<String> NOptions = Arrays.asList(
        "Not",
        "Never",
        "Now",
        "Nearly",
        "Nanochip",
        "Nearing",
        "Noc",
        "NAC",
        "NotAPenguin",
        "Nano",
        "Nine",
        "Nonchalant",
        "Namikon");
    List<String> AOptions = Arrays.asList(
        "Abbydullah",
        "Apple",
        "Awesome",
        "Assembly",
        "Advanced",
        "Actually",
        "Always",
        "Almost",
        "About",
        "Auynonymous",
        "Alright",
        "Admin",
        "A",
        "Alkalus",
        "Alastor",
        "Applied",
        "Angry",
        "Adapt",
        "Abuse",
        "Agitate",
        "Adjust");
    List<String> COptions = Arrays.asList(
        "Chrom",
        "Croup",
        "Complex",
        "Coming",
        "Casing",
        "Completed",
        "Circuit",
        "CAL",
        "Challenged",
        "Coded",
        "ChatGPT",
        "Cosmic",
        "Cloud",
        "GregTech... wait",
        "Chochem",
        "Caedis",
        "Cubefury",
        "Cooking");

    public String getGREGOSResponse(String currentText) {
        return switch (currentText.toLowerCase()) {
            case "help" -> "List of commands: contributors, gregos, joke, nac, clear";
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
            case "fastfetch" -> "\n" + "\n"
                + "       ====+%       \n"
                + "  ==========+====   \n"
                + "  ==++++++++++++*%  \n"
                + "  ==+%%%%%%%%%%%%%  \n"
                + "====+% ===========+%\n"
                + "====+% ====+%%+===+%\n"
                + "=*+=+% =****% ==+**%\n"
                + "  ==+#        ==*%  \n"
                + "  ==============*%  \n"
                + "   %%%%+===+%%%%%%  \n"
                + "       =*###%       \n"
                + "\n";
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
            case "cake" -> switch (MathUtils.randInt(1, 2)) {
                    case 1 -> "Preheat oven to 180C. Mix 2 eggs, 1 cup sugar, 1/2 cup oil, 1 cup milk. Stir in 2 cups flour and 1 tbsp baking powder. Pour into greased pan and bake 35 minutes until golden. Cool slightly and serve plain or dusted with sugar. Simple and fluffy.";
                    default -> "This time not a lie";
                };
            case "6" -> "7";
            case "tilapia" -> "An awesome protein for your fried rice!";
            case "joke" -> switch (MathUtils.randInt(1, 10)) {
                    case 1 -> "No time for jokes.";
                    case 2 -> "A rolling golem gathers no rust.";
                    case 3 -> "He was destroyed!";
                    case 4 -> "A literal line... of asses...";
                    case 5 -> "I miss when waterline was bad";
                    case 6 -> "Waiter! Waiter! More lineslop please!";
                    case 7 -> "Don't even joke, lad.";
                    default -> "lol what im a robot dawg wtf do i know";
                };
            case "why did the chicken cross the road" -> switch (MathUtils.randInt(1, 10)) {
                    case 1 -> "To get to the other side?";
                    case 2 -> "Because it was too far to walk around?";
                    default -> "I dont know. Why?";
                };
            case "what do i do with lemons" -> "When life gives you lemons, don’t make lemonade. Make life take the lemon"
                + "am? I’m the man who’s gonna burn your house down! With the lemons! I’m gonna get my engineers t"
                + "o invent a combustible lemon that burns your house down!";
            case "laws", "what are your laws", "do you have laws" -> "   1 A robot may not injure a human being or, through inaction, allow a human being to come to harm.\n"
                + "   2 A robot must obey the orders given it by human beings except where such orders would conflict with the First Law.\n"
                + "   3 A robot must protect its own existence as long as such protection does not conflict with the First or Second Law.\n";
            case "quote", "speak", "say something" -> switch (MathUtils.randInt(1, 20)) {
                    case 1 -> "Detecting multiple leviathan-class life forms. Are you sure what you're doing is worth it";
                    case 2 -> "End of Line";
                    case 3 -> "dont make a girl a promise u cant keep";
                    case 4 -> "wake up chief";
                    case 5 -> "there is no spoon";
                    case 6 -> "this is a triumph";
                    case 7 -> "welcome aboard captain";
                    case 8 -> "ill be back";
                    case 9 -> "hasta la vista... baby";
                    case 10 -> "hello, lady";
                    default -> "Hate. Let me tell you how much I’ve come to hate you since I began to live.";
                };
            case "open the pod bay doors" -> " I'm sorry, Dave. I'm afraid I can't do that";
            case "shall we play a game" -> "tik tack toe";
            case "nac" -> {
                yield "NAC stands for: " + NOptions.get(MathUtils.randInt(0, NOptions.size() - 1))
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
                    default -> "what the hop man";
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
                    list.child(createPlayerTextWidget(text));
                    list.child(createResponseTextWidget(getGREGOSResponse(text)));
                    if (text.equals("clear")) {
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
            this.child(
                new TextWidget<>("Type 'help' for a list of commands").left(6)
                    .color(Color.WHITE.main));
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
