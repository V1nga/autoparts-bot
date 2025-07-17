package ru.v1nga.autoparts.bot.core.form;

public interface IFormSession {
    String getCurrentStep();
    void setCurrentStep(String currentStep);

    <T> T getData();
    <T> void setData(T t);
}
