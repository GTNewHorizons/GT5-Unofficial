package gtPlusPlus.xmod.bop;

import static gregtech.api.enums.Mods.BiomesOPlenty;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;

public class HANDLER_BiomesOPlenty {

    public static Item mPineCone;

    public static Block logs1;
    public static Block logs2;
    public static Block logs3;
    public static Block logs4;

    public static Block leaves1;
    public static Block leaves2;
    public static Block leaves3;
    public static Block leaves4;

    public static Block colorizedLeaves1;
    public static Block colorizedLeaves2;

    public static Block saplings;
    public static Block colorizedSaplings;

    public static void preInit() {
        BOP_Block_Registrator.run();
        if (BiomesOPlenty.isModLoaded()) {
            setFields();
            registerPineconeToOreDict();
        }
    }

    public static void postInit() {
        BOP_Block_Registrator.recipes();
    }

    private static void registerPineconeToOreDict() {
        if (mPineCone != null) {
            ItemStack aPinecone = ItemUtils.simpleMetaStack(mPineCone, 13, 1);
            if (aPinecone != null) {
                ItemUtils.addItemToOreDictionary(aPinecone, "pinecone");
            }
        }
    }

    public static ItemStack getStack(Block aBlock, int aMeta, int aSize) {
        return ItemUtils.simpleMetaStack(aBlock, aMeta, aSize);
    }

    // BOPCBlocks.logs4 - 0
    // BOPCBlocks.colorizedLeaves2 - 1

    private static void setFields() {
        Field aBopMiscItem = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCItems"), "misc");

        Field aBopBlock1 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "logs1");
        Field aBopBlock2 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "logs2");
        Field aBopBlock3 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "logs3");
        Field aBopBlock4 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "logs4");

        Field aBopLeaves1 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "leaves1");
        Field aBopLeaves2 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "leaves2");
        Field aBopLeaves3 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "leaves3");
        Field aBopLeaves4 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "leaves4");

        Field aBopColouredLeaves1 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "colorizedLeaves1");
        Field aBopColouredLeaves2 = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "colorizedLeaves2");

        Field aBopSapling = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "saplings");
        Field aBopColouredSapling = ReflectionUtils
                .getField(ReflectionUtils.getClass("biomesoplenty.api.content.BOPCBlocks"), "colorizedSaplings");

        if (aBopMiscItem != null) {
            Item aMiscItem = ReflectionUtils.getFieldValue(aBopMiscItem);
            if (aMiscItem != null) {
                mPineCone = aMiscItem;
            }
        }

        if (aBopBlock1 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopBlock1);
            if (aBlock != null) {
                logs1 = aBlock;
            }
        }
        if (aBopBlock2 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopBlock2);
            if (aBlock != null) {
                logs2 = aBlock;
            }
        }
        if (aBopBlock3 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopBlock3);
            if (aBlock != null) {
                logs3 = aBlock;
            }
        }
        if (aBopBlock4 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopBlock4);
            if (aBlock != null) {
                logs4 = aBlock;
            }
        }

        if (aBopLeaves1 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopLeaves1);
            if (aBlock != null) {
                leaves1 = aBlock;
            }
        }
        if (aBopLeaves2 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopLeaves2);
            if (aBlock != null) {
                leaves2 = aBlock;
            }
        }
        if (aBopLeaves3 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopLeaves3);
            if (aBlock != null) {
                leaves3 = aBlock;
            }
        }
        if (aBopLeaves4 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopLeaves4);
            if (aBlock != null) {
                leaves4 = aBlock;
            }
        }

        if (aBopColouredLeaves1 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopColouredLeaves1);
            if (aBlock != null) {
                colorizedLeaves1 = aBlock;
            }
        }
        if (aBopColouredLeaves2 != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopColouredLeaves2);
            if (aBlock != null) {
                colorizedLeaves2 = aBlock;
            }
        }

        if (aBopSapling != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopSapling);
            if (aBlock != null) {
                saplings = aBlock;
            }
        }
        if (aBopColouredSapling != null) {
            Block aBlock = ReflectionUtils.getFieldValue(aBopColouredSapling);
            if (aBlock != null) {
                colorizedSaplings = aBlock;
            }
        }
    }
}
