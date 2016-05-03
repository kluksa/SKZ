/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Komentar.findAll", query = "SELECT k FROM Komentar k"),
    @NamedQuery(name = "Komentar.findByProgramPocetakKraj", query = "SELECT k FROM Komentar k WHERE k.programMjerenjaId = :programMjerenjaId AND k.pocetak < :kraj AND k.kraj > :pocetak "),
    @NamedQuery(name = "Komentar.findByTekst", query = "SELECT k FROM Komentar k WHERE k.tekst = :tekst")})
public class Komentar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "komentar_id_seq",
            sequenceName = "komentar_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "komentar_id_seq")
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pocetak;
    
    @Basic(optional = false)
    @NotNull
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date kraj;

    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull
    private ProgramMjerenja programMjerenjaId;
    

    @Size(max = 510)
    @NotNull
    private String tekst;

    public Komentar() {
    }

    public Integer getId() {
        return id;
    }

    public Date getPocetak() {
        return pocetak;
    }

    public void setPocetak(Date pocetak) {
        this.pocetak = pocetak;
    }

    public Date getKraj() {
        return kraj;
    }

    public void setKraj(Date kraj) {
        this.kraj = kraj;
    }

    public ProgramMjerenja getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(ProgramMjerenja programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.pocetak);
        hash = 29 * hash + Objects.hashCode(this.kraj);
        hash = 29 * hash + Objects.hashCode(this.programMjerenjaId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Komentar other = (Komentar) obj;
        if (!Objects.equals(this.pocetak, other.pocetak)) {
            return false;
        }
        if (!Objects.equals(this.kraj, other.kraj)) {
            return false;
        }
        if (!Objects.equals(this.programMjerenjaId, other.programMjerenjaId)) {
            return false;
        }
        return true;
    }
}
