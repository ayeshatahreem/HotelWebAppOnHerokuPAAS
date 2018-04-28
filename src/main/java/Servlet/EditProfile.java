package Servlet;

import Model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

@WebServlet("/EditProfile")
@MultipartConfig
public class EditProfile extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            System.out.println("Content type is not multipart/form-data");
        }
        File file = null;
        User user = ((User) request.getSession().getAttribute("user"));
        String fieldName;
        String fieldValue;
        try {
            List<FileItem> info = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            fieldValue = info.get(0).getString();
            if (!fieldValue.isEmpty()) {
                user.setName(fieldValue);
            }
            fieldValue = info.get(1).getString();
            if (!fieldValue.isEmpty()) {
                user.setEmail(fieldValue);
            }
            fieldValue = info.get(2).getString();
            if (!fieldValue.isEmpty()) {
                user.setContactNumber(fieldValue);
            }
            fieldValue = info.get(3).getString();
            if (!fieldValue.isEmpty()) {
                user.setBillingAddress(fieldValue);
            }
            fieldValue = info.get(4).getString();
            if (!fieldValue.isEmpty()) {
                user.setCnic(fieldValue);
            }
            fieldName = info.get(5).getFieldName();
            String fileName = FilenameUtils.getName(info.get(5).getName());
            if (!fileName.isEmpty()) {
                StringTokenizer stt = new StringTokenizer(fileName, ".");
                String token = stt.nextToken();
                token = stt.nextToken();
                fileName = String.valueOf(user.getUserId());
                fileName = fileName + "." + token;
                file = new File(request.getServletContext().getAttribute("USER_IMAGE_FILES_DIR") + File.separator + fileName);
                info.get(5).write(file);
                String path = file.getAbsolutePath();
                path = path.replace("\\", "/");
                int a = path.indexOf("images");
                path = path.substring(a, path.length());
                user.setImagePath(path);
                user.saveEditedUserInfo();
            }
            request.setAttribute("updated", "updated");
            RequestDispatcher rd1 = request.getRequestDispatcher("/profile.jsp");
            rd1.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}