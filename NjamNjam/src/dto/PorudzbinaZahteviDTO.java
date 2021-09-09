package dto;

import java.util.ArrayList;

import beans.Korisnik;
public class PorudzbinaZahteviDTO {

	public String ID;
	
	public Integer idRestorana;
	public String vremePorudzbine;
	public Double cena;
	public String imePrezimeKupca;
	public String status;
	
	public String imeRestorana;
	public String tipRestorana;
	
	public ArrayList<Korisnik> dostavljaci;
	
}
