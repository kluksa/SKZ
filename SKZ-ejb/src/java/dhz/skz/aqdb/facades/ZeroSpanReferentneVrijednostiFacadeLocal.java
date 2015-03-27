/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.facades;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpanReferentneVrijednosti;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kraljevic
 */
@Local
public interface ZeroSpanReferentneVrijednostiFacadeLocal {

    void create(ZeroSpanReferentneVrijednosti zeroSpanReferentneVrijednosti);

    void edit(ZeroSpanReferentneVrijednosti zeroSpanReferentneVrijednosti);

    void remove(ZeroSpanReferentneVrijednosti zeroSpanReferentneVrijednosti);

    ZeroSpanReferentneVrijednosti find(Object id);

    List<ZeroSpanReferentneVrijednosti> findAll();

    List<ZeroSpanReferentneVrijednosti> findRange(int[] range);

    int count();

    List<ZeroSpanReferentneVrijednosti> findZadnjiPrije(final ProgramMjerenja program,  final Date uPrimjeniPrije);
    
}
