package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Artikal;
import dto.ArtikalIzmenaDTO;
import dto.ArtikalUKorpiDTO;

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
			artikli.add(a);
		}
	}

	public void sacuvajArtikleJSON() {
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
	public void dodajNoviArtikal(Artikal artikal) {
		Artikal noviArtikal= new Artikal(artikli.size() + 1, 0, artikal.getNaziv(), artikal.getCena(),
				artikal.getTip(), artikal.getIdRestoranaKomPripada(),artikal.getKolicina(),artikal.getOpis(),artikal.getPutanjaDoSlike());
		artikli.add(noviArtikal);
		sacuvajArtikleJSON();
	}
	public Boolean izmenaArtikla(ArtikalIzmenaDTO artikal)
	{
		for(Artikal a : artikli)
		{
			if(a.getID() == artikal.ID)
			{
				a.setNaziv(artikal.naziv);
				a.setCena(artikal.cena);
				a.setKolicina(artikal.kolicina);
				a.setOpis(artikal.opis);
				a.setPutanjaDoSlike(artikal.putanjaDoSlike);
				
				sacuvajArtikleJSON();
				return true;
			}
		}
		return false;
	}
	public void obrisiArtikal(Integer id) {
		Artikal artikal = nadjiArtikalPoID(id);
		if(artikal != null) {
			artikal.setLogickiObrisan(1);
			sacuvajArtikleJSON();
		}
	}
	
	public Artikal nadjiArtikalPoID(Integer id) {
		for (Artikal artikal : artikli) {
			if(artikal.getID() == id)
				return artikal;
		}
		
		return null;
	}
	public ArrayList<Artikal> dobaviArtiklePoId(Set<Integer> keySet) {
		ArrayList<Artikal> artikliIzKorpe = new ArrayList<Artikal>();
		for (Artikal a : artikli) {
			for(Integer id : keySet)
			{
				if(a.getID() == id)
				{
					artikliIzKorpe.add(a);
					break;
				}
			}
		}
		return artikliIzKorpe;
	}
	public ArrayList<ArtikalUKorpiDTO> dobaviArtikleZaKorpu(HashMap<Integer, Integer> artikliIzKorpe) {
		ArrayList<ArtikalUKorpiDTO> artikliZaKorpu = new ArrayList<ArtikalUKorpiDTO>();
		for (Artikal a : artikli) {
			for(Integer id : artikliIzKorpe.keySet())
			{
				if(a.getID() == id)
				{
					ArtikalUKorpiDTO artikalUKorpi = new ArtikalUKorpiDTO();
					artikalUKorpi.id = a.getID();
					artikalUKorpi.cena = a.getCena();
					artikalUKorpi.idRestoranaKomPripada = a.getIdRestoranaKomPripada();
					artikalUKorpi.kolicina = a.getKolicina();
					artikalUKorpi.kolicinaZaKupovinu = artikliIzKorpe.get(id);
					artikalUKorpi.logickiObrisan = a.getLogickiObrisan();
					artikalUKorpi.naziv = a.getNaziv();
					artikalUKorpi.opis = a.getOpis();
					artikalUKorpi.tip = a.getTip();
					artikalUKorpi.putanjaDoSlike = a.getPutanjaDoSlike();
					artikliZaKorpu.add(artikalUKorpi);
					break;
				}
			}
		}
		return artikliZaKorpu;
	}
}
