package library.util;
import javax.swing.ImageIcon;

//设置Icon
public class LoadIcon {
	public static ImageIcon getIcon(String ImageName) {
		//得到图片路径	
		String path = "images/" + ImageName;
		ImageIcon icon = new ImageIcon(path);
		return icon;
	}
}
