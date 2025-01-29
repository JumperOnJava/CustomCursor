package io.github.jumperonjava.customcursor;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static io.github.jumperonjava.customcursor.CustomCursorInit.client;

public class CursorRenderer {
    public static void render(DrawContext context, int mouseX, int mouseY, float delta) {
        var config = CustomCursorInit.getConfig().pointer;
        if (MinecraftClient.getInstance().currentScreen == null)
            return;;
        if (config.enabled) {
            var scale = client.getWindow().getScaleFactor();
            RenderSystem.depthFunc(GL11.GL_ALWAYS);
            var identifier = config.identifier;
            var x = (int) Math.round(mouseX - config.size * config.x / scale);
            var y = (int) Math.round(mouseY - config.size * config.y / scale);
            var u = (float) 0;
            var v = (float) 0;
            var width = (int) (config.size / scale);
            var height = (int) (config.size / scale);
            var textureWidth = (int) (config.size / scale);
            var textureHeight = (int) (config.size / scale);
            //? if < 1.21.3 {
            context.drawTexture(identifier,x,y,u,v,width,height,textureWidth,textureHeight);
             //?} else {
            /*context.drawTexture(RenderLayer::getGuiTexturedOverlay,identifier,x,y,u,v,width,height,textureWidth,textureHeight);
            *///?}
            RenderSystem.depthFunc(GL11.GL_LEQUAL);

            //for debugging
            //context.drawTexture(new Identifier("customcursor","textures/gui/pointer.png"), (int) (mouseX-4), (int) (mouseY-4),0,0,8,8,8,8);
            GLFW.glfwSetInputMode(client.getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        } else {
            GLFW.glfwSetInputMode(client.getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }
}
