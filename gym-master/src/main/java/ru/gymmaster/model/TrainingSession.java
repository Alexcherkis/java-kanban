package ru.gymmaster.model;

/**
 * Класс, представляющий одно конкретное занятие в расписании.
 */
public class TrainingSession implements Comparable<TrainingSession> {
    private final Group group;
    private final Coach coach;
    private final DayOfWeek dayOfWeek;
    private final TimeOfDay timeOfDay;

    public TrainingSession(Group group, Coach coach, DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        this.group = group;
        this.coach = coach;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    public Group getGroup() {
        return group;
    }

    public Coach getCoach() {
        return coach;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    @Override
    public int compareTo(TrainingSession other) {
        return this.timeOfDay.compareTo(other.timeOfDay);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (Тренер: %s)", dayOfWeek, timeOfDay, group.getTitle(), coach.getFullName());
    }
}
