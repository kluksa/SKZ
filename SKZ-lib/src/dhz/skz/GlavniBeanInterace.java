/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz;

/**
 *
 * @author kraljevic
 */
public interface GlavniBeanInterace {

    int getMinutaPokretanja();

    boolean isAktivan();

    void pokreni();

    void schedule(int minuti);

    void zaustavi();
    
}
