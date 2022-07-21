package gregtech.api.interfaces;

import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

// If you are adding very late-game content feel free to tap into this interface.
// The eventual goal is to bypass laser/dynamo stuff and have energy deposited directly from ultra-endgame
// multi-blocks directly into the users network.
public interface IGlobalWirelessEnergy {
    HashMap<String, BigInteger> GlobalEnergyMap = new HashMap<>(100, 0.9f);
    String GlobalEnergyMapFileName = "GlobalEnergyMap";

    default void loadGlobalEnergyMap(World world) {
        try {
            Path path = Paths.get("./saves/" + world.getWorldInfo().getWorldName() + "/" + GlobalEnergyMapFileName + ".txt").toAbsolutePath();

            String[] data;
            for(String line : Files.readAllLines(path)) {
                data = line.split(":");

                String UUID = data[0];
                BigInteger num = new BigInteger(data[1]);

                GlobalEnergyMap.put(UUID, num);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) { }
    };

}


