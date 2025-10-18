package gregtech.api.enums;

public class OrePrefixBuilder {

    private final String name;
    private String defaultLocalName;
    private String localMaterialPre;
    private String localMaterialPost;
    private boolean isUnifiable = false;
    private boolean isMaterialBased = false;
    private boolean isSelfReferencing = false;
    private boolean isContainer = false;
    private boolean doNotUnifyActively = false;
    private boolean isUsedForBlocks = false;
    private boolean allowNormalRecycling = false;
    private boolean generateDefaultItem = false;
    private boolean isEnchantable = false;
    private boolean isUsedForOreProcessing = false;
    private int materialGenerationBits = 0;

    protected OrePrefixBuilder(String name) {
        this.name = name;
    }

    protected OrePrefixes build() {
        return new OrePrefixes(
            // spotless:off
            name,
            defaultLocalName,
            localMaterialPre,
            localMaterialPost,
            isUnifiable,
            isMaterialBased,
            isSelfReferencing,
            isContainer,
            doNotUnifyActively,
            isUsedForBlocks,
            allowNormalRecycling,
            generateDefaultItem,
            isEnchantable,
            isUsedForOreProcessing,
            materialGenerationBits
            // spotless:on
        );
    }

    protected OrePrefixBuilder setDefaultLocalName(String defaultLocalName) {
        this.defaultLocalName = defaultLocalName;
        return this;
    }

    protected OrePrefixBuilder setLocalMaterialPre(String localMaterialPre){
        this.localMaterialPre = localMaterialPre;
        return this;
    }

    protected OrePrefixBuilder setLocalMaterialPost(String localMaterialPost){
        this.localMaterialPost = localMaterialPost;
        return this;
    }

    protected OrePrefixBuilder setUnifiable() {
        this.isUnifiable = true;
        return this;
    }

    protected OrePrefixBuilder setMaterialBased() {
        this.isMaterialBased = true;
        return this;
    }

    protected OrePrefixBuilder setSelfReferencing() {
        this.isSelfReferencing = true;
        return this;
    }

    protected OrePrefixBuilder setContainer() {
        this.isContainer = true;
        return this;
    }

    protected OrePrefixBuilder setDoNotUnifyActively() {
        this.doNotUnifyActively = true;
        return this;
    }

    protected OrePrefixBuilder setUsedForBlocks() {
        this.isUsedForBlocks = true;
        return this;
    }

    protected OrePrefixBuilder setAllowNormalRecycling() {
        this.allowNormalRecycling = true;
        return this;
    }

    protected OrePrefixBuilder setGenerateDefaultItem() {
        this.generateDefaultItem = true;
        return this;
    }

    protected OrePrefixBuilder setIsEnchantable() {
        this.isEnchantable = true;
        return this;
    }

    protected OrePrefixBuilder setIsUsedForOreProcessing() {
        this.isUsedForOreProcessing = true;
        return this;
    }

    protected OrePrefixBuilder setMaterialGenerationBits(int materialGenerationBits) {
        this.materialGenerationBits = materialGenerationBits;
        return this;
    }
}
