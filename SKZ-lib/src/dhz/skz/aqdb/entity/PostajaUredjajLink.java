/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "program_uredjaj_link", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostajaUredjajLink.findAll", query = "SELECT p FROM ProgramUredjajLink p"),
    @NamedQuery(name = "PostajaUredjajLink.findById", query = "SELECT p FROM ProgramUredjajLink p WHERE p.id = :id"),
    @NamedQuery(name = "PostajaUredjajLink.findByPostaja", query = "SELECT p FROM ProgramUredjajLink p WHERE p.id = :id"),
    @NamedQuery(name = "PostajaUredjajLink.findByUredjaj", query = "SELECT p FROM ProgramUredjajLink p WHERE p.id = :id"),
    @NamedQuery(name = "PostajaUredjajLink.findByVrijemePostavljanja", query = "SELECT p FROM ProgramUredjajLink p WHERE p.vrijemePostavljanja = :vrijemePostavljanja"),
    @NamedQuery(name = "PostajaUredjajLink.findByVrijemeUklanjanja", query = "SELECT p FROM ProgramUredjajLink p WHERE p.vrijemeUklanjanja = :vrijemeUklanjanja")})
public class PostajaUredjajLink implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(name = "vrijeme_postavljanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemePostavljanja;
    @Column(name = "vrijeme_uklanjanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemeUklanjanja;
    @JoinColumn(name = "uredjaj_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Uredjaj uredjajId;
    @JoinColumn(name = "postaja_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Postaja postajaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usporedno_mjerenje", nullable = false)
    private short usporednoMjerenje;


    public PostajaUredjajLink() {
    }

    public PostajaUredjajLink(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVrijemePostavljanja() {
        return vrijemePostavljanja;
    }

    public void setVrijemePostavljanja(Date vrijemePostavljanja) {
        this.vrijemePostavljanja = vrijemePostavljanja;
    }

    public Date getVrijemeUklanjanja() {
        return vrijemeUklanjanja;
    }

    public void setVrijemeUklanjanja(Date vrijemeUklanjanja) {
        this.vrijemeUklanjanja = vrijemeUklanjanja;
    }

    public Uredjaj getUredjajId() {
        return uredjajId;
    }

    public void setUredjajId(Uredjaj uredjajId) {
        this.uredjajId = uredjajId;
    }

    public Postaja getPostajaId() {
        return postajaId;
    }

    public void setPostajaId(Postaja postajaId) {
        this.postajaId = postajaId;
    }

    public short getUsporednoMjerenje() {
        return usporednoMjerenje;
    }

    public void setUsporednoMjerenje(short usporednoMjerenje) {
        this.usporednoMjerenje = usporednoMjerenje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PostajaUredjajLink)) {
            return false;
        }
        PostajaUredjajLink other = (PostajaUredjajLink) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PostajaUredjajLink[ id=" + id + " ]";
    }
    
}
