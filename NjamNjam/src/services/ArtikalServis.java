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
	
	@GET
	@Path("/dobaviArtikleMenadzera")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviArtikleMenadzera(@Context HttpServletRequest request) {	
		
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("ulogovanKorisnik");
			System.out.println("korime:" + korisnik.getKorisnickoIme());
			System.out.println("uloga:" + korisnik.getUloga());
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
						.entity(dobaviArtikleDAO().dobaviArtiklePoIdRestorana(korisnik.getIdRestorana()))
						.build();	
			}
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
