package services;

import java.util.ArrayList;
import java.util.Collection;

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

import beans.Komentar;
import beans.Korisnik;
import beans.Korpa;
import beans.Porudzbina;
import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.KorpaDAO;
import dao.PorudzbinaDAO;
import dto.PorudzbinaDostavljacDTO;
import dto.PorudzbinaDostavljacJSONDTO;
import dto.PorudzbinaJSONDTO;
import dto.PorudzbinaZahteviDTO;
import dto.PorudzbinaZahteviJSONDTO;

@Path("/Porudzbina")
public class PorudzbinaServis {

	@Context
	ServletContext ctx;
	
	@GET
	@Path("/dobaviPorudzbineMenadzera")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviPorudzbineMenadzera(@Context HttpServletRequest request) {	
		
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			if(korisnik != null)
			if(korisnik.getUloga().equals("MENADZER")) {
			{			
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(porudzbine.dobaviPorudzbinePoIdRestorana(korisnik.getIdRestorana()))
						.build();	
			}
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/dobaviZahteve")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviZahteve( @Context HttpServletRequest request) {	
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");	
		if(korisnikJeMenadzer(request)) {
			
			ArrayList<PorudzbinaZahteviDTO> zahteviZaPrikaz = dobaviZahteveZaPrikaz(korisnik.getIdRestorana());
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(zahteviZaPrikaz)
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	private ArrayList<PorudzbinaZahteviDTO> dobaviZahteveZaPrikaz(Integer idRestorana)
	{
			// Sve porudzbine iz restorana
			ArrayList<Porudzbina> porudzbine = dobaviPorudzbineDAO().dobaviPorudzbinePoIdRestorana(idRestorana);
			ArrayList<PorudzbinaZahteviDTO> porudzbineZaPrikaz = new ArrayList<PorudzbinaZahteviDTO>();
					for(Porudzbina porudzbina : porudzbine)
					{
						if(porudzbina.getStatus().contains("CEKA DOSTAVLJACA"))
						{
							PorudzbinaZahteviDTO porudzbinaZaPrikaz = new PorudzbinaZahteviDTO();
							porudzbinaZaPrikaz.ID = porudzbina.getID();
							porudzbinaZaPrikaz.cena = porudzbina.getCena();
							porudzbinaZaPrikaz.idRestorana = porudzbina.getIdRestorana();
							porudzbinaZaPrikaz.imePrezimeKupca = porudzbina.getImePrezimeKupca();
							porudzbinaZaPrikaz.imeRestorana = porudzbina.getImeRestorana();
							porudzbinaZaPrikaz.status = porudzbina.getStatus();
							porudzbinaZaPrikaz.tipRestorana = porudzbina.getTipRestorana();
							porudzbinaZaPrikaz.vremePorudzbine = porudzbina.getVremePorudzbine();
							porudzbinaZaPrikaz.dostavljaci = new ArrayList<Korisnik>();
							for(Integer id : porudzbina.getZahteviOdDostavljaca())
							{
								porudzbinaZaPrikaz.dostavljaci.add(dobaviKorisnike().nadjiKorisnikaPoID(id));
							}
							porudzbineZaPrikaz.add(porudzbinaZaPrikaz);
						}
					}

					return porudzbineZaPrikaz;
	}
	
	@GET
	@Path("/dobaviPorudzbineKupca")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviPorudzbineKupca(@Context HttpServletRequest request) {	
		
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
			if(korisnik != null)
			if(korisnik.getUloga().equals("KUPAC")) {
			{			
				
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(dobaviPorudzbineKupca(korisnik))
						.build();
			}
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	private ArrayList<PorudzbinaDostavljacDTO> dobaviPorudzbineKupca(Korisnik korisnik)
	{
		ArrayList<Porudzbina> porudzbine = dobaviPorudzbineDAO().dobaviPorudzbineKupca(korisnik.getIdPorudzbina());
		ArrayList<PorudzbinaDostavljacDTO> porudzbineZaPrikaz = new ArrayList<PorudzbinaDostavljacDTO>();
		for(Porudzbina porudzbina : porudzbine)
		{
			PorudzbinaDostavljacDTO porudzbinaZaPrikaz = new PorudzbinaDostavljacDTO();
			porudzbinaZaPrikaz.ID = porudzbina.getID();
			porudzbinaZaPrikaz.cena = porudzbina.getCena();
			porudzbinaZaPrikaz.idRestorana = porudzbina.getIdRestorana();
			porudzbinaZaPrikaz.imePrezimeKupca = porudzbina.getImePrezimeKupca();
			porudzbinaZaPrikaz.imeRestorana = porudzbina.getImeRestorana();
			porudzbinaZaPrikaz.status = porudzbina.getStatus();
			porudzbinaZaPrikaz.tipRestorana = porudzbina.getTipRestorana();
			porudzbinaZaPrikaz.vremePorudzbine = porudzbina.getVremePorudzbine();
			porudzbinaZaPrikaz.poslatZahtev = 0;
			for(Integer id : porudzbina.getZahteviOdDostavljaca())
			{
				if(id == korisnik.getID())
				{
					porudzbinaZaPrikaz.poslatZahtev = 1;
					break;
				}
			}
			porudzbineZaPrikaz.add(porudzbinaZaPrikaz);
		}
		return porudzbineZaPrikaz;
	}
	
	@GET
	@Path("/dobaviPorudzbineDostavljaca")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviPorudzbineDostavljaca(@Context HttpServletRequest request) {	
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			if(korisnik != null)
			if(korisnik.getUloga().equals("DOSTAVLJAC")) {
			{			
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(porudzbine.dobaviPorudzbineKupca(korisnik.getIdPorudzbina()))
						.build();	
			}
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/dobaviPorudzbine")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Porudzbina> dobaviPorudzbine() {	
		return dobaviPorudzbineDAO().getValues();
	}
	
	@POST
	@Path("/dodajNovuPorudzbinu")                  //izmeniti: salju se podaci i porudzbina se tek ovde kreira
	@Consumes(MediaType.APPLICATION_JSON)          //dodati racunanje bodova
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajNovuPorudzbinu(PorudzbinaJSONDTO porudzbina, @Context HttpServletRequest request) {
		if(korisnikJeKupac(request)) {
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.dodajNovuPorudzbinu(porudzbina.porudzbina);
		}

		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/dodajKomentar")                  //izmeniti: salju se podaci i porudzbina se tek ovde kreira
	@Consumes(MediaType.APPLICATION_JSON)          //dodati racunanje bodova
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajKomentar(Komentar komentar, @Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnik != null)
		if(korisnik.getUloga().equals("KUPAC")) {		
			PorudzbinaDAO porudzbineDAO = dobaviPorudzbineDAO();
			porudzbineDAO.dodajZahtevUListuPorudzbine(komentar.getIdPorudzbine(), korisnik.getID());
			komentar.setIdKupca(korisnik.getID());
			// Dodavanje komentara u fajl
			dobaviKomentareDAO().dodajKomentar(komentar);

			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(dobaviPorudzbineKupca(korisnik))
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/posaljiZahtev")                  //izmeniti: salju se podaci i porudzbina se tek ovde kreira
	@Consumes(MediaType.APPLICATION_JSON)          //dodati racunanje bodova
	@Produces(MediaType.APPLICATION_JSON)
	public Response posaljiZahtev(PorudzbinaDostavljacJSONDTO porudzbinaDTO, @Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnikJeDostavljac(request)) {
			PorudzbinaDAO porudzbineDAO = dobaviPorudzbineDAO();
			porudzbineDAO.dodajZahtevUListuPorudzbine(porudzbinaDTO.porudzbina.ID, korisnik.getID());
			
			ArrayList<Porudzbina> porudzbine = porudzbineDAO.dobaviPorudzbineZaDostavu();
			ArrayList<PorudzbinaDostavljacDTO> porudzbineZaPrikaz = new ArrayList<PorudzbinaDostavljacDTO>();
			for(Porudzbina porudzbina : porudzbine)
			{
				PorudzbinaDostavljacDTO porudzbinaZaPrikaz = new PorudzbinaDostavljacDTO();
				porudzbinaZaPrikaz.ID = porudzbina.getID();
				porudzbinaZaPrikaz.cena = porudzbina.getCena();
				porudzbinaZaPrikaz.idRestorana = porudzbina.getIdRestorana();
				porudzbinaZaPrikaz.imePrezimeKupca = porudzbina.getImePrezimeKupca();
				porudzbinaZaPrikaz.imeRestorana = porudzbina.getImeRestorana();
				porudzbinaZaPrikaz.status = porudzbina.getStatus();
				porudzbinaZaPrikaz.tipRestorana = porudzbina.getTipRestorana();
				porudzbinaZaPrikaz.vremePorudzbine = porudzbina.getVremePorudzbine();
				porudzbinaZaPrikaz.poslatZahtev = 0;
				for(Integer id : porudzbina.getZahteviOdDostavljaca())
				{
					if(id == korisnik.getID())
					{
						porudzbinaZaPrikaz.poslatZahtev = 1;
						break;
					}
				}
				porudzbineZaPrikaz.add(porudzbinaZaPrikaz);
			}
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(porudzbineZaPrikaz)
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/pripremiPorudzbinu")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pripremiPorudzbinu(PorudzbinaJSONDTO porudzbina, @Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnikJeMenadzer(request)) {
			
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.pripremiPorudzbinu(porudzbina.porudzbina);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(porudzbine.dobaviPorudzbinePoIdRestorana(korisnik.getIdRestorana()))
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/cekaDostavljacaPorudzbina")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cekaDostavljacaPorudzbina(PorudzbinaJSONDTO porudzbina, @Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnikJeMenadzer(request)) {
			
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.cekaDostavljacaPorudzbina(porudzbina.porudzbina);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(porudzbine.dobaviPorudzbinePoIdRestorana(korisnik.getIdRestorana()))
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/transportujPorudzbinu")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transportujPorduzbinu(PorudzbinaZahteviJSONDTO porudzbina, @Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnikJeMenadzer(request)) {
			// status porudzbine u transportu
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.transportujPorudzbinu(porudzbina.porudzbina.ID);
			// dostavljacu dodajemo porudzbinu u listu
			Korisnik dostavljac = porudzbina.porudzbina.dostavljaci.get(0);
			dobaviKorisnike().dodajPorudzbinuDostavljacu(dostavljac.getID(), porudzbina.porudzbina.ID);
			ArrayList<PorudzbinaZahteviDTO> zahteviZaPrikaz = dobaviZahteveZaPrikaz(korisnik.getID());
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(zahteviZaPrikaz)
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/dostaviPorudzbinu")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dostaviPorudzbinu(PorudzbinaJSONDTO porudzbina, @Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnikJeDostavljac(request)) {
			
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.dostaviPorudzbinu(porudzbina.porudzbina);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(porudzbine.dobaviPorudzbineKupca(korisnik.getIdPorudzbina()))
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	
	@POST
	@Path("/otkaziPorudzbinu")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response otkaziPorudzbinu(PorudzbinaJSONDTO porudzbina, @Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnikJeKupac(request) && porudzbina.porudzbina.getStatus().equals("OBRADA")) {
			korisnik.oduzmiBodove(porudzbina.porudzbina.getCena());
			KorisnikDAO korisnici = dobaviKorisnike();
			korisnici.promeniKorisnikaNakonKupovine(korisnik);
			request.getSession().setAttribute("ulogovanKorisnik", korisnik);
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.otkaziPorudzbinu(porudzbina.porudzbina);
			KorpaDAO korpe = dobaviKorpeDAO();
			Korpa korpa = korpe.nadjiKorpuPoId(korisnik.getIdKorpe());
			korpa.setPopust(korisnik.getTip().getPopust());
			korpe.azurirajKorpu(korpa);
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(porudzbine.dobaviPorudzbineKupca(korisnik.getIdPorudzbina()))
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	
	@GET
	@Path("/dobaviPorudzbineZaDostavu")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviPorudzbineZaDostavu( @Context HttpServletRequest request) {	
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");	
		if(korisnikJeDostavljac(request)) {
			ArrayList<Porudzbina> porudzbine = dobaviPorudzbineDAO().dobaviPorudzbineZaDostavu();
			ArrayList<PorudzbinaDostavljacDTO> porudzbineZaPrikaz = new ArrayList<PorudzbinaDostavljacDTO>();
			for(Porudzbina porudzbina : porudzbine)
			{
				PorudzbinaDostavljacDTO porudzbinaZaPrikaz = new PorudzbinaDostavljacDTO();
				porudzbinaZaPrikaz.ID = porudzbina.getID();
				porudzbinaZaPrikaz.cena = porudzbina.getCena();
				porudzbinaZaPrikaz.idRestorana = porudzbina.getIdRestorana();
				porudzbinaZaPrikaz.imePrezimeKupca = porudzbina.getImePrezimeKupca();
				porudzbinaZaPrikaz.imeRestorana = porudzbina.getImeRestorana();
				porudzbinaZaPrikaz.status = porudzbina.getStatus();
				porudzbinaZaPrikaz.tipRestorana = porudzbina.getTipRestorana();
				porudzbinaZaPrikaz.vremePorudzbine = porudzbina.getVremePorudzbine();
				porudzbinaZaPrikaz.poslatZahtev = 0;
				for(Integer id : porudzbina.getZahteviOdDostavljaca())
				{
					if(id == korisnik.getID())
					{
						porudzbinaZaPrikaz.poslatZahtev = 1;
						break;
					}
				}
				porudzbineZaPrikaz.add(porudzbinaZaPrikaz);
			}
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(porudzbineZaPrikaz)
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/dobaviNedostavljenePorudzbine")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviNedostavljenePorudzbine() {	
		return Response
				.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
				.entity(dobaviPorudzbineDAO().dobaviNedostavljenePorudzbine())
				.build();
	}	

	private PorudzbinaDAO dobaviPorudzbineDAO() {
		PorudzbinaDAO porudzbine = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		if(porudzbine == null) {
			porudzbine = new PorudzbinaDAO();
			porudzbine.ucitajPorudzbine();
			ctx.setAttribute("porudzbine", porudzbine);
		}
		return porudzbine;
	}
	
	private boolean korisnikJeMenadzer(@Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("MENADZER")) {
				return true;
			}
		}	
		return false;
	}
	
	private boolean korisnikJeDostavljac(@Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("DOSTAVLJAC")) {
				return true;
			}
		}	
		return false;
	}
	
	private boolean korisnikJeKupac(@Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		
		if(korisnik!= null) {
			if(korisnik.getUloga().equals("KUPAC")) {
				return true;
			}
		}	
		return false;
	}
	
	@SuppressWarnings("unused")
	private KorisnikDAO dobaviKorisnike() {
		KorisnikDAO korisnici = (KorisnikDAO) ctx.getAttribute("korisnici");
		
		if (korisnici == null) {
			korisnici = new KorisnikDAO();
			korisnici.ucitajKorisnike();
			ctx.setAttribute("korisnici", korisnici);
		}
		return korisnici;
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
	private KomentarDAO dobaviKomentareDAO() {
		KomentarDAO komentari = (KomentarDAO) ctx.getAttribute("komentari");
		
		if(komentari == null) {
			komentari = new KomentarDAO();
			komentari.ucitajKomentare();
			
			ctx.setAttribute("komentari", komentari);
		}
		return komentari;		
	}
}
