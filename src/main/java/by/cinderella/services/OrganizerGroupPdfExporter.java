package by.cinderella.services;

import by.cinderella.model.organizer.Organizer;
import by.cinderella.model.organizer.OrganizerCategory;
import by.cinderella.model.user.OrganizerList;
import by.cinderella.model.user.UserOrganizer;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class OrganizerGroupPdfExporter extends OrganizerPDFExporter {
    public OrganizerGroupPdfExporter(String uploadPath, OrganizerList organizerList, CurrencyRateService currencyRateService, UserService userService) throws DocumentException, IOException {
        super(uploadPath, organizerList, currencyRateService, userService);
    }


    private void addTableHeader(PdfPTable table) {
        Font font = new Font(super.baseFont, 10, Font.BOLD);
        font.setColor(BaseColor.WHITE);
        String priceHeader = "Цена(" + userService.getUserCurrency().CUR_ABBREVIATION + ")";
        Stream.of("", "Ссылка", priceHeader,
                        "Ширина", "Глубина", "Высота", "Комментарий",
//                        "Категории",
                        "Материал", "Продавец")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setPadding(7f);
                    header.setBorderWidth(0);
                    header.setPhrase(new Phrase(columnTitle, font));
            table.addCell(header);
        });
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException, DocumentException, URISyntaxException {
        Document document = new Document(PageSize.A4.rotate());
        //document.setPageSize(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        if (!userService.getAuthUser().isProAccount()) {
            writer.setPageEvent(new OrganizerPDFExporter.Header());
        }

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


        Map<String, java.util.List<UserOrganizer>> organizerListsGroup =
                this.organizerList.getUserOrganizerList().stream()
                        .collect(groupingBy(m -> m.getOrganizerGroup() == null ?
                                "Без группы" : m.getOrganizerGroup()));
        font = new Font(baseFont, 12, Font.NORMAL, fontColor);
        for (Map.Entry<String, java.util.List<UserOrganizer>> entry : organizerListsGroup.entrySet()) {
            List<UserOrganizer> sortedList;
            sortedList = entry.getValue().stream().
                    sorted((a, b) -> a.getOrganizer().getAbsolutePrice().compareTo(b.getOrganizer().getAbsolutePrice())).collect(Collectors.toList());

            paragraph = new Paragraph(entry.getKey(), font);
            document.add(paragraph);


            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{2f, 3.5f, 2.0f, 2.0f, 2.0f, 2.0f, 4.0f,
//                    2.0f,
                    2.0f, 3.0f});
            table.setSpacingBefore(10f);


            table.getDefaultCell().setBorderWidth(0f);

            addTableHeader(table);
            writeTableData(table, sortedList);

            document.add(table);
        }
        document.close();
    }

    private void writeTableData(PdfPTable table, List<UserOrganizer> userOrganizerList) throws URISyntaxException, BadElementException, IOException{
        Font regularFont = new Font(baseFont, 10, Font.NORMAL, BaseColor.BLACK);
        Font regularFontRed = new Font(baseFont, 10, Font.BOLD, BaseColor.RED);
        Font linkFont = new Font(baseFont, 12, Font.NORMAL, fontColor);
        for (UserOrganizer userOrganizer : userOrganizerList) {
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

            String resultComment = "";
            if (organizer.getSeller().promo != null) {
                resultComment += organizer.getSeller().promo;
                if (userOrganizer.getComment() != null) {
                    resultComment += "\n" + userOrganizer.getComment();
                }
            } else if (userOrganizer.getComment() != null && !userOrganizer.getComment().equals("")) {
                resultComment += userOrganizer.getComment();
            } else {
                resultComment = "-";
            }

            table.addCell(new Phrase(resultComment, regularFont));

            StringBuilder categories = new StringBuilder();
            for (OrganizerCategory category : organizer.getCategories()) {
                categories.append(category.getLabel()).append("\n");
            };

            //table.addCell(new Phrase(categories.toString(), regularFont));

            table.addCell(new Phrase(String.valueOf(organizer.getMaterial().label), regularFont));
            table.addCell(new Phrase(String.valueOf(organizer.getSeller().label), regularFont));
        }
    }


}
