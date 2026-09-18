package gregtech.common.items.tools;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone scoop: catches Forestry butterflies, and is otherwise an ordinary tool.
 * <p/>
 * See {@link ToolItemBase} for everything it has in common with the other standalone tools.
 */
public class ToolScoopItem extends ToolItemBase {

    public ToolScoopItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip) {
        super(unlocalizedName, toolStats, englishNameFormat, englishTooltip, null, ToolDictNames.craftingToolScoop);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        // Naming ScoopActions is what loads it, and everything in it touches Forestry, so the check comes first.
        if (Mods.Forestry.isModLoaded() && getToolMaterial(stack) != Materials._NULL
            && ScoopActions.catchButterfly(this, stack, player, entity)) {
            return true;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    protected void addBehaviourToolTips(List<String> list, ItemStack stack) {
        if (Mods.Forestry.isModLoaded()) list.add(translateToLocal("gt.behaviour.scoop"));
    }
}
