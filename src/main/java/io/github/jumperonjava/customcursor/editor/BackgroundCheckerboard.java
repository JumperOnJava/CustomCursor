package io.github.jumperonjava.customcursor.editor;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.jumperonjava.customcursor.util.VersionFunctions;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;


//? if <= 1.21.5 {
import com.mojang.blaze3d.systems.RenderSystem;
//?} else {
/*import net.minecraft.client.gl.RenderPipelines;
*///?}

class BackgroundCheckerboard implements Drawable {
    private final CursorEditScreen cursorEditScreen;
    private final int x;
    private final int y;
    private final int size;

    public BackgroundCheckerboard(CursorEditScreen cursorEditScreen, int x, int y, int size) {
        this.cursorEditScreen = cursorEditScreen;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    private float bgx, bgy, color;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //generates slowly changing pastel colors for backround
        color += delta * 0.05f;
        var n = 2;
        var color1 = VersionFunctions.ColorHelper.getArgb(255,
                (int) (128 + 64 * Math.pow(Math.sin(color + 0 * Math.PI / 3), n)),
                (int) (128 + 64 * Math.pow(Math.sin(color + 2 * Math.PI / 3), n)),
                (int) (128 + 64 * Math.pow(Math.sin(color + 4 * Math.PI / 3), n))
        );
        var color2 = VersionFunctions.ColorHelper.getArgb(255,
                (int) ((192 + 63 * Math.pow(Math.cos(color + 0 * Math.PI / 3), n))),
                (int) ((192 + 63 * Math.pow(Math.cos(color + 2 * Math.PI / 3), n))),
                (int) ((192 + 63 * Math.pow(Math.cos(color + 4 * Math.PI / 3), n)))
        );

        float r = VersionFunctions.ColorHelper.getRed(color1) / 255f;
        float g = VersionFunctions.ColorHelper.getGreen(color1) / 255f;
        float b = VersionFunctions.ColorHelper.getBlue(color1) / 255f;


        var vec = new Vec2f(mouseX - (x + size / 2f), mouseY - (y + size / 2f)).normalize().multiply(delta);
        bgx += vec.x;
        bgy += vec.y;

        context.fill(x, y, x + size, y + size, color2);
        context.enableScissor(x, y, x + size, y + size);


        var cellsize = size / 8;


        //? if < 1.21.6 {

        context.getMatrices().push();
        context.getMatrices().translate(
                (x + MathHelper.floorMod(bgx, cellsize) - cellsize),
                (y + MathHelper.floorMod(bgy, cellsize) - cellsize),
                0);
        RenderSystem.setShaderColor(r, g, b, 255);

        int checkerRenderSize = size + cellsize;
        VersionFunctions.drawTexture(context, Identifier.of("customcursor", "textures/gui/backgroundcheckerboard.png"),
                0,
                0,
                0,
                0, checkerRenderSize, checkerRenderSize, checkerRenderSize, checkerRenderSize);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        context.getMatrices().pop();
        context.disableScissor();
//?} else {


        /*context.getMatrices().pushMatrix();
        context.getMatrices().translate(
                (x + MathHelper.floorMod(bgx, cellsize) - cellsize),
                (y + MathHelper.floorMod(bgy, cellsize) - cellsize));


        int color3 = VersionFunctions.ColorHelper.getArgb(255, (int) (r * 255), (int) (g * 255), (int) (b * 255));

        int checkerRenderSize = size + cellsize;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("customcursor", "textures/gui/backgroundcheckerboard.png"),
                0,
                0,
                0,
                0, checkerRenderSize, checkerRenderSize, checkerRenderSize, checkerRenderSize, color3);

        context.getMatrices().popMatrix();
        context.disableScissor();

        *///?}
    }
}
