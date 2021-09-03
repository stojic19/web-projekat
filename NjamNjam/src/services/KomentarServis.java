package services;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Korisnik;
import dao.KomentarDAO;

/**
 * Servlet implementation class KomentarServis
 */
@WebServlet("/KomentarServis")
public class KomentarServis {
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/dobaviKomentare")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dobaviKomentare(@Context HttpServletRequest request){
		if(korisnikJeAdmin(request)) {
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS SHOW")
					.entity(dobaviKomentareDAO().getKomentari())
					.build();
			}else{
				return Response.status(403).type("text/plain")
					.entity("Nedozvoljen pristup!").build();
				}
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
}
