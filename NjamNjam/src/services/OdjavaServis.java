package services;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Korisnik;

@Path("/odjava")
public class OdjavaServis {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	
	@GET
	@Path("/korisnik")
	@Produces(MediaType.TEXT_HTML)
	public Response logoutUser() {
		
		if(korisnikJeKupac() || korisnikJeAdmin() || korisnikJeMenadzer() || korisnikJeDostavljac()) {
		
			HttpSession session = request.getSession();
			if(session != null && session.getAttribute("ulogovanKorisnik") != null) {
				session.invalidate();
			}
			return Response
					.status(Response.Status.ACCEPTED).entity("Uspešna odjava")
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nemate dozvolu da pritupite!").build();
	}
	
	@SuppressWarnings("unused")
	private boolean korisnikJeDostavljac() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("DOSTAVLJAC")) {
				return true;
			}
		}	
		return false;
	}
	
	@SuppressWarnings("unused")
	private boolean korisnikJeMenadzer() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("MENADZER")) {
				return true;
			}
		}	
		return false;
	}

	@SuppressWarnings("unused")
	private boolean korisnikJeAdmin() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("ADMIN")) {
				return true;
			}
		}	
		return false;
	}
	
	@SuppressWarnings("unused")
	private boolean korisnikJeKupac() {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			System.out.println(korisnik.getUloga());
			if(korisnik.getUloga().equals("KUPAC")) {
				return true;
			}
		}	
		System.out.println("\n\n\nNULL JE\n\n\n");
		return false;
	}
}
