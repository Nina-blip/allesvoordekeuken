package be.vdab.allesvoordekeuken.repositories;

import java.util.Optional;
import be.vdab.allesvoordekeuken.domain.Artikel;

public interface ArtikelRepository {
    Optional<Artikel> findById(long id);
}
