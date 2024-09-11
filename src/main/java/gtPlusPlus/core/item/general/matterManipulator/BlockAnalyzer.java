package gtPlusPlus.core.item.general.matterManipulator;

import java.util.ArrayList;

import javax.annotation.Nullable;

import appeng.api.implementations.tiles.IColorableTile;
import appeng.api.util.AEColor;
import appeng.tile.AEBaseTile;
import appeng.util.SettingsFrom;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.metatileentity.IConnectable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockAnalyzer {
    
    public static class BlockActionContext {
        public World world;
        public int x, y, z;
        public EntityPlayer player;
        public PendingBuild build;
        public ItemStack manipulator;

        public static final double EU_PER_ACTION = 8192;

        public TileEntity getTileEntity() {
            return world.getTileEntity(x, y, z);
        }

        public boolean tryConsumePower(double mult) {
            return build.tryConsumePower(manipulator, x, y, z, EU_PER_ACTION * mult);
        }

        public boolean tryConsumeItems(ItemStack... items) {
            return build.tryConsumeItems(items);
        }

        public void givePlayerItems(ItemStack... items) {
            build.givePlayerItems(items);
        }
    }

    static interface BlockAction {
        BlockActionResult apply(BlockActionContext context);
    }

    static enum BlockActionResult {
        NOT_APPLICABLE,
        COULD_NOT_APPLY,
        ALREADY_APPLIED,
        APPLIED,
    }

    private BlockAnalyzer() { }

    public static BlockAction[] getActions(BlockActionContext context) {
        TileEntity te = context.getTileEntity();

        if(te == null) {
            return null;
        }

        ArrayList<BlockAction> actions = new ArrayList<>();

        if (te instanceof IGregTechTileEntity gte) {
            IMetaTileEntity imte = gte.getMetaTileEntity();

            if(imte instanceof IConnectable conn) {
                actions.add(ConnectionAction.fromTile(conn));
            }

        }
        
        if (te instanceof appeng.tile.AEBaseTile ae) {
            actions.add(CopyAETileAction.fromTile(ae));
        }

        if (te instanceof IColorableTile colorable) {
            actions.add(ApplyAEColourAction.fromTile(colorable));
        }

        actions.removeIf(a -> a == null);

        return actions.toArray(new BlockAction[actions.size()]);
    }

    public static class ConnectionAction implements BlockAction {
        
        public final byte mConnections;

        private ConnectionAction(byte connections) {
            this.mConnections = connections;
        }

        public static @Nullable ConnectionAction fromTile(IConnectable conn) {
            byte connections = 0;

            for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
                ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];

                if(conn.isConnectedAtSide(dir)) {
                    connections |= dir.flag;
                }
            }

            return connections == 0 ? null : new ConnectionAction(connections);
        }

        public BlockActionResult apply(BlockActionContext context) {
            var te = context.getTileEntity() instanceof IGregTechTileEntity igte && igte.getMetaTileEntity() instanceof IConnectable conn ? conn : null;

            if (te == null) {
                return BlockActionResult.NOT_APPLICABLE;
            }

            if (!context.tryConsumePower(1)) {
                return BlockActionResult.COULD_NOT_APPLY;
            }

            boolean didSomething = false;

            for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
                ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];

                if(te.isConnectedAtSide(dir) != ((mConnections & dir.flag) != 0)) {
                    didSomething = true;
                    if ((mConnections & dir.flag) != 0){
                        te.connect(dir);
                    } else {
                        te.disconnect(dir);
                    }
                }
            }

            return didSomething ? BlockActionResult.APPLIED : BlockActionResult.ALREADY_APPLIED;
        }
    }

    public static class CopyAETileAction implements BlockAction {

        public final boolean hasRotation;
        public final ForgeDirection forward;
        public final ForgeDirection up;
        public final NBTTagCompound data;
    
        private CopyAETileAction(boolean hasRotation, ForgeDirection forward, ForgeDirection up, NBTTagCompound data) {
            this.hasRotation = hasRotation;
            this.forward = forward;
            this.up = up;
            this.data = data;
        }

        public static @Nullable CopyAETileAction fromTile(AEBaseTile sourceTile) {
            boolean hasRotation = false;
            ForgeDirection forward = null, up = null;

            if(sourceTile.canBeRotated()) {
                hasRotation = true;
                forward = sourceTile.getForward();
                up = sourceTile.getUp();
            }

            var data = sourceTile.downloadSettings(SettingsFrom.MEMORY_CARD);

            return !hasRotation && data == null ? null : new CopyAETileAction(hasRotation, forward, up, data);
        }

        @Override
        public BlockActionResult apply(BlockActionContext context) {
            var te = context.getTileEntity() instanceof appeng.tile.AEBaseTile ae ? ae : null;

            if (te == null) {
                return BlockActionResult.NOT_APPLICABLE;
            }

            if (!context.tryConsumePower(((hasRotation && te.canBeRotated()) ? 1 : 0) + (data != null ? 0.5 : 0))) {
                return BlockActionResult.COULD_NOT_APPLY;
            }
            
            if (hasRotation && te.canBeRotated()) {
                te.setOrientation(forward, up);
            }

            if (data != null) {
                te.uploadSettings(SettingsFrom.MEMORY_CARD, data);
            }

            return BlockActionResult.APPLIED;
        }
    }

    public static class ApplyAEColourAction implements BlockAction {

        public final AEColor colour;

        private ApplyAEColourAction(AEColor colour) {
            this.colour = colour;
        }

        public static @Nullable ApplyAEColourAction fromTile(IColorableTile colorable) {
            return new ApplyAEColourAction(colorable.getColor());
        }

        @Override
        public BlockActionResult apply(BlockActionContext context) {
            var te = context.getTileEntity() instanceof IColorableTile colorable ? colorable : null;

            if (te == null) {
                return BlockActionResult.NOT_APPLICABLE;
            }

            if (!context.tryConsumePower(te.getColor() != colour ? 1 : 0)) {
                return BlockActionResult.COULD_NOT_APPLY;
            }
            
            if (te.getColor() != colour) {
                te.recolourBlock(ForgeDirection.UNKNOWN, colour, context.player);
                return BlockActionResult.APPLIED;
            } else {
                return BlockActionResult.ALREADY_APPLIED;
            }
        }
    }
}
