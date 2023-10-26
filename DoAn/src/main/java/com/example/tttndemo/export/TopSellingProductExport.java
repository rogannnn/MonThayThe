package com.example.tttndemo.export;

import com.example.tttndemo.entity.Order;
import com.example.tttndemo.entity.OrderDetail;
import com.example.tttndemo.utils.TopSellingProduct;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TopSellingProductExport {
    private List<TopSellingProduct> list;
    private Font vietnameseFont;

    public TopSellingProductExport(List<TopSellingProduct>  list) throws IOException {
        this.list = list;
        this.vietnameseFont = setVietnameseFont();
    }

    public Font setVietnameseFont() throws IOException {
        String fontPath = "static/fonts/font-times-new-roman.ttf";
        BaseFont unicodeFont = BaseFont.createFont(new ClassPathResource(fontPath).getURL().getPath()   , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font vietnameseFont = new Font(unicodeFont, 12);
        return vietnameseFont;
    }

    private void writeTableHeader(PdfPTable table) throws IOException {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(8);
        vietnameseFont.setSize(16);



        cell.setPhrase(new Phrase("Mã sản phẩm", vietnameseFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tên sản phẩm", vietnameseFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Số lượng đã bán", vietnameseFont));
        table.addCell(cell);

    }

    private void writeTableData(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setPadding(6);
        vietnameseFont.setSize(14);
        for(TopSellingProduct p : list){
            cell.setPhrase(new Phrase(String.valueOf(p.getId())));
            table.addCell(cell);
            cell.setPhrase(new Phrase(p.getName()));
            table.addCell(cell);
            cell.setPhrase(new Phrase(String.valueOf(p.getTotalSoldQuantity())));
            table.addCell(cell);
        }
    }

    public void export(HttpServletResponse response, Integer limit, Date startDate, Date finishDate) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        NumberFormat formatter = new DecimalFormat("#,###");

        setVietnameseFont();
        Document document  = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        String imagePath = new ClassPathResource("static/images/ptit.png").getURL().getPath();
        Image image = Image.getInstance(imagePath);
        image.scaleToFit(60, 60);
        document.add(image);
        Paragraph shopName = new Paragraph("PTIT Shop") ;
        shopName.setSpacingBefore(2);
        shopName.setSpacingAfter(20);
        document.add(shopName);



        vietnameseFont.setSize(20);
        vietnameseFont.setStyle(Font.NORMAL);


        Paragraph title = new Paragraph("Thống kê top " + limit + " bán chạy", vietnameseFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        vietnameseFont.setSize(14);
        Paragraph phoneNumber = new Paragraph("Thời gian: " + dateFormat.format(startDate) + " đến " + dateFormat.format(finishDate),vietnameseFont);
        title.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(phoneNumber);
        phoneNumber.setSpacingBefore(5);


        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setWidths(new float[] {1f,5f,1f});

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);

        document.close();
    }
}
