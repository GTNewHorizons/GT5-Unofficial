package gregtech.loaders.misc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

import static gregtech.GT_Mod.GT_FML_LOGGER;

@SideOnly(Side.CLIENT)
public class GT_JsonLoader implements IResourceManagerReloadListener {
    private static final Gson gson = new Gson();
    private JsonObject json;
    private final ResourceLocation jsonLocation;
    private final List<Consumer<GT_JsonLoader>> reloadListeners;

    public String getString(String key) {
        String s = "";
        try {
            s = this.json.get(key).getAsString();
        } catch (Exception e) {
            // stupid log4j
            GT_FML_LOGGER.error("GT_JsonLoader " + jsonLocation + " read error", e);
        }
        return s;
    }

    private void loadJson(IResourceManager rm) {
        this.json = new JsonObject();
        try (Reader in = new BufferedReader(new InputStreamReader(rm.getResource(this.jsonLocation).getInputStream(), StandardCharsets.UTF_8))) {
            json = gson.fromJson(in, JsonObject.class);
        } catch (Exception e) {
            // stupid log4j
            GT_FML_LOGGER.error("GT_JsonLoader " + jsonLocation + " load error", e);
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager rm) {
        loadJson(rm);
        for (Consumer<GT_JsonLoader> listener : reloadListeners) {
            listener.accept(this);
        }
    }

    public void registerReloadListener(Consumer<GT_JsonLoader> listener) {
        reloadListeners.add(listener);
    }

    public GT_JsonLoader(String resourcePath) {
        GT_FML_LOGGER.info("GT_JsonLoader: Init");
        this.jsonLocation = new ResourceLocation("gregtech", resourcePath);
        reloadListeners = new ArrayList<>();
        IReloadableResourceManager rm = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
        rm.registerReloadListener(this);
        loadJson(rm);
    }
}
