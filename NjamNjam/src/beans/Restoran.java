package beans;

import java.util.List;

public class Restoran {

	private Integer ID;
	private Integer logickiObrisan;						// 1 - obrisan, 0 - nije obrisan
	
	private String naziv;
	private String tip;
	private List<Integer> idArtiklaUPonudi;
	private Integer status; 							// 1 - radi, 0 - ne radi
	private Lokacija lokacija;
	// TO DO: logo restorana
	private Integer idMenadzera;
	
	public Restoran() {
		
	}

	public Restoran(Integer iD, Integer logickiObrisan, String naziv, String tip, List<Integer> idArtiklaUPonudi,
			Integer status, Lokacija lokacija, Integer idMenadzera) {
		super();
		ID = iD;
		this.logickiObrisan = logickiObrisan;
		this.naziv = naziv;
		this.tip = tip;
		this.idArtiklaUPonudi = idArtiklaUPonudi;
		this.status = status;
		this.lokacija = lokacija;
		this.idMenadzera = idMenadzera;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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
