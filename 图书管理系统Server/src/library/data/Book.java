package library.data;
import java.io.Serializable;
import java.util.Date;

//图书类
public class Book implements Serializable{
	private int BookID;			//唯一ID
	private String BookName;	//书名
	private String Author;		//作者
	private String BookType;	//书籍类别
	private Date BookDate;		//创建日期
	private int BookNum;		//剩余数量
	//序列化ID
	private static final long serialVersionUID = -5331456312044134350L;
	
	public Book() {
		BookID = -1;
		BookName = "";
		Author = "";
		BookType = "";
		BookDate = new Date();
	}
	
	public Book(int id,String name,String author,String bookType,Date date,int num) {
		BookID = id;
		BookName = name;
		Author = author;
		BookType = bookType;
		BookDate = date;
		BookNum = num;
	}
	
	public void setBookName(String name){
		BookName = name;
	}
	public String getBookName(){
		return BookName;
	}
	
	public int getBookID(){
		return BookID;
	}
	
	public void setBookID(int id){
		BookID = id;
	}
	
	public void setAuthor(String authors){
		Author = authors;
	}
	
	public String getAuthor(){
		return Author;
	}
	
	public void setBookDate(Date date){
		BookDate = date;
	}
	
	public Date getBookDate(){
		return BookDate;
	}

	public int getBookNum() {
		return BookNum;
	}

	public void setBookNum(int bookNum) {
		BookNum = bookNum;
	}

	public String getBookType() {
		return BookType;
	}

	public void setBookType(String bookType) {
		BookType = bookType;
	}
}