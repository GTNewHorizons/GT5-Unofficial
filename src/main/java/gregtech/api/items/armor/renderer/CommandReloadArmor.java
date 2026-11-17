package gregtech.api.items.armor.renderer;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandReloadArmor extends CommandBase {

    @Override
    public String getCommandName() {
        return "reloadarmor";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/reloadarmor - Reloads all GTNH 3D armor models and textures.";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        long startTime = System.currentTimeMillis();

        try {
            ArmorRegistry.clearCache();
            ArmorComponentRegistry.loadAllAssets();

            long duration = System.currentTimeMillis() - startTime;

            Minecraft.getMinecraft().thePlayer.addChatMessage(
                new ChatComponentText(
                    EnumChatFormatting.GREEN + "[GTNH-VoxelArmor] Successfully reloaded assets in "
                        + duration
                        + "ms!"));

        } catch (Exception e) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                new ChatComponentText(
                    EnumChatFormatting.RED + "[GTNH-VoxelArmor] Error during asset reload! Check the console/logs."));
            GT_FML_LOGGER.error("[VoxelArmor] Command execution failed!", e);
        }
    }
}
