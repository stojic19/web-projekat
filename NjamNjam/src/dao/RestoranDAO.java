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

import beans.Adresa;
import beans.Artikal;
import beans.Lokacija;
import beans.Restoran;
import dto.NoviRestoranDTO;
import dto.RestoranIzmenaDTO;
import dto.RestoranJSONDTO;
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
	
	public void dodajNoviRestoran(RestoranJSONDTO restoran) {
		Restoran noviRestoran = new Restoran(getValues().size() + 1, 0, restoran.restoran.getNaziv(), restoran.restoran.getTip() ,
				restoran.restoran.getStatus(), restoran.restoran.getLokacija(),restoran.restoran.getPutanjaDoSlike(),restoran.restoran.getIdMenadzera());
		dodajRestoran(noviRestoran);
		sacuvajRestoraneJSON();
	}
	
	public void dodajNoviRestoran(NoviRestoranDTO restoran) {
		Restoran noviRestoran = new Restoran(getValues().size() + 1, 0, restoran.naziv, restoran.tip,
				restoran.status, new Lokacija(restoran.geografskaDuzina, restoran.geografskaSirina, new Adresa(restoran.ulica,restoran.broj,restoran.mesto,restoran.postanskiBroj)),restoran.putanjaDoSlike,restoran.idMenadzera);
		dodajRestoran(noviRestoran);
		sacuvajRestoraneJSON();
	}
	
	public void dodajRestoran(Restoran restoran) {
		if (!restorani.containsValue(restoran)) {
			restorani.put(restoran.getID(),restoran);
		}
	}
	
	public Boolean izmeniRestoran(RestoranIzmenaDTO azuriranRestoran) {

		for (Restoran restoran: getValues()) {
			if (restoran.getID() == azuriranRestoran.ID) {
				// TODO: Proveriti jel sve ovo treba menjati kad se uradi front
				restoran.setLokacija(new Lokacija(azuriranRestoran.geografskaDuzina, azuriranRestoran.geografskaSirina, new Adresa(azuriranRestoran.ulica,azuriranRestoran.broj,azuriranRestoran.mesto,azuriranRestoran.postanskiBroj)));
				restoran.setNaziv(azuriranRestoran.naziv);
				restoran.setPutanjaDoSlike(azuriranRestoran.putanjaDoSlike);
				restoran.setTip(azuriranRestoran.tip);
				restoran.setStatus(azuriranRestoran.status);
				
				sacuvajRestoraneJSON();

				return true;
			}
		}
		return false;
	}
	
	public void obrisiRestoran(Integer id) {
		Restoran restoran = nadjiRestoranPoID(id);
		if(restoran != null) {
			restoran.setLogickiObrisan(1);
			sacuvajRestoraneJSON();
		}
	}
	
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
	public List<Integer> dobaviKupcePoIdRestorana(Integer id)
	{
		for(Restoran restoran : getValues())
		{
			if(restoran.getID() == id)
				return restoran.getIdKupaca();
		}
		return null;
	}
	public void dodajArtikalRestoranu(Artikal artikal) {
		for(Restoran restoran : getValues())
		{
			if(restoran.getID()==artikal.getIdRestoranaKomPripada())
			{
				List<Integer> listaArtikala = restoran.getIdArtiklaUPonudi();
				listaArtikala.add(artikal.getID());
				restoran.setIdArtiklaUPonudi(listaArtikala);
				sacuvajRestoraneJSON();
				return;
			}
		}
	}
}
