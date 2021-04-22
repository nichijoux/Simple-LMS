package library.data;
import java.util.List;
import java.io.Serializable;

public class ListUser implements Serializable {
	private List<User> list;
	private static final long serialVersionUID = 2149744734331738836L;
	
	public ListUser() {
		list = null;
	}
	
	public ListUser(List<User> list) {
		this.list = list;
	}
	
	public List<User> getList() {
		return list;
	}
	public void setList(List<User> list) {
		this.list = list;
	}

}
