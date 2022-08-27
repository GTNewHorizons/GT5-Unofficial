/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.crossmod.openComputers;

import com.github.bartimaeusnek.bartworks.API.ITileAddsInformation;
import com.github.bartimaeusnek.bartworks.API.ITileHasDifferentTextureSides;
import com.github.bartimaeusnek.bartworks.API.ITileWithGUI;
import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import cpw.mods.fml.common.Optional;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntity_GTDataServer extends TileEntity
        implements ISidedInventory, ITileWithGUI, ITileAddsInformation, ITileHasDifferentTextureSides, SimpleComponent {

    private final BiMap<Long, GT_NBT_DataBase> OrbDataBase = HashBiMap.create();

    private ItemStack[] mItems = new ItemStack[2];
    private byte TickTimer;

    @Optional.Method(modid = "OpenComputers")
    public String getComponentName() {
        return "GT-Data Server";
    }

    @Optional.Method(modid = "OpenComputers")
    @Callback
    public Object[] listData(Context context, Arguments args) {
        Set<String> ret = new HashSet<>();
        for (Map.Entry<Long, GT_NBT_DataBase> entry : OrbDataBase.entrySet()) {
            ret.add((entry.getValue().getId() + Long.MAX_VALUE) + ". "
                    + entry.getValue().getmDataTitle());
        }
        return ret.toArray(new String[0]);
    }

    @Optional.Method(modid = "OpenComputers")
    @Callback
    public Object[] imprintOrb(Context context, Arguments args) {
        return new Object[] {false};
    }

    private boolean isServerSide() {
        return !this.worldObj.isRemote || SideReference.Side.Server;
    }

    @Override
    public void updateEntity() {
        if (this.TickTimer++ % 20 != 0) return;

        if (this.isServerSide()) {
            if (GT_Utility.areStacksEqual(this.mItems[0], ItemList.Tool_DataOrb.get(1))) {
                if (this.mItems[0].hasTagCompound()) {
                    if (GT_NBT_DataBase.getIdFromTag(this.mItems[0].getTagCompound()) == null) {
                        this.OrbDataBase.put(
                                GT_NBT_DataBase.getMaxID(),
                                new GT_NBT_DataBase(
                                        Behaviour_DataOrb.getDataName(this.mItems[0]),
                                        Behaviour_DataOrb.getDataTitle(this.mItems[0]),
                                        this.mItems[0].getTagCompound()));
                    } else {
                        long id = GT_NBT_DataBase.getIdFromTag(this.mItems[0].getTagCompound());
                        this.OrbDataBase.put(id, GT_NBT_DataBase.getGTTagFromId(id));
                    }
                }
            }
            if (GT_Utility.areStacksEqual(this.mItems[0], ItemList.Tool_DataStick.get(1))) {
                if (this.mItems[0].hasTagCompound()) {

                    String bookTitle = GT_Utility.ItemNBT.getBookTitle(this.mItems[0]);
                    String punchcardData = GT_Utility.ItemNBT.getPunchCardData(this.mItems[0]);
                    short mapID = GT_Utility.ItemNBT.getMapID(this.mItems[0]);
                    byte data = (byte) (bookTitle.isEmpty() ? punchcardData.isEmpty() ? mapID != -1 ? 3 : -1 : 2 : 1);

                    String title =
                            data == 1 ? bookTitle : data == 2 ? punchcardData : data == 3 ? "" + mapID : "Custom Data";
                    String name = data == 1
                            ? "eBook"
                            : data == 2 ? "Punch Card Data" : data == 3 ? "Map Data" : "Custom Data";
                    if (GT_NBT_DataBase.getIdFromTag(this.mItems[0].getTagCompound()) == null) {
                        this.OrbDataBase.put(
                                GT_NBT_DataBase.getMaxID(),
                                new GT_NBT_DataBase(name, title, this.mItems[0].getTagCompound()));
                    } else {
                        long id = GT_NBT_DataBase.getIdFromTag(this.mItems[0].getTagCompound());
                        this.OrbDataBase.put(id, GT_NBT_DataBase.getGTTagFromId(id));
                    }
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
    public int getGUIID() {
        return 0;
    }

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
    public ItemStack getStackInSlot(int p_70301_1_) {
        return p_70301_1_ == 0 ? this.mItems[0] : this.mItems[1];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        if (p_70299_1_ > 1 || p_70299_1_ < 0) return;
        this.mItems[p_70299_1_] = p_70299_2_;
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
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return p_94041_1_ == 0;
    }
}
