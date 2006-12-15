package demo.bean;

import java.io.Serializable;

public class Record implements Serializable{
	private static final long serialVersionUID = -7592137482034023821L;
	
	private int id = 0;
	private String name = null;
	
	public Record(int i, String n) {
		setId(i);
		setName(n);
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

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof Record)) {
			return false;
		}
		
		return id == ((Record)obj).id;
	}
	
	
}
