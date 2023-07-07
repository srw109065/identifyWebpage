package com.customer;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.annotation.MultipartConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
@WebServlet("/UserServlet")
@MultipartConfig
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
	
	int currentPage = 1;
	int pageSize = 10;
	
	public void init() {
		userDAO = new UserDAO();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ServletException {
		String action = request.getParameter("action");
		try {
			switch (action) {
			case "new":
				showNewForm(request, response);
				break;
			case "insert":
				insertUser(request, response);
				break;
			case "delete":
				deleteUser(request, response);
				break;
			case "edit":
				showEditForm(request, response);
				break;
			case "update":
				updateUser(request, response);
				break;
			default:
				listUser(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		
		String pageParam = request.getParameter("page");//取得當前頁面參數

	    if (pageParam != null && !pageParam.isEmpty()) {
	        currentPage = Integer.parseInt(pageParam);
	    }
		
		List<User> listUser = userDAO.selectAllUsers();
		
		int totalPage = (int) Math.ceil((double) listUser.size() / pageSize);
		
		int startIndex = (currentPage - 1) * pageSize;//第一頁-1 顯示10筆資料
		int endIndex = Math.min(startIndex + pageSize, listUser.size());//10索引 + 10數據 , 15數據庫全內容 取最小值
		List<User> displayedUsers = listUser.subList(startIndex, endIndex);
		
		request.setAttribute("displayedUsers", displayedUsers);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("currentPage", currentPage); // 將當前頁數傳遞到前端

		jakarta.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, ServletException {
		jakarta.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("user_form.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, ServletException, IOException {
//		String mail = request.getParameter("mail");
//		User existingUser = userDAO.selectUserById(mail);
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser = userDAO.selectUser(id);
		jakarta.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("user_form.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);
		System.out.print("ii======>");
	}
	
	private void insertUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException,ServletException {
		// 讀取提交的資料
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		System.out.println("dd======>");
		// 讀取提交的資料
		String time = request.getParameter("time");
		Part imagePart = request.getPart("image"); // 獲取上傳的圖像。 Part，它提供了訪問上傳文件內容的方法。
		//圖轉成BLOB 格式
		
	    InputStream imageStream = imagePart.getInputStream();//獲取圖像文件的輸入流，以方便讀取文件內容。
		
	    // 修改图像大小
	    BufferedImage originalImage = ImageIO.read(imageStream);
	    int newWidth = 400; // 新的宽度
	    int newHeight = 200; // 新的高度
	    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);//新的大小空白圖
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);//透過舊的圖內容，繪製到新的空白圖上，得到一張新的圖
	    g.dispose();
	    
	    Blob imageBlob = null;
	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	    	//ByteArrayOutputStream暫時讀取的文件數據的對象。這個流可以將數據寫入到內部的字節數據組中。
	    	//InputStream 以下是屬於InputStream 轉Blob的方式
//	        byte[] buffer = new byte[4096];
//	        int bytesRead;
//	        while ((bytesRead = imageStream.read(buffer)) != -1) {
//	        	//通過調用imageStream.read(buffer)方法來讀取數據並將其存儲到buffer數據組中。該方法返回實際讀取的字節數，並將其賦值給bytesRead變化量。
//	        	//循環會一直執行，直接到imageStream.read(buffer)方法返回-1，表示已讀取完所有的數據，沒有更多的數據可讀。
//	        	
//	            outputStream.write(buffer, 0, bytesRead);//buffer是告訴只有4096大小數據 然後0是說從起始位置開始 bytesRead  而從0開始填寫實際數據 ?
//	        }
//	        imageBlob = new SerialBlob(outputStream.toByteArray());
	    	//而這裡是屬於BufferedImage 轉BLOB的方式
	        ImageIO.write(resizedImage, "JPEG", outputStream); // 調整後的圖像數據直接寫入輸出流，這個方法會自動處理圖像的編碼和寫入過程。
	        //然後，通過 outputStream.toByteArray() 獲取完整的字節數組，並將其存儲為imageBlob。
	        imageBlob = new SerialBlob(outputStream.toByteArray());
	    } catch (IOException | SQLException e) {
	        e.printStackTrace();
	    }
		User newUser = new User(time,imageBlob);
		userDAO.insertUser(newUser);//新增
		
		String pageParam = request.getParameter("page");//取得當前頁面參數
		
	    if (pageParam != null && !pageParam.isEmpty()) { //讀取操作頁面，刷新頁數
	        currentPage = Integer.parseInt(pageParam);
	    }
		
		List<User> listUser = userDAO.selectAllUsers();
		
		int totalPage = (int) Math.ceil((double) listUser.size() / pageSize);//15 / 10 = 1.5 << 1.5 2
		
		int startIndex = (currentPage - 1) * pageSize; // 2-1 1 * 10 =第一頁 0 第二頁 10索引 
		int endIndex = Math.min(startIndex + pageSize, listUser.size());//10索引 + 10數據 , 15數據庫全內容 取最小值 
		List<User> displayedUsers = listUser.subList(startIndex, endIndex);
		
		request.setAttribute("displayedUsers", displayedUsers);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("currentPage", currentPage); // 將當前頁數傳遞到前端
		
		jakarta.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}
	
	private void updateUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		System.out.print(id);
		String time = request.getParameter("time");
		Part imagePart = request.getPart("image"); // 獲取上傳的圖像。 Part，它提供了訪問上傳文件內容的方法。
		//圖轉成BLOB 格式
	    InputStream imageStream = imagePart.getInputStream();//獲取圖像文件的輸入流，以方便讀取文件內容。
	    
	    // 修改图像大小
	    BufferedImage originalImage = ImageIO.read(imageStream);
	    int newWidth = 400; // 新的宽度
	    int newHeight = 200; // 新的高度
	    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);//新的大小空白圖
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);//透過舊的圖內容，繪製到新的空白圖上，得到一張新的圖
	    g.dispose();
	    
	    Blob imageBlob = null;
	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	    	//ByteArrayOutputStream暫時讀取的文件數據的對象。這個流可以將數據寫入到內部的字節數據組中。
//	        byte[] buffer = new byte[4096];
//	        int bytesRead;
//	        while ((bytesRead = imageStream.read(buffer)) != -1) {
//	        	//通過調用imageStream.read(buffer)方法來讀取數據並將其存儲到buffer數據組中。該方法返回實際讀取的字節數，並將其賦值給bytesRead變化量。
//	        	//循環會一直執行，直接到imageStream.read(buffer)方法返回-1，表示已讀取完所有的數據，沒有更多的數據可讀。
//	        	
//	            outputStream.write(buffer, 0, bytesRead);//buffer是告訴只有4096大小數據 然後0是說從起始位置開始 bytesRead  而從0開始填寫實際數據 ?
//	            System.out.println("add======>"+bytesRead);
//	        	System.out.println("adssd======>"+Arrays.toString(buffer));
//	        }
//	        imageBlob = new SerialBlob(outputStream.toByteArray());
	        ImageIO.write(resizedImage, "JPEG", outputStream); // 調整後的圖像數據直接寫入輸出流，這個方法會自動處理圖像的編碼和寫入過程。
	        //然後，通過 outputStream.toByteArray() 獲取完整的字節數組，並將其存儲為imageBlob。
	        imageBlob = new SerialBlob(outputStream.toByteArray());
	    } catch (IOException | SQLException e) {
	        e.printStackTrace();
	    }
		
		User book = new User(id,time,imageBlob);
		userDAO.updateUser(book);
		//將page參數轉換為整數
		String pageParam = request.getParameter("page");//取得當前頁面參數

		
	    if (pageParam != null && !pageParam.isEmpty()) {
	        currentPage = Integer.parseInt(pageParam);
	    }
		//計算索引值
		List<User> listUser = userDAO.selectAllUsers();
		
		int totalPage = (int) Math.ceil((double) listUser.size() / pageSize);
		
		int startIndex = (currentPage - 1) * pageSize;//用於計算起始索引 以及該頁數要多少筆資料
		int endIndex = Math.min(startIndex + pageSize, listUser.size());//0索引 + 10數據 , 15數據庫全內容 取最小值
		List<User> displayedUsers = listUser.subList(startIndex, endIndex);
		
		request.setAttribute("displayedUsers", displayedUsers);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("currentPage", currentPage); // 將當前頁數傳遞到前端
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
		System.out.print("gg======>");
		}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		userDAO.deleteUser(id);
		
		String pageParam = request.getParameter("page");//取得當前頁面參數

		
	    if (pageParam != null && !pageParam.isEmpty()) {
	        currentPage = Integer.parseInt(pageParam);
			
	    }
		
		List<User> listUser = userDAO.selectAllUsers();
		
		int totalPage = (int) Math.ceil((double) listUser.size() / pageSize);
		
		int startIndex = (currentPage - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, listUser.size());//10索引 + 10數據 , 15數據庫全內容 取最小值
		List<User> displayedUsers = listUser.subList(startIndex, endIndex);
		
		request.setAttribute("displayedUsers", displayedUsers);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("currentPage", currentPage); // 將當前頁數傳遞到前端
		
		jakarta.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
	}
}