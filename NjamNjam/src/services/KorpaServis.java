package services;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

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
import beans.Korpa;
import beans.Porudzbina;
import beans.Restoran;
import dao.ArtikalDAO;
import dao.KorisnikDAO;
import dao.KorpaDAO;
import dao.PorudzbinaDAO;
import dao.RestoranDAO;
import dto.ArtikalUKorpiDTO;
import dto.ArtikalZaKorpuDTO;


@Path("/korpa")
public class KorpaServis {

	@Context
	ServletContext ctx;
	
	@POST
	@Path("/dodajArtikalUKorpu")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response dodajArtikalUKorpu(ArtikalZaKorpuDTO artikal,@Context HttpServletRequest request){
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnik != null)
		if(korisnik.getUloga().contains("KUPAC")) {
			KorpaDAO korpe = dobaviKorpeDAO();
			if(korisnik.getIdKorpe() == null)
			{
				KorisnikDAO korisnikDAO = dobaviKorisnike();
				korisnikDAO.dodajKorpuKorisniku(korisnik.getID(), korpe.dodajKorpu());
				korisnik = korisnikDAO.dobaviKorisnikaPoKorisnickomImenu(korisnik.getKorisnickoIme());
				request.getSession().setAttribute("ulogovanKorisnik", korisnik);
			}
			Korpa korpa = korpe.nadjiKorpuPoId(korisnik.getIdKorpe());
			korpa.dodajArtikal(artikal.idArtikla, artikal.kolicina);
			korpa.dodajNaCenu(artikal.cena * artikal.kolicina);
			korpe.azurirajKorpu(korpa);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity("Uspešno dodat artikal u korpu.")
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/izmeniKolicinuArtikla")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response izmeniKolicinuArtikla(ArtikalUKorpiDTO artikal,@Context HttpServletRequest request){
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnik != null)
		if(korisnik.getUloga().contains("KUPAC")) {
			KorpaDAO korpe = dobaviKorpeDAO();
			Korpa korpa = korpe.nadjiKorpuPoId(korisnik.getIdKorpe());
			korpa.azurirajArtikal(artikal.id, artikal.kolicinaZaKupovinu, artikal.cena);
			korpe.azurirajKorpu(korpa);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity("Uspešno izmenjena količina artikla " + artikal.naziv + ".")
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/ukloniArtikalIzKorpe")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ukloniArtikalIzKorpe(ArtikalUKorpiDTO artikal,@Context HttpServletRequest request){
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnik != null)
		if(korisnik.getUloga().contains("KUPAC")) {
			KorpaDAO korpe = dobaviKorpeDAO();
			Korpa korpa = korpe.nadjiKorpuPoId(korisnik.getIdKorpe());
			korpa.azurirajArtikal(artikal.id, 0, artikal.cena);
			korpe.azurirajKorpu(korpa);
			ArtikalDAO artikli = dobaviArtikleDAO();
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity(artikli.dobaviArtikleZaKorpu(korpa.getArtikli()))
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/ukloniSveIzKorpe")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ukloniSveIzKorpe(@Context HttpServletRequest request) {	
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnik != null)
		if(korisnik.getUloga().contains("KUPAC")) {
			KorpaDAO korpe = dobaviKorpeDAO();
			Korpa korpa = korpe.nadjiKorpuPoId(korisnik.getIdKorpe());
			korpa.ukloniSveIzKorpe();
			korpe.azurirajKorpu(korpa);
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity(null)
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/poruci")
	@Produces(MediaType.APPLICATION_JSON)
	public Response poruci(@Context HttpServletRequest request) {	
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnik != null)
		if(korisnik.getUloga().contains("KUPAC")) {
			KorpaDAO korpe = dobaviKorpeDAO();
			Korpa korpa = korpe.nadjiKorpuPoId(korisnik.getIdKorpe());
			//Kreirati porudzbine za svaki restoran pojedinacno, dodati bodove korisniku
			ArtikalDAO artikli = dobaviArtikleDAO();
			RestoranDAO restorani = dobaviRestoraneDAO();
			ArrayList<ArtikalUKorpiDTO> artikliIzKorpe = artikli.dobaviArtikleZaKorpu(korpa.getArtikli());
			
			HashMap<Integer,Integer> restoraniZaPorudzbine = new HashMap<Integer, Integer>(); //Evidencija svih restorana koji se nalaze u porudzbini
			for(ArtikalUKorpiDTO artikal : artikliIzKorpe)	
			{
				if(!restoraniZaPorudzbine.containsKey(artikal.idRestoranaKomPripada))
					restoraniZaPorudzbine.put(artikal.idRestoranaKomPripada, 1);
			}
			
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			for(Integer restoran : restoraniZaPorudzbine.keySet())	//Prolazimo kroz listu artikala i pravimo posebnu porudzbinu za svaki restoran
			{
				Restoran restoranModel = restorani.nadjiRestoranPoID(restoran);
				Porudzbina porudzbina = new Porudzbina(restoran, restoranModel.getNaziv(), restoranModel.getTip(), vremePorudzbine(),korisnik.getIme() + korisnik.getPrezime());
				for(ArtikalUKorpiDTO artikal : artikliIzKorpe)
				{
					if(restoran == artikal.idRestoranaKomPripada)
					{
						// Dodavanje artikla sa kolicinom, cenom i popustom
						porudzbina.dodajArtikal(artikal.id, artikal.kolicinaZaKupovinu, artikal.cena, korpa.getPopust());	
					}
				}
				// Dodavanje ID porudzbine kupcu
				korisnik.dodajIdPorudzbine(porudzbina.getID());
				// Dodavanje porudzbine u datoteku
				porudzbine.dodajNovuPorudzbinu(porudzbina);
				// Dodavanje kupca u listu musterija restorana ukoliko se tamo vec ne nalazi
				restorani.dodajKupcaUListuMusterija(restoran, korisnik.getID());
			}
			// Dodavanje bodova kupcu, provera da li je promenio tip, cuvanje promena u fajlu i u sesiji
			korisnik.dodajBodove(korpa.getCena());
			KorisnikDAO korisnici = dobaviKorisnike();
			korisnici.promeniKorisnikaNakonKupovine(korisnik);
			request.getSession().setAttribute("ulogovanKorisnik", korisnik);
			//Nakon kreiranja porudzbina, korpa se prazni
			korpa.ukloniSveIzKorpe();
			korpa.setPopust(korisnik.getTip().getPopust());
			korpe.azurirajKorpu(korpa);
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity(null)
					.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	private String vremePorudzbine() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}
	
	@SuppressWarnings("unused")
	private KorpaDAO dobaviKorpeDAO() {
		KorpaDAO korpe = (KorpaDAO) ctx.getAttribute("korpe");
		if (korpe == null) {
			korpe = new KorpaDAO();
			korpe.ucitajKorpe();
			ctx.setAttribute("korpe", korpe);
		}
		return korpe;
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
	private ArtikalDAO dobaviArtikleDAO() {
		ArtikalDAO artikli = (ArtikalDAO) ctx.getAttribute("artikli");
		if(artikli == null) {
			artikli = new ArtikalDAO();
			artikli.ucitajArtikle();
			ctx.setAttribute("artikli", artikli);
		}
		return artikli;		
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
	private RestoranDAO dobaviRestoraneDAO() {
		RestoranDAO restorani = (RestoranDAO) ctx.getAttribute("restorani");
		if (restorani == null) {
			restorani = new RestoranDAO();
			restorani.ucitajRestorane();
			ctx.setAttribute("restorani", restorani);
		}
		return restorani;
	}
}
