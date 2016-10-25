/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.umjeravanje.dto;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@XmlRootElement
public class GeneratorCistogZraka {
    @NotNull
    private Uredjaj uredjaj;
    private List<Koncentracija> maksimalniUdjeliPolutanata;

    public Uredjaj getUredjaj() {
        return uredjaj;
    }

    public void setUredjaj(Uredjaj uredjaj) {
        this.uredjaj = uredjaj;
    }


    public List<Koncentracija> getMaksimalniUdjeliPolutanata() {
        return maksimalniUdjeliPolutanata;
    }

    public void setMaksimalniUdjeliPolutanata(List<Koncentracija> maksimalniUdjeliPolutanata) {
        this.maksimalniUdjeliPolutanata = maksimalniUdjeliPolutanata;
    }
    
}
