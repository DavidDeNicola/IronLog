package org.ironlog.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

public class DashboardDTO {

    private Integer allenamentiSettimana;
    private BigDecimal volumeSettimana;
    private Integer serieSettimana;
    private Integer streak;
    private BigDecimal variazioneVolumePercentuale;

}
