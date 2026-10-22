package DateTime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class WorldcupSchedule {
    public static  void main(String[] args) {
        LocalDate matchDate = LocalDate.of(2026, 7, 9);
        LocalDate today = LocalDate.of(2026, 10, 20);

        Period diffDate = Period.between(matchDate, today);

        System.out.println(diffDate.toTotalMonths());

        LocalDateTime kickOff = LocalDateTime.of(2026, 7, 9, 18, 0);
        LocalDateTime endTime = LocalDateTime.of(2026, 7, 9, 22, 30);

        Duration diffTime = Duration.between(kickOff, endTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy, hh:mm");
        System.out.println("Match start: " + kickOff.format(formatter));
        System.out.println("Match length: " + diffTime.toHours() + " hours, " + diffTime.toMinutes() + " minutes");
    }
}
