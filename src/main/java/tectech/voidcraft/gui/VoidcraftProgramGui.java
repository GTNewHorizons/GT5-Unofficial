package tectech.voidcraft.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.StringValue;
import com.cleanroommc.modularui.value.sync.DynamicLinkedSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DropDownMenu;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import cpw.mods.fml.relauncher.Side;
import tectech.voidcraft.uss.USSCapabilities;
import tectech.voidcraft.uss.USSCommand;
import tectech.voidcraft.uss.USSCommandRepair;
import tectech.voidcraft.uss.USSConditionOp;
import tectech.voidcraft.uss.USSConstants;
import tectech.voidcraft.uss.USSProgramDefaults;
import tectech.voidcraft.uss.USSProgramView;

/**
 * The Voidcraft programming GUI — a linear block-based visual programming view
 * (Scratch-style), opened by right-clicking the controller block, or by right-clicking a digitized blueprint
 * item (ship / base) in hand.
 *
 * <p>
 * LEFT: the current program as horizontal blocks (loops indented one level per depth), each block showing its
 * arguments as visually distinct SLOTS; a FREEFORM slot with hard-coded options (the MOVE / SEND / TAKE /
 * REPAIR target, the WRITE value) is a drop-down that lists those options on click (the click also selects the
 * slot into the CURSOR editor above the list, where the text field keeps the freeform text path); each row has
 * up / down arrow (reorder) and x (delete) buttons; the footer shows the program caps and the last server
 * rejection.
 *
 * <p>
 * RIGHT: (1) USS VALUES — pick a global USS variable slot (0..255) and assign it as a reference into the
 * selected slot (SLOT SELECTION ONLY — no live USS value display); (2) COMMANDS — the palette with an [Add] per
 * command (inserts below the selected node), gated by the craft's
 * CAPABILITY SET (a ship without thrusters gets no MOVE row; without mining power no MINE row, etc.) and each
 * row's tooltip carries the command description + the craft's stat line for it (speed / mining power / scan
 * power / siphon power / construction power); (3) PRESETS — Miner / Starlifter / Explorer / Build / Clear, each
 * hidden when the craft lacks the capability the preset needs.
 *
 * <p>
 * Sync (server authoritative): the program lives in the {@link VoidcraftProgramSource} (the controller block
 * NBT or the blueprint item NBT); the ROWS sync (S2C list of row JSON) drives the left list; every edit is one
 * C2S action ({@code uss.action} = one action JSON, see {@link tectech.voidcraft.uss.USSProgramSync}) — the
 * server runs the editor and pushes the new rows + note.
 */
public class VoidcraftProgramGui {

    // command palette node specs (see USSProgramSync#readNode)
    private static final String SPEC_MOVE = "{\"t\":0,\"c\":0,\"p\":{\"target\":\"HOME\"}}";
    private static final String SPEC_MINE = "{\"t\":0,\"c\":1}";
    private static final String SPEC_WRITE = "{\"t\":0,\"c\":2,\"p\":{\"value\":\"\",\"slot\":0}}";
    private static final String SPEC_READ = "{\"t\":0,\"c\":3,\"p\":{\"from\":0,\"to\":1}}";
    private static final String SPEC_WAIT = "{\"t\":0,\"c\":4,\"p\":{\"ticks\":20}}";
    private static final String SPEC_STOP = "{\"t\":0,\"c\":5}";
    private static final String SPEC_CONSTRUCT = "{\"t\":0,\"c\":6}";
    private static final String SPEC_REPAIR = "{\"t\":0,\"c\":7,\"p\":{\"target\":\"SELF\"}}";
    private static final String SPEC_SCAN = "{\"t\":0,\"c\":8}";
    private static final String SPEC_SIPHON = "{\"t\":0,\"c\":9}";
    private static final String SPEC_SEND = "{\"t\":0,\"c\":10,\"p\":{\"amount\":-1,\"filter\":\"*\",\"target\":\"\"}}";
    private static final String SPEC_TAKE = "{\"t\":0,\"c\":11,\"p\":{\"amount\":-1,\"filter\":\"*\",\"target\":\"\"}}";
    private static final String SPEC_STABILIZE = "{\"t\":0,\"c\":12,\"p\":{\"ticks\":"
        + USSConstants.STABILIZE_DEFAULT_TICKS
        + "}}";
    private static final String SPEC_IF = "{\"t\":1,\"l\":{\"k\":0,\"s\":\"\"},\"op\":0,\"r\":{\"k\":0,\"s\":\"\"}}";
    private static final String SPEC_WHILE = "{\"t\":2,\"l\":{\"k\":0,\"s\":\"\"},\"op\":0,\"r\":{\"k\":0,\"s\":\"\"}}";
    private static final String SPEC_REPEAT = "{\"t\":3,\"n\":1}";

    private final VoidcraftProgramSource source;

