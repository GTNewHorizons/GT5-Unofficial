/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.ASM;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.POP;

import java.util.Arrays;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BWCoreTransformer implements IClassTransformer {

    public static final String[] DESCRIPTIONFORCONFIG = { "REMOVING RAIN FROM LAST MILLENNIUM (EXU)",
            "REMOVING CREATURES FROM LAST MILLENNIUM (EXU)",
            "PATCHING THAUMCRAFT WAND PEDESTAL TO PREVENT VIS DUPLICATION",
            "PATCHING CRAFTING MANAGER FOR CACHING RECIPES" };
    public static final String[] CLASSESBEINGTRANSFORMED = {
            "com.rwtema.extrautils.worldgen.endoftime.WorldProviderEndOfTime",
            "com.rwtema.extrautils.worldgen.endoftime.ChunkProviderEndOfTime",
            "thaumcraft.common.tiles.TileWandPedestal", "net.minecraft.item.crafting.CraftingManager" };
    static boolean obfs;

    public static boolean[] shouldTransform = new boolean[BWCoreTransformer.CLASSESBEINGTRANSFORMED.length];

    /**
     * Made by DarkShaddow44
     */
    private static MethodNode transformThaumcraftWandPedestal(MethodNode method) {
        InsnList nu = new InsnList();
        for (int j = 0; j < method.instructions.size(); j++) {
            AbstractInsnNode instruction = method.instructions.get(j);
            nu.add(instruction);
            if (instruction.getOpcode() == INVOKEVIRTUAL) {
                MethodInsnNode invokevirtual = (MethodInsnNode) instruction;
                if ("addVis".equals(invokevirtual.name)) {
                    AbstractInsnNode beginning = method.instructions.get(j - 7);
                    LabelNode label = new LabelNode();
                    nu.insertBefore(beginning, new VarInsnNode(ALOAD, 0));
                    nu.insertBefore(
                            beginning,
                            new FieldInsnNode(
                                    GETFIELD,
                                    "thaumcraft/common/tiles/TileWandPedestal",
                                    obfs ? "field_145850_b" : "worldObj",
                                    "Lnet/minecraft/world/World;"));
                    nu.insertBefore(
                            beginning,
                            new FieldInsnNode(
                                    GETFIELD,
                                    "net/minecraft/world/World",
                                    obfs ? "field_72995_K" : "isRemote",
                                    "Z"));
                    nu.insertBefore(beginning, new JumpInsnNode(IFNE, label));
                    nu.add(new InsnNode(POP));
                    nu.add(label);
                    j++; // Skip actual Pop
                }
            }
        }
        method.instructions = nu;
        return method;
    }

    public static byte[] transform(int id, byte[] basicClass) {
        if (!BWCoreTransformer.shouldTransform[id]) {
            BWCore.BWCORE_LOG
                    .info("Patch: " + BWCoreTransformer.DESCRIPTIONFORCONFIG[id] + " is disabled, will not patch!");
            return basicClass;
        }

        if (id < BWCoreTransformer.CLASSESBEINGTRANSFORMED.length) {
            BWCore.BWCORE_LOG.info(BWCoreTransformer.DESCRIPTIONFORCONFIG[id]);
            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.SKIP_FRAMES);
            List<MethodNode> methods = classNode.methods;
            scase: switch (id) {
                case 0: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEINGTRANSFORMED[id]);
                    String name_deObfs = "canDoRainSnowIce";

                    String dsc_deObfs = "(Lnet/minecraft/world/chunk/Chunk;)Z";
                    for (int i = 0; i < methods.size(); i++) {
                        if (methods.get(i).name.equalsIgnoreCase(name_deObfs)) {
                            BWCore.BWCORE_LOG.info("Found " + name_deObfs + "! Removing!");
                            methods.remove(i);
                            break;
                        }
                    }
                    BWCore.BWCORE_LOG.info("Creating new " + name_deObfs + "!");
                    MethodNode nu = new MethodNode(ACC_PUBLIC, name_deObfs, dsc_deObfs, null, new String[0]);
                    InsnList insnList = new InsnList();
                    insnList.add(new InsnNode(ICONST_0));
                    insnList.add(new InsnNode(IRETURN));
                    nu.instructions = insnList;
                    nu.maxLocals = 1;
                    nu.maxStack = 1;
                    methods.add(nu);
                    break;
                }
                case 1: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEINGTRANSFORMED[id]);
                    String name_deObfs = "getPossibleCreatures";
                    String name_src = "func_73155_a";
                    String name_Obfs = "a";
                    String dsc_deObfs = "(Lnet/minecraft/entity/EnumCreatureType;III)Ljava/util/List;";
                    String dsc_Obfs = "(Lsx;III)Ljava/util/List;";

                    for (int i = 0; i < methods.size(); i++) {
                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs, name_Obfs, name_src)
                                && ASMUtils.isCorrectMethod(methods.get(i), dsc_deObfs, dsc_Obfs)) {
                            BWCore.BWCORE_LOG.info("Found " + name_deObfs + "! Patching!");
                            MethodNode toPatch = methods.get(i);
                            InsnList insnList = new InsnList();
                            insnList.add(new InsnNode(ACONST_NULL));
                            insnList.add(new InsnNode(ARETURN));
                            toPatch.instructions = insnList;
                            toPatch.maxStack = 1;
                            toPatch.maxLocals = 5;
                            methods.set(i, toPatch);
                            break scase;
                        }
                    }
                }
                case 2: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEINGTRANSFORMED[id]);
                    String name_deObfs = "updateEntity";
                    String name_src = "func_145845_h";
                    String name_Obfs = "h";
                    String dsc_universal = "()V";

                    for (int i = 0; i < methods.size(); i++) {
                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs, name_Obfs, name_src)
                                && ASMUtils.isCorrectMethod(methods.get(i), dsc_universal, dsc_universal)) {
                            BWCore.BWCORE_LOG.info("Found " + name_deObfs + "! Patching!");
                            methods.set(i, BWCoreTransformer.transformThaumcraftWandPedestal(methods.get(i)));
                            break scase;
                        }
                    }
                }
                case 3: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEINGTRANSFORMED[id]);
                    String name_deObfs = "findMatchingRecipe";
                    String name_Obfs = "a";
                    String name_src = "func_82787_a";
                    for (MethodNode toPatch : methods) {
                        if (ASMUtils.isCorrectMethod(toPatch, name_deObfs, name_Obfs, name_src)) {
                            toPatch.instructions = new InsnList();
                            toPatch.instructions.add(new VarInsnNode(ALOAD, 1));
                            toPatch.instructions.add(new VarInsnNode(ALOAD, 2));
                            toPatch.instructions.add(
                                    new MethodInsnNode(
                                            INVOKESTATIC,
                                            "com/github/bartimaeusnek/bartworks/ASM/BWCoreStaticReplacementMethodes",
                                            "findCachedMatchingRecipe",
                                            "(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Lnet/minecraft/item/ItemStack;",
                                            false));
                            toPatch.instructions.add(new InsnNode(ARETURN));
                            toPatch.localVariables.clear();
                            toPatch.maxStack = 2;
                            toPatch.maxLocals = 3;
                            break scase;
                        }
                    }
                }

                default: {
                    BWCore.BWCORE_LOG.info("Could not find: " + BWCoreTransformer.CLASSESBEINGTRANSFORMED[id]);
                    return basicClass;
                }
            }

            classNode.methods = methods;
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            byte[] ret = classWriter.toByteArray();
            if (Arrays.hashCode(basicClass) == Arrays.hashCode(ret))
                BWCore.BWCORE_LOG.warn("Could not patch: " + BWCoreTransformer.CLASSESBEINGTRANSFORMED[id]);
            return ret;
        }
        return basicClass;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (int i = 0; i < BWCoreTransformer.CLASSESBEINGTRANSFORMED.length; i++) {
            if (name.equalsIgnoreCase(BWCoreTransformer.CLASSESBEINGTRANSFORMED[i])
                    || transformedName.equalsIgnoreCase(BWCoreTransformer.CLASSESBEINGTRANSFORMED[i]))
                return BWCoreTransformer.transform(i, basicClass);
        }
        return basicClass;
    }
}
