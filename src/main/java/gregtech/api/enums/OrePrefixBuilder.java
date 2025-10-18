package gregtech.api.enums;

public class OrePrefixBuilder {

    private final String name;
    private String defaultLocalName;
    private String localMaterialPre;

    protected OrePrefixBuilder(String name) {
        this.name = name;
    }

    protected OrePrefixes build() {
        return new OrePrefixes(
            // spotless:off
            name,
            defaultLocalName,
            localMaterialPre
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
}
