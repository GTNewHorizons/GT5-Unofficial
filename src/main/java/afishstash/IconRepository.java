package afishstash;

import com.slprime.chromatictooltips.TooltipHandler;
import com.slprime.chromatictooltips.api.ITooltipComponent;
import com.slprime.chromatictooltips.component.InlineComponent;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IconRepository {
    private static final Map<String, String> cache = new ConcurrentHashMap<>();

    /**
     * Gets the tooltip icon by the string key
     * @param key the key
     * @return the component id
     * @apiNote By default, the path {@code resources/assets/gregtech/textures/tooltip/%s.png}
     * is searched, where {@code %s} is the key.
     */
    public static String getComponentKey(String key) {
        if (cache.containsKey(key)) return cache.get(key);

        IconComponent component = createComponent(
            String.format("gregtech:textures/tooltip/%s.png", key)
        );

        String id = getComponentId(component);

        cache.put(key, id);

        return id;
    }

    private static String getComponentId(IconComponent component) {
        List<ITooltipComponent> components = List.of(component);
        return TooltipHandler.getPermanentId(new InlineComponent(List.of(components), 2, 2));
    }

    private static IconComponent createComponent(String resourceLocation) {
        return new IconComponent(resourceLocation, 8);
    }
}
