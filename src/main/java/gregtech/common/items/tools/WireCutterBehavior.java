package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IGTTool;

/**
 * What a wire cutter does, independent of whether it wears out or runs on EU: renaming AE2 machines. See
 * {@link WrenchBehavior} for why this is composed into {@link ToolWireCutterItem} and
 * {@link ToolWireCutterElectricItem} rather than inherited.
 * <p/>
 * Unlike wrench, screwdriver and file, a wire cutter answers to no cross-mod tool interface, so this is the whole of
 * what the two classes would otherwise have had to duplicate.
 */
public interface WireCutterBehavior extends IGTTool {

    /** See {@link WrenchBehavior#wrenchOnItemUseFirst} for why this cannot just be {@code onItemUseFirst} itself. */
    default boolean wireCutterOnItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return WireCutterActions
            .use(player, world, x, y, z, ForgeDirection.getOrientation(ordinalSide), hitX, hitY, hitZ);
    }
}
