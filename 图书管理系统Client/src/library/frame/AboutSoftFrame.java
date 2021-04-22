package library.frame;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import library.util.LoadIcon;

import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

//关于软件
@SuppressWarnings("serial")
public class AboutSoftFrame extends JInternalFrame {

	private JPanel contentPane;

	public AboutSoftFrame() {
		setTitle("关于软件");
		setClosable(true);//设置窗口可关闭
		setResizable(false);
		setFont(new Font("楷体", Font.PLAIN, 16));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 300, 447, 108);
		setFrameIcon(LoadIcon.getIcon("AboutJInternalFrame.png"));
		
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.light"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel githubLabel = new JLabel("github地址：https://github.com/nichijoux/Simple-LMS");
		githubLabel.setFont(new Font("楷体", Font.PLAIN, 16));
		
		JLabel describeLabel = new JLabel("本系统为简易图书管理系统，实现了C/S交互和数据库管理");
		describeLabel.setFont(new Font("楷体", Font.PLAIN, 16));
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(9)
							.addComponent(describeLabel, GroupLayout.PREFERRED_SIZE, 410, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(githubLabel, GroupLayout.PREFERRED_SIZE, 424, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(describeLabel, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(githubLabel, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addGap(14))
		);
		contentPane.setLayout(gl_contentPane);
		
		//设置窗口可视
		setVisible(true);
	}
}
