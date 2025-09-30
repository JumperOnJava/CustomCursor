package io.github.jumperonjava.customcursor.editor;

import io.github.jumperonjava.customcursor.CursorConfigStorage;
import io.github.jumperonjava.customcursor.CursorSettings;
import io.github.jumperonjava.customcursor.CustomCursorInit;
import io.github.jumperonjava.customcursor.util.FolderTextureAskList;
import io.github.jumperonjava.customcursor.util.SliderWidget;
import io.github.jumperonjava.customcursor.util.VersionFunctions;
//? if > 1.21.8 {
import net.minecraft.client.gui.Click;
//?}

//? if <= 1.21.5 {
/*import com.mojang.blaze3d.systems.RenderSystem;
*///?}

//? if >= 1.21.5 {
import net.minecraft.client.gl.RenderPipelines;
//?}
import net.minecraft.client.gui.DrawContext;
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
    private final Screen parent;
    private CursorSettings.CursorSprite targetCursor;
    private PreviewWidget previewWidget;

    public CursorEditScreen(Screen parent, CursorSettings cursorConfig, Consumer<CursorSettings> onSuccess) {
        super(Text.empty());
        this.parent = parent;
        this.onSuccess = onSuccess;
        this.targetConfig = cursorConfig.clone();
        this.targetCursor = targetConfig.arrow;
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    private int centerX;
    private int centerY;

    int previewSize = 128;
    int previewPosX;
    int previewPosY;

    @Override
    protected void init() {


        centerX = width / 2;
        centerY = height / 2;

        previewPosX = (width - previewSize) / 2;
        previewPosY = centerY;

        super.init();

        Function<Boolean, Text> enableText = (Boolean b) -> Text.translatable("customcursor.edit.enabled." + b);
        var toggleWidget = new ButtonWidget.Builder(enableText.apply(this.targetConfig.enabled), (buttonWidget) -> {
            this.targetConfig.enabled = !targetConfig.enabled;
            buttonWidget.setMessage(enableText.apply(this.targetConfig.enabled));
        });

        Function<Boolean, Text> dynamicText = (Boolean b) -> Text.translatable("customcursor.edit.dynamic." + b);
        var dynmicWidget = new ButtonWidget.Builder(dynamicText.apply(this.targetConfig.dynamicEnabled), (buttonWidget) -> {
            this.targetConfig.dynamicEnabled = !targetConfig.dynamicEnabled;
            if (!targetConfig.dynamicEnabled) {
                targetCursor = targetConfig.arrow;
            }
            buttonWidget.setMessage(dynamicText.apply(this.targetConfig.dynamicEnabled));
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

        var rowCount = 4;
        var columnCount = 2;

        var startColumnPos = centerX - (columnCount * columnSize) / 2;
        var startRowPos = centerY - rowCount * rowSize;

        var rows = new int[rowCount];
        var columns = new int[columnCount];

        var padding = 2;
        var sizePadding = padding * 2;

        for (int i = 0; i < rowCount; i++) {
            rows[i] = startRowPos + rowSize * i + padding;
        }
        for (int i = 0; i < columnCount; i++) {
            columns[i] = startColumnPos + columnSize * i + padding;
        }

        int columnSizePadding = columnSize - sizePadding;
        int twoColumnSizePadding = columnSize * 2 - sizePadding;
        int rowSizePadding = rowSize - sizePadding;

        //? if <= 1.21.8 {
        /*toggleWidget = toggleWidget.dimensions(columns[0], rows[0], twoColumnSizePadding, rowSizePadding);
        dynmicWidget = dynmicWidget.dimensions(10000, 10000, 20, 20);
        *///?} else {
        toggleWidget = toggleWidget.dimensions(columns[0], rows[0], columnSizePadding, rowSizePadding);
        dynmicWidget = dynmicWidget.dimensions(columns[1], rows[0], columnSizePadding, rowSizePadding);
        //?}
        folderButton = folderButton.dimensions(columns[0] + twoColumnSizePadding - rowSizePadding, rows[2], rowSizePadding, rowSizePadding);
        confrmButton = confrmButton.dimensions(columns[0], rows[3], columnSizePadding, rowSizePadding);
        cancelButton = cancelButton.dimensions(columns[1], rows[3], columnSizePadding, rowSizePadding);

        var maxsize = 256.;
        var imagePathField = new TextFieldWidget(client.textRenderer, columns[0], rows[2], twoColumnSizePadding - rowSize, rowSizePadding, Text.empty());
        var sizeSlider = new SliderWidget(columns[0], rows[1], twoColumnSizePadding, rowSizePadding, Text.translatable("customcursor.edit.size"), this.targetConfig.size, 0, maxsize, 256);
        var xPosSlider = new SliderWidget(columns[0], rows[2], columnSizePadding, rowSizePadding, Text.translatable("customcursor.edit.x"), this.targetCursor.x, 0., 1., 64);
        var yPosSlider = new SliderWidget(columns[1], rows[2], columnSizePadding, rowSizePadding, Text.translatable("customcursor.edit.y"), this.targetCursor.y, 0., 1., 64);

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
        addDrawableChild(dynmicWidget.build());
        addDrawableChild(confrmButton.build());
        addDrawableChild(cancelButton.build());
        addDrawableChild(folderButton.build());
        addDrawableChild(imagePathField);
        addDrawableChild(sizeSlider);
//        addDrawableChild(xPosSlider);
//        addDrawableChild(yPosSlider);

        this.previewWidget = new PreviewWidget("", targetCursor, previewPosX, previewPosY, previewSize, (x, y) -> {
            x = Math.round(x * 16) / 16d;
            y = Math.round(y * 16) / 16d;
            xPosSlider.setValue(x);
            yPosSlider.setValue(y);
        });


        var ref = new Object() {
            int count = 0;
        };
        //? if > 1.21.8 {
        CursorSettings.cursorOrder.forEach((key) -> {
            var value = this.targetConfig.allCursors().get(key);
            final int index = ref.count;

            int size = 16;
            int left = startColumnPos - size - padding;
            int top = rows[0] + index * (size + padding);

            addDrawableChild(new CursorWidget(key, value, left, top, size, () -> {
                this.targetCursor = value;
                xPosSlider.setValue(value.x);
                yPosSlider.setValue(value.y);
                imagePathField.setText(value.identifier.toString());
                previewWidget.setSprite(value);
            }) {
                @Override
                public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
                    if (!targetConfig.dynamicEnabled) {
                        return;
                    }
                    super.render(context, mouseX, mouseY, deltaTicks);
                }

                @Override
                public boolean mouseClicked(Click click, boolean doubled) {
                    if (!targetConfig.dynamicEnabled) {
                        return false;
                    }
                    return super.mouseClicked(click, doubled);

                }
            });

            addDrawable((ctx, x, y, td) -> {
                if (!targetConfig.dynamicEnabled) {
                    return;
                }
                var text = Text.translatable("customcursor.cursortype." + key);
                var width = textRenderer.getWidth(text);
                ctx.drawTextWithShadow(textRenderer, text, left - width - padding, top + 3, 0xFFFFFFFF);
            });

            ref.count++;
        });
        //?} else {

        //?}

        var rotateCCW_builder = new ButtonWidget.Builder(Text.literal("⟳"), (buttonWidget) -> {
            this.targetCursor.rotation += 90;
            if (this.targetCursor.rotation >= 360) {
                this.targetCursor.rotation -= 360;
            }
        });
        var rotateCW_builder = new ButtonWidget.Builder(Text.literal("⟲ "), (buttonWidget) -> {
            this.targetCursor.rotation -= 90;
            if (this.targetCursor.rotation < 0) {
                this.targetCursor.rotation += 360;
            }
        });
        var mirror_builder = new ButtonWidget.Builder(Text.literal("|"), (buttonWidget) -> {
            if (this.targetCursor.rotation == 0 || this.targetCursor.rotation == 180) {
                this.targetCursor.mirroredX = !this.targetCursor.mirroredX;
            }
            if (this.targetCursor.rotation == 90 || this.targetCursor.rotation == 270) {
                this.targetCursor.mirroredY = !this.targetCursor.mirroredY;
            }
        });

        rotateCW_builder.dimensions(previewPosX - 24, previewPosY + previewSize / 2 - 12, 22, 22);
        rotateCCW_builder.dimensions(previewPosX + 2 + previewSize, previewPosY + previewSize / 2 - 12, 22, 22);
        mirror_builder.dimensions(previewPosX + previewSize / 2 - 11, previewPosY + previewSize + 2, 22, 22);

        addDrawableChild(rotateCCW_builder.build());
        addDrawableChild(rotateCW_builder.build());
        addDrawableChild(mirror_builder.build());

        addDrawable(this::renderCheckerboard);
        addDrawableChild(previewWidget);
        addDrawable(this::testcursor);
    }


    float time = 0;

    private void testcursor(DrawContext context, int mouseX, int mouseY, float delta) {
//        context.setCursor(StandardCursors.CROSSHAIR);
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

    private void dynamic(ButtonWidget buttonWidget) {
        targetConfig.dynamicEnabled = !targetConfig.dynamicEnabled;
    }


    //? if <= 1.20.1 {
    /*@Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
    *///?}

    //? if <= 1.21.8 {
    /*@Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        previewWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    *///?} else {
    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        previewWidget.mouseDragged(click, offsetX, offsetY);
        return super.mouseDragged(click, offsetX, offsetY);
    }
    //?}

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


        var vec = new Vec2f(mouseX - (previewPosX + previewSize / 2f), mouseY - (previewPosY + previewSize / 2f)).normalize().multiply(delta);
        bgx += vec.x;
        bgy += vec.y;

        context.fill(previewPosX, previewPosY, previewPosX + previewSize, previewPosY + previewSize, color2);
        context.enableScissor(previewPosX, previewPosY, previewPosX + previewSize, previewPosY + previewSize);


        var cellsize = previewSize / 8;


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


        int color = VersionFunctions.ColorHelper.getArgb(255, (int) (r * 255), (int) (g * 255), (int) (b * 255));

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
        return new CursorEditScreen(parent, CustomCursorInit.getConfig().settings, c -> {
            var cfg = new CursorConfigStorage();
            cfg.settings = c;
            CustomCursorInit.setConfig(cfg);
        });
    }
}
