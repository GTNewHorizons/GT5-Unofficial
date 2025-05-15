package tectech.thing.casing;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import gregtech.common.blocks.ItemCasings;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class ItemCasingsTT extends ItemCasings {

    public ItemCasingsTT(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> tooltip, boolean aF3_H) {
        if (aStack.getItemDamage() < 15) {
            tooltip.add(CommonValues.TEC_MARK_EM);
        } else {
            tooltip.add(CommonValues.COSMIC_MARK);
        }
        switch (aStack.getItemDamage()) {
            case 0: // "High Power Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.0.desc.0")); // Well suited for high power applications.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.0.desc.1")); // The power levels are rising!
                break;
            case 1: // "Computer Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.1.desc.0")); // Nice and clean casing.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.1.desc.1")); // Dust can break it!?
                break;
            case 2: // "Computer Heat Vent"
                tooltip.add(translateToLocal("gt.blockcasingsTT.2.desc.0")); // Air vent with a filter.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.2.desc.1")); // Perfectly muffled sound!
                break;
            case 3: // "Advanced Computer Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.3.desc.0")); // Contains high bandwidth bus
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.3.desc.1")); // couple thousand qubits wide.
                break;
            case 4: // "Molecular Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.4.desc.0")); // Stops elemental things.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.4.desc.1")); // Radiation and emotions too...
                break;
            case 5: // "Advanced Molecular Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.5.desc.0")); // Cooling and stabilization.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.5.desc.1")); // A comfortable machine bed.
                break;
            case 6: // "Containment Field Generator"
                tooltip.add(translateToLocal("gt.blockcasingsTT.6.desc.0")); // Creates a field that...
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.6.desc.1")); // can stop even force carriers.
                break;
            case 7: // "Molecular Coil"
                tooltip.add(translateToLocal("gt.blockcasingsTT.7.desc.0")); // Well it does things too...
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.7.desc.1")); // [Use this coil!]
                break;
            case 8: // "Collider Hollow Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.8.desc.0")); // Reinforced accelerator tunnel.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.8.desc.1")); // Most advanced pipe ever.
                break;
            case 9: // "Spacetime Altering Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.9.desc.0")); // c is no longer the limit.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.9.desc.1")); // Wibbly wobbly timey wimey stuff.
                break;
            case 10: // "Teleportation Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.10.desc.0")); // Remote connection.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.10.desc.1")); // Better touch with a stick.
                break;
            case 11: // "Dimensional Bridge Generator"
                tooltip.add(translateToLocal("gt.blockcasingsTT.11.desc.0")); // Interdimensional Operations.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.11.desc.1")); // Around the universe and other
                                                                            // places too.
                break;
            case 12: // "Ultimate Molecular Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.12.desc.0")); // Ultimate in every way.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.12.desc.1")); // I don't know what it can't do.
                break;
            case 13: // "Ultimate Advanced Molecular Casing"
                tooltip.add(translateToLocal("gt.blockcasingsTT.13.desc.0")); // More Ultimate in every way.
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.13.desc.1")); // I don't know what I am doing!
                break;
            case 14: // "Ultimate Containment Field Generator"
                tooltip.add(translateToLocal("gt.blockcasingsTT.14.desc.0")); // Black Hole...
                tooltip.add(
                    EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockcasingsTT.14.desc.1")); // Meh...
                break;
            case 15: // "Debug Sides"
                tooltip.add(translateToLocal("gt.blockcasingsTT.15.desc.0")); // Lazy man way of determining sides.
                tooltip.add(EnumChatFormatting.BLUE + translateToLocal("gt.blockcasingsTT.15.desc.1"));
                break;
            default: // WTF?
                tooltip.add(translateToLocal("gt.blockcasingsTT.default.desc.0"));
                tooltip
                    .add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("gt.blockcasingsTT.default.desc.1"));
        }
    }
}
