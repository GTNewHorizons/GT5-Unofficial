/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.commands;

import static forestry.api.apiculture.BeeManager.beeRoot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.io.Files;

import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import kubatech.api.utils.ModUtils;

@CommandHandler.ChildCommand
public class CommandBees extends CommandBase {

    @Override
    public String getCommandName() {
        return "bees";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "bees";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {

        if (!ModUtils.isClientSided) {
            p_71515_1_
                .addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "This command is single-player only!"));
            return;
        }

        // https://docs.google.com/spreadsheets/d/1XaNGtJZ8WYv2nMnYcixTX4Jz3qUr71RadiKT5pToYFk/edit?usp=sharing
        try {
            File f = new File("bees.txt");
            BufferedWriter writer = Files.newWriter(f, StandardCharsets.UTF_8);
            String delimer = ",";

            writer.write(
                "Bee,CHANCE,OLD_0.6S_0UP,OLD_0.6S_8UP,OLD_1.7S_0UP,OLD_1.7S_8UP,NEW_0.6S_0UP_1T,NEW_0.6S_8UP_1T,NEW_1.7S_0UP_1T,NEW_1.7S_8UP_1T,NEW_1.7S_0UP_8T,NEW_1.7S_8UP_8T\n");

            List<IBee> bees = beeRoot.getIndividualTemplates();
            for (IBee bee : bees) {
                // System.out.println("Bee: " + bee.getDisplayName());
                StringBuilder b = new StringBuilder(bee.getDisplayName());
                b.append(",-,-,-,-,-,-,-,-,-,-\n");
                IBeeGenome genome = bee.getGenome();
                IAlleleBeeSpecies primary = genome.getPrimary();
                IAlleleBeeSpecies secondary = genome.getSecondary();
                primary.getProductChances()
                    .forEach((k, v) -> printData("[PRIMARY]", k, v, delimer, b));
                secondary.getProductChances()
                    .forEach((k, v) -> printData("[SECONDARY]", k, v / 2f, delimer, b));
                primary.getSpecialtyChances()
                    .forEach((k, v) -> printData("[SPECIALITY]", k, v, delimer, b));
                writer.write(b.toString());
            }

            writer.flush();
            writer.close();
            p_71515_1_.addChatMessage(new ChatComponentText(f.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printData(String nameOfData, ItemStack k, float v, String delimer, StringBuilder b) {
        b.append(nameOfData);
        b.append(k.getDisplayName());
        b.append(delimer);
        b.append(format(v));
        b.append(delimer);
        b.append(format(productChanceOld(0, 0.6d, v)));
        b.append(delimer);
        b.append(format(productChanceOld(8, 0.6d, v)));
        b.append(delimer);
        b.append(format(productChanceOld(0, 1.7d, v)));
        b.append(delimer);
        b.append(format(productChanceOld(8, 1.7d, v)));
        b.append(delimer);
        b.append(format(productChanceNew(0, 0.6d, v, 1)));
        b.append(delimer);
        b.append(format(productChanceNew(8, 0.6d, v, 1)));
        b.append(delimer);
        b.append(format(productChanceNew(0, 1.7d, v, 1)));
        b.append(delimer);
        b.append(format(productChanceNew(8, 1.7d, v, 1)));
        b.append(delimer);
        b.append(format(productChanceNew(0, 1.7d, v, 8)));
        b.append(delimer);
        b.append(format(productChanceNew(8, 1.7d, v, 8)));
        b.append("\n");
    }

    private String format(double chance) {
        return String.format("%.2f%%", chance * 100d);
    }

    private double productChanceNew(int upgradeCount, double beeSpeed, double chance, int t) {
        chance *= 100f;
        float productionModifier = (float) upgradeCount * 0.25f;
        return (float) (((1f + t / 6f) * Math.sqrt(chance) * 2f * (1f + beeSpeed)
            + Math.pow(productionModifier, Math.cbrt(chance))
            - 3f) / 100f);
    }

    private double productChanceOld(int upgradeCount, double beeSpeed, double chance) {
        return chance * beeSpeed * Math.pow(1.2d, upgradeCount);
    }
}
