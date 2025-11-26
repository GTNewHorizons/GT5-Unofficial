package gregtech.common.items;

import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import mods.railcraft.api.core.items.TagList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.items.GTGenericItem;
import gregtech.common.gui.modularui.item.ItemToolboxGui;

import java.util.ArrayList;

public class ItemGTToolbox extends GTGenericItem implements IGuiHolder<PlayerInventoryGuiData> {


    public ItemGTToolbox(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            GuiFactories.playerInventory()
                .openFromMainHand(player);


        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return new ItemToolboxGui(syncManager, data).build();
    }

    public IItemHandlerModifiable getHandler(ItemStack stack) {
        return new ItemStackItemHandler(() -> stack, s -> {}, 9);
    }
}
