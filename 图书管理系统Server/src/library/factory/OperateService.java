package library.factory;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

//Server对数据库的操作的接口
public interface OperateService {
	void tryAction(Socket client,BufferedReader read) throws IOException;
}
