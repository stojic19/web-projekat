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

import beans.Artikal;

public class ArtikalDAO {
	private ArrayList<Artikal> artikli;
	private String imeFajla;

	public ArtikalDAO() {
		imeFajla = System.getProperty("catalina.base") + File.separator + "data";
		
		File podaciDir = new File(imeFajla);
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		this.imeFajla += File.separator + "artikli.json";
		this.artikli = new ArrayList<Artikal>();
		// fejkArtikli();
	}
	/*public void fejkArtikli() {
		artikli.add();
		artikli.add();
		artikli.add();
	}*/
	public void ucitajArtikle() {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File(this.imeFajla);

		List<Artikal> ucitaniArtikli = new ArrayList<Artikal>();
		try {
			ucitaniArtikli = objectMapper.readValue(file, new TypeReference<List<Artikal>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Artikal a : ucitaniArtikli) {
			ucitaniArtikli.add(a);
		}
	}

	public void sacuvajKomentareJSON() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(this.imeFajla), this.artikli);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<Artikal> getArtikli() {
		return artikli;
	}
	public ArrayList<Artikal> dobaviArtiklePoIdRestorana(Integer id){
		ArrayList<Artikal> artikliRestorana = new ArrayList<Artikal>();
		for (Artikal a : artikli) {
			if(a.getIdRestoranaKomPripada()==id)
				artikliRestorana.add(a);
		}
		return artikliRestorana;
	}
}