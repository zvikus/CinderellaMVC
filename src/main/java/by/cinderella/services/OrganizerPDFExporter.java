package by.cinderella.services;

import by.cinderella.model.organizer.Organizer;
import by.cinderella.model.organizer.OrganizerCategory;
import by.cinderella.model.user.OrganizerList;
import by.cinderella.model.user.UserOrganizer;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class OrganizerPDFExporter {

    UserService userService;

    CurrencyRateService currencyRateService;

    private OrganizerList organizerList;
    private BaseFont baseFont = BaseFont.createFont("/static/fonts/Neuron.ttf", "cp1251", BaseFont.EMBEDDED, true);
    private String uploadPath;
    private BaseColor fontColor = new BaseColor(99, 122, 164);


    public OrganizerPDFExporter(String uploadPath,
                                OrganizerList organizerList,
                                CurrencyRateService currencyRateService,
                                UserService userService) throws DocumentException, IOException {
        this.uploadPath = uploadPath;
        this.organizerList = organizerList;
        this.currencyRateService = currencyRateService;
        this.userService = userService;
    }

    private void addTableHeader(PdfPTable table) {
        Font font = new Font(baseFont, 10, Font.BOLD);
        font.setColor(BaseColor.WHITE);
        String priceHeader = "Цена(" + userService.getUserCurrency().CUR_ABBREVIATION + ")";
        Stream.of("", "Ссылка", priceHeader,
                        "Ширина", "Глубина", "Высота", "Категория", "Материал", "Продавец", "Кол-во", "Комментарий")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setPadding(7f);
                    header.setBorderWidth(0);
                    header.setPhrase(new Phrase(columnTitle, font));
                    table.addCell(header);
                });
    }

    private void writeTableData(PdfPTable table) throws URISyntaxException, BadElementException, IOException{
        Font regularFont = new Font(baseFont, 10, Font.NORMAL, BaseColor.BLACK);
        Font regularFontRed = new Font(baseFont, 10, Font.BOLD, BaseColor.RED);
        Font linkFont = new Font(baseFont, 12, Font.NORMAL, fontColor);
        for (UserOrganizer userOrganizer : this.organizerList.getUserOrganizerList()) {
            Organizer organizer = userOrganizer.getOrganizer();

            try {
                Path path = Paths.get(uploadPath + "/" + organizer.getImageName());
                Image img = Image.getInstance(path.toAbsolutePath().toString());
                PdfPCell imageCell = new PdfPCell(img, true);
                imageCell.setBorder(0);
                table.addCell(imageCell);
            } catch (Exception exception) {
                table.addCell(new Phrase("-", regularFont));
            }

            Phrase phrase = new Phrase();
            Chunk chunk = new Chunk(organizer.getName(), linkFont);
            chunk.setAnchor(String.valueOf(organizer.getLink()));
            phrase.add(chunk);

            table.addCell(phrase);

            table.addCell(new Phrase(
                    currencyRateService.getPrice(organizer), regularFontRed));
            table.addCell(new Phrase(String.valueOf(organizer.getWidth()), regularFont));
            table.addCell(new Phrase(String.valueOf(organizer.getLength()), regularFont));
            table.addCell(new Phrase(String.valueOf(organizer.getHeight()), regularFont));

            StringBuilder categories = new StringBuilder();
            for (OrganizerCategory category : organizer.getCategories()) {
                categories.append(category.parentCategory.label).append(": ").append(category.label).append("\n");
            };

            table.addCell(new Phrase(categories.toString(), regularFont));

            table.addCell(new Phrase(String.valueOf(organizer.getMaterial().label), regularFont));
            table.addCell(new Phrase(String.valueOf(organizer.getSeller().label), regularFont));
            table.addCell(new Phrase(String.valueOf(userOrganizer.getCount()), regularFont));

            String resultComment = "";
            if (organizer.getSeller().promo != null) {
                resultComment += organizer.getSeller().promo;
            }
            if (userOrganizer.getComment() != null) {
                resultComment += userOrganizer.getComment();
            }
            if (resultComment.equals("")) {
                resultComment = "-";
            }

            table.addCell(new Phrase(resultComment, regularFont));
        }
    }



    public void export(HttpServletResponse response) throws DocumentException, IOException, DocumentException, URISyntaxException {
        Document document = new Document(PageSize.A4.rotate());
        //document.setPageSize(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = new Font(baseFont, 14, Font.NORMAL, fontColor);
        Paragraph paragraph = new Paragraph(this.organizerList.getName(), font);
        document.add(paragraph);


        /*Chunk chunk = new Chunk("ОРГАНИЗАЦИЯ ПРОСТРАНСТВА", font);
        chunk.setAnchor("https://cinderella.by");
        document.add(chunk);*/

        /*Chunk cinderellaLabel = new Chunk();
        Path path = Paths.get(uploadPath + "/" + organizer.getImageName());
        Image img = Image.getInstance(path.toAbsolutePath().toString());*/

        /*Paragraph p = new Paragraph("Список органайзеров", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);*/

        PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2f, 3.5f, 2.0f, 2.0f, 2.0f, 2.0f, 4.0f, 2.0f, 1.5f, 1.5f, 3.0f});
        table.setSpacingBefore(50f);




        table.getDefaultCell().setBorderWidth(0f);

        addTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }

    public void setOrganizerList(OrganizerList organizerList) {
        this.organizerList = organizerList;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
