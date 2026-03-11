package gregtech.common.items;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.items.GTGenericItem;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.item.ChaosLocatorGui;

public class ItemChaosLocator extends GTGenericItem implements IGuiHolder<GuiData> {

    private final String nameKey;
    private final String tooltipKey;

    public ItemChaosLocator(String aUnlocalized, String nameKey, String tooltipKey) {
        super(aUnlocalized, "", "");
        setMaxStackSize(1);
        this.nameKey = nameKey;
        this.tooltipKey = tooltipKey;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return GTUtility.translate(nameKey);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean f3) {
        if (tooltipKey != null && !tooltipKey.isEmpty()) {
            String translated = GTUtility.translate(tooltipKey);
            for (String line : translated.split("\\\\n")) {
                if (GTUtility.isStringValid(line)) {
                    list.add(line);
                }
            }
        }

        addAdditionalToolTips(list, stack, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (!world.isRemote) {
            GuiFactories.item()
                .open(player);
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager, UISettings uiSettings) {
        return new ChaosLocatorGui(guiData).build();
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        aList.add(translateToLocal("gt.item.chaos_locator.tooltip.01"));
    }

}
