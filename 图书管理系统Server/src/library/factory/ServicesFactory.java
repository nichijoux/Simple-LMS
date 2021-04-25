package library.factory;

import java.util.HashMap;
import java.util.Map;

//策略  + 工厂模式
public class ServicesFactory {
	private static final Map<String, OperateService> map = new HashMap<String, OperateService>();
	//初始化
	static {
		map.put("login", new TryLoginService());
		map.put("register",new TryRegisterService());
		map.put("getBookInfo",new TryGetAllBookInfoService());
		map.put("modifyBookInfo",new TryModifyBookInfoService());
		map.put("addBookInfo",new TryAddBookInfoService());
		map.put("deleteBookInfo",new TryDeleteBookInfoService());
		map.put("queryBookInfo",new TryQueryBookInfoService());
		map.put("getAllBookType",new TryGetAllBookTypeService());
		map.put("modifyBookType",new TryModifyBookTypeService());
		map.put("addBookType",new TryAddBookTypeService());
		map.put("deleteBookType",new TryDeleteBookTypeService());
		map.put("getAllBookTypeNum",new TryGetAllBookTypeNumService());
		map.put("getAllUserInfo",new TryGetAllUserInfoService());
		map.put("deleteUser",new TryDeleteUserService());
		map.put("modifyUserPermission",new TryModifyUserPermissionService());
		map.put("modifyUserPassword",new TryModifyUserPasswordService());
		map.put("getBorrowInfo",new TryGetBorrowInfoService());
		map.put("returnBook",new TryReturnBookService());
		map.put("borrowBook",new TryBorrowBookService());
		
	}
	public static OperateService getOperateService(String command) {
		return map.get(command);
	}
}
