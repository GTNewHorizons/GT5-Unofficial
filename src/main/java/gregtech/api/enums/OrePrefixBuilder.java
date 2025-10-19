package gregtech.api.enums;

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

    public OrePrefixBuilder unifiable() {
        this.isUnifiable = true;
        return this;
    }

    public OrePrefixBuilder materialBased() {
        this.isMaterialBased = true;
        return this;
    }

    public OrePrefixBuilder selfReferencing() {
        this.isSelfReferencing = true;
        return this;
    }

    public OrePrefixBuilder container() {
        this.isContainer = true;
        return this;
    }

    public OrePrefixBuilder skipActiveUnification() {
        this.skipActiveUnification = true;
        return this;
    }

    public OrePrefixBuilder recyclable() {
        this.isRecyclable = true;
        return this;
    }

    public OrePrefixBuilder enchantable() {
        this.isEnchantable = true;
        return this;
    }

    public OrePrefixBuilder materialGenerationBits(int materialGenerationBits) {
        this.materialGenerationBits = materialGenerationBits;
        return this;
    }

    /**
     * Used to determine the amount of Material this Prefix contains. Multiply or Divide GT_Values.M to get the Amounts
     * in comparision to one Ingot. 0 = Null Negative = Undefined Amount
     */
    public OrePrefixBuilder materialAmount(long materialAmount) {
        this.materialAmount = materialAmount;
        return this;
    }

    public OrePrefixBuilder defaultStackSize(int defaultStackSize) {
        this.defaultStackSize = defaultStackSize;
        return this;
    }

    public OrePrefixBuilder textureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
        return this;
    }
}
