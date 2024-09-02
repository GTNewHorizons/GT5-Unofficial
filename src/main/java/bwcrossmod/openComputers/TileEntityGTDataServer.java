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

package bwcrossmod.openComputers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import bartworks.API.ITileAddsInformation;
import bartworks.API.ITileHasDifferentTextureSides;
import bartworks.API.SideReference;
import cpw.mods.fml.common.Optional;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourDataOrb;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = Mods.Names.OPEN_COMPUTERS)
public class TileEntityGTDataServer extends TileEntity
    implements ISidedInventory, ITileAddsInformation, ITileHasDifferentTextureSides, SimpleComponent {

    private final BiMap<Long, GTNBTDataBase> OrbDataBase = HashBiMap.create();

    private ItemStack[] mItems = new ItemStack[2];
    private byte TickTimer;

    @Optional.Method(modid = Mods.Names.OPEN_COMPUTERS)
    public String getComponentName() {
        return "GT-Data Server";
    }

    @Optional.Method(modid = Mods.Names.OPEN_COMPUTERS)
    @Callback
    public Object[] listData(Context context, Arguments args) {
        Set<String> ret = new HashSet<>();
        for (Map.Entry<Long, GTNBTDataBase> entry : this.OrbDataBase.entrySet()) {
            ret.add(
                entry.getValue()
                    .getId() + Long.MAX_VALUE
                    + ". "
                    + entry.getValue()
                        .getmDataTitle());
        }
        return ret.toArray(new String[0]);
    }

    @Optional.Method(modid = Mods.Names.OPEN_COMPUTERS)
    @Callback
    public Object[] imprintOrb(Context context, Arguments args) {
        return new Object[] { false };
    }

    private boolean isServerSide() {
        return !this.worldObj.isRemote || SideReference.Side.Server;
    }

    @Override
    public void updateEntity() {
        if (this.TickTimer++ % 20 != 0) return;

        if (this.isServerSide()) {
            if (GTUtility.areStacksEqual(this.mItems[0], ItemList.Tool_DataOrb.get(1))
                && this.mItems[0].hasTagCompound()) {
                if (GTNBTDataBase.getIdFromTag(this.mItems[0].getTagCompound()) == null) {
                    this.OrbDataBase.put(
                        GTNBTDataBase.getMaxID(),
                        new GTNBTDataBase(
                            BehaviourDataOrb.getDataName(this.mItems[0]),
                            BehaviourDataOrb.getDataTitle(this.mItems[0]),
                            this.mItems[0].getTagCompound()));
                } else {
                    long id = GTNBTDataBase.getIdFromTag(this.mItems[0].getTagCompound());
                    this.OrbDataBase.put(id, GTNBTDataBase.getGTTagFromId(id));
                }
            }
            if (GTUtility.areStacksEqual(this.mItems[0], ItemList.Tool_DataStick.get(1))
                && this.mItems[0].hasTagCompound()) {

                String bookTitle = GTUtility.ItemNBT.getBookTitle(this.mItems[0]);
                String punchcardData = GTUtility.ItemNBT.getPunchCardData(this.mItems[0]);
                short mapID = GTUtility.ItemNBT.getMapID(this.mItems[0]);
                byte data = (byte) (bookTitle.isEmpty() ? punchcardData.isEmpty() ? mapID != -1 ? 3 : -1 : 2 : 1);

                String title = data == 1 ? bookTitle
                    : data == 2 ? punchcardData : data == 3 ? "" + mapID : "Custom Data";
                String name = data == 1 ? "eBook"
                    : data == 2 ? "Punch Card Data" : data == 3 ? "Map Data" : "Custom Data";
                if (GTNBTDataBase.getIdFromTag(this.mItems[0].getTagCompound()) == null) {
                    this.OrbDataBase
                        .put(GTNBTDataBase.getMaxID(), new GTNBTDataBase(name, title, this.mItems[0].getTagCompound()));
                } else {
                    long id = GTNBTDataBase.getIdFromTag(this.mItems[0].getTagCompound());
                    this.OrbDataBase.put(id, GTNBTDataBase.getGTTagFromId(id));
                }
            }
        }
    }

    @Override
    public String[] getInfoData() {
        return new String[0];
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {}

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return this.mItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return slotIn == 0 ? this.mItems[0] : this.mItems[1];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index > 1 || index < 0) return;
        this.mItems[index] = stack;
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0;
    }
}
