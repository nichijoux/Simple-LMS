package library.data;
import java.io.Serializable;
import java.util.Map;

//图书类别的数量
public class MapBookTypeNum implements Serializable {
	private static final long serialVersionUID = -8151204887987409876L;
	private Map<String,Integer> map;
	
	public MapBookTypeNum(Map<String, Integer> map) {
		this.map = map;
	}
	
	public Map<String,Integer> getMap() {
		return map;
	}
	public void setMap(Map<String,Integer> map) {
		this.map = map;
	}
	
}
