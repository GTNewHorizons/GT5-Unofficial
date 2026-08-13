package gregtech.common.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.GTMod;
import gregtech.common.render.shader.ShaderProfile;
import gregtech.common.render.shader.SharedShaders;
import gregtech.common.tileentities.machines.multi.foundry.MTEExoFoundry;

public final class RenderInit implements IResourceManagerReloadListener {

    private static final RenderInit INSTANCE = new RenderInit();

    private static final List<Runnable> RESOURCE_HOOKS = new ArrayList<>();
    private static final List<Runnable> ATLAS_HOOKS = new ArrayList<>();

    private static boolean pending;

    private RenderInit() {}

    public static void onResourceReload(Runnable hook) {
        RESOURCE_HOOKS.add(hook);
    }

    public static void onAtlasStitched(Runnable hook) {
        ATLAS_HOOKS.add(hook);
    }

    public static void registerEarly() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public static void register() {
        ShaderProfile.init();

        onResourceReload(NanoForgeRenderer::reload);
        onResourceReload(DroneRender::reload);
        onResourceReload(WormholeRenderer::reload);
        onResourceReload(BlackholeRenderer::reload);
        onResourceReload(MTEExoFoundry::reloadRender);

        ((IReloadableResourceManager) Minecraft.getMinecraft()
            .getResourceManager()).registerReloadListener(INSTANCE);
    }

    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        pending = true;
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Post event) {
        if (event.map.getTextureType() != 0) return;
        pending = true;
    }

    public static void runPendingReload() {
        if (!pending) return;
        pending = false;

        SharedShaders.reload();
        run(ATLAS_HOOKS);
        run(RESOURCE_HOOKS);
    }

    private static void run(List<Runnable> hooks) {
        for (Runnable hook : hooks) {
            try {
                hook.run();
            } catch (Throwable t) {
                GTMod.GT_FML_LOGGER.error("Renderer asset load failed", t);
            }
        }
    }
}
