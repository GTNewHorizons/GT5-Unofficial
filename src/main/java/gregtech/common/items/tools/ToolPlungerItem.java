package gregtech.common.items.tools;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatFluid;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.function.BooleanSupplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone plunger: empties item pipes, fluid tanks and (with Thaumcraft) essentia tubes.
 * <p/>
 * See {@link ToolItemBase} for everything it has in common with the other standalone tools.
 */
public class ToolPlungerItem extends ToolItemBase {

    public ToolPlungerItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip) {
        super(unlocalizedName, toolStats, englishNameFormat, englishTooltip, null, ToolDictNames.craftingToolPlunger);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        final BooleanSupplier pay = () -> player.capabilities.isCreativeMode || spendOneUse(stack);

        // Same order the three behaviours were registered in; the first one to take the click wins.
        if (PlungerActions
            .pullItems(player, world, x, y, z, ForgeDirection.getOrientation(ordinalSide), hitX, hitY, hitZ, pay)) {
            return true;
        }
        if (PlungerActions.pullFluid(player, world, x, y, z, hitX, hitY, hitZ, pay)) return true;
        // Naming PlungerEssentiaActions is what loads it, and all of it touches Thaumcraft, so the check comes first.
        return Mods.Thaumcraft.isModLoaded()
            && PlungerEssentiaActions.pullEssentia(world, x, y, z, hitX, hitY, hitZ, pay);
    }

    @Override
    protected void addBehaviourToolTips(List<String> list, ItemStack stack) {
        list.add(translateToLocal("gt.behaviour.plunger.item"));
        list.add(translateToLocalFormatted("gt.behaviour.plunger.fluid", formatFluid(PlungerActions.PLUNGER_DRAIN_AMOUNT)));
        if (Mods.Thaumcraft.isModLoaded()) list.add(translateToLocal("gt.behaviour.plunger.essentia"));
    }
}