    // the craft's capability set (the capability system): which commands the underlying ship / base can run —
    // read once at build (the caps never change while the GUI is open)
    private USSCapabilities caps;

    // sync handlers (created in build)
    private GenericListSyncHandler<String> rowsSyncer;
    private StringSyncValue noteSyncer;
    private PanelSyncManager syncManager;

    // client-side CURSOR (selected row + slot)
    private final AtomicInteger selRow = new AtomicInteger(-1);
    private final AtomicInteger selSlot = new AtomicInteger(-1);
    private volatile USSProgramView.Row selectedRow;
    private final AtomicInteger maxDepth = new AtomicInteger(0);

    // input fields (client-side text; the StringValue binding is the source of truth for the text)
    private TextFieldWidget slotField;
    private TextFieldWidget varField;
    private String slotText = "";
    private String varText = "";

    // selection highlight (UI-3): per-row button references, rebuilt with the list
    private final List<ButtonWidget> rowLabelBtns = new ArrayList<ButtonWidget>();
    private final List<int[]> rowPaths = new ArrayList<int[]>();
    private final List<List<ButtonWidget>> rowSlotBtns = new ArrayList<List<ButtonWidget>>();
    private final List<List<Rectangle>> rowSlotDefaults = new ArrayList<List<Rectangle>>();
    private final List<List<DropDownMenu>> rowSlotDropdowns = new ArrayList<List<DropDownMenu>>();

    // the WRITE-value drop-down's "Current location" choice (the action JSON carries the "loc" marker instead)
    private static final String LOC_CHOICE = "Current location";

    // highlight / slot-kind backgrounds (UI-3)
    private static final Rectangle ROW_HILITE = new Rectangle().color(70, 130, 70, 255);
    private static final Rectangle SLOT_HILITE = new Rectangle().color(110, 170, 110, 255);
    private static final Rectangle OP_BG = new Rectangle().color(110, 62, 24, 255);
    private static final Rectangle VAR_BG = new Rectangle().color(32, 52, 110, 255);

    public VoidcraftProgramGui(VoidcraftProgramSource source) {
        this.source = source;
    }

    public ModularPanel build(GuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        this.caps = source.getCommandCaps();
        this.syncManager = syncManager;
        rowsSyncer = new GenericListSyncHandler<String>(
            source::getProgramRows,
            null,
            buf -> buf.readStringFromBuffer(32768),
            PacketBuffer::writeStringToBuffer,
            String::equals,
            null);
        noteSyncer = new StringSyncValue(source::getNote);
        syncManager.syncValue("uss.rows", rowsSyncer);
        syncManager.syncValue("uss.note", noteSyncer);
        syncManager.registerSyncedAction("uss.action", Side.SERVER, buf -> {
            String json;
            try {
                json = buf.readStringFromBuffer(32768);
            } catch (IOException e) {
                json = null; // unreadable payload — ignore the action
            }
            if (json != null) {
                source.applyAction(json);
            }
            rowsSyncer.notifyUpdate();
            noteSyncer.notifyUpdate();
        });

        ModularPanel panel = ModularPanel.defaultPanel("voidcraft_program", 440, 312);
        panel.child(
            Flow.row()
                .child(createLeftPanel())
                .child(createRightPanel())
                .childPadding(4)
                .margin(4)
                .coverChildren());
        return panel;
    }

    // region left panel (program block list + cursor + footer)

    private IWidget createLeftPanel() {
        // The DynamicLinkedSyncHandler is bound to the rows sync value: it rebuilds the list
        // automatically whenever the rows change (initial S2C push + every server update),
        // and pushes the initial content when the widget is initialised.
        DynamicLinkedSyncHandler<GenericListSyncHandler<String>> rowsDynamic = new DynamicLinkedSyncHandler<GenericListSyncHandler<String>>(
            rowsSyncer).widgetProvider((manager, rows) -> createRowsList(rows.getValue()));
        return new ParentWidget<>().width(248)
            .height(300)
            .child(
                Flow.column()
                    .child(createSlotEditor())
                    .child(
                        new DynamicSyncedWidget<>().syncHandler(rowsDynamic)
                            .coverChildren())
                    .child(createFooter())
                    .childPadding(2)
                    .coverChildren())
            .coverChildren();
    }

    /** The CURSOR line: the selected row + slot (with its current value), then the text field + Set / VAR. */
    private IWidget createSlotEditor() {
        // A bound value is required for the text field to render (bare text fields drop out of the flow)
        slotField = new TextFieldWidget().value(new StringValue.Dynamic(this::readSlotText, this::writeSlotText))
            .width(110)
            .height(14);
        return new ParentWidget<>().width(244)
            .height(30)
            .child(
                Flow.column()
                    .child(new TextWidget(IKey.dynamic(this::selectionLabel)).textAlign(Alignment.CenterLeft))
                    .child(
                        Flow.row()
                            .child(slotField)
                            .child(new ButtonWidget<>().onMousePressed(mb -> {
                                applySet();
                                return true;
                            })
                                .overlay(IKey.str("Set"))
                                .width(24)
                                .height(14))
                            .child(new ButtonWidget<>().onMousePressed(mb -> {
                                applyVar();
                                return true;
                            })
                                .overlay(IKey.str("VAR"))
                                .width(24)
                                .height(14))
                            .childPadding(2)
                            .coverChildren())
                    .childPadding(1)
                    .coverChildren())
            .coverChildren();
    }

