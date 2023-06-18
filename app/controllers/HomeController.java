package controllers;

import play.libs.Json;
import play.mvc.*;
import play.libs.Files.TemporaryFile;
import services.FileService;
import java.util.Map;

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

    public Result read(Http.Request request) {
        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> filePart = body.getFile("file");
        if (filePart != null) {
//            String fileName = filePart.getFilename();
//            long fileSize = filePart.getFileSize();
            TemporaryFile temporaryFile = filePart.getRef();

            FileService fileService = new FileService();
            Map<String, Long> map = fileService.getFrequencyList(temporaryFile);
            return ok(Json.toJson(map));
        } else {
            return badRequest().flashing("error", "Missing file");
        }
    }
}
