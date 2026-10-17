package io.github.jumperonjava.customcursor.util;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.jumperonjava.customcursor.CursorRenderer;
import io.github.jumperonjava.customcursor.CursorSettings;
import net.minecraft.client.Minecraft;

//? if >= 26.3 {
/*import org.lwjgl.sdl.*;
*///?} else {
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
//?}

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

public class WindowingBackend {
    //? if >= 26.3 {
    /*public static Backend INSTANCE = new Backend() {
        @Override
        public long getCursorHandle(CursorRenderer.RenderedCursorBuffer rendered) {
            var sdlSurface = SDLSurface.SDL_CreateSurface(rendered.size(), rendered.size(), SDLPixels.SDL_PIXELFORMAT_RGBA32);
            assert sdlSurface != null;
            sdlSurface.pixels(rendered.pixels());
            var cursorHandle = SDLMouse.SDL_CreateColorCursor(sdlSurface, rendered.x(), rendered.y());
            if(cursorHandle == 0){
                throw new RuntimeException("[Customcursor] SDL cursor handle is 0");
            }
            return cursorHandle;
        }
    };
    *///?} else {
    public static Backend INSTANCE = new Backend() {
        public long getCursorHandle(CursorRenderer.RenderedCursorBuffer rendered) {
            var glfwImage = GLFWImage.malloc().set(rendered.size(), rendered.size(), rendered.pixels());
            return GLFW.glfwCreateCursor(glfwImage, rendered.x(), rendered.y());
        }
    };
    //?}

    public interface Backend {
        long getCursorHandle(CursorRenderer.RenderedCursorBuffer buffer);

    }


}

