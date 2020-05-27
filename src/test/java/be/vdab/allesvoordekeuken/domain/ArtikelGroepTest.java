package be.vdab.allesvoordekeuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;

class ArtikelGroepTest {
    private Artikel artikel1;
    private Artikel artikel2;
    private ArtikelGroep artikelGroep1;
    private ArtikelGroep artikelGroep2;


    @BeforeEach
    void BeforeEach(){
        artikelGroep1 = new ArtikelGroep("test1");
        artikelGroep2 = new ArtikelGroep("test2");
        artikel1 = new FoodArtikel("testF", BigDecimal.ONE, BigDecimal.TEN, artikelGroep1, 2);
        artikel2 = new NonFoodArtikel("testNF", BigDecimal.ONE, BigDecimal.TEN, artikelGroep1, 8);

    }

    @Test
    void artikel1BehoortTotArtikelGroep1(){
        assertThat(artikelGroep1.getArtikels()).contains(artikel1);
        assertThat(artikel1.getArtikelGroep()).isEqualTo(artikelGroep1);
    }

    @Test
    void artikel1VerhuistNaarArtikelGroep2(){
        assertThat(artikelGroep2.add(artikel1)).isTrue();
        assertThat(artikelGroep2.getArtikels()).containsOnly(artikel1);
        assertThat(artikelGroep1.getArtikels()).containsOnly(artikel2);
    }

    @Test
    void JeKanGeenNullToevoegenalsArtikel(){
        assertThatNullPointerException().isThrownBy(()->artikelGroep1.add(null));
    }
}