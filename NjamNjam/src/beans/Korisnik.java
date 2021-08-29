package beans;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Korisnik {
	
	private Integer ID;
	private Integer logickiObrisan;						// 1 - obrisan, 0 - nije obrisan
	private Integer blokiran;							// 1 - blokiran, 0 - nije blokiran
	
	private String korisnickoIme;
	private String lozinka;
	private String ime;
	private String prezime;
	private String pol;
	private Date datumRodjenja;
	private String uloga;
	
	private List<Integer> idPorudzbina;			// sve porudzbine kupca / dostavljaca
	private Integer idKorpe;								// korpa kupca
	private Integer idRestorana;							// restoran menadzera
	private Integer brojSakupljenihBodova;
	private TipKupca tip;
	
	public Korisnik () {
		
	}

	public Korisnik(Integer iD, Integer logickiObrisan, Integer blokiran, String korisnickoIme, String lozinka,
			String ime, String prezime, String pol, Date datumRodjenja, String uloga) {
		super();
		ID = iD;
		this.logickiObrisan = logickiObrisan;
		this.blokiran = blokiran;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.datumRodjenja = datumRodjenja;
		this.uloga = uloga;
		this.idPorudzbina = new ArrayList<Integer>();
		this.idKorpe = -1;
		this.idRestorana = -1;
		this.brojSakupljenihBodova = 0;
		// this.tip = tip;	//TODO: RESITI OVO ZA TIPOVE KUPCA
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

	public Integer getBlokiran() {
		return blokiran;
	}

	public void setBlokiran(Integer blokiran) {
		this.blokiran = blokiran;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public Date getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(Date datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public String getUloga() {
		return uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
	}

	public List<Integer> getIdPorudzbina() {
		return idPorudzbina;
	}

	public void setIdPorudzbina(List<Integer> idPorudzbina) {
		this.idPorudzbina = idPorudzbina;
	}

	public Integer getIdKorpe() {
		return idKorpe;
	}

	public void setIdKorpe(Integer idKorpe) {
		this.idKorpe = idKorpe;
	}

	public Integer getIdRestorana() {
		return idRestorana;
	}

	public void setIdRestorana(Integer idRestorana) {
		this.idRestorana = idRestorana;
	}

	public Integer getBrojSakupljenihBodova() {
		return brojSakupljenihBodova;
	}

	public void setBrojSakupljenihBodova(Integer brojSakupljenihBodova) {
		this.brojSakupljenihBodova = brojSakupljenihBodova;
	}

	public TipKupca getTip() {
		return tip;
	}

	public void setTip(TipKupca tip) {
		this.tip = tip;
	}
	
}
