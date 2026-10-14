package io.github.jumperonjava.customcursor.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.core.Direction;
import net.minecraft.util.SegmentedAnglePrecision;
import org.joml.Matrix3x2f;
import org.joml.Quaternionf;

//Some methods do not have any version differences. The context is that they had a lot of differences before, but after
//dropping support for pre 26.1 versions i just removed them for readability’s sake. I keep these methods like that to
//not change a lot of code for now as i'm still not sure about removing compatibility
public class VF {
    public static void blit(GuiGraphicsExtractor context, Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        if (identifier == null)
            return;
        context.blit(RenderPipelines.GUI_TEXTURED,identifier,x,y,u,v,width,height,textureWidth,textureHeight);
    }

    public static ARGB ARGB = new ARGB();

    public static void pushMatrix(GuiGraphicsExtractor context) {
        context.pose().pushMatrix();
    }

    public static void popMatrix(GuiGraphicsExtractor context) {
        context.pose().popMatrix();
    }

    public static void rotateAbout(GuiGraphicsExtractor context, float radians, float x, float y) {
        context.pose().rotateAbout(radians, x, y);
    }

    public static Screen getScreen(){
        //? if < 26.2 {
        return Minecraft.getInstance().screen;
        //?} else {
        /*return Minecraft.getInstance().gui.screen();
        *///?}
    }
    public static void setScreen(Screen screen){
        //? if < 26.2 {
        Minecraft.getInstance().setScreen(screen);
        //?} else {
        /*Minecraft.getInstance().gui.setScreen(screen);
        *///?}
    }

}
