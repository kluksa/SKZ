/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.umjeravanje.dto;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@XmlRootElement
public class Dilucija {

    @NotNull
    private Uredjaj uredjaj;
    private MFC mfc1;
    private MFC mfc2;
    private MFC o3;

    public MFC getMfc1() {
        return mfc1;
    }

    public void setMfc1(MFC mfc1) {
        this.mfc1 = mfc1;
    }

    public MFC getMfc2() {
        return mfc2;
    }

    public void setMfc2(MFC mfc2) {
        this.mfc2 = mfc2;
    }

    public MFC getO3() {
        return o3;
    }

    public void setO3(MFC o3) {
        this.o3 = o3;
    }

}
