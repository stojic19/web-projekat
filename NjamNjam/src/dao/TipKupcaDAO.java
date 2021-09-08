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

import beans.TipKupca;

public class TipKupcaDAO {

	private ArrayList<TipKupca> tipovi;
	private String imeFajla;

	public TipKupcaDAO() {
		imeFajla = System.getProperty("catalina.base") + File.separator + "data";
		
		File podaciDir = new File(imeFajla);
		if (!podaciDir.exists()) {
			podaciDir.mkdir();
		}
		this.imeFajla += File.separator + "tipovi.json";
		this.tipovi = new ArrayList<TipKupca>();
		//fejkTipovi();
		//sacuvajTipoveJSON();
	}
	/*public void fejkTipovi() {
		tipovi.add(new TipKupca("Bronza",2,1000));
		tipovi.add(new TipKupca("Srebro",5,2000));
		tipovi.add(new TipKupca("Zlato",7,3500));
		tipovi.add(new TipKupca("Dijamant",10,5000));
	}*/
	public void ucitajTipove() {
		ObjectMapper objectMapper = new ObjectMapper();

		File file = new File(this.imeFajla);

		List<TipKupca> ucitaniTipovi= new ArrayList<TipKupca>();
		try {
			ucitaniTipovi = objectMapper.readValue(file, new TypeReference<List<TipKupca>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		for (TipKupca a : ucitaniTipovi) {
			tipovi.add(a);
		}
	}

	public void sacuvajTipoveJSON() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(this.imeFajla), this.tipovi);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<TipKupca> getTipovi() {
		return tipovi;
	}
	
}
