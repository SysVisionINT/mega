package demo.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import demo.bean.Record;
import net.java.mega.action.api.AbstractAction;
import net.java.mega.action.api.Message;
import net.java.mega.action.api.SessionObject;

public class RecordList extends AbstractAction implements SessionObject {
	private int id = 0;
	private List recordList = null;
	private Timestamp lastChange = null;

	public Timestamp getLastChange() {
		return lastChange;
	}

	public List getRecordList() {
		return recordList;
	}

	public void addRecord(Record record) {
		recordList.add(record);
		updateDate(); 
	}

	private void updateDate() {
		lastChange = new Timestamp(System.currentTimeMillis());
	}

	public void setId(int id) {
		this.id = id;
	}

	public void onLoad() {
		if (recordList == null) {
			recordList = new ArrayList();
			updateDate(); 
		}
	}

	public void add() {
		gotoAction(Add.class);
	}
	
	public void delete() {		
		recordList.remove(new Record(id, null));
		updateDate();
	}
}
