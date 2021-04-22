package library;
import javax.swing.JFrame;
import javax.swing.UIManager;
//自己的包
import library.frame.LoginFrame;

//学生管理系统类
@SuppressWarnings("serial")
public class Client extends JFrame {	
	public static void main(String[] args) throws Exception {
		//设置窗口样式
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new LoginFrame();
	}
}
