package com.github.technus.tectech.loader;

import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import com.github.technus.tectech.mechanics.constructable.IMultiblockInfoContainer;
import com.github.technus.tectech.mechanics.structure.Structure;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ElectricBlastFurnace;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.mechanics.constructable.IMultiblockInfoContainer.*;
import static gregtech.api.GregTech_API.sBlockCasings1;

public class ConstructableLoader implements Runnable {

    @Override
    public void run() {
        registerMetaClass(GT_MetaTileEntity_ElectricBlastFurnace.class, new IMultiblockInfoContainer() {
            //region Structure
            private final String[][] shape = new String[][]{
                    {"000","\"\"\"","\"\"\""," . ",},
                    {"0!0","\"A\"","\"A\"","   ",},
                    {"000","\"\"\"","\"\"\"","   ",},
            };
            private final Block[] blockType = new Block[]{sBlockCasings1};
            private final byte[] blockMeta = new byte[]{11};
            private final String[] desc=new String[]{
                    EnumChatFormatting.AQUA+"Hint Details:",
                    "1 - Classic Hatches or Heat Proof Casing",
                    "2 - Muffler Hatch",
                    "3 - Coil blocks"
            };
            //endregion

            @Override
            public void construct(ItemStack stackSize, boolean hintsOnly, TileEntity tileEntity, ExtendedFacing aSide) {
                Structure.builder(shape, blockType, blockMeta, 1, 3, 0, tileEntity, aSide, hintsOnly);
            }

            @Override
            public String[] getDescription(ItemStack stackSize) {
                return desc;
            }
        });
    }
}
