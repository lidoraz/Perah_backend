package com.hnn;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//import org.springframework.web.bind.annotation
import org.apache.commons.io.IOUtils;


@RestController
//    @RequiredArgsConstructor
public class DBController {
    @CrossOrigin
    @RequestMapping(value = "/db", method = RequestMethod.GET)
    public void getFile(
//            @PathVariable("file_name") String fileName,
            HttpServletResponse response) {
        try {
            // get your file as InputStream
            InputStream is = new FileInputStream(new File(Consts.TestDB));
            // copy it to response's OutputStream

            IOUtils.copy(is, response.getOutputStream());
//            response.setContentType("application/pdf");
            response.flushBuffer();
        } catch (IOException ex) {
//            log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }

    }
}
