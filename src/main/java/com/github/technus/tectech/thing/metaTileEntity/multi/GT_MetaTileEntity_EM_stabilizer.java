package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.HatchAdder;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_stabilizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region structure
    private static final String[][] shape = new String[][]{
            {"A010","0   0","1 . 1","0   0","A010",},
            {"23232","32223","22222","32223","23232",},
            {"12!21","22422","!444!","22422","12!21",},
            {"23232","32223","22222","32223","23232",},
            {"A010","0   0","1   1","0   0","A010",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT ,sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4, 0, 5, 6, 9};
    private final HatchAdder[] addingMethods = new HatchAdder[]{this::addClassicToMachineList, this::addElementalToMachineList};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Hatches or Molecular Casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_stabilizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_stabilizer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_stabilizer(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMeta,2, 2, 0, getBaseMetaTileEntity(),this,hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Alters time to stabilize matter",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Wibbly wobbly timey wimey, stuff."
        };
    }
}
