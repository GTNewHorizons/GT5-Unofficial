package gregtech.api.enums;

public class OrePrefixBuilder {

    private final String name;
    private String defaultLocalName;
    private String materialPrefix;
    private String materialPostfix;
    private boolean isUnifiable = false;
    private boolean isMaterialBased = false;
    private boolean isSelfReferencing = false;
    private boolean isContainer = false;
    private boolean skipActiveUnification = false;
    private boolean isUsedForBlocks = false;
    private boolean isRecyclable = false;
    private boolean generateDefaultItem = false;
    private boolean isEnchantable = false;
    private boolean isUsedForOreProcessing = false;
    private int materialGenerationBits = 0;
    private long materialAmount = -1;
    private int defaultStackSize = 64;
    private int textureIndex = -1;

    protected OrePrefixBuilder(String name) {
        this.name = name;
    }

    protected OrePrefixes build() {
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
            isUsedForBlocks,
            isRecyclable,
            generateDefaultItem,
            isEnchantable,
            isUsedForOreProcessing,
            materialGenerationBits,
            materialAmount,
            defaultStackSize,
            textureIndex
            // spotless:on
        );
    }

    protected OrePrefixBuilder withDefaultLocalName(String defaultLocalName) {
        this.defaultLocalName = defaultLocalName;
        return this;
    }

    protected OrePrefixBuilder withPrefix(String materialPrefix) {
        this.materialPrefix = materialPrefix;
        return this;
    }

    protected OrePrefixBuilder withSuffix(String materialPostfix) {
        this.materialPostfix = materialPostfix;
        return this;
    }

    protected OrePrefixBuilder unifiable() {
        this.isUnifiable = true;
        return this;
    }

    protected OrePrefixBuilder materialBased() {
        this.isMaterialBased = true;
        return this;
    }

    protected OrePrefixBuilder selfReferencing() {
        this.isSelfReferencing = true;
        return this;
    }

    protected OrePrefixBuilder container() {
        this.isContainer = true;
        return this;
    }

    protected OrePrefixBuilder skipActiveUnification() {
        this.skipActiveUnification = true;
        return this;
    }

    protected OrePrefixBuilder usedForBlocks() {
        this.isUsedForBlocks = true;
        return this;
    }

    protected OrePrefixBuilder recyclable() {
        this.isRecyclable = true;
        return this;
    }

    protected OrePrefixBuilder generateDefaultItem() {
        this.generateDefaultItem = true;
        return this;
    }

    protected OrePrefixBuilder enchantable() {
        this.isEnchantable = true;
        return this;
    }

    protected OrePrefixBuilder usedForOreProcessing() {
        this.isUsedForOreProcessing = true;
        return this;
    }

    protected OrePrefixBuilder materialGenerationBits(int materialGenerationBits) {
        this.materialGenerationBits = materialGenerationBits;
        return this;
    }

    protected OrePrefixBuilder materialAmount(long materialAmount) {
        this.materialAmount = materialAmount;
        return this;
    }

    @SuppressWarnings("SameParameterValue") // The only passed argument is defaultStackSize = 1
    protected OrePrefixBuilder defaultStackSize(int defaultStackSize) {
        this.defaultStackSize = defaultStackSize;
        return this;
    }

    protected OrePrefixBuilder textureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
        return this;
    }
}
