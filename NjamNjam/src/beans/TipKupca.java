package beans;

public class TipKupca {
	
	private String ime;
	private Integer popust;
	private Integer trazeniBrojBodova;
	
	public TipKupca() {}
	
	public TipKupca(String ime, Integer popust, Integer trazeniBrojBodova) {
		super();
		this.ime = ime;
		this.popust = popust;
		this.trazeniBrojBodova = trazeniBrojBodova;
	}
	
	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public Integer getPopust() {
		return popust;
	}

	public void setPopust(Integer popust) {
		this.popust = popust;
	}

	public Integer getTrazeniBrojBodova() {
		return trazeniBrojBodova;
	}

	public void setTrazeniBrojBodova(Integer trazeniBrojBodova) {
		this.trazeniBrojBodova = trazeniBrojBodova;
	}
}
