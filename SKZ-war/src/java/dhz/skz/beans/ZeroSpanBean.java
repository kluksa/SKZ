/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.beans;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.entity.ZeroSpanReferentneVrijednosti;
import dhz.skz.aqdb.facades.ZeroSpanFacadeRemote;
import dhz.skz.aqdb.facades.ZeroSpanReferentneVrijednostiFacadeRemote;
import dhz.skz.rs.dto.ZeroSpanDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class ZeroSpanBean {
    @EJB
    private ZeroSpanReferentneVrijednostiFacadeRemote zeroSpanReferentneVrijednostiFacade;
    @EJB
    private ZeroSpanFacadeRemote zeroSpanFacadeB;
    
    
    public List<ZeroSpanDTO> getZeroSpan(ProgramMjerenja pm, Date vrijemeOd, Date vrijemeDo) {
            
        List<ZeroSpan> zeroSpan = zeroSpanFacadeB.getZeroSpan(pm, vrijemeOd, vrijemeDo);
        List<ZeroSpanReferentneVrijednosti> refZS = zeroSpanReferentneVrijednostiFacade.findZadnjiPrije(pm, vrijemeDo);
        
        NavigableMap<Date, ZeroSpanReferentneVrijednosti> refZ = new TreeMap<>();
        NavigableMap<Date, ZeroSpanReferentneVrijednosti> refS = new TreeMap<>();
        NavigableMap<Date, ZeroSpan> zero = new TreeMap<>();
        
        
        float deltaz = pm.getMetoda().getZeroDriftAbs();
        float deltas = pm.getMetoda().getSpanDriftRelativ();
        
        for (ZeroSpanReferentneVrijednosti rzs : refZS){
            if (rzs.getVrsta().contains("Z")) {
                refZ.put(rzs.getPocetakPrimjene(), rzs);
            } else if ( rzs.getVrsta().contains("S")) {
                refS.put(rzs.getPocetakPrimjene(), rzs);
            } 
        }
        
        for (ZeroSpan zs : zeroSpan) {
            if ( zs.getVrsta().contains("Z")) {
                zero.put(zs.getVrijeme(), zs);
            } 
        }
        
        List<ZeroSpanDTO> zsl = new ArrayList<>();
        
        for (ZeroSpan zs : zeroSpan) {
            ZeroSpanDTO  zz = new ZeroSpanDTO();
            zz.setVrijeme(zs.getVrijeme().getTime());
            zz.setVrijednost(zs.getVrijednost());
            
            if ( zs.getVrsta().contains("Z")) {
                zz.setVrsta('Z');
                if ( refZ.floorEntry(zs.getVrijeme()) != null ) {
                    zz.setMinDozvoljeno(refZ.floorEntry(zs.getVrijeme()).getValue().getVrijednost()-deltaz);
                    zz.setMaxDozvoljeno(refZ.floorEntry(zs.getVrijeme()).getValue().getVrijednost()+deltaz);
                }
            } else if ( zs.getVrsta().contains("S")){
                zz.setVrsta('S');
                
                Map.Entry<Date, ZeroSpan> najbliziZero = zero.floorEntry(new Date(zs.getVrijeme().getTime()+3600*1000));
                
                if ( najbliziZero!=null && refZ.floorEntry(najbliziZero.getKey())!= null && refS.floorEntry(zs.getVrijeme())!=null){
                    float dz =  najbliziZero.getValue().getVrijednost() - refZ.floorEntry(najbliziZero.getKey()).getValue().getVrijednost();
                    zz.setMinDozvoljeno((1-deltas)*refS.floorEntry(zs.getVrijeme()).getValue().getVrijednost()+dz);
                    zz.setMaxDozvoljeno((1+deltas)*refS.floorEntry(zs.getVrijeme()).getValue().getVrijednost()+dz);
                }
            }
            
            zsl.add(zz);
        }
        return zsl;
    }
}
