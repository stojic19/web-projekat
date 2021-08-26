package beans;

public class Slika {

	private Integer ID;
	private String code64;
	
	public Slika() {
		
	}

	public Slika(Integer iD, String code64) {
		super();
		ID = iD;
		this.code64 = code64;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getCode64() {
		return code64;
	}

	public void setCode64(String code64) {
		this.code64 = code64;
	}
	
}
