package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * The ONE server-side entry for program edits from the Controller GUI (pass 33 UI, UI-2). Parses one ACTION
 * JSON and applies it with {@link USSProgramEditor}; the result carries the new program (accepted) or a
 * user-visible rejection reason. Pure — bare JVM testable (see {@code USSProgramSyncTest}).
 *
 * <p>
 * Action JSON (everything the GUI sends is text):
 * <ul>
 * <li>{@code {"op":"insert","path":[0],"index":1,"node":{"t":0,"c":0,"p":{"target":"HOME"}}}};</li>
 * <li>{@code {"op":"remove","path":[0]}};</li>
 * <li>{@code {"op":"move","path":[0],"up":true}};</li>
 * <li>{@code {"op":"copy","path":[0]}} (duplicate the block right after itself);</li>
 * <li>{@code {"op":"param","path":[0],"key":"target","value":"STAR"}} (literal) or
 * {@code {"op":"param","path":[0],"key":"value","var":17}} (assign a USS slot reference to a WRITE value);</li>
 * <li>{@code {"op":"count","path":[0],"value":5}};</li>
 * <li>{@code {"op":"cond","path":[0],"side":0,"lit":"x"}} | {@code …,"var":3} | {@code …,"stat":5};</li>
 * <li>{@code {"op":"condop","path":[0],"operator":"EQ"}} (EQ / NEQ / LT / GT);</li>
 * <li>{@code {"op":"apply","preset":"miner"|"starlifter"|"explorer"|"clear"}}.</li>
 * </ul>
 *
 * Node spec (the insert {@code node}): {@code {"t":0,"c":<cmdId>,"p":{…}}} |
 * {@code {"t":1|"t":2,"l":<value>,"op":<0-3>,"r":<value>,"b":[…]}} | {@code {"t":3,"n":N,"b":[…]}}.
 * Value spec: {@code {"k":0,"s":"lit"}} | {@code {"k":1,"v":17}} | {@code {"k":2,"st":5}}.
 */
public final class USSProgramSync {

    /** The outcome of one action: the resulting program (accepted) or a user-visible rejection reason. */
    public static final class Outcome {

        public final USSProgram program;
        public final boolean ok;
        public final String message;

        private Outcome(USSProgram program, boolean ok, String message) {
            this.program = program;
            this.ok = ok;
            this.message = message;
        }

        public static Outcome accepted(USSProgram program) {
            return new Outcome(program, true, null);
        }

        public static Outcome rejected(String message) {
            return new Outcome(null, false, message);
        }
    }

    private USSProgramSync() {}

    /**
     * Apply one action JSON to {@code current}. NEVER mutates {@code current}; never throws (bad JSON →
     * rejection).
     */
    public static Outcome handle(USSProgram current, String actionJson) {
        try {
            JsonObject a = new JsonParser().parse(actionJson)
                .getAsJsonObject();
            JsonElement opEl = a.get("op");
            String op = opEl == null ? "" : opEl.getAsString();
            switch (op) {
                case "insert": {
                    USSNode node = readNode(a.getAsJsonObject("node"));
                    if (node == null) {
                        return Outcome.rejected("bad node spec");
                    }
                    return map(
                        USSProgramEditor.insert(
                            current,
                            readPath(a.get("path")),
                            a.get("index")
                                .getAsInt(),
                            node));
                }
                case "remove":
                    return map(USSProgramEditor.remove(current, readPath(a.get("path"))));
                case "move":
                    return map(
                        USSProgramEditor.move(
                            current,
                            readPath(a.get("path")),
                            a.get("up")
                                .getAsBoolean()));
                case "copy":
                    return map(USSProgramEditor.copy(current, readPath(a.get("path"))));
                case "param": {
                    String key = a.get("key")
                        .getAsString();
                    if (a.has("var")) {
                        return map(
                            USSProgramEditor.setParam(
                                current,
                                readPath(a.get("path")),
                                key,
                                USSValue.variable(
                                    a.get("var")
                                        .getAsInt())));
                    }
                    String value = a.has("value") ? a.get("value")
                        .getAsString() : "";
                    return map(USSProgramEditor.setParam(current, readPath(a.get("path")), key, value));
                }
                case "count":
                    return map(
                        USSProgramEditor.setCount(
                            current,
                            readPath(a.get("path")),
                            a.get("value")
                                .getAsInt()));
                case "cond": {
                    int side = a.get("side")
                        .getAsInt();
                    if (side != 0 && side != 1) {
                        return Outcome.rejected("bad side (0 = left, 1 = right)");
                    }
                    boolean left = side == 0;
                    USSValue value;
                    if (a.has("var")) {
                        value = USSValue.variable(
                            a.get("var")
                                .getAsInt());
                    } else if (a.has("stat")) {
                        value = USSValue.stat(
                            a.get("stat")
                                .getAsInt());
                    } else if (a.has("lit")) {
                        value = USSValue.literal(
                            a.get("lit")
                                .getAsString());
                    } else {
                        return Outcome.rejected("bad value");
                    }
                    return map(USSProgramEditor.setConditionSide(current, readPath(a.get("path")), left, value));
                }
                case "condop": {
                    // NOTE: the operator field is "operator" — "op" is the action discriminator (Gson keeps the
                    // LAST value for a duplicated key, so the two would collide).
                    JsonElement opvEl = a.get("operator");
                    USSConditionOp opv = opvEl == null ? null
                        : (opvEl.isJsonPrimitive() && ((JsonPrimitive) opvEl).isString()
                            ? USSConditionOp.valueOf(opvEl.getAsString())
                            : USSConditionOp.byId(opvEl.getAsInt()));
                    if (opv == null) {
                        return Outcome.rejected("unknown operator");
                    }
                    return map(USSProgramEditor.setOp(current, readPath(a.get("path")), opv));
                }
                case "apply": {
                    String preset = a.get("preset")
                        .getAsString();
                    USSProgram replacement = "miner".equals(preset) ? USSProgramDefaults.miner()
                        : "starlifter".equals(preset) ? USSProgramDefaults.starlifter()
                            : "explorer".equals(preset) ? USSProgramDefaults.explorer()
                                : "clear".equals(preset) ? USSProgram.empty() : null;
                    if (replacement == null) {
                        return Outcome.rejected("unknown preset");
                    }
                    return map(USSProgramEditor.apply(replacement));
                }
                default:
                    return Outcome.rejected("unknown op '" + op + "'");
            }
        } catch (RuntimeException ex) {
            return Outcome.rejected("bad action");
        }
    }

