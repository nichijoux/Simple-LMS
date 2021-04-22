package library.data;
import java.io.Serializable;
import java.util.List;

public class ListBookType implements Serializable{
	private static final long serialVersionUID = -8037347147105472544L;
	private List<String> list;
	
	public ListBookType(List<String> booktype) {
		setList(booktype);
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
}
