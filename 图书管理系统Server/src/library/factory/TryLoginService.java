package library.factory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import library.database.OperateDataBase;

//尝试登陆的函数
public class TryLoginService implements OperateService{
	@Override
	public void tryAction(Socket client,BufferedReader read) throws IOException {
		String username = read.readLine();
		String password = read.readLine();
        //输出流,向socket中输出数据
        DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
        boolean output[] = OperateDataBase.checkLogin(username, password);
	    //写数据
        outputStream.writeBoolean(output[0]);
        //如果登陆成功,则还要输入用户类型
        if(output[0] == true) {
        	outputStream.writeBoolean(output[1]);
        }
        outputStream.flush();
	    //输出
        read.close();
	    outputStream.close();
	}
}