    private String selectionLabel() {
        USSProgramView.Row row = selectedRow;
        int si = selSlot.get();
        if (row == null || si < 0 || si >= row.slots.size()) {
            return "no slot selected - click a slot inside a block";
        }
        USSProgramView.Slot slot = row.slots.get(si);
        return "row " + (selRow.get() + 1) + " " + slot.label + " = " + slot.display;
    }

    private String readSlotText() {
        return slotText == null ? "" : slotText;
    }

    private void writeSlotText(String text) {
        slotText = text == null ? "" : text;
    }

    private String readVarText() {
        return varText == null ? "" : varText;
    }

    private void writeVarText(String text) {
        varText = text == null ? "" : text;
    }

    private IWidget createRowsList(List<String> rows) {
        maxDepth.set(0);
        rowLabelBtns.clear();
        rowPaths.clear();
        rowSlotBtns.clear();
        rowSlotDefaults.clear();
        rowSlotDropdowns.clear();
        List<IWidget> children = new ArrayList<IWidget>();
        if (rows != null) {
            for (int i = 0; i < rows.size(); i++) {
                USSProgramView.Row row = USSProgramView.rowFromJson(rows.get(i));
                if (row == null) {
                    continue;
                }
                if (row.depth > maxDepth.get()) {
                    maxDepth.set(row.depth);
                }
                children.add(createRow(row, i));
            }
        }
        if (children.isEmpty()) {
            TextWidget<?> hint = new TextWidget(IKey.str("(empty program - add commands on the right)"));
            hint.textAlign(Alignment.CenterLeft);
            children.add(
                hint.posRel(0.5F, 0.5F)
                    .center());
        }
        refreshHighlight(); // re-apply the selection to the freshly built rows (or clear it if the row is gone)
        return new ListWidget<>().children(children)
            .childSeparator(IIcon.EMPTY_2PX)
            .size(244, 196)
            .crossAxisAlignment(Alignment.CrossAxis.START);
    }

    /** One block row: indent, label, argument slots, up / down / delete. */
    private IWidget createRow(USSProgramView.Row row, int index) {
        Flow flow = Flow.row()
            .coverChildren();
        if (row.depth > 1) {
            flow.child(new ParentWidget<>().size((row.depth - 1) * 12, 2));
        }
        ButtonWidget<?> labelBtn = new ButtonWidget<>().onMousePressed(mb -> {
            select(index, -1);
            return true;
        })
            .overlay(IKey.str(row.label))
            .width(row.label.length() * 5 + 8)
            .height(14)
            .tooltip(t -> t.add(IKey.str(rowTip(row, index))));
        flow.child(labelBtn);
        rowLabelBtns.add(labelBtn);
        rowPaths.add(row.path);
        List<ButtonWidget> slotBtns = new ArrayList<ButtonWidget>();
        List<Rectangle> slotDefs = new ArrayList<Rectangle>();
        List<DropDownMenu> slotDds = new ArrayList<DropDownMenu>();
        for (int si = 0; si < row.slots.size(); si++) {
            final int s = si;
            USSProgramView.Slot slot = row.slots.get(si);
            String ddKind = slotOptionKind(row, slot);
            if (ddKind != null) {
                DropDownMenu dd = createSlotDropdown(row, index, s, ddKind, slot);
                slotBtns.add(null);
                slotDefs.add(null);
                slotDds.add(dd);
                flow.child(dd);
                continue;
            }
            Rectangle def = slot.isOp ? OP_BG : (slot.display.startsWith("VAR ") ? VAR_BG : null);
            ButtonWidget<?> slotBtn = new ButtonWidget<>().onMousePressed(mb -> {
                if (slot.isOp) {
                    cycleOp(row, s);
                } else {
                    select(index, s);
                }
                return true;
            })
                .overlay(IKey.str(slot.display))
                .width(Math.max(14, slot.display.length() * 5 + 8))
                .height(14)
                .tooltip(
                    t -> t.add(
                        IKey.str(
                            slot.label + (slot.isOp ? " (click to cycle EQ/NEQ/LT/GT)"
                                : slot.display.startsWith("VAR ")
                                    ? " (a USS variable slot - assign a new slot, or a literal, via Set/VAR)"
                                    : " (click to edit)"))));
            if (def != null) {
                slotBtn.disableThemeBackground(true)
                    .background(def);
            }
            slotBtns.add(slotBtn);
            slotDefs.add(def);
            slotDds.add(null);
            flow.child(slotBtn);
        }
        rowSlotBtns.add(slotBtns);
        rowSlotDefaults.add(slotDefs);
        rowSlotDropdowns.add(slotDds);
        flow.child(new ButtonWidget<>().onMousePressed(mb -> {
            sendAction(moveJson(row.path, true));
            return true;
        })
            .overlay(GuiTextures.ARROW_UP)
            .width(14)
            .height(14)
            .tooltip(t -> t.add(IKey.str("move up"))));
        flow.child(new ButtonWidget<>().onMousePressed(mb -> {
            sendAction(moveJson(row.path, false));
            return true;
        })
            .overlay(GuiTextures.ARROW_DOWN)
            .width(14)
            .height(14)
            .tooltip(t -> t.add(IKey.str("move down"))));
        flow.child(new ButtonWidget<>().onMousePressed(mb -> {
            sendAction(removeJson(row.path));
            return true;
        })
            .overlay(IKey.str("x"))
            .width(14)
            .height(14)
            .tooltip(t -> t.add(IKey.str("delete"))));
        return flow.childPadding(2)
            .margin(1)
            .height(16);
    }

