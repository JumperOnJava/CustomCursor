package io.github.jumperonjava.customcursor.editor;

import io.github.jumperonjava.customcursor.CursorConfigStorage;
import io.github.jumperonjava.customcursor.CursorSettings;
import io.github.jumperonjava.customcursor.CustomCursorInit;
import io.github.jumperonjava.customcursor.util.VF;
import net.minecraft.client.input.MouseButtonEvent;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;

public class CursorEditScreen extends Screen {
    private final Consumer<CursorSettings> onSuccess;
    private final CursorSettings targetConfig;
    private final Screen parent;
    private CursorSettings.CursorSprite targetCursor;
    private PreviewWidget previewWidget;

    public CursorEditScreen(Screen parent, CursorSettings cursorConfig, Consumer<CursorSettings> onSuccess) {
        super(Component.empty());
        this.parent = parent;
        this.onSuccess = onSuccess;
        this.targetConfig = cursorConfig.clone();
        this.targetCursor = targetConfig.arrow;
    }

    @Override
    public void onClose() {
        VF.setScreen(parent);
    }

    private int centerX;
    private int centerY;


    @Override
    protected void init() {
        centerX = width / 2;
        centerY = height / 2;

        super.init();

        Function<Boolean, Component> enableText = (Boolean b) -> Component.translatable("customcursor.edit.enabled." + b);
        var toggleWidget = new Button.Builder(enableText.apply(this.targetConfig.enabled), (buttonWidget) -> {
            this.targetConfig.enabled = !targetConfig.enabled;
            buttonWidget.setMessage(enableText.apply(this.targetConfig.enabled));
        });

        Function<Boolean, Component> dynamicText = (Boolean b) -> Component.translatable("customcursor.edit.dynamic." + b);
        var dynmicWidget = new Button.Builder(dynamicText.apply(this.targetConfig.isDynamicEnabled()), (buttonWidget) -> {
            this.targetConfig.setDynamicEnabled(!targetConfig.isDynamicEnabled());
            if (!targetConfig.isDynamicEnabled()) {
                targetCursor = targetConfig.arrow;
            }
            buttonWidget.setMessage(dynamicText.apply(this.targetConfig.isDynamicEnabled()));
        });

        var save__Button = new Button.Builder(Component.translatable("customcursor.edit.save"), this::save);
        var cancelButton = new Button.Builder(Component.translatable("customcursor.edit.exit"), (f) -> onClose());
        var folderButton = new Button.Builder(Component.translatable("customcursor.edit.folder"), (b) -> {
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

        int previewSize = 128;
        var startColumnPos = centerX - (columnCount * columnSize) / 2;
        var startRowPos = centerY - (rowCount * rowSize + previewSize)/2;

        var rows = new int[100];
        var columns = new int[columnCount];

        var padding = 2;
        var sizePadding = padding * 2;

        for (int i = 0; i < 100; i++) {
            rows[i] = startRowPos + rowSize * i + padding;
        }
        for (int i = 0; i < columnCount; i++) {
            columns[i] = startColumnPos + columnSize * i + padding;
        }

        int columnSizePadding = columnSize - sizePadding;
        int twoColumnSizePadding = columnSize * 2 - sizePadding;
        int rowSizePadding = rowSize - sizePadding;

        int previewPosX = (width - previewSize) / 2;
        int previewPosY = rows[rowCount];


        toggleWidget = toggleWidget.bounds(columns[0], rows[0], columnSizePadding, rowSizePadding);
        dynmicWidget = dynmicWidget.bounds(columns[1], rows[0], columnSizePadding, rowSizePadding);
        folderButton = folderButton.bounds(columns[0] + twoColumnSizePadding - rowSizePadding, rows[2], rowSizePadding, rowSizePadding);
        save__Button = save__Button.bounds(columns[0], rows[3], columnSizePadding, rowSizePadding);
        cancelButton = cancelButton.bounds(columns[1], rows[3], columnSizePadding, rowSizePadding);

        var maxsize = 256.;
        var imagePathField = new EditBox(minecraft.font, columns[0], rows[2], twoColumnSizePadding - rowSize, rowSizePadding, Component.empty());
        var sizeSlider = new SliderWidget(columns[0], rows[1], twoColumnSizePadding, rowSizePadding, Component.translatable("customcursor.edit.size"), this.targetConfig.size, 8, maxsize, 256-8);
        var xPosSlider = new SliderWidget(columns[0], rows[2], columnSizePadding, rowSizePadding, Component.translatable("customcursor.edit.x"), this.targetCursor.x, 0., 1., 64);
        var yPosSlider = new SliderWidget(columns[1], rows[2], columnSizePadding, rowSizePadding, Component.translatable("customcursor.edit.y"), this.targetCursor.y, 0., 1., 64);

        xPosSlider.setResponder(d -> this.targetCursor.x = (float) (double) d);
        yPosSlider.setResponder(d -> this.targetCursor.y = (float) (double) d);
        sizeSlider.setResponder(d -> this.targetConfig.size = Math.max(1,(int) (double) d));

        imagePathField.setMaxLength(512);
        imagePathField.setValue(this.targetCursor.identifier.toString());
        imagePathField.setResponder((s) -> {
            try {
                setIdentifier(Identifier.tryParse(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        addRenderableWidget(toggleWidget.build());
        addRenderableWidget(dynmicWidget.build());
        addRenderableWidget(save__Button.build());
        addRenderableWidget(cancelButton.build());
        addRenderableWidget(folderButton.build());
        addRenderableWidget(imagePathField);
        addRenderableWidget(sizeSlider);
//        addRenderableWidget(xPosSlider);
//        addRenderableWidget(yPosSlider);

        this.previewWidget = new PreviewWidget("", targetCursor, previewPosX, previewPosY, previewSize, (x, y) -> {
            x = Math.round(x * 16) / 16d;
            y = Math.round(y * 16) / 16d;
            xPosSlider.setValue(x);
            yPosSlider.setValue(y);
        });


        var ref = new Object() {
            int count = 0;
        };
        CursorSettings.cursorOrder.forEach((key) -> {
            var value = this.targetConfig.allCursors().get(key);
            final int index = ref.count;

            int size = 16;
            int left = startColumnPos - size - padding;
            int top = rows[0] + index * (size + padding);

            addRenderableWidget(new CursorWidget(key, value, left, top, size, () -> {
                this.targetCursor = value;
                xPosSlider.setValue(value.x);
                yPosSlider.setValue(value.y);
                imagePathField.setValue(value.identifier.toString());
                previewWidget.setSprite(value);
            }) {
                @Override
                public void render(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
                    if (!targetConfig.isDynamicEnabled()) {
                        return;
                    }
                    super.render(context, mouseX, mouseY, deltaTicks);
                }

                @Override
                public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
                    if (!targetConfig.isDynamicEnabled()) {
                        return false;
                    }
                    return super.mouseClicked(click, doubled);

                }
            });

            addRenderableOnly((ctx, x, y, td) -> {
                if (!targetConfig.isDynamicEnabled()) {
                    return;
                }
                var text = Component.translatable("customcursor.cursortype." + key);
                var width = font.width(text);
                ctx.text(font, text, left - width - padding, top + 3, 0xFFFFFFFF);
            });

            ref.count++;
        });

        var rotateCCW_builder = new Button.Builder(Component.literal("⟳"), (buttonWidget) -> {
            this.targetCursor.rotation += 90;
            if (this.targetCursor.rotation >= 360) {
                this.targetCursor.rotation -= 360;
            }
        });
        var rotateCW_builder = new Button.Builder(Component.literal("⟲ "), (buttonWidget) -> {
            this.targetCursor.rotation -= 90;
            if (this.targetCursor.rotation < 0) {
                this.targetCursor.rotation += 360;
            }
        });
        var mirror_builder = new Button.Builder(Component.literal("|"), (buttonWidget) -> {
            if (this.targetCursor.rotation == 0 || this.targetCursor.rotation == 180) {
                this.targetCursor.mirroredX = !this.targetCursor.mirroredX;
            }
            if (this.targetCursor.rotation == 90 || this.targetCursor.rotation == 270) {
                this.targetCursor.mirroredY = !this.targetCursor.mirroredY;
            }
        });

        rotateCW_builder.bounds(previewPosX - 24, previewPosY + previewSize / 2 - 12, 22, 22);
        rotateCCW_builder.bounds(previewPosX + 2 + previewSize, previewPosY + previewSize / 2 - 12, 22, 22);
        mirror_builder.bounds(previewPosX + previewSize / 2 - 11, previewPosY + previewSize + 2, 22, 22);

        addRenderableWidget(rotateCCW_builder.build());
        addRenderableWidget(rotateCW_builder.build());
        addRenderableWidget(mirror_builder.build());

        addRenderableOnly(new BackgroundCheckerboard(this, previewPosX, previewPosY, previewSize));
        addRenderableWidget(previewWidget);
        addRenderableOnly(this::testcursor);
    }


    float time = 0;

    private void testcursor(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
//        context.requestCursor(CursorTypes.CROSSHAIR);
    }

    private void setIdentifier(Identifier identifier) {
        this.targetCursor.identifier = identifier;
    }

    private void save(Button buttonWidget) {
        try {
            onSuccess.accept(targetConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dynamic(Button buttonWidget) {
        targetConfig.setDynamicEnabled(!targetConfig.isDynamicEnabled());
    }


    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        previewWidget.mouseDragged(click, offsetX, offsetY);
        return super.mouseDragged(click, offsetX, offsetY);
    }


    public static CursorEditScreen createCursorEditScreen(Screen parent) {
        return new CursorEditScreen(parent, CustomCursorInit.getConfig().settings, c -> {
            var cfg = new CursorConfigStorage();
            cfg.settings = c.clone();
            CustomCursorInit.setConfig(cfg);
            CustomCursorInit.updateCursors();

        });
    }

}
