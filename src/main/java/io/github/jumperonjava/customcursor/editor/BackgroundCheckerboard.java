package io.github.jumperonjava.customcursor.editor;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.jumperonjava.customcursor.util.VF;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

import net.minecraft.client.renderer.RenderPipelines;

class BackgroundCheckerboard implements Renderable {
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
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        render(graphics, mouseX, mouseY, a);
    }

    public void render(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        //generates slowly changing pastel colors for backround
        color += delta * 0.05f;
        var n = 2;
        var color1 = VF.ARGB.color(255,
                (int) (128 + 64 * Math.pow(Math.sin(color + 0 * Math.PI / 3), n)),
                (int) (128 + 64 * Math.pow(Math.sin(color + 2 * Math.PI / 3), n)),
                (int) (128 + 64 * Math.pow(Math.sin(color + 4 * Math.PI / 3), n))
        );
        var color2 = VF.ARGB.color(255,
                (int) ((192 + 63 * Math.pow(Math.cos(color + 0 * Math.PI / 3), n))),
                (int) ((192 + 63 * Math.pow(Math.cos(color + 2 * Math.PI / 3), n))),
                (int) ((192 + 63 * Math.pow(Math.cos(color + 4 * Math.PI / 3), n)))
        );

        float r = VF.ARGB.red(color1) / 255f;
        float g = VF.ARGB.green(color1) / 255f;
        float b = VF.ARGB.blue(color1) / 255f;


        var vec = new Vec2(mouseX - (x + size / 2f), mouseY - (y + size / 2f)).normalized().scale(delta);

        bgx += vec.x;
        bgy += vec.y;

        context.fill(x, y, x + size, y + size, color2);
        context.enableScissor(x, y, x + size, y + size);


        var cellsize = size / 8;


        context.pose().pushMatrix();
        context.pose().translate(
                (x + Mth.positiveModulo(bgx, cellsize) - cellsize),
                (y + Mth.positiveModulo(bgy, cellsize) - cellsize));


        int color3 = VF.ARGB.color(255, (int) (r * 255), (int) (g * 255), (int) (b * 255));

        int checkerRenderSize = size + cellsize;
        context.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath("customcursor", "textures/gui/backgroundcheckerboard.png"),
                0,
                0,
                0,
                0, checkerRenderSize, checkerRenderSize, checkerRenderSize, checkerRenderSize, color3);

        context.pose().popMatrix();
        context.disableScissor();

    }
}
