package library.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import library.data.Book;
import library.data.ListBook;
import library.database.OperateDataBase;

//查询指定信息的函数
public class TryQueryBookInfoService implements OperateService{
	private String[] query = {"Author","BookName"};
	
	@Override
	public void tryAction(Socket client, BufferedReader read) throws IOException {
		int selectIndex = Integer.parseInt(read.readLine());
		String info = read.readLine();
		String filed = query[selectIndex % query.length];
		//精确查询[0,1]和模糊查询[2,3]
		List<Book> list = new ArrayList<Book>();
		switch(selectIndex) 
		{
		case 0:
		case 1:
			list = OperateDataBase.refindedQuery(filed, info);
			break;
		case 2:
		case 3:
			list = OperateDataBase.fuzzyQuery(filed, info);
			break;
		default:
			break;
		}
		if(list != null)
		{
			ListBook listBook = new ListBook();
			listBook.setList(list);
			ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
			outputStream.writeObject(listBook);
			outputStream.flush();
			outputStream.close();	
		}		
		//准备关闭输入端
		read.close();
	}
}
