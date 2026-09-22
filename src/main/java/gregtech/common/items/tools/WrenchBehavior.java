package gregtech.common.items.tools;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.implementations.items.IAEWrench;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Optional;
import crazypants.enderio.api.tool.ITool;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.IGTTool;

/**
 * What a wrench does, independent of whether it wears out or runs on EU: rotating blocks and the cross-mod wrench
 * interfaces.
 * <p/>
 * {@link ToolWrenchItem} and {@link ToolWrenchElectricItem} cannot share this by one extending the other -- each
 * already has to extend a different base ({@link ToolItemBase} or {@link ToolElectricItemBase}) for its durability
 * or energy model, and Java allows only one superclass. So the cross-mod interfaces are declared here instead, and
 * each class implements only this one interface.
 * <p/>
 * This has to {@code extend IToolWrench, IAEWrench, ITool} rather than merely mimicking their method signatures: a
 * default method satisfies an abstract method of an interface only when its <em>own</em> declaring interface extends
 * that interface (or the interface itself) -- an unrelated sibling interface's default, no matter how exactly the
 * signature matches, does not, and the class implementing both is rejected as still abstract. (Checked directly: a
 * class implementing both {@code A { void foo(); }} and unrelated {@code B { default void foo() {} }} fails to
 * compile; making {@code B extend A} fixes it.)
 * <p/>
 * The {@code @Optional} annotation moves here too, onto the interface, rather than staying on each implementing
 * class as it used to live on {@link ToolWrenchItem} alone. FML's stripping transformer
 * ({@code ModAPITransformer.stripInterface}) removes the named interface from whichever classfile carries the
 * annotation by editing that classfile's {@code interfaces} attribute directly, and that attribute exists on an
 * interface classfile exactly as it does on a regular class's, so this works the same way for both.
 */
@Optional.InterfaceList(
    value = { @Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = Mods.ModIDs.BUILD_CRAFT_CORE),
        @Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = Mods.ModIDs.ENDER_I_O), })
public interface WrenchBehavior extends IGTTool, IToolWrench, IAEWrench, ITool {

    /* ---------- CROSS-MOD WRENCH INTERFACES ---------- */

    @Override
    default boolean canWrench(EntityPlayer player, int x, int y, int z) {
        if (player == null) return false;
        return canWrench(player.getHeldItem(), player, x, y, z);
    }

    @Override
    default boolean canWrench(ItemStack wrench, EntityPlayer player, int x, int y, int z) {
        return wrench != null && getToolMaterial(wrench) != Materials._NULL;
    }

    @Override
    default void wrenchUsed(EntityPlayer player, int x, int y, int z) {}

    // EnderIO ITool
    @Override
    default boolean canUse(ItemStack stack, EntityPlayer player, int x, int y, int z) {
        return canWrench(player, x, y, z);
    }

    @Override
    default void used(ItemStack stack, EntityPlayer player, int x, int y, int z) {}

    @Override
    default boolean shouldHideFacades(ItemStack stack, EntityPlayer player) {
        return stack != null && getToolMaterial(stack) != Materials._NULL;
    }

    /* ---------- USE ---------- */

    /**
     * What {@code onItemUseFirst} does for a wrench. Named differently from {@code onItemUseFirst} itself because
     * that method is declared concretely on vanilla {@code Item}, so a default for it here would be silently
     * ignored (a class method always beats an interface default, no matter how the interface relates to anything
     * else); each implementing class still has to override {@code onItemUseFirst} and delegate to this one line.
     */
    default boolean wrenchOnItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return WrenchRotation
            .rotate(this, stack, player, world, x, y, z, ForgeDirection.getOrientation(ordinalSide), hitX, hitY, hitZ);
    }

    /* ---------- DISPLAY ---------- */

    /**
     * As with {@link #wrenchOnItemUseFirst}, named apart from {@code addBehaviourToolTips} because that method is a
     * concrete {@code protected} method on {@link ToolItemBase}.
     */
    default void addWrenchBehaviourToolTip(List<String> list) {
        list.add(translateToLocal("gt.behaviour.wrench"));
    }
}
