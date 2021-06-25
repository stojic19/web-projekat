package services;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Obrok;

@Path("/obrokservices")
public class ObrokServices {
	@Context
	ServletContext ctx;
	private ArrayList<Obrok> obroci;
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("obroci") == null) {
			this.obroci = new ArrayList<Obrok>();
	        this.obroci.add(new Obrok("123-5216-12","Pecenje","Milutin Milunovic",1.00,"RUCAK",2200.00,false));
	        this.obroci.add(new Obrok("321-5753-85","Gulas","Lacika",10.00,"RUCAK",1500.00,false));
	        this.obroci.add(new Obrok("111-5555-22","Kajgana","Luj XVI",5.00,"DORUCAK",500.00,false));
			ctx.setAttribute("obroci", obroci);
		}
	}
	
	
	@POST
	@Path("/unesi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Obrok> unesi(Obrok o) {
		obroci = (ArrayList<Obrok>) ctx.getAttribute("obroci");
		for (Obrok obrok : obroci) {
			if(obrok.getId().equals(o.getId())) {
				return null;
			}
		}
		obroci.add(o);
		return obroci;
	}
	
	@GET
	@Path("/obroci/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public ArrayList<Obrok> dopuni(@PathParam("id") String id) {
		obroci = (ArrayList<Obrok>) ctx.getAttribute("obroci");
		for (Obrok o : obroci) {
			if(o.getId().equals(id)) {
				o.setDopuna(true);
			}
		}
		return obroci;
	}
	
	@GET
	@Path("/pretraga/{cena}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public ArrayList<Obrok> pretraga(@PathParam("cena") String cena) {
		obroci = (ArrayList<Obrok>) ctx.getAttribute("obroci");
		ArrayList<Obrok> obrokCena = new ArrayList<Obrok>();
		try {
			Double.parseDouble(cena);
		} catch (Exception e) {
			return null;
		}
		for (Obrok o: obroci) {
			if(o.getCena() == Double.parseDouble(cena)) {
				obrokCena.add(o);
			}
		}
		if(obrokCena.size() > 0) {
			return obrokCena;
		} else {
			return null;
		}
	}
}
