package dto;

import beans.Lokacija;

public class RestoranDTO {
	public Integer ID;
	
	public String naziv;
	public String tip;
	public String status; 							// 1 - radi, 0 - ne radi
	public Lokacija lokacija;
	public String putanjaDoSlike;
	public Integer idMenadzera;
}
