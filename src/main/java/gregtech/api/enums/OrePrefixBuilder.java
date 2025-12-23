package gregtech.api.enums;

import org.intellij.lang.annotations.MagicConstant;

public class OrePrefixBuilder {

    private final String name;
    private String defaultLocalName = "";
    private String materialPrefix = "";
    private String materialPostfix = "";
    private boolean isUnifiable = false;
    private boolean isMaterialBased = false;
    private boolean isSelfReferencing = false;
    private boolean isContainer = false;
    private boolean skipActiveUnification = false;
    private boolean isRecyclable = false;
    private boolean isEnchantable = false;
    private int materialGenerationBits = 0;
    private long materialAmount = -1;
    private int defaultStackSize = 64;
    private int textureIndex = -1;

    public OrePrefixBuilder(String name) {
        this.name = name;
    }

    public OrePrefixes build() {
        return new OrePrefixes(
            // spotless:off
            name,
            defaultLocalName,
            materialPrefix,
            materialPostfix,
            isUnifiable,
            isMaterialBased,
            isSelfReferencing,
            isContainer,
            skipActiveUnification,
            isRecyclable,
            isEnchantable,
            materialGenerationBits,
            materialAmount,
            defaultStackSize,
            textureIndex
            // spotless:on
        );
    }

    public OrePrefixBuilder withDefaultLocalName(String defaultLocalName) {
        this.defaultLocalName = defaultLocalName;
        return this;
    }

    public OrePrefixBuilder withPrefix(String materialPrefix) {
        this.materialPrefix = materialPrefix;
        return this;
    }

    public OrePrefixBuilder withSuffix(String materialPostfix) {
        this.materialPostfix = materialPostfix;
        return this;
    }

    /** Marks this prefix as unifiable during the unification process. */
    public OrePrefixBuilder unifiable() {
        this.isUnifiable = true;
        return this;
    }

    /** Indicates that this prefix represents items derived from a material (e.g., ingots, dusts). */
    public OrePrefixBuilder materialBased() {
        this.isMaterialBased = true;
        return this;
    }

    public OrePrefixBuilder selfReferencing() {
        this.isSelfReferencing = true;
        return this;
    }

    /** Marks this prefix as a container (e.g., cells, buckets). */
    public OrePrefixBuilder container() {
        this.isContainer = true;
        return this;
    }

    public OrePrefixBuilder skipActiveUnification() {
        this.skipActiveUnification = true;
        return this;
    }

    /** Marks this prefix as recyclable (e.g. maceration, fluid-extraction). */
    public OrePrefixBuilder recyclable() {
        this.isRecyclable = true;
        return this;
    }

    /** Marks this prefix as enchantable. */
    public OrePrefixBuilder enchantable() {
        this.isEnchantable = true;
        return this;
    }

    public OrePrefixBuilder materialGenerationBits(
        @MagicConstant(flagsFromClass = OrePrefixes.class) int materialGenerationBits) {
        this.materialGenerationBits = materialGenerationBits;
        return this;
    }

    /** Sets the amount of material per item. One ingot is worth {@link GTValues#M}. */
    public OrePrefixBuilder materialAmount(long materialAmount) {
        this.materialAmount = materialAmount;
        return this;
    }

    /** Sets the default stack size for items with this prefix. */
    public OrePrefixBuilder defaultStackSize(int defaultStackSize) {
        this.defaultStackSize = defaultStackSize;
        return this;
    }

    /** Sets the texture index used for this prefix. See {@link OrePrefixTextureID}. */
    public OrePrefixBuilder textureIndex(@MagicConstant(flagsFromClass = OrePrefixTextureID.class) int textureIndex) {
        this.textureIndex = textureIndex;
        return this;
    }
}
