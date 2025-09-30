package io.github.jumperonjava.customcursor.util;

//? if >= 1.21.6
import net.minecraft.client.gl.RenderPipelines;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationCalculator;
import org.joml.Matrix3x2f;
import org.joml.Quaternionf;

public class VersionFunctions {
    public static void drawTexture(DrawContext context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        if (identifier == null)
            return;

        //? if < 1.21.3 {
        /*context.drawTexture(identifier,x,y,u,v,width,height,textureWidth,textureHeight);
         *///?} else if < 1.21.6 {
        /*context.drawTexture(RenderLayer::getGuiTextured, identifier, x, y, u, v, width, height, textureWidth, textureHeight);
        *///?} else {
        context.drawTexture(RenderPipelines.GUI_TEXTURED,identifier,x,y,u,v,width,height,textureWidth,textureHeight);

         //?}

    }


    //? if < 1.21.3 {
    /*public static ColorHelper.Argb ColorHelper = new ColorHelper.Argb();
     *///?} else {
    public static ColorHelper ColorHelper = new ColorHelper();
    //?}

    public static void pushMatrix(DrawContext context) {
        //? if <= 1.21.5 {
        /*context.getMatrices().push();
         //} else {
        /^context.getMatrices().pushMatrix();
        ^/*///?}
    }

    public static void popMatrix(DrawContext context) {
        //? if <= 1.21.5 {
        /*context.getMatrices().pop();
         *///?} else {
        context.getMatrices().popMatrix();
        //?}
    }

    public static void rotateAbout(DrawContext context, float radians, float x, float y) {
        //? if < 1.21.5 {
        /*var peek = context.getMatrices().peek();
        peek.getPositionMatrix().rotateAround(new Quaternionf().rotateZ(radians), x, y, 0);
        peek.getNormalMatrix().rotate(new Quaternionf().rotateZ(radians));
        *///?} elif = 1.21.5 {
        /*context.getMatrices().peek().rotateAround(new Quaternionf().rotateZ(radians),x,y,0);
        *///?} else {
        context.getMatrices().rotateAbout(radians, x, y);
        //?}
    }
}
