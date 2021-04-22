package library.factory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import library.database.OperateDataBase;

//修改图书信息的函数
public class TryModifyBookInfoService implements OperateService{
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		int id = Integer.parseInt(read.readLine());
		String bookname = read.readLine();
		String authors = read.readLine();
		String booktype = read.readLine();
		String date = read.readLine();
		String num = read.readLine();
		
		boolean flag = (OperateDataBase.UpdateBook(id, bookname, authors,booktype,Date.valueOf(date),Integer.parseInt(num)) == 1);
        //输出流,向socket中输出数据
        DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
        outputStream.writeBoolean(flag);
        
        read.close();
        outputStream.close();
	}
}
