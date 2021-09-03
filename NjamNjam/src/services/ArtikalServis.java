package services;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
