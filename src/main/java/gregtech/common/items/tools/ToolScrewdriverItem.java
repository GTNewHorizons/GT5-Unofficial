package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.util.GTUtility;
import mrtjp.projectred.api.IScrewdriver;

/**
 * A standalone screwdriver: adjusts covers and machines, and answers to ProjectRed's screwdriver interface.
 * <p/>
 * One instance of this class is registered per tier. The electric tiers use {@link ToolScrewdriverElectricItem},
 * which stores energy instead of durability. See {@link ToolItemBase} for everything these have in common with the
 * other standalone tools.
 */
@Optional.Interface(iface = "mrtjp.projectred.api.IScrewdriver", modid = Mods.ModIDs.PROJECT_RED_CORE)
public class ToolScrewdriverItem extends ToolItemBase implements IScrewdriver {

    /** Durability cost of one use, in the unit where 100 is one durability point. */
    public static final int USE_COST = 100;

    /**
     * @param unlocalizedName   appended to {@code gt.}; becomes both the registry name and the localization key root.
     * @param toolStats         the generic stats for this tier, reused verbatim from the old tool registry.
     * @param englishNameFormat the default display name, where {@code %material} is replaced by the material name.
     */
    public ToolScrewdriverItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            "Adjusts covers and machines",
            GregTechAPI.sScrewdriverList,
            ToolDictNames.craftingToolScrewdriver);
    }

    /* ---------- USE ---------- */

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return ScrewdriverActions.use(
            world,
            x,
            y,
            z,
            hitX,
            hitY,
            hitZ,
            () -> player.capabilities.isCreativeMode || doDamage(stack, USE_COST));
    }

    /* ---------- PROJECTRED SCREWDRIVER ---------- */

    @Override
    public boolean canUse(EntityPlayer player, ItemStack stack) {
        if (player == null || GTUtility.isStackInvalid(stack)) return false;
        return getToolMaterial(stack) != Materials._NULL;
    }

    @Override
    public void damageScrewdriver(EntityPlayer player, ItemStack stack) {
        if (player == null || GTUtility.isStackInvalid(stack)) return;
        if (getToolMaterial(stack) == Materials._NULL) return;
        doDamage(stack, getToolStats().getToolDamagePerEntityAttack());
    }
}
