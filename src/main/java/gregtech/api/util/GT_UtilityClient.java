package gregtech.api.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.ARBImaging.*;
import static org.lwjgl.opengl.ARBImaging.GL_POST_COLOR_MATRIX_COLOR_TABLE;
import static org.lwjgl.opengl.ARBImaging.GL_POST_CONVOLUTION_COLOR_TABLE;
import static org.lwjgl.opengl.ARBImaging.GL_SEPARABLE_2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.GL_FOG_COORD_ARRAY;
import static org.lwjgl.opengl.GL20.GL_POINT_SPRITE;
import static org.lwjgl.opengl.GL20.GL_VERTEX_PROGRAM_POINT_SIZE;
import static org.lwjgl.opengl.GL20.GL_VERTEX_PROGRAM_TWO_SIDE;

public class GT_UtilityClient {
	private static final Field isDrawingField = ReflectionHelper.findField(Tessellator.class, "isDrawing", "field_78415_z");

	public static boolean isDrawing(Tessellator tess) {
		try {
			return isDrawingField.getBoolean(tess);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}


	@SuppressWarnings("unchecked")
	public static List<String> getTooltip(ItemStack aStack, boolean aGuiStyle) {
		try {
			List<String> tooltip = aStack.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
			if (aGuiStyle) {
				tooltip.set(0, (aStack.getRarity() == null ? EnumRarity.common : aStack.getRarity()).rarityColor +tooltip.get(0));
				for (int i = 1; i < tooltip.size(); i++) {
					tooltip.set(i, EnumChatFormatting.GRAY + tooltip.get(i));
				}
			}
			return tooltip;
		} catch (RuntimeException e) {
			// Collections.singletonList() can not be added to. we don't want that
			if (aGuiStyle)
				return Lists.newArrayList((aStack.getRarity() == null ? EnumRarity.common : aStack.getRarity()).rarityColor + aStack.getDisplayName());
			return Lists.newArrayList(aStack.getDisplayName());
		}
	}

    public static void debugPrintGLEnable() {
        Map<Integer, String> functions = ImmutableMap.<Integer, String>builder()
            .put(GL11.GL_NEVER, "GL_NEVER")
            .put(GL11.GL_LESS, "GL_LESS")
            .put(GL11.GL_EQUAL, "GL_EQUAL")
            .put(GL11.GL_LEQUAL, "GL_LEQUAL")
            .put(GL11.GL_GREATER, "GL_GREATER")
            .put(GL11.GL_NOTEQUAL, "GL_NOTEQUAL")
            .put(GL11.GL_GEQUAL, "GL_GEQUAL")
            .put(GL11.GL_ALWAYS, "GL_ALWAYS")
            .put(GL11.GL_ZERO, "GL_ZERO")
            .put(GL11.GL_ONE, "GL_ONE")
            .put(GL11.GL_SRC_COLOR, "GL_SRC_COLOR")
            .put(GL11.GL_ONE_MINUS_SRC_COLOR, "GL_ONE_MINUS_SRC_COLOR")
            .put(GL11.GL_DST_COLOR, "GL_DST_COLOR")
            .put(GL11.GL_ONE_MINUS_DST_COLOR, "GL_ONE_MINUS_DST_COLOR")
            .put(GL11.GL_SRC_ALPHA, "GL_SRC_ALPHA")
            .put(GL11.GL_ONE_MINUS_SRC_ALPHA, "GL_ONE_MINUS_SRC_ALPHA")
            .put(GL11.GL_DST_ALPHA, "GL_DST_ALPHA")
            .put(GL11.GL_ONE_MINUS_DST_ALPHA, "GL_ONE_MINUS_DST_ALPHA")
            .put(GL11.GL_CONSTANT_COLOR, "GL_CONSTANT_COLOR")
            .put(GL11.GL_ONE_MINUS_CONSTANT_COLOR, "GL_ONE_MINUS_CONSTANT_COLOR")
            .put(GL11.GL_CONSTANT_ALPHA, "GL_CONSTANT_ALPHA")
            .put(GL11.GL_ONE_MINUS_CONSTANT_ALPHA, "GL_ONE_MINUS_CONSTANT_ALPHA")
            .put(GL11.GL_SRC_ALPHA_SATURATE, "GL_SRC_ALPHA_SATURATE")
            .build();
        System.out.println("GL_ALPHA_TEST: " + GL11.glIsEnabled(GL_ALPHA_TEST));
        System.out.println("GL_AUTO_NORMAL: " + GL11.glIsEnabled(GL_AUTO_NORMAL));
        System.out.println("GL_BLEND: " + GL11.glIsEnabled(GL_BLEND));
        System.out.println("GL_COLOR_ARRAY: " + GL11.glIsEnabled(GL_COLOR_ARRAY));
        System.out.println("GL_COLOR_LOGIC_OP: " + GL11.glIsEnabled(GL_COLOR_LOGIC_OP));
        System.out.println("GL_COLOR_MATERIAL: " + GL11.glIsEnabled(GL_COLOR_MATERIAL));
        System.out.println("GL_COLOR_SUM: " + GL11.glIsEnabled(GL_COLOR_SUM));
        System.out.println("GL_COLOR_TABLE: " + GL11.glIsEnabled(GL_COLOR_TABLE));
        System.out.println("GL_CONVOLUTION_1D: " + GL11.glIsEnabled(GL_CONVOLUTION_1D));
        System.out.println("GL_CONVOLUTION_2D: " + GL11.glIsEnabled(GL_CONVOLUTION_2D));
        System.out.println("GL_CULL_FACE: " + GL11.glIsEnabled(GL_CULL_FACE));
        System.out.println("GL_DEPTH_TEST: " + GL11.glIsEnabled(GL_DEPTH_TEST));
        System.out.println("GL_DITHER: " + GL11.glIsEnabled(GL_DITHER));
        System.out.println("GL_EDGE_FLAG_ARRAY: " + GL11.glIsEnabled(GL_EDGE_FLAG_ARRAY));
        System.out.println("GL_FOG: " + GL11.glIsEnabled(GL_FOG));
        System.out.println("GL_FOG_COORD_ARRAY: " + GL11.glIsEnabled(GL_FOG_COORD_ARRAY));
        System.out.println("GL_HISTOGRAM: " + GL11.glIsEnabled(GL_HISTOGRAM));
        System.out.println("GL_INDEX_ARRAY: " + GL11.glIsEnabled(GL_INDEX_ARRAY));
        System.out.println("GL_INDEX_LOGIC_OP: " + GL11.glIsEnabled(GL_INDEX_LOGIC_OP));
        System.out.println("GL_LIGHTING: " + GL11.glIsEnabled(GL_LIGHTING));
        System.out.println("GL_LINE_SMOOTH: " + GL11.glIsEnabled(GL_LINE_SMOOTH));
        System.out.println("GL_LINE_STIPPLE: " + GL11.glIsEnabled(GL_LINE_STIPPLE));
        System.out.println("GL_MAP1_COLOR_4: " + GL11.glIsEnabled(GL_MAP1_COLOR_4));
        System.out.println("GL_MAP1_INDEX: " + GL11.glIsEnabled(GL_MAP1_INDEX));
        System.out.println("GL_MAP1_NORMAL: " + GL11.glIsEnabled(GL_MAP1_NORMAL));
        System.out.println("GL_MAP1_TEXTURE_COORD_1: " + GL11.glIsEnabled(GL_MAP1_TEXTURE_COORD_1));
        System.out.println("GL_MAP1_TEXTURE_COORD_2: " + GL11.glIsEnabled(GL_MAP1_TEXTURE_COORD_2));
        System.out.println("GL_MAP1_TEXTURE_COORD_3: " + GL11.glIsEnabled(GL_MAP1_TEXTURE_COORD_3));
        System.out.println("GL_MAP1_TEXTURE_COORD_4: " + GL11.glIsEnabled(GL_MAP1_TEXTURE_COORD_4));
        System.out.println("GL_MAP2_COLOR_4: " + GL11.glIsEnabled(GL_MAP2_COLOR_4));
        System.out.println("GL_MAP2_INDEX: " + GL11.glIsEnabled(GL_MAP2_INDEX));
        System.out.println("GL_MAP2_NORMAL: " + GL11.glIsEnabled(GL_MAP2_NORMAL));
        System.out.println("GL_MAP2_TEXTURE_COORD_1: " + GL11.glIsEnabled(GL_MAP2_TEXTURE_COORD_1));
        System.out.println("GL_MAP2_TEXTURE_COORD_2: " + GL11.glIsEnabled(GL_MAP2_TEXTURE_COORD_2));
        System.out.println("GL_MAP2_TEXTURE_COORD_3: " + GL11.glIsEnabled(GL_MAP2_TEXTURE_COORD_3));
        System.out.println("GL_MAP2_TEXTURE_COORD_4: " + GL11.glIsEnabled(GL_MAP2_TEXTURE_COORD_4));
        System.out.println("GL_MAP2_VERTEX_3: " + GL11.glIsEnabled(GL_MAP2_VERTEX_3));
        System.out.println("GL_MAP2_VERTEX_4: " + GL11.glIsEnabled(GL_MAP2_VERTEX_4));
        System.out.println("GL_MINMAX: " + GL11.glIsEnabled(GL_MINMAX));
        System.out.println("GL_MULTISAMPLE: " + GL11.glIsEnabled(GL_MULTISAMPLE));
        System.out.println("GL_NORMAL_ARRAY: " + GL11.glIsEnabled(GL_NORMAL_ARRAY));
        System.out.println("GL_NORMALIZE: " + GL11.glIsEnabled(GL_NORMALIZE));
        System.out.println("GL_POINT_SMOOTH: " + GL11.glIsEnabled(GL_POINT_SMOOTH));
        System.out.println("GL_POINT_SPRITE: " + GL11.glIsEnabled(GL_POINT_SPRITE));
        System.out.println("GL_POLYGON_SMOOTH: " + GL11.glIsEnabled(GL_POLYGON_SMOOTH));
        System.out.println("GL_POLYGON_OFFSET_FILL: " + GL11.glIsEnabled(GL_POLYGON_OFFSET_FILL));
        System.out.println("GL_POLYGON_OFFSET_LINE: " + GL11.glIsEnabled(GL_POLYGON_OFFSET_LINE));
        System.out.println("GL_POLYGON_OFFSET_POINT: " + GL11.glIsEnabled(GL_POLYGON_OFFSET_POINT));
        System.out.println("GL_POLYGON_STIPPLE: " + GL11.glIsEnabled(GL_POLYGON_STIPPLE));
        System.out.println("GL_POST_COLOR_MATRIX_COLOR_TABLE: " + GL11.glIsEnabled(GL_POST_COLOR_MATRIX_COLOR_TABLE));
        System.out.println("GL_POST_CONVOLUTION_COLOR_TABLE: " + GL11.glIsEnabled(GL_POST_CONVOLUTION_COLOR_TABLE));
        System.out.println("GL_RESCALE_NORMAL: " + GL11.glIsEnabled(GL_RESCALE_NORMAL));
        System.out.println("GL_SAMPLE_ALPHA_TO_COVERAGE: " + GL11.glIsEnabled(GL_SAMPLE_ALPHA_TO_COVERAGE));
        System.out.println("GL_SAMPLE_ALPHA_TO_ONE: " + GL11.glIsEnabled(GL_SAMPLE_ALPHA_TO_ONE));
        System.out.println("GL_SAMPLE_COVERAGE: " + GL11.glIsEnabled(GL_SAMPLE_COVERAGE));
        System.out.println("GL_SCISSOR_TEST: " + GL11.glIsEnabled(GL_SCISSOR_TEST));
        System.out.println("GL_SECONDARY_COLOR_ARRAY: " + GL11.glIsEnabled(GL_SECONDARY_COLOR_ARRAY));
        System.out.println("GL_SEPARABLE_2D: " + GL11.glIsEnabled(GL_SEPARABLE_2D));
        System.out.println("GL_STENCIL_TEST: " + GL11.glIsEnabled(GL_STENCIL_TEST));
        System.out.println("GL_TEXTURE_1D: " + GL11.glIsEnabled(GL_TEXTURE_1D));
        System.out.println("GL_TEXTURE_2D: " + GL11.glIsEnabled(GL_TEXTURE_2D));
        System.out.println("GL_TEXTURE_3D: " + GL11.glIsEnabled(GL_TEXTURE_3D));
        System.out.println("GL_TEXTURE_COORD_ARRAY: " + GL11.glIsEnabled(GL_TEXTURE_COORD_ARRAY));
        System.out.println("GL_TEXTURE_CUBE_MAP: " + GL11.glIsEnabled(GL_TEXTURE_CUBE_MAP));
        System.out.println("GL_TEXTURE_GEN_Q: " + GL11.glIsEnabled(GL_TEXTURE_GEN_Q));
        System.out.println("GL_TEXTURE_GEN_R: " + GL11.glIsEnabled(GL_TEXTURE_GEN_R));
        System.out.println("GL_TEXTURE_GEN_S: " + GL11.glIsEnabled(GL_TEXTURE_GEN_S));
        System.out.println("GL_TEXTURE_GEN_T: " + GL11.glIsEnabled(GL_TEXTURE_GEN_T));
        System.out.println("GL_VERTEX_ARRAY: " + GL11.glIsEnabled(GL_VERTEX_ARRAY));
        System.out.println("GL_VERTEX_PROGRAM_POINT_SIZE: " + GL11.glIsEnabled(GL_VERTEX_PROGRAM_POINT_SIZE));
        System.out.println("GL_VERTEX_PROGRAM_TWO_SIDE: " + GL11.glIsEnabled(GL_VERTEX_PROGRAM_TWO_SIDE));
        System.out.println("Depth Function: " + functions.get(GL11.glGetInteger(GL_DEPTH_FUNC)));
        System.out.printf("Blend Function: DST: Alpha %s RGB %s, SRC: Alpha %s RGB %s\n", functions.get(GL11.glGetInteger(GL_BLEND_DST_ALPHA)), functions.get(GL11.glGetInteger(GL_BLEND_DST_RGB)), functions.get(GL11.glGetInteger(GL_BLEND_SRC_ALPHA)), functions.get(GL11.glGetInteger(GL_BLEND_SRC_RGB)));
        System.out.println("Alpha Test Function: " + functions.get(GL11.glGetInteger(GL_VERTEX_PROGRAM_TWO_SIDE)) + ", Reference: " + GL11.glGetDouble(GL11.GL_ALPHA_TEST_REF));
    }
}
