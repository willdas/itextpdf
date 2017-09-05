import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @ClassName: PdfDemoTest 
 * @Description: TODO(ItextPdf设置页码(第x页/共y页)) 
 * @author willdas
 * @date 2017年9月5日 下午5:55:43 
 *
 */
public class PdfDemoTest {
	
	public static void main(String[] args) {
		demo();
	}

	public static  void demo(){
		// 创建文档对象
		Document document = new Document();
		try {
			// 创建对写器
			FileOutputStream file = new FileOutputStream("F:/test.pdf");
			PdfWriter writer = PdfWriter.getInstance(document, file);
			//设置系统字体
			BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\simkai.ttf", BaseFont.IDENTITY_H, false);
			//设置文档字体
			Font font = new Font(bf, 13, Font.NORMAL);
			//设置页码
			setFooter(writer,bf);
			// 打开文档
			document.open();
			// 向文档添加内容
			for (int i = 0; i < 200; i++) {
				PdfPTable table = createTable1(font);
				document.add(table);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭文档
			document.close();
		}
	}
	//创建表格
	private static PdfPTable createTable1(Font font) throws Exception {
		PdfPTable table = new PdfPTable(10);
		table.setTotalWidth(new float[] { 50, 50, 50, 50, 50, 50, 50, 50, 50, 50});
		table.setLockedWidth(true);
		for (int i = 0; i < 10; i++) {
			PdfPCell pdfCell = new PdfPCell(new Paragraph(String.valueOf(i), font));
			table.addCell(pdfCell);
		}
		return table;
	}
	//设置页码
	private static void setFooter(PdfWriter writer,BaseFont bf) throws DocumentException, IOException{  
		PdfHeaderFooter headerFooter = new PdfHeaderFooter(bf,13,PageSize.A0); 
		writer.setPageEvent(headerFooter);  
	}  
}
