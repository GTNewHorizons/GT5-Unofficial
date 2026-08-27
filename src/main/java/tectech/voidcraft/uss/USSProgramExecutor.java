package tectech.voidcraft.uss;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * The per-ship program EXECUTOR (programming framework, Phase B) — runs a {@link USSProgram} over the ship's
 * lifetime.
 *
 * <p>
 * Contract (user decisions):
 * <ul>
 * <li><b>Pacing</b> — one node step per second: {@link #STEP_TICKS} (20) ticks per node transition (decision #6,
 * "support much larger amount of in-flight Voidcraft"). Long-running commands (MOVE / WORK / WAIT) run in REAL
 * TIME meanwhile — their handlers are polled every tick; the legs themselves tick on the game side. A
 * {@code WHILE(true){…}} body therefore takes at least 20 ticks per node, so a busy-loop can never consume a
 * tick budget.</li>
 * <li><b>Failure → SKIP</b> (decision #3) — a FAILED instruction (unresolvable MOVE target, missing params,
 * unknown command id, refused leg) is logged and the program continues with the NEXT node. There is no HALT
 * state.</li>
 * <li><b>Invisible while — the program repeats</b> — when the root scope exhausts the executor restarts at the
 * FIRST node (an invisible {@code while(true)} around the whole program): a one-command program (a lone WORK)
 * runs forever. The program ENDS — the executor becomes {@link State#COMPLETED} — only on a STOP, an empty
 * program, or a corrupt-cursor fail-safe; at the end nothing else happens: no implicit MOVE HOME, no delivery,
 * no re-emission.</li>
 * </ul>
 *
 * <p>
 * <b>Resumable</b>: the whole cursor (scope stack + active instruction + step timer) serializes to NBT, so a ship
 * mid-while-loop survives save/reload (the game pilot stores this under {@code vc_exec}).
 *
 * <p>
 * <b>Control-flow model</b>: a stack of {@link Scope}s. The root scope holds the program's nodes; an IF / WHILE /
 * REPEAT pushes its body as a new scope WITHOUT advancing past itself — when the body scope exhausts, the executor
 * resolves the owning control node (IF → advance past it; WHILE → re-evaluate, re-loop or advance; REPEAT →
 * counter: next iteration or advance past it). Exactly one node transition per step.
 */
public final class USSProgramExecutor {

    /** Ticks per node step (decision #6 — one node step per second). */
    public static final int STEP_TICKS = 20;

    /** The executor's state. */
    public enum State {

        /** The program is running (instructions still pending, or an instruction in flight). */
        RUNNING(0),
        /** The program ended (a STOP ran, or the program was empty) — the ship/base HOLDS. */
        COMPLETED(1);

        private final int id;

        State(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static State byId(int id) {
            for (State state : values()) {
                if (state.id == id) return state;
            }
            return null;
        }
    }

    /** The kinds of scopes in the cursor (the root + the bodies of IF / WHILE / REPEAT). */
    private enum ScopeKind {

        ROOT(0),
        IF(1),
        WHILE(2),
        REPEAT(3);

        private final int id;

        ScopeKind(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static ScopeKind byId(int id) {
            for (ScopeKind kind : values()) {
                if (kind.id == id) return kind;
            }
            return null;
        }
    }

    /** One level of the cursor: a node list + where we are inside it (+ the REPEAT iteration counter). */
    private static final class Scope {

        private final ScopeKind kind;
        private final List<USSNode> nodes;
        private int index;
        private int counter;

        private Scope(ScopeKind kind, List<USSNode> nodes, int index, int counter) {
            this.kind = kind;
            this.nodes = nodes;
            this.index = index;
            this.counter = counter;
        }

        private NBTTagCompound writeToNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger(TAG_KIND, kind.getId());
            nbt.setInteger(TAG_INDEX, index);
            nbt.setInteger(TAG_COUNTER, counter);
            NBTTagList body = new NBTTagList();
            for (USSNode node : nodes) {
                body.appendTag(node.writeToNBT());
            }
            nbt.setTag(TAG_BODY, body);
            return nbt;
        }
    }

    /** The active in-flight instruction (MOVE / WORK / WAIT) + its private handler state. */
    private static final class Active {

        private final int cmdId;
        private final USSNode node;
        private final NBTTagCompound state;

        private Active(int cmdId, USSNode node, NBTTagCompound state) {
            this.cmdId = cmdId;
            this.node = node;
            this.state = state;
        }

        private NBTTagCompound writeToNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger(TAG_CMD, cmdId);
            nbt.setTag(TAG_NODE, node.writeToNBT());
            nbt.setTag(TAG_STATE, state);
            return nbt;
        }

        private static Active readFromNBT(NBTTagCompound nbt) {
            Integer cmdId = nbt.hasKey(TAG_CMD) ? nbt.getInteger(TAG_CMD) : null;
            USSNode node = USSNode.readFromNBT(nbt.getCompoundTag(TAG_NODE), 1, new int[] { 0 });
            if (cmdId == null || node == null) {
                return null;
            }
            NBTTagCompound state = nbt.getCompoundTag(TAG_STATE);
            return new Active(cmdId, node, state);
        }
    }

    private State state = State.COMPLETED;
    private final Deque<Scope> scopes = new ArrayDeque<Scope>();
    private Active active;
    private int timer = STEP_TICKS;

    private USSProgramExecutor() {}

    /**
     * Start executing a program (fresh cursor at the first node). An empty / null program completes immediately
     * (nothing to do — the ship then holds).
     *
     * @param program the program (null-safe)
     * @return a RUNNING executor, or a COMPLETED one when there is nothing to run
     */
    public static USSProgramExecutor start(USSProgram program) {
        USSProgramExecutor executor = new USSProgramExecutor();
        if (program != null && !program.isEmpty()) {
            executor.state = State.RUNNING;
            executor.scopes.addLast(new Scope(ScopeKind.ROOT, program.nodes(), 0, 0));
        } else {
            executor.state = State.COMPLETED;
        }
        executor.timer = STEP_TICKS;
        return executor;
    }

    /**
     * One tick. Call every tick while the ship exists (the game pilot does; the cost is a countdown decrement
     * plus — only while an instruction is in flight — one handler poll).
     *
     * @param ctx the execution context (null → no-op)
     */
    public void tick(USSExecutionContext ctx) {
        if (ctx == null || state != State.RUNNING) {
            return;
        }
        if (active != null) {
            USSCommandHandler handler = USSCommandRegistry.handler(active.cmdId);
            if (handler == null) {
                ctx.log("executor: unknown command " + active.cmdId + " in flight — skipping");
                active = null;
                advanceTop();
                timer = STEP_TICKS;
                return;
            }
            USSCommandStatus status = handler.tick(ctx, active.node, active.state);
            if (status == USSCommandStatus.RUNNING) {
                return; // still in flight — keep polling next tick
            }
            if (status == USSCommandStatus.FAILED) {
                ctx.log("executor: command " + active.cmdId + " failed — skipping");
            }
            active = null;
            if (status == USSCommandStatus.STOP) {
                state = State.COMPLETED;
                return;
            }
            advanceTop();
            timer = STEP_TICKS; // restart the rhythm on the next node step
            return;
        }
        timer--;
        if (timer > 0) {
            return;
        }
        timer = STEP_TICKS;
        step(ctx);
    }

    /** One node transition (called on the 20-tick boundary). */
    private void step(USSExecutionContext ctx) {
        if (scopes.isEmpty()) {
            state = State.COMPLETED;
            return;
        }
        Scope top = scopes.peekLast();
        if (top.index >= top.nodes.size()) {
            handleExhausted(top, ctx);
            return;
        }
        processNode(top, top.nodes.get(top.index), ctx);
    }

    /**
     * Resolve an exhausted scope (one scope transition per step): pop it, then resolve the control node that owns
     * its body (IF → advance past; WHILE → re-evaluate; REPEAT → counter).
     */
    private void handleExhausted(Scope top, USSExecutionContext ctx) {
        scopes.removeLast();
        if (scopes.isEmpty()) {
            if (top.nodes.isEmpty()) {
                state = State.COMPLETED; // nothing to repeat — the program ends
            } else {
                scopes.addLast(new Scope(ScopeKind.ROOT, top.nodes, 0, 0)); // invisible while: the program starts over
            }
            return;
        }
        Scope parent = scopes.peekLast();
        USSNode owner = parent.nodes.get(parent.index);
        switch (owner.type()) {
            case IF:
                advanceParent(parent); // body done — continue after the IF
                break;
            case WHILE:
                if (evaluate(owner, ctx)) {
                    top.index = 0;
                    scopes.addLast(top); // another iteration
                }
                // false: the parent stays AT the WHILE — the next step re-evaluates and advances
                break;
            case REPEAT:
                if (top.counter < owner.count()) {
                    top.counter++;
                    top.index = 0;
                    scopes.addLast(top); // next iteration
                } else {
                    advanceParent(parent); // count exhausted — continue after the REPEAT
                }
                break;
            default:
                advanceParent(parent); // defensive — a body always belongs to IF/WHILE/REPEAT
                break;
        }
    }

    /** Process one node: exactly one transition, always. */
    private void processNode(Scope scope, USSNode node, USSExecutionContext ctx) {
        switch (node.type()) {
            case COMMAND:
                command(scope, node, ctx);
                return;
            case IF:
                if (evaluate(node, ctx)) {
                    pushBody(scope, node);
                } else {
                    advanceParent(scope);
                }
                return;
            case WHILE:
                if (evaluate(node, ctx)) {
                    pushBody(scope, node);
                    // empty body → exhausts on the next step → re-evaluation: at least 20 ticks per iteration
                } else {
                    advanceParent(scope);
                }
                return;
            case REPEAT:
                if (node.count() == 0) {
                    advanceParent(scope); // count 0 — the body never runs (Phase A contract)
                } else {
                    pushBodyCounter(scope, node, 1); // counter starts at 1 — the first iteration begins
                }
                return;
            default:
                advanceParent(scope); // defensive
                return;
        }
    }

    private void command(Scope scope, USSNode node, USSExecutionContext ctx) {
        USSCommandHandler handler = USSCommandRegistry.handler(node.cmdId());
        if (handler == null) {
            ctx.log("executor: unknown command " + node.cmdId() + " — skipping");
            advanceParent(scope);
            return;
        }
        NBTTagCompound handlerState = new NBTTagCompound();
        USSCommandStatus status = handler.begin(ctx, node, handlerState);
        if (status == USSCommandStatus.RUNNING) {
            active = new Active(node.cmdId(), node, handlerState);
            return; // in flight — tick() polls from here on
        }
        if (status == USSCommandStatus.FAILED) {
            ctx.log("executor: command " + node.cmdId() + " failed — skipping");
        }
        if (status == USSCommandStatus.STOP) {
            state = State.COMPLETED; // STOP — the only way a non-empty program ends
            return;
        }
        advanceParent(scope); // DONE — next node
    }

    private void pushBody(Scope scope, USSNode owner) {
        scopes.addLast(new Scope(kindOf(owner), owner.body(), 0, 0));
        // the parent stays AT the owner — handleExhausted resolves it when the body finishes
    }

    private void pushBodyCounter(Scope scope, USSNode owner, int counter) {
        scopes.addLast(new Scope(ScopeKind.REPEAT, owner.body(), 0, counter));
    }

    private static ScopeKind kindOf(USSNode owner) {
        switch (owner.type()) {
            case IF:
                return ScopeKind.IF;
            case WHILE:
                return ScopeKind.WHILE;
            case REPEAT:
                return ScopeKind.REPEAT;
            default:
                return ScopeKind.ROOT;
        }
    }

    /** Advance the top scope past its current node (with the root-exhaustion → COMPLETED check). */
    private void advanceParent(Scope scope) {
        scope.index++;
        checkRootDone(scope);
    }

    private void advanceTop() {
        Scope top = scopes.peekLast();
        top.index++;
        checkRootDone(top);
    }

    /**
     * The invisible-while check: when the ROOT scope exhausts the program restarts at its first node (a lone
     * command loops forever) — only a STOP / an empty program ends it.
     */
    private void checkRootDone(Scope scope) {
        if (scopes.size() == 1 && scope == scopes.peekLast() && scope.index >= scope.nodes.size()) {
            if (scope.nodes.isEmpty()) {
                scopes.clear();
                state = State.COMPLETED; // nothing to repeat — the program ends
            } else {
                scope.index = 0; // restart the program
            }
        }
    }

    /**
     * @return the condition's verdict, with both sides resolved through the context (null-safe: a missing side
     *         reads as {@code ""})
     */
    private boolean evaluate(USSNode node, USSExecutionContext ctx) {
        USSCondition condition = node.condition();
        if (condition == null) {
            return false; // corrupt node — treat as not-taken (skip the body), never crash
        }
        String left = resolveValue(condition.left(), ctx);
        String right = resolveValue(condition.right(), ctx);
        return condition.evaluate(left, right);
    }

    private String resolveValue(USSValue value, USSExecutionContext ctx) {
        String resolved = ctx.resolve(value);
        return resolved == null ? "" : resolved;
    }

    public State state() {
        return state;
    }

    public boolean isCompleted() {
        return state == State.COMPLETED;
    }

    /** @return true while an instruction is in flight (MOVE / WORK / WAIT) */
    public boolean isActive() {
        return active != null;
    }

    /**
     * Serialize the cursor (scopes + active instruction + step timer). The program itself is stored separately
     * (the game pilot keeps both); the cursor is self-contained (each scope carries its body), so
     * {@link #readFromNBT} never needs the program.
     */
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(TAG_STATUS, state.getId());
        nbt.setInteger(TAG_TIMER, timer);
        NBTTagList scopeList = new NBTTagList();
        for (Scope scope : scopes) {
            scopeList.appendTag(scope.writeToNBT());
        }
        nbt.setTag(TAG_SCOPES, scopeList);
        if (active != null) {
            nbt.setTag(TAG_ACTIVE, active.writeToNBT());
        }
        return nbt;
    }

    /**
     * Restore a cursor (save/reload mid-program). Corrupt input is fail-safe: any unreadable scope or active
     * record drops the whole cursor into COMPLETED (the ship holds — never a half-run, never a double-run from
     * the start).
     *
     * @param nbt the cursor NBT (null → a COMPLETED executor)
     * @return the restored executor
     */
    public static USSProgramExecutor readFromNBT(NBTTagCompound nbt) {
        USSProgramExecutor executor = new USSProgramExecutor();
        if (nbt == null) {
            executor.state = State.COMPLETED;
            return executor;
        }
        State state = State.byId(nbt.hasKey(TAG_STATUS) ? nbt.getInteger(TAG_STATUS) : 1);
        if (state == null) {
            state = State.COMPLETED;
        }
        executor.timer = Math
            .max(1, Math.min(STEP_TICKS, nbt.hasKey(TAG_TIMER) ? nbt.getInteger(TAG_TIMER) : STEP_TICKS));

        NBTTagList scopeList = null;
        Object raw = nbt.getTag(TAG_SCOPES);
        if (raw instanceof NBTTagList) {
            scopeList = (NBTTagList) raw;
        }
        if (scopeList == null && state == State.RUNNING) {
            // RUNNING with no scope list — nothing to continue
            state = State.COMPLETED;
        }
        if (state == State.RUNNING && scopeList != null) {
            List<Scope> scopes = new ArrayList<Scope>();
            for (int i = 0; i < scopeList.tagCount(); i++) {
                Object tag = scopeList.tagList.get(i);
                if (!(tag instanceof NBTTagCompound)) {
                    state = State.COMPLETED; // corrupt cursor — fail-safe
                    scopes = null;
                    break;
                }
                NBTTagCompound scopeNbt = (NBTTagCompound) tag;
                ScopeKind kind = ScopeKind.byId(scopeNbt.hasKey(TAG_KIND) ? scopeNbt.getInteger(TAG_KIND) : 0);
                if (kind == null) {
                    state = State.COMPLETED;
                    scopes = null;
                    break;
                }
                List<USSNode> body = new ArrayList<USSNode>();
                Object bodyRaw = scopeNbt.getTag(TAG_BODY);
                if (bodyRaw instanceof NBTTagList) {
                    NBTTagList bodyList = (NBTTagList) bodyRaw;
                    int[] budget = new int[] { 0 };
                    for (int j = 0; j < bodyList.tagCount(); j++) {
                        Object bodyTag = bodyList.tagList.get(j);
                        if (!(bodyTag instanceof NBTTagCompound)) {
                            state = State.COMPLETED; // corrupt cursor — fail-safe
                            scopes = null;
                            break;
                        }
                        if (budget[0] >= USSProgram.MAX_NODES) {
                            state = State.COMPLETED; // corrupt cursor — fail-safe
                            scopes = null;
                            break;
                        }
                        USSNode node = USSNode.readFromNBT((NBTTagCompound) bodyTag, 1, budget);
                        if (node == null) {
                            state = State.COMPLETED; // corrupt cursor — fail-safe
                            scopes = null;
                            break;
                        }
                        body.add(node);
                    }
                    if (scopes == null) {
                        break;
                    }
                }
                int index = scopeNbt.hasKey(TAG_INDEX) ? scopeNbt.getInteger(TAG_INDEX) : 0;
                if (index < 0) {
                    index = 0;
                }
                if (index > body.size()) {
                    index = body.size();
                }
                int counter = Math.max(0, scopeNbt.hasKey(TAG_COUNTER) ? scopeNbt.getInteger(TAG_COUNTER) : 0);
                scopes.add(new Scope(kind, body, index, counter));
            }
            if (scopes != null) {
                executor.scopes.addAll(scopes);
            }
        }
        if (state == State.RUNNING && executor.scopes.isEmpty()) {
            state = State.COMPLETED; // nothing to continue
        }
        if (state == State.RUNNING && nbt.hasKey(TAG_ACTIVE)) {
            Object activeRaw = nbt.getTag(TAG_ACTIVE);
            if (activeRaw instanceof NBTTagCompound) {
                executor.active = Active.readFromNBT((NBTTagCompound) activeRaw);
                if (executor.active == null) {
                    state = State.COMPLETED; // corrupt cursor — fail-safe
                }
            } else {
                state = State.COMPLETED;
            }
        }
        executor.state = state;
        return executor;
    }

    // NBT keys
    private static final String TAG_STATUS = "st";
    private static final String TAG_TIMER = "timer";
    private static final String TAG_SCOPES = "sc";
    private static final String TAG_ACTIVE = "act";
    private static final String TAG_KIND = "k";
    private static final String TAG_INDEX = "i";
    private static final String TAG_COUNTER = "c";
    private static final String TAG_BODY = "b";
    private static final String TAG_CMD = "c";
    private static final String TAG_NODE = "n";
    private static final String TAG_STATE = "s";
}
