package gregtech.common.items;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.misc.techtree.gui.TechTreeGui;
import gregtech.common.misc.techtree.gui.TechTreeGuiData;
import gregtech.common.misc.techtree.gui.TechTreeGuiFactory;

public class ItemTechAccessor extends Item implements IGuiHolder<TechTreeGuiData> {

    public ItemTechAccessor() {
        super();
        setUnlocalizedName("gt.techaccessor");
        setMaxStackSize(1);
        GameRegistry.registerItem(this, this.getUnlocalizedName(), GregTech.ID);
        GT_LanguageManager.addStringLocalization("item.gt.techaccessor.name", "Tech Tree Accessor");
    }

    @Override
    public ModularScreen createScreen(TechTreeGuiData data, ModularPanel mainPanel) {
        return new ModularScreen(mainPanel);
    }

    @Override
    public ModularPanel buildUI(TechTreeGuiData data, GuiSyncManager syncManager) {
        return TechTreeGui.buildUI(data, syncManager);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        TechTreeGuiFactory.open(player, player.serverPosX, player.serverPosY, player.serverPosZ);
        return itemStackIn;
    }
}
