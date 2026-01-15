package ru.gymmaster.service;

import ru.gymmaster.model.Coach;
import ru.gymmaster.model.DayOfWeek;
import ru.gymmaster.model.TimeOfDay;
import ru.gymmaster.model.TrainingSession;

import java.util.*;
import java.util.stream.Collectors;

public class Timetable {

    // Храним тренировки по дням недели
    // Используем EnumMap, потому что это эффективно для перечислений
    private final Map<DayOfWeek, List<TrainingSession>> scheduleByDay;

    // Сделал отдельную мапу для быстрого поиска по времени, 
    // чтобы сразу доставать нужные тренировки (сложность O(1))
    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> scheduleByTime;

    public Timetable() {
        this.scheduleByDay = new EnumMap<>(DayOfWeek.class);
        this.scheduleByTime = new EnumMap<>(DayOfWeek.class);
        
        // Сразу создаем пустые списки для каждого дня, чтобы потом не проверять на null
        for (DayOfWeek day : DayOfWeek.values()) {
            scheduleByDay.put(day, new ArrayList<>());
            scheduleByTime.put(day, new HashMap<>());
        }
    }

    // Метод добавления новой тренировки
    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        // Добавляем в основной список дня
        List<TrainingSession> sessions = scheduleByDay.get(day);
        sessions.add(trainingSession);
        
        // Сортируем список по времени (через Comparable в TrainingSession)
        Collections.sort(sessions);

        // Обновляем наш "индекс" для поиска по времени
        Map<TimeOfDay, List<TrainingSession>> timeMap = scheduleByTime.get(day);
        if (!timeMap.containsKey(time)) {
            timeMap.put(time, new ArrayList<>());
        }
        timeMap.get(time).add(trainingSession);
    }

    // Возвращаем все тренировки за день. Они уже отсортированы при добавлении
    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return scheduleByDay.get(dayOfWeek);
    }

    // Быстрый поиск тренировок по времени и дню
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        List<TrainingSession> result = scheduleByTime.get(dayOfWeek).get(timeOfDay);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }

    // Считаем сколько тренировок у каждого тренера для зарплаты
    public List<Map.Entry<Coach, Integer>> getCountByCoaches() {
        Map<Coach, Integer> counts = new HashMap<>();

        // Проходимся по всем тренировкам во всех днях
        for (List<TrainingSession> sessions : scheduleByDay.values()) {
            for (TrainingSession s : sessions) {
                Coach coach = s.getCoach();
                counts.put(coach, counts.getOrDefault(coach, 0) + 1);
            }
        }

        // Сортируем: те, у кого больше тренировок, идут первыми
        return counts.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList());
    }
}
