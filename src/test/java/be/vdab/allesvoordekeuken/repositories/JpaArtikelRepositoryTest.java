package be.vdab.allesvoordekeuken.repositories;

import be.vdab.allesvoordekeuken.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaArtikelRepository.class)
@Sql("/insertArtikelGroep.sql")
@Sql("/insertArtikel.sql")
class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final JpaArtikelRepository repository;
    private final EntityManager manager;
    private static final String ARTIKELS = "artikels";
    private ArtikelGroep artikelGroep1;
    private ArtikelGroep artikelGroep2;


    public JpaArtikelRepositoryTest(JpaArtikelRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    @BeforeEach
    void BeforeEach(){
        artikelGroep1 = new ArtikelGroep("testF");
        artikelGroep2 = new ArtikelGroep("testNF");
    }

    private long idTestFoodArtikel() {
        return super.jdbcTemplate.queryForObject("select id from artikels where naam='testFood'", Long.class);
    }


    private long idTestNonFoodArtikel() {
        return super.jdbcTemplate.queryForObject("select id from artikels where naam='testNonFood'", Long.class);
    }


    @Test
    void findFoodArtikelById() {
        assertThat(repository.findById(idTestFoodArtikel()).get().getNaam()).isEqualTo("testFood");
        assertThat(((FoodArtikel) repository.findById(idTestFoodArtikel()).get()).getHoudbaarheid()).isEqualTo(4);
    }

    @Test
    void findNonFoodArtikelById() {
        assertThat(repository.findById(idTestNonFoodArtikel()).get().getNaam()).isEqualTo("testNonFood");
        assertThat(((NonFoodArtikel) repository.findById(idTestNonFoodArtikel()).get()).getGarantie()).isEqualTo(10);
    }

    @Test
    void findByOnbestaandeId(){
        assertThat(repository.findById(-1)).isNotPresent();
    }

    @Test
    void createFoodArtikel(){
        manager.persist(artikelGroep1);
        Artikel artikel = new FoodArtikel("aardbei", BigDecimal.valueOf(1.7), BigDecimal.valueOf(3.8), artikelGroep1, 2);
        repository.create(artikel);
        manager.flush();
        assertThat(super.countRowsInTableWhere(ARTIKELS, "id="+artikel.getId())).isOne();
    }

    @Test
    void createNonFoodArtikel(){
        manager.persist(artikelGroep2);
        Artikel artikel = new NonFoodArtikel("pleister", BigDecimal.valueOf(2.4), BigDecimal.valueOf(3.2), artikelGroep2, 12);
        repository.create(artikel);
        manager.flush();
        assertThat(super.countRowsInTableWhere(ARTIKELS, "id="+artikel.getId())).isOne();
    }

    @Test
    void findByWoord(){
        assertThat(repository.findByWoord("te")).hasSize(super.countRowsInTableWhere(ARTIKELS, "naam like '%te%'"));
    }

    @Test
    void findByWoordGeeftGesorteerdeLijst(){
        assertThat(repository.findByWoord("e")).hasSize(super.countRowsInTableWhere(ARTIKELS, "naam like '%e%'"))
                .extracting(artikel -> artikel.getNaam().toLowerCase())
                .allSatisfy(naam -> assertThat(naam).contains("e"))
                .isSorted();
        assertThat(repository.findByWoord("e")).extracting(artikel -> artikel.getArtikelGroep().getNaam());
    }

    @Test
    void findByWoordMetNietsGeeftLegeLijst(){
        assertThat(repository.findByWoord("")).isEmpty();
    }

    @Test
    void findByWoordMetNullGaatNiet(){
        assertThatNullPointerException().isThrownBy(() -> repository.findByWoord(null));
    }

    @Test
    void algemenePrijsverhoging(){
        assertThat(repository.algemenePrijsverhoging(BigDecimal.TEN)).isEqualTo(super.countRowsInTable(ARTIKELS));
        assertThat(super.jdbcTemplate.queryForObject("select verkoopprijs from artikels where id=?", BigDecimal.class, idTestFoodArtikel())).isEqualByComparingTo("0.99");
    }

    @Test
    void kortingenLezen(){
        assertThat(repository.findById(idTestFoodArtikel()).get().getKortingen()).containsOnly(new Korting(7, BigDecimal.valueOf(15)));
    }

    @Test
    void artikelGroepLazyLoaded(){
        Artikel artikel = repository.findById(idTestFoodArtikel()).get();
        assertThat(artikel.getArtikelGroep().getNaam()).isEqualTo("test");
    }
}