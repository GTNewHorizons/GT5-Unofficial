package com.github.technus.tectech.loader;

import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import com.github.technus.tectech.mechanics.constructable.IMultiblockInfoContainer;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ElectricBlastFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.mechanics.constructable.IMultiblockInfoContainer.registerMetaClass;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.ofBlock;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.ofHintOnly;
import static gregtech.api.GregTech_API.sBlockCasings1;

public class ConstructableLoader implements Runnable {

    @Override
    public void run() {
        registerMetaClass(GT_MetaTileEntity_ElectricBlastFurnace.class, new IMultiblockInfoContainer<GT_MetaTileEntity_ElectricBlastFurnace>() {
            //region Structure
            private final IStructureDefinition<GT_MetaTileEntity_ElectricBlastFurnace> definition=
                    StructureDefinition.<GT_MetaTileEntity_ElectricBlastFurnace>builder()
                            .addShapeOldApi("main",new String[][]{
                                    {"000","\"\"\"","\"\"\""," . ",},
                                    {"0!0","\"A\"","\"A\"","   ",},
                                    {"000","\"\"\"","\"\"\"","   ",},
                            })
                            .addElement('0', ofBlock(sBlockCasings1,11))
                            .addElement('\"', ofHintOnly(3))
                            .addElement('!', ofHintOnly(2))
                            .addElement(' ', ofHintOnly(1))
                            .build();
            private final String[] desc=new String[]{
                    EnumChatFormatting.AQUA+"Hint Details:",
                    "1 - Classic Hatches or Heat Proof Casing",
                    "2 - Muffler Hatch",
                    "3 - Coil blocks"
            };
            //endregion

            @Override
            public void construct(ItemStack stackSize, boolean hintsOnly, GT_MetaTileEntity_ElectricBlastFurnace tileEntity, ExtendedFacing aSide) {
                IGregTechTileEntity base = tileEntity.getBaseMetaTileEntity();
                definition.buildOrHints(tileEntity,"main", base.getWorld(),aSide,
                        base.getXCoord(),base.getYCoord(),base.getZCoord(),
                        1, 3, 0,hintsOnly);
            }

            @Override
            public String[] getDescription(ItemStack stackSize) {
                return desc;
            }
        });
    }
}
