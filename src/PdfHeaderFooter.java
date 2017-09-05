import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * ClassName:PdfReportM1HeaderFooter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * 继承Pdf页面事件
 */
public class PdfHeaderFooter extends PdfPageEventHelper {

	/**
	 * 页眉
	 */
	public String header = "";

	/**
	 * 文档字体大小，页脚页眉最好和文本大小一致
	 */
	public int presentFontSize = 13;

	/**
	 * 文档页面大小，否则默认为A4纸张
	 */
	public Rectangle pageSize = PageSize.A4;

	// 模板
	public PdfTemplate total;

	// 基础字体对象
	public BaseFont bf = null;

	// 利用基础字体生成的字体对象，一般用于生成中文文字
	public Font fontDetail = null;

	/**
	 * 无参构造方法.
	 */
	public PdfHeaderFooter() {
		
	}
	
	/**
	 * 
	 * @param bf
	 * 		基础字体大小
	 * @param presentFontSize
	 * 		数据字体大小
	 * @param pageSize
	 * 		文档格式
	 */
	public PdfHeaderFooter(BaseFont bf, int presentFontSize, Rectangle pageSize) {
		this.bf = bf;
		this.presentFontSize = presentFontSize;
		this.pageSize = pageSize;
	}

	/**
	 *
	 * @param yeMei
	 *            页眉字符串
	 * @param presentFontSize
	 *            数据体字体大小
	 * @param pageSize
	 *            页面文档大小，A4，A5，A6横转翻转等Rectangle对象
	 */
	public PdfHeaderFooter(String yeMei, int presentFontSize, Rectangle pageSize) {
		this.header = yeMei;
		this.presentFontSize = presentFontSize;
		this.pageSize = pageSize;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setPresentFontSize(int presentFontSize) {
		this.presentFontSize = presentFontSize;
	}

	/**
	 * TODO 文档打开时创建模板
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(50, 50);// 共 页 的矩形的长宽高
	}

	/**
	 * TODO 关闭每页的时候，写入页眉，写入'第几页共'这几个字。
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			if (fontDetail == null) {
				fontDetail = new Font(bf, presentFontSize, Font.NORMAL);// 数据体字体
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 1.写入页眉
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(header, fontDetail),document.left(), document.top() + 20, 0);

		// 2.写入前半部分的 第 X页/共
		int pageS = writer.getPageNumber();
		String foot1 = "第 " + pageS + " 页/共";
		Phrase footer = new Phrase(foot1, fontDetail);

		// 3.计算前半部分的foot1的长度，后面好定位最后一部分的'Y页'这俩字的x轴坐标，字体长度也要计算进去 = len
		float len = bf.getWidthPoint(foot1, presentFontSize);

		// 4.拿到当前的PdfContentByte
		PdfContentByte cb = writer.getDirectContent();

		// 5.写入页脚1，x轴就是(右margin+左margin + right() -left()- len)/2.0F
		// ,y轴就是底边界-20,否则就贴边重叠到数据体里了就不是页脚了；注意Y轴是从下往上累加的，最上方的Top值是大于Bottom好几百开外的。
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,(document.rightMargin() + document.right() + document.leftMargin() - document.left() - len) / 2.0F + 20F,document.bottom() - 20, 0);

		// 6.写入页脚2的模板（就是页脚的Y页这俩字）添加到文档中，计算模板的和Y轴,X=(右边界-左边界 - 前半部分的len值)/2.0F + len ， y 轴和之前的保持一致，底边界-20
		cb.addTemplate(total,(document.rightMargin() + document.right() + document.leftMargin() - document.left()) / 2.0F + 20F,document.bottom() - 20); 
		// 调节模版显示的位置
	}

	/**
	 * TODO 关闭文档时，替换模板，完成整个页眉页脚组件
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		// 7.最后一步了，就是关闭文档的时候，将模板替换成实际的 Y 值,至此，page x of y 制作完毕，完美兼容各种文档size。
		total.beginText();
		total.setFontAndSize(bf, presentFontSize);// 生成的模版的字体、颜色
		String foot2 = " " + (writer.getPageNumber()) + " 页";
		total.showText(foot2);// 模版显示的内容
		total.endText();
		total.closePath();
	}
}