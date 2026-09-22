package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * An electric wire cutter: LV, MV or HV.
 * <p/>
 * These run on EU only, via {@link ToolElectricItemBase} -- the old metadata-based ones carried a durability bar as
 * well, but only lost a point of it on one action in twenty-five, so it was noise on top of the energy cost; the
 * energy cost per action is unchanged, since {@code GTModHandler.damageOrDechargeItem} always reached these through
 * {@code IDamagableItem} and so spent 100 units per use either way.
 * <p/>
 * This cannot extend {@link ToolWireCutterItem}, because it already has to extend {@link ToolElectricItemBase} for
 * its energy model. What a wire cutter actually does comes from {@link WireCutterBehavior} instead, shared with
 * {@link ToolWireCutterItem} by composition rather than inheritance.
 */
public class ToolWireCutterElectricItem extends ToolElectricItemBase implements WireCutterBehavior {

    /**
     * Energy one action costs. The tree farm drives this tool through the machine path, which cost 100 an operation,
     * so that is what it keeps; being consumed by a recipe used to cost 400 and now costs the same 100.
     */
    public static final long EU_PER_USE = 100;

    @Override
    public long getEnergyCostPerUse() {
        return EU_PER_USE;
    }

    public ToolWireCutterElectricItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip, long maxCharge, long voltage, int tier) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            englishTooltip,
            maxCharge,
            voltage,
            tier,
            GregTechAPI.sWireCutterList,
            ToolDictNames.craftingToolWireCutter);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        return wireCutterOnItemUseFirst(stack, player, world, x, y, z, ordinalSide, hitX, hitY, hitZ);
    }
}
