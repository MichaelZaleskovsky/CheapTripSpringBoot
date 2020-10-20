package mz.project.cheaptrip.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDb {

    @Id
    private long id;

    @Column(length=100)
    private String name;

    @Column(length=10)
    private String code;

    @Column(length=10)
    private String symbol;

    @Column(length=20)
    private String r2rSymbol;

    @Column(scale=2)
    private BigDecimal oneEuroRate;

}
