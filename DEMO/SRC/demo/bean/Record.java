package demo.bean;

import java.io.Serializable;

public class Record implements Serializable{
	private static final long serialVersionUID = -7592137482034023821L;
	
	private int id = 0;
	private String name = null;
	private boolean admin = false;
	private String obs = null;
	private String ficheiro = null;
	
	public Record(int i, String n, boolean b, String o, String ficheiro) {
		setId(i);
		setName(n);
		setAdmin(b);
		setObs(o);
		setFicheiro(ficheiro);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof Record)) {
			return false;
		}
		
		return id == ((Record)obj).id;
	}

	public String getFicheiro() {
		return ficheiro;
	}

	public void setFicheiro(String ficheiro) {
		this.ficheiro = ficheiro;
	}
	
	
}
