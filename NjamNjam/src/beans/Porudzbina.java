package beans;

import java.util.List;

public class Porudzbina {

	private String ID;
	
	private List<Integer> idArtikala;
	private Integer idRestorana;
	private String vremePorudzbine;
	private String cena;
	private String imePrezimeKupca;
	private String status;
	
	private String imeRestorana;
	private String tipRestorana;
	
	public Porudzbina(String iD, List<Integer> idArtikala, Integer idRestorana, String imeRestorana, String tipRestorana, String vremePorudzbine, String cena,
			String imePrezimeKupca, String status) {
		super();
		ID = iD;
		this.idArtikala = idArtikala;
		this.idRestorana = idRestorana;
		this.vremePorudzbine = vremePorudzbine;
		this.cena = cena;
		this.imePrezimeKupca = imePrezimeKupca;
		this.status = status;
		
		this.setImeRestorana(imeRestorana);
		this.idRestorana = idRestorana;
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

	public String getVremePorudzbine() {
		return vremePorudzbine;
	}

	public void setVremePorudzbine(String vremePorudzbine) {
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

	public String getTipRestorana() {
		return tipRestorana;
	}

	public void setTipRestorana(String tipRestorana) {
		this.tipRestorana = tipRestorana;
	}

	public String getImeRestorana() {
		return imeRestorana;
	}

	public void setImeRestorana(String imeRestorana) {
		this.imeRestorana = imeRestorana;
	}
	
	
}
