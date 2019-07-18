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

import com.github.bartimaeusnek.bartworks.common.tileentities.tiered.GT_MetaTileEntity_RadioHatch;
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
    private long timer;

    public GT_Container_RadioHatch(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!this.mTileEntity.isClientSide() && this.mTileEntity.getMetaTileEntity() != null) {
            this.TE = (GT_MetaTileEntity_RadioHatch) this.mTileEntity.getMetaTileEntity();
            this.mass = this.TE.getMass();
            this.sievert = (short) this.TE.sievert;
            short[] rgb = this.TE.getColorForGUI();
            this.r = rgb[0];
            this.g = rgb[1];
            this.b = rgb[2];
            this.sv = (short) this.TE.getSievert();
            this.teTimer = ByteBuffer.allocate(8).putLong(this.TE.getTimer()).array();
            ++this.timer;
            Iterator var2 = this.crafters.iterator();
            if (this.timer >= Long.MAX_VALUE - 1)
                this.timer = 0;
            while (true) {
                do {
                    if (!var2.hasNext()) {
                        this.dmass = this.mass;
                        this.dsievert = this.sievert;
                        this.dr = this.r;
                        this.dg = this.g;
                        this.db = this.b;
                        this.dteTimer = this.teTimer;
                        this.dsv = this.sv;
                        return;
                    }
                    ICrafting var1 = (ICrafting) var2.next();

                    if (this.timer % 500 == 10 || this.dmass != this.mass)
                        var1.sendProgressBarUpdate(this, 21, this.mass);
                    if (this.timer % 500 == 10 || this.dsievert != this.sievert)
                        var1.sendProgressBarUpdate(this, 22, (this.sievert - 100));
                    if (this.timer % 500 == 10 || this.dr != this.r)
                        var1.sendProgressBarUpdate(this, 23, this.r);
                    if (this.timer % 500 == 10 || this.dg != this.g)
                        var1.sendProgressBarUpdate(this, 24, this.g);
                    if (this.timer % 500 == 10 || this.db != this.b)
                        var1.sendProgressBarUpdate(this, 25, this.b);
                    if (this.timer % 500 == 10 || this.dteTimer != this.teTimer)
                        for (int i = 0; i < this.teTimer.length; i++) {
                            var1.sendProgressBarUpdate(this, 26 + i, this.teTimer[i]);
                        }
                    if (this.timer % 500 == 10 || this.dsv != this.sv)
                        var1.sendProgressBarUpdate(this, 34, this.sv);

                } while (this.timer % 500 != 10 && this.dmass == this.mass);
            }
        }
    }


    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 21:
                this.mass = (byte) par2;
                break;
            case 22:
                this.sievert = (short) (par2 + 100);
                break;
            case 23:
                this.r = (short) par2;
                break;
            case 24:
                this.g = (short) par2;
                break;
            case 25:
                this.b = (short) par2;
                break;
            case 26:
                this.teTimer[0] = (byte) par2;
                break;
            case 27:
                this.teTimer[1] = (byte) par2;
                break;
            case 28:
                this.teTimer[2] = (byte) par2;
                break;
            case 29:
                this.teTimer[3] = (byte) par2;
                break;
            case 30:
                this.teTimer[4] = (byte) par2;
                break;
            case 31:
                this.teTimer[5] = (byte) par2;
                break;
            case 32:
                this.teTimer[6] = (byte) par2;
                break;
            case 33:
                this.teTimer[7] = (byte) par2;
                break;
            case 34:
                this.sv = (short) par2;
                break;
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

}
