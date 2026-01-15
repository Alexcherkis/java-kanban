package ru.gymmaster.model;

/**
 * Класс, представляющий группу тренировок.
 */
public class Group {
    private final String title;
    private final Age age;
    private final int duration;

    public Group(String title, Age age, int duration) {
        this.title = title;
        this.age = age;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public Age getAge() {
        return age;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d мин)", title, age, duration);
    }
}
