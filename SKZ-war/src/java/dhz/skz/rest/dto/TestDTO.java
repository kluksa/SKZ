/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@XmlRootElement
public class TestDTO implements Serializable {
    
    private String string ="ABBBBB";

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    
}
