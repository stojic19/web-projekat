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

import beans.Porudzbina;

public class PorudzbinaDAO {
	
	private LinkedHashMap<String, Porudzbina> porudzbine;
	private String imeFajla;
	
	public PorudzbinaDAO() {
		imeFajla = System.getProperty("catalina.base") + File.separator + "data";
		
		File podaciDir = new File(imeFajla);
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		this.imeFajla += File.separator + "porudzbine.json";
		this.porudzbine = new LinkedHashMap<String, Porudzbina>();
	}
	
	public void ucitajPorudzbine() {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File(this.imeFajla);

		List<Porudzbina> ucitanePorudzbine = new ArrayList<Porudzbina>();
		try {
			ucitanePorudzbine = objectMapper.readValue(file, new TypeReference<List<Porudzbina>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Porudzbina p : ucitanePorudzbine) {
			porudzbine.put(p.getID(), p);
		}
	}
	
	public void sacuvajPorudzbineJSON() {

		List<Porudzbina> svePorudzbine = new ArrayList<Porudzbina>();
		for (Porudzbina p : getValues()) {
			svePorudzbine.add(p);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(this.imeFajla), svePorudzbine);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean dodajNovuPorudzbinu(Porudzbina porudzbina) {
		dodajPorudzbinu(porudzbina);
		sacuvajPorudzbineJSON();
		return true;
	}
	
	public void pripremiPorudzbinu(Porudzbina porudzbina) {
		
		for(Porudzbina p : getValues()) {
			if(p.getID().equals(porudzbina.getID())) {
				p.setStatus("U PRIPREMI");
			}		
		}
		sacuvajPorudzbineJSON();
	}
	
	public void cekaDostavljacaPorudzbina(Porudzbina porudzbina) {
		
		for(Porudzbina p : getValues()) {
			if(p.getID().equals(porudzbina.getID())) {
				p.setStatus("CEKA DOSTAVLJACA");
			}		
		}
		sacuvajPorudzbineJSON();
	}
	
	public void transportujPorudzbinu(Porudzbina porudzbina) {

		for(Porudzbina p : getValues()) {
			if(p.getID().equals(porudzbina.getID())) {
				p.setStatus("U TRANSPORTU");
			}		
		}
		sacuvajPorudzbineJSON();
	}
	
	public void dostaviPorudzbinu(Porudzbina porudzbina) {
		
		for(Porudzbina p : getValues()) {
			if(p.getID().equals(porudzbina.getID())) {
				p.setStatus("DOSTAVLJENA");
			}		
		}
		sacuvajPorudzbineJSON();
	}
	
	public void otkaziPorudzbinu(Porudzbina porudzbina) {
		
		for(Porudzbina p : getValues()) {
			if(p.getID().equals(porudzbina.getID())) {
					p.setStatus("OTKAZANA");
			}		
		}
		sacuvajPorudzbineJSON();
	}

	public Collection<Porudzbina> getValues() {
		return porudzbine.values();
	}
	
	public void dodajPorudzbinu(Porudzbina porudzbina) {
		if (!porudzbine.containsValue(porudzbina)) {
			porudzbine.put(porudzbina.getID(),porudzbina);
		}
	}
}