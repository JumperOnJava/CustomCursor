//? if > 1.21.8 {
/*package io.github.jumperonjava.customcursor.editor;

import io.github.jumperonjava.customcursor.CursorSettings;
import io.github.jumperonjava.customcursor.util.VersionFunctions;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.Click;

public class CursorWidget implements Drawable, Element, Selectable {
    private final String key;
    private final CursorSettings.CursorSprite sprite;
    private final int x;
    private final int y;
    private final int size;
    private final Runnable callback;

    public CursorWidget(String key, CursorSettings.CursorSprite sprite, int x, int y, int size, Runnable callback) {
        this.key = key;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.size = size;
        this.callback = callback;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.getMatrices().pushMatrix();
        context.getMatrices().rotateAbout((float) Math.toRadians(sprite.rotation), x + (float) size / 2, y + (float) size / 2);
        if (isMouseOver(mouseX, mouseY)) {
            context.setCursor(CursorSettings.cursors().get(key));
            context.fill(x,y,x+size,y+size, 0x7f000000);
        }
        VersionFunctions.drawTexture(context, sprite.identifier, x, y, 0, 0, size, size, size * (sprite.mirroredX ? -1 : 1), size * (sprite.mirroredY ? -1 : 1));
        context.getMatrices().popMatrix();

    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (isMouseOver(click.x(), click.y())) {
            callback.run();
            return true;
        }
        return false;
    }


    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        var minx = x;
        var maxx = x + size;
        var miny = y;
        var maxy = y + size;

        return mouseX >= minx && mouseX < maxx && mouseY >= miny && mouseY < maxy;
    }

    boolean focused = false;

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.FOCUSED;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
*///?}