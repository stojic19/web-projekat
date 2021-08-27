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
		
		// UNCOMMENT IF YOU WANT TO ADD MOCKUP DATA TO FILE addMockupData();
	}

	/**
	 * Read the data from the path.
	 * 
	 * @param path
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
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

		// Get all users
		List<Korisnik> sviKorisnici = new ArrayList<Korisnik>();
		for (Korisnik k : getValues()) {
			sviKorisnici.add(k);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Write them to the file
			objectMapper.writeValue(new FileOutputStream(this.imeFajla), sviKorisnici);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Add new user to files.
	 * @param user : user data from form
	 */
	public void dodajNovogKorisnika(KorisnikDTO korisnik) {
		Korisnik noviKorisnik = new Korisnik(getValues().size() + 1, 0, 0, korisnik.korisnickoIme, korisnik.lozinka, korisnik.ime, korisnik.prezime, korisnik.pol, korisnik.datumRodjenja, korisnik.uloga);		
		dodajKorisnika(noviKorisnik);
		sacuvajKorisnikeJSON();
	}
	/*
	public Collection<User> getGuestsOfHost(User user, ArrayList<Reservation> allReservations) {
		
		
		// Get ID-s of guest which have reservation on Host apartments
		ArrayList<Integer> guestsID = new ArrayList<Integer>();
		for (Integer idOfApartment : user.getApartmentsForRentingHostIDs()) {
			for (Reservation currReservation : allReservations) {
				if(idOfApartment.equals(currReservation.getIdOfReservedApartment())) {
					guestsID.add(currReservation.getGuestID());
					break;
				}
			}
		}
		
		// Get real object of those guests (by id ofc)
		ArrayList<User> guestsOfHost = new ArrayList<User>();
		for (Integer userID : guestsID) {
			if(findUserById(userID) != null) {
				guestsOfHost.add(findUserById(userID));
			}
		}
		
		return guestsOfHost;
	}
*/
	/*
	public Collection<Reservation> getReservationsOfHost(User user, ArrayList<Reservation> allReservations) {
		
		ArrayList<Reservation> reservationsOfHost = new ArrayList<Reservation>();
		
		for (Integer idOfApartment : user.getApartmentsForRentingHostIDs()) {
			for (Reservation currReservation : allReservations) {
				if(idOfApartment.equals(currReservation.getIdOfReservedApartment())) {
					reservationsOfHost.add(currReservation);
				}
			}
		}
		
		return reservationsOfHost;
	}
	*/
	public void dodajKorisnika(Korisnik korisnik) {
		if (!korisnici.containsValue(korisnik)) {
			System.out.println("DODAO SAM: " + korisnik.getKorisnickoIme());
			korisnici.put(korisnik.getKorisnickoIme(), korisnik);
		}
	}

	public Boolean promeniKorisnika(KorisnikDTO azuriranKorisnik) {

		// Find user with that name, and change his data.
		for (Korisnik korisnik : korisnici.values()) {
			if (korisnik.getKorisnickoIme().equals(azuriranKorisnik.korisnickoIme)) {
				System.out.println("NASAO SAM " + korisnik.getKorisnickoIme() + " i sad cu mu izmeniti podatke");
				System.out.println("NJEGOVA ROLA JE TRENUTNO: " + korisnik.getTip());
				System.out.println("A NOVA JE: " + azuriranKorisnik.tip);

				/*	
				user.setName(updatedUser.name);
				user.setPassword(updatedUser.password);
				user.setSurname(updatedUser.surname);
				user.setRole(updatedUser.role);
				*/
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
/*
	public void addHostApartments(User updatedUser, Integer idOfApartment) {
		// Find user with that name, and change his data.
		for (User user : users.values()) {
			if (user.getUserName().equals(updatedUser.getUserName())) {
				
				// Check for unique apartment in host list of apartments
				if(!user.getApartmentsForRentingHostIDs().contains(idOfApartment))
					user.getApartmentsForRentingHostIDs().add(idOfApartment);
				
				saveUsersJSON();
				return;
			}
		}

	}
*/	
	/**
	 * Physical delete of apartmentID in host list of apartments.
	 * 
	 * @param hostID : ID of host to whom we want to remove apartment
	 * @param apartmentID : ID of apartment which we want to remove from host
	 *//*
	public void deleteHostApartment(Integer hostID, Integer apartmentID) {

		System.out.println("\n\n hostu sa id-em: " + hostID + " smo obrisali apartman sa id-em: " + apartmentID);
		User host = findUserById(hostID);
		List<Integer> apartmentsOfHostIDs = host.getApartmentsForRentingHostIDs();
		apartmentsOfHostIDs.remove(apartmentID);
		saveUsersJSON();

	}

	public User findUserById(Integer ID) {
		for (User currentUser : getValues()) {
			if(currentUser.getID().equals(ID))
				return currentUser;
		}
		
		return null;
	}
	*/
	/**
	 * Block user with forwarded id [ set property blocked to 1 ]
	 * @param id : unique represent of user
	 *//*
	public void blockUserById(Integer id) {

		User tempUser = findUserById(id);
		if( tempUser != null) {
			tempUser.setBlocked(1);
		}
		
		saveUsersJSON();
	}
	
	/**
	 * Unblock user with forwarded id [ set property blocked to 0 ]
	 * @param id : unique represent of user
	 *//*
	public void unblockUserById(Integer id) {

		User tempUser = findUserById(id);
		if( tempUser != null) {
			tempUser.setBlocked(0);
		}
		
		saveUsersJSON();
	}*/
	
	public boolean jeBlokiran(String korisnickoIme) {
		
		return ( dobaviKorisnikaPoKorisnickomImenu(korisnickoIme).getBlokiran() == 1 ) ? true : false;
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

	/**
	 * Method for adding dummy data to JSON file of users
	 */
	@SuppressWarnings("unused")
	private void addMockupData() {

		// Make all users
		List<Korisnik> sviKorisnici = new ArrayList<Korisnik>();
		/*
		List<Integer> apartmentsForRentingHostIDs = new ArrayList<Integer>(); // apartmani za izdavanje
		apartmentsForRentingHostIDs.add(1);
		apartmentsForRentingHostIDs.add(2);

		List<Integer> rentedApartmentsOfGuestIDs = new ArrayList<Integer>(); // iznajmljeni apartmani

		List<Integer> listOfReservationsGuestIDs = new ArrayList<Integer>(); // lista rezervacija
		
		allUsers.add(new User(1, 0, 0, "dule", "12345", "Dule", "Maksimovic", "ADMINISTRATOR", "Male",
				new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>()));
		
		allUsers.add(new User(2, 0, 0, "vaxi", "12345", "Vladislav", "Maksimovic", "HOST", "Male",
				apartmentsForRentingHostIDs, new ArrayList<Integer>(), new ArrayList<Integer>()));
		
		
		rentedApartmentsOfGuestIDs.add(1); rentedApartmentsOfGuestIDs.add(2);
		listOfReservationsGuestIDs.add(10); listOfReservationsGuestIDs.add(20);
		allUsers.add(new User(3, 0, 0, "pufke", "12345", "Nemanja", "Pualic", "GUEST", "Male",
				new ArrayList<Integer>(), rentedApartmentsOfGuestIDs, listOfReservationsGuestIDs));
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Write them to the file
			objectMapper.writeValue(new FileOutputStream(this.path), allUsers);

		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
}
