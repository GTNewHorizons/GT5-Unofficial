package gregtech.api.gui.ModularUI;

import com.gtnewhorizons.modularui.ModularUI;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.common.builder.UIBuilder;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehaviorBase;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_UIInfo {

    // in case we want to change something in the future
    public static final UIInfo<?, ?> GTTileEntityUI = UIInfos.TILE_MODULAR_UI;

    public static final Map<ForgeDirection, UIInfo<?, ?>> CoverUI = new HashMap<>();

    static {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            CoverUI.put(
                    direction,
                    UIBuilder.of()
                            .container((player, world, x, y, z) -> {
                                TileEntity te = world.getTileEntity(x, y, z);
                                if (!(te instanceof ICoverable)) return null;
                                ICoverable gtTileEntity = (ICoverable) te;
                                GT_CoverBehaviorBase<?> cover =
                                        gtTileEntity.getCoverBehaviorAtSideNew((byte) direction.ordinal());
                                return ModularUI.createContainer(player, cover::createWindow, te::markDirty);
                            })
                            .gui((player, world, x, y, z) -> {
                                if (!world.isRemote) return null;
                                TileEntity te = world.getTileEntity(x, y, z);
                                if (!(te instanceof ICoverable)) return null;
                                ICoverable gtTileEntity = (ICoverable) te;
                                GT_CoverBehaviorBase<?> cover =
                                        gtTileEntity.getCoverBehaviorAtSideNew((byte) direction.ordinal());
                                return ModularUI.createGuiScreen(player, cover::createWindow);
                            })
                            .build());
        }
    }
}
