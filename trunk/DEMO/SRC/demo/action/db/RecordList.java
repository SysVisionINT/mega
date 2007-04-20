package demo.action.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.java.mega.action.api.AbstractAction;
import net.java.mega.action.api.SessionObject;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import demo.bean.Record;

public class RecordList extends AbstractAction implements SessionObject {
	private static Log log = LogFactory.getLog(RecordList.class);
	
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
		log.info("onLoad()");
		
		if (recordList == null) {
			recordList = new ArrayList();
			updateDate(); 
		}
	}

	public void add() {
		log.info("add()");
		
		gotoAction(Add.class);
	}
	
	public void delete() {		
		log.info("delete()");
		
		recordList.remove(new Record(id, null, false));
		updateDate();
	}
}
