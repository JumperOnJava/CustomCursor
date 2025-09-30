package io.github.jumperonjava.customcursor.editor;

import io.github.jumperonjava.customcursor.CursorSettings;
import io.github.jumperonjava.customcursor.util.VersionFunctions;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class PreviewWidget implements Drawable, Element, Selectable {
    private final String key;
    private final int x;
    private final int y;
    private final int size;
    private final BiConsumer<Double, Double> callback;
    private CursorSettings.CursorSprite sprite;

    public PreviewWidget(String key, CursorSettings.CursorSprite sprite, int x, int y, int size, BiConsumer<Double, Double> callback) {
        this.key = key;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.size = size;
        this.callback = callback;
    }

    boolean update(double mouseX, double mouseY) {
        if (isMouseOver(mouseX, mouseY)) {
            callback.accept((mouseX - x) / size, (mouseY - y) / size);
            return true;
        }
        return false;
    }

    //? if <= 1.21.8 {
    /*@Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return update(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return update(mouseX, mouseY);
    }
    *///?} else {
    @Override
    public boolean mouseDragged(Click click, double xdelta, double ydelta) {
        return update(click.x(), click.y());
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        return update(click.x(), click.y());
    }
    //?}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        VersionFunctions.pushMatrix(context);
        VersionFunctions.rotateAbout(context,(float) Math.toRadians(sprite.rotation), x + (float) size / 2, y + (float) size / 2);
        VersionFunctions.drawTexture(context, sprite.identifier, x, y, 0, 0, size, size, size * (sprite.mirroredX ? -1 : 1), size * (sprite.mirroredY ? -1 : 1));
        VersionFunctions.popMatrix(context);
        VersionFunctions.drawTexture(
                context,
                Identifier.of("customcursor", "textures/gui/pointer.png"),
                (int) (x + this.sprite.x * size) - 4,
                (int) (y + this.sprite.y * size) - 4,
                0, 0,
                8, 8,
                8, 8);
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

    public void setSprite(CursorSettings.CursorSprite value) {
        this.sprite = value;
    }
}
