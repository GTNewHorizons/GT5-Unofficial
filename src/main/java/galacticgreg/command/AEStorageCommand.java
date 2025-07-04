package galacticgreg.command;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import appeng.api.util.WorldCoord;
import appeng.items.storage.ItemSpatialStorageCell;
import galacticgreg.GalacticGreg;
import galacticgreg.auxiliary.PlayerChatHelper;
import galacticgreg.schematics.SpaceSchematic;
import galacticgreg.schematics.SpaceSchematicFactory;
import gregtech.commands.GTBaseCommand;

/**
 * This command allows to export any structure that has been stored inside a spatial storage cell to a xml file that can
 * later be enabled for spawning in dimensions.
 */
public class AEStorageCommand extends GTBaseCommand {

    public AEStorageCommand() {
        super("exportae");
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
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (sender instanceof EntityPlayer player) {
                if (args.length < 1) return;

                String tName = args[0];

                // Check if item in hand is a spatial storage cell
                ItemStack tIS = player.inventory.getCurrentItem();
                if (tIS.getItem() instanceof ItemSpatialStorageCell tCell) {
                    World tSpatialWorld = tCell.getWorld(tIS);
                    WorldCoord storedSize = tCell.getStoredSize(tIS);

                    // Check if SSC is filled
                    if (storedSize.x == 0 || storedSize.y == 0 || storedSize.z == 0) {
                        PlayerChatHelper.SendError(sender, "Error: This spatial storage is empty");
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
                                        GalacticGreg.Logger.warn(
                                            "Warning: Found TileEntity at X[%d] Y[%d] Z[%d]. NBT States are not exported!",
                                            lX,
                                            lY,
                                            lZ);
                                        if (!tTEWarningSend) {
                                            // Send a warning ingame, once per export command
                                            tTEWarningSend = true;
                                            PlayerChatHelper.SendWarn(sender, "TileEntity states are not exported!");
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
                        PlayerChatHelper.SendError(sender, "Something went wrong. Structure not saved");
                    } else {
                        // All good, xml exported. Notify player that he needs to edit the file
                        PlayerChatHelper.SendInfo(
                            sender,
                            "Structure has been exported to " + tSchematic.getName()
                                + ".xml. It contains "
                                + tSchematic.coordInfo()
                                    .size()
                                + " Blocks");
                        PlayerChatHelper.SendInfo(sender, "You have to edit the file before a reload will accept it!");
                    }
                } else PlayerChatHelper.SendError(sender, "Error: Item in your hand is not a spatial storage drive!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        // Command is only enabled for actual players and only if they are OP-ed
        return isOpedPlayer(sender);
    }
}
