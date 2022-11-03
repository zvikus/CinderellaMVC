package by.cinderella.services;

import by.cinderella.model.user.OrganizerList;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public class OrganizerGroupPdfExporter extends OrganizerPDFExporter {
    public OrganizerGroupPdfExporter(String uploadPath, OrganizerList organizerList, CurrencyRateService currencyRateService, UserService userService) throws DocumentException, IOException {
        super(uploadPath, organizerList, currencyRateService, userService);
    }



}
