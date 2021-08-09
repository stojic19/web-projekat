package beans;

public class Lokacija {
	
	private String geografskaDuzina;
	private String geografskaSirina;
	private Adresa adresa;
	
	public Lokacija() {
		
	}

	public Lokacija(String geografskaDuzina, String geografskaSirina, Adresa adresa) {
		super();
		this.geografskaDuzina = geografskaDuzina;
		this.geografskaSirina = geografskaSirina;
		this.adresa = adresa;
	}

	public String getGeografskaDuzina() {
		return geografskaDuzina;
	}

	public void setGeografskaDuzina(String geografskaDuzina) {
		this.geografskaDuzina = geografskaDuzina;
	}

	public String getGeografskaSirina() {
		return geografskaSirina;
	}

	public void setGeografskaSirina(String geografskaSirina) {
		this.geografskaSirina = geografskaSirina;
	}

	public Adresa getAdresa() {
		return adresa;
	}

	public void setAdresa(Adresa adresa) {
		this.adresa = adresa;
	}
	
}
