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

package com.github.bartimaeusnek.bartworks;

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.client.gui.BW_GUIContainer_HeatedWaterPump;
import com.github.bartimaeusnek.bartworks.client.gui.BW_GUIContainer_RadLevel;
import com.github.bartimaeusnek.bartworks.client.gui.GT_GUIContainer_CircuitProgrammer;
import com.github.bartimaeusnek.bartworks.client.gui.GT_GUIContainer_Destructopack;
import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_TileEntity_HeatedWaterPump;
import com.github.bartimaeusnek.bartworks.server.container.BW_Container_HeatedWaterPump;
import com.github.bartimaeusnek.bartworks.server.container.BW_Container_RadioHatch;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_CircuitProgrammer;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_Item_Destructopack;
import cpw.mods.fml.common.network.IGuiHandler;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new GT_Container_Item_Destructopack(player.inventory);
            case 1:
                return new GT_Container_CircuitProgrammer(player.inventory);
            case 2: {
                if (world.getTileEntity(x, y, z) instanceof IGregTechTileEntity) {
                    IGregTechTileEntity te = (IGregTechTileEntity) world.getTileEntity(x, y, z);
                    return new BW_Container_RadioHatch(player.inventory, te.getMetaTileEntity());
                }
            }
            case 3:
                return new BW_Container_HeatedWaterPump(
                        (BW_TileEntity_HeatedWaterPump) world.getTileEntity(x, y, z), player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (SideReference.Side.Client) {
            switch (ID) {
                case 0:
                    return new GT_GUIContainer_Destructopack(player.inventory);
                case 1:
                    return new GT_GUIContainer_CircuitProgrammer(player.inventory);
                case 2: {
                    if (world.getTileEntity(x, y, z) instanceof IGregTechTileEntity) {
                        IGregTechTileEntity te = (IGregTechTileEntity) world.getTileEntity(x, y, z);
                        return new BW_GUIContainer_RadLevel(
                                new BW_Container_RadioHatch(player.inventory, te.getMetaTileEntity()));
                    }
                }
                case 3:
                    return new BW_GUIContainer_HeatedWaterPump(new BW_Container_HeatedWaterPump(
                            (BW_TileEntity_HeatedWaterPump) world.getTileEntity(x, y, z), player));
            }
        } else return getServerGuiElement(ID, player, world, x, y, z);
        return null;
    }
}
