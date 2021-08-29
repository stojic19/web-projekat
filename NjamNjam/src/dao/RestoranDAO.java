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

import beans.Restoran;
//import dto.RestoranDTO;

public class RestoranDAO {
	
	private LinkedHashMap<Integer, Restoran> restorani;
	private String imeFajla;

	public RestoranDAO() {
		imeFajla = System.getProperty("catalina.base") + File.separator + "data";
		
		System.out.println("\n\n\nIme fajla:" + imeFajla + "\n\n\n");
		
		File podaciDir = new File(imeFajla);
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		this.imeFajla += File.separator + "restorani.json";
		this.restorani = new LinkedHashMap<Integer, Restoran>();

	}

	public void ucitajRestorane() {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File(this.imeFajla);

		List<Restoran> ucitaniRestorani = new ArrayList<Restoran>();
		try {
			ucitaniRestorani = objectMapper.readValue(file, new TypeReference<List<Restoran>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Restoran r : ucitaniRestorani) {
			restorani.put(r.getID(), r);
		}
	}

	public void sacuvajRestoraneJSON() {

		List<Restoran> sviRestorani = new ArrayList<Restoran>();
		for (Restoran k : getValues()) {
			sviRestorani.add(k);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(this.imeFajla), sviRestorani);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	public void dodajNoviRestoran(KorisnikDTO korisnik) {
		Korisnik noviKorisnik = new Korisnik(getValues().size() + 1, 0, 0, korisnik.korisnickoIme, korisnik.lozinka, korisnik.ime, korisnik.prezime, korisnik.pol, korisnik.datumRodjenja, korisnik.uloga);		
		dodajKorisnika(noviKorisnik);
		sacuvajKorisnikeJSON();
	}

	public void dodajRestoran(Korisnik korisnik) {
		if (!korisnici.containsValue(korisnik)) {
			System.out.println("DODAO SAM: " + korisnik.getKorisnickoIme());
			korisnici.put(korisnik.getKorisnickoIme(), korisnik);
		}
	}
	
	public Boolean promeniKorisnika(RestoranDTO azuriranKorisnik) {

		for (Restoran restoran: getValues()) {
			if (restoran.getID() == azuriranRestoran.id)) {
				/*
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
	}*/
	
	public Restoran nadjiRestoranPoID(Integer id) {
		for (Restoran restoran : getValues()) {
			if(restoran.getID() == id)
				return restoran;
		}
		
		return null;
	}
	public Collection<Restoran> getValues() {
		return restorani.values();
	}
}
