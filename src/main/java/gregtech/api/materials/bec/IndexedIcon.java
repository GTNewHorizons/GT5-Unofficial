package gregtech.api.materials.bec;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.function.Function;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.event.TextureStitchEvent;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.color.ImmutableColor;
import com.gtnewhorizon.gtnhlib.color.RGBColor;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.gtnewhorizon.gtnhlib.itemrendering.IItemTexture;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.GTMod;
import gregtech.api.enums.Mods;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/// An IndexedIcon is a wrapper around several [IndexedIconSprite]s, which are a custom implementation of [IIcon].
/// The purpose of this class is to allow you to render [indexed images](https://en.wikipedia.org/wiki/Indexed_color).
/// This is primarily for the purpose of allowing the creation of generic indexed sprites, which can be combined with a
/// per-material palette. Currently, the material systems have two layers: the background, and the overlay.
/// Materials only have one color, which forces unique materials to have a custom [gregtech.api.enums.TextureSet].
/// This class is an experimental solution for avoiding this, since indexed sprites are easier to work with on the art
/// side of development.
/// <br />
/// Instances of this class must be closed after they are created, so that their [IndexedIconSprite]s are no longer
/// added to their corresponding [TextureMap]. This is done so that [IndexedIcon]s are completely self-contained - you
/// just have to call render on them, and they will render, the same as normal icons.
@EventBusSubscriber
public class IndexedIcon implements Closeable {

    public static final EnumMap<TextureMapType, Object2ObjectMap<String, IndexedIcon>> ICONS = new EnumMap<>(
        TextureMapType.class);

    static {
        ICONS.put(TextureMapType.ITEMS, new Object2ObjectOpenHashMap<>());
        ICONS.put(TextureMapType.BLOCKS, new Object2ObjectOpenHashMap<>());
    }

    private final TextureMapType mapType;
    private final String iconName;
    private final ResourceLocation location;

    private int width, height;

    private AnimationMetadataSection animation;

    private final ArrayList<IndexedIconSprite> layers = new ArrayList<>();

    public enum TextureMapType {

        BLOCKS("textures/blocks"),
        ITEMS("textures/items");

        public final String root;

        TextureMapType(String root) {
            this.root = root;
        }
    }

    public IndexedIcon(TextureMapType mapType, String iconName) {
        this.mapType = mapType;
        this.iconName = iconName;
        this.location = Mods.GregTech.getResourceLocation(mapType.root, iconName + ".png");

        ICONS.get(mapType)
            .put(iconName, this);
    }

    public static IndexedIcon getIcon(TextureMapType mapType, String iconName) {
        Function<String, IndexedIcon> mapper = switch (mapType) {
            case BLOCKS -> IndexedIcon::createBlockIcon;
            case ITEMS -> IndexedIcon::createItemIcon;
        };

        return ICONS.get(mapType)
            .computeIfAbsent(iconName, mapper);
    }

    private static IndexedIcon createBlockIcon(String name) {
        return new IndexedIcon(TextureMapType.BLOCKS, name);
    }

    private static IndexedIcon createItemIcon(String name) {
        return new IndexedIcon(TextureMapType.ITEMS, name);
    }