    /** A short per-block description for the label tooltip (UI-3). */
    private static String rowTip(USSProgramView.Row row, int index) {
        switch (row.label) {
            case "MOVE":
                return "row " + (index + 1) + " - fly to a target (HOME / planet / ripple)";
            case "WORK":
                return "row " + (index + 1) + " - do work at the current position (mine / starlift / build)";
            case "WRITE":
                return "row " + (index + 1) + " - write a value into a USS variable slot";
            case "READ":
                return "row " + (index + 1) + " - read a USS variable slot into another slot";
            case "WAIT":
                return "row " + (index + 1) + " - wait for N ticks";
            case "STOP":
                return "row " + (index + 1) + " - end the program (the ship holds)";
            case "SEND":
                return "row " + (index + 1) + " - send cargo to the target ship (needs a shared location)";
            case "TAKE":
                return "row " + (index + 1) + " - take cargo from the target ship (needs a shared location)";
            case "IF":
                return "row " + (index + 1) + " - run the block below once, when the condition holds";
            case "WHILE":
                return "row " + (index + 1) + " - run the block below, while the condition holds";
            case "REPEAT":
                return "row " + (index + 1) + " - run the block below N times";
            default:
                return "row " + (index + 1);
        }
    }

    private IWidget createFooter() {
        return new ParentWidget<>().height(20)
            .child(
                Flow.column()
                    .child(
                        new TextWidget(
                            IKey.dynamic(() -> "nodes " + rowCount() + " / 255    depth " + maxDepth.get() + " / 8")))
                    .child(new TextWidget(IKey.dynamic(() -> noteSyncer.getStringValue())))
                    .childPadding(0)
                    .coverChildren())
            .coverChildren();
    }

    private int rowCount() {
        List<String> rows = rowsSyncer.getValue();
        return rows == null ? 0 : rows.size();
    }

    // endregion

    // region right panel (USS values / arguments / commands / presets)

