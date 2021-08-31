package beans;

import java.util.List;

public class Restoran {

	private Integer ID;
	private Integer logickiObrisan;						// 1 - obrisan, 0 - nije obrisan
	
	private String naziv;
	private String tip;
	private List<Integer> idArtiklaUPonudi;
	private String status; 							// 1 - radi, 0 - ne radi
	private Lokacija lokacija;
	private String putanjaDoSlike;
	private Integer idMenadzera;
	private Double prosecnaOcena;
	
	public Restoran() {
		
	}

	public Restoran(Integer iD, Integer logickiObrisan, String naziv, String tip, List<Integer> idArtiklaUPonudi,
			String status, Lokacija lokacija,String putanjaDoSlike, Integer idMenadzera) {
		super();
		ID = iD;
		this.logickiObrisan = logickiObrisan;
		this.naziv = naziv;
		this.tip = tip;
		this.idArtiklaUPonudi = idArtiklaUPonudi;
		this.status = status;
		this.lokacija = lokacija;
		this.putanjaDoSlike = putanjaDoSlike;
		this.idMenadzera = idMenadzera;
		this.prosecnaOcena = 0.0;
	}

	public Double getProsecnaOcena() {
		return prosecnaOcena;
	}

	public void setProsecnaOcena(Double prosecnaOcena) {
		this.prosecnaOcena = prosecnaOcena;
	}

	public String getPutanjaDoSlike() {
		return putanjaDoSlike;
	}

	public void setPutanjaDoSlike(String putanjaDoSlike) {
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

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public List<Integer> getIdArtiklaUPonudi() {
		return idArtiklaUPonudi;
	}

	public void setIdArtiklaUPonudi(List<Integer> idArtiklaUPonudi) {
		this.idArtiklaUPonudi = idArtiklaUPonudi;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Lokacija getLokacija() {
		return lokacija;
	}

	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}

	public Integer getIdMenadzera() {
		return idMenadzera;
	}

	public void setIdMenadzera(Integer idMenadzera) {
		this.idMenadzera = idMenadzera;
	}
	
}
