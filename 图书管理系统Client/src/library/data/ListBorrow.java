package library.data;

import java.util.List;
import java.io.Serializable;

public class ListBorrow implements Serializable{
	private static final long serialVersionUID = 192426203598413212L;
	private List<Borrow> list;
	
	public ListBorrow(List<Borrow> list) {
		this.setList(list);
	}

	public List<Borrow> getList() {
		return list;
	}

	public void setList(List<Borrow> list) {
		this.list = list;
	}
}
