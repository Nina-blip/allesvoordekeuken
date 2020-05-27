package be.vdab.allesvoordekeuken.domain;


import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Collections;

@Entity
@Table(name = "artikelgroepen")
public class ArtikelGroep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;

    @OneToMany(mappedBy = "artikelGroep")
    @OrderBy("naam")
    private Set<Artikel> artikels;

    protected ArtikelGroep() {
    }

    public ArtikelGroep(String naam) {
        this.naam = naam;
        this.artikels = new LinkedHashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public Set<Artikel> getArtikels() {
        return Collections.unmodifiableSet(artikels);
    }

    public boolean add(Artikel artikel) {
        boolean toegevoegd = artikels.add(artikel);
        ArtikelGroep oudeArtikelgroep = artikel.getArtikelGroep();
        if (oudeArtikelgroep != null && oudeArtikelgroep != this){
            oudeArtikelgroep.artikels.remove(artikel);
        }
        if (this != oudeArtikelgroep){
            artikel.setArtikelGroep(this);
        }
        return toegevoegd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtikelGroep)) return false;
        ArtikelGroep that = (ArtikelGroep) o;
        return Objects.equals(naam.toUpperCase(), that.naam.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam.toUpperCase());
    }
}