    private IWidget createRightPanel() {
        varField = new TextFieldWidget().value(new StringValue.Dynamic(this::readVarText, this::writeVarText))
            .width(30)
            .height(14);
        // The capability system: the palette only offers the commands the craft can run (a ship without
        // thruster covers gets no MOVE row; the presets needing a missing capability are hidden). WRITE /
        // READ / WAIT / STOP and the flow commands are always available (they need no ship capability).
        Flow col = Flow.column()
            .child(sectionLabel("USS VALUES"))
            .child(
                Flow.row()
                    .child(slotLabel("slot").width(26))
                    .child(varField)
                    .child(new ButtonWidget<>().onMousePressed(mb -> {
                        applyVar();
                        return true;
                    })
                        .overlay(IKey.str("Assign"))
                        .width(36)
                        .height(14)
                        .tooltip(t -> t.add(IKey.str("assign this USS slot to the selected slot"))))
                    .childPadding(2)
                    .coverChildren());
        col.child(sectionLabel("COMMANDS"));
        if (caps.has(USSCapabilities.MOVE)) {
            col.child(cmdRow(USSCommand.MOVE, SPEC_MOVE, "fly to the target body (HOME = the gateway)"));
        }
        if (caps.has(USSCapabilities.MINE)) {
            col.child(cmdRow(USSCommand.MINE, SPEC_MINE, "mine the target body"));
        }
        if (caps.has(USSCapabilities.SCAN)) {
            col.child(cmdRow(USSCommand.SCAN, SPEC_SCAN, "scan the system for new ripple points"));
        }
        if (caps.has(USSCapabilities.SIPHON)) {
            col.child(cmdRow(USSCommand.SIPHON, SPEC_SIPHON, "siphon the target star into fluid cargo"));
        }
        if (caps.has(USSCapabilities.LOGISTICS)) {
            col.child(
                cmdRow(
                    USSCommand.SEND,
                    SPEC_SEND,
                    "send cargo to the target ship (amount, filter, target — index / name / NEARBY; default ALL / * )"))
                .child(
                    cmdRow(
                        USSCommand.TAKE,
                        SPEC_TAKE,
                        "take cargo from the target ship (amount, filter, target — index / name / NEARBY; default ALL / * )"));
        }
        col.child(cmdRow(USSCommand.WRITE, SPEC_WRITE, "write a USS slot value into the selected slot"))
            .child(cmdRow(USSCommand.READ, SPEC_READ, "copy one USS slot into another"))
            .child(cmdRow(USSCommand.WAIT, SPEC_WAIT, "pause the program for a number of ticks"))
            .child(cmdRow(USSCommand.STOP, SPEC_STOP, "end the program and return home"));
        if (caps.has(USSCapabilities.CONSTRUCT)) {
            col.child(cmdRow(USSCommand.CONSTRUCT, SPEC_CONSTRUCT, "build the stored base at the target site"));
        }
        if (caps.has(USSCapabilities.REPAIR) || caps.has(USSCapabilities.STABILIZE)) {
            Flow station = Flow.row();
            if (caps.has(USSCapabilities.REPAIR)) {
                station.child(
                    cmdRow(
                        USSCommand.REPAIR,
                        SPEC_REPAIR,
                        "repair the station (target — SELF or a fleet member at the station's location)"));
            }
            if (caps.has(USSCapabilities.STABILIZE)) {
                station.child(
                    cmdRow(
                        USSCommand.STABILIZE,
                        SPEC_STABILIZE,
                        "run a Stabilization Matrix window for the given ticks (needs a built Stabilizer + Field Generators)"));
            }
            col.child(
                station.childPadding(2)
                    .coverChildren());
        }
        col.child(flowRow("IF", SPEC_IF, "run the block when the condition holds"))
            .child(flowRow("WHILE", SPEC_WHILE, "repeat the block while the condition holds"))
            .child(flowRow("REPEAT", SPEC_REPEAT, "repeat the block a fixed number of times"));
        col.child(sectionLabel("PRESETS"));
        if (caps.has(USSCapabilities.MINE) || caps.has(USSCapabilities.SIPHON)) {
            Flow row = Flow.row();
            if (caps.has(USSCapabilities.MINE)) {
                row.child(presetButton("Miner", "miner"));
            }
            if (caps.has(USSCapabilities.SIPHON)) {
                row.child(presetButton("Star", "starlifter"));
            }
            col.child(
                row.childPadding(2)
                    .coverChildren());
        }
        Flow row2 = Flow.row()
            .child(presetButton("Clear", "clear"));
        if (caps.has(USSCapabilities.SCAN)) {
            row2.child(presetButton("Scan", "explorer"));
        }
        col.child(
            row2.childPadding(2)
                .coverChildren());
        if (caps.has(USSCapabilities.CONSTRUCT) || caps.has(USSCapabilities.STABILIZE)) {
            Flow row3 = Flow.row();
            if (caps.has(USSCapabilities.CONSTRUCT)) {
                row3.child(presetButton("Build", "constructor"));
            }
            if (caps.has(USSCapabilities.STABILIZE)) {
                row3.child(presetButton("Stabilize", "stabilizer"));
            }
            col.child(
                row3.childPadding(2)
                    .coverChildren());
        }
        return new ParentWidget<>().width(172)
            .height(300)
            .child(
                col.childPadding(2)
                    .coverChildren())
            .coverChildren();
    }

    private IWidget sectionLabel(String text) {
        TextWidget<?> label = new TextWidget(IKey.str(text));
        label.textAlign(Alignment.CenterLeft);
        return label.height(12)
            .marginTop(2);
    }

    private TextWidget<?> slotLabel(String text) {
        TextWidget<?> label = new TextWidget(IKey.str(text));
        label.textAlign(Alignment.CenterLeft);
        return label;
    }

    /**
     * A palette row: label + [Add] (inserts the node at the cursor). The tooltip carries the description plus an
     * optional STAT LINE (the craft's stats for the command — the ship's speed for MOVE, its mining power for
     * MINE, and so on; flow rows carry none).
     */
    private IWidget row(String label, String specJson, String description, String stat) {
        return Flow.row()
            .child(slotLabel(label).width(56))
            .child(new ButtonWidget<>().onMousePressed(mb -> {
                sendAction(insertSpecJson(specJson));
                return true;
            })
                .overlay(IKey.str("Add"))
                .width(24)
                .height(14)
                .tooltip(t -> {
                    t.add(IKey.str(description));
                    if (!stat.isEmpty()) {
                        t.newLine();
                        t.add(IKey.str(stat));
                    }
                }))
            .childPadding(2)
            .coverChildren();
    }

