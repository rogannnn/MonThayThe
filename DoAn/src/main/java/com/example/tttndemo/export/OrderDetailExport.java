package com.example.tttndemo.export;

import com.example.tttndemo.entity.Order;
import com.example.tttndemo.entity.OrderDetail;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class OrderDetailExport {
    private Order order;
    private Font vietnameseFont;

    public OrderDetailExport(Order order) throws IOException {
        this.order = order;
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



        cell.setPhrase(new Phrase("Sản phẩm", vietnameseFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Đơn giá", vietnameseFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Số lượng", vietnameseFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Thành tiền", vietnameseFont));
        table.addCell(cell);


    }

    private void writeTableData(PdfPTable table,  NumberFormat formatter){
        PdfPCell cell = new PdfPCell();
        cell.setPadding(6);
        vietnameseFont.setSize(14);
        for(OrderDetail orderDetail : order.getOrderDetails()){
            cell.setPhrase(new Phrase(orderDetail.getProduct().getName()));
            table.addCell(cell);
            cell.setPhrase(new Phrase(formatter.format(orderDetail.getUnitPrice())));
            table.addCell(cell);
            cell.setPhrase(new Phrase(formatter.format(orderDetail.getQuantity())));
            table.addCell(cell);
            cell.setPhrase(new Phrase(formatter.format(orderDetail.getUnitPrice() * orderDetail.getQuantity()) + "đ"));
            table.addCell(cell);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
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


        Paragraph title = new Paragraph("Đơn hàng", vietnameseFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);


        vietnameseFont.setSize(14);
        Paragraph customer = new Paragraph("Khách hàng: " + order.getUser().getFullName(),vietnameseFont);
        title.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(customer);
        customer.setSpacingBefore(5);

        Paragraph phoneNumber = new Paragraph("Số điện thoại: " + order.getAddress().getPhone(),vietnameseFont);
        title.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(phoneNumber);
        phoneNumber.setSpacingBefore(5);

        Paragraph time = new Paragraph("Thời gian đặt: " + dateFormat.format(order.getDate()),vietnameseFont);
        title.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(time);
        time.setSpacingBefore(5);



        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[] {4.5f,2f,2f,2.5f});

        writeTableHeader(table);
        writeTableData(table, formatter);
        document.add(table);

        vietnameseFont.setSize(16);
        Paragraph total = new Paragraph("Tổng cộng: "+ formatter.format(order.getTotalPrice()) + "đ", vietnameseFont) ;
        total.setSpacingBefore(5);
        total.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(total);

        document.close();
    }
}
