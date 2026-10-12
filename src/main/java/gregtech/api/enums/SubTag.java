package gregtech.api.enums;

/// A tag passed in a GregTech meta item's registration data, or the projectile type an item supplies.
public final class SubTag {

    /// Hides the meta item from creative tabs and NEI.
    public static final SubTag INVISIBLE = new SubTag("INVISIBLE");
    /// Marks the meta item as never being a unified copy; see [gregtech.api.util.OreDictUtils#addToBlacklist].
    public static final SubTag NO_UNIFICATION = new SubTag("NO_UNIFICATION");
    /// The projectile type of arrows.
    public static final SubTag PROJECTILE_ARROW = new SubTag("PROJECTILE_ARROW");

    public final String mName;

    private SubTag(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }
}
