package gtPlusPlus.core.client;

import gregtech.api.enums.TextureSet;

public class CustomTextureSet extends TextureSet {

    public enum TextureSets {

        REFINED(),
        GEM_A(),
        ENRICHED(),
        NUCLEAR;

        private final CustomTextureSet A;

        TextureSets() {
            A = new CustomTextureSet(
                this.name()
                    .toUpperCase());
        }

        public CustomTextureSet get() {
            return A;
        }
    }

    public CustomTextureSet(String aSetName) {
        super(aSetName);
    }
}
