package gregtech.client.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static com.gtnewhorizon.gtnhlib.bytebuf.MemoryStack.*;
import static com.gtnewhorizon.gtnhlib.bytebuf.MemoryUtilities.*;

import com.gtnewhorizon.gtnhlib.bytebuf.MemoryStack;
import de.javagl.jgltf.impl.v2.Accessor;
import de.javagl.jgltf.impl.v2.GlTF;
import de.javagl.jgltf.impl.v2.MeshPrimitive;
import de.javagl.jgltf.model.io.GltfAsset;
import de.javagl.jgltf.model.io.GltfAssetReader;
import de.javagl.jgltf.model.io.GltfReferenceResolver;
import gregtech.GTMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Model implements AutoCloseable {
    private static final GltfAssetReader GLTF_READER = new GltfAssetReader();
    private final GltfAsset asset;
    private final GlTF gltf;
    private int glDataBuffer = Integer.MIN_VALUE;
    private int[] gltfDataOffsets;

    private static void clearGlErrors() {
        int errc;
        while ((errc = glGetError()) != GL_NO_ERROR) {
            GTMod.GT_FML_LOGGER.warn("GL error encountered: {} {}", errc, Util.translateGLErrorString(errc));
        }
    }

    private static void checkGl() {
        final int errc = glGetError();
        if (errc != GL_NO_ERROR) {
            throw new OpenGLException(errc);
        }
    }

    public Model(final ResourceLocation gltfResource) {
        try {
            clearGlErrors();
            // Load and parse GLTF
            final Path resRoot = Paths.get(gltfResource.getResourcePath().replace('/', File.separatorChar)).getParent();
            final IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
            final IResource res = resourceManager.getResource(gltfResource);
            final GltfAsset asset;
            try (final InputStream is = res.getInputStream()) {
                asset = GLTF_READER.readWithoutReferences(is);
            }
            GltfReferenceResolver.resolveAll(asset.getReferences(), subPath -> {
                final Path resPath = resRoot.resolve(subPath.replace('/', File.separatorChar)).normalize();
                final ResourceLocation subResource = new ResourceLocation(gltfResource.getResourceDomain(), resPath.toString().replace(File.separatorChar, '/'));
                try (final InputStream is = resourceManager.getResource(subResource).getInputStream()) {
                    return ByteBuffer.wrap(IOUtils.toByteArray(is));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            this.asset = asset;
            this.gltf = (GlTF) this.asset.getGltf();

            // Upload OpenGL data
            glDataBuffer = glGenBuffers();
            gltfDataOffsets = new int[gltf.getBuffers().size()];
            {
                int i = 0, offset = 0;
                final ByteBuffer[] slices = new ByteBuffer[gltfDataOffsets.length];
                for (final var buffer : gltf.getBuffers()) {
                    gltfDataOffsets[i] = offset;
                    slices[i] = asset.getReferenceData(buffer.getUri());
                    offset += buffer.getByteLength();
                    i++;
                }
                final ByteBuffer uploadBuffer = memAlloc(offset);
                try {
                    for (i = 0; i < slices.length; i++) {
                        memCopy(
                            memAddress(slices[i]),
                            memAddress(uploadBuffer, gltfDataOffsets[i]),
                            gltf.getBuffers().get(i).getByteLength());
                    }
                    glBindBuffer(GL_ARRAY_BUFFER, glDataBuffer);
                    checkGl();
                    glBufferData(GL_ARRAY_BUFFER, uploadBuffer, GL_STATIC_DRAW);
                    checkGl();
                } finally {
                    memFree(uploadBuffer);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (glDataBuffer != Integer.MIN_VALUE) {
            glDeleteBuffers(glDataBuffer);
            glDataBuffer = Integer.MIN_VALUE;
        }
    }

    public void draw() {
        final var nodes = gltf.getScenes().get(gltf.getScene()).getNodes();

        glBindBuffer(GL_ARRAY_BUFFER, glDataBuffer);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glDataBuffer);
        nodes.forEach(this::drawNode);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void drawNode(int nodeId) {
        final var node = gltf.getNodes().get(nodeId);

        if (node.getMesh() != null) {
            final int meshId = node.getMesh();
            final var mesh = gltf.getMeshes().get(meshId);
            for (final MeshPrimitive primitive : mesh.getPrimitives()) {
                final int mode = primitive.getMode() == null ? GL_TRIANGLES : primitive.getMode();
                final int positionArrayAccessor = primitive.getAttributes().get("POSITION");
                final Accessor positionArray = gltf.getAccessors().get(positionArrayAccessor);
                final int positionOffset = positionArray.getByteOffset() == null ? 0 : positionArray.getByteOffset();
                final int positionCount = positionArray.getCount();
                if (primitive.getIndices() != null) {
                    //
                } else {
                    //glDrawArrays(mode, );
                }
            }
        }

        if (node.getChildren() != null) {
            node.getChildren().forEach(this::drawNode);
        }
    }
}

