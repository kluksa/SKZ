/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function chartExtender() {
    // this = chart widget instance        
    // this.cfg = options        
//    this.cfg.grid = {
//        background: 'transparent',
//        gridLineColor: '#303030',
//        drawBorder: false
//    };
//    this.cfg.seriesDefaults.markerOptions  = {
//        size : 30,
//    }
    this.cfg.seriesDefaults = {
        lineWidth: 1,
        markerOptions: {
            size: 5
        }
    };
}
