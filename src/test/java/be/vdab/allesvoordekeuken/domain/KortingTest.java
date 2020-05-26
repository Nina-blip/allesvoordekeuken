package be.vdab.allesvoordekeuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class KortingTest {
    private Korting korting1, nogEensKorting1, korting2;

    @BeforeEach
    void beforeEach(){
        korting1 = new Korting(5, BigDecimal.TEN);
        nogEensKorting1 = new Korting(5, BigDecimal.TEN);
        korting2 = new Korting(10, BigDecimal.valueOf(25));
    }

    @Test
    void gelijkeKortingengevenTrue(){
        assertThat(korting1).isEqualTo(nogEensKorting1);
    }

    @Test
    void OngelijkeKortingenGevenFalse(){
        assertThat(korting1).isNotEqualTo(korting2);
    }

    @Test
    void eenKortingGelijkAanNullMislukt(){
        assertThat(korting1).isNotEqualTo(null);
    }

    @Test
    void gelijkeKortingenHebbenDezelfdeHashcode(){
        assertThat(korting1).hasSameHashCodeAs(nogEensKorting1);
    }

}