    /// Loads the icon's image from the resource manager and splits it into layers (one sprite per index in the image).
    public void load(IResourceManager resourceManager, TextureMap textureMap) {
        IResource resource;
        BufferedImage image;

        try {
            resource = resourceManager.getResource(location);

            image = ImageIO.read(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!(image.getColorModel() instanceof IndexColorModel)) {
            GTMod.GT_FML_LOGGER
                .error("Sprite {} is not an indexed image and cannot be loaded into an IndexedIcon", location);
            return;
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
        this.animation = (AnimationMetadataSection) resource.getMetadata("animation");

        int[] indices = new int[width * height];
        image.getData()
            .getPixels(0, 0, width, height, indices);

        IntSet usedIndices = new IntOpenHashSet();
        usedIndices.addAll(IntArrayList.wrap(indices));

        // 0 is always transparent, we don't want to make a sprite for it
        usedIndices.remove(0);

        layers.clear();

        for (int index : usedIndices) {
            String iconName = location.toString() + ":" + index;

            IndexedIconSprite icon = new IndexedIconSprite(
                iconName,
                indices,
                index,
                textureMap.mipmapLevels,
                textureMap.anisotropicFiltering > 1.0F);

            layers.add(icon);
            textureMap.setTextureEntry(iconName, icon);
        }
    }

    @Override
    public void close() {
        ICONS.get(mapType)
            .remove(iconName, this);
    }

    public IItemTexture asTexture(Function<ItemStack, Int2ObjectFunction<ImmutableColor>> paletteExtractor) {
        return (type, stack) -> {
            Int2ObjectFunction<ImmutableColor> palette = paletteExtractor.apply(stack);

            if (palette == null) return;

            this.render(type, palette);
        };
    }

    public void render(IItemRenderer.ItemRenderType type, Int2ObjectFunction<ImmutableColor> palette) {
        Tessellator tessellator = Tessellator.instance;

        for (IndexedIconSprite icon : layers) {
            ImmutableColor colour = palette.get(icon.paletteIndex);

            if (colour == null || colour.getAlpha() == 0) continue;

            switch (type) {
                case ENTITY -> icon.renderInWorld(tessellator, colour);
                case EQUIPPED, EQUIPPED_FIRST_PERSON -> icon.render3d(tessellator, 0.0625F, colour);
                case INVENTORY -> icon.render2d(tessellator, 0f, 0f, 16f, 16f, 0.001f, 0f, 0f, -1f, colour);
                default -> {}
            }
        }
    }

    /// Loads a palette from the resource manager, and converts it into a map for easy querying.
    /// A palette is just an indexed image - each pixel corresponds to one colour.
    /// The dimensions don't matter - the pixels are iterated over in order (left to right, top to bottom).
    /// Transparent pixels are skipped.
    public static Int2ObjectMap<ImmutableColor> loadPalette(ResourceLocation paletteLocation) {
        IResource resource;
        BufferedImage image;

        try {
            resource = Minecraft.getMinecraft()
                .getResourceManager()
                .getResource(paletteLocation);

            image = ImageIO.read(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (image == null) {
            GTMod.GT_FML_LOGGER.error("Could not load palette {}", paletteLocation);
            return null;
        }

        if (!(image.getColorModel() instanceof IndexColorModel model)) {
            GTMod.GT_FML_LOGGER
                .error("Sprite {} is not an indexed image and cannot be loaded as a palette", paletteLocation);
            return null;
        }

        int[] colours = new int[model.getMapSize()];
        model.getRGBs(colours);

        Int2ObjectMap<ImmutableColor> palette = new Int2ObjectOpenHashMap<>();
        palette.defaultReturnValue(RGBColor.fromARGB(0xFF000000));

        for (int i = 0; i < colours.length; i++) {
            int argb = colours[i];

            if (argb == 0xFF000000) continue;

            palette.put(i, RGBColor.fromARGB(argb));
        }

        return palette;
    }

    @SubscribeEvent
    public static void registerIcons(TextureStitchEvent.Pre event) {
        TextureMapType type;

        switch (event.map.getTextureType()) {
            case 0 -> type = TextureMapType.BLOCKS;
            case 1 -> type = TextureMapType.ITEMS;
            default -> {
                return;
            }
        }

        IResourceManager resourceManager = Minecraft.getMinecraft()
            .getResourceManager();

        ICONS.get(type)
            .values()
            .forEach(indexedIcon -> indexedIcon.load(resourceManager, event.map));
    }

    public class IndexedIconSprite extends TextureAtlasSprite {

        private int[] indices;
        private final int paletteIndex;
        private final int mipmapLevels;
        private final boolean useAnisotropicFiltering;

        IndexedIconSprite(String iconName, int[] indices, int paletteIndex, int mipmapLevels,
            boolean useAnisotropicFiltering) {
            super(iconName);
            this.indices = indices;
            this.paletteIndex = paletteIndex;
            this.mipmapLevels = mipmapLevels;
            this.useAnisotropicFiltering = useAnisotropicFiltering;
        }

        @Override
        public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
            return true;
        }

        @Override
        public boolean load(IResourceManager manager, ResourceLocation location) {
            int[] pixels = new int[IndexedIcon.this.width * IndexedIcon.this.height * 4];

            for (int i = 0; i < indices.length; i++) {
                if (indices[i] == paletteIndex) {
                    pixels[i * 4] = 0xFF;
                    pixels[i * 4 + 1] = 0xFF;
                    pixels[i * 4 + 2] = 0xFF;
                    pixels[i * 4 + 3] = 0xFF;
                }
            }

            indices = null;

            BufferedImage[] images = new BufferedImage[1 + mipmapLevels];

            BufferedImage image = new BufferedImage(
                IndexedIcon.this.width,
                IndexedIcon.this.height,
                BufferedImage.TYPE_INT_ARGB);
            images[0] = image;

            image.getRaster()
                .setPixels(0, 0, IndexedIcon.this.width, IndexedIcon.this.height, pixels);

            loadSprite(images, animation, useAnisotropicFiltering);

            return false;
        }

        public void renderInWorld(Tessellator tessellator, ImmutableColor colour) {
            if (Minecraft.getMinecraft().gameSettings.fancyGraphics) {
                render3d(tessellator, 0.0625F, colour);
            } else {
                GL11.glPushMatrix();

                if (!RenderItem.renderInFrame) {
                    GL11.glRotatef(180.0F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                render2d(tessellator, -0.5, -0.25, 0.5f, 0.75f, 0f, 0f, 1f, 0f, colour);

                GL11.glPopMatrix();
            }
        }

        public void render2d(Tessellator tessellator, double minX, double minY, double maxX, double maxY, double z,
            float nx, float ny, float nz, ImmutableColor colour) {
            float minU = getMinU();
            float maxU = getMaxU();
            float minV = getMinV();
            float maxV = getMaxV();

            tessellator.startDrawingQuads();

            tessellator.setColorRGBA(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());
            tessellator.setNormal(nx, ny, nz);

            if (nz > 0.0F) {
                tessellator.addVertexWithUV(minX, minY, z, minU, minV);
                tessellator.addVertexWithUV(maxX, minY, z, maxU, minV);
                tessellator.addVertexWithUV(maxX, maxY, z, maxU, maxV);
                tessellator.addVertexWithUV(minX, maxY, z, minU, maxV);
            } else {
                tessellator.addVertexWithUV(minX, maxY, z, minU, maxV);
                tessellator.addVertexWithUV(maxX, maxY, z, maxU, maxV);
                tessellator.addVertexWithUV(maxX, minY, z, maxU, minV);
                tessellator.addVertexWithUV(minX, minY, z, minU, minV);
            }

            tessellator.draw();
        }

        public void render3d(Tessellator tessellator, float thickness, ImmutableColor colour) {
            float minU = getMinU();
            float maxU = getMaxU();
            float minV = getMinV();
            float maxV = getMaxV();

            tessellator.startDrawingQuads();

            tessellator.setColorRGBA(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha());

            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, maxU, maxV);
            tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, minU, maxV);
            tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, minU, minV);
            tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, maxU, minV);

            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - thickness, maxU, minV);
            tessellator.addVertexWithUV(1.0D, 1.0D, 0.0F - thickness, minU, minV);
            tessellator.addVertexWithUV(1.0D, 0.0D, 0.0F - thickness, minU, maxV);
            tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - thickness, maxU, maxV);

            int width = IndexedIcon.this.width;
            int height = IndexedIcon.this.height;

            float invWidth = 1f / (float) width;
            float invHeight = 1f / (float) height;

            float spanU = maxU - minU;
            float spanV = maxV - minV;

            float perPixelHalfU = spanU * 0.5f * invWidth;
            float perPixelHalfV = spanV * 0.5f * invHeight;

            tessellator.setNormal(-1.0F, 0.0F, 0.0F);

            for (int k = 0; k < width; ++k) {
                float x = k * invWidth;
                float U = maxU - spanU * x - perPixelHalfU;

                tessellator.addVertexWithUV(x, 0.0, 0.0 - thickness, U, maxV);
                tessellator.addVertexWithUV(x, 0.0, 0.0, U, maxV);
                tessellator.addVertexWithUV(x, 1.0, 0.0, U, minV);
                tessellator.addVertexWithUV(x, 1.0, 0.0 - thickness, U, minV);
            }

            tessellator.setNormal(1.0F, 0.0F, 0.0F);

            for (int k = 0; k < width; ++k) {
                float x = k * invWidth;
                float U = maxU - spanU * x - perPixelHalfU;

                tessellator.addVertexWithUV(x + invWidth, 1.0, 0.0 - thickness, U, minV);
                tessellator.addVertexWithUV(x + invWidth, 1.0, 0.0, U, minV);
                tessellator.addVertexWithUV(x + invWidth, 0.0, 0.0, U, maxV);
                tessellator.addVertexWithUV(x + invWidth, 0.0, 0.0 - thickness, U, maxV);
            }

            tessellator.setNormal(0.0F, 1.0F, 0.0F);

            for (int k = 0; k < height; ++k) {
                float y = k * invHeight;
                float V = maxV - spanV * y - perPixelHalfV;

                tessellator.addVertexWithUV(0.0, y + invHeight, 0.0, maxU, V);
                tessellator.addVertexWithUV(1.0, y + invHeight, 0.0, minU, V);
                tessellator.addVertexWithUV(1.0, y + invHeight, 0.0 - thickness, minU, V);
                tessellator.addVertexWithUV(0.0, y + invHeight, 0.0 - thickness, maxU, V);
            }

            tessellator.setNormal(0.0F, -1.0F, 0.0F);

            for (int k = 0; k < height; ++k) {
                float y = k * invHeight;
                float V = maxV - spanV * y - perPixelHalfV;

                tessellator.addVertexWithUV(1.0, y, 0.0, minU, V);
                tessellator.addVertexWithUV(0.0, y, 0.0, maxU, V);
                tessellator.addVertexWithUV(0.0, y, 0.0 - thickness, maxU, V);
                tessellator.addVertexWithUV(1.0, y, 0.0 - thickness, minU, V);
            }

            tessellator.draw();
        }
    }
}
