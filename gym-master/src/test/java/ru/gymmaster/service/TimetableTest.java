package ru.gymmaster.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gymmaster.model.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    private Timetable timetable;
    private Coach vasyiliev;
    private Group childAcrobatics;
    private Group adultAcrobatics;

    @BeforeEach
    void setUp() {
        timetable = new Timetable();
        vasyiliev = new Coach("Васильев", "Николай", "Сергеевич");
        childAcrobatics = new Group("Акробатика для детей", Age.CHILD, 60);
        adultAcrobatics = new Group("Акробатика для взрослых", Age.ADULT, 90);
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        TrainingSession singleTrainingSession = new TrainingSession(childAcrobatics, vasyiliev,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(), "Должно быть одно занятие в понедельник");
        assertEquals(singleTrainingSession, mondaySessions.get(0));

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(), "Во вторник занятий быть не должно");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        TrainingSession thursdayAdult = new TrainingSession(adultAcrobatics, vasyiliev,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        TrainingSession mondayChild = new TrainingSession(childAcrobatics, vasyiliev,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChild = new TrainingSession(childAcrobatics, vasyiliev,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChild = new TrainingSession(childAcrobatics, vasyiliev,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(thursdayAdult);
        timetable.addNewTrainingSession(mondayChild);
        timetable.addNewTrainingSession(thursdayChild);
        timetable.addNewTrainingSession(saturdayChild);

        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        // Проверка порядка: сначала в 13:00, потом в 20:00
        assertEquals(13, thursdaySessions.get(0).getTimeOfDay().getHours());
        assertEquals(20, thursdaySessions.get(1).getTimeOfDay().getHours());

        assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        TrainingSession singleTrainingSession = new TrainingSession(childAcrobatics, vasyiliev,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, sessions.size());
        assertEquals(singleTrainingSession, sessions.get(0));

        List<TrainingSession> noSessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(noSessions.isEmpty(), "В 14:00 занятий быть не должно");
    }

    // --- ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ ---

    @Test
    void testGetCountByCoaches() {
        Coach semenov = new Coach("Семенов", "Виктор", "Кириллови");
        
        // Васильев - 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(childAcrobatics, vasyiliev, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childAcrobatics, vasyiliev, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childAcrobatics, vasyiliev, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));
        
        // Семенов - 1 тренировка
        timetable.addNewTrainingSession(new TrainingSession(adultAcrobatics, semenov, DayOfWeek.TUESDAY, new TimeOfDay(19, 0)));

        List<Map.Entry<Coach, Integer>> counts = timetable.getCountByCoaches();
        
        assertEquals(2, counts.size());
        assertEquals(vasyiliev, counts.get(0).getKey());
        assertEquals(3, counts.get(0).getValue());
        assertEquals(semenov, counts.get(1).getKey());
        assertEquals(1, counts.get(1).getValue());
    }

    @Test
    void testSameTimeMultipleSessions() {
        Coach izmaylov = new Coach("Измайлов", "Кирилл", "Владимирович");
        TrainingSession session1 = new TrainingSession(childAcrobatics, vasyiliev, DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(adultAcrobatics, izmaylov, DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        assertEquals(2, sessions.size(), "Должно быть 2 параллельных занятия в одно время");
    }

    @Test
    void testSortingOrderComplex() {
        // Добавляем занятия вразнобой по времени
        timetable.addNewTrainingSession(new TrainingSession(childAcrobatics, vasyiliev, DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childAcrobatics, vasyiliev, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childAcrobatics, vasyiliev, DayOfWeek.FRIDAY, new TimeOfDay(14, 30)));

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);
        
        assertEquals(10, sessions.get(0).getTimeOfDay().getHours());
        assertEquals(14, sessions.get(1).getTimeOfDay().getHours());
        assertEquals(18, sessions.get(2).getTimeOfDay().getHours());
    }
}
