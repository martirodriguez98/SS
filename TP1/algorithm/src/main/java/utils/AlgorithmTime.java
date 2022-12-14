package utils;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class AlgorithmTime {
    private LocalDateTime start;
    private LocalDateTime end;

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalTime getTotalTime(){
        return LocalTime.ofNanoOfDay(getEnd().toLocalTime().toNanoOfDay() - getStart().toLocalTime().toNanoOfDay());
    }
}
