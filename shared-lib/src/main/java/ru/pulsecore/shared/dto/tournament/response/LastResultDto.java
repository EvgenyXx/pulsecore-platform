package ru.pulsecore.shared.dto.tournament.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LastResultDto implements Serializable {
    private String date;
    private Double amount;
    private String tournamentLink;


}