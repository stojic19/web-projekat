package services;

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
import dao.KomentarDAO;
import dto.KomentarJSONDTO;

/**
 * Servlet implementation class KomentarServis
 */
@Path("/komentar")
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
	
	@POST
	@Path("/prihvatiKomentar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response prihvatiKomentar(KomentarJSONDTO komentar,@Context HttpServletRequest request){
		if(korisnikJeAdmin(request) || korisnikJeMenadzer(request)) {
			KomentarDAO komentari = dobaviKomentareDAO();
			komentari.prihvatiKomentar(komentar.komentar.getID());		
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS BLOCK")
					.entity(dobaviKomentareDAO().getKomentari())
					.build();
		}
		else {
		return Response.status(403).type("text/plain")
				.entity("Nedozvoljen pristup!").build();
			}
		}
	
	@POST
	@Path("/odbijKomentar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response odbijKomentar(KomentarJSONDTO komentar,@Context HttpServletRequest request){
		if(korisnikJeAdmin(request) || korisnikJeMenadzer(request)) {
			KomentarDAO komentari = dobaviKomentareDAO();
			komentari.odbijKomentar(komentar.komentar.getID());		
			return Response
					.status(Response.Status.ACCEPTED).entity("SUCCESS BLOCK")
					.entity(dobaviKomentareDAO().getKomentari())
					.build();
		}
		else {
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
