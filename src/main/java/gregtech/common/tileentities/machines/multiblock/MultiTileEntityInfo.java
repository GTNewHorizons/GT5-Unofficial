package gregtech.common.tileentities.machines.multiblock;

import com.badlogic.ashley.core.Entity;
import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.mutecore.api.block.MultiTileEntityBlock;
import com.gtnewhorizons.mutecore.api.item.MultiTileEntityItem;
import com.gtnewhorizons.mutecore.api.tile.MultiTileEntity;

import gregtech.api.multitileentity.StructureHandler;
import gregtech.api.multitileentity.data.Structure;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MultiTileEntityInfo implements IMultiblockInfoContainer<MultiTileEntity> {

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly, MultiTileEntity tileEntity,
            ExtendedFacing side) {
        Entity entity = tileEntity.getEntity();
        if (entity.getComponent(Structure.class) == null)
            return;
        Structure struct = entity.getComponent(Structure.class);
        StructureHandler structH;
        try {
            structH = struct.getHandlerClass()
                    .getConstructor(Entity.class)
                    .newInstance(entity);
        } catch (Exception ing) {
            return;
        }
        structH.construct(stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudge, ISurvivalBuildEnvironment env,
            MultiTileEntity tileEntity, ExtendedFacing side) {
        Entity entity = tileEntity.getEntity();
        if (entity.getComponent(Structure.class) == null)
            return -1;
        Structure struct = entity.getComponent(Structure.class);
        StructureHandler structH;
        try {
            structH = struct.getHandlerClass()
                    .getConstructor(Entity.class)
                    .newInstance(entity);
        } catch (Exception ing) {
            return -1;
        }
        return structH.survivalConstruct(stackSize, elementBudge, env);
    }

    @Override
    public String[] getDescription(ItemStack stackSize) {
        Item item = stackSize.getItem();
        if (!(item instanceof MultiTileEntityItem muteItem))
            return new String[0];

        MultiTileEntityBlock muBlock = (MultiTileEntityBlock) Block.getBlockFromItem(muteItem);
        Entity entity = muBlock.getRegistry()
                .getMultiTileContainer(stackSize.getItemDamage())
                .getFakeEntity();
        if (entity.getComponent(Structure.class) == null)
            return new String[0];
        Structure struct = entity.getComponent(Structure.class);
        StructureHandler structH;
        try {
            structH = struct.getHandlerClass()
                    .getConstructor(Entity.class)
                    .newInstance(entity);
        } catch (Exception ing) {
            return new String[0];
        }
        return structH.getStructureDescription(stackSize);
    }

}
