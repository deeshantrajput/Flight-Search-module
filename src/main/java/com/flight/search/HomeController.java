package com.flight.search;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.util.*;

import javax.swing.text.View;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.qpxExpress.QPXExpressRequestInitializer;
import com.google.api.services.qpxExpress.QPXExpress;
import com.google.api.services.qpxExpress.model.FlightInfo;
import com.google.api.services.qpxExpress.model.LegInfo;
import com.google.api.services.qpxExpress.model.PassengerCounts;
import com.google.api.services.qpxExpress.model.PricingInfo;
import com.google.api.services.qpxExpress.model.SegmentInfo;
import com.google.api.services.qpxExpress.model.SliceInfo;
import com.google.api.services.qpxExpress.model.TripOption;
import com.google.api.services.qpxExpress.model.TripOptionsRequest;
import com.google.api.services.qpxExpress.model.TripsSearchRequest;
import com.google.api.services.qpxExpress.model.SliceInput;
import com.google.api.services.qpxExpress.model.TripsSearchResponse;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private static final String APPLICATION_NAME = "Flight Search";

    private static final String API_KEY = "AIzaSyAETJvTaNTXnVSCtGXtg9D8bXOfXOdQ3So";

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
    
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}
	
	
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public String search(@RequestParam("originname") String originname,@RequestParam("destinationname") String destinationname,
	@RequestParam("adult") int adult,@RequestParam("child") int child,@RequestParam("tdate") String tdate,
	@RequestParam("arrivaltime") String arrivaltime,Model model){
		
		 LinkedList<String> flightNum = new LinkedList<String>();
		    LinkedList<String> flightCarrier = new LinkedList<String>();
		    LinkedList<Integer> durationLeg = new LinkedList<Integer>();
		    LinkedList<String> origin = new LinkedList<String>();
		    LinkedList<String> arrivalTime = new LinkedList<String>();
		    LinkedList<String> departTime = new LinkedList<String>();
		    LinkedList<String> dest = new LinkedList<String>();
		    LinkedList<String>  Price = new LinkedList<String>();
		
		System.out.println("origin:"+originname);
	    System.out.println("destination:"+destinationname);
	    System.out.println("Number of adults:"+adult);
	    System.out.println("Number of children:"+child);
	    System.out.println("Transfer date:"+tdate);
	    System.out.println("Arrival Time:"+arrivaltime);
	    
	    try {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
           
        PassengerCounts passengers= new PassengerCounts();
        passengers.setAdultCount(adult);
        passengers.setChildCount(child);
        
        List<SliceInput> slices = new ArrayList<SliceInput>();
        SliceInput slice = new SliceInput();
        slice.setOrigin(originname); 
        slice.setDestination(destinationname); 
        slice.setDate(tdate);
        slices.add(slice);

        TripOptionsRequest request= new TripOptionsRequest();
        request.setSolutions(3);
        request.setPassengers(passengers);
        request.setSlice(slices);
       
        TripsSearchRequest parameters = new TripsSearchRequest();
        parameters.setRequest(request);
        
        QPXExpress qpXExpress= new QPXExpress.Builder(httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME)
        .setGoogleClientRequestInitializer(new QPXExpressRequestInitializer(API_KEY)).build();

        TripsSearchResponse list= qpXExpress.trips().search(parameters).execute();
        
        List<TripOption> tripResults=list.getTrips().getTripOption();

        for(int i=0; i<tripResults.size(); i++){
     	   
            //Slice
            List<SliceInfo> sliceInfo= tripResults.get(i).getSlice();
            for(int j=0; j<sliceInfo.size(); j++){
                
                // Segment Information
                List<SegmentInfo> segInfo= sliceInfo.get(j).getSegment();
                for(int k=0; k<segInfo.size(); k++){
                    
                    FlightInfo flightInfo=segInfo.get(k).getFlight();
                    flightNum.add(flightInfo.getNumber());
                    System.out.println("flightNum "+flightNum);
                    flightCarrier.add(flightInfo.getCarrier());
                    System.out.println("flightCarrier "+flightCarrier);
                    
                    List<LegInfo> leg=segInfo.get(k).getLeg();
                    for(int l=0; l<leg.size(); l++){
                           arrivalTime.add(leg.get(l).getArrivalTime());
                           System.out.println("arrivalTime "+arrivalTime);
                           departTime.add(leg.get(l).getDepartureTime());
                           System.out.println("departTime "+departTime);
                           dest.add(leg.get(l).getDestination());
                           System.out.println("Destination "+dest);
                           origin.add(leg.get(l).getOrigin());
                           System.out.println("origin "+origin);
                           durationLeg.add(leg.get(l).getDuration());
                           int mil= leg.get(l).getMileage();
                           System.out.println("Milleage "+mil);
                           }
                     }
               }

               //Pricing
               List<PricingInfo> priceInfo= tripResults.get(i).getPricing();
               for(int p=0; p<priceInfo.size(); p++){
                   Price.add(priceInfo.get(p).getSaleTotal());
                   
                  }
              }
        
        model.addAttribute("Price", Price);
        model.addAttribute("flightNum", flightNum );
        model.addAttribute("flightCarrier", flightCarrier);
        model.addAttribute("origin", origin);
        model.addAttribute("durationLeg", durationLeg);
        model.addAttribute("arrivalTime", arrivalTime);
        model.addAttribute("departTime", departTime);
        model.addAttribute("dest",dest);
       
        return "home";
       } catch (IOException e) {
         System.err.println(e.getMessage());
       } catch (Throwable t) {
         t.printStackTrace();
       }
	    return "home"; 
       //System.exit(1);
       
 }
	
}
