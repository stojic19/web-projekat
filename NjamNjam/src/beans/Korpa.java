package beans;


import java.util.HashMap;

public class Korpa {

	private Integer ID;
	
	private  HashMap<Integer, Integer> artikli;	// Id artikla kljuc, vrednost kolicina
	private Double cena;
	private Integer popust;					// Azurira se kada korisnik predje na novi tip
	
	public Korpa() {}
	
	public Korpa(Integer iD) {
		super();
		ID = iD;
		this.artikli = new HashMap<Integer, Integer>();
		this.cena = 0.0;
		this.popust = 0;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public HashMap<Integer, Integer> getArtikli() {
		return artikli;
	}

	public void setArtikli(HashMap<Integer, Integer> artikli) {
		this.artikli = artikli;
	}

	public Double getCena() {
		return cena;
	}

	public void setCena(Double cena) {
		this.cena = cena;
	}
	
	public Integer getPopust() {
		return popust;
	}

	public void setPopust(Integer popust) {
		this.popust = popust;
	}

	public void dodajArtikal(Integer idArtikla, Integer kolicina)
	{
		if(artikli.containsKey(idArtikla))
		{
			Integer staraKolicina = artikli.get(idArtikla);
			artikli.put(idArtikla, staraKolicina + kolicina);
		}
		else
		{
			artikli.put(idArtikla, kolicina);
		}
	}
	
	public void dodajNaCenu(Double suma)
	{
		cena += suma;
	}

	public void azurirajArtikal(Integer id, Integer kolicinaZaKupovinu, Double cenaArtikla) {
		Integer staraKolicina = artikli.get(id);
		artikli.put(id, kolicinaZaKupovinu);
		cena -= (staraKolicina - kolicinaZaKupovinu) * cenaArtikla;
		if(kolicinaZaKupovinu == 0)
		{
			artikli.remove(id);
		}
	}

	public void ukloniSveIzKorpe() {
		artikli = new HashMap<Integer,Integer>();
		cena = 0.0;
	}
}
