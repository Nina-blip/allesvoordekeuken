package be.vdab.allesvoordekeuken.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("NF")
public class NonFoodArtikel extends Artikel {
    private int garantie;

    protected NonFoodArtikel() {
    }

    public NonFoodArtikel(String naam, BigDecimal aankoopprijs, BigDecimal verkoopprijs, ArtikelGroep artikelGroep, int garantie) {
        super(naam, aankoopprijs, verkoopprijs, artikelGroep);
        this.garantie = garantie;
    }

    public int getGarantie() {
        return garantie;
    }
}
