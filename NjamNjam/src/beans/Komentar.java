package beans;

public class Komentar {

	private Integer ID;
	
	private Integer idKupca;
	private Integer idRestorana;
	private String idPorudzbine;
	private String tekst;
	private Double ocena;
	private String status;
	
	public Komentar() {
		
	}

	public Komentar(Integer iD, Integer idKupca, Integer idRestorana, String idPorudzbine, String tekst, Double ocena, String status) {
		super();
		ID = iD;
		this.idKupca = idKupca;
		this.idRestorana = idRestorana;
		this.idPorudzbine = idPorudzbine;
		this.tekst = tekst;
		this.ocena = ocena;
		this.status = status;
	}

	public String getIdPorudzbine() {
		return idPorudzbine;
	}

	public void setIdPorudzbine(String idPorudzbine) {
		this.idPorudzbine = idPorudzbine;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Integer getIdKupca() {
		return idKupca;
	}

	public void setIdKupca(Integer idKupca) {
		this.idKupca = idKupca;
	}

	public Integer getIdRestorana() {
		return idRestorana;
	}

	public void setIdRestorana(Integer idRestorana) {
		this.idRestorana = idRestorana;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public Double getOcena() {
		return ocena;
	}

	public void setOcena(Double ocena) {
		this.ocena = ocena;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
