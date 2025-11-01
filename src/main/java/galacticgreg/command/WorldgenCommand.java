package galacticgreg.command;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import galacticgreg.WorldGeneratorSpace;
import galacticgreg.WorldGeneratorSpace.AsteroidGenerator;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.enums.DimensionDef;
import galacticgreg.dynconfig.DynamicDimensionConfig;
import galacticgreg.dynconfig.DynamicDimensionConfig.AsteroidConfig;
import gregtech.common.worldgen.HEEIslandScanner;

public class WorldgenCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "asteroid";
    }

    @Override
    public String getCommandUsage(ICommandSender pCommandSender) {
        return "asteroid";
    }

    @Override
    public List<String> getCommandAliases() {
        return ImmutableList.of();
    }

    @Override
    public void processCommand(ICommandSender pCommandSender, String[] pArgs) {
        if (pArgs.length < 1) return;

        World world = pCommandSender.getEntityWorld();
        int chunkX = pCommandSender.getPlayerCoordinates().posX >> 4;
        int chunkZ = pCommandSender.getPlayerCoordinates().posZ >> 4;

        ModDimensionDef dimensionDef = DimensionDef.getEffectiveDefForChunk(world, chunkX, chunkZ);
        AsteroidConfig asteroidConfig = DynamicDimensionConfig.getAsteroidConfig(dimensionDef);

        switch (pArgs[0]) {
            case "spawns" -> {
                HEEIslandScanner.clearCache();

                if (asteroidConfig == null) {
                    pCommandSender.addChatMessage(new ChatComponentText("This chunk cannot spawn asteroids"));
                    return;
                }

                boolean spawns = WorldGeneratorSpace.generatesAsteroid(
                    world.getSeed(),
                    chunkX,
                    chunkZ,
                    world.provider.dimensionId,
                    asteroidConfig.Probability);

                pCommandSender.addChatMessage(new ChatComponentText("spawns: " + spawns));
            }
            case "generate" -> {
                for (int offsetZ = -2; offsetZ <= 2; offsetZ++) {
                    for (int offsetX = -2; offsetX <= 2; offsetX++) {
                        AsteroidGenerator gen = AsteroidGenerator.forChunk(world, chunkX + offsetX, chunkZ + offsetZ);

                        if (gen == null) {
                            continue;
                        }

                        pCommandSender.addChatMessage(
                            new ChatComponentText(
                                "found asteroid seed at " + (chunkX + offsetX) + ", " + (chunkZ + offsetZ)));

                        gen.generateChunk(world, chunkX, chunkZ);
                    }
                }
            }
            default -> {}
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender pCommandSender) {
        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER
            && !FMLCommonHandler.instance()
                .getMinecraftServerInstance()
                .isDedicatedServer())
            return true;

        if (pCommandSender instanceof EntityPlayerMP tEP) {
            return MinecraftServer.getServer()
                .getConfigurationManager()
                .func_152596_g(tEP.getGameProfile());
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }
}
