package services;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Korisnik;
import beans.Korpa;
import dao.KorisnikDAO;
import dao.KorpaDAO;
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
}
