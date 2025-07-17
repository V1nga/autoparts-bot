package ru.v1nga.autoparts.bot.forms;

import lombok.Getter;
import lombok.Setter;
import ru.v1nga.autoparts.bot.core.form.FormSession;

@Getter
@Setter
public class SearchFormSession extends FormSession {

    private String name;
    private String age;

    @Override
    public boolean isCompleted() {
        return this.name != null && this.age != null;
    }
}
