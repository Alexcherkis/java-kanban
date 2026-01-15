package ru.gymmaster;

import ru.gymmaster.model.*;
import ru.gymmaster.service.Timetable;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Тестируем проект Gym Master ---");

        Timetable timetable = new Timetable();

        // Добавляем тренеров для теста
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Измайлов", "Кирилл", "Владимирович");
        Coach coach3 = new Coach("Семенов", "Виктор", "Кириллович");

        // Группы
        Group kids = new Group("Акробатика (Дети)", Age.CHILD, 60);
        Group adults = new Group("Акробатика (Взрослые)", Age.ADULT, 90);
        Group yoga = new Group("Йога", Age.ADULT, 60);

        // Наполняем данными
        timetable.addNewTrainingSession(new TrainingSession(kids, coach1, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(adults, coach2, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(kids, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(yoga, coach3, DayOfWeek.MONDAY, new TimeOfDay(18, 0))); 
        timetable.addNewTrainingSession(new TrainingSession(kids, coach1, DayOfWeek.FRIDAY, new TimeOfDay(13, 0)));

        // Проверяем вывод на понедельник
        System.out.println("\nРасписание на понедельник:");
        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        for (TrainingSession s : monday) {
            System.out.println(" -> " + s);
        }

        // Поиск по времени
        System.out.println("\nКто занимается в 18:00 в понедельник?");
        List<TrainingSession> evening = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        for (TrainingSession s : evening) {
            System.out.println(" Нашел: " + s.getGroup().getTitle() + " ведет " + s.getCoach().getSurname());
        }

        // Кто сколько работает
        System.out.println("\nСтатистика по тренерам:");
        List<Map.Entry<Coach, Integer>> stats = timetable.getCountByCoaches();
        for (Map.Entry<Coach, Integer> entry : stats) {
            System.out.println(" " + entry.getKey() + " ведет занятий: " + entry.getValue());
        }

        System.out.println("\nВроде всё работает корректно.");
    }
}
