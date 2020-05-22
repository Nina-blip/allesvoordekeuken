package be.vdab.allesvoordekeuken.repositories;

import be.vdab.allesvoordekeuken.domain.Artikel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaArtikelRepository.class)
@Sql("/insertArtikel.sql")
class JpaArtikelRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final JpaArtikelRepository repository;
    private static final String ARTIKELS = "artikels";
    private Artikel artikel;

    public JpaArtikelRepositoryTest(JpaArtikelRepository repository) {
        this.repository = repository;
    }

    private long idTest() {
        return super.jdbcTemplate.queryForObject("select id from artikels where naam='test'", Long.class);
    }

    @BeforeEach
    void beforeEach(){
        artikel = new Artikel("Aardbei", BigDecimal.valueOf(1.7), BigDecimal.valueOf(3.8));
    }

    @Test
    void findById() {
        assertThat(repository.findById(idTest()).get().getNaam()).isEqualTo("test");
    }

    @Test
    void findByOnbestaandeId(){
        assertThat(repository.findById(-1)).isNotPresent();
    }

    @Test
    void create(){
        repository.create(artikel);
        assertThat(artikel.getId()).isPositive();
        assertThat(super.countRowsInTableWhere(ARTIKELS, "id="+idTest())).isOne();
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
        assertThat(super.jdbcTemplate.queryForObject("select verkoopprijs from artikels where id=?", BigDecimal.class, idTest())).isEqualByComparingTo("0.99");
    }
}