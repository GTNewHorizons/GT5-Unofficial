package gregtech.api.gui.ModularUI;

import com.gtnewhorizons.modularui.ModularUI;
import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.builder.UIBuilder;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CoverBehaviorBase;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_UIInfo {

    private static final Function<BiFunction<ModularUIContext, ModularWindow, ModularUIContainer>, UIInfo<?, ?>>
            GTTileEntityUI = containerCreator -> UIBuilder.of()
            .gui(((player, world, x, y, z) -> {
                if (!world.isRemote) return null;
                TileEntity te = world.getTileEntity(x, y, z);
                if (te instanceof ITileWithModularUI) {
                    return createGuiScreen(player, ((ITileWithModularUI) te)::createWindow, containerCreator);
                }
                return null;
            }))
            .container((player, world, x, y, z) -> {
                TileEntity te = world.getTileEntity(x, y, z);
                if (te instanceof ITileWithModularUI) {
                    return createContainer(
                            player, ((ITileWithModularUI) te)::createWindow, te::markDirty, containerCreator);
                }
                return null;
            })
            .build();

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

    public static void openGTTileEntityUI(
            IGregTechTileEntity aBaseMetaTileEntity,
            EntityPlayer aPlayer,
            BiFunction<ModularUIContext, ModularWindow, ModularUIContainer> containerCreator) {
        if (aBaseMetaTileEntity.isClientSide()) return;
        GTTileEntityUI.apply(containerCreator)
                .open(
                        aPlayer,
                        aBaseMetaTileEntity.getWorld(),
                        aBaseMetaTileEntity.getXCoord(),
                        aBaseMetaTileEntity.getYCoord(),
                        aBaseMetaTileEntity.getZCoord());
    }

    public static void openGTTileEntityUI(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGTTileEntityUI(aBaseMetaTileEntity, aPlayer, ModularUIContainer::new);
    }

    private static ModularUIContainer createContainer(
            EntityPlayer player,
            Function<UIBuildContext, ModularWindow> windowCreator,
            Runnable onWidgetUpdate,
            BiFunction<ModularUIContext, ModularWindow, ModularUIContainer> containerCreator) {
        UIBuildContext buildContext = new UIBuildContext(player);
        ModularWindow window = windowCreator.apply(buildContext);
        return containerCreator.apply(new ModularUIContext(buildContext, onWidgetUpdate), window);
    }

    @SideOnly(Side.CLIENT)
    public static ModularGui createGuiScreen(
            EntityPlayer player,
            Function<UIBuildContext, ModularWindow> windowCreator,
            BiFunction<ModularUIContext, ModularWindow, ModularUIContainer> containerCreator) {
        return new ModularGui(createContainer(player, windowCreator, null, containerCreator));
    }
}
