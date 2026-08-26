package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Flat ROW VIEW of a Voidcraft program (pass 33 UI) — the Scratch-style indented block list shown in the
 * Controller GUI (left panel). EVERY node (at any depth) becomes exactly one row carrying: its depth (the
 * indent), its node ADDRESS (see {@link USSProgramEditor} — used by the GUI to address editor ops), its label
 * (the block name) and its visual SLOTS (the arguments shown inside the block, each editable).
 *
 * <p>
 * Also the row WIRE FORM for the list sync (one JSON string per row; see {@link #rowToJson} /
 * {@link #rowFromJson}). Pure — bare JVM testable (see {@code USSProgramViewTest}).
 */
public final class USSProgramView {

    /** One visual SLOT inside a block — an argument the user can click and edit. */
    public static final class Slot {

        /**
         * Slot key — the param key or condition side: target / index / value / slot / from / to / ticks / count / left
         * / op / right.
         */
        public final String label;
        /** Current value as text (what the block shows): "HOME", "3", "VAR 17", "EQ", … */
        public final String display;
        /** True when the slot is a condition OPERATOR (edited by cycling, not by text). */
        public final boolean isOp;

        public Slot(String label, String display, boolean isOp) {
            this.label = label;
            this.display = display;
            this.isOp = isOp;
        }
    }

    /** One row of the block list (one node, any depth). */
    public static final class Row {

        /** Indent depth: 1 = root row. */
        public final int depth;
        /** The node's editor ADDRESS ({@code [i,k,…]}; {@code [i]} = root). */
        public final int[] path;
        /** Block label: MOVE / WORK / WRITE / READ / WAIT / STOP / IF / WHILE / REPEAT. */
        public final String label;
        /** The block's argument slots (empty for WORK / STOP). */
        public final List<Slot> slots;
        /** True for IF / WHILE / REPEAT (a row that owns a body — the rows below it are its children). */
        public final boolean hasBody;

        public Row(int depth, int[] path, String label, List<Slot> slots, boolean hasBody) {
            this.depth = depth;
            this.path = path;
            this.label = label;
            this.slots = slots;
            this.hasBody = hasBody;
        }
    }

    private USSProgramView() {}

    /**
     * Flatten the program into rows (root order, children immediately after their parent — the visual
     * Scratch order).
     */
    public static List<Row> rows(USSProgram program) {
        List<Row> out = new ArrayList<Row>();
        if (program == null) {
            return out;
        }
        List<USSNode> roots = program.nodes();
        for (int i = 0; i < roots.size(); i++) {
            walk(roots.get(i), new int[] { i }, 1, out);
        }
        return out;
    }

    /** All rows of the program as wire strings (the list-sync content). */
    public static List<String> rowsJsonList(USSProgram program) {
        List<String> out = new ArrayList<String>();
        for (Row row : rows(program)) {
            out.add(rowToJson(row));
        }
        return out;
    }

    private static void walk(USSNode node, int[] path, int depth, List<Row> out) {
        out.add(new Row(depth, path, name(node), slots(node), node.hasBody()));
        if (node.hasBody()) {
            List<USSNode> body = node.body();
            for (int i = 0; i < body.size(); i++) {
                int[] child = new int[path.length + 1];
                System.arraycopy(path, 0, child, 0, path.length);
                child[path.length] = i;
                walk(body.get(i), child, depth + 1, out);
            }
        }
    }

    private static String name(USSNode node) {
        if (node.type() == USSNodeType.COMMAND) {
            switch (node.cmdId()) {
                case USSCommand.MOVE:
                    return "MOVE";
                case USSCommand.WORK:
                    return "WORK";
                case USSCommand.WRITE:
                    return "WRITE";
                case USSCommand.READ:
                    return "READ";
                case USSCommand.WAIT:
                    return "WAIT";
                case USSCommand.STOP:
                    return "STOP";
                default:
                    return "CMD" + node.cmdId();
            }
        }
        switch (node.type()) {
            case IF:
                return "IF";
            case WHILE:
                return "WHILE";
            case REPEAT:
                return "REPEAT";
            default:
                return "?";
        }
    }

    private static List<Slot> slots(USSNode node) {
        List<Slot> s = new ArrayList<Slot>();
        if (node.type() != USSNodeType.COMMAND) {
            if (node.type() == USSNodeType.REPEAT) {
                s.add(new Slot("count", String.valueOf(node.count()), false));
            } else {
                USSCondition c = node.condition();
                s.add(new Slot("left", valueDisplay(c.left()), false));
                s.add(
                    new Slot(
                        "op",
                        c.op()
                            .name(),
                        true));
                s.add(new Slot("right", valueDisplay(c.right()), false));
            }
            return s;
        }
        NBTTagCompound p = node.params();
        switch (node.cmdId()) {
            case USSCommand.MOVE:
                s.add(new Slot(USSProgramDefaults.PARAM_TARGET, p.getString(USSProgramDefaults.PARAM_TARGET), false));
                if (p.hasKey(USSProgramDefaults.PARAM_INDEX)) {
                    s.add(
                        new Slot(
                            USSProgramDefaults.PARAM_INDEX,
                            String.valueOf(p.getInteger(USSProgramDefaults.PARAM_INDEX)),
                            false));
                }
                break;
            case USSCommand.WRITE:
                NBTBase valueTag = p.getTag(USSCommandWrite.PARAM_VALUE);
                String display;
                if (valueTag instanceof NBTTagCompound) {
                    display = valueDisplay(USSValue.readFromNBT((NBTTagCompound) valueTag));
                } else {
                    display = p.getString(USSCommandWrite.PARAM_VALUE);
                }
                s.add(new Slot(USSCommandWrite.PARAM_VALUE, display, false));
                s.add(
                    new Slot(
                        USSCommandWrite.PARAM_SLOT,
                        String.valueOf(p.getInteger(USSCommandWrite.PARAM_SLOT)),
                        false));
                break;
            case USSCommand.READ:
                s.add(
                    new Slot(
                        USSCommandRead.PARAM_FROM,
                        String.valueOf(p.getInteger(USSCommandRead.PARAM_FROM)),
                        false));
                s.add(new Slot(USSCommandRead.PARAM_TO, String.valueOf(p.getInteger(USSCommandRead.PARAM_TO)), false));
                break;
            case USSCommand.WAIT:
                s.add(
                    new Slot(USSCommandWait.PARAM_TICKS, String.valueOf(p.getLong(USSCommandWait.PARAM_TICKS)), false));
                break;
            default:
                break; // WORK / STOP take no arguments
        }
        return s;
    }

    /** A program VALUE as block text: literal → the text itself; VAR → "VAR n"; STAT → "STAT n". */
    public static String valueDisplay(USSValue value) {
        if (value == null) {
            return "";
        }
        switch (value.kind()) {
            case LITERAL:
                return value.literal();
            case VAR:
                return "VAR " + value.slot();
            case STAT:
                return "STAT " + value.statId();
            default:
                return "";
        }
    }

    // region row wire form (list sync)

    /**
     * Row → wire JSON: {@code {"d":1,"p":[0,1],"l":"IF","s":[["left","VAR 3",0],["op","EQ",1],["right","5",0]]}}.
     */
    public static String rowToJson(Row row) {
        JsonObject o = new JsonObject();
        o.addProperty("d", row.depth);
        JsonArray p = new JsonArray();
        for (int v : row.path) {
            p.add(new JsonPrimitive(v));
        }
        o.add("p", p);
        o.addProperty("l", row.label);
        JsonArray s = new JsonArray();
        for (Slot slot : row.slots) {
            JsonArray e = new JsonArray();
            e.add(new JsonPrimitive(slot.label));
            e.add(new JsonPrimitive(slot.display));
            e.add(new JsonPrimitive(slot.isOp ? 1 : 0));
            s.add(e);
        }
        o.add("s", s);
        return o.toString();
    }

    /** Wire JSON → row (null when malformed — a row is display-only, so a bad row is simply skipped). */
    public static Row rowFromJson(String json) {
        try {
            JsonObject o = new JsonParser().parse(json)
                .getAsJsonObject();
            int depth = o.get("d")
                .getAsInt();
            int[] path = new int[o.getAsJsonArray("p")
                .size()];
            for (int i = 0; i < path.length; i++) {
                path[i] = o.getAsJsonArray("p")
                    .get(i)
                    .getAsInt();
            }
            String label = o.get("l")
                .getAsString();
            List<Slot> slots = new ArrayList<Slot>();
            for (JsonElement e : o.getAsJsonArray("s")) {
                JsonArray a = e.getAsJsonArray();
                slots.add(
                    new Slot(
                        a.get(0)
                            .getAsString(),
                        a.get(1)
                            .getAsString(),
                        a.size() > 2 && a.get(2)
                            .getAsInt() == 1));
            }
            return new Row(depth, path, label, slots, false);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    // endregion
}
