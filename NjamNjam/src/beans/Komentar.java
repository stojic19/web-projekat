package beans;

public class Komentar {

	private Integer ID;
	
	private Integer idKupca;
	private Integer idRestorana;
	private Integer idPorudzbine;
	private String tekst;
	private Integer ocena;
	private String status;
	
	public Komentar() {
		
	}

	public Komentar(Integer iD, Integer idKupca, Integer idRestorana, Integer idPorudzbine, String tekst, Integer ocena, String status) {
		super();
		ID = iD;
		this.idKupca = idKupca;
		this.idRestorana = idRestorana;
		this.idPorudzbine = idPorudzbine;
		this.tekst = tekst;
		this.ocena = ocena;
		this.status = status;
	}

	public Integer getIdPorudzbine() {
		return idPorudzbine;
	}

	public void setIdPorudzbine(Integer idPorudzbine) {
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

	public Integer getOcena() {
		return ocena;
	}

	public void setOcena(Integer ocena) {
		this.ocena = ocena;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
