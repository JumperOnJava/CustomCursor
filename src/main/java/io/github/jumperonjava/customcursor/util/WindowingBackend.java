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
        public void hideCursor() {

        }

        @Override
        public void showCursor() {

        }

        @Override
        public long getCursorHandle(CursorRenderer.RenderedCursorBuffer rendered) {
                 var sdlSurface = SDLSurface.SDL_CreateSurface(rendered.size(),rendered.size(),SDLPixels.SDL_PIXELFORMAT_RGBA32);
                ByteBuffer image = rendered.pixels();
                sdlSurface.pixels(image);
                var cursorHandle = SDLMouse.SDL_CreateColorCursor(sdlSurface,rendered.x(),rendered.y());
                if(cursorHandle == 0){
                    throw new RuntimeException("SDL surface handle for cursor is 0");
                }
                return cursorHandle;
        }


        @Override
        public long getCursorVanillaHandle(CursorType cursor) {
            if (cursor == CursorTypes.ARROW) return SDLMouse.SDL_CreateSystemCursor(0);
            if (cursor == CursorTypes.IBEAM) return SDLMouse.SDL_CreateSystemCursor(1);
            if (cursor == CursorTypes.CROSSHAIR) return SDLMouse.SDL_CreateSystemCursor(3);
            if (cursor == CursorTypes.POINTING_HAND) return SDLMouse.SDL_CreateSystemCursor(1);
            if (cursor == CursorTypes.RESIZE_NS) return SDLMouse.SDL_CreateSystemCursor(8);
            if (cursor == CursorTypes.RESIZE_EW) return SDLMouse.SDL_CreateSystemCursor(7);
            if (cursor == CursorTypes.RESIZE_ALL) return SDLMouse.SDL_CreateSystemCursor(9);
            if (cursor == CursorTypes.NOT_ALLOWED) return SDLMouse.SDL_CreateSystemCursor(1);
            if (cursor == CursorType.DEFAULT) return SDLMouse.SDL_CreateSystemCursor(0);
            return 0;
        }
    };
    *///?} else {
    public static Backend INSTANCE = new Backend() {
        @Override
        public void hideCursor() {
            GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().handle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        }

        @Override
        public void showCursor() {
            GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().handle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }

        public long getCursorHandle(CursorRenderer.RenderedCursorBuffer rendered) {
            var glfwImage = GLFWImage.malloc().set(rendered.size(), rendered.size(), rendered.pixels());
            return GLFW.glfwCreateCursor(glfwImage, rendered.x(), rendered.y());
        }

        public long getCursorVanillaHandle(CursorType cursor) {
            if (cursor == CursorTypes.ARROW) return GLFW.glfwCreateStandardCursor(221185);
            if (cursor == CursorTypes.IBEAM) return GLFW.glfwCreateStandardCursor(221186);
            if (cursor == CursorTypes.CROSSHAIR) return GLFW.glfwCreateStandardCursor(221187);
            if (cursor == CursorTypes.POINTING_HAND) return GLFW.glfwCreateStandardCursor(221188);
            if (cursor == CursorTypes.RESIZE_NS) return GLFW.glfwCreateStandardCursor(221190);
            if (cursor == CursorTypes.RESIZE_EW) return GLFW.glfwCreateStandardCursor(221189);
            if (cursor == CursorTypes.RESIZE_ALL) return GLFW.glfwCreateStandardCursor(221193);
            if (cursor == CursorTypes.NOT_ALLOWED) return GLFW.glfwCreateStandardCursor(221194);
            if (cursor == CursorType.DEFAULT) return 0;
            return 0;
        }
    };
    //?}

    public interface Backend {
        void hideCursor();

        void showCursor();

        long getCursorHandle(CursorRenderer.RenderedCursorBuffer buffer);

        long getCursorVanillaHandle(CursorType cursor);

    }


}

