package services;

import java.util.ArrayList;

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

import beans.Artikal;
import beans.Korisnik;
import beans.Korpa;
import beans.Restoran;
import beans.Slika;
import dao.ArtikalDAO;
import dao.KorisnikDAO;
import dao.KorpaDAO;
import dao.RestoranDAO;
import dao.SlikaDAO;
import dto.ArtikalIzmenaDTO;
import dto.ArtikalJSONDTO;
import dto.RestoranJSONDTO;

@Path("/artikal")
public class ArtikalServis {

	@Context
	ServletContext ctx;
	
	@POST
	@Path("/dobaviArtikleRestorana")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response dobaviArtikleRestorana(RestoranJSONDTO restoran){
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity(dobaviArtikleDAO().dobaviArtiklePoIdRestorana(restoran.restoran.getID()))
					.build();
			
	}
	
	@POST
	@Path("/dodajArtikal")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response dodajArtikal(ArtikalJSONDTO artikal,@Context HttpServletRequest request){
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		ArrayList<Artikal> artikliRestorana = dobaviArtikleDAO().dobaviArtiklePoIdRestorana(korisnik.getIdRestorana());
		for(Artikal artikalZaProveru : artikliRestorana)
		{
			if(artikalZaProveru.getNaziv().toLowerCase().equals(artikal.artikal.getNaziv().toLowerCase()))
				return Response
						.status(Response.Status.BAD_REQUEST)
						.entity("Već postoji artikal sa istim imenom.")
						.build();
		}
		SlikaDAO slike = dobaviSlike();
		if(!artikal.artikal.getPutanjaDoSlike().equals("nema"))
		{
			Slika slika = slike.dodajNovuSliku(artikal.artikal.getPutanjaDoSlike());
			artikal.artikal.setPutanjaDoSlike(Integer.toString(slika.getID())); 	
		}
		artikal.artikal.setIdRestoranaKomPripada(korisnik.getIdRestorana());
		dobaviRestoraneDAO().dodajArtikalRestoranu(artikal.artikal);
		dobaviArtikleDAO().dodajNoviArtikal(artikal.artikal);
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity("Uspešno dodat artikal.")
					.build();
			
	}
	
	@GET
	@Path("/dobaviArtikleIzKorpe")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviArtikleIzKorpe(@Context HttpServletRequest request) {	
		
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
			ArtikalDAO artikli = dobaviArtikleDAO();
			if(korisnik != null)
			if(korisnik.getUloga().equals("KUPAC")) {
			{
				KorpaDAO korpe = dobaviKorpeDAO();
				if(korisnik.getIdKorpe() == null)
				{
					KorisnikDAO korisnikDAO = dobaviKorisnike();
					korisnikDAO.dodajKorpuKorisniku(korisnik.getID(), korpe.dodajKorpu());
					korisnik = korisnikDAO.dobaviKorisnikaPoKorisnickomImenu(korisnik.getKorisnickoIme());
					request.getSession().setAttribute("ulogovanKorisnik", korisnik);
				}
				Korpa korpa = korpe.nadjiKorpuPoId(korisnik.getIdKorpe());
				
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(artikli.dobaviArtikleZaKorpu(korpa.getArtikli()))
						.build();	
			}
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/dobaviArtikleMenadzera")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviArtikleMenadzera(@Context HttpServletRequest request) {	
		
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
			ArtikalDAO artikli = dobaviArtikleDAO();
			if(korisnik != null)
			if(korisnik.getUloga().equals("MENADZER")) {
			if(korisnik.getIdRestorana() == -1 || korisnik.getIdRestorana() == 0)
			{
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(null)
						.build();
			}else {
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(artikli.dobaviArtiklePoIdRestorana(korisnik.getIdRestorana()))
						.build();	
			}
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@GET
	@Path("/dobaviArtikleRestorana")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviArtikleRestorana(@Context HttpServletRequest request) {	
		
			Restoran restoran = (Restoran) request.getSession().getAttribute("restoranZaPregled");
			ArtikalDAO artikli = dobaviArtikleDAO();
			if(restoran != null)
			{
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(artikli.dobaviArtiklePoIdRestorana(restoran.getID()))
						.build();	
			}
		return Response.status(403).type("text/plain")
				.entity("Greška!").build();
	}
	
	@GET
	@Path("/daLiMenadzerImaRestoran")
	@Produces(MediaType.APPLICATION_JSON)
	public Response daLiMenadzerImaRestoran(@Context HttpServletRequest request) {	
		
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
			
			System.out.println(korisnik.getKorisnickoIme() + " " + korisnik.getIdRestorana() + " " + korisnik.getUloga());
			
			if(korisnik.getUloga().contains("MENADZER")) 
			{
				System.out.println("Menadzer je");
				if(korisnik.getIdRestorana() == -1 || korisnik.getIdRestorana() == 0)
				{
					System.out.println("NEMA RESTORAN");
					return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(false)
						.build();
				}else {
					return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(true)
						.build();	
				}
			}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	@POST
	@Path("/izmeniArtikal")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response izmeniArtikal(ArtikalIzmenaDTO artikal,@Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnik.getUloga().contains("MENADZER")) {
			SlikaDAO slike = dobaviSlike();
			if(!isNumeric(artikal.putanjaDoSlike))
			{
				Slika slika = slike.dodajNovuSliku(artikal.putanjaDoSlike);
				artikal.putanjaDoSlike = Integer.toString(slika.getID()); 	
			}
			ArtikalDAO artikli = dobaviArtikleDAO();
			artikli.izmenaArtikla(artikal);
	
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(artikli.dobaviArtiklePoIdRestorana(korisnik.getIdRestorana()))
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
	@Path("/obrisiArtikal")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response obrisiArtikal(ArtikalJSONDTO artikal,@Context HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
		if(korisnik.getUloga().contains("MENADZER")) {
				ArtikalDAO artikli = dobaviArtikleDAO();
				artikli.obrisiArtikal(artikal.artikal.getID());
						
				return Response
						.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
						.entity(artikli.dobaviArtiklePoIdRestorana(korisnik.getIdRestorana()))
						.build();
		}
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
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
