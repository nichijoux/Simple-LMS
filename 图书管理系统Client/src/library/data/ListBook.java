package library.data;
import java.io.Serializable;
import java.util.List;

//为了发送list<Book>集合
public class ListBook implements Serializable{
	private static final long serialVersionUID = 237900557591273554L;
	private List<Book> list;
	
	public ListBook() {list = null;}
	
	public List<Book> getList(){
		return list;
	}
	public void setList(List<Book> list) {
		this.list = list;
	}
}
