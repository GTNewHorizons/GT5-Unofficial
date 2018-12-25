package com.github.bartimaeusnek.bartworks.common.tileentities;

import com.github.bartimaeusnek.bartworks.client.gui.BW_GUIContainer_RotorBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.kineticgenerator.container.ContainerWindKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class BW_RotorBlock extends TileEntityWindKineticGenerator {

    public int getGrindPower(){
       return super.getKuOutput();
    }

    public int getKuOutput() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new BW_GUIContainer_RotorBlock(new ContainerWindKineticGenerator(entityPlayer, this));
    }

}
