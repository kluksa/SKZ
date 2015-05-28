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
package dhz.skz;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

/**
 *
 * @author kraljevic
 */
public abstract class Scheduler {
    private static final Logger log = Logger.getLogger(Scheduler.class.getName());
    @Resource
    protected TimerService timerService;
    protected final String TIMER_NAZIV;
    
    protected int minutaPokretanja;

    public Scheduler(String timer_naziv, int minutaPokretanja) {
        this.TIMER_NAZIV = timer_naziv;
        this.minutaPokretanja = minutaPokretanja;
    }

    @PostConstruct
    public void init() {
        schedule(minutaPokretanja);
    }

    public void schedule(int minutaPokretanja) {
        this.minutaPokretanja = minutaPokretanja;
        zaustavi();
        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.hour("*").minute(minutaPokretanja).second("0");
        timerService.createCalendarTimer(scheduleExpression, new TimerConfig(TIMER_NAZIV, true));
        log.log(Level.INFO, "Stvoren {0} timer", new Object[]{TIMER_NAZIV});
    }

    public void zaustavi() {
        if (timerService.getTimers() != null) {
            for (Timer timer : timerService.getTimers()) {
                if (timer.getInfo().equals(TIMER_NAZIV)) {
                    timer.cancel();
                    log.log(Level.INFO,"Programatic Timer got stopped succesfully");
                }
            }
        }
    }
    
    public boolean isAktivan(){
        if (timerService.getTimers() != null) {
            for (Timer timer : timerService.getTimers()) {
                if (timer.getInfo().equals(TIMER_NAZIV)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getMinutaPokretanja() {
        return minutaPokretanja;
    }
    
    public abstract void pokreni();
    
}
