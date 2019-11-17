package fxplatform.monitorservice.service.dto;

import lombok.Data;

import java.math.BigDecimal;

/*
 * eodhistoricaldata.com real time currency update DTO.
 */

@Data
public class FxPairDto {
    private String code;
    private Long timestamp;
    private Long gmtoffset;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal low;
    private BigDecimal high;
}
