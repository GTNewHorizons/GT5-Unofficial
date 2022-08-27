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

package com.github.bartimaeusnek.bartworks.API;

import com.github.bartimaeusnek.bartworks.MainMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public interface ITileWithGUI {

    /**
     * @return the ID of the GUI, see @link com.github.bartimaeusnek.bartworks.GuiHandler
     */
    int getGUIID();

    /**
     * gets called from BW_TileEntityContainer(or _Multiple) when right clicked.
     *
     * @param tileEntity this tile entity
     * @param player     the player right clicking it
     * @return true always.
     */
    default boolean openGUI(TileEntity tileEntity, EntityPlayer player) {
        if (!tileEntity.getWorldObj().isRemote)
            player.openGui(
                    MainMod.MOD_ID,
                    getGUIID(),
                    tileEntity.getWorldObj(),
                    tileEntity.xCoord,
                    tileEntity.yCoord,
                    tileEntity.zCoord);
        return true;
    }
}
