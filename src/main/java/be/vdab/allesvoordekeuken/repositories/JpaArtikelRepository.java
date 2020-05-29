package be.vdab.allesvoordekeuken.repositories;

import be.vdab.allesvoordekeuken.domain.Artikel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaArtikelRepository implements ArtikelRepository {
    private final EntityManager manager;

    public JpaArtikelRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Optional<Artikel> findById(long id) {
        return Optional.ofNullable(manager.find(Artikel.class, id));
    }

    @Override
    public void create(Artikel artikel) {
        manager.persist(artikel);
    }

    @Override
    public List<Artikel> findByWoord(String woord) {
        if (woord.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        woord = "%"+woord+"%";
        return manager.createQuery("select a from Artikel a where a.naam like :woord order by a.naam", Artikel.class).setParameter("woord", woord).setHint("javax.persistence.loadgraph", manager.createEntityGraph(Artikel.MET_ARTIKELGROEPNAAM)).getResultList();
    }

    @Override
    public int algemenePrijsverhoging(BigDecimal percentage) {
        BigDecimal factor = BigDecimal.ONE.add(percentage.divide(BigDecimal.valueOf(100)));
        return manager.createQuery("update Artikel a set a.verkoopprijs = a.verkoopprijs* :factor").setParameter("factor", factor).executeUpdate();
    }
}
