package afishstash;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.slprime.chromatictooltips.TooltipHandler;
import com.slprime.chromatictooltips.api.ITooltipComponent;
import com.slprime.chromatictooltips.api.TooltipContext;
import com.slprime.chromatictooltips.component.InlineComponent;

public class IconRepository {

    private static final Map<String, String> cache = new ConcurrentHashMap<>();

    /**
     * Gets the tooltip icon by the string key
     *
     * @param key the key
     * @return the component id
     * @apiNote By default, the path {@code resources/assets/gregtech/textures/tooltip/%s.png}
     *          is searched, where {@code %s} is the key.
     */
    public static String getComponentKey(String key) {
        if (cache.containsKey(key)) return cache.get(key);

        IconComponent component = createComponent(String.format("gregtech:textures/tooltip/%s.png", key));

        String id = getComponentId(component);

        cache.put(key, id);

        return id;
    }

    private static String getComponentId(IconComponent component) {
        List<ITooltipComponent> line = List.of(component, GAP);
        return TooltipHandler.getPermanentId(new InlineComponent(List.of(line), 2, 2));
    }

    private static IconComponent createComponent(String resourceLocation) {
        return new IconComponent(resourceLocation, 8);
    }

    // Adds small padding on icon's right side for better aesthetics
    private static final ITooltipComponent GAP = new ITooltipComponent() {

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public int getSpacing() {
            return 0;
        }

        @Override
        public ITooltipComponent[] paginate(TooltipContext context, int maxWidth, int maxHeight) {
            return new ITooltipComponent[] { this };
        }

        @Override
        public void draw(int x, int y, int availableWidth, TooltipContext context) {}
    };
}
