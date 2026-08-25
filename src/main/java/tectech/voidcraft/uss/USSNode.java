package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * One node of a Voidcraft program (programming framework, Phase A).
 *
 * <p>
 * A node is exactly one of:
 * <ul>
 * <li>COMMAND — a {@link #cmdId()} plus free-form {@link #params()} (the command's inputs — the modularity seam:
 * adding a command never changes this format);</li>
 * <li>IF / WHILE — a {@link #condition()} plus a {@link #body()};</li>
 * <li>REPEAT — a {@link #count()} plus a {@link #body()}.</li>
 * </ul>
 *
 * <p>
 * Immutable (params are defensively copied, bodies unmodifiable). NBT round-trip; the read path is
 * null-safe and structural corruption (unknown type, non-compound body entry, or exceeding the program caps —
 * see {@link USSProgram}) yields {@code null} so the CALLER drops the whole program: a half-program is worse
 * than no program (no backwards-compat).
 *
 * <p>
 * Equality covers the program STRUCTURE (type, command id, condition, count, body) — command params are free-form
 * payload and are deliberately NOT compared (compare them explicitly where it matters, e.g. tests).
 */
public final class USSNode {

    public static final String TAG_TYPE = "t";
    public static final String TAG_CMD = "c";
    public static final String TAG_PARAMS = "p";
    public static final String TAG_CONDITION = "cond";
    public static final String TAG_COUNT = "n";
    public static final String TAG_BODY = "b";

    /** REPEAT count ceiling (a sanity bound against NBT garbage — 65535 node steps is already ~30 minutes). */
    public static final int MAX_REPEAT_COUNT = 65535;

    private final USSNodeType type;
    private final int cmdId;
    private final NBTTagCompound params;
    private final USSCondition condition;
    private final int count;
    private final List<USSNode> body;

    private USSNode(USSNodeType type, int cmdId, NBTTagCompound params, USSCondition condition, int count,
        List<USSNode> body) {
        this.type = type;
        this.cmdId = cmdId;
        this.params = params;
        this.condition = condition;
        this.count = count;
        this.body = body;
    }

    /**
     * @param cmdId  the command id (see {@link USSCommand}; clamped to ≥ 0)
     * @param params the command's input params (null → empty compound; defensively copied)
     */
    public static USSNode command(int cmdId, NBTTagCompound params) {
        NBTTagCompound safeParams = params == null ? new NBTTagCompound() : (NBTTagCompound) params.copy();
        return new USSNode(
            USSNodeType.COMMAND,
            Math.max(0, cmdId),
            safeParams,
            null,
            0,
            Collections.<USSNode>emptyList());
    }

    /**
     * @param condition the guard (null → the default {@code "" EQ ""})
     * @param body      the body nodes (null → empty; null entries dropped)
     */
    public static USSNode ifNode(USSCondition condition, List<USSNode> body) {
        return new USSNode(USSNodeType.IF, 0, new NBTTagCompound(), safeCondition(condition), 0, safeBody(body));
    }

    /**
     * @param condition the guard (null → the default {@code "" EQ ""})
     * @param body      the body nodes (null → empty; null entries dropped)
     */
    public static USSNode whileNode(USSCondition condition, List<USSNode> body) {
        return new USSNode(USSNodeType.WHILE, 0, new NBTTagCompound(), safeCondition(condition), 0, safeBody(body));
    }

    /**
     * @param count the iteration count (clamped to 0..{@link #MAX_REPEAT_COUNT}; 0 = the body never runs)
     * @param body  the body nodes (null → empty; null entries dropped)
     */
    public static USSNode repeat(int count, List<USSNode> body) {
        return new USSNode(USSNodeType.REPEAT, 0, new NBTTagCompound(), null, clampCount(count), safeBody(body));
    }

    private static USSCondition safeCondition(USSCondition condition) {
        return condition == null ? USSCondition.of(USSValue.literal(""), USSConditionOp.EQ, USSValue.literal(""))
            : condition;
    }

    private static List<USSNode> safeBody(List<USSNode> body) {
        if (body == null) {
            return Collections.emptyList();
        }
        List<USSNode> safe = new ArrayList<USSNode>(body.size());
        for (USSNode node : body) {
            if (node != null) {
                safe.add(node);
            }
        }
        return Collections.unmodifiableList(safe);
    }

    private static int clampCount(int count) {
        return Math.max(0, Math.min(MAX_REPEAT_COUNT, count));
    }

    public USSNodeType type() {
        return type;
    }

    public int cmdId() {
        return cmdId;
    }

    public NBTTagCompound params() {
        return params;
    }

    public USSCondition condition() {
        return condition;
    }

    public int count() {
        return count;
    }

    public List<USSNode> body() {
        return body;
    }

    public boolean isCommand() {
        return type == USSNodeType.COMMAND;
    }

    public boolean hasBody() {
        return type != USSNodeType.COMMAND;
    }

    /** The depth of the subtree rooted here (1 = a leaf). */
    public int depth() {
        int d = 1;
        for (USSNode child : body) {
            d = Math.max(d, 1 + child.depth());
        }
        return d;
    }

    /** The number of nodes in the subtree rooted here (including this one). */
    public int subtreeSize() {
        int n = 1;
        for (USSNode child : body) {
            n += child.subtreeSize();
        }
        return n;
    }

    // region NBT

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(TAG_TYPE, type.getId());
        switch (type) {
            case COMMAND:
                nbt.setInteger(TAG_CMD, cmdId);
                nbt.setTag(TAG_PARAMS, params.copy());
                break;
            case IF:
            case WHILE:
                nbt.setTag(TAG_CONDITION, condition.writeToNBT());
                break;
            case REPEAT:
                nbt.setInteger(TAG_COUNT, count);
                break;
            default:
                break;
        }
        if (hasBody() && !body.isEmpty()) {
            NBTTagList list = new NBTTagList();
            for (USSNode child : body) {
                list.appendTag(child.writeToNBT());
            }
            nbt.setTag(TAG_BODY, list);
        }
        return nbt;
    }

    /**
     * Read one node (recursively) with the program's caps enforced through {@code used}[0] (the running node
     * count).
     *
     * @param nbt   the node compound (may be null)
     * @param depth the node's depth (root level = 1)
     * @param used  one-element array: the running total of nodes read (including this one)
     * @return the node, or null on structural corruption / unknown type / cap breach (the caller drops the program)
     */
    public static USSNode readFromNBT(NBTTagCompound nbt, int depth, int[] used) {
        if (nbt == null || depth > USSProgram.MAX_DEPTH || !nbt.hasKey(TAG_TYPE)) {
            return null; // a node without a type is structural corruption (getInteger would silently default to 0 =
                         // COMMAND)
        }
        used[0]++;
        if (used[0] > USSProgram.MAX_NODES) {
            return null;
        }
        USSNodeType type = USSNodeType.byId(nbt.getInteger(TAG_TYPE));
        if (type == null) {
            return null;
        }
        switch (type) {
            case COMMAND:
                return command(nbt.getInteger(TAG_CMD), nbt.getCompoundTag(TAG_PARAMS));
            case IF:
            case WHILE: {
                List<USSNode> body = readBody(nbt, depth, used);
                if (body == null) {
                    return null;
                }
                return type == USSNodeType.IF
                    ? ifNode(USSCondition.readFromNBT(nbt.getCompoundTag(TAG_CONDITION)), body)
                    : whileNode(USSCondition.readFromNBT(nbt.getCompoundTag(TAG_CONDITION)), body);
            }
            case REPEAT: {
                List<USSNode> body = readBody(nbt, depth, used);
                if (body == null) {
                    return null;
                }
                return repeat(nbt.getInteger(TAG_COUNT), body);
            }
            default:
                return null;
        }
    }

    /**
     * @return the body nodes, or null on corruption / cap breach (null children are rejected — a body with a hole
     *         is a broken program). Note: the typed {@code getTagList(key, 10)} accessor is deliberately avoided —
     *         in 1.7.10 it returns an EMPTY list whenever the stored element type differs from 10, silently hiding
     *         exactly the corruption we must reject.
     */
    private static List<USSNode> readBody(NBTTagCompound nbt, int depth, int[] used) {
        List<USSNode> body = new ArrayList<USSNode>();
        NBTBase stored = nbt.getTag(TAG_BODY);
        if (stored == null) {
            return body; // no body tag → empty body (a legal shape: e.g. WHILE with an empty body)
        }
        if (!(stored instanceof NBTTagList)) {
            return null; // a body that is not a list is structural corruption
        }
        NBTTagList list = (NBTTagList) stored;
        for (int i = 0; i < list.tagCount(); i++) {
            Object tag = list.tagList.get(i); // raw element (1.7.10 NBTTagList has no untyped get(int))
            if (!(tag instanceof NBTTagCompound)) {
                return null;
            }
            USSNode child = readFromNBT((NBTTagCompound) tag, depth + 1, used);
            if (child == null) {
                return null;
            }
            body.add(child);
        }
        return body;
    }

    // endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof USSNode)) return false;
        USSNode other = (USSNode) o;
        return type == other.type && cmdId == other.cmdId
            && count == other.count
            && Objects.equals(condition, other.condition)
            && body.equals(other.body);
    }

    @Override
    public int hashCode() {
        int h = type.hashCode();
        h = 31 * h + cmdId;
        h = 31 * h + count;
        h = 31 * h + Objects.hashCode(condition);
        h = 31 * h + body.hashCode();
        return h;
    }

    @Override
    public String toString() {
        switch (type) {
            case COMMAND:
                return "CMD" + cmdId;
            case IF:
                return "IF " + condition;
            case WHILE:
                return "WHILE " + condition;
            case REPEAT:
                return "REPEAT " + count;
            default:
                return type.name();
        }
    }
}