    /**
     * A COMMAND-palette row: the command's label + [Add], with the craft's STAT LINE for the command in the
     * tooltip (the capability system: the ship's speed for MOVE, its mining power for MINE, and so on).
     */
    private IWidget cmdRow(int commandId, String specJson, String description) {
        return row(USSCommand.label(commandId), specJson, description, source.getCommandStatLine(commandId));
    }

    /** A FLOW-block row (IF / WHILE / REPEAT — always available, no stat line). */
    private IWidget flowRow(String label, String specJson, String description) {
        return row(label, specJson, description, "");
    }

    private IWidget presetButton(String label, String preset) {
        return new ButtonWidget<>().onMousePressed(mb -> {
            sendAction(applyPresetJson(preset));
            return true;
        })
            .overlay(IKey.str(label))
            .width(38)
            .height(14);
    }

    // endregion

    // region cursor + actions

    private void select(int rowIndex, int slotIndex) {
        List<String> rows = rowsSyncer.getValue();
        if (rows == null || rowIndex < 0 || rowIndex >= rows.size()) {
            selRow.set(-1);
            selSlot.set(-1);
            selectedRow = null;
            writeSlotText("");
            if (slotField != null) {
                slotField.setText("");
            }
            refreshHighlight();
            return;
        }
        USSProgramView.Row row = USSProgramView.rowFromJson(rows.get(rowIndex));
        selRow.set(rowIndex);
        selSlot.set(slotIndex);
        selectedRow = row;
        if (slotField != null) {
            String text = (row != null && slotIndex >= 0 && slotIndex < row.slots.size())
                ? row.slots.get(slotIndex).display
                : "";
            writeSlotText(text);
            slotField.setText(text);
        }
        refreshHighlight();
    }

    private boolean hasSelectedSlot() {
        USSProgramView.Row row = selectedRow;
        int si = selSlot.get();
        return row != null && si >= 0 && si < row.slots.size();
    }

    /** Restore every row button to its default look (theme background / slot-kind background). */
    private void clearHighlights() {
        for (ButtonWidget b : rowLabelBtns) {
            b.disableThemeBackground(false);
        }
        for (int i = 0; i < rowSlotBtns.size(); i++) {
            List<ButtonWidget> btns = rowSlotBtns.get(i);
            List<Rectangle> defs = rowSlotDefaults.get(i);
            List<DropDownMenu> dds = rowSlotDropdowns.get(i);
            for (int j = 0; j < btns.size(); j++) {
                if (btns.get(j) != null) {
                    if (defs.get(j) == null) {
                        btns.get(j)
                            .disableThemeBackground(false);
                    } else {
                        btns.get(j)
                            .disableThemeBackground(true)
                            .background(defs.get(j));
                    }
                } else {
                    dds.get(j)
                        .disableThemeBackground(false);
                }
            }
        }
    }

    /** (Re)apply the selection highlight to the currently built rows; clears the selection if the row is gone. */
    private void refreshHighlight() {
        clearHighlights();
        USSProgramView.Row sel = selectedRow;
        if (sel == null) {
            return;
        }
        int idx = -1;
        for (int i = 0; i < rowPaths.size(); i++) {
            if (java.util.Arrays.equals(rowPaths.get(i), sel.path)) {
                idx = i;
                break;
            }
        }
        if (idx < 0) {
            // the selected row no longer exists (it was deleted) — drop the selection
            select(-1, -1);
            return;
        }
        rowLabelBtns.get(idx)
            .disableThemeBackground(true)
            .background(ROW_HILITE);
        int si = selSlot.get();
        if (si >= 0 && si < rowSlotBtns.get(idx)
            .size()) {
            if (rowSlotBtns.get(idx)
                .get(si) != null) {
                rowSlotBtns.get(idx)
                    .get(si)
                    .disableThemeBackground(true)
                    .background(SLOT_HILITE);
            } else {
                rowSlotDropdowns.get(idx)
                    .get(si)
                    .disableThemeBackground(true)
                    .background(SLOT_HILITE);
            }
        }
    }

    private String selectedSlotLabel() {
        return selectedRow.slots.get(selSlot.get()).label;
    }

    /** [Set]: write the text field as a literal into the selected slot (param / count / condition side). */
    private void applySet() {
        if (!hasSelectedSlot()) {
            return;
        }
        USSProgramView.Row row = selectedRow;
        String label = selectedSlotLabel();
        String text = readSlotText();
        if ("count".equals(label)) {
            int n;
            try {
                n = Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                return;
            }
            sendAction(countJson(row, n));
            return;
        }
        if ("left".equals(label) || "right".equals(label)) {
            sendAction(condLitJson(row, "left".equals(label), text));
            return;
        }
        if ("op".equals(label)) {
            cycleOp(row, selSlot.get());
            return;
        }
        // target / index / value / slot / from / to / ticks
        sendAction(paramJson(row, label, text));
    }

