package gtPlusPlus.xmod.gregtech.common.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class GT_MethodHelper {

	private static final Method mGetTexture;
	private static final Class mITexturedTileEntity;

	static {
		Class clazz = null;
		Method aMeth = null;
		if (ReflectionUtils.doesClassExist("gregtech.api.interfaces.tileentity.ITexturedTileEntity")) {
			clazz = ReflectionUtils.getClass("gregtech.api.interfaces.tileentity.ITexturedTileEntity");
			aMeth = ReflectionUtils.getMethod(clazz, "getTexture", Block.class, byte.class);
		}
		mITexturedTileEntity = clazz;
		mGetTexture = aMeth;
	}


	public static ITexture[] getTexture(TileEntity tTileEntity, Block aBlock, byte aSide) {

		if (mITexturedTileEntity.isInstance(tTileEntity)) {

			if (mGetTexture != null) {
				try {
					mGetTexture.invoke(tTileEntity, aBlock, aSide);
				}
				catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			else {
				if (tTileEntity instanceof BaseMetaTileEntity) {
					try {
						BaseMetaTileEntity aTile = (BaseMetaTileEntity) tTileEntity;					
						ITexture rIcon = aTile.getCoverTexture(aSide);
						Field aFacing = ReflectionUtils.getField(BaseMetaTileEntity.class, "mFacing");
						Field aColor = ReflectionUtils.getField(BaseMetaTileEntity.class, "mColor");
						Field aActive = ReflectionUtils.getField(BaseMetaTileEntity.class, "mActive");
						Field aMetaTile = ReflectionUtils.getField(BaseMetaTileEntity.class, "mMetaTileEntity");
						Method aHasValidTile = ReflectionUtils.getMethod(BaseMetaTileEntity.class, "hasValidMetaTileEntity", new Class[] {});

						boolean hasValidTileObj = (boolean) aHasValidTile.invoke(aTile, new Object[] {});
						boolean aActiveObj = aActive.getBoolean(aTile);
						byte aFacingObj = aFacing.getByte(aTile);
						byte aColorObj = aColor.getByte(aTile);;
						MetaTileEntity aMetaTileObj = (MetaTileEntity) aMetaTile.get(aTile);

						if (rIcon != null) {
							return new ITexture[]{rIcon};
						} else {
							return hasValidTileObj
									? aMetaTileObj.getTexture(aTile, aSide, aFacingObj, (byte) (aColorObj - 1), aActiveObj,
											aTile.getOutputRedstoneSignal(aSide) > 0)
											: BlockIcons.ERROR_RENDERING;
						}
					}
					catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
		return BlockIcons.ERROR_RENDERING;
	}

}
