package com.detrav.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import scala.Int;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wital_000 on 25.03.2016.
 */
public class DetravBiomeScannerCommand implements ICommand {
    private List aliases;

    public DetravBiomeScannerCommand()
    {
        this.aliases = new ArrayList<String>();
        this.aliases.add("DetravBiomeScanner");
        this.aliases.add("dbscan");
    }

    @Override
    public String getCommandName() {
        return "DetravBiomeScanner";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "DetravBiomeScanner radius [name]";
    }

    @Override
    public List getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            if(args.length>0) {
                int aXaZ = Integer.parseInt(args[0]);
                String name = null;
                if(args.length>1)
                name = args[1].toLowerCase();
                else
                name = "";
                int step = aXaZ * 2 / 512;
                if (step < 1) step = 1;
                World w = sender.getEntityWorld();
                ChunkCoordinates cor = sender.getPlayerCoordinates();
                BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
                WritableRaster r = image.getRaster();
                HashMap<String, Integer> colors = new HashMap<String, Integer>();
                for (int i = cor.posX - aXaZ, ii = 0; ii < 512; i += step, ii++)
                    for (int j = cor.posZ - aXaZ, jj = 0; jj < 512; j += step, jj++) {
                        BiomeGenBase biome = w.getBiomeGenForCoords(i, j);
                        if(name.length()==0 || biome.biomeName.toLowerCase().contains(name)) {
                            r.setSample(ii, jj, 0, (biome.color  >> 16) & 0xFF );
                            r.setSample(ii, jj, 1, (biome.color >> 8) & 0xFF);
                            r.setSample(ii, jj, 2, biome.color & 0xFF);
                            r.setSample(ii, jj, 3, 0xFF);
                            if (!colors.containsKey(biome.biomeName))
                                colors.put(biome.biomeName, biome.color);
                        }
                    }
                File outfile = new File("biomes.png");
                ImageIO.write(image, "png", outfile);
                File outfileTxt = new File("biomes.txt");
                PrintWriter fw = new PrintWriter(outfileTxt);
                for (String key : colors.keySet()) {
                    fw.println(String.format("Biome: %s , Color: %d", key, colors.get(key)));
                }
                fw.println(String.format("From (%d,%d) to (%d,%d)", cor.posX - aXaZ, cor.posZ - aXaZ, cor.posX - aXaZ + step * 512, cor.posZ - aXaZ + step * 512));
                fw.flush();
                fw.close();
                return;
            }
        }
        catch (Exception e)
        {
            sender.addChatMessage(new ChatComponentText("catch ERROR"));
            return;
        }
        sender.addChatMessage(new ChatComponentText("ERROR"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
