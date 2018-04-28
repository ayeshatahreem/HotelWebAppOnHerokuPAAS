package Servlet;

import Model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

public class SignUp extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            System.out.println("Content type is not multipart/form-data");
        }
        File file = null;
        User user = new User();
        String fieldName;
        String fieldValue;
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            synchronized (User.class) {
                user.setUserId(User.getIdInc());
            }
            user.incrementIdInc();
            fieldValue = items.get(0).getString();
            user.setName(fieldValue);

            fieldValue = items.get(1).getString();
            user.setEmail(fieldValue);

            fieldValue = items.get(2).getString();
            user.setPassword(fieldValue);

            fieldValue = items.get(4).getString();
            user.setContactNumber(fieldValue);

            fieldValue = items.get(5).getString();
            user.setBillingAddress(fieldValue);

            fieldValue = items.get(6).getString();
            user.setCnic(fieldValue);

            String fileName = FilenameUtils.getName(items.get(7).getName());

            StringTokenizer stt = new StringTokenizer(fileName, ".");
            String token = stt.nextToken();
            token = stt.nextToken();
            fileName = String.valueOf(user.getUserId());
            fileName = fileName + "." + token;

            file = new File(request.getServletContext().getAttribute("USER_IMAGE_FILES_DIR") + File.separator + fileName);
            items.get(7).write(file);

            String path = file.getAbsolutePath();

            path = path.replace("\\", "/");
            int a = path.indexOf("images");
            path = path.substring(a, path.length());

            user.setImagePath(path);

            if (user.RegisterUser()) {
                HttpSession sessionUser = request.getSession();
                user.GetUser();
                sessionUser.setAttribute("user", User.findUserById(user.getUserId()));

                user.loadOrdersFromDb();
                RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}