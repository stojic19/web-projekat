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

import beans.Slika;

public class SlikaDAO {
	private ArrayList<Slika> slike;
	private String imeFajla;

	public SlikaDAO() {
		imeFajla = System.getProperty("catalina.base") + File.separator + "data";
				
		File podaciDir = new File(imeFajla);
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		this.imeFajla += File.separator + "slike.json";
		this.slike = new ArrayList<Slika>();

	}

	public void ucitajSlike() {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File(this.imeFajla);

		List<Slika> ucitaneSlike = new ArrayList<Slika>();
		try {
			ucitaneSlike = objectMapper.readValue(file, new TypeReference<List<Slika>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Slika a : ucitaneSlike) {
			slike.add(a);
		}
	}

	private void sacuvajSlikeJSON() {
		List<Slika> sveSlike = new ArrayList<Slika>();
		for (Slika a : getValues()) {
			sveSlike.add(a);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(this.imeFajla), sveSlike);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Slika> getValues() {
		return slike;
	}

	public Slika dodajNovuSliku(String code64ForImage) {
		
		Integer ID = getValues().size() + 1;
		Slika novaSlika = new Slika(ID, code64ForImage);
		slike.add(novaSlika);
		sacuvajSlikeJSON();
		
		return novaSlika;
	}
}
