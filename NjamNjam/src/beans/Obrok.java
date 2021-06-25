package beans;

public class Obrok {
	private String id;
	private String nazivJela;
	private String imeIPrezimeSefaKuhinje;
	private double kolicinaSastojaka;
	private String vremeSluzenja;
	private Double cena;
	private boolean dopuna;
	
	public boolean isDopuna() {
		return dopuna;
	}

	public void setDopuna(boolean dopuna) {
		this.dopuna = dopuna;
	}

	public Obrok() {}
	
	public Obrok(String id, String nazivJela, String imeIPrezimeSefaKuhinje, double kolicinaSastojaka,
			String vremeSluzenja, Double cena, boolean dopuna) {
		super();
		this.id = id;
		this.nazivJela = nazivJela;
		this.imeIPrezimeSefaKuhinje = imeIPrezimeSefaKuhinje;
		this.kolicinaSastojaka = kolicinaSastojaka;
		this.vremeSluzenja = vremeSluzenja;
		this.cena = cena;
		this.dopuna = dopuna;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNazivJela() {
		return nazivJela;
	}
	public void setNazivJela(String nazivJela) {
		this.nazivJela = nazivJela;
	}
	public String getImeIPrezimeSefaKuhinje() {
		return imeIPrezimeSefaKuhinje;
	}
	public void setImeIPrezimeSefaKuhinje(String imeIPrezimeSefaKuhinje) {
		this.imeIPrezimeSefaKuhinje = imeIPrezimeSefaKuhinje;
	}
	public double getKolicinaSastojaka() {
		return kolicinaSastojaka;
	}
	public void setKolicinaSastojaka(double kolicinaSastojaka) {
		this.kolicinaSastojaka = kolicinaSastojaka;
	}
	public String getVremeSluzenja() {
		return vremeSluzenja;
	}
	public void setVremeSluzenja(String vremeSluzenja) {
		this.vremeSluzenja = vremeSluzenja;
	}
	public Double getCena() {
		return cena;
	}
	public void setCena(Double cena) {
		this.cena = cena;
	}
}
