package cm.fastrelays.common.domain;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Period {

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    public Period(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        if (Objects.equals(startDate, endDate)) {
            throw new IllegalArgumentException("Start date and end date cannot be the same");
        }

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        if (endDate == null) {
            return !now.isBefore(startDate);
        }
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    public boolean contains(LocalDateTime dateTime) {
        if (endDate == null) {
            return !dateTime.isBefore(startDate);
        }
        return !dateTime.isBefore(startDate) && !dateTime.isAfter(endDate);
    }

    public boolean overlapsWith(Period other) {
        if (other == null) {
            return false;
        }

        if (this.endDate == null && other.endDate == null) {
            return true;
        }

        if (this.endDate == null) {
            return !other.startDate.isAfter(this.startDate) ||
                    (other.endDate != null && !other.endDate.isBefore(this.startDate));
        }

        if (other.endDate == null) {
            return !other.startDate.isAfter(this.endDate);
        }

        return !this.endDate.isBefore(other.startDate) && !other.endDate.isBefore(this.startDate);
    }
}
