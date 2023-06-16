package controllers;

import play.mvc.*;
import play.libs.Files.TemporaryFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }
    
    public Result explore() {
        return ok(views.html.explore.render());
    }
    
    public Result tutorial() {
        return ok(views.html.tutorial.render());
    }

    public Result upload(Http.Request request) {
        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> picture = body.getFile("file");
        if (picture != null) {
            String fileName = picture.getFilename();
            System.out.println("filename: " + fileName);
            long fileSize = picture.getFileSize();
            String contentType = picture.getContentType();
            System.out.println("contentType: "+contentType);
            TemporaryFile temporaryFile = picture.getRef();
            File file = new File("test.txt");
            temporaryFile.copyTo(file.toPath(), true);
            String data ="";
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    data+= scanner.nextLine();
                }
                scanner.close();;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println(data);
            return ok("File uploaded");
        } else {
            return badRequest().flashing("error", "Missing file");
        }
    }
}
