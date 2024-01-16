package gregtech.api.modernmaterials;
import java.util.List;

public class PartTextureConfig {
    private List<Layer> layers;

    public List<Layer> getLayers() {
        return layers;
    }

    public static class Layer {
        private String file;
        private boolean isColored;
        private boolean isAnimated;

        public String getFile() {
            return file;
        }

        public boolean isColored() {
            return isColored;
        }

        public boolean isAnimated() {
            return isAnimated;
        }
    }
}
