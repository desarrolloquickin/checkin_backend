package com.fjrotgerl.checkinonline.controllers;

import com.fjrotgerl.checkinonline.entities.BBCodeText;
import com.fjrotgerl.checkinonline.entities.PDF;
import com.fjrotgerl.checkinonline.entities.Usuario;
import com.fjrotgerl.checkinonline.repositories.UsuarioRepository;
import com.fjrotgerl.checkinonline.services.BBCodeEncoder;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private UsuarioRepository usuarioRepository;
    private BBCodeEncoder bbCodeEncoder;

    public RestController(UsuarioRepository usuarioRepository, BBCodeEncoder bbCodeEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.bbCodeEncoder = bbCodeEncoder;
    }

    /* ------------------------------------------------------------------------------------------------------- */
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public @ResponseBody Iterable<Usuario> getAllUsers() {
        return this.usuarioRepository.findAll();
    }
    /* ------------------------------------------------------------------------------------------------------- */

    /* ------------------------------------------------------------------------------------------------------- */
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/getPDFs", method = RequestMethod.POST)
    public @ResponseBody void getPDFs(@RequestBody PDF pdf) throws IOException {
        System.out.println(pdf.getPdfList());
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();

        for (String pdfName: pdf.getPdfList()) {
            String filePath = "src\\main\\resources\\static\\ficheros\\" + pdfName;
            StringBuilder contentBuilder = new StringBuilder();

            try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
            {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            this.bbCodeEncoder.setMessage(contentBuilder.toString());
            this.bbCodeEncoder.exportToPdf(pdfName);


            pdfMergerUtility.addSource("src\\main\\resources\\static\\pdfs\\" + pdfName + ".pdf");
        }

        Path path = Paths.get("C:\\CopiasPDF\\Reserva" + pdf.getId());
        Files.createDirectories(path);

        pdfMergerUtility.setDestinationFileName("C:\\CopiasPDF\\Reserva" + pdf.getId() + "\\" + "Firma-" + pdf.getPersonaId() + ".pdf");
        pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
    }
    /* ------------------------------------------------------------------------------------------------------- */

    /* ------------------------------------------------------------------------------------------------------- */
    @RequestMapping(value = "/exportBBCodeToPdf", method = RequestMethod.POST)
    public @ResponseBody void exportBBCodeToPdf(@RequestBody BBCodeText bbCodeText) {
        this.bbCodeEncoder.setMessage(bbCodeText.getText());
        this.bbCodeEncoder.exportToPdf("test");
    }
    /* ------------------------------------------------------------------------------------------------------- */

}
