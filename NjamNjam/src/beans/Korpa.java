package beans;

import java.util.Map;

public class Korpa {

	private Integer ID;
	
	private  Map<Integer, Integer> artikli;	// Id artikla kljuc, vrednost kolicina
	private Integer cena;
	
	public Korpa(Integer iD, Map<Integer, Integer> artikli, Integer cena) {
		super();
		ID = iD;
		this.artikli = artikli;
		this.cena = cena;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Map<Integer, Integer> getArtikli() {
		return artikli;
	}

	public void setArtikli(Map<Integer, Integer> artikli) {
		this.artikli = artikli;
	}

	public Integer getCena() {
		return cena;
	}

	public void setCena(Integer cena) {
		this.cena = cena;
	}
	
	
}
