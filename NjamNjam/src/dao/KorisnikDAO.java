package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Korisnik;
import dto.KorisnikDTO;
import dto.NoviRestoranDTO;

public class KorisnikDAO {

	private LinkedHashMap<String, Korisnik> korisnici;
	private String imeFajla;

	public KorisnikDAO() {
		imeFajla = System.getProperty("catalina.base") + File.separator + "data";
		
		System.out.println("\n\n\nIme fajla:" + imeFajla + "\n\n\n");
		
		File podaciDir = new File(imeFajla);
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		this.imeFajla += File.separator + "korisnici.json";
		this.korisnici = new LinkedHashMap<String, Korisnik>();
	}

	public void ucitajKorisnike() {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File(this.imeFajla);

		List<Korisnik> ucitaniKorisnici = new ArrayList<Korisnik>();
		try {
			ucitaniKorisnici = objectMapper.readValue(file, new TypeReference<List<Korisnik>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\n\n UCITAVANJE \n\n");
		for (Korisnik k : ucitaniKorisnici) {
			System.out.println("ime: " + k.getKorisnickoIme());
			korisnici.put(k.getKorisnickoIme(), k);
		}
		System.out.println("\n\n");
	}

	public void sacuvajKorisnikeJSON() {

		List<Korisnik> sviKorisnici = new ArrayList<Korisnik>();
		for (Korisnik k : getValues()) {
			sviKorisnici.add(k);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(this.imeFajla), sviKorisnici);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void dodajNovogKorisnika(KorisnikDTO korisnik) {
		Korisnik noviKorisnik = new Korisnik(getValues().size() + 1, 0, 0, korisnik.korisnickoIme, korisnik.lozinka, korisnik.ime, korisnik.prezime, korisnik.pol, korisnik.datumRodjenja, korisnik.uloga);		
		dodajKorisnika(noviKorisnik);
		sacuvajKorisnikeJSON();
	}
	public void dodajMenadzera(KorisnikDTO korisnik) {
		Korisnik noviKorisnik = new Korisnik(getValues().size() + 1, 0, 0, korisnik.korisnickoIme, korisnik.lozinka, korisnik.ime, korisnik.prezime, korisnik.pol, korisnik.datumRodjenja, "MENADZER");		
		noviKorisnik.setIdRestorana(korisnik.idRestorana);
		dodajKorisnika(noviKorisnik);
		sacuvajKorisnikeJSON();
	}
	public void dodajMenadzera(NoviRestoranDTO korisnik) {
		Korisnik noviKorisnik = new Korisnik(getValues().size() + 1, 0, 0, korisnik.korisnickoIme, korisnik.lozinka, korisnik.ime, korisnik.prezime, korisnik.pol, korisnik.datumRodjenja, "MENADZER");		
		noviKorisnik.setIdRestorana(korisnik.idRestorana);
		dodajKorisnika(noviKorisnik);
		sacuvajKorisnikeJSON();
	}
	public void dodajKorisnika(Korisnik korisnik) {
		if (!korisnici.containsValue(korisnik)) {
			System.out.println("DODAO SAM: " + korisnik.getKorisnickoIme());
			korisnici.put(korisnik.getKorisnickoIme(), korisnik);
		}
	}

	public Boolean promeniKorisnika(KorisnikDTO azuriranKorisnik) {

		for (Korisnik korisnik : korisnici.values()) {
			if (korisnik.getKorisnickoIme().equals(azuriranKorisnik.korisnickoIme)) {

				korisnik.setIme(azuriranKorisnik.ime);
				korisnik.setPrezime(azuriranKorisnik.prezime);
				korisnik.setLozinka(azuriranKorisnik.lozinka);
				korisnik.setPol(azuriranKorisnik.pol);
				korisnik.setDatumRodjenja(azuriranKorisnik.datumRodjenja);
				
				sacuvajKorisnikeJSON();

				return true;
			}
		}
		return false;
	}
	public Boolean promeniKorisnikaNakonKupovine(Korisnik azuriranKorisnik) {

		for (Korisnik korisnik : korisnici.values()) {
			if (korisnik.getKorisnickoIme().equals(azuriranKorisnik.getKorisnickoIme())) {

				korisnik.setBrojSakupljenihBodova(azuriranKorisnik.getBrojSakupljenihBodova());
				korisnik.setIdPorudzbina(azuriranKorisnik.getIdPorudzbina());
				korisnik.setTip(azuriranKorisnik.getTip());
				korisnik.setBrojOtkazanihPorudzbina(azuriranKorisnik.getBrojOtkazanihPorudzbina());
				
				sacuvajKorisnikeJSON();

				return true;
			}
		}
		return false;
	}
	public Boolean dodajPorudzbinuDostavljacu(Integer idKorisnika, String idPorudzbine) {

		for (Korisnik korisnik : korisnici.values()) {
			if (korisnik.getID() == idKorisnika) {

				korisnik.dodajIdPorudzbine(idPorudzbine);
				
				sacuvajKorisnikeJSON();

				return true;
			}
		}
		return false;
	}
	public Korisnik nadjiKorisnikaPoID(Integer id) {
		for (Korisnik korisnik : getValues()) {
			if(korisnik.getID() == id)
				return korisnik;
		}
		
		return null;
	}
	public Korisnik nadjiKorisnikaPoKorisnickomImenu(String korisnickoIme)
	{
		for (Korisnik korisnik : korisnici.values()) {
			if (korisnik.getKorisnickoIme().equals(korisnickoIme)) {
					return korisnik;
				}
			}
		return null;
	}

	public void blokirajKorisnikaPoID(String korisnickoIme) {
		
		Korisnik korisnik = nadjiKorisnikaPoKorisnickomImenu(korisnickoIme);
		if( korisnik != null) {
			korisnik.setBlokiran(1);
			sacuvajKorisnikeJSON();
		}
	}
	
	public void oblokirajKorisnikaPoID(String korisnickoIme) {

		Korisnik korisnik = nadjiKorisnikaPoKorisnickomImenu(korisnickoIme);
		if( korisnik != null) {
			korisnik.setBlokiran(0);
			sacuvajKorisnikeJSON();
		}
	}
	
	public boolean jeBlokiran(String korisnickoIme) {
		
		return ( dobaviKorisnikaPoKorisnickomImenu(korisnickoIme).getBlokiran() == 1 ) ? true : false;
	}
	
	public void dodajRestoranMenadzeru(NoviRestoranDTO restoran){
		if(nadjiKorisnikaPoID(restoran.idMenadzera) != null)  {
			Korisnik korisnik = nadjiKorisnikaPoID(restoran.idMenadzera);
			korisnik.setIdRestorana(restoran.ID);
		}
		sacuvajKorisnikeJSON();
	}
	public void obrisiRestoranMenadzeru(Integer idMenadzera) {
		Korisnik korisnik = this.nadjiKorisnikaPoID(idMenadzera);
		if(korisnik != null) {
			korisnik.setIdRestorana(-1);
			sacuvajKorisnikeJSON();
		}
	}
	public LinkedHashMap<String, Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(LinkedHashMap<String, Korisnik> korisnici) {
		this.korisnici = korisnici;
	}

	public Collection<Korisnik> values() {
		return korisnici.values();
	}

	public Collection<Korisnik> getValues() {
		return korisnici.values();
	}

	public Korisnik dobaviKorisnikaPoKorisnickomImenu(String korisnickoIme) {
		if (korisnici.containsKey(korisnickoIme)) {
			return korisnici.get(korisnickoIme);
		}

		return null;
	}
	public ArrayList<Korisnik> dobaviKupceIzListe(List<Integer> kupci)
	{
		if(kupci==null)
			return new ArrayList<Korisnik>();
		ArrayList<Korisnik> kupciRestorana = new ArrayList<Korisnik>();
		for(Korisnik korisnik : getValues())
		{
			if(kupci.contains(korisnik.getID()))
			{
				kupciRestorana.add(korisnik);
			}
		}
		return kupciRestorana;
	}
	public void dodajKorpuKorisniku(Integer idKorisnika,Integer idKorpe)
	{
		for (Korisnik korisnik : korisnici.values()) {
			if (korisnik.getID() == idKorisnika) {

				korisnik.setIdKorpe(idKorpe);
				sacuvajKorisnikeJSON();

				return;
			}
		}
		return;
	}
}
