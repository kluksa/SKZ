/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import com.csvreader.CsvWriter;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.util.OperStatus;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author kraljevic
 */
@Named(value = "satniPodaciMB")
@ViewScoped
public class SatniPodaciMB implements Serializable {

    @Inject
    private  transient Logger log;
    private boolean novi = false;

    /**
     * Creates a new instance of SatniPodaciMB
     */
    public SatniPodaciMB() {
    }

    @EJB
    private PodatakFacade podatakFacade;

    @EJB
    private PostajaFacade postajaFacade;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Postaja postaja;
    private Integer postajaId;
    private Collection<Postaja> postaje;
//    private TreeMap<String, Postaja> postaje;

    private List<ProgramMjerenja> selektiraniProgram;
    private Map<ProgramMjerenja, List<Podatak>> selektiraniPodaci;

    private Collection<ProgramMjerenja> programiSvi;

    private LineChartModel dateModel;

    public LineChartModel getDateModel() {
        return dateModel;
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
        postaja = null;
        postaje = postajaFacade.findAll();
        dateModel = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.set(d1, 0);
        dateModel.addSeries(series1);
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

    public List<ProgramMjerenja> getSelektiraniProgram() {
        return selektiraniProgram;
    }

    public void setSelektiraniProgram(List<ProgramMjerenja> selektiraniProgram) {
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
            programiSvi = postaja.getProgramMjerenjaCollection();
        } else {
            programiSvi = new ArrayList<>();

        }
    }

    public void onProgramChange() {
        novi = false;
    }

    public void pokupiPodatke() {
        if (!novi) {
            selektiraniPodaci = new HashMap<>();
            if (selektiraniProgram != null) {
                for (ProgramMjerenja pm : selektiraniProgram) {
                    selektiraniPodaci.put(pm, podatakFacade.getPodatak(pm, d1, d2, 0, true, true));
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
        dateModel.getAxis(AxisType.Y).setLabel("Values");

        for (ProgramMjerenja pm : selektiraniPodaci.keySet()) {
            List<Podatak> podaci = selektiraniPodaci.get(pm);

            LineChartSeries series1 = new LineChartSeries();
            series1.setLabel(pm.getKomponentaId().getNaziv());
            for (Podatak p : podaci) {
                if (p.getObuhvat() > 74) {
                    series1.set(sdf.format(p.getVrijeme()), p.getVrijednost());
                }
            }
//            series1.setMarkerStyle("circle");
            dateModel.addSeries(series1);
        }
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMin(sdf.format(d1));
        axis.setMax(sdf.format(d2));
        axis.setTickFormat("%m. %d. %H:%M");
        dateModel.getAxes().put(AxisType.X, axis);
        dateModel.setLegendPosition("ne");

    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public StreamedContent download() {

        pokupiPodatke();
        DefaultStreamedContent file = new DefaultStreamedContent();
        file.setContentType("application/csv");
        file.setName(postaja.getNazivPostaje().replace(' ', '_').toLowerCase().concat(".csv"));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CsvWriter csvWriter = new CsvWriter(os, ';', Charset.forName("utf-8"));

        pisiCsv(csvWriter);
        ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray());
        file.setStream(bis);

        return file;

    }

    private void pisiCsv(CsvWriter csvWriter) {
        try {
            SortedMap<Date, Integer> vremena = new TreeMap<>();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
            cal.setTime(d1);
            int nrow = 0;
            while (!cal.getTime().after(d2)) {
                vremena.put(cal.getTime(), nrow);
                cal.add(Calendar.HOUR, 1);
                nrow++;
            }
            int size = selektiraniPodaci.keySet().size();

            csvWriter.write("Vrijeme");
            HashMap<ProgramMjerenja, Integer> komponente = new HashMap<>();
            Integer ncol = 0;
            for (ProgramMjerenja pm : selektiraniPodaci.keySet()) {
                try {
                    komponente.put(pm, ncol);
                    csvWriter.write(pm.getKomponentaId().getFormula());
                    csvWriter.write("obuhvat");
                    csvWriter.write("status");
                    ncol++;
                } catch (IOException ex) {
                    Logger.getLogger(SatniPodaciMB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            csvWriter.endRecord();
            Podatak[][] tablica = new Podatak[nrow][size];
            for (ProgramMjerenja pm : selektiraniPodaci.keySet()) {
                List<Podatak> podatak = podatakFacade.getPodatak(pm, d1, d2, 0, true, true);
                for (Podatak p : podatak) {
                    Integer i = komponente.get(pm);
                    Integer j = vremena.get(p.getVrijeme());
                    tablica[j][i] = p;
                }
            }
            for (Date d : vremena.keySet()) {
                try {

                    csvWriter.write(sdf.format(d));
                    for (int i = 0; i < ncol; i++) {
                        Podatak p = tablica[vremena.get(d)][i];
                        if (p != null) {
                            csvWriter.write(p.getVrijednost().toString());
                            csvWriter.write(p.getObuhvat().toString());
                            csvWriter.write(Integer.toString(p.getStatus()));
                        } else {
                            csvWriter.write("-999");
                            csvWriter.write("0");
                            csvWriter.write(Integer.toString((1 << OperStatus.NEDOSTAJE.ordinal())));
                        }
                    }
                    csvWriter.endRecord();
                } catch (IOException ex) {
                    Logger.getLogger(SatniPodaciMB.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            csvWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(SatniPodaciMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
