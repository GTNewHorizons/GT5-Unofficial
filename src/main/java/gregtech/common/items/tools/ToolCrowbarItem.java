package gregtech.common.items.tools;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;
import mods.railcraft.api.core.items.IToolCrowbar;

/**
 * A standalone crowbar: pulls covers off machines, turns rails, and answers to Railcraft's crowbar interface.
 * <p/>
 * There is no electric crowbar, so this is a single item. See {@link ToolItemBase} for everything it has in common
 * with the other standalone tools.
 */
@Optional.Interface(iface = "mods.railcraft.api.core.items.IToolCrowbar", modid = Mods.ModIDs.RAILCRAFT)
public class ToolCrowbarItem extends ToolItemBase implements IToolCrowbar {

    /** Durability cost of one use, in the unit where 100 is one durability point. */
    public static final int USE_COST = 100;

    public ToolCrowbarItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            "Dismounts Covers and Rotates Rails",
            ToolDictNames.craftingToolCrowbar,
            GregTechAPI.sCrowbarList);
    }

    /* ---------- USE ---------- */

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return CrowbarActions.use(
            world,
            x,
            y,
            z,
            hitX,
            hitY,
            hitZ,
            () -> player.capabilities.isCreativeMode || doDamage(stack, USE_COST));
    }

    /* ---------- RAILCRAFT CROWBAR ---------- */

    private boolean isUsable(ItemStack stack) {
        return stack != null && getToolMaterial(stack) != Materials._NULL;
    }

    private void wear(ItemStack stack) {
        doDamage(stack, getToolStats().getToolDamagePerEntityAttack());
    }

    @Override
    public boolean canWhack(EntityPlayer player, ItemStack stack, int x, int y, int z) {
        return isUsable(stack);
    }

    @Override
    public void onWhack(EntityPlayer player, ItemStack stack, int x, int y, int z) {
        wear(stack);
    }

    @Override
    public boolean canLink(EntityPlayer player, ItemStack stack, EntityMinecart cart) {
        return isUsable(stack) && player.isSneaking();
    }

    @Override
    public void onLink(EntityPlayer player, ItemStack stack, EntityMinecart cart) {
        wear(stack);
    }

    @Override
    public boolean canBoost(EntityPlayer player, ItemStack stack, EntityMinecart cart) {
        return isUsable(stack) && !player.isSneaking();
    }

    @Override
    public void onBoost(EntityPlayer player, ItemStack stack, EntityMinecart cart) {
        wear(stack);
    }
}
