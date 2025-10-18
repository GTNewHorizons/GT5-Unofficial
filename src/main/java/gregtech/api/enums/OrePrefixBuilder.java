package gregtech.api.enums;

public class OrePrefixBuilder {

    private final String name;
    private String defaultLocalName;
    private String localMaterialPre;
    private String localMaterialPost;
    private boolean isUnifiable = false;
    private boolean isMaterialBased = false;
    private boolean isSelfReferencing = false;

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
            isSelfReferencing
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
}
