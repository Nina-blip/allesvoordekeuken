package be.vdab.allesvoordekeuken.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Korting {
    private int vanafAantal;
    private BigDecimal percentage;

    protected Korting() {
    }

    public Korting(int vanafAantal, BigDecimal percentage) {
        this.vanafAantal = vanafAantal;
        this.percentage = percentage;
    }

    public int getVanafAantal() {
        return vanafAantal;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Korting korting = (Korting) o;
        return vanafAantal == korting.vanafAantal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vanafAantal);
    }
}
