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
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone wrench: rotates blocks, dismantles machines, and answers to the cross-mod wrench interfaces.
 * <p/>
 * One instance of this class is registered per wrench tier. The electric tiers use {@link ToolWrenchElectricItem},
 * which stores energy instead of durability. See {@link ToolItemBase} for everything these have in common with the
 * other standalone tools.
 */
@Optional.InterfaceList(
    value = { @Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = Mods.ModIDs.BUILD_CRAFT_CORE),
        @Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = Mods.ModIDs.ENDER_I_O), })
public class ToolWrenchItem extends ToolItemBase implements IToolWrench, IAEWrench, ITool {

    /**
     * @param unlocalizedName   appended to {@code gt.}; becomes both the registry name and the localization key root.
     * @param toolStats         the generic stats for this wrench tier, reused verbatim from the old tool registry.
     * @param englishNameFormat the default display name, where {@code %material} is replaced by the material name.
     * @param englishTooltip    the default tooltip, or an empty string for none.
     */
    public ToolWrenchItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            englishTooltip,
            GregTechAPI.sWrenchList,
            ToolDictNames.craftingToolWrench);
    }

    /* ---------- USE ---------- */

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return WrenchRotation
            .rotate(this, stack, player, world, x, y, z, ForgeDirection.getOrientation(ordinalSide), hitX, hitY, hitZ);
    }

    /* ---------- CROSS-MOD WRENCH INTERFACES ---------- */

    @Override
    public boolean canWrench(EntityPlayer player, int x, int y, int z) {
        if (player == null) return false;
        return canWrench(player.getHeldItem(), player, x, y, z);
    }

    @Override
    public boolean canWrench(ItemStack wrench, EntityPlayer player, int x, int y, int z) {
        return wrench != null && getToolMaterial(wrench) != Materials._NULL;
    }

    @Override
    public void wrenchUsed(EntityPlayer player, int x, int y, int z) {}

    // EnderIO ITool
    @Override
    public boolean canUse(ItemStack stack, EntityPlayer player, int x, int y, int z) {
        return canWrench(player, x, y, z);
    }

    @Override
    public void used(ItemStack stack, EntityPlayer player, int x, int y, int z) {}

    @Override
    public boolean shouldHideFacades(ItemStack stack, EntityPlayer player) {
        return stack != null && getToolMaterial(stack) != Materials._NULL;
    }

    /* ---------- DISPLAY ---------- */

    @Override
    protected void addBehaviourToolTips(List<String> list, ItemStack stack) {
        list.add(translateToLocal("gt.behaviour.wrench"));
    }
}
