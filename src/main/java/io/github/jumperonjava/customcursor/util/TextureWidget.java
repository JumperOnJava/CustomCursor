package io.github.jumperonjava.customcursor.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.util.Identifier;

public class TextureWidget{
    public static void render(DrawContext context, Identifier texture, int x, int y, int width, int height) {
        VersionFunctions.drawTexture(context,texture,x,y,0,0,width,height,width,height);
    }
}