    /** [VAR] / [Assign]: assign the selected USS slot as a reference (WRITE value / condition side). */
    private void applyVar() {
        if (!hasSelectedSlot() || varField == null) {
            return;
        }
        USSProgramView.Row row = selectedRow;
        String label = selectedSlotLabel();
        int slot;
        try {
            slot = Integer.parseInt(readVarText().trim());
        } catch (NumberFormatException e) {
            slot = 0;
        }
        slot = Math.max(0, Math.min(255, slot));
        if ("left".equals(label) || "right".equals(label)) {
            sendAction(condVarJson(row, "left".equals(label), slot));
            return;
        }
        if ("value".equals(label)) {
            sendAction(paramVarJson(row, slot));
        }
        // any other slot kind: a USS reference is not valid there — silent no-op
    }

    /** Op slot: cycle EQ → NEQ → LT → GT → EQ. */
    private void cycleOp(USSProgramView.Row row, int slotIndex) {
        String current = row.slots.get(slotIndex).display;
        USSConditionOp op;
        try {
            op = USSConditionOp.valueOf(current);
        } catch (IllegalArgumentException e) {
            op = USSConditionOp.EQ;
        }
        USSConditionOp next = op == USSConditionOp.EQ ? USSConditionOp.NEQ
            : op == USSConditionOp.NEQ ? USSConditionOp.LT
                : op == USSConditionOp.LT ? USSConditionOp.GT : USSConditionOp.EQ;
        sendAction(condopJson(row, next.name()));
    }

    /**
     * The drop-down option kind of a freeform slot, or null when the slot has no hard-coded options: "move" /
     * "send" / "repair" = the target slot of those commands, "write" = the WRITE value slot.
     */
    private static String slotOptionKind(USSProgramView.Row row, USSProgramView.Slot slot) {
        if (slot.isOp) {
            return null;
        }
        if ("target".equals(slot.label)) {
            switch (row.label) {
                case "MOVE":
                    return "move";
                case "SEND":
                case "TAKE":
                    return "send";
                case "REPAIR":
                    return "repair";
                default:
                    return null;
            }
        }
        if ("value".equals(slot.label) && "WRITE".equals(row.label)) {
            return "write";
        }
        return null;
    }

