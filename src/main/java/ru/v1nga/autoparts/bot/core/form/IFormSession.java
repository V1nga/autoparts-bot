package ru.v1nga.autoparts.bot.core.form;

public interface IFormSession {
    String getCurrentStep();
    void setCurrentStep(String step);
    boolean isCompleted();
}
