package GoodGenerator.util;

import com.github.technus.tectech.TecTech;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Arrays;

public class StructureHelper {

    public static <T> IStructureElement<T> addFrame(Materials aMaterials) {
        return new IStructureElement<T>() {

            private IIcon[] mIcons;

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tBlock = world.getTileEntity(x, y, z);
                if (tBlock instanceof BaseMetaPipeEntity) {
                    BaseMetaPipeEntity tFrame = (BaseMetaPipeEntity) tBlock;
                    if (tFrame.isInvalidTileEntity()) return false;
                    if (tFrame.getMetaTileEntity() instanceof GT_MetaPipeEntity_Frame) {
                        return ((GT_MetaPipeEntity_Frame) tFrame.getMetaTileEntity()).mMaterial == aMaterials;
                    }
                }
                return false;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                if (mIcons == null) {
                    mIcons = new IIcon[6];
                    Arrays.fill(mIcons, aMaterials.mIconSet.mTextures[OrePrefixes.frameGt.mTextureIndex].getIcon());
                }
                TecTech.proxy.hint_particle_tinted(world, x, y, z, mIcons, aMaterials.mRGBa);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                ItemStack tFrame = GT_OreDictUnificator.get(OrePrefixes.frameGt, aMaterials, 1);
                if (tFrame.getItem() instanceof ItemBlock) {
                    ItemBlock tFrameStackItem = (ItemBlock) tFrame.getItem();
                    return tFrameStackItem.placeBlockAt(tFrame, null, world, x, y, z, 6, 0, 0, 0, Items.feather.getDamage(tFrame));
                }
                return false;
            }
        };
    }

}
