package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.IGTTool;
import gregtech.api.util.GTUtility;
import mrtjp.projectred.api.IScrewdriver;

/**
 * What a screwdriver does, independent of whether it wears out or runs on EU: adjusting redstone components and
 * ProjectRed's screwdriver interface. See {@link WrenchBehavior} for why this extends {@code IScrewdriver} itself
 * (a default method only satisfies an abstract method of an interface its own declaring interface extends) and why
 * the {@code @Optional} annotation belongs here rather than on {@link ToolScrewdriverItem} and
 * {@link ToolScrewdriverElectricItem}.
 */
@Optional.Interface(iface = "mrtjp.projectred.api.IScrewdriver", modid = Mods.ModIDs.PROJECT_RED_CORE)
public interface ScrewdriverBehavior extends IGTTool, IScrewdriver {

    /* ---------- PROJECTRED SCREWDRIVER ---------- */

    @Override
    default boolean canUse(EntityPlayer player, ItemStack stack) {
        if (player == null || GTUtility.isStackInvalid(stack)) return false;
        return getToolMaterial(stack) != Materials._NULL;
    }

    @Override
    default void damageScrewdriver(EntityPlayer player, ItemStack stack) {
        if (player == null || GTUtility.isStackInvalid(stack)) return;
        if (getToolMaterial(stack) == Materials._NULL) return;
        spendOneUse(stack);
    }

    /* ---------- USE ---------- */

    /** See {@link WrenchBehavior#wrenchOnItemUseFirst} for why this cannot just be {@code onItemUseFirst} itself. */
    default boolean screwdriverOnItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return ScrewdriverActions
            .use(world, x, y, z, hitX, hitY, hitZ, () -> player.capabilities.isCreativeMode || spendOneUse(stack));
    }
}
