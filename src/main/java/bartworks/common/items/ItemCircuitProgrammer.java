/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.items;

import static gregtech.api.enums.Mods.GregTech;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import bartworks.MainMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.items.GTGenericItem;
import gregtech.common.gui.modularui.item.CircuitProgrammerGui;
import gregtech.crossmod.backhand.Backhand;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class ItemCircuitProgrammer extends GTGenericItem implements IElectricItem, IGuiHolder<PlayerInventoryGuiData> {

    private static final int COST_PER_USE = 100;

    public ItemCircuitProgrammer() {
        super("BWCircuitProgrammer", "Circuit Programmer", "Programs Integrated Circuits");
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setHasSubtypes(false);
        this.setCreativeTab(MainMod.BWT);
    }

    public boolean useItem(ItemStack stack, EntityPlayer player) {
        return ElectricItem.manager.use(stack, COST_PER_USE, player);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        if (aStack != null && aStack.getTagCompound() != null) aList.add(
            StatCollector.translateToLocal("tooltip.cp.0.name") + " "
                + (aStack.getTagCompound()
                    .getBoolean("HasChip") ? StatCollector.translateToLocal("tooltip.bw.yes.name")
                        : StatCollector.translateToLocal("tooltip.bw.no.name")));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (useItem(aStack, aPlayer) && !aWorld.isRemote) {
            if (aStack == Backhand.getOffhandItem(aPlayer)) {
                GuiFactories.playerInventory()
                    .openFromPlayerInventory(aPlayer, Backhand.getOffhandSlot(aPlayer));
            } else {
                GuiFactories.playerInventory()
                    .openFromMainHand(aPlayer);
            }
        }
        return aStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        if (this.getChargedItem(itemStack) == this) {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
            itemList.add(charged);
        }
        if (this.getEmptyItem(itemStack) == this) {
            itemList.add(new ItemStack(this, 1, this.getMaxDamage()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        this.mIcon = aIconRegister.registerIcon("bartworks:CircuitProgrammer");
    }

    @Override
    public int getTier(ItemStack var1) {
        return 1;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return GTValues.V[1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModularScreen createScreen(PlayerInventoryGuiData data, ModularPanel mainPanel) {
        return new ModularScreen(GregTech.ID, mainPanel);
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return new CircuitProgrammerGui(data, syncManager).build();
    }
}
