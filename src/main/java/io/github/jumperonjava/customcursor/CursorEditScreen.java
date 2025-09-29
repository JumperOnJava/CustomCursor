package io.github.jumperonjava.customcursor;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.jumperonjava.customcursor.util.*;
//? if >= 1.21.6
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.Cursor;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

import java.util.function.Consumer;
import java.util.function.Function;

public class CursorEditScreen extends Screen {
    private final Consumer<CursorSettings> onSuccess;
    private final CursorSettings targetConfig;
    private final CursorSettings.CursorSprite targetCursor;
    private final Screen parent;

    public CursorEditScreen(Screen parent, CursorSettings cursorConfig, Consumer<CursorSettings> onSuccess) {
        super(Text.empty());
        this.parent = parent;
        this.onSuccess = onSuccess;
        this.targetConfig = cursorConfig.clone();
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    private int centerX;
    private int centerY;

    private final int previewSize = 128;
    private int previewPosX;
    private int previewPosY;

    @Override
    protected void init() {

        centerX = width / 2;
        centerY = height / 2;

        previewPosX = (width - previewSize) / 2;
        previewPosY = centerY;

        super.init();

        Function<Boolean, Text> textfunc = (Boolean b) -> Text.translatable("customcursor.edit.enabled." + b);
        var toggleWidget = new ButtonWidget.Builder(textfunc.apply(this.targetConfig.enabled), (buttonWidget) -> {
            this.targetConfig.enabled = !targetConfig.enabled;
            buttonWidget.setMessage(textfunc.apply(this.targetConfig.enabled));
        });

        var confrmButton = new ButtonWidget.Builder(Text.translatable("customcursor.edit.confirm"), this::confirm);
        var cancelButton = new ButtonWidget.Builder(Text.translatable("customcursor.edit.cancel"), (f) -> close());
        var folderButton = new ButtonWidget.Builder(Text.translatable("customcursor.edit.folder"), (b) -> {
            FolderTextureAskList.ask(
                    new FolderTextureAskList(
                            CustomCursorInit.getTextureFolder(),
                            this::setIdentifier
                    )
            );
        });

        int rowSize = 24;
        int columnSize = 128;

        var rowCount = 6;
        var columnCount = 2;

        var startColumnPos = centerX - (columnCount * columnSize) / 2;
        var startRowPos = centerY - rowCount * rowSize;

        var rows = new int[rowCount];
        var columns = new int[columnCount];

        var padding = 2;
        var sizePadding = padding*2;

        for (int i = 0; i < rowCount; i++) {
            rows[i] = startRowPos + rowSize * i + padding;
        }
        for (int i = 0; i < columnCount; i++) {
            columns[i] = startColumnPos + columnSize * i+ padding;
        }

        int columnSizePadding = columnSize - sizePadding;
        int twoColumnSizePadding = columnSize * 2 - sizePadding;
        int rowSizePadding = rowSize - sizePadding;

        toggleWidget = toggleWidget.dimensions(columns[0], rows[0], twoColumnSizePadding, rowSizePadding);
        folderButton = folderButton.dimensions(columns[0], rows[3], twoColumnSizePadding, rowSizePadding);
        confrmButton = confrmButton.dimensions(columns[0], rows[5], columnSizePadding, rowSizePadding);
        cancelButton = cancelButton.dimensions(columns[1], rows[5], columnSizePadding, rowSizePadding);


        var maxsize = 256.;
        var imagePathField = new TextFieldWidget(client.textRenderer, columns[0], rows[4], twoColumnSizePadding, rowSizePadding, Text.empty());
        var sizeSlider = new SliderWidget(columns[0], rows[1], twoColumnSizePadding, rowSizePadding, Text.translatable("customcursor.edit.size"), this.targetConfig.size,0, maxsize,  256);
        var xPosSlider = new SliderWidget(columns[0], rows[2], columnSizePadding, rowSizePadding, Text.translatable("customcursor.edit.x"), this.targetCursor.x,0., 1.,  64);
        var yPosSlider = new SliderWidget(columns[1], rows[2], columnSizePadding, rowSizePadding, Text.translatable("customcursor.edit.y"), this.targetCursor.y,0., 1.,  64);

        xPosSlider.setChangedListener(d -> this.targetCursor.x = (float) (double) d);
        yPosSlider.setChangedListener(d -> this.targetCursor.y = (float) (double) d);
        sizeSlider.setChangedListener(d -> this.targetConfig.size = (int) (double) d);

        imagePathField.setMaxLength(512);
        imagePathField.setText(this.targetCursor.identifier.toString());
        imagePathField.setChangedListener((s) -> {
            try {
                setIdentifier(Identifier.tryParse(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        addDrawableChild(toggleWidget.build());
        addDrawableChild(confrmButton.build());
        addDrawableChild(cancelButton.build());
        addDrawableChild(folderButton.build());
        addDrawableChild(imagePathField);
        addDrawableChild(sizeSlider);
        addDrawableChild(xPosSlider);
        addDrawableChild(yPosSlider);
        addDrawable(this::renderCheckerboard);
        addDrawable(this::renderPreview);
        addDrawable(this::testcursor);
    }

    float time = 0;
    private void testcursor(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setCursor(StandardCursors.CROSSHAIR);
    }

    private void setIdentifier(Identifier identifier) {
        this.targetCursor.identifier = identifier;
    }

    private void confirm(ButtonWidget buttonWidget) {
        try {
            onSuccess.accept(targetConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.close();
    }


    //? if <= 1.20.1 {
    /*@Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
    *///?}

    private void renderPreview(DrawContext context, int mouseX, int mouseY, float delta){
        VersionFunctions.drawTexture(context, this.targetCursor.identifier, previewPosX, previewPosY, 0, 0, previewSize, previewSize, previewSize, previewSize);
        VersionFunctions.drawTexture(
                context,
                Identifier.of("customcursor", "textures/gui/pointer.png"),
                (int) (previewPosX + this.targetCursor.x * previewSize) - 4,
                (int) (previewPosY + this.targetCursor.y * previewSize) - 4,
                0, 0,
                8, 8,
                8, 8);
    }

    private float bgx = 0, bgy = 0, color;

    private void renderCheckerboard(DrawContext context, int mouseX, int mouseY, float delta) {

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


        var vec = new Vec2f(mouseX - (previewPosX+previewSize/2f), mouseY - (previewPosY+previewSize/2f)).normalize().multiply(delta);
        bgx += vec.x;
        bgy += vec.y;

        context.fill(previewPosX, previewPosY, previewPosX + previewSize, previewPosY + previewSize, color2);
        context.enableScissor(previewPosX, previewPosY, previewPosX + previewSize, previewPosY + previewSize);


        var cellsize = previewSize/8;


        //? if < 1.21.6 {
        
        /*context.getMatrices().push();
        context.getMatrices().translate(
                (previewPosX + MathHelper.floorMod(bgx, cellsize) - cellsize),
                (previewPosY + MathHelper.floorMod(bgy, cellsize) - cellsize),
                0);
        RenderSystem.setShaderColor(r, g, b, 255);

        int checkerRenderSize = previewSize + cellsize;
        VersionFunctions.drawTexture(context, Identifier.of("customcursor", "textures/gui/backgroundcheckerboard.png"),
                0,
                0,
                0,
                0, checkerRenderSize, checkerRenderSize, checkerRenderSize, checkerRenderSize);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        context.getMatrices().pop();
        context.disableScissor();
        *///?} else {


        context.getMatrices().pushMatrix();
        context.getMatrices().translate(
                (previewPosX + MathHelper.floorMod(bgx, cellsize) - cellsize),
                (previewPosY + MathHelper.floorMod(bgy, cellsize) - cellsize));



        int color = VersionFunctions.ColorHelper.getArgb(255, (int) (r*255), (int) (g*255), (int) (b*255));

        int checkerRenderSize = previewSize + cellsize;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("customcursor", "textures/gui/backgroundcheckerboard.png"),
                0,
                0,
                0,
                0, checkerRenderSize, checkerRenderSize, checkerRenderSize, checkerRenderSize, color);

        context.getMatrices().popMatrix();
        context.disableScissor();

        //?}
    }

    public static CursorEditScreen createCursorEditScreen(Screen parent) {
        return new CursorEditScreen(parent, CustomCursorInit.getConfig().pointer, c -> {
            var cfg = new CursorConfigStorage();
            cfg.pointer = c;
            CustomCursorInit.setConfig(cfg);
        });
    }
}
