package gregtech.api.gui.modularui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.manager.GuiInfos;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.net.GT_Packet_SendCoverData;

public class GT_UIInfos {

    public static void init() {}

    // private static final Map<ForgeDirection, UIInfo<?, ?>> coverUI = new HashMap<>();

    // static {
    // for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
    // coverUI.put(
    // side,
    // UIBuilder.of()
    // .container((player, world, x, y, z) -> {
    // final TileEntity te = world.getTileEntity(x, y, z);
    // if (!(te instanceof ICoverable gtTileEntity)) return null;
    // final GT_CoverBehaviorBase<?> cover = gtTileEntity.getCoverBehaviorAtSideNew(side);
    // return createCoverContainer(
    // player,
    // cover::createWindow,
    // te::markDirty,
    // gtTileEntity.getCoverIDAtSide(side),
    // side,
    // gtTileEntity);
    // })
    // .gui((player, world, x, y, z) -> {
    // if (!world.isRemote) return null;
    // final TileEntity te = world.getTileEntity(x, y, z);
    // if (!(te instanceof ICoverable gtTileEntity)) return null;
    // final GT_CoverBehaviorBase<?> cover = gtTileEntity.getCoverBehaviorAtSideNew(side);
    // return createCoverGuiContainer(
    // player,
    // cover::createWindow,
    // gtTileEntity.getCoverIDAtSide(side),
    // side,
    // gtTileEntity);
    // })
    // .build());
    // }
    // }

    /**
     * Opens machine GUI.
     */
    public static <T extends IHasWorldObjectAndCoords & IGuiHolder> void openGTTileEntityUI(T tileEntity,
        EntityPlayer player) {
        if (tileEntity.isClientSide()) return;

        GuiInfos.TILE_ENTITY
            .open(player, player.worldObj, tileEntity.getXCoord(), tileEntity.getYCoord(), tileEntity.getZCoord());
    }

    // /**
    // * Opens cover UI, created by {@link GT_CoverBehaviorBase#createWindow}.
    // */
    public static void openCoverUI(ICoverable tileEntity, EntityPlayer player, ForgeDirection side) {
        if (tileEntity.isClientSide()) return;

        GT_Values.NW.sendToPlayer(
            new GT_Packet_SendCoverData(
                side,
                tileEntity.getCoverIDAtSide(side),
                tileEntity.getComplexCoverDataAtSide(side),
                tileEntity),
            (EntityPlayerMP) player);

        // coverUI.get(side)
        // .open(
        // player,
        // tileEntity.getWorld(),
        // tileEntity.getXCoord(),
        // tileEntity.getYCoord(),
        // tileEntity.getZCoord());
    }

    // /**
    // * Opens UI for player's item, created by
    // * {@link com.gtnewhorizons.modularui.api.screen.IItemWithModularUI#createWindow}.
    // */
    public static void openPlayerHeldItemUI(EntityPlayer player) {
        // if (NetworkUtils.isClient()) return;
        // UIInfos.PLAYER_HELD_ITEM_UI.open(player);
    }

    // private static ModularUIContainer createCoverContainer(EntityPlayer player,
    // Function<GT_CoverUIBuildContext, ModularWindow> windowCreator, Runnable onWidgetUpdate, int coverID,
    // ForgeDirection side, ICoverable tile) {
    // final GT_CoverUIBuildContext buildContext = new GT_CoverUIBuildContext(player, coverID, side, tile, false);
    // final ModularWindow window = windowCreator.apply(buildContext);
    // if (window == null) return null;
    // return new ModularUIContainer(new ModularUIContext(buildContext, onWidgetUpdate), window);
    // }

    // @SideOnly(Side.CLIENT)
    // private static ModularGui createCoverGuiContainer(EntityPlayer player,
    // Function<GT_CoverUIBuildContext, ModularWindow> windowCreator, int coverID, ForgeDirection side,
    // ICoverable tile) {
    // final ModularUIContainer container = createCoverContainer(player, windowCreator, null, coverID, side, tile);
    // if (container == null) {
    // return null;
    // }
    // return new ModularGui(container);
    // }
}
