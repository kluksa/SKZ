/*
 * Copyright (C) 2016 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.StoredProcedureParameter;

/**
 *
 * @author kraljevic
 */
@SqlResultSetMapping (
        name = "PrekoracenjaUpozorenjaMapping",
        entities =
                @EntityResult(
                        entityClass = PrekoracenjaUpozorenjaResult.class,
                        fields={
                            @FieldResult(name = "programMjerenjaId", column = "program_mjerenja_id"),
                            @FieldResult(name = "brojPojavljivanja", column = "broj_pojavljivanja"),
                            @FieldResult(name = "minimalnaVrijednost", column = "minimalna_vrijednost")
                                
                        }
                )
)
@NamedStoredProcedureQuery(
        name = "PrekoracenjaUpozorenja",
        resultSetMappings = {
            "PrekoracenjaUpozorenjaMapping"
        },
        procedureName = "prekoracenja_upozorenja",
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "obavijest_id", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "vrijeme", type = Date.class)
        }
)
@Entity
public class PrekoracenjaUpozorenjaResult implements Serializable {
    @Id
    private Integer programMjerenjaId;
    private Integer brojPojavljivanja;
    private Double minimalnaVrijednost;
    @ManyToOne
    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id")    
    private ProgramMjerenja programMjerenja;

    public ProgramMjerenja getProgramMjerenja() {
        return programMjerenja;
    }

    public void setProgramMjerenja(ProgramMjerenja programMjerenja) {
        this.programMjerenja = programMjerenja;
    }


    public Integer getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(Integer programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public Integer getBrojPojavljivanja() {
        return brojPojavljivanja;
    }

    public void setBrojPojavljivanja(Integer brojPojavljivanja) {
        this.brojPojavljivanja = brojPojavljivanja;
    }

    public Double getMinimalnaVrijednost() {
        return minimalnaVrijednost;
    }

    public void setMinimalnaVrijednost(Double minimalnaVrijednost) {
        this.minimalnaVrijednost = minimalnaVrijednost;
    }
    
}
