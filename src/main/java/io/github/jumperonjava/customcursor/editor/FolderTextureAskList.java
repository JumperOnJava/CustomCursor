package io.github.jumperonjava.customcursor.editor;

import com.mojang.blaze3d.Blaze3D;
import io.github.jumperonjava.customcursor.util.VF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FolderTextureAskList extends Screen {
    public static final int gap = 2;
    private final TextureFolder folder;
    private final Consumer<Identifier> onSuccess;

    ScrollListWidget list;

    protected Identifier EMPTY_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "empty");
    private List<Identifier> textures = new ArrayList<>();

    private Identifier selectedTexture = EMPTY_TEXTURE;

    private Screen parent;
    private String lastFilter = "";

    public FolderTextureAskList(TextureFolder folder, Consumer<Identifier> onSuccess) {
        super(Component.empty());
        this.onSuccess = onSuccess;
        this.folder = folder;
    }

    public static void ask(FolderTextureAskList askScreen) {
        var client = Minecraft.getInstance();
        var currentScreen = VF.getScreen();
        if (currentScreen == null) {
            VF.setScreen(askScreen);
            return;
        }
        VF.setScreen(askScreen);
        askScreen.parent = currentScreen;
    }
    @Override
    protected void init() {
        // Initializations
        list = new ScrollListWidget(minecraft, width, height - 64, 0, 22, 40);
        var search = new EditBox(minecraft.font, 0, 0, width, 20, Component.empty());
        var accept = new Button.Builder(Component.translatable("customcursor.folder.accept"), b1 -> {
            if (selectedTexture != EMPTY_TEXTURE)
                success(selectedTexture);
        });
        var cancel1 = new Button.Builder(Component.translatable("customcursor.folder.cancel"), b1 -> closeScreen());
        var cancel = new Button.Builder(Component.translatable("customcursor.folder.openfolder"), b -> openCursorFolder());

        accept.bounds(40 + gap, height - 20 - gap, 100, 20);
        cancel1.bounds((int) (140 + gap * 1.5), height - 20 - gap, 100, 20);
        cancel.bounds(240 + gap * 2, height - 20 - gap, 100, 20);

        // Adding Widgets
        refreshListByFilter("");
        addRenderableWidget(list);
        search.setResponder(this::refreshListByFilter);
        addRenderableWidget(search);
        addRenderableWidget(accept.build());
        addRenderableWidget(cancel1.build());
        addRenderableWidget(cancel.build());
        addRenderableOnly(((context, mouseX, mouseY, delta) ->
        {
            var texture = selectedTexture == EMPTY_TEXTURE ? Identifier.fromNamespaceAndPath("minecraft", "textures/item/barrier.png") : selectedTexture;
            TextureWidget.render(context,texture, gap / 2, height - 40 - gap / 2, 40, 40);
        }));

        setTexturesCallback(this::setTextures);
        addRenderableOnly(((context, mouseX, mouseY, delta) -> context.text(
                font,
                Component.translatable("customcursor.folder.selected").append(": ").append(selectedTexture.toString()),
                45,
                height - 30 - 6 - gap / 2,
                0xFFFFFFFF,
                true)));
    }


    private void openCursorFolder() {
        //? if >= 26.3 {
        /*Blaze3D.openPath(folder.path);
        *///?} else {
        Util.getPlatform().openPath(folder.path);
        //?}
    }

    public void setTexturesCallback(Consumer<List<Identifier>> consumer) {
        if (!this.textures.isEmpty())
            consumer.accept(textures);
        folder.redefineTextures(()->{
            this.textures = folder.getTextures();
            consumer.accept(this.textures);
        });

    }

    public void setTextures(List<Identifier> identifiers) {
        this.textures = identifiers;
        refreshListByFilter(lastFilter);
    }

    private void refreshListByFilter(String s) {
        lastFilter = s;
        list.replaceEntries(List.of());
        for (var key : textures) {
            extracted(s, key);
        }
    }

    private void extracted(String s, Identifier key) {
        if (key == null) return;
        var id = key.toString();
        if (!id.toLowerCase().contains(s.toLowerCase()))
            return;
        Identifier finalKey = key;
        var entry = new ScrollListWidget.ScrollListEntry(key, () -> {
            this.selectedTexture = finalKey;
        });
        list.addEntry(entry);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
    }

    public void success(Identifier ret) {
        onSuccess.accept(ret);
        this.closeScreen();
    }

    private void closeScreen() {
        VF.setScreen(this.parent);
    }
}
