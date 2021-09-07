package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Korpa;

public class KorpaDAO {

	private ArrayList<Korpa> korpe;
	private String imeFajla;

	public KorpaDAO() {
		imeFajla = System.getProperty("catalina.base") + File.separator + "data";
		
		File podaciDir = new File(imeFajla);
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		this.imeFajla += File.separator + "korpe.json";
		this.korpe = new ArrayList<Korpa>();
	}

	public void ucitajKorpe() {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File(this.imeFajla);

		List<Korpa> ucitaneKorpe = new ArrayList<Korpa>();
		try {
			ucitaneKorpe = objectMapper.readValue(file, new TypeReference<List<Korpa>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		for (Korpa k : ucitaneKorpe) {
			korpe.add(k);
		}
	}

	public void sacuvajKorpeJSON() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(this.imeFajla), this.korpe);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Integer dodajKorpu() {
		Korpa korpa = new Korpa(korpe.size()+1);
		korpe.add(korpa);
		sacuvajKorpeJSON();
		return korpe.size();
	}
	
	public Korpa nadjiKorpuPoId(Integer idKorpe) {
		for(Korpa korpa : korpe)
		{
			if(korpa.getID() == idKorpe)
				return korpa;
		}
		return null;
	}
	
	public void azurirajKorpu(Korpa korpaZaAzuriranje) {
		for(Korpa korpa : korpe)
		{
			if(korpa.getID() == korpaZaAzuriranje.getID())
			{
				korpa.setArtikli(korpaZaAzuriranje.getArtikli());
				korpa.setCena(korpaZaAzuriranje.getCena());
				sacuvajKorpeJSON();
				return;
			}
		}
	}
	public void azurirajPopust(Integer idKorpe, Integer popust)
	{
		for(Korpa korpa : korpe)
		{
			if(korpa.getID() == idKorpe)
			{
				korpa.setPopust(popust);
				sacuvajKorpeJSON();
				return;
			}
		}
	}
}
