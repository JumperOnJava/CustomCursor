package io.github.jumperonjava.customcursor.editor;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

/**
 * Slider widget for general use.
 */
public class SliderWidget extends net.minecraft.client.gui.components.AbstractSliderButton {
    private final int steps;
    private Consumer<Double> changedListener;
    private Component text;
    public double min, max;

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param text
     * @param minValue
     * @param maxValue
     * @param initialValue
     * @param steps
     */
    public SliderWidget(int x, int y, int width, int height, Component text, double initialValue, double minValue, double maxValue, int steps) {
        super(x, y, width, height, text, Mth.map(initialValue,minValue,maxValue,0,1));
        this.min=minValue;
        this.max=maxValue;
        this.text=text;
        this.steps=steps;
        updateMessage();
    }

    @Override
    public void updateMessage() {
        var mappedValue = getMappedValue();
        var stringValue = mappedValue == (int)mappedValue
                ? String.valueOf((int)mappedValue)
                : String.valueOf(mappedValue);
        this.setMessage(this.text.copy().append(" ").append(stringValue));
    }
    private double getMappedValue(){
        var value = this.value;
        value = Math.floor(value*steps)/steps;
        value = Mth.lerp(value, this.min, this.max);
        return value;
    }
    @Override
    protected void applyValue() {
        if(changedListener==null)
            return;
        changedListener.accept(getMappedValue());
    }

    @Override
    public void setValue(double newValue) {
        super.setValue(newValue);
    }

    public void setResponder(Consumer<Double> changedListener) {
        this.changedListener = changedListener;
    }
}
