package velibApp;
 
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBException;
  
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import velibCarto.Carto;
import velibStation.Station;
 
@ManagedBean
@ViewScoped
public class MarkerStationsView implements Serializable {
     
    private MapModel simpleModel;
  
    private Marker marker;
    
    private ApplicationStations appStation;
  
    @PostConstruct
    public void init(){
        try {
            simpleModel = new DefaultMapModel();
            appStation = new ApplicationStations();
            //Call on method getAllStation() of ApplicationStations Java class
            Carto markerCarto = appStation.getAllStation();
            // Used for the path of the Gmap Marker
            StringBuilder ruta = new StringBuilder();
            ruta.append(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
            ruta.append("/resources/img");
            // Looping every marker of Carto object 
            for(Carto.Markers.Marker marker: markerCarto.getMarkers().getMarker()){
                if(marker.getNumber() > 0){
                    // Remove the 8 first character of the marker name e.g. "20030 - "
                    String name = marker.getName().substring(8);
                    LatLng coord = new LatLng(marker.getLat(),marker.getLng());
                    String img_path;
                    // If the velib station is open we use a green marker, if not a red
                    if(marker.getOpen() == 1){
                        img_path = ruta.toString() + "/green-marker.png";
                    } else {
                        img_path = ruta.toString() + "/red-marker.png";
                    }
                    
                    Marker mark = new Marker(coord, name, marker.getNumber(), img_path);
                    simpleModel.addOverlay(mark);
                }   
            }

        } catch (IOException ex) {
            Logger.getLogger(MarkerStationsView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(MarkerStationsView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    public MapModel getSimpleModel() {
        return simpleModel;
    }
      
    public void onMarkerSelect(OverlaySelectEvent event) {
        try {
            marker = (Marker) event.getOverlay();
            // marker.data is set in the init(), the 3rd parameter of PrimeFaces Marker instanciation
            Short number = (Short) marker.getData();
            //Call on method getStation() of ApplicationStations Java class
            Station station = this.appStation.getStation(number);
            String message = "Disponible : "+ station.getAvailable() + ", Utilisé : "+ station.getFree() + ", Total : " + station.getTotal();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, marker.getTitle(), message));
        } catch (IOException ex) {
            Logger.getLogger(MarkerStationsView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(MarkerStationsView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    public Marker getMarker() {
        return marker;
    }
}