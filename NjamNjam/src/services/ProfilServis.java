package services;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Korisnik;
import dao.KorisnikDAO;
import dto.KorisnikDTO;

@Path("/profil")
public class ProfilServis {
	
	@Context
	ServletContext ctx;
	
	
	@GET
	@Path("/profilKorisnika")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviProfilKorisnika(@Context HttpServletRequest request) {
		
		if(korisnikJeKupac(request) || korisnikJeAdmin(request) || korisnikJeMenadzer(request) || korisnikJeDostavljac(request)) {
	
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");		

			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity( korisnik)
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nemate dozvolu za pristup!").build();
	}
	
	@POST
	@Path("/sacuvajIzmeneKorisnika")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sacuvajIzmeneKorisnika(KorisnikDTO izmenjenKorisnik,@Context HttpServletRequest request) {
		
		if(korisnikJeKupac(request) || korisnikJeAdmin(request) || korisnikJeMenadzer(request) || korisnikJeDostavljac(request)) {
			
			KorisnikDAO korisnici = dobaviKorisnike();
			korisnici.promeniKorisnika(izmenjenKorisnik);
						
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nemate dozvolu za pristup!").build();

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