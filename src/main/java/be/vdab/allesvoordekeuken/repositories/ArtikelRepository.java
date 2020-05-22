package be.vdab.allesvoordekeuken.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import be.vdab.allesvoordekeuken.domain.Artikel;

public interface ArtikelRepository {
    Optional<Artikel> findById(long id);
    void create(Artikel artikel);
    List<Artikel> findByWoord(String woord);
    int algemenePrijsverhoging(BigDecimal percentage);
}
