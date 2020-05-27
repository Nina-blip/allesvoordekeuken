package be.vdab.allesvoordekeuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ArtikelTest {
    private Artikel artikel1;
    private Artikel artikel2;
    private Artikel nogEensArtikel1;
    private ArtikelGroep artikelGroep1;
    private ArtikelGroep artikelGroep2;



    @BeforeEach
    void beforeEach(){
        artikelGroep1 = new ArtikelGroep("test1" );
        artikelGroep2 = new ArtikelGroep("test2");
        artikel1 = new FoodArtikel("testF", BigDecimal.ONE, BigDecimal.TEN, artikelGroep1, 6);
        artikel2 = new NonFoodArtikel("testNF", BigDecimal.ONE, BigDecimal.TEN, artikelGroep1, 8);
        nogEensArtikel1 = new FoodArtikel("testF", BigDecimal.ONE, BigDecimal.TEN, artikelGroep1, 6);
    }

    @Test
    void artikel1BehoortTotArtikelGroep1(){
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep1);
    }

    @Test
    void artikel1VerhuistNaarArtikelGroep2(){
        artikel1.setArtikelGroep(artikelGroep2);
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep2);
        assertThat(artikelGroep2.getArtikels()).containsOnly(artikel1);
        assertThat(artikelGroep1.getArtikels()).containsOnly(artikel2);
    }

    @Test
    void JeKanGeenNullToevoegenAlsArtikelGroep(){
        assertThatNullPointerException().isThrownBy(()-> artikel1.setArtikelGroep(null));
    }

    @Test
    void ArtikelsMetDezelfdeNaamZijnGelijk(){
        assertThat(artikel1).isEqualTo(nogEensArtikel1);
    }

    @Test
    void ArtikelsMetVerschillendeNaamZijnNietGelijk(){
        assertThat(artikel1).isNotEqualTo(artikel2);
    }

    @Test
    void eenArtikelVerschiltVanEenAnderTypeObject(){
        assertThat(artikel1).isNotEqualTo("");
    }

    @Test
    void dezelfdeArtikelsHebbenDezelfdeHashcode(){
        assertThat(artikel1).hasSameHashCodeAs(nogEensArtikel1);
    }

    @Test
    void verschillendeArtikelsKunnenTotDezelfdeArtikelGroepBehoren(){
        assertThat(artikelGroep1.getArtikels()).containsOnly(artikel1, artikel2);
    }
}