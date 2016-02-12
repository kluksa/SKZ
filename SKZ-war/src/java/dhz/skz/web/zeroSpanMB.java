/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.LinearAxis;

/**
 *
 * @author kraljevic
 */
@Named(value = "zeroSpanMB")
@ViewScoped
public class zeroSpanMB implements Serializable {

    @Inject
    private  transient Logger log;
    private boolean novi = false;
    private double minZ;
    private double maxZ;
    private double minS;
    private double maxS;

    /**
     * Creates a new instance of zeroSpanMB
     */
    public zeroSpanMB() {
    }
    @EJB
    private ZeroSpanFacade zeroSpanFacade;

    @EJB
    private PostajaFacade postajaFacade;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Postaja postaja;
    private Integer postajaId;
    private List<Postaja> postaje;
//    private TreeMap<String, Postaja> postaje;

    private ProgramMjerenja selektiraniProgram;
    private Collection<ProgramMjerenja> programiSvi;

    private LineChartModel dateModel;
    private LineChartModel spanModel, zeroModel;

    private String prikaz = "ZS";

    private LineChartSeries zeroS;
    private LineChartSeries spanS;

    public String getPrikaz() {
        return prikaz;
    }

    public void setPrikaz(String prikaz) {
        this.prikaz = prikaz;
    }

    public LineChartModel getDateModel() {
        return dateModel;
    }

    public ZeroSpanFacade getZeroSpanFacade() {
        return zeroSpanFacade;
    }

    public LineChartModel getZeroModel() {
        return zeroModel;
    }

    public LineChartModel getSpanModel() {
        return spanModel;
    }

    public Date getD1() {
        return d1;
    }

    public void setD1(Date d1) {
        this.d1 = d1;
    }

    public Date getD2() {
        return d2;
    }

    public void setD2(Date d2) {
        this.d2 = d2;
    }
    private Date d1, d2;

    @PostConstruct
    public void init() {
        prikaz = "ZS";
        postaje = postajaFacade.findAll();
        postaja = postaje.get(0);
        programiSvi = zeroSpanFacade.getProgram(postaja);
        
        novi = false;
        dateModel = new LineChartModel();
        zeroModel = new LineChartModel();
        spanModel = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.set(d1, 0);
        LineChartSeries series2 = new LineChartSeries();
        series2.set(d1, 0);

        dateModel.addSeries(series1);
        zeroModel.addSeries(series1);
        spanModel.addSeries(series2);
    }

    public Integer getPostajaId() {
        return postajaId;
    }

    public void setPostajaId(Integer postajaId) {
        this.postajaId = postajaId;
    }

    public Postaja getPostaja() {
        return postaja;
    }

    public void setPostaja(Postaja postaja) {
        this.postaja = postaja;
    }

    public Collection<Postaja> getPostaje() {
        return postaje;
    }

    public ProgramMjerenja getSelektiraniProgram() {
        return selektiraniProgram;
    }

    public void setSelektiraniProgram(ProgramMjerenja selektiraniProgram) {
        this.selektiraniProgram = selektiraniProgram;
    }

    public Collection<ProgramMjerenja> getProgramiSvi() {
        return programiSvi;
    }

    public void setProgramiSvi(List<ProgramMjerenja> programiSvi) {
        this.programiSvi = programiSvi;
    }

    public void onPostajaChange() {
        novi = false;
        if (postaja != null) {
            programiSvi = zeroSpanFacade.getProgram(postaja);
        } else {
            programiSvi = new ArrayList<>();

        }
    }

    public void onProgramChange() {
        novi = false;
    }

    public void pokupiPodatke() {
        if (!novi && selektiraniProgram != null) {
            minZ = 999999.f;
            maxZ = -999.f;
            minS = 999999.f;
            maxS = -999.f;
            zeroS = new LineChartSeries();
            spanS = new LineChartSeries();
            zeroS.setLabel("Zero");
            spanS.setLabel("Span");
            for (ZeroSpan zs : zeroSpanFacade.getPodaci(selektiraniProgram, d1, d2)) {
                if (zs.getVrijednost() > -999 ) {
                    switch (zs.getVrsta()) {
                        case "AZ":
                            zeroS.set(sdf.format(zs.getVrijeme()), zs.getVrijednost());
                            if (zs.getVrijednost() > maxZ) {
                                maxZ = zs.getVrijednost();
                            }
                            if (zs.getVrijednost() < minZ) {
                                minZ = zs.getVrijednost();
                            }
                            break;
                        case "AS":
                            spanS.set(sdf.format(zs.getVrijeme()), zs.getVrijednost());
                            if (zs.getVrijednost() > maxS) {
                                maxS = zs.getVrijednost();
                            }
                            if (zs.getVrijednost() < minS) {
                                minS = zs.getVrijednost();
                            }
                            break;
                    }
                }
            }
            novi = true;
        }
    }

    public void displayLocation() {
        pokupiPodatke();
        
        dateModel = new LineChartModel();
        dateModel.setExtender("chartExtender");
        dateModel.setZoom(true);

        zeroModel = new LineChartModel();
        zeroModel.setExtender("chartExtender");
        zeroModel.setZoom(true);

        spanModel = new LineChartModel();
        spanModel.setExtender("chartExtender");
        spanModel.setZoom(true);

        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMin(sdf.format(d1));
        axis.setMax(sdf.format(d2));
        axis.setTickFormat("%m. %d. %H:%M");
        dateModel.getAxes().put(AxisType.X, axis);
        dateModel.setLegendPosition("ne");
        
        zeroModel.getAxes().put(AxisType.X, axis);
        zeroModel.setLegendPosition("ne");
        spanModel.getAxes().put(AxisType.X, axis);
        spanModel.setLegendPosition("ne");

        
        Axis zeroAxis = new LinearAxis("Zero");
        zeroAxis.setMin(minZ-1);
        zeroAxis.setMax(maxZ+1);
        Axis spanAxis = new LinearAxis("Span");
        spanAxis.setMin(minS - 1);
        spanAxis.setMax(maxS + 1);
//        spanS.setYaxis(AxisType.Y2);
        spanS.setYaxis(AxisType.Y);
        zeroS.setYaxis(AxisType.Y);
        
        if (this.prikaz.contains("Z")) {
            dateModel.addSeries(zeroS);
        }
        if (this.prikaz.contains("S")) {
            dateModel.addSeries(spanS);
        }
        dateModel.getAxes().put(AxisType.Y, zeroAxis);
        dateModel.getAxes().put(AxisType.Y2, spanAxis);        

        zeroModel.addSeries(zeroS);
        spanModel.addSeries(spanS);
        zeroModel.getAxes().put(AxisType.Y, zeroAxis);
        spanModel.getAxes().put(AxisType.Y, spanAxis);        

        
    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    


}
