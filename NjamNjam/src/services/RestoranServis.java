package services;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Korisnik;
import beans.Restoran;
import beans.Slika;
import dao.KorisnikDAO;
import dao.RestoranDAO;
import dao.SlikaDAO;
import dto.NoviRestoranDTO;
import dto.RestoranIzmenaDTO;
import dto.RestoranJSONDTO;

@Path("/restoran")
public class RestoranServis {

	@Context
	ServletContext ctx;
	
	@GET
	@Path("/dobaviRestoranZaPrikaz")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviRestoranZaPrikaz(@Context HttpServletRequest request) {	
		Restoran restoran = (Restoran) request.getSession().getAttribute("restoranZaPregled");
		return Response
				.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGE")
				.entity(dobaviRestoraneDAO().nadjiRestoranPoID(restoran.getID()))
				.build();
	}
	
	@POST
	@Path("/restoranZaPregled")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response restoranZaPregled(RestoranJSONDTO restoran,@Context HttpServletRequest request) {
			request.getSession().setAttribute("restoranZaPregled", this.dobaviRestoraneDAO().nadjiRestoranPoID(restoran.restoran.getID()));
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGE")
					.entity(dobaviRestoraneDAO())
					.build();
		
	}
	
	@POST
	@Path("/dodajNoviRestoran")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajNoviRestoran(NoviRestoranDTO noviRestoran,@Context HttpServletRequest request) {
		
		if(korisnikJeAdmin(request)) {			
			SlikaDAO slike = dobaviSlike();
			if(!noviRestoran.putanjaDoSlike.equals("nema"))
			{
				Slika slika = slike.dodajNovuSliku(noviRestoran.putanjaDoSlike);
				noviRestoran.putanjaDoSlike = Integer.toString(slika.getID());
			}	
			// Dodavanje restorana u fajl
			RestoranDAO restorani = dobaviRestoraneDAO();
			restorani.dodajNoviRestoran(noviRestoran);
			// Dodavanje id restorana u id korisnika
			KorisnikDAO korisnici = dobaviKorisnike();
			noviRestoran.ID = restorani.getValues().size();
			korisnici.dodajRestoranMenadzeru(noviRestoran);
	
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGE")
					.entity(dobaviRestoraneDAO())
					.build();
		
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/dodajNoviRestoranIMenadzera")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajNoviRestoranIMenadzera(NoviRestoranDTO noviRestoran,@Context HttpServletRequest request) {
		
		if(korisnikJeAdmin(request)) {			
			SlikaDAO slike = dobaviSlike();
			if(!noviRestoran.putanjaDoSlike.equals("nema"))
			{
				Slika slika = slike.dodajNovuSliku(noviRestoran.putanjaDoSlike);
				noviRestoran.putanjaDoSlike = Integer.toString(slika.getID());
			}		
			
			RestoranDAO restorani = dobaviRestoraneDAO();
			KorisnikDAO korisnici = dobaviKorisnike();
			if (korisnici.dobaviKorisnikaPoKorisnickomImenu(noviRestoran.korisnickoIme) != null) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("Već postoji korisnik sa unetim korisničkim imenom. Molimo vas pokušajte drugo.").build();
			}
			// Dodavanje restorana u fajl
			noviRestoran.idMenadzera = korisnici.getValues().size() + 1;
			restorani.dodajNoviRestoran(noviRestoran);
			// Dodavanje menadzera u fajl
			noviRestoran.idRestorana = restorani.getValues().size();
			korisnici.dodajMenadzera(noviRestoran);
	
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGE")
					.entity(dobaviRestoraneDAO())
					.build();
		
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/dobaviRestorane")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Restoran> dobaviRestorane() {	
		return dobaviRestoraneDAO().getValues();
	}
	
	@GET
	@Path("/dobaviRestoranMenadzera")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviRestoranMenadzera(@Context HttpServletRequest request) {	
		if(korisnikJeMenadzer(request)) {
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
			if(korisnik.getIdRestorana() == -1 || korisnik.getIdRestorana() == 0)
			{
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(null)
						.build();
			}else {
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(dobaviRestoraneDAO().nadjiRestoranPoID(korisnik.getIdRestorana()))
						.build();	
			}
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/izmeniRestoran")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response izmeniRestoran(RestoranIzmenaDTO restoran,@Context HttpServletRequest request) {
		if(korisnikJeAdmin(request) || korisnikJeMenadzer(request)) {
			SlikaDAO slike = dobaviSlike();
			if(!isNumeric(restoran.putanjaDoSlike))
			{
				Slika slika = slike.dodajNovuSliku(restoran.putanjaDoSlike);
				restoran.putanjaDoSlike = Integer.toString(slika.getID()); 	
			}
			
			RestoranDAO restorani = dobaviRestoraneDAO();
			restorani.izmeniRestoran(restoran);
	
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(dobaviRestoraneDAO().getValues())
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}

	private static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
	
	@DELETE
	@Path("/obrisiRestoranMenadzer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response obrisiRestoranMenadzer(RestoranJSONDTO restoran,@Context HttpServletRequest request) {
		
		if(korisnikJeMenadzer(request)) {
				RestoranDAO restorani = dobaviRestoraneDAO();
				restorani.obrisiRestoran(restoran.restoran.getID());
		
				// Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
				KorisnikDAO korisnici = dobaviKorisnike();
				korisnici.obrisiRestoranMenadzeru(restoran.restoran.getIdMenadzera());
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity("Trenutno niste taduženi ni za jedan restoran.")
						.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
		
	}

	@DELETE
	@Path("/obrisiRestoran")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response obrisiRestoran(RestoranJSONDTO restoran,@Context HttpServletRequest request) {
		if(korisnikJeAdmin(request)) {
				RestoranDAO restorani = dobaviRestoraneDAO();
				restorani.obrisiRestoran(restoran.restoran.getID());
				
				KorisnikDAO korisnici = dobaviKorisnike();
				korisnici.obrisiRestoranMenadzeru(restoran.restoran.getID());
		
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(dobaviRestoraneDAO().getValues())
						.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}

	
	private SlikaDAO dobaviSlike() {
		SlikaDAO slike = (SlikaDAO) ctx.getAttribute("slike");
		if(slike == null) {
			slike = new SlikaDAO();
			slike.ucitajSlike();
			ctx.setAttribute("slike", slike);
		}
		return slike;		
	}
	
	private RestoranDAO dobaviRestoraneDAO() {
		RestoranDAO restorani = (RestoranDAO) ctx.getAttribute("restorani");
		if (restorani == null) {
			restorani = new RestoranDAO();
			restorani.ucitajRestorane();
			ctx.setAttribute("restorani", restorani);
		}
		return restorani;
	}

	private KorisnikDAO dobaviKorisnike() {
		KorisnikDAO korisnici = (KorisnikDAO) ctx.getAttribute("korisnici");
		
		if (korisnici == null) {
			korisnici = new KorisnikDAO();
			korisnici.ucitajKorisnike();
			ctx.setAttribute("korisnici", korisnici);
		}
		return korisnici;
	}
	
	@SuppressWarnings("unused")
	private boolean korisnikJeDostavljac(@Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("DOSTAVLJAC")) {
				return true;
			}
		}	
		return false;
	}
	
	@SuppressWarnings("unused")
	private boolean korisnikJeMenadzer(@Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("MENADZER")) {
				return true;
			}
		}	
		return false;
	}

	@SuppressWarnings("unused")
	private boolean korisnikJeAdmin(@Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("ADMIN")) {
				return true;
			}
		}	
		return false;
	}
	
	@SuppressWarnings("unused")
	private boolean korisnikJeKupac(@Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("KUPAC")) {
				return true;
			}
		}	
		return false;
	}

}