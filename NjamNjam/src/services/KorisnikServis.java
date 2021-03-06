package services;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import dao.KorpaDAO;
import dao.RestoranDAO;
import dto.KorisnikDTO;
import dto.KorisnikJSONDTO;
import dto.KorisnikPrijavaDTO;

@Path("/korisnici")
public class KorisnikServis {

	@Context
	ServletContext ctx;

	@GET
	@Path("/odjava")
	@Produces(MediaType.TEXT_HTML)
	public Response odjava(@Context HttpServletRequest request) {
		
		if(korisnikJeKupac(request) || korisnikJeAdmin(request) || korisnikJeMenadzer(request) || korisnikJeDostavljac(request)) {
		
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
	
	@POST
	@Path("/prijava")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response prijava(KorisnikPrijavaDTO korisnikDTO,@Context HttpServletRequest request) {
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

		request.getSession().setAttribute("ulogovanKorisnik", korisnik);

		if (korisnik.getUloga().equals("ADMIN")) {
			return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/admin.html").build();

		} else if (korisnik.getUloga().equals("KUPAC")) {
			return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/kupac.html").build();

		} else if (korisnik.getUloga().equals("MENADZER")) {
			return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/menadzer.html").build();
		}
		else if (korisnik.getUloga().equals("DOSTAVLJAC")) {
			return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/dostavljac.html").build();
		}

		return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/#/prijava").build(); 
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
		KorpaDAO korpe = dobaviKorpeDAO();
		sviKorisnici.dodajKorpuKorisniku(sviKorisnici.getValues().size(), korpe.dodajKorpu());
		return Response.status(Response.Status.ACCEPTED).entity("/NjamNjam/#/prijava").build();																						// accepted
	}

	@GET
	@Path("/dobaviKorisnikeBezAdmina")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviKorisnikeBezAdmina(@Context HttpServletRequest request) {
		
		if(korisnikJeAdmin(request)) {
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity(dobaviKorisnike().getValues())
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/daLiJeKorisnikKupac")
	@Produces(MediaType.APPLICATION_JSON)
	public Response daLiJeKorisnikKupac(@Context HttpServletRequest request) {
		
		if(korisnikJeKupac(request)) {
			return Response
					.status(Response.Status.ACCEPTED)
					.entity(true)
					.build();
		}
		return Response.status(Response.Status.ACCEPTED)
				.entity(false).build();
	}
	
	@GET
	@Path("/dobaviSveKorisnike")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviSveKorisnike(@Context HttpServletRequest request) {
		
		if(korisnikJeAdmin(request)) {
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity(dobaviKorisnike().getValues())
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/dobaviSveKupceRestorana")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviSveKupceRestorana(@Context HttpServletRequest request) {
		
		if(korisnikJeMenadzer(request)) {
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
			if(korisnik.getIdRestorana()==-1)	// Vraca praznu listu ako nema restoran
			{
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
						.entity(new ArrayList<Korisnik>())
						.build();
			}else {	// Vraca listu kupaca iz restorana
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity(dobaviKorisnike().dobaviKupceIzListe(dobaviRestoraneDAO().dobaviKupcePoIdRestorana(korisnik.getIdRestorana())))
					.build();}
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/blokirajKorisnika")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response blokirajKorisnika(KorisnikJSONDTO korisnik,@Context HttpServletRequest request){
		
		if(korisnikJeAdmin(request)) {
			KorisnikDAO sviKorisnici = dobaviKorisnike();
			sviKorisnici.blokirajKorisnikaPoID(korisnik.korisnik.getKorisnickoIme());
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS BLOCK")
					.entity(dobaviKorisnike().getValues())
					.build();
		}
		else {
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
			}
		}
	
	@POST
	@Path("/odblokirajKorisnika")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response odblokirajKorisnika(KorisnikJSONDTO korisnik,@Context HttpServletRequest request){
		
		if(korisnikJeAdmin(request)) {
		
			KorisnikDAO sviKorisnici = dobaviKorisnike();
			sviKorisnici.oblokirajKorisnikaPoID(korisnik.korisnik.getKorisnickoIme());
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS UNBLOCK")
					.entity(dobaviKorisnike().getValues())
					.build();
		}
		else {
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}}
	
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
	
	private RestoranDAO dobaviRestoraneDAO() {
		RestoranDAO restorani = (RestoranDAO) ctx.getAttribute("restorani");
		if (restorani == null) {
			restorani = new RestoranDAO();
			restorani.ucitajRestorane();
			ctx.setAttribute("restorani", restorani);
		}
		return restorani;
	}
	
	private KorpaDAO dobaviKorpeDAO() {
		KorpaDAO korpe = (KorpaDAO) ctx.getAttribute("korpe");
		if (korpe == null) {
			korpe = new KorpaDAO();
			korpe.ucitajKorpe();
			ctx.setAttribute("korpe", korpe);
		}
		return korpe;
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