    /**
     * A freeform slot with hard-coded options, rendered as a drop-down: on click the options appear (and the
     * slot is selected into the editor at the same time, so the freeform text path stays available). The closed
     * state shows the current value; a value that is not an option gets its own choice added. A choice click
     * sends the same one C2S action as the freeform path (the server stays authoritative).
     */
    @SuppressWarnings("deprecation")
    private DropDownMenu createSlotDropdown(USSProgramView.Row row, int rowIndex, int slotIndex, String kind,
        USSProgramView.Slot slot) {
        final int s = slotIndex;
        List<String> labels = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        if ("move".equals(kind)) {
            labels.add("Star");
            values.add(USSProgramDefaults.TARGET_STAR);
            labels.add("Planet");
            values.add(USSProgramDefaults.TARGET_PLANET);
            labels.add("Nearest planet");
            values.add(USSProgramDefaults.TARGET_NEAREST_PLANET);
            labels.add("Random planet");
            values.add(USSProgramDefaults.TARGET_RANDOM_PLANET);
            labels.add("Ripple");
            values.add(USSProgramDefaults.TARGET_RIPPLE);
            labels.add("Random ripple");
            values.add(USSProgramDefaults.TARGET_RIPPLE_UNSCANNED);
            labels.add("Ship");
            values.add(USSProgramDefaults.TARGET_SHIP);
            labels.add("Home");
            values.add(USSProgramDefaults.TARGET_HOME);
        } else if ("send".equals(kind)) {
            labels.add("Nearby");
            values.add(USSProgramDefaults.TARGET_NEARBY);
        } else if ("repair".equals(kind)) {
            labels.add("Self");
            values.add(USSCommandRepair.TARGET_SELF);
        } else {
            labels.add(LOC_CHOICE);
            values.add(LOC_CHOICE);
        }
        String display = slot.display;
        int sel = -1;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i)
                .equals(display)) {
                sel = i;
                break;
            }
        }
        if (sel < 0 && !display.isEmpty()) {
            // a custom value (freeform text, a VAR / STAT reference) gets its own choice so the closed state
            // can show it
            labels.add(display);
            values.add(display);
            sel = values.size() - 1;
        }
        int textW = display.length();
        for (String l : labels) {
            textW = Math.max(textW, l.length());
        }
        DropDownMenu dd = new SlotDropdown(new Runnable() {

            @Override
            public void run() {
                select(rowIndex, s);
            }
        });
        for (int i = 0; i < values.size(); i++) {
            final String value = values.get(i);
            dd.addChoice(new DropDownMenu.ItemSelected() {

                @Override
                public void selected(DropDownMenu menu) {
                    // the built-in choice handler already closed the internal list (on mouse release) — the
                    // mirror must not stay open or the next bar press reopens it
                    ((SlotDropdown) dd).setOpen(false);
                    String json = slotChoiceActionJson(row, kind, value);
                    if (json != null) {
                        sendAction(json);
                    }
                }
            }, labels.get(i));
        }
        dd.setSelectedIndex(sel);
        dd.width(Math.max(40, Math.min(textW * 5 + 17, 110)))
            .height(14)
            .tooltip(t -> t.add(IKey.str(slot.label + " - pick an option, or type a value in the editor")));
        return dd;
    }

    /** The one C2S action a drop-down choice sends (null = no action — the current value is kept). */
    private String slotChoiceActionJson(USSProgramView.Row row, String kind, String value) {
        if (!"write".equals(kind)) {
            return paramJson(row, "target", value);
        }
        if (LOC_CHOICE.equals(value)) {
            return paramLocJson(row);
        }
        if (value.startsWith("VAR ")) {
            try {
                return paramVarJson(row, Integer.parseInt(value.substring(4)));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if (value.startsWith("STAT ")) {
            return null; // no GUI action for a STAT reference — the value is kept as-is
        }
        return paramJson(row, "value", value);
    }

    // endregion

    // region action JSON (one C2S action = one edit)

    private void sendAction(String json) {
        syncManager.callSyncedAction("uss.action", buf -> {
            try {
                buf.writeStringToBuffer(json);
            } catch (IOException e) {
                // payload too large / bad — the action is dropped
            }
        });
    }

    private static JsonArray pathArray(int[] path) {
        JsonArray a = new JsonArray();
        for (int v : path) {
            a.add(new JsonPrimitive(v));
        }
        return a;
    }

    private String paramJson(USSProgramView.Row row, String key, String value) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "param");
        o.add("path", pathArray(row.path));
        o.addProperty("key", key);
        o.addProperty("value", value);
        return o.toString();
    }

    private String paramVarJson(USSProgramView.Row row, int slot) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "param");
        o.add("path", pathArray(row.path));
        o.addProperty("key", "value");
        o.addProperty("var", slot);
        return o.toString();
    }

    private String paramLocJson(USSProgramView.Row row) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "param");
        o.add("path", pathArray(row.path));
        o.addProperty("key", "value");
        o.addProperty("loc", true);
        return o.toString();
    }

    private String countJson(USSProgramView.Row row, int value) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "count");
        o.add("path", pathArray(row.path));
        o.addProperty("value", value);
        return o.toString();
    }

    private String condLitJson(USSProgramView.Row row, boolean left, String lit) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "cond");
        o.add("path", pathArray(row.path));
        o.addProperty("side", left ? 0 : 1);
        o.addProperty("lit", lit);
        return o.toString();
    }

    private String condVarJson(USSProgramView.Row row, boolean left, int slot) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "cond");
        o.add("path", pathArray(row.path));
        o.addProperty("side", left ? 0 : 1);
        o.addProperty("var", slot);
        return o.toString();
    }

    private String condopJson(USSProgramView.Row row, String op) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "condop");
        o.add("path", pathArray(row.path));
        o.addProperty("operator", op); // "op" is the action discriminator — the operator goes under "operator"
        return o.toString();
    }

    private static String removeJson(int[] path) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "remove");
        o.add("path", pathArray(path));
        return o.toString();
    }

    private static String moveJson(int[] path, boolean up) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "move");
        o.add("path", pathArray(path));
        o.addProperty("up", up);
        return o.toString();
    }

    /**
     * INSERT at the cursor: below the selected row (its list, one after its index) or at the end of the
     * program.
     */
    private String insertSpecJson(String specJson) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "insert");
        USSProgramView.Row sel = selectedRow;
        if (sel == null) {
            o.add("path", new JsonArray());
            o.addProperty("index", rowCount());
        } else {
            int[] p = sel.path;
            o.add("path", pathArray(java.util.Arrays.copyOf(p, p.length - 1)));
            o.addProperty("index", p[p.length - 1] + 1);
        }
        o.add(
            "node",
            new JsonParser().parse(specJson)
                .getAsJsonObject());
        return o.toString();
    }

    private static String applyPresetJson(String preset) {
        JsonObject o = new JsonObject();
        o.addProperty("op", "apply");
        o.addProperty("preset", preset);
        return o.toString();
    }

    // endregion

    /**
     * A {@link DropDownMenu} that selects its slot into the editor the moment it opens — the same click that
     * shows the options also makes the freeform text field available for that slot.
     */
    @SuppressWarnings("deprecation")
    private static final class SlotDropdown extends DropDownMenu {

        private final Runnable onOpen;
        private boolean open;

        SlotDropdown(Runnable onOpen) {
            this.onOpen = onOpen;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        @Override
        public Interactable.Result onMousePressed(int mouseButton) {
            if (open) {
                open = false;
            } else {
                open = true;
                onOpen.run();
            }
            return super.onMousePressed(mouseButton);
        }
    }
}
