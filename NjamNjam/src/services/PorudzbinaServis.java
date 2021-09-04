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
import beans.Porudzbina;
import dao.KorisnikDAO;
import dao.PorudzbinaDAO;
import dto.PorudzbinaJSONDTO;

@Path("/Porudzbina")
public class PorudzbinaServis {

	@Context
	ServletContext ctx;
	
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
	@Path("/pripremiPorudzbinu")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pripremiPorudzbinu(PorudzbinaJSONDTO porudzbina, @Context HttpServletRequest request) {
		if(korisnikJeMenadzer(request)) {
			
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.pripremiPorudzbinu(porudzbina.porudzbina);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(dobaviPorudzbineDAO().getValues())
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
		if(korisnikJeMenadzer(request)) {
			
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.cekaDostavljacaPorudzbina(porudzbina.porudzbina);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(dobaviPorudzbineDAO().getValues())
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	
	@POST
	@Path("/transportujPorudzbinu")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response transportujPorduzbinu(PorudzbinaJSONDTO porudzbina, @Context HttpServletRequest request) {
		if(korisnikJeDostavljac(request)) {
			
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.transportujPorudzbinu(porudzbina.porudzbina);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(dobaviPorudzbineDAO().getValues())
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
		if(korisnikJeMenadzer(request)) {
			
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.dostaviPorudzbinu(porudzbina.porudzbina);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(dobaviPorudzbineDAO().getValues())
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	
	@POST
	@Path("/otkaziPorudzbinu")                                //dodati racunanje bodova
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response otkaziPorudzbinu(PorudzbinaJSONDTO porudzbina, @Context HttpServletRequest request) {
		if(korisnikJeKupac(request) && porudzbina.porudzbina.getStatus().equals("OBRADA")) {
			
			PorudzbinaDAO porudzbine = dobaviPorudzbineDAO();
			porudzbine.otkaziPorudzbinu(porudzbina.porudzbina);
			
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS CHANGED")
					.entity(dobaviPorudzbineDAO().getValues())
					.build();
		}
		
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
	}
	
	/*
	@GET
	@Path("/dobaviPorudzbineZaDostavljaca")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Porudzbina> dobaviPorudzbineZaDostavljaca() {	
		return dobaviPorudzbineZaDostavljacaDAO().getValues();
	}
	*/
	

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
	
	private KorisnikDAO dobaviKorisnike() {
		KorisnikDAO korisnici = (KorisnikDAO) ctx.getAttribute("korisnici");
		
		if (korisnici == null) {
			korisnici = new KorisnikDAO();
			korisnici.ucitajKorisnike();
			ctx.setAttribute("korisnici", korisnici);
		}
		return korisnici;
	}
	
	
	/*
	private PorudzbinaDAO dobaviPorudzbineZaDostavljacaDAO() {
		PorudzbinaDAO porudzbine = (PorudzbinaDAO) ctx.getAttribute("porudzbine");
		if(porudzbine == null) {
			porudzbine = new PorudzbinaDAO();
			porudzbine.UcitajPorudzbineZaDostavljaca();
			ctx.setAttribute("porudzbine", porudzbine);
		}
		return porudzbine;
	}
	*/
	
}
