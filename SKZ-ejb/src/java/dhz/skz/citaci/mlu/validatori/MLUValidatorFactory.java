/*
 * Copyright (C) 2015 kraljevic
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
package dhz.skz.citaci.mlu.validatori;

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.validatori.ValidatorFactoryFF;
import dhz.skz.validatori.Validator;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author kraljevic
 */
public class MLUValidatorFactory extends ValidatorFactoryFF {
    private final Validator validator;

    public MLUValidatorFactory(Collection<ProgramMjerenja> programi) {
        super(programi);
        this.validator = new MLUValidator();
    }


    @Override
    public Validator getValidator(ProgramMjerenja pm, Date vrijeme) {
        validator.setPodaciUmjeravanja(getA(pm, vrijeme), getB(pm, vrijeme), getDL(pm, vrijeme), getOpseg(pm, vrijeme));
        return validator;
    }
}