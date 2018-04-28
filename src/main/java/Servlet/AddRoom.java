package Servlet;

import Model.Room;
import Model.RoomType;
import Model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

@MultipartConfig
public class AddRoom extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            System.out.println("Content type is not multipart/form-data");
        }
        File file = null;
        RoomType item = new RoomType();
        String fieldName;
        String fieldValue;
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            synchronized (Room.class) {
                item.setId(Room.getIdInc());
            }
            Room.incrementIdInc();
            fieldValue = items.get(0).getString();
            item.setName(fieldValue);
            fieldValue = items.get(1).getString();
            item.setDetails(fieldValue);
            fieldValue = items.get(2).getString();
            item.setDescription(fieldValue);

            //fieldName = items.get(3).getFieldName();
            String fileName = FilenameUtils.getName(items.get(3).getName());
            StringTokenizer stt = new StringTokenizer(fileName, ".");
            String token = stt.nextToken();
            token = stt.nextToken();
            fileName = String.valueOf(item.getId());
            fileName = fileName + "." + token;
            file = new File(request.getServletContext().getAttribute("ROOM_IMAGE_FILES_DIR") + File.separator + fileName);
            items.get(3).write(file);
            String path = file.getAbsolutePath();
            path = path.replace("\\", "/");
            int a = path.indexOf("images");
            path = path.substring(a, path.length());
            item.setImagePath(path);
            fieldValue = items.get(4).getString();
            item.setCount(Integer.valueOf(fieldValue));
            fieldValue = items.get(5).getString();
            item.setStartTime(fieldValue);
            fieldValue = items.get(6).getString();
            item.setDeleteTime(fieldValue);
            fieldValue = items.get(7).getString();
            item.setPrice(Integer.valueOf(fieldValue));
            item.setAdmin((User) (request.getSession().getAttribute("user")));
            item.setAdminId(((User) (request.getSession().getAttribute("user"))).getUserId());
            item.AddRoom((User) (request.getSession().getAttribute("user")), "RoomType");
            RoomType.allItems.add(item);//add to list of items
            RequestDispatcher rd1 = request.getRequestDispatcher("/index.jsp");
            rd1.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}