    private static Outcome map(USSProgramEditor.Result r) {
        return r.accepted() ? Outcome.accepted(r.program()) : Outcome.rejected(r.error());
    }

    private static int[] readPath(JsonElement element) {
        JsonArray array = element.getAsJsonArray();
        int[] path = new int[array.size()];
        for (int i = 0; i < path.length; i++) {
            path[i] = array.get(i)
                .getAsInt();
        }
        return path;
    }

    // region node / value specs

    /**
     * Node spec → node (null when malformed). Bodies are optional (default empty).
     */
    public static USSNode readNode(JsonObject spec) {
        if (spec == null || !spec.has("t")) {
            return null;
        }
        int t = spec.get("t")
            .getAsInt();
        if (t == 0) {
            if (!spec.has("c")) {
                return null;
            }
            NBTTagCompound params = new NBTTagCompound();
            if (spec.has("p")) {
                JsonObject p = spec.getAsJsonObject("p");
                for (Map.Entry<String, JsonElement> entry : p.entrySet()) {
                    JsonElement v = entry.getValue();
                    if (!(v instanceof JsonPrimitive) || ((JsonPrimitive) v).isBoolean()) {
                        return null;
                    }
                    JsonPrimitive prim = (JsonPrimitive) v;
                    if (prim.isString()) {
                        params.setString(entry.getKey(), prim.getAsString());
                    } else {
                        params.setLong(entry.getKey(), prim.getAsLong());
                    }
                }
            }
            return USSNode.command(
                spec.get("c")
                    .getAsInt(),
                params);
        }
        if (t == 1 || t == 2) {
            if (!spec.has("l") || !spec.has("r")) {
                return null;
            }
            USSValue left = readValue(spec.getAsJsonObject("l"));
            USSValue right = readValue(spec.getAsJsonObject("r"));
            if (left == null || right == null) {
                return null;
            }
            USSConditionOp op = spec.has("op") ? USSConditionOp.byId(
                spec.get("op")
                    .getAsInt())
                : USSConditionOp.EQ;
            if (op == null) {
                return null;
            }
            List<USSNode> body = readBody(spec);
            if (body == null) {
                return null;
            }
            USSCondition cond = USSCondition.of(left, op, right);
            return t == 1 ? USSNode.ifNode(cond, body) : USSNode.whileNode(cond, body);
        }
        if (t == 3) {
            int count = spec.has("n") ? spec.get("n")
                .getAsInt() : 1;
            List<USSNode> body = readBody(spec);
            if (body == null) {
                return null;
            }
            return USSNode.repeat(count, body);
        }
        return null;
    }

    private static List<USSNode> readBody(JsonObject spec) {
        List<USSNode> body = new ArrayList<USSNode>();
        if (spec.has("b") && spec.get("b")
            .isJsonArray()) {
            for (JsonElement e : spec.getAsJsonArray("b")) {
                USSNode node = readNode(e.getAsJsonObject());
                if (node == null) {
                    return null;
                }
                body.add(node);
            }
        }
        return body;
    }

    /**
     * Value spec → value (null when malformed): {@code {"k":0,"s":…}} literal | {@code {"k":1,"v":slot}} VAR |
     * {@code {"k":2,"st":id}} STAT.
     */
    public static USSValue readValue(JsonObject v) {
        if (v == null || !v.has("k")) {
            return null;
        }
        int k = v.get("k")
            .getAsInt();
        if (k == 0) {
            return USSValue.literal(
                v.has("s") ? v.get("s")
                    .getAsString() : "");
        }
        if (k == 1) {
            return USSValue.variable(
                v.has("v") ? v.get("v")
                    .getAsInt() : 0);
        }
        if (k == 2) {
            return USSValue.stat(
                v.has("st") ? v.get("st")
                    .getAsInt() : 0);
        }
        return null;
    }

    // endregion
}
