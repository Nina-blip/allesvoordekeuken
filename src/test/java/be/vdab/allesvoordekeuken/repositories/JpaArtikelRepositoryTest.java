package be.vdab.allesvoordekeuken.repositories;

import be.vdab.allesvoordekeuken.domain.Artikel;
import be.vdab.allesvoordekeuken.domain.FoodArtikel;
import be.vdab.allesvoordekeuken.domain.NonFoodArtikel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaArtikelRepository.class)
@Sql("/insertArtikel.sql")
class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final JpaArtikelRepository repository;
    private static final String ARTIKELS = "artikels";


    public JpaArtikelRepositoryTest(JpaArtikelRepository repository) {
        this.repository = repository;
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
        Artikel artikel = new FoodArtikel("aardbei", BigDecimal.valueOf(1.7), BigDecimal.valueOf(3.8), 2);
        repository.create(artikel);
        assertThat(super.countRowsInTableWhere(ARTIKELS, "id="+artikel.getId())).isOne();
    }

    @Test
    void createNonFoodArtikel(){
        Artikel artikel = new NonFoodArtikel("pleister", BigDecimal.valueOf(2.4), BigDecimal.valueOf(3.2), 12);
        repository.create(artikel);
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
}