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

package com.github.bartimaeusnek.bartworks.server.container;

import com.github.bartimaeusnek.bartworks.common.tileentities.GT_MetaTileEntity_RadioHatch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_Container_1by1;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import java.nio.ByteBuffer;
import java.util.Iterator;

public class GT_Container_RadioHatch extends GT_Container_1by1 {

    public byte mass, dmass;
    public short sv, dsv, sievert, r, g, b, dsievert, dr, dg, db;
    public byte[] teTimer = new byte[8], dteTimer = new byte[8];
    GT_MetaTileEntity_RadioHatch TE;
    private long timer = 0;

    public GT_Container_RadioHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!this.mTileEntity.isClientSide() && this.mTileEntity.getMetaTileEntity() != null) {
            TE = (GT_MetaTileEntity_RadioHatch) this.mTileEntity.getMetaTileEntity();
            mass = TE.getMass();
            sievert = (short) TE.sievert;
            short[] rgb = TE.getColorForGUI();
            r = rgb[0];
            g = rgb[1];
            b = rgb[2];
            sv = (short) TE.getSievert();
            teTimer = ByteBuffer.allocate(8).putLong(TE.getTimer()).array();
            ++timer;
            Iterator var2 = this.crafters.iterator();
            if (timer >= Long.MAX_VALUE - 1)
                timer = 0;
            while (true) {
                do {
                    if (!var2.hasNext()) {
                        dmass = mass;
                        dsievert = sievert;
                        dr = r;
                        dg = g;
                        db = b;
                        dteTimer = teTimer;
                        dsv = sv;
                        return;
                    }
                    ICrafting var1 = (ICrafting) var2.next();

                    if (this.timer % 500 == 10 || this.dmass != mass)
                        var1.sendProgressBarUpdate(this, 21, mass);
                    if (this.timer % 500 == 10 || this.dsievert != sievert)
                        var1.sendProgressBarUpdate(this, 22, (sievert - 100));
                    if (this.timer % 500 == 10 || this.dr != r)
                        var1.sendProgressBarUpdate(this, 23, r);
                    if (this.timer % 500 == 10 || this.dg != g)
                        var1.sendProgressBarUpdate(this, 24, g);
                    if (this.timer % 500 == 10 || this.db != b)
                        var1.sendProgressBarUpdate(this, 25, b);
                    if (this.timer % 500 == 10 || this.dteTimer != teTimer)
                        for (int i = 0; i < teTimer.length; i++) {
                            var1.sendProgressBarUpdate(this, 26 + i, teTimer[i]);
                        }
                    if (this.timer % 500 == 10 || this.dsv != sv)
                        var1.sendProgressBarUpdate(this, 34, sv);

                } while (this.timer % 500 != 10 && this.dmass == this.mass);
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 21:
                mass = (byte) par2;
                break;
            case 22:
                sievert = (short) (par2 + 100);
                break;
            case 23:
                r = (short) par2;
                break;
            case 24:
                g = (short) par2;
                break;
            case 25:
                b = (short) par2;
                break;
            case 26:
                teTimer[0] = (byte) par2;
                break;
            case 27:
                teTimer[1] = (byte) par2;
                break;
            case 28:
                teTimer[2] = (byte) par2;
                break;
            case 29:
                teTimer[3] = (byte) par2;
                break;
            case 30:
                teTimer[4] = (byte) par2;
                break;
            case 31:
                teTimer[5] = (byte) par2;
                break;
            case 32:
                teTimer[6] = (byte) par2;
                break;
            case 33:
                teTimer[7] = (byte) par2;
                break;
            case 34:
                sv = (short) par2;
                break;
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

}
