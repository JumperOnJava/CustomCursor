package io.github.jumperonjava.customcursor;

import io.github.jumperonjava.customcursor.util.VersionFunctions;
import net.minecraft.client.MinecraftClient;
//? if >= 1.21.6 {
/*import net.minecraft.client.gl.RenderPipelines;
*///?} else {
import net.minecraft.client.render.RenderLayer;
import com.mojang.blaze3d.systems.RenderSystem;
//?}
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class CursorRenderer {
    public static void render(DrawContext context, double mouseX, double mouseY, float donotuse_delta) {
        var config = CustomCursorInit.getConfig().settings;
        if (MinecraftClient.getInstance().currentScreen == null)
            return;
        if (!config.enabled) {
            GLFW.glfwSetInputMode(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
            return;
        }
        var scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        //? if < 1.21.5
        RenderSystem.depthFunc(GL11.GL_ALWAYS);

        var sprite = config.arrow;

        //? if > 1.21.8 {
        /*var contextCursor = context.cursor;
        sprite = config.cursorToSprite(contextCursor);
        *///?}

        var identifier = sprite.identifier;


        mouseX = (float) MinecraftClient.getInstance().mouse.getX();
        mouseY = (float) MinecraftClient.getInstance().mouse.getY();
        var x = (int) Math.round(mouseX - config.size * sprite.x);
        var y = (int) Math.round(mouseY - config.size * sprite.y);
        var u = (float) 0;
        var v = (float) 0;
        var width = config.size;
        var height = config.size;
        var textureWidth = config.size;
        var textureHeight = config.size;

        VersionFunctions.pushMatrix(context);
        //? if < 1.21.6 {
        context.getMatrices().scale((float) (1f/scale), (float) (1f/scale),1f);
        //?} else {
        /*context.getMatrices().scale((float) (1f/scale), (float) (1f/scale));
        *///?}
        VersionFunctions.rotateAbout(context, (float) Math.toRadians(sprite.rotation), x+ (float) textureWidth /2f, y+ (float) textureHeight /2f);
        //? if < 1.21.3 {
        context.drawTexture(identifier,x,y,u,v,width,height,textureWidth * (sprite.mirroredX ? -1:1), textureHeight* (sprite.mirroredY ? -1:1));
         //?} else if < 1.21.6 {
        /*context.drawTexture(RenderLayer::getGuiTexturedOverlay, identifier, x, y, u, v, width, height, textureWidth * (sprite.mirroredX ? -1:1), textureHeight* (sprite.mirroredY ? -1:1));
        *///?} else {
        /*context.drawTexture(RenderPipelines.GUI_TEXTURED, identifier, x, y, u, v, width, height, textureWidth * (sprite.mirroredX ? -1:1), textureHeight* (sprite.mirroredY ? -1:1));
        *///?}

        VersionFunctions.popMatrix(context);
        //? if < 1.21.5
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        //for debugging
        //context.drawTexture(new Identifier("customcursor","textures/gui/pointer.png"), (int) (mouseX-4), (int) (mouseY-4),0,0,8,8,8,8);
        GLFW.glfwSetInputMode(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);

    }
}
