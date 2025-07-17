package ru.v1nga.autoparts.bot.core.form;

public abstract class FormSession implements IFormSession {

    private String currentStep;

    @Override
    public String getCurrentStep() {
        return this.currentStep;
    }

    @Override
    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }
}
