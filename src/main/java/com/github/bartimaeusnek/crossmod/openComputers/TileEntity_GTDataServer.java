/*
 * Copyright (c) 2019 bartimaeusnek
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
import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;

//@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntity_GTDataServer extends TileEntity implements ITileWithGUI, ITileAddsInformation, ITileHasDifferentTextureSides/*, SimpleComponent*/ {

    private static HashMap<Long,GT_NBT_DataBase> OrbDataBase = new HashMap<>();

    private ItemStack[] mItems = new ItemStack[2];
    
    
    private byte TickTimer = 0;

    public String getComponentName() {
        return "GT-Data Server";
    }

//    @Callback
//    @Optional.Method(modid = "OpenComputers")
//    public Object[] listData(Context context, Arguments args) {
//        return new String[]
//    }

    private boolean isServerSide(){
        return !this.worldObj.isRemote || FMLCommonHandler.instance().getSide().isServer();
    }
    
    
    @Override
    public void updateEntity() {
        if (this.TickTimer++ % 20 != 0)
            return;
        
        if (this.isServerSide()) {
            if (GT_Utility.areStacksEqual(this.mItems[0],ItemList.Tool_DataOrb.get(1))) {
                if (this.mItems[0].hasTagCompound()) {
                    if (GT_NBT_DataBase.getIdFromTag(this.mItems[0].getTagCompound()) == null) {
                        TileEntity_GTDataServer.OrbDataBase.put(
                                GT_NBT_DataBase.getMaxID(),
                                new GT_NBT_DataBase(
                                        Behaviour_DataOrb.getDataName(this.mItems[0]),
                                        Behaviour_DataOrb.getDataTitle(this.mItems[0]),
                                        this.mItems[0].getTagCompound()
                                )
                        );
                    }
                }
            }
            if (GT_Utility.areStacksEqual(this.mItems[0],ItemList.Tool_DataStick.get(1))) {
                if (this.mItems[0].hasTagCompound()) {
                    
                    String bookTitle = GT_Utility.ItemNBT.getBookTitle(this.mItems[0]);
                    String punchcardData = GT_Utility.ItemNBT.getPunchCardData(this.mItems[0]);
                    short mapID = GT_Utility.ItemNBT.getMapID(this.mItems[0]);
                    byte data = (byte) (bookTitle.isEmpty() ? punchcardData.isEmpty() ? mapID != -1 ? 3 : -1 : 2 : 1);
                    
                    String title = data == 1 ? bookTitle : data == 2 ? punchcardData : data == 3 ? ""+mapID : "Custom Data";
                    String name = data == 1 ? "eBook" : data == 2 ? "Punch Card Data" : data == 3 ? "Map Data" : "Custom Data";
                    if (GT_NBT_DataBase.getIdFromTag(this.mItems[0].getTagCompound()) == null) {
                        TileEntity_GTDataServer.OrbDataBase.put(
                                GT_NBT_DataBase.getMaxID(),
                                new GT_NBT_DataBase(
                                        name,
                                        title,
                                        this.mItems[0].getTagCompound()
                                )
                        );
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
    public void registerBlockIcons(IIconRegister par1IconRegister) {

    }

    @Override
    public int getGUIID() {
        return 0;
    }
}
