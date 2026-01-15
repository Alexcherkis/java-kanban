package ru.gymmaster.model;

import java.util.Objects;

/**
 * Класс для представления времени начала занятия.
 * Реализует Comparable для удобной сортировки в расписании.
 */
public class TimeOfDay implements Comparable<TimeOfDay> {
    private final int hours;
    private final int minutes;

    public TimeOfDay(int hours, int minutes) {
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Некорректное время: " + hours + ":" + minutes);
        }
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public int compareTo(TimeOfDay other) {
        if (this.hours != other.hours) {
            return Integer.compare(this.hours, other.hours);
        }
        return Integer.compare(this.minutes, other.minutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeOfDay timeOfDay = (TimeOfDay) o;
        return hours == timeOfDay.hours && minutes == timeOfDay.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hours, minutes);
    }
}
