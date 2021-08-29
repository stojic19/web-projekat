package services;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Slika;
import dao.SlikaDAO;

@Path("/slike")
public class SlikaServis {

	@Context
	ServletContext ctx;
	
	@GET
	@Path("/dobaviSlike")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Slika> dobaviSlike(){
		return dobaviSlikeDAO().getValues();
	}
	
	private SlikaDAO dobaviSlikeDAO() {
		SlikaDAO slike = (SlikaDAO) ctx.getAttribute("slike");
		
		if(slike == null) {
			slike = new SlikaDAO();
			slike.ucitajSlikeJSON();
			
			ctx.setAttribute("slike", slike);
		}
		return slike;		
	}
}
