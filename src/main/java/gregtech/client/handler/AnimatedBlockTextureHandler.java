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
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Materials2PipeShapes;

/// Angelica pauses an animated block icon's frame advance while it is off-screen. [#ANIMATED]'s storage-block
/// and frame-box art must keep animating regardless of visibility, so this forces their icons to tick every
/// client frame through [TextureServices#updateTextureAnimation].
@SideOnly(Side.CLIENT)
public class AnimatedBlockTextureHandler implements IResourceManagerReloadListener {

    /// The materials whose storage block and frame box carry a baked animated icon. `Dragonblood` needs no
    /// entry of its own despite sharing the same animation: it declares the same texture set as `Hypogen`
    /// (`CUSTOM/hypogen`), so both materials' `block`/`frameGt` icons resolve to the same stitched sprite --
    /// forcing `Hypogen`'s instance already advances `Dragonblood`'s.
    private static final Material[] ANIMATED = { Materials.AstralTitanium,
        Materials.CelestialTungsten, Materials.ChromaticGlass, Materials.Hypogen };

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
        // A reload re-stitches the atlas and reassigns each block's sprite, so the cached icons go stale; drop them
        // and let the next tick re-resolve against the fresh sprites.
        icons = null;
    }

    private static IIcon[] resolveIcons() {
        List<IIcon> resolved = new ArrayList<>();
        Block block = MaterialLibAPI.getBlock(Materials2BlockShapes.block);
        Block frame = MaterialLibAPI.getBlock(Materials2PipeShapes.frameGt);
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
