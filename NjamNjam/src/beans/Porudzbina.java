package beans;

import java.sql.Date;
import java.util.List;

public class Porudzbina {

	private String ID;
	
	private List<Integer> idArtikala;
	private Integer idRestorana;
	private Date vremePorudzbine;
	private String cena;
	private String imePrezimeKupca;
	private String status;
	
	public Porudzbina(String iD, List<Integer> idArtikala, Integer idRestorana, Date vremePorudzbine, String cena,
			String imePrezimeKupca, String status) {
		super();
		ID = iD;
		this.idArtikala = idArtikala;
		this.idRestorana = idRestorana;
		this.vremePorudzbine = vremePorudzbine;
		this.cena = cena;
		this.imePrezimeKupca = imePrezimeKupca;
		this.status = status;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public List<Integer> getIdArtikala() {
		return idArtikala;
	}

	public void setIdArtikala(List<Integer> idArtikala) {
		this.idArtikala = idArtikala;
	}

	public Integer getIdRestorana() {
		return idRestorana;
	}

	public void setIdRestorana(Integer idRestorana) {
		this.idRestorana = idRestorana;
	}

	public Date getVremePorudzbine() {
		return vremePorudzbine;
	}

	public void setVremePorudzbine(Date vremePorudzbine) {
		this.vremePorudzbine = vremePorudzbine;
	}

	public String getCena() {
		return cena;
	}

	public void setCena(String cena) {
		this.cena = cena;
	}

	public String getImePrezimeKupca() {
		return imePrezimeKupca;
	}

	public void setImePrezimeKupca(String imePrezimeKupca) {
		this.imePrezimeKupca = imePrezimeKupca;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
