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

import beans.Komentar;

public class KomentarDAO {
	private ArrayList<Komentar> komentari;
	private String imeFajla;

	public KomentarDAO() {
		imeFajla = System.getProperty("catalina.base") + File.separator + "data";
		
		File podaciDir = new File(imeFajla);
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		this.imeFajla += File.separator + "komentari.json";
		this.komentari = new ArrayList<Komentar>();
		// fejkKomentari();
	}
	public void fejkKomentari() {
		komentari.add(new Komentar(1, 1, 1, 1, "Tekst", 5, "CEKANJE"));
		komentari.add(new Komentar(2, 2, 2, 2, "Tekst", 4, "Odobren"));
		komentari.add(new Komentar(3, 3, 3, 3, "Tekst", 3, "Odbijen"));
	}
	public void ucitajKomentare() {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File(this.imeFajla);

		List<Komentar> ucitaniKomentari = new ArrayList<Komentar>();
		try {
			ucitaniKomentari = objectMapper.readValue(file, new TypeReference<List<Komentar>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Komentar k : ucitaniKomentari) {
			komentari.add(k);
		}
	}

	public void sacuvajKomentareJSON() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(this.imeFajla), this.komentari);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void prihvatiKomentar(Integer id) {
		Komentar komentar = nadjiKomentarPoId(id);
		if( komentar != null) {
			komentar.setStatus("Odobren");
			sacuvajKomentareJSON();
		}
	}
	public void odbijKomentar(Integer id) {
		Komentar komentar = nadjiKomentarPoId(id);
		if( komentar != null) {
			komentar.setStatus("Odbijen");
			sacuvajKomentareJSON();
		}
	}
	private Komentar nadjiKomentarPoId(Integer id) {
		for (Komentar komentar : getKomentari()) {
			if(komentar.getID() == id)
				return komentar;
		}
		return null;
	}
	public ArrayList<Komentar> getKomentari() {
		return komentari;
	}
	
}
