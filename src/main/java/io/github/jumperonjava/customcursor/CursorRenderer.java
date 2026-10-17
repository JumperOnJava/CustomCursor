package io.github.jumperonjava.customcursor;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class CursorRenderer {
    public static RenderedCursorBuffer renderState(int size, float x, float y, float rotation, boolean mirroredX, boolean mirroredY, Identifier identifier) {
        var initialNative = getNativeImage(identifier);
        var awtImage = new BufferedImage(initialNative.getWidth(),initialNative.getWidth(), BufferedImage.TYPE_INT_ARGB);
        awtImage.setRGB(0,0,initialNative.getWidth(),initialNative.getHeight(), initialNative.getPixels(), 0, initialNative.getWidth());
        var transformed = transform(awtImage, size, rotation,mirroredX,mirroredY,ScalingMode.NEAREST_NEIGHBOR);
        var pixels = transformed.getRGB(0,0,size,size, null,0,size);
        var buffer = argb2rgbaBuf(pixels);
        return new RenderedCursorBuffer(buffer, size, (int)(x*size), (int)(y*size));
    }

    public static BufferedImage transform(BufferedImage original, int size,
                                          double rotation, boolean mirroredX, boolean mirroredY,
                                          ScalingMode scalingMode) {

        BufferedImage output = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = output.createGraphics();

        switch (scalingMode) {
            case NEAREST_NEIGHBOR:
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                break;
            case BILINEAR:
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                break;
            case BICUBIC:
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                break;
        }

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform transform = new AffineTransform();

        double c = size / 2.0;

        transform.translate(c, c);
        transform.rotate(Math.toRadians(rotation));

        double scaleXRatio = (double) size / original.getWidth();
        double scaleYRatio = (double) size / original.getHeight();
        transform.scale(scaleXRatio * (mirroredX ? -1 : 1), scaleYRatio * (mirroredY ? -1 : 1));

        transform.translate(-original.getWidth() / 2.0, -original.getHeight() / 2.0);

        g2d.drawImage(original, transform, null);
        g2d.dispose();

        return output;
    }

    public enum ScalingMode {
        NEAREST_NEIGHBOR, BILINEAR, BICUBIC
    }

    private static NativeImage getNativeImage(Identifier identifier){
        try {
            if(identifier.getNamespace().equals("cursorfolder")){
                return specialcase_cursorfolder(identifier.getPath());
            }
            return NativeImage.read(Minecraft.getInstance().getResourceManager().open(identifier));
        } catch (IOException e) {
            try{
                return NativeImage.read(Minecraft.getInstance().getResourceManager().open(identifier));
            } catch (IOException e2){
                throw new RuntimeException(e2);
            }
        }
    }

    private static NativeImage specialcase_cursorfolder(String filename) throws IOException {
        return NativeImage.read(Files.newInputStream(CustomCursorInit.getTextureFolder().path.resolve(filename), StandardOpenOption.READ));
    }

    private static ByteBuffer argb2rgbaBuf(int[] pixels) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);

        for (int pixel : pixels) {
            byte a = (byte) ((pixel >> 24) & 0xFF);
            byte r = (byte) ((pixel >> 16) & 0xFF);
            byte g = (byte) ((pixel >> 8) & 0xFF);
            byte b = (byte) (pixel & 0xFF);

            buffer.put(r);
            buffer.put(g);
            buffer.put(b);
            buffer.put(a);
        }

        buffer.flip();
        return buffer;
    }


    public record RenderedCursorBuffer(
        ByteBuffer pixels,
        int size,
        int x,
        int y
    ){

    }
}
