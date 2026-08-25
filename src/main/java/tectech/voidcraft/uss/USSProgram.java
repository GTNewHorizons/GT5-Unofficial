package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * A Voidcraft program: an ordered root list of {@link USSNode}s (programming framework, Phase A).
 *
 * <p>
 * The program is what the CONTROLLER holds (user spec: "the Voidcraft controller itself holds a list of
 * instructions that is user-editable"). It is digitized into the ship item NBT ({@code vc_program}) and executed
 * by the per-ship executor (Phase B).
 *
 * <p>
 * Caps (enforced on the NBT READ path — a program that exceeds them is dropped wholesale and yields the empty
 * program, i.e. corrupt/oversized NBT is treated exactly like corrupt NBT: no partial program, no migration):
 * at most {@link #MAX_NODES} nodes in total and nesting of at most {@link #MAX_DEPTH}. The in-code path
 * ({@link #of(List)}) is trusted and does not enforce the caps.
 *
 * <p>
 * Immutable, bare-JVM (NBT + JDK only) — unit-testable (see {@code USSProgramTest}).
 */
public final class USSProgram {

    /** Maximum TOTAL number of nodes (all nesting levels). */
    public static final int MAX_NODES = 255;
    /** Maximum nesting depth (a root node is depth 1; its leaf descendants are one more each). */
    public static final int MAX_DEPTH = 8;
    /** Maximum string-literal length (a program carries short strings, not blobs). */
    public static final int MAX_LITERAL_LENGTH = 255;

    private final List<USSNode> nodes;

    private USSProgram(List<USSNode> nodes) {
        this.nodes = nodes;
    }

    public static USSProgram empty() {
        return new USSProgram(Collections.<USSNode>emptyList());
    }

    /**
     * @param nodes the root nodes (null → empty program; null entries are dropped)
     */
    public static USSProgram of(List<USSNode> nodes) {
        if (nodes == null) {
            return empty();
        }
        List<USSNode> safe = new ArrayList<USSNode>(nodes.size());
        for (USSNode node : nodes) {
            if (node != null) {
                safe.add(node);
            }
        }
        return new USSProgram(Collections.unmodifiableList(safe));
    }

    public List<USSNode> nodes() {
        return nodes;
    }

    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    /** The maximum depth of any root node (0 for an empty program). */
    public int depth() {
        int d = 0;
        for (USSNode node : nodes) {
            d = Math.max(d, node.depth());
        }
        return d;
    }

    /** The total number of nodes across all nesting levels. */
    public int nodeCount() {
        int n = 0;
        for (USSNode node : nodes) {
            n += node.subtreeSize();
        }
        return n;
    }

    // region NBT

    /**
     * @return the program as an NBT list of node compounds (stored under the ship's {@code vc_program} tag)
     */
    public NBTTagList writeToNBT() {
        NBTTagList list = new NBTTagList();
        for (USSNode node : nodes) {
            list.appendTag(node.writeToNBT());
        }
        return list;
    }

    /**
     * @param list the tag list as written by {@link #writeToNBT()} (may be null)
     * @return the program — NEVER null; a missing/corrupt list, a non-compound entry, an unknown node type, or a
     *         program over the caps ({@link #MAX_NODES}/{@link #MAX_DEPTH}) yields the EMPTY program
     */
    public static USSProgram readFromNBT(NBTTagList list) {
        if (list == null) {
            return empty();
        }
        int[] used = { 0 };
        List<USSNode> nodes = new ArrayList<USSNode>();
        for (int i = 0; i < list.tagCount(); i++) {
            Object tag = list.tagList.get(i); // raw element (1.7.10 NBTTagList has no untyped get(int))
            if (!(tag instanceof NBTTagCompound)) {
                return empty();
            }
            USSNode node = USSNode.readFromNBT((NBTTagCompound) tag, 1, used);
            if (node == null) {
                return empty();
            }
            nodes.add(node);
        }
        return new USSProgram(Collections.unmodifiableList(nodes));
    }

    // endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof USSProgram)) return false;
        return nodes.equals(((USSProgram) o).nodes);
    }

    @Override
    public int hashCode() {
        return nodes.hashCode();
    }

    @Override
    public String toString() {
        return "USSProgram" + nodes;
    }
}
