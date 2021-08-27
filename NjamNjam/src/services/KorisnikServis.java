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
import dto.KorisnikPrijavaDTO;

@Path("/korisnici")
public class KorisnikServis {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	@POST
	@Path("/prijava")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response prijava(KorisnikPrijavaDTO korisnikDTO) {
		KorisnikDAO korisnici = dobaviKorisnike();

		Korisnik korisnik = korisnici.dobaviKorisnikaPoKorisnickomImenu(korisnikDTO.korisnickoIme);

		if (korisnik == null) {
			System.out.println("Nema takvog korisnika");
			return Response.status(Response.Status.BAD_REQUEST).entity("Korisničko ime ili lozinka netačni, pokušajte ponovo")
					.build();
		}	
		
		
		
		if (!korisnik.getLozinka().equals(korisnikDTO.lozinka)) {
			System.out.println("Sifre se ne poklapaju");
			return Response.status(Response.Status.BAD_REQUEST).entity("Korisničko ime ili lozinka netačni, pokušajte ponovo")
					.build();
		}
		
		if(korisnici.jeBlokiran(korisnikDTO.korisnickoIme)) {
			System.out.println("Blokiran");
			return Response.status(Response.Status.BAD_REQUEST).entity("Blokirani ste!")
					.build();
		}

		request.getSession().setAttribute("ulogovanKorisnik", korisnik); // we give him a session


		if (korisnik.getUloga().equals("ADMIN")) {
			return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/admin.html").build();

		} else if (korisnik.getUloga().equals("KUPAC")) {
			return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/kupac.html").build();

		} else if (korisnik.getUloga().equals("MENADZER")) {
			// return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/").build();
		}
		else if (korisnik.getUloga().equals("DOSTAVLJAC")) {
			// return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/").build();
		}

		return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/#/loginaaa").build(); // redirect to login
																									// when is login
																									// accepted
		// return Response.ok().build();

	}
	
	@POST
	@Path("/registracija")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registracija(KorisnikDTO korisnik) {
		
		KorisnikDAO sviKorisnici = dobaviKorisnike();

		System.out.println("Broj ucitanih korisnika:" + sviKorisnici.values().size());
		
		if (sviKorisnici.dobaviKorisnikaPoKorisnickomImenu(korisnik.korisnickoIme) != null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Već postoji korisnik sa unetim korisničkim imenom. Molimo vas pokušajte drugo.").build();
		}

		sviKorisnici.dodajNovogKorisnika(korisnik);

		return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/#/prijava").build();																						// accepted
	}

	@GET
	@Path("/dobaviKorisnikeBezAdmina")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviKorisnikeBezAdmina() {
		
		if(korisnikJeAdmin()) {
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity(dobaviKorisnike().getValues())
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/dobaviNovogKorisnika")
	@Produces(MediaType.APPLICATION_JSON)
	public Korisnik dobaviNovogKorisnika() {
		
		
		Korisnik korisnik = new Korisnik();
		KorisnikDAO sviKorisnici = dobaviKorisnike();
		
		System.out.println("Broj ucitanih korisnika:" + sviKorisnici.values().size());
		
		Integer idKorisnika = sviKorisnici.getValues().size() + 1;
		korisnik.setID(idKorisnika);
		
		return korisnik;

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
			if(korisnik.getUloga().equals("KUPAC")) {
				return true;
			}
		}	
		return false;
	}
}
