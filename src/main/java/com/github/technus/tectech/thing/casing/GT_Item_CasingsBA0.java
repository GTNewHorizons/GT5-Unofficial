package com.github.technus.tectech.thing.casing;

import static com.github.technus.tectech.util.CommonValues.COSMIC_MARK;
import static com.github.technus.tectech.util.CommonValues.THETA_MOVEMENT;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.TierEU;
import gregtech.common.blocks.GT_Item_Casings_Abstract;

public class GT_Item_CasingsBA0 extends GT_Item_Casings_Abstract {

    public GT_Item_CasingsBA0(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        if (aStack.getItemDamage() < 15) {
            aList.add(THETA_MOVEMENT);
        } else {
            aList.add(COSMIC_MARK);
        }
        switch (aStack.getItemDamage()) {
            case 0: // "Redstone Alloy Primary Tesla Windings"
            case 1: // "MV Superconductor Primary Tesla Windings"
            case 2: // "HV Superconductor Primary Tesla Windings"
            case 3: // "EV Superconductor Primary Tesla Windings"
            case 4: // "IV Superconductor Primary Tesla Windings"
            case 5: // "LuV Superconductor Primary Tesla Windings"
                aList.add(
                    translateToLocal("gt.blockcasingsBA0.0.desc.0") + " "
                        + formatNumbers(V[aStack.getItemDamage() + 1])
                        + " EU/t"); // Handles up to
                aList.add(AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockcasingsBA0.0.desc.1")); // What
                                                                                                                        // one
                                                                                                                        // man
                                                                                                                        // calls
                                                                                                                        // God,
                                                                                                                        // another
                                                                                                                        // calls
                                                                                                                        // the
                // laws of physics.
                break;
            case 6: // "Tesla Base Casing"
                aList.add(translateToLocal("gt.blockcasingsBA0.6.desc.0")); // The base of a wondrous contraption
                aList.add(AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockcasingsBA0.6.desc.1")); // it's
                                                                                                                        // alive,
                                                                                                                        // IT'S
                                                                                                                        // ALIVE!
                break;
            case 7: // "Tesla Toroid Casing"
                aList.add(translateToLocal("gt.blockcasingsBA0.7.desc.0")); // Made out of the finest tin foil!
                aList.add(AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockcasingsBA0.7.desc.1")); // Faraday
                                                                                                                        // suits
                                                                                                                        // might
                                                                                                                        // come
                                                                                                                        // later
                break;
            case 8: // "Tesla Secondary Windings"
                aList.add(translateToLocal("gt.blockcasingsBA0.8.desc.0")); // Picks up power from a primary coil
                aList.add(AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockcasingsBA0.8.desc.1")); // Who
                                                                                                                        // wouldn't
                                                                                                                        // want
                                                                                                                        // a
                                                                                                                        // 32k
                                                                                                                        // epoxy
                                                                                                                        // multi?
                break;
            case 9: // "ZPM Superconductor Primary Tesla Windings"
                aList.add(translateToLocal("gt.blockcasingsBA0.0.desc.0") + " " + formatNumbers(TierEU.ZPM) + " EU/t"); // Handles
                // up
                // to
                aList.add(AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockcasingsBA0.0.desc.1")); // What
                                                                                                                        // one
                                                                                                                        // man
                                                                                                                        // calls
                                                                                                                        // God,
                                                                                                                        // another
            case 10: // Reinforced Temporal Structure Casing
                aList.add(AQUA + translateToLocal("gt.blockcasingsBA0.10.desc.0"));
                aList.add(GRAY + translateToLocal("gt.blockcasingsBA0.10.desc.1"));
                // Designed to resist spatial shearing from internal volume expansion.
                // Can survive at least one big bang, maybe two...
                break;
            case 11: // Reinforced Temporal Structure Casing
                aList.add(AQUA + translateToLocal("gt.blockcasingsBA0.11.desc.0"));
                aList.add(GRAY + translateToLocal("gt.blockcasingsBA0.11.desc.1"));
                // Resistant to temporal shearing from time dilation differences.
                // This block can last an eternity without any decay.
                break;
            case 12: // Infinite Spacetime Energy Boundary Casing
                aList.add(AQUA + translateToLocal("gt.blockcasingsBA0.12.desc.0"));
                // Provides a stable bridge between spacetime regions.
                break;
            default:
                aList.add("Damn son where did you get that!?");
                aList.add(EnumChatFormatting.BLUE + "From outer space... I guess...");
        }
    }
}
