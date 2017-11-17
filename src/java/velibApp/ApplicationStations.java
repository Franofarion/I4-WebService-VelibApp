package velibApp;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import velibCarto.Carto;
import velibStation.Station;

 

public class ApplicationStations implements Serializable {

    
    public ApplicationStations() {
        
    }

    public Carto getAllStation() throws MalformedURLException, IOException, JAXBException{
        URL url = new URL("http://www.velib.paris.fr/service/carto");
        InputStream fluxLecture = url.openStream(); 
        
        JAXBContext jc = JAXBContext.newInstance("velibCarto"); // l'endroit où se trouve l'objectFactory
        Unmarshaller u = jc.createUnmarshaller();
        
        Carto carte = (Carto) u.unmarshal(fluxLecture);
        return carte;
    }
      

    public Station getStation(Short number) throws MalformedURLException, IOException, JAXBException{
        URL url = new URL("http://www.velib.paris.fr/service/stationdetails/"+ Short.toString(number));
        InputStream fluxLecture = url.openStream(); 
        
        JAXBContext jc = JAXBContext.newInstance("velibStation"); // l'endroit où se trouve l'objectFactory
        Unmarshaller u = jc.createUnmarshaller();
        
        Station station = (Station) u.unmarshal(fluxLecture);
        return station;
    }
    
}