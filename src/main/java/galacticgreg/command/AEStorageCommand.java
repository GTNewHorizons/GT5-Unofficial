package galacticgreg.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import appeng.api.util.WorldCoord;
import appeng.items.storage.ItemSpatialStorageCell;
import galacticgreg.GalacticGreg;
import galacticgreg.auxiliary.PlayerChatHelper;
import galacticgreg.schematics.SpaceSchematic;
import galacticgreg.schematics.SpaceSchematicFactory;

/**
 * This command allows to export any structure that has been stored inside a spatial storage cell to a xml file that can
 * later be enabled for spawning in dimensions.
 */
public class AEStorageCommand implements ICommand {

    private List<String> aliases;

    public AEStorageCommand() {
        this.aliases = new ArrayList<>();
        this.aliases.add("exportae");
    }

    @Override
    public String getCommandName() {
        return "exportae";
    }

    @Override
    public String getCommandUsage(ICommandSender pCommandSender) {
        return "exportae <structure name>";
    }

    @Override
    public List<String> getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender pCommandSender, String[] pArgs) {
        try {
            if (pCommandSender instanceof EntityPlayer) {
                if (pArgs.length < 1) return;

                String tName = pArgs[0];

                EntityPlayer tEP = (EntityPlayer) pCommandSender;
                // Check if item in hand is a spatial storage cell
                ItemStack tIS = tEP.inventory.getCurrentItem();
                if (tIS.getItem() instanceof ItemSpatialStorageCell) {
                    ItemSpatialStorageCell tCell = (ItemSpatialStorageCell) tIS.getItem();
                    World tSpatialWorld = tCell.getWorld(tIS);
                    WorldCoord storedSize = tCell.getStoredSize(tIS);

                    // Check if SSC is filled
                    if (storedSize.x == 0 || storedSize.y == 0 || storedSize.z == 0) {
                        PlayerChatHelper.SendError(pCommandSender, "Error: This spatial storage is empty");
                        return;
                    }

                    // Export structure
                    GalacticGreg.Logger.info(
                        "Creating Structure from Spatial AE drive. Dimensions: X [%d] Y [%d] Z [%d]",
                        storedSize.x,
                        storedSize.y,
                        storedSize.z);
                    SpaceSchematic tSchematic = SpaceSchematicFactory.createSchematic(tName);
                    boolean tTEWarningSend = false;

                    // Loop all 3 dimensions
                    for (int lX = 1; lX <= storedSize.x; lX++) {
                        for (int lY = 65; lY < 65 + storedSize.y; lY++) {
                            for (int lZ = 1; lZ <= storedSize.z; lZ++) {

                                // Get the block
                                Block b = tSpatialWorld.getBlock(lX, lY, lZ);
                                // Get the meta
                                int bm = tSpatialWorld.getBlockMetadata(lX, lY, lZ);

                                // Search for the blocks name
                                String tBlockName = Block.blockRegistry.getNameForObject(b);

                                // Check if block is a tileentity
                                TileEntity bTE = tSpatialWorld.getTileEntity(lX, lY, lZ);

                                String tMsg = String.format("[X-%d][Y-%d][Z-%d] ", lX, lY, lZ);
                                String nbtComp = "";
                                // If block could be found...
                                if (b != null) {
                                    tMsg += tBlockName;
                                    // If block is a TileEntity
                                    if (bTE != null) {
                                        // Print a warning on the console
                                        tMsg += " TE";
                                        GalacticGreg.Logger.warn(
                                            "Warning: Found TileEntity at X[%d] Y[%d] Z[%d]. NBT States are not exported!",
                                            lX,
                                            lY,
                                            lZ);
                                        if (!tTEWarningSend) {
                                            // Send a warning ingame, once per export command
                                            tTEWarningSend = true;
                                            PlayerChatHelper
                                                .SendWarn(pCommandSender, "TileEntity states are not exported!");
                                        }

                                    }

                                    // If the block is not air, add it to the structure
                                    if (b != Blocks.air) tSchematic
                                        .addStructureInfo(SpaceSchematicFactory.createStructureInfo(lX, lY, lZ, b, bm));
                                }
                            }
                        }
                    }

                    // Save structure to disk
                    if (!GalacticGreg.SchematicHandler.SaveSpaceStructure(tSchematic)) {
                        // Something went wrong...
                        PlayerChatHelper.SendError(pCommandSender, "Something went wrong. Structure not saved");
                    } else {
                        // All good, xml exported. Notify player that he needs to edit the file
                        PlayerChatHelper.SendInfo(
                            pCommandSender,
                            "Structure has been exported to " + tSchematic.getName()
                                + ".xml. It contains "
                                + tSchematic.coordInfo()
                                    .size()
                                + " Blocks");
                        PlayerChatHelper
                            .SendInfo(pCommandSender, "You have to edit the file before a reload will accept it!");
                    }
                } else PlayerChatHelper
                    .SendError(pCommandSender, "Error: Item in your hand is not a spatial storage drive!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender pCommandSender) {
        // Command is only enabled for actual players and only if they are OP-ed
        if (pCommandSender instanceof EntityPlayerMP) {
            EntityPlayerMP tEP = (EntityPlayerMP) pCommandSender;
            return MinecraftServer.getServer()
                .getConfigurationManager()
                .func_152596_g(tEP.getGameProfile());
        } else return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }
}
