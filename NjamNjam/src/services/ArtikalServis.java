package services;

import java.util.ArrayList;

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

import beans.Artikal;
import beans.Korisnik;
import dao.ArtikalDAO;
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
			if(artikalZaProveru.getNaziv().toLowerCase().equals(artikal.artikal.getOpis().toLowerCase()))
				return Response
						.status(Response.Status.BAD_REQUEST)
						.entity("Već postoji artikal sa istim imenom.")
						.build();
		}
		artikal.artikal.setIdRestoranaKomPripada(korisnik.getIdRestorana());
		dobaviArtikleDAO().dodajNoviArtikal(artikal.artikal);
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity("Uspešno dodat artikal.")
					.build();
			
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
	
	private ArtikalDAO dobaviArtikleDAO() {
		ArtikalDAO artikli = (ArtikalDAO) ctx.getAttribute("artikli");
		if(artikli == null) {
			artikli = new ArtikalDAO();
			artikli.ucitajArtikle();
			ctx.setAttribute("artikli", artikli);
		}
		return artikli;		
	}
}
