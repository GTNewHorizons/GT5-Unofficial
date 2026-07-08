package gtPlusPlus.core.handler.events;

import static gregtech.api.enums.Materials.AstralTitanium;
import static gregtech.api.enums.Materials.CelestialTungsten;
import static gregtech.api.enums.Materials.ChromaticGlass;
import static gregtech.api.enums.Materials.Hypogen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.IIcon;

import com.gtnewhorizons.angelica.api.TextureServices;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.material.Material;

/// Angelica prevents animated blocks from cycling unless they are actively on-screen. This is desirable most of the
/// time, but not when different blocks need to be synchronized or when blocks should synchronize to items.
/// This can be used to force those blocks to update constantly, so they stay in-sync.
/// To add more, add them in {@link #resolveIcons()}.
@SideOnly(Side.CLIENT)
public class AnimatedBlockTextureHandler implements IResourceManagerReloadListener {

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
        Material[] animated = { ChromaticGlass, AstralTitanium, CelestialTungsten, Hypogen };
        for (Material material : animated) {
            for (BlockTypes type : new BlockTypes[] { BlockTypes.STANDARD, BlockTypes.FRAME }) {
                Block block = BlockBaseModular.getMaterialBlock(material, type);
                if (block != null) {
                    IIcon icon = block.getIcon(0, 0);
                    if (icon != null) {
                        resolved.add(icon);
                    }
                }
            }
        }
        return resolved.toArray(new IIcon[0]);
    }
}
