package beans;

import java.util.ArrayList;
import java.util.HashMap;

public class Porudzbina {

	private String ID;
	
	private HashMap<Integer, Integer> idArtikala;
	private Integer idRestorana;
	private String vremePorudzbine;
	private Double cena;
	private String imePrezimeKupca;
	private String status;
	
	private String imeRestorana;
	private String tipRestorana;
	
	private ArrayList<Integer> zahteviOdDostavljaca;
	
	public Porudzbina() {}
	
	public Porudzbina(String iD, HashMap<Integer, Integer> idArtikala, Integer idRestorana, String imeRestorana, String tipRestorana, String vremePorudzbine, Double cena,
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
		this.tipRestorana = tipRestorana;
		this.zahteviOdDostavljaca = new ArrayList<Integer>();
	}
	
	public Porudzbina(Integer idRestorana, String imeRestorana, String tipRestorana, String vremePorudzbine,String imePrezimeKupca) {
		super();
		ID = getAlphaNumericString();
		this.idArtikala = new HashMap<Integer, Integer>();
		this.idRestorana = idRestorana;
		this.vremePorudzbine = vremePorudzbine;
		this.cena = 0.0;
		this.imePrezimeKupca = imePrezimeKupca;
		this.status = "OBRADA";
		this.imeRestorana = imeRestorana;
		this.tipRestorana = tipRestorana;
		this.zahteviOdDostavljaca = new ArrayList<Integer>();
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public HashMap<Integer, Integer> getIdArtikala() {
		return idArtikala;
	}

	public void setIdArtikala(HashMap<Integer, Integer> idArtikala) {
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

	public Double getCena() {
		return cena;
	}

	public void setCena(Double cena) {
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
	
	public void dodajArtikal(Integer idArtikla, Integer kolicina, Double cenaArtikla,Integer popust) {
		idArtikala.put(idArtikla, kolicina);
		double doublePopust = popust;
		this.cena += ((100.0 - doublePopust)/100.0) * (kolicina * cenaArtikla);
	}
	
	public void dodajZahtevOdDostavljaca(Integer idDostavljaca) {
		if(this.zahteviOdDostavljaca == null)
			this.zahteviOdDostavljaca = new ArrayList<Integer>();
		if(!this.zahteviOdDostavljaca.contains(idDostavljaca))
			this.zahteviOdDostavljaca.add(idDostavljaca);
	}
	
	public ArrayList<Integer> getZahteviOdDostavljaca() {
		if(zahteviOdDostavljaca == null)
		{
			zahteviOdDostavljaca = new ArrayList<Integer>();
		}
		return zahteviOdDostavljaca;
	}

	public void setZahteviOdDostavljaca(ArrayList<Integer> zahteviOdDostavljaca) {
		this.zahteviOdDostavljaca = zahteviOdDostavljaca;
	}

	static String getAlphaNumericString()
    {
  
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz";
  
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(10);
  
        for (int i = 0; i < 10; i++) {
  
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                = (int)(AlphaNumericString.length()
                        * Math.random());
  
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                          .charAt(index));
        }
  
        return sb.toString();
    }
}
