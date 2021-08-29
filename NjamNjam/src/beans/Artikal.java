package beans;

public class Artikal {
	
	private Integer ID;
	private Integer logickiObrisan;						// 1 - obrisan, 0 - nije obrisan
	
	private String naziv;
	private String cena;
	private String tip;
	private Integer idRestoranaKomPripada;
	private String kolicina;
	private String opis;
	private String putanjaDoSlike;
	
	public String getPutanjaDoSlike() {
		return putanjaDoSlike;
	}

	public void setPutanjaDoSlike(String putanjaDoSlike) {
		this.putanjaDoSlike = putanjaDoSlike;
	}

	public Artikal() {
		
	}

	public Artikal(Integer iD, Integer logickiObrisan, String naziv, String cena, String tip,
			Integer idRestoranaKomPripada, String kolicina, String opis, String putanjaDoSlike) {
		super();
		ID = iD;
		this.logickiObrisan = logickiObrisan;
		this.naziv = naziv;
		this.cena = cena;
		this.tip = tip;
		this.idRestoranaKomPripada = idRestoranaKomPripada;
		this.kolicina = kolicina;
		this.opis = opis;
		this.putanjaDoSlike = putanjaDoSlike;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Integer getLogickiObrisan() {
		return logickiObrisan;
	}

	public void setLogickiObrisan(Integer logickiObrisan) {
		this.logickiObrisan = logickiObrisan;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getCena() {
		return cena;
	}

	public void setCena(String cena) {
		this.cena = cena;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Integer getIdRestoranaKomPripada() {
		return idRestoranaKomPripada;
	}

	public void setIdRestoranaKomPripada(Integer idRestoranaKomPripada) {
		this.idRestoranaKomPripada = idRestoranaKomPripada;
	}

	public String getKolicina() {
		return kolicina;
	}

	public void setKolicina(String kolicina) {
		this.kolicina = kolicina;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}
	
}
