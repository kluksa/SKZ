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
package dhz.skz.citaci.iox.validatori;

import dhz.skz.validatori.Validator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kraljevic
 */
public class IoxValidatorFactory {
    private final Map<String, Validator> mapa;
    private final Validator defaultValidator;
    
    public IoxValidatorFactory(){
        mapa = new HashMap<>();
        mapa.put("APSA-370", new APSA370Validator());
        mapa.put("APMA-370", new APMA370Validator());
        mapa.put("APNA-370", new APNA370Validator());
        mapa.put("APOA-370", new APOA370Validator());
        defaultValidator = new IoxValidator();
    }
    
    public Validator getValidator(String device) {
        if ( mapa.containsKey(device)) 
            return mapa.get(device);
        else 
            return defaultValidator;
    }
    
}
