package com.doritech.microservice.CustomerService.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.microservice.CustomerService.Entity.DiwaliCustomer;
import com.doritech.microservice.CustomerService.Repository.DiwaliCustomerRepository;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class DiwaliCustomerPdfService {

	// Background image must be placed in src/main/resources/
	private static final String BACKGROUND_RESOURCE = "envelope-bg.png";

	// Original background image dimensions
	private static final float IMG_PX_W = 2576f;
	private static final float IMG_PX_H = 1304f;

	// Address block coordinates measured from the image's top-left
	private static final float ADDR_LEFT_PX = 450f;
	private static final float ADDR_RIGHT_PX = 1980f; // stops before the hanging lamp decoration
	private static final float ADDR_TOP_PX = 415f;
	private static final float ADDR_BOTTOM_PX = 905f; // stays above the divider line (~922px)

	// Single-envelope PDF dimensions
	private static final float SINGLE_W = 624f;
	private static final float SINGLE_H = SINGLE_W * IMG_PX_H / IMG_PX_W;

	// Multiple envelopes on A4: 10 per page (2 columns x 5 rows)
	private static final int PER_PAGE = 10;

	private static final float LIST_W = 245f;
	private static final float LIST_H = LIST_W * IMG_PX_H / IMG_PX_W;

	private static final float LIST_GAP = 8f;
	private static final float HORIZONTAL_GAP = 12f;

	private static final float BASE_WIDTH = 500f;
	private static final float BOLD_SIZE = 11f;
	private static final float NORMAL_SIZE = 10.5f;
	private static final float LEADING = 12f;

	// Auto-fit: lowest allowed font scale (fraction of the starting scale)
	private static final float MIN_SCALE_FACTOR = 0.55f;
	private static final float SCALE_STEP_FACTOR = 0.03f;

	@Autowired
	private DiwaliCustomerRepository diwaliCustomerRepository;

	// ---------------------------------------------------------
	// Generate PDF for one customer using database ID
	// ---------------------------------------------------------

	@Transactional(readOnly = true)
	public byte[] generateCustomerPdf(Long customerId) throws DocumentException, IOException {

		Document document = new Document(new Rectangle(SINGLE_W, SINGLE_H), 0, 0, 0, 0);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		boolean succeeded = false;

		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			DiwaliCustomer customer = diwaliCustomerRepository.findById(customerId).orElse(null);

			if (customer == null) {
				addMessage(document, "Customer not found!");
			} else {
				Image background = loadBackground();

				drawEnvelope(writer, background, customer, 0, SINGLE_H, SINGLE_W, SINGLE_H,
						SINGLE_W / BASE_WIDTH);
			}

			succeeded = true;

		} finally {
			closeSafely(document, succeeded);
		}

		return out.toByteArray();
	}

	// ---------------------------------------------------------
	// Generate PDF for all customers
	// Ten envelopes per A4 page
	// ---------------------------------------------------------

	@Transactional(readOnly = true)
	public byte[] generateCustomerListPdf() throws DocumentException, IOException {

		Document document = new Document(PageSize.A4, 0, 0, 0, 0);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		boolean succeeded = false;

		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			List<DiwaliCustomer> customers = diwaliCustomerRepository.findAll().stream()
					.filter(this::hasValidAddress).collect(Collectors.toList());

			if (customers.isEmpty()) {
				addMessage(document, "No customers with a valid address found.");
			} else {
				createMultipleEnvelopesOnPage(document, writer, customers);
			}

			succeeded = true;

		} finally {
			closeSafely(document, succeeded);
		}

		return out.toByteArray();
	}

	// ---------------------------------------------------------
	// Multiple envelopes layout
	// ---------------------------------------------------------

	private void createMultipleEnvelopesOnPage(Document document, PdfWriter writer,
			List<DiwaliCustomer> customers) throws DocumentException, IOException {

		Image background = loadBackground();

		float fontScale = LIST_W / BASE_WIDTH;
		Rectangle page = document.getPageSize();

		int columns = 2;
		int rows = 5;

		float totalWidth = columns * LIST_W + (columns - 1) * HORIZONTAL_GAP;
		float totalHeight = rows * LIST_H + (rows - 1) * LIST_GAP;

		float startX = (page.getWidth() - totalWidth) / 2f;
		float startTop = (page.getHeight() + totalHeight) / 2f;

		for (int i = 0; i < customers.size(); i++) {

			if (i > 0 && i % PER_PAGE == 0) {
				document.newPage();
			}

			DiwaliCustomer customer = customers.get(i);

			int slot = i % PER_PAGE;
			int row = slot / columns;
			int column = slot % columns;

			float x = startX + column * (LIST_W + HORIZONTAL_GAP);
			float top = startTop - row * (LIST_H + LIST_GAP);

			drawEnvelope(writer, background, customer, x, top, LIST_W, LIST_H, fontScale);
		}
	}

	// ---------------------------------------------------------
	// Draw envelope background and address (with auto-fit)
	// ---------------------------------------------------------

	private void drawEnvelope(PdfWriter writer, Image background, DiwaliCustomer customer, float x, float top,
			float width, float height, float fontScale) throws DocumentException {

		float px = width / IMG_PX_W;

		Image bg = Image.getInstance(background);
		bg.scaleAbsolute(width, height);
		bg.setAbsolutePosition(x, top - height);
		writer.getDirectContentUnder().addImage(bg);

		float llx = x + ADDR_LEFT_PX * px;
		float lly = top - ADDR_BOTTOM_PX * px;
		float urx = x + ADDR_RIGHT_PX * px;
		float ury = top - ADDR_TOP_PX * px;

		// Shrink the font until the whole address fits inside the box
		float scale = fontScale;
		float minScale = fontScale * MIN_SCALE_FACTOR;

		while (scale > minScale) {
			ColumnText probe = new ColumnText(writer.getDirectContent());
			probe.setSimpleColumn(llx, lly, urx, ury);
			probe.setLeading(LEADING * scale);
			probe.addText(buildAddressPhrase(customer, scale));

			// true = simulate only, nothing is drawn
			if (!ColumnText.hasMoreText(probe.go(true))) {
				break;
			}
			scale -= fontScale * SCALE_STEP_FACTOR;
		}

		ColumnText columnText = new ColumnText(writer.getDirectContent());
		columnText.setSimpleColumn(llx, lly, urx, ury);
		columnText.setLeading(LEADING * scale);
		columnText.addText(buildAddressPhrase(customer, scale));
		columnText.go();

		// Required because the page content was drawn directly.
		writer.setPageEmpty(false);
	}

	// ---------------------------------------------------------
	// Load envelope background image
	// ---------------------------------------------------------

	private Image loadBackground() throws IOException, BadElementException {

		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(BACKGROUND_RESOURCE)) {

			if (inputStream == null) {
				throw new IOException(BACKGROUND_RESOURCE + " not found in src/main/resources");
			}

			return Image.getInstance(inputStream.readAllBytes());
		}
	}

	// ---------------------------------------------------------
	// Build address from DiwaliCustomer fields (no email)
	// ---------------------------------------------------------

	private Phrase buildAddressPhrase(DiwaliCustomer customer, float fontScale) {

		Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, BOLD_SIZE * fontScale, BaseColor.BLACK);
		Font normal = FontFactory.getFont(FontFactory.HELVETICA, NORMAL_SIZE * fontScale, BaseColor.BLACK);

		Phrase phrase = new Phrase();

		appendLine(phrase, customer.getContactPersonName(), bold);
		appendLine(phrase, customer.getDesignation(), bold);
		appendLine(phrase, customer.getCompanyName(), bold);
		appendLine(phrase, customer.getDepartment(), bold);

		appendLine(phrase, customer.getBranchRegionalOffice(), normal);
		appendLine(phrase, customer.getHeadOfficeAddress(), normal);

		String city = getValueOrEmpty(customer.getCity()).trim();
		String state = getValueOrEmpty(customer.getState()).trim();
		String pincode = getValueOrEmpty(customer.getPincode()).trim();
		String country = getValueOrEmpty(customer.getCountry()).trim();

		String location = (city + " " + state).trim();

		if (!pincode.isEmpty()) {
			location += (location.isEmpty() ? "" : " - ") + pincode;
		}

		if (!country.isEmpty()) {
			location += (location.isEmpty() ? "" : ", ") + country;
		}

		// Skip the location line if the address already contains the pincode
		String headAddr = getValueOrEmpty(customer.getHeadOfficeAddress()).toUpperCase();
		boolean pincodeAlreadyInAddress = !pincode.isEmpty() && headAddr.contains(pincode.toUpperCase());

		if (!location.isEmpty() && !pincodeAlreadyInAddress) {
			appendLine(phrase, location, normal);
		}

		// Mobile and alternate number share one line
		String mobile = getValueOrEmpty(customer.getMobileNumber()).trim();
		String alternate = getValueOrEmpty(customer.getAlternateNumber()).trim();

		if (!mobile.isEmpty()) {
			phrase.add(new Chunk("Mobile No: " + mobile, bold));
		}

		if (!alternate.isEmpty()) {
			phrase.add(new Chunk((mobile.isEmpty() ? "" : "    |    ") + "Alternate No: " + alternate, normal));
		}

		if (!mobile.isEmpty() || !alternate.isEmpty()) {
			phrase.add(new Chunk("\n", normal));
		}

		return phrase;
	}

	// ---------------------------------------------------------
	// Address validation
	// ---------------------------------------------------------

	private boolean hasValidAddress(DiwaliCustomer customer) {
		return customer != null && !getValueOrEmpty(customer.getHeadOfficeAddress()).trim().isEmpty();
	}

	// ---------------------------------------------------------
	// Helper methods
	// ---------------------------------------------------------

	// Trims the value and removes stray leading commas/spaces from the data
	private void appendLine(Phrase phrase, String value, Font font) {

		if (value == null) {
			return;
		}

		String cleaned = value.trim().replaceAll("^[,\\s]+", "");

		if (!cleaned.isEmpty()) {
			phrase.add(new Chunk(cleaned + "\n", font));
		}
	}

	private String getValueOrEmpty(String value) {
		return value != null ? value : "";
	}

	private void addMessage(Document document, String message) throws DocumentException {

		Paragraph paragraph = new Paragraph(message);
		paragraph.setIndentationLeft(20);
		paragraph.setSpacingBefore(20);
		document.add(paragraph);
	}

	private void closeSafely(Document document, boolean succeeded) {

		try {
			document.close();
		} catch (RuntimeException exception) {
			if (succeeded) {
				throw exception;
			}
		}
	}
}