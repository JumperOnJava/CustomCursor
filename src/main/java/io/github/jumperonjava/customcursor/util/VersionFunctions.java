package io.github.jumperonjava.customcursor.util;

//? if >= 1.21.6
/*import net.minecraft.client.gl.RenderPipelines;*/
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class VersionFunctions {
    public static void drawTexture(DrawContext context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        //? if < 1.21.3 {
        context.drawTexture(identifier,x,y,u,v,width,height,textureWidth,textureHeight);
        //?} else if < 1.21.6 {
        /*context.drawTexture(RenderLayer::getGuiTextured,identifier,x,y,u,v,width,height,textureWidth,textureHeight);
        *///?} else {
        /*context.drawTexture(RenderPipelines.GUI_TEXTURED,identifier,x,y,u,v,width,height,textureWidth,textureHeight);

        *///?}

    }


    //? if < 1.21.3 {
    public static ColorHelper.Argb ColorHelper = new ColorHelper.Argb();
    //?} else {
    /*public static ColorHelper ColorHelper = new ColorHelper();
     *///?}
}
