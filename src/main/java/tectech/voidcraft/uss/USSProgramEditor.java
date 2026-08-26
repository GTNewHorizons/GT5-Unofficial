package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The pure program EDITOR (pass-33, programming UI — the bare-JVM core of the Controller's program editor).
 *
 * <p>
 * The editor applies ONE edit to an immutable {@link USSProgram} and returns either the NEW program (accepted) or
 * a REJECTION with a human-readable reason (shown in the GUI; the program is left untouched). It is the single
 * place where program shape is validated, so every path to a stored program (GUI actions, preset buttons, future
 * remote writes) enforces the same rules:
 * <ul>
 * <li>caps — {@link USSProgram#MAX_NODES} total nodes, {@link USSProgram#MAX_DEPTH} nesting,
 * {@link USSProgram#MAX_LITERAL_LENGTH} per literal, {@link USSNode#MAX_REPEAT_COUNT} per REPEAT;</li>
 * <li>parameter schema — each command's known params, their NBT types (the executor reads
 * {@code getInteger}/{@code getLong}/{@code getString}), and their ranges (slots 0..255, MOVE targets from the
 * known set, WAIT ticks ≥ 0);</li>
 * <li>structural sanity — flow nodes for condition/count edits, bodies for list edits, valid node addresses.</li>
 * </ul>
 *
 * <p>
 * <b>Path semantics</b> (the shared vocabulary between this class and the GUI):
 * <ul>
 * <li>a NODE is addressed by {@code int[] path}: {@code [i]} = the i-th root node; {@code [i, k]} = the k-th node
 * in the body of the i-th root node; {@code [i, k, m]} = one level deeper, and so on (indices 0-based);</li>
 * <li>a LIST is addressed by the ADDRESS OF ITS OWNER: an empty {@code []} = the root list;
 * {@code [i]} = the body of the i-th root node; {@code [i, k]} = the body of node {@code [i, k]}</li>
 * <li>every edit is a pure function: the input program is never mutated, the result is a new {@link USSProgram}.</li>
 * </ul>
 *
 * <p>
 * Bare-JVM (NBT + JDK only) — unit-testable (see {@code USSProgramEditorTest}).
 */
public final class USSProgramEditor {

    // region Result

    /**
     * The outcome of one edit: either the accepted NEW program ({@link #program()}) or a rejection
     * ({@link #error()} — a short, user-visible reason).
     */
    public static final class Result {

        private final USSProgram program; // null when rejected
        private final String error; // null when accepted

        private Result(USSProgram program, String error) {
            this.program = program;
            this.error = error;
        }

        static Result accept(USSProgram program) {
            return new Result(program, null);
        }

        static Result reject(String message) {
            return new Result(null, message);
        }

        /** @return true when the edit was applied ({@link #program()} holds the new program) */
        public boolean accepted() {
            return error == null;
        }

        /** @return the new program (accepted edits) — null when rejected */
        public USSProgram program() {
            return program;
        }

        /** @return the rejection reason (rejected edits) — null when accepted */
        public String error() {
            return error;
        }
    }

    // endregion

    // region MOVE target whitelist (the editor's schema knows the full target set — including pass-33's RANDOM_PLANET)

    private static final Set<String> KNOWN_TARGETS = Collections.unmodifiableSet(
        new HashSet<String>(
            Arrays.asList(
                USSProgramDefaults.TARGET_STAR,
                USSProgramDefaults.TARGET_PLANET,
                USSProgramDefaults.TARGET_NEAREST_PLANET,
                USSProgramDefaults.TARGET_RANDOM_PLANET,
                USSProgramDefaults.TARGET_RIPPLE,
                USSProgramDefaults.TARGET_RIPPLE_UNSCANNED,
                USSProgramDefaults.TARGET_SHIP,
                USSProgramDefaults.TARGET_HOME)));

    private static boolean isKnownTarget(String target) {
        return target != null && KNOWN_TARGETS.contains(target);
    }

    // endregion

    private USSProgramEditor() {}

    // region insert / remove / move (structural edits)

    /**
     * Insert a node into the list addressed by {@code listPath} (see the class javadoc for path semantics) BEFORE
     * position {@code index} (0 = first, {@code listSize} = append).
     *
     * @param program  the program (null → rejected)
     * @param listPath the LIST owner address (empty/nil = the root list; a node address = that node's body)
     * @param index    the insert position (0..listSize)
     * @param node     the node to insert (null → rejected)
     * @return the new program, or a rejection (null inputs, out-of-range index, a node without a body, a cap breach)
     */
    public static Result insert(USSProgram program, int[] listPath, int index, USSNode node) {
        if (program == null) {
            return Result.reject("no program");
        }
        if (node == null) {
            return Result.reject("no node to insert");
        }
        List<USSNode> list = resolveList(program, listPath);
        if (list == null) {
            if (listPath == null || listPath.length == 0) {
                return Result.reject("no program");
            }
            return (nodeAt(program, listPath) == null) ? Result.reject("node not found")
                : Result.reject("target has no body");
        }
        if (index < 0 || index > list.size()) {
            return Result.reject("insert position out of range");
        }
        List<USSNode> newList = new ArrayList<USSNode>(list);
        newList.add(index, node);
        USSProgram next = USSProgram.of(rebuildRoots(program, listPath, newList));
        return checkCaps(next);
    }

    /**
     * Remove the node at {@code path} (its whole subtree goes with it).
     *
     * @param program the program
     * @param path    the node address (non-empty)
     * @return the new program, or a rejection (node not found)
     */
    public static Result remove(USSProgram program, int[] path) {
        if (program == null || path == null || path.length == 0) {
            return Result.reject("no node selected");
        }
        USSNode node = nodeAt(program, path);
        if (node == null) {
            return Result.reject("node not found");
        }
        int[] ownerPath = Arrays.copyOf(path, path.length - 1); // may be empty = root list
        List<USSNode> list = resolveList(program, ownerPath);
        if (list == null) {
            return Result.reject("target has no body");
        }
        int index = path[path.length - 1];
        if (index < 0 || index >= list.size()) {
            return Result.reject("node not found");
        }
        List<USSNode> newList = new ArrayList<USSNode>(list);
        newList.remove(index);
        return Result.accept(USSProgram.of(rebuildRoots(program, ownerPath, newList)));
    }

    /**
     * Move the node at {@code path} one position UP (toward the start) or DOWN (toward the end) in its list.
     *
     * @param program the program
     * @param path    the node address (non-empty)
     * @param up      true = swap with the previous node, false = swap with the next one
     * @return the new program, or a rejection (node not found, or it is already first/last)
     */
    public static Result move(USSProgram program, int[] path, boolean up) {
        if (program == null || path == null || path.length == 0) {
            return Result.reject("no node selected");
        }
        USSNode node = nodeAt(program, path);
        if (node == null) {
            return Result.reject("node not found");
        }
        int[] ownerPath = Arrays.copyOf(path, path.length - 1); // may be empty = root list
        List<USSNode> list = resolveList(program, ownerPath);
        if (list == null) {
            return Result.reject("target has no body");
        }
        int i = path[path.length - 1];
        int j = up ? i - 1 : i + 1;
        if (j < 0) {
            return Result.reject("already first in its list");
        }
        if (j >= list.size()) {
            return Result.reject("already last in its list");
        }
        List<USSNode> newList = new ArrayList<USSNode>(list);
        USSNode tmp = newList.get(i);
        newList.set(i, newList.get(j));
        newList.set(j, tmp);
        return Result.accept(USSProgram.of(rebuildRoots(program, ownerPath, newList)));
    }

    /**
     * Copy the node at {@code path} (a deep copy of its whole subtree) and insert the copy immediately AFTER the
     * original in its list.
     *
     * @param program the program
     * @param path    the node address (non-empty)
     * @return the new program, or a rejection (no node selected, node not found, or the copy breaks the node cap)
     */
    public static Result copy(USSProgram program, int[] path) {
        if (program == null || path == null || path.length == 0) {
            return Result.reject("no node selected");
        }
        USSNode node = nodeAt(program, path);
        if (node == null) {
            return Result.reject("node not found");
        }
        int[] ownerPath = Arrays.copyOf(path, path.length - 1); // may be empty = root list
        List<USSNode> list = resolveList(program, ownerPath);
        if (list == null) {
            return Result.reject("target has no body");
        }
        int i = path[path.length - 1];
        if (i < 0 || i >= list.size()) {
            return Result.reject("node not found");
        }
        List<USSNode> newList = new ArrayList<USSNode>(list);
        newList.add(i + 1, copyNode(node));
        return checkCaps(USSProgram.of(rebuildRoots(program, ownerPath, newList)));
    }

    /** A deep copy of one node (params defensively copied; condition values are immutable — shared safely). */
    private static USSNode copyNode(USSNode node) {
        switch (node.type()) {
            case IF:
                return USSNode.ifNode(node.condition(), copyBody(node));
            case WHILE:
                return USSNode.whileNode(node.condition(), copyBody(node));
            case REPEAT:
                return USSNode.repeat(node.count(), copyBody(node));
            case COMMAND:
            default:
                return USSNode.command(node.cmdId(), node.params()); // the factory copies the params
        }
    }

    private static List<USSNode> copyBody(USSNode node) {
        List<USSNode> out = new ArrayList<USSNode>(
            node.body()
                .size());
        for (USSNode child : node.body()) {
            out.add(copyNode(child));
        }
        return out;
    }

    // endregion

    // region content edits (params / count / condition)

    /**
     * Set ONE parameter of the COMMAND at {@code path}. The raw value is a string (the GUI sends text); the editor
     * parses it into the NBT type the executor reads for THAT command/param:
     * <ul>
     * <li>MOVE: {@code target} (a known {@link USSProgramDefaults} target string), {@code index} (int ≥ 0);</li>
     * <li>WRITE: {@code value} (string, ≤ 255 chars), {@code slot} (0..255);</li>
     * <li>READ: {@code from} / {@code to} (0..255);</li>
     * <li>WAIT: {@code ticks} (long, 0..2147483647).</li>
     * </ul>
     *
     * @param program  the program
     * @param path     the node address (a COMMAND node)
     * @param key      the param key (one of the command's known keys)
     * @param rawValue the value as text (null allowed where the schema accepts empty — a null WRITE value is "")
     * @return the new program, or a rejection (unknown param, bad value, non-command node)
     */
    public static Result setParam(USSProgram program, int[] path, String key, String rawValue) {
        if (program == null || key == null || key.isEmpty()) {
            return Result.reject("no parameter selected");
        }
        USSNode node = nodeAt(program, path);
        if (node == null) {
            return Result.reject("node not found");
        }
        if (!node.isCommand()) {
            return Result.reject("only commands take parameters");
        }
        String schemaError = paramSchemaError(node.cmdId(), key);
        if (schemaError != null) {
            return Result.reject(schemaError);
        }
        String valueError = paramValueError(node.cmdId(), key, rawValue);
        if (valueError != null) {
            return Result.reject(valueError);
        }
        NBTTagCompound params = (NBTTagCompound) node.params()
            .copy(); // 1.7.10: copy() returns NBTBase
        writeParam(params, node.cmdId(), key, rawValue);
        USSNode newNode = USSNode.command(node.cmdId(), params);
        return Result.accept(USSProgram.of(replaceAt(program, path, newNode)));
    }

    /**
     * Set ONE parameter of the COMMAND at {@code path} to a USS VALUE REFERENCE (a VAR slot, a literal or a
     * STAT) — the GUI "assign a global USS value" op (pass 33). Only a WRITE {@code value} param accepts a
     * reference (the executor resolves it at run time); condition sides take references through
     * {@link #setConditionSide} instead.
     *
     * @return the new program, or a rejection (non-command node, non-WRITE command, non-{@code value} key)
     */
    public static Result setParam(USSProgram program, int[] path, String key, USSValue value) {
        if (program == null || key == null || key.isEmpty()) {
            return Result.reject("no parameter selected");
        }
        if (value == null) {
            return Result.reject("no value selected");
        }
        USSNode node = nodeAt(program, path);
        if (node == null) {
            return Result.reject("node not found");
        }
        if (!node.isCommand()) {
            return Result.reject("only commands take parameters");
        }
        if (node.cmdId() != USSCommand.WRITE || !USSCommandWrite.PARAM_VALUE.equals(key)) {
            return Result.reject("only a WRITE value takes a USS reference");
        }
        NBTTagCompound params = (NBTTagCompound) node.params()
            .copy(); // 1.7.10: copy() returns NBTBase
        params.removeTag(USSCommandWrite.PARAM_VALUE); // replace a previous literal
        params.setTag(USSCommandWrite.PARAM_VALUE, value.writeToNBT());
        USSNode newNode = USSNode.command(node.cmdId(), params);
        return Result.accept(USSProgram.of(replaceAt(program, path, newNode)));
    }

    /**
     * Set the iteration COUNT of the REPEAT node at {@code path} (0..{@link USSNode#MAX_REPEAT_COUNT};
     * 0 = the body never runs).
     *
     * @return the new program, or a rejection (not a REPEAT node, count out of range)
     */
    public static Result setCount(USSProgram program, int[] path, int count) {
        if (program == null) {
            return Result.reject("no program");
        }
        USSNode node = nodeAt(program, path);
        if (node == null) {
            return Result.reject("node not found");
        }
        if (node.type() != USSNodeType.REPEAT) {
            return Result.reject("not a REPEAT block");
        }
        if (count < 0 || count > USSNode.MAX_REPEAT_COUNT) {
            return Result.reject("repeat count must be 0.." + USSNode.MAX_REPEAT_COUNT);
        }
        USSNode newNode = USSNode.repeat(count, node.body());
        return Result.accept(USSProgram.of(replaceAt(program, path, newNode)));
    }

    /**
     * Set one SIDE of the condition of the IF / WHILE node at {@code path}.
     *
     * @param program the program
     * @param path    the node address (an IF or WHILE node)
     * @param left    true = replace the left side, false = the right side
     * @param value   the new value (a literal / VAR slot / STAT — null becomes the empty literal)
     * @return the new program, or a rejection (not an IF/WHILE node)
     */
    public static Result setConditionSide(USSProgram program, int[] path, boolean left, USSValue value) {
        if (program == null) {
            return Result.reject("no program");
        }
        USSNode node = nodeAt(program, path);
        if (node == null) {
            return Result.reject("node not found");
        }
        if (node.type() != USSNodeType.IF && node.type() != USSNodeType.WHILE) {
            return Result.reject("not an IF/WHILE block");
        }
        USSCondition c = node.condition() == null ? USSCondition.of(null, null, null) : node.condition();
        USSCondition next = USSCondition.of(left ? value : c.left(), c.op(), left ? c.right() : value);
        USSNode newNode = (node.type() == USSNodeType.IF) ? USSNode.ifNode(next, node.body())
            : USSNode.whileNode(next, node.body());
        return Result.accept(USSProgram.of(replaceAt(program, path, newNode)));
    }

    /**
     * Set the OPERATOR of the condition of the IF / WHILE node at {@code path}.
     *
     * @return the new program, or a rejection (not an IF/WHILE node, or no operator)
     */
    public static Result setOp(USSProgram program, int[] path, USSConditionOp op) {
        if (program == null) {
            return Result.reject("no program");
        }
        if (op == null) {
            return Result.reject("no operator selected");
        }
        USSNode node = nodeAt(program, path);
        if (node == null) {
            return Result.reject("node not found");
        }
        if (node.type() != USSNodeType.IF && node.type() != USSNodeType.WHILE) {
            return Result.reject("not an IF/WHILE block");
        }
        USSCondition c = node.condition() == null ? USSCondition.of(null, null, null) : node.condition();
        USSCondition next = USSCondition.of(c.left(), op, c.right());
        USSNode newNode = (node.type() == USSNodeType.IF) ? USSNode.ifNode(next, node.body())
            : USSNode.whileNode(next, node.body());
        return Result.accept(USSProgram.of(replaceAt(program, path, newNode)));
    }

    // endregion

    // region replace (preset chips / clear)

    /**
     * Validate a WHOLE replacement program (a preset chip, a Clear, or any program about to be stored) and accept
     * it unchanged: caps + every command's parameter schema (including MOVE targets and slot ranges) + literal
     * lengths. This is the gate for programs that bypass the single-edit API.
     *
     * @param replacement the candidate program (null → rejected)
     * @return the accepted program (the SAME instance — it is already immutable), or a rejection with the first
     *         problem found
     */
    public static Result apply(USSProgram replacement) {
        if (replacement == null) {
            return Result.reject("no program");
        }
        if (replacement.nodeCount() > USSProgram.MAX_NODES) {
            return Result.reject("program exceeds the " + USSProgram.MAX_NODES + "-node cap");
        }
        if (replacement.depth() > USSProgram.MAX_DEPTH) {
            return Result.reject("program exceeds the " + USSProgram.MAX_DEPTH + "-level nesting cap");
        }
        for (USSNode root : replacement.nodes()) {
            String error = validateNode(root);
            if (error != null) {
                return Result.reject(error);
            }
        }
        return Result.accept(replacement);
    }

    // endregion

    // region internals — path resolution + immutable rebuild

    /**
     * @param program the program (null → null)
     * @param path    the node address (null/empty → null)
     * @return the node, or null when the address does not exist (out of range, or an intermediate node without a body)
     */
    private static USSNode nodeAt(USSProgram program, int[] path) {
        if (program == null || path == null || path.length == 0) {
            return null;
        }
        if (path[0] < 0 || path[0] >= program.nodes()
            .size()) {
            return null;
        }
        USSNode node = program.nodes()
            .get(path[0]);
        for (int d = 1; d < path.length; d++) {
            int i = path[d];
            if (i < 0 || !node.hasBody()
                || i >= node.body()
                    .size()) {
                return null;
            }
            node = node.body()
                .get(i);
        }
        return node;
    }

    /**
     * Resolve the LIST addressed by {@code listPath} (a fresh mutable copy): empty path = the root list, a node
     * address = that node's body.
     *
     * @return the list, or null when the owner does not exist / has no body
     */
    private static List<USSNode> resolveList(USSProgram program, int[] listPath) {
        if (program == null) {
            return null;
        }
        if (listPath == null || listPath.length == 0) {
            return new ArrayList<USSNode>(program.nodes());
        }
        USSNode owner = nodeAt(program, listPath);
        if (owner == null || !owner.hasBody()) {
            return null;
        }
        return new ArrayList<USSNode>(owner.body());
    }

    /**
     * Rebuild the root list with {@code newList} installed at {@code listPath} (empty = the root list itself).
     */
    private static List<USSNode> rebuildRoots(USSProgram program, int[] listPath, List<USSNode> newList) {
        if (listPath == null || listPath.length == 0) {
            return newList;
        }
        List<USSNode> roots = new ArrayList<USSNode>(program.nodes());
        USSNode owner = roots.get(listPath[0]);
        USSNode newOwner = (listPath.length == 1) ? withBody(owner, newList) // the root node's OWN body is the list
            : replaceInBody(owner, subPath(listPath, 1), newList);
        roots.set(listPath[0], newOwner);
        return roots;
    }

    /**
     * Rebuild {@code node}'s subtree with {@code newList} installed in the BODY of the child at
     * {@code path[0]} (recursing along {@code path}).
     */
    private static USSNode replaceInBody(USSNode node, int[] path, List<USSNode> newList) {
        List<USSNode> body = new ArrayList<USSNode>(node.body());
        USSNode child = body.get(path[0]);
        USSNode newChild = (path.length == 1) ? withBody(child, newList)
            : replaceInBody(child, subPath(path, 1), newList);
        body.set(path[0], newChild);
        return withBody(node, body);
    }

    /**
     * Rebuild the root list with {@code replacement} installed at the node address {@code path}.
     */
    private static List<USSNode> replaceAt(USSProgram program, int[] path, USSNode replacement) {
        List<USSNode> roots = new ArrayList<USSNode>(program.nodes());
        USSNode owner = roots.get(path[0]);
        roots.set(path[0], (path.length == 1) ? replacement : replaceChildInBody(owner, subPath(path, 1), replacement));
        return roots;
    }

    private static USSNode replaceChildInBody(USSNode node, int[] path, USSNode replacement) {
        List<USSNode> body = new ArrayList<USSNode>(node.body());
        USSNode child = body.get(path[0]);
        USSNode newChild = (path.length == 1) ? replacement : replaceChildInBody(child, subPath(path, 1), replacement);
        body.set(path[0], newChild);
        return withBody(node, body);
    }

    private static int[] subPath(int[] path, int from) {
        return Arrays.copyOfRange(path, from, path.length);
    }

    /** Rebuild a flow node with a new body (a command's "body" is empty and this is a no-op for it). */
    private static USSNode withBody(USSNode node, List<USSNode> body) {
        switch (node.type()) {
            case IF:
                return USSNode.ifNode(node.condition(), body);
            case WHILE:
                return USSNode.whileNode(node.condition(), body);
            case REPEAT:
                return USSNode.repeat(node.count(), body);
            default:
                return node; // defensive — callers only pass flow nodes here
        }
    }

    /** Cap check for programs that GREW (insert) — removal / reordering / content edits can never breach caps. */
    private static Result checkCaps(USSProgram next) {
        if (next.nodeCount() > USSProgram.MAX_NODES) {
            return Result.reject("program exceeds the " + USSProgram.MAX_NODES + "-node cap");
        }
        if (next.depth() > USSProgram.MAX_DEPTH) {
            return Result.reject("program exceeds the " + USSProgram.MAX_DEPTH + "-level nesting cap");
        }
        return Result.accept(next);
    }

    // endregion

    // region internals — parameter schema (the editor's typed view of the command params)

    /** @return null when {@code key} is a known param of the command, else the rejection reason */
    private static String paramSchemaError(int cmdId, String key) {
        switch (cmdId) {
            case USSCommand.MOVE:
                if (USSProgramDefaults.PARAM_TARGET.equals(key) || USSProgramDefaults.PARAM_INDEX.equals(key)) {
                    return null;
                }
                return "unknown MOVE parameter '" + key + "'";
            case USSCommand.WRITE:
                if (USSCommandWrite.PARAM_VALUE.equals(key) || USSCommandWrite.PARAM_SLOT.equals(key)) {
                    return null;
                }
                return "unknown WRITE parameter '" + key + "'";
            case USSCommand.READ:
                if (USSCommandRead.PARAM_FROM.equals(key) || USSCommandRead.PARAM_TO.equals(key)) {
                    return null;
                }
                return "unknown READ parameter '" + key + "'";
            case USSCommand.WAIT:
                if (USSCommandWait.PARAM_TICKS.equals(key)) {
                    return null;
                }
                return "unknown WAIT parameter '" + key + "'";
            default:
                return "this command takes no parameters";
        }
    }

    /** @return null when the value is valid for that command/param, else the rejection reason */
    private static String paramValueError(int cmdId, String key, String raw) {
        if (cmdId == USSCommand.MOVE && USSProgramDefaults.PARAM_TARGET.equals(key)) {
            return isKnownTarget(raw) ? null : "unknown target '" + raw + "'";
        }
        if (cmdId == USSCommand.MOVE && USSProgramDefaults.PARAM_INDEX.equals(key)) {
            Long v = parseLong(raw);
            return (v != null && v >= 0L) ? null : "index must be a number ≥ 0";
        }
        if (cmdId == USSCommand.WRITE && USSCommandWrite.PARAM_VALUE.equals(key)) {
            String s = (raw == null) ? "" : raw;
            return s.length() <= USSProgram.MAX_LITERAL_LENGTH ? null
                : "value exceeds " + USSProgram.MAX_LITERAL_LENGTH + " characters";
        }
        if ((cmdId == USSCommand.WRITE && USSCommandWrite.PARAM_SLOT.equals(key)) || (cmdId == USSCommand.READ
            && (USSCommandRead.PARAM_FROM.equals(key) || USSCommandRead.PARAM_TO.equals(key)))) {
            Long v = parseLong(raw);
            return (v != null && v >= 0L && v <= USSVariableSpace.SLOT_COUNT - 1) ? null
                : "slot must be 0.." + (USSVariableSpace.SLOT_COUNT - 1);
        }
        if (cmdId == USSCommand.WAIT && USSCommandWait.PARAM_TICKS.equals(key)) {
            Long v = parseLong(raw);
            return (v != null && v >= 0L && v <= USSCommandWait.MAX_WAIT_TICKS) ? null
                : "ticks must be 0.." + USSCommandWait.MAX_WAIT_TICKS;
        }
        return "bad parameter value";
    }

    /** Write the param with the NBT type the executor reads. */
    private static void writeParam(NBTTagCompound params, int cmdId, String key, String raw) {
        if (cmdId == USSCommand.MOVE && USSProgramDefaults.PARAM_TARGET.equals(key)) {
            params.setString(USSProgramDefaults.PARAM_TARGET, raw.trim());
        } else if (cmdId == USSCommand.MOVE && USSProgramDefaults.PARAM_INDEX.equals(key)) {
            params.setInteger(USSProgramDefaults.PARAM_INDEX, parseLong(raw).intValue());
        } else if (cmdId == USSCommand.WRITE && USSCommandWrite.PARAM_VALUE.equals(key)) {
            params.setString(USSCommandWrite.PARAM_VALUE, raw == null ? "" : raw);
        } else if (cmdId == USSCommand.WRITE && USSCommandWrite.PARAM_SLOT.equals(key)) {
            params.setInteger(USSCommandWrite.PARAM_SLOT, parseLong(raw).intValue());
        } else if (cmdId == USSCommand.READ && USSCommandRead.PARAM_FROM.equals(key)) {
            params.setInteger(USSCommandRead.PARAM_FROM, parseLong(raw).intValue());
        } else if (cmdId == USSCommand.READ && USSCommandRead.PARAM_TO.equals(key)) {
            params.setInteger(USSCommandRead.PARAM_TO, parseLong(raw).intValue());
        } else if (cmdId == USSCommand.WAIT && USSCommandWait.PARAM_TICKS.equals(key)) {
            params.setLong(USSCommandWait.PARAM_TICKS, parseLong(raw).longValue());
        }
    }

    private static Long parseLong(String s) {
        if (s == null) {
            return null;
        }
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Validate one node's command params (for {@link #apply}); flow-node bodies are validated recursively.
     *
     * @return null when the node is valid, else the first problem found
     */
    private static String validateNode(USSNode node) {
        if (node == null) {
            return "corrupt node";
        }
        if (node.isCommand()) {
            NBTTagCompound p = node.params();
            switch (node.cmdId()) {
                case USSCommand.MOVE: {
                    String target = p.getString(USSProgramDefaults.PARAM_TARGET);
                    if (target.isEmpty()) {
                        return "a MOVE has no target";
                    }
                    if (!isKnownTarget(target)) {
                        return "a MOVE has an unknown target '" + target + "'";
                    }
                    if (p.hasKey(USSProgramDefaults.PARAM_INDEX) && p.getInteger(USSProgramDefaults.PARAM_INDEX) < 0) {
                        return "a MOVE index must be ≥ 0";
                    }
                    return null;
                }
                case USSCommand.WRITE: {
                    if (p.hasKey(USSCommandWrite.PARAM_SLOT) && (p.getInteger(USSCommandWrite.PARAM_SLOT) < 0
                        || p.getInteger(USSCommandWrite.PARAM_SLOT) > USSVariableSpace.SLOT_COUNT - 1)) {
                        return "a WRITE slot must be 0.." + (USSVariableSpace.SLOT_COUNT - 1);
                    }
                    // getString() reads the string for an NBTTagString and "" for a nested USSValue compound
                    // (allowed — the executor resolves it) or any non-string tag; only a real string can breach the
                    // cap.
                    if (p.getString(USSCommandWrite.PARAM_VALUE)
                        .length() > USSProgram.MAX_LITERAL_LENGTH) {
                        return "a WRITE value exceeds " + USSProgram.MAX_LITERAL_LENGTH + " characters";
                    }
                    return null;
                }
                case USSCommand.READ: {
                    if (p.hasKey(USSCommandRead.PARAM_FROM) && (p.getInteger(USSCommandRead.PARAM_FROM) < 0
                        || p.getInteger(USSCommandRead.PARAM_FROM) > USSVariableSpace.SLOT_COUNT - 1)) {
                        return "a READ from-slot must be 0.." + (USSVariableSpace.SLOT_COUNT - 1);
                    }
                    if (p.hasKey(USSCommandRead.PARAM_TO) && (p.getInteger(USSCommandRead.PARAM_TO) < 0
                        || p.getInteger(USSCommandRead.PARAM_TO) > USSVariableSpace.SLOT_COUNT - 1)) {
                        return "a READ to-slot must be 0.." + (USSVariableSpace.SLOT_COUNT - 1);
                    }
                    return null;
                }
                case USSCommand.WAIT:
                    if (p.hasKey(USSCommandWait.PARAM_TICKS) && p.getLong(USSCommandWait.PARAM_TICKS) < 0L) {
                        return "a WAIT ticks value must be ≥ 0";
                    }
                    return null;
                default:
                    return null; // WORK / STOP (and future commands) — unknown extra keys are ignored by the executor
            }
        }
        for (USSNode child : node.body()) {
            String error = validateNode(child);
            if (error != null) {
                return error;
            }
        }
        return null;
    }

    // endregion
}
