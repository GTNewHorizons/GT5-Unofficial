package gregtech.client.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.IIcon;

import com.gtnewhorizons.angelica.api.TextureServices;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.TEBlockShapes;

/// Advances the animated storage-block and frame-box icons of [#ANIMATED] every client tick. Angelica
/// pauses animations of off-screen block icons, and these must keep animating.
@SideOnly(Side.CLIENT)
public class AnimatedBlockTextureHandler implements IResourceManagerReloadListener {

    /// The materials whose storage block and frame box carry an animated icon. `Dragonblood` shares `Hypogen`'s
    /// texture set, so the `Hypogen` entry covers it.
    private static final Material[] ANIMATED = { Materials.AstralTitanium, Materials.CelestialTungsten,
        Materials.ChromaticGlass, Materials.Hypogen };

    private IIcon[] icons;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (icons == null) {
            IIcon[] resolved = resolveIcons();
            if (resolved.length == 0) return; // block icons not stitched yet
            icons = resolved;
        }
        for (IIcon icon : icons) {
            TextureServices.updateTextureAnimation(icon);
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        icons = null;
    }

    private static IIcon[] resolveIcons() {
        List<IIcon> resolved = new ArrayList<>();
        Block block = MaterialLibAPI.getBlock(BlockShapes.block);
        Block frame = MaterialLibAPI.getBlock(TEBlockShapes.frameGt);
        for (Material material : ANIMATED) {
            addIcon(resolved, block, material);
            addIcon(resolved, frame, material);
        }
        return resolved.toArray(new IIcon[0]);
    }

    private static void addIcon(List<IIcon> resolved, Block block, Material material) {
        if (block == null) return;
        IIcon icon = block.getIcon(0, material.getIndex());
        if (icon != null) {
            resolved.add(icon);
        }
    }
